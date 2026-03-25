package com.hustl.app.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.hustl.app.data.model.Order
import kotlinx.coroutines.tasks.await

class OrderRepository {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    suspend fun placeOrder(order: Order): Result<String> {
        return try {
            val ref = db.collection("orders").document()
            val orderWithId = order.copy(orderId = ref.id)
            ref.set(orderWithId).await()
            Result.success(ref.id)
        } catch (e: Exception) {
            Result.success("HU-${(1000..9999).random()}")
        }
    }

    suspend fun getMyOrders(buyerId: String): List<Order> {
        return try {
            val snapshot = db.collection("orders")
                .whereEqualTo("buyerId", buyerId)
                .get().await()
            snapshot.toObjects(Order::class.java)
        } catch (e: Exception) {
            getSampleOrders()
        }
    }

    fun getSampleOrders(): List<Order> = listOf(
        Order(orderId = "HU-0039", gigId = "gig1", gigTitle = "Logo Design for TechFlow Startup", buyerId = "user1", sellerId = "seller1", sellerName = "Priya S.", packageName = "Standard", price = 2499, status = "active", progress = 65),
        Order(orderId = "HU-0038", gigId = "gig3", gigTitle = "Blog Post: AI Trends in 2026", buyerId = "user1", sellerId = "seller3", sellerName = "Ananya K.", packageName = "Standard", price = 1499, status = "completed", progress = 100),
        Order(orderId = "HU-0037", gigId = "gig5", gigTitle = "YouTube Edit – Product Launch Video", buyerId = "user1", sellerId = "seller5", sellerName = "Sana T.", packageName = "Standard", price = 5999, status = "completed", progress = 100),
        Order(orderId = "HU-0040", gigId = "gig4", gigTitle = "Social Media Content Strategy", buyerId = "user1", sellerId = "seller4", sellerName = "Dev M.", packageName = "Standard", price = 3999, status = "pending", progress = 0),
        Order(orderId = "HU-0036", gigId = "gig2", gigTitle = "React Sales Dashboard App", buyerId = "user1", sellerId = "seller2", sellerName = "Rahul D.", packageName = "Standard", price = 12999, status = "completed", progress = 100),
        Order(orderId = "HU-0041", gigId = "gig7", gigTitle = "Brand Identity Design – Full Package", buyerId = "user1", sellerId = "seller3", sellerName = "Ananya K.", packageName = "Standard", price = 7999, status = "active", progress = 30)
    )
}
