package com.hustl.app.data.local;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\'\u0018\u0000 \r2\u00020\u0001:\u0001\rB\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H&J\b\u0010\u0005\u001a\u00020\u0006H&J\b\u0010\u0007\u001a\u00020\bH&J\b\u0010\t\u001a\u00020\nH&J\b\u0010\u000b\u001a\u00020\fH&\u00a8\u0006\u000e"}, d2 = {"Lcom/hustl/app/data/local/AppDatabase;", "Landroidx/room/RoomDatabase;", "()V", "chatDao", "Lcom/hustl/app/data/local/ChatDao;", "gigDao", "Lcom/hustl/app/data/local/GigDao;", "messageDao", "Lcom/hustl/app/data/local/MessageDao;", "orderDao", "Lcom/hustl/app/data/local/OrderDao;", "userDao", "Lcom/hustl/app/data/local/UserDao;", "Companion", "app_debug"})
@androidx.room.Database(entities = {com.hustl.app.data.model.User.class, com.hustl.app.data.model.Gig.class, com.hustl.app.data.model.Order.class, com.hustl.app.data.model.Chat.class, com.hustl.app.data.model.Message.class, com.hustl.app.data.model.Review.class}, version = 1)
@androidx.room.TypeConverters(value = {com.hustl.app.data.model.Converters.class})
public abstract class AppDatabase extends androidx.room.RoomDatabase {
    @kotlin.jvm.Volatile()
    @org.jetbrains.annotations.Nullable()
    private static volatile com.hustl.app.data.local.AppDatabase INSTANCE;
    @org.jetbrains.annotations.NotNull()
    public static final com.hustl.app.data.local.AppDatabase.Companion Companion = null;
    
    public AppDatabase() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.hustl.app.data.local.UserDao userDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.hustl.app.data.local.GigDao gigDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.hustl.app.data.local.OrderDao orderDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.hustl.app.data.local.ChatDao chatDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.hustl.app.data.local.MessageDao messageDao();
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u0007R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\b"}, d2 = {"Lcom/hustl/app/data/local/AppDatabase$Companion;", "", "()V", "INSTANCE", "Lcom/hustl/app/data/local/AppDatabase;", "getDatabase", "context", "Landroid/content/Context;", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.hustl.app.data.local.AppDatabase getDatabase(@org.jetbrains.annotations.NotNull()
        android.content.Context context) {
            return null;
        }
    }
}