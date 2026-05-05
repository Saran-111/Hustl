package com.hustl.app.data.repository

import android.content.Context
import com.hustl.app.data.local.AppDatabase
import com.hustl.app.data.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepository(context: Context) {
    private val userDao = AppDatabase.getDatabase(context).userDao()
    private val prefs = context.getSharedPreferences("hustl_prefs", Context.MODE_PRIVATE)
    
    companion object {
        private var cachedUser: User? = null
    }

    val currentUser: User? get() = cachedUser

    suspend fun login(email: String, password: String): Result<User> = withContext(Dispatchers.IO) {
        try {
            val user = userDao.getUserByEmail(email)
            if (user != null && user.password == password) {
                persistLogin(user)
                Result.success(user)
            } else {
                Result.failure(Exception("Invalid email or password"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(name: String, email: String, password: String, role: String): Result<User> = withContext(Dispatchers.IO) {
        try {
            val existingUser = userDao.getUserByEmail(email)
            if (existingUser != null) {
                return@withContext Result.failure(Exception("Email already registered"))
            }
            
            val uid = "user_${System.currentTimeMillis()}"
            val user = User(
                userId = uid, 
                name = name, 
                email = email, 
                password = password, 
                role = role,
                location = "Mumbai, India",
                rating = 4.5,
                walletBalance = 500 // Start with some demo credits
            )
            userDao.insertUser(user)
            persistLogin(user)
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun persistLogin(user: User) {
        cachedUser = user
        prefs.edit().putString("logged_in_uid", user.userId).apply()
    }

    suspend fun checkSession(): Boolean = withContext(Dispatchers.IO) {
        val uid = prefs.getString("logged_in_uid", null)
        if (uid != null) {
            val user = userDao.getUserById(uid)
            if (user != null) {
                cachedUser = user
                return@withContext true
            }
        }
        false
    }

    suspend fun getUserProfile(uid: String): User = withContext(Dispatchers.IO) {
        userDao.getUserById(uid) ?: User()
    }

    suspend fun updateWalletBalance(amount: Int): Boolean = withContext(Dispatchers.IO) {
        val user = cachedUser ?: return@withContext false
        val updatedUser = user.copy(walletBalance = user.walletBalance + amount)
        userDao.updateUser(updatedUser)
        cachedUser = updatedUser
        true
    }

    suspend fun deductWalletBalance(amount: Int): Boolean = withContext(Dispatchers.IO) {
        val user = cachedUser ?: return@withContext false
        if (user.walletBalance < amount) return@withContext false
        val updatedUser = user.copy(walletBalance = user.walletBalance - amount)
        userDao.updateUser(updatedUser)
        cachedUser = updatedUser
        true
    }

    fun logout() {
        cachedUser = null
        prefs.edit().remove("logged_in_uid").apply()
    }

    fun isLoggedIn() = cachedUser != null
}
