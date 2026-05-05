package com.hustl.app.data.local

import android.content.Context
import androidx.room.*
import com.hustl.app.data.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE userId = :uid")
    suspend fun getUserById(uid: String): User?

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): User?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Update
    suspend fun updateUser(user: User)
}

@Dao
interface GigDao {
    @Query("SELECT * FROM gigs")
    fun getAllGigs(): Flow<List<Gig>>

    @Query("SELECT * FROM gigs WHERE category = :category")
    fun getGigsByCategory(category: String): Flow<List<Gig>>

    @Query("SELECT * FROM gigs WHERE gigId = :id")
    suspend fun getGigById(id: String): Gig?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGigs(gigs: List<Gig>)
}

@Dao
interface OrderDao {
    @Query("SELECT * FROM orders WHERE buyerId = :uid OR sellerId = :uid")
    fun getOrdersForUser(uid: String): Flow<List<Order>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: Order)

    @Update
    suspend fun updateOrder(order: Order)
}

@Dao
interface ChatDao {
    @Query("SELECT * FROM chats ORDER BY lastMessageTime DESC")
    fun getAllChats(): Flow<List<Chat>>

    @Query("SELECT * FROM chats WHERE chatId = :id")
    suspend fun getChatById(id: String): Chat?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChats(chats: List<Chat>)

    @Update
    suspend fun updateChat(chat: Chat)
}

@Dao
interface MessageDao {
    @Query("SELECT * FROM messages WHERE chatId = :chatId ORDER BY timestamp ASC")
    fun getMessagesForChat(chatId: String): Flow<List<Message>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: Message)
}

@Database(entities = [User::class, Gig::class, Order::class, Chat::class, Message::class, Review::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun gigDao(): GigDao
    abstract fun orderDao(): OrderDao
    abstract fun chatDao(): ChatDao
    abstract fun messageDao(): MessageDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "hustl_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
