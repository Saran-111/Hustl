package com.hustl.app.data.repository

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import com.hustl.app.data.model.Order
import com.hustl.app.data.model.toFirestoreMap
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.tasks.await

class OrderRepository(context: Context) {

    private val db = FirebaseFirestore.getInstance()
    private val ordersCol = db.collection("orders")

    // ─── Place Order ──────────────────────────────────────────────────────────

    suspend fun placeOrder(order: Order): Result<String> {
        return try {
            val docRef = ordersCol.document()
            val orderWithId = order.copy(orderId = docRef.id)
            docRef.set(orderWithId.toFirestoreMap()).await()
            Result.success(docRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ─── Get My Orders (buyer + hustlr merged) ────────────────────────────────
    // Firestore can't do OR across different fields, so we run two queries and merge

    fun getMyOrders(userId: String): Flow<List<Order>> {
        val asBuyer = ordersAsBuyer(userId)
        val asHustlr = ordersAsHustlr(userId)
        return asBuyer.combine(asHustlr) { buyerOrders, hustlrOrders ->
            (buyerOrders + hustlrOrders)
                .distinctBy { it.orderId }
                .sortedByDescending { it.createdAt }
        }
    }

    private fun ordersAsBuyer(userId: String): Flow<List<Order>> = callbackFlow {
        val listener = ordersCol
            .whereEqualTo("buyerId", userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) { trySend(emptyList()); return@addSnapshotListener }
                val orders = snapshot?.documents?.mapNotNull { it.toOrder() } ?: emptyList()
                trySend(orders)
            }
        awaitClose { listener.remove() }
    }

    private fun ordersAsHustlr(userId: String): Flow<List<Order>> = callbackFlow {
        val listener = ordersCol
            .whereEqualTo("sellerId", userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) { trySend(emptyList()); return@addSnapshotListener }
                val orders = snapshot?.documents?.mapNotNull { it.toOrder() } ?: emptyList()
                trySend(orders)
            }
        awaitClose { listener.remove() }
    }

    // ─── Update Order Status ──────────────────────────────────────────────────

    suspend fun updateOrderStatus(orderId: String, status: String): Result<Unit> {
        return try {
            ordersCol.document(orderId).update(
                mapOf(
                    "status" to status,
                    "updatedAt" to System.currentTimeMillis()
                )
            ).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun requestRevision(orderId: String, note: String): Result<Unit> {
        return try {
            ordersCol.document(orderId).update(
                mapOf(
                    "status" to "active",
                    "revisionNote" to note,
                    "updatedAt" to System.currentTimeMillis()
                )
            ).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun cancelOrder(orderId: String): Result<Unit> {
        return try {
            ordersCol.document(orderId).update(
                mapOf(
                    "status" to "cancelled",
                    "updatedAt" to System.currentTimeMillis()
                )
            ).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun markRatingGiven(orderId: String): Result<Unit> {
        return try {
            ordersCol.document(orderId).update("ratingGiven", true).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

// ─── DocumentSnapshot extension ───────────────────────────────────────────────

private fun com.google.firebase.firestore.DocumentSnapshot.toOrder(): Order? {
    val id = this.id.ifEmpty { return null }
    return try {
        Order(
            orderId = id,
            gigId = getString("gigId") ?: "",
            gigTitle = getString("gigTitle") ?: "",
            buyerId = getString("buyerId") ?: "",
            buyerName = getString("buyerName") ?: "",
            sellerId = getString("sellerId") ?: "",
            sellerName = getString("sellerName") ?: "",
            packageName = getString("packageName") ?: "",
            price = (getLong("price") ?: 0L).toInt(),
            status = getString("status") ?: "pending",
            requirements = getString("requirements") ?: "",
            progress = (getLong("progress") ?: 0L).toInt(),
            revisionNote = getString("revisionNote") ?: "",
            ratingGiven = getBoolean("ratingGiven") ?: false,
            createdAt = getLong("createdAt") ?: 0L,
            deliveryDate = getLong("deliveryDate") ?: 0L
        )
    } catch (_: Exception) { null }
}
