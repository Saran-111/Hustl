package com.hustl.app.data.local;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile UserDao _userDao;

  private volatile GigDao _gigDao;

  private volatile OrderDao _orderDao;

  private volatile ChatDao _chatDao;

  private volatile MessageDao _messageDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `users` (`userId` TEXT NOT NULL, `name` TEXT NOT NULL, `email` TEXT NOT NULL, `password` TEXT NOT NULL, `role` TEXT NOT NULL, `bio` TEXT NOT NULL, `profileImageUrl` TEXT NOT NULL, `rating` REAL NOT NULL, `totalOrders` INTEGER NOT NULL, `location` TEXT NOT NULL, `walletBalance` INTEGER NOT NULL, `createdAt` INTEGER NOT NULL, PRIMARY KEY(`userId`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `gigs` (`gigId` TEXT NOT NULL, `sellerId` TEXT NOT NULL, `sellerName` TEXT NOT NULL, `sellerImageUrl` TEXT NOT NULL, `title` TEXT NOT NULL, `description` TEXT NOT NULL, `category` TEXT NOT NULL, `tags` TEXT NOT NULL, `imageUrl` TEXT NOT NULL, `rating` REAL NOT NULL, `reviewCount` INTEGER NOT NULL, `packages` TEXT NOT NULL, `createdAt` INTEGER NOT NULL, `isActive` INTEGER NOT NULL, PRIMARY KEY(`gigId`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `orders` (`orderId` TEXT NOT NULL, `gigId` TEXT NOT NULL, `gigTitle` TEXT NOT NULL, `buyerId` TEXT NOT NULL, `sellerId` TEXT NOT NULL, `sellerName` TEXT NOT NULL, `packageName` TEXT NOT NULL, `price` INTEGER NOT NULL, `status` TEXT NOT NULL, `requirements` TEXT NOT NULL, `progress` INTEGER NOT NULL, `createdAt` INTEGER NOT NULL, `deliveryDate` INTEGER NOT NULL, PRIMARY KEY(`orderId`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `chats` (`chatId` TEXT NOT NULL, `participants` TEXT NOT NULL, `participantNames` TEXT NOT NULL, `gigId` TEXT NOT NULL, `gigTitle` TEXT NOT NULL, `lastMessage` TEXT NOT NULL, `lastMessageTime` INTEGER NOT NULL, `unreadCount` INTEGER NOT NULL, PRIMARY KEY(`chatId`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `messages` (`messageId` TEXT NOT NULL, `chatId` TEXT NOT NULL, `senderId` TEXT NOT NULL, `senderName` TEXT NOT NULL, `text` TEXT NOT NULL, `timestamp` INTEGER NOT NULL, `isRead` INTEGER NOT NULL, PRIMARY KEY(`messageId`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `reviews` (`reviewId` TEXT NOT NULL, `gigId` TEXT NOT NULL, `orderId` TEXT NOT NULL, `buyerId` TEXT NOT NULL, `buyerName` TEXT NOT NULL, `rating` REAL NOT NULL, `comment` TEXT NOT NULL, `createdAt` INTEGER NOT NULL, PRIMARY KEY(`reviewId`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '1bd5ad890513c3a4e35bb1075fa85cb9')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `users`");
        db.execSQL("DROP TABLE IF EXISTS `gigs`");
        db.execSQL("DROP TABLE IF EXISTS `orders`");
        db.execSQL("DROP TABLE IF EXISTS `chats`");
        db.execSQL("DROP TABLE IF EXISTS `messages`");
        db.execSQL("DROP TABLE IF EXISTS `reviews`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsUsers = new HashMap<String, TableInfo.Column>(12);
        _columnsUsers.put("userId", new TableInfo.Column("userId", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("email", new TableInfo.Column("email", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("password", new TableInfo.Column("password", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("role", new TableInfo.Column("role", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("bio", new TableInfo.Column("bio", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("profileImageUrl", new TableInfo.Column("profileImageUrl", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("rating", new TableInfo.Column("rating", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("totalOrders", new TableInfo.Column("totalOrders", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("location", new TableInfo.Column("location", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("walletBalance", new TableInfo.Column("walletBalance", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysUsers = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesUsers = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoUsers = new TableInfo("users", _columnsUsers, _foreignKeysUsers, _indicesUsers);
        final TableInfo _existingUsers = TableInfo.read(db, "users");
        if (!_infoUsers.equals(_existingUsers)) {
          return new RoomOpenHelper.ValidationResult(false, "users(com.hustl.app.data.model.User).\n"
                  + " Expected:\n" + _infoUsers + "\n"
                  + " Found:\n" + _existingUsers);
        }
        final HashMap<String, TableInfo.Column> _columnsGigs = new HashMap<String, TableInfo.Column>(14);
        _columnsGigs.put("gigId", new TableInfo.Column("gigId", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGigs.put("sellerId", new TableInfo.Column("sellerId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGigs.put("sellerName", new TableInfo.Column("sellerName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGigs.put("sellerImageUrl", new TableInfo.Column("sellerImageUrl", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGigs.put("title", new TableInfo.Column("title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGigs.put("description", new TableInfo.Column("description", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGigs.put("category", new TableInfo.Column("category", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGigs.put("tags", new TableInfo.Column("tags", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGigs.put("imageUrl", new TableInfo.Column("imageUrl", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGigs.put("rating", new TableInfo.Column("rating", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGigs.put("reviewCount", new TableInfo.Column("reviewCount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGigs.put("packages", new TableInfo.Column("packages", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGigs.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGigs.put("isActive", new TableInfo.Column("isActive", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysGigs = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesGigs = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoGigs = new TableInfo("gigs", _columnsGigs, _foreignKeysGigs, _indicesGigs);
        final TableInfo _existingGigs = TableInfo.read(db, "gigs");
        if (!_infoGigs.equals(_existingGigs)) {
          return new RoomOpenHelper.ValidationResult(false, "gigs(com.hustl.app.data.model.Gig).\n"
                  + " Expected:\n" + _infoGigs + "\n"
                  + " Found:\n" + _existingGigs);
        }
        final HashMap<String, TableInfo.Column> _columnsOrders = new HashMap<String, TableInfo.Column>(13);
        _columnsOrders.put("orderId", new TableInfo.Column("orderId", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsOrders.put("gigId", new TableInfo.Column("gigId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsOrders.put("gigTitle", new TableInfo.Column("gigTitle", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsOrders.put("buyerId", new TableInfo.Column("buyerId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsOrders.put("sellerId", new TableInfo.Column("sellerId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsOrders.put("sellerName", new TableInfo.Column("sellerName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsOrders.put("packageName", new TableInfo.Column("packageName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsOrders.put("price", new TableInfo.Column("price", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsOrders.put("status", new TableInfo.Column("status", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsOrders.put("requirements", new TableInfo.Column("requirements", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsOrders.put("progress", new TableInfo.Column("progress", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsOrders.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsOrders.put("deliveryDate", new TableInfo.Column("deliveryDate", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysOrders = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesOrders = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoOrders = new TableInfo("orders", _columnsOrders, _foreignKeysOrders, _indicesOrders);
        final TableInfo _existingOrders = TableInfo.read(db, "orders");
        if (!_infoOrders.equals(_existingOrders)) {
          return new RoomOpenHelper.ValidationResult(false, "orders(com.hustl.app.data.model.Order).\n"
                  + " Expected:\n" + _infoOrders + "\n"
                  + " Found:\n" + _existingOrders);
        }
        final HashMap<String, TableInfo.Column> _columnsChats = new HashMap<String, TableInfo.Column>(8);
        _columnsChats.put("chatId", new TableInfo.Column("chatId", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChats.put("participants", new TableInfo.Column("participants", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChats.put("participantNames", new TableInfo.Column("participantNames", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChats.put("gigId", new TableInfo.Column("gigId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChats.put("gigTitle", new TableInfo.Column("gigTitle", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChats.put("lastMessage", new TableInfo.Column("lastMessage", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChats.put("lastMessageTime", new TableInfo.Column("lastMessageTime", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChats.put("unreadCount", new TableInfo.Column("unreadCount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysChats = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesChats = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoChats = new TableInfo("chats", _columnsChats, _foreignKeysChats, _indicesChats);
        final TableInfo _existingChats = TableInfo.read(db, "chats");
        if (!_infoChats.equals(_existingChats)) {
          return new RoomOpenHelper.ValidationResult(false, "chats(com.hustl.app.data.model.Chat).\n"
                  + " Expected:\n" + _infoChats + "\n"
                  + " Found:\n" + _existingChats);
        }
        final HashMap<String, TableInfo.Column> _columnsMessages = new HashMap<String, TableInfo.Column>(7);
        _columnsMessages.put("messageId", new TableInfo.Column("messageId", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMessages.put("chatId", new TableInfo.Column("chatId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMessages.put("senderId", new TableInfo.Column("senderId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMessages.put("senderName", new TableInfo.Column("senderName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMessages.put("text", new TableInfo.Column("text", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMessages.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMessages.put("isRead", new TableInfo.Column("isRead", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMessages = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesMessages = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoMessages = new TableInfo("messages", _columnsMessages, _foreignKeysMessages, _indicesMessages);
        final TableInfo _existingMessages = TableInfo.read(db, "messages");
        if (!_infoMessages.equals(_existingMessages)) {
          return new RoomOpenHelper.ValidationResult(false, "messages(com.hustl.app.data.model.Message).\n"
                  + " Expected:\n" + _infoMessages + "\n"
                  + " Found:\n" + _existingMessages);
        }
        final HashMap<String, TableInfo.Column> _columnsReviews = new HashMap<String, TableInfo.Column>(8);
        _columnsReviews.put("reviewId", new TableInfo.Column("reviewId", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReviews.put("gigId", new TableInfo.Column("gigId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReviews.put("orderId", new TableInfo.Column("orderId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReviews.put("buyerId", new TableInfo.Column("buyerId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReviews.put("buyerName", new TableInfo.Column("buyerName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReviews.put("rating", new TableInfo.Column("rating", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReviews.put("comment", new TableInfo.Column("comment", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReviews.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysReviews = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesReviews = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoReviews = new TableInfo("reviews", _columnsReviews, _foreignKeysReviews, _indicesReviews);
        final TableInfo _existingReviews = TableInfo.read(db, "reviews");
        if (!_infoReviews.equals(_existingReviews)) {
          return new RoomOpenHelper.ValidationResult(false, "reviews(com.hustl.app.data.model.Review).\n"
                  + " Expected:\n" + _infoReviews + "\n"
                  + " Found:\n" + _existingReviews);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "1bd5ad890513c3a4e35bb1075fa85cb9", "de7c2096ecadb6b3a199d0036ee8fe4c");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "users","gigs","orders","chats","messages","reviews");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `users`");
      _db.execSQL("DELETE FROM `gigs`");
      _db.execSQL("DELETE FROM `orders`");
      _db.execSQL("DELETE FROM `chats`");
      _db.execSQL("DELETE FROM `messages`");
      _db.execSQL("DELETE FROM `reviews`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(UserDao.class, UserDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(GigDao.class, GigDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(OrderDao.class, OrderDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ChatDao.class, ChatDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(MessageDao.class, MessageDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public UserDao userDao() {
    if (_userDao != null) {
      return _userDao;
    } else {
      synchronized(this) {
        if(_userDao == null) {
          _userDao = new UserDao_Impl(this);
        }
        return _userDao;
      }
    }
  }

  @Override
  public GigDao gigDao() {
    if (_gigDao != null) {
      return _gigDao;
    } else {
      synchronized(this) {
        if(_gigDao == null) {
          _gigDao = new GigDao_Impl(this);
        }
        return _gigDao;
      }
    }
  }

  @Override
  public OrderDao orderDao() {
    if (_orderDao != null) {
      return _orderDao;
    } else {
      synchronized(this) {
        if(_orderDao == null) {
          _orderDao = new OrderDao_Impl(this);
        }
        return _orderDao;
      }
    }
  }

  @Override
  public ChatDao chatDao() {
    if (_chatDao != null) {
      return _chatDao;
    } else {
      synchronized(this) {
        if(_chatDao == null) {
          _chatDao = new ChatDao_Impl(this);
        }
        return _chatDao;
      }
    }
  }

  @Override
  public MessageDao messageDao() {
    if (_messageDao != null) {
      return _messageDao;
    } else {
      synchronized(this) {
        if(_messageDao == null) {
          _messageDao = new MessageDao_Impl(this);
        }
        return _messageDao;
      }
    }
  }
}
