package com.hustl.app.data.repository

import android.content.Context
import com.hustl.app.data.local.AppDatabase
import com.hustl.app.data.model.Order
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onStart

class OrderRepository(context: Context) {
    private val orderDao = AppDatabase.getDatabase(context).orderDao()

    suspend fun placeOrder(order: Order): Result<String> {
        return try {
            val orderId = "HU-${(1000..9999).random()}"
            val orderWithId = order.copy(orderId = orderId)
            orderDao.insertOrder(orderWithId)
            Result.success(orderId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getMyOrders(userId: String): Flow<List<Order>> {
        return orderDao.getOrdersForUser(userId).onStart {
            val current = orderDao.getOrdersForUser(userId).first()
            if (current.isEmpty() && userId == "user1") {
                getSampleOrders().forEach { orderDao.insertOrder(it) }
            }
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
