package com.hustl.app.data.repository

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.hustl.app.data.model.Gig
import com.hustl.app.data.model.Review
import com.hustl.app.data.model.toFirestoreMap
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class GigRepository() {

    private val db = FirebaseFirestore.getInstance()
    private val gigsCol = db.collection("gigs")
    private val reviewsCol = db.collection("reviews")

    // ─── Browse / Search ──────────────────────────────────────────────────────

    fun getAllGigs(): Flow<List<Gig>> = callbackFlow {
        val listener = gigsCol
            .whereEqualTo("isActive", true)
            .addSnapshotListener { snapshot, error ->
                if (error != null) { trySend(emptyList()); return@addSnapshotListener }
                val gigs = snapshot?.documents
                    ?.mapNotNull { it.toGig() }
                    ?.sortedByDescending { it.createdAt }
                    ?: emptyList()
                trySend(gigs)
            }
        awaitClose { listener.remove() }
    }

    fun getGigsByCategory(category: String): Flow<List<Gig>> {
        if (category == "All") return getAllGigs()
        return callbackFlow {
            val listener = gigsCol
                .whereEqualTo("isActive", true)
                .whereEqualTo("category", category)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) { trySend(emptyList()); return@addSnapshotListener }
                    val gigs = snapshot?.documents
                        ?.mapNotNull { it.toGig() }
                        ?.sortedByDescending { it.createdAt }
                        ?: emptyList()
                    trySend(gigs)
                }
            awaitClose { listener.remove() }
        }
    }

    fun getGigsByHustlr(sellerId: String): Flow<List<Gig>> = callbackFlow {
        val listener = gigsCol
            .whereEqualTo("sellerId", sellerId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) { trySend(emptyList()); return@addSnapshotListener }
                val gigs = snapshot?.documents
                    ?.mapNotNull { it.toGig() }
                    ?.sortedByDescending { it.createdAt }
                    ?: emptyList()
                trySend(gigs)
            }
        awaitClose { listener.remove() }
    }

    suspend fun getGigById(gigId: String): Gig? {
        return try {
            val doc = gigsCol.document(gigId).get().await()
            doc.toGig()
        } catch (_: Exception) { null }
    }

    // ─── Create / Edit / Delete ───────────────────────────────────────────────

    suspend fun createGig(gig: Gig): Result<String> {
        return try {
            val docRef = gigsCol.document()
            val gigWithId = gig.copy(gigId = docRef.id)
            docRef.set(gigWithId.toFirestoreMap()).await()
            Result.success(docRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateGig(gig: Gig): Result<Unit> {
        return try {
            gigsCol.document(gig.gigId).set(gig.toFirestoreMap()).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteGig(gigId: String): Result<Unit> {
        return try {
            gigsCol.document(gigId).update("isActive", false).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ─── Reviews ──────────────────────────────────────────────────────────────

    fun getReviewsForGig(gigId: String): Flow<List<Review>> = callbackFlow {
        val listener = reviewsCol
            .whereEqualTo("gigId", gigId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) { trySend(emptyList()); return@addSnapshotListener }
                val reviews = snapshot?.documents?.mapNotNull { doc ->
                    Review(
                        reviewId = doc.id,
                        gigId = doc.getString("gigId") ?: "",
                        orderId = doc.getString("orderId") ?: "",
                        buyerId = doc.getString("buyerId") ?: "",
                        buyerName = doc.getString("buyerName") ?: "",
                        reviewedUserId = doc.getString("reviewedUserId") ?: "",
                        rating = (doc.getDouble("rating") ?: 5.0).toFloat(),
                        comment = doc.getString("comment") ?: "",
                        createdAt = doc.getLong("createdAt") ?: 0L
                    )
                }?.sortedByDescending { it.createdAt } ?: emptyList()
                trySend(reviews)
            }
        awaitClose { listener.remove() }
    }

    // Kept for backward compatibility (GigDetailActivity calls this)
    fun getReviews(unused: String): List<Review> = emptyList()

    fun getReviewsForUser(userId: String): Flow<List<Review>> = callbackFlow {
        val listener = reviewsCol
            .whereEqualTo("reviewedUserId", userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) { trySend(emptyList()); return@addSnapshotListener }
                val reviews = snapshot?.documents?.mapNotNull { doc ->
                    Review(
                        reviewId = doc.id,
                        gigId = doc.getString("gigId") ?: "",
                        orderId = doc.getString("orderId") ?: "",
                        buyerId = doc.getString("buyerId") ?: "",
                        buyerName = doc.getString("buyerName") ?: "",
                        reviewedUserId = doc.getString("reviewedUserId") ?: "",
                        rating = (doc.getDouble("rating") ?: 5.0).toFloat(),
                        comment = doc.getString("comment") ?: "",
                        createdAt = doc.getLong("createdAt") ?: 0L
                    )
                }?.sortedByDescending { it.createdAt } ?: emptyList()
                trySend(reviews)
            }
        awaitClose { listener.remove() }
    }

    suspend fun addReview(review: Review): Result<Unit> {
        return try {
            val batch = db.batch()
            val reviewRef = reviewsCol.document()
            val reviewWithId = review.copy(reviewId = reviewRef.id)
            
            // 1. Add review
            batch.set(reviewRef, hashMapOf(
                "reviewId" to reviewWithId.reviewId,
                "gigId" to reviewWithId.gigId,
                "orderId" to reviewWithId.orderId,
                "buyerId" to reviewWithId.buyerId,
                "buyerName" to reviewWithId.buyerName,
                "reviewedUserId" to reviewWithId.reviewedUserId,
                "rating" to reviewWithId.rating.toDouble(),
                "comment" to reviewWithId.comment,
                "createdAt" to reviewWithId.createdAt
            ))

            // 2. Fetch all reviews to calculate new average
            val allReviews = reviewsCol.whereEqualTo("reviewedUserId", reviewWithId.reviewedUserId).get().await()
            val ratings = allReviews.documents.mapNotNull { it.getDouble("rating") }.toMutableList()
            ratings.add(reviewWithId.rating.toDouble())
            
            val newAvg = ratings.average()
            val total = ratings.size

            // 3. Update Hustlr profile
            val userRef = db.collection("users").document(reviewWithId.reviewedUserId)
            batch.set(userRef, mapOf("rating" to newAvg, "reviewCount" to total), SetOptions.merge())

            // 4. Update the specific Gig
            val gigRef = gigsCol.document(reviewWithId.gigId)
            batch.set(gigRef, mapOf("rating" to newAvg, "reviewCount" to total), SetOptions.merge())

            batch.commit().await()
            
            // Background: Update other gigs for this hustlr (less critical)
            recalculateOtherGigs(reviewWithId.reviewedUserId, newAvg, total)
            
            Result.success(Unit)
        } catch (e: Exception) {
            android.util.Log.e("Hustl", "Review batch failed: ${e.message}")
            Result.failure(e)
        }
    }

    private suspend fun recalculateOtherGigs(hustlrId: String, newAvg: Double, total: Int) {
        try {
            val gigs = gigsCol.whereEqualTo("sellerId", hustlrId).get().await()
            for (doc in gigs.documents) {
                doc.reference.set(mapOf("rating" to newAvg, "reviewCount" to total), SetOptions.merge()).await()
            }
        } catch (_: Exception) {}
    }


}

// ─── DocumentSnapshot extension ───────────────────────────────────────────────

@Suppress("UNCHECKED_CAST")
private fun com.google.firebase.firestore.DocumentSnapshot.toGig(): Gig? {
    val id = this.id.ifEmpty { return null }
    return try {
        val tags = get("tags") as? List<String> ?: emptyList()
        Gig(
            gigId = id,
            sellerId = getString("sellerId") ?: "",
            sellerName = getString("sellerName") ?: "",
            sellerImageUrl = getString("sellerImageUrl") ?: "",
            title = getString("title") ?: "",
            description = getString("description") ?: "",
            category = getString("category") ?: "",
            tags = tags,
            imageUrl = getString("imageUrl") ?: "",
            rating = getDouble("rating") ?: 0.0,
            reviewCount = (getLong("reviewCount") ?: 0L).toInt(),
            minPrice = (getLong("minPrice") ?: 0L).toInt(),
            maxPrice = (getLong("maxPrice") ?: 0L).toInt(),
            deliveryDays = (getLong("deliveryDays") ?: 3L).toInt(),
            revisions = (getLong("revisions") ?: 1L).toInt(),
            createdAt = getLong("createdAt") ?: 0L,
            isActive = getBoolean("isActive") ?: true
        )
    } catch (_: Exception) { null }
}
