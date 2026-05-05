package com.hustl.app.data.local;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.hustl.app.data.model.Order;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class OrderDao_Impl implements OrderDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Order> __insertionAdapterOfOrder;

  private final EntityDeletionOrUpdateAdapter<Order> __updateAdapterOfOrder;

  public OrderDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfOrder = new EntityInsertionAdapter<Order>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `orders` (`orderId`,`gigId`,`gigTitle`,`buyerId`,`sellerId`,`sellerName`,`packageName`,`price`,`status`,`requirements`,`progress`,`createdAt`,`deliveryDate`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Order entity) {
        if (entity.getOrderId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getOrderId());
        }
        if (entity.getGigId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getGigId());
        }
        if (entity.getGigTitle() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getGigTitle());
        }
        if (entity.getBuyerId() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getBuyerId());
        }
        if (entity.getSellerId() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getSellerId());
        }
        if (entity.getSellerName() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getSellerName());
        }
        if (entity.getPackageName() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getPackageName());
        }
        statement.bindLong(8, entity.getPrice());
        if (entity.getStatus() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getStatus());
        }
        if (entity.getRequirements() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getRequirements());
        }
        statement.bindLong(11, entity.getProgress());
        statement.bindLong(12, entity.getCreatedAt());
        statement.bindLong(13, entity.getDeliveryDate());
      }
    };
    this.__updateAdapterOfOrder = new EntityDeletionOrUpdateAdapter<Order>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `orders` SET `orderId` = ?,`gigId` = ?,`gigTitle` = ?,`buyerId` = ?,`sellerId` = ?,`sellerName` = ?,`packageName` = ?,`price` = ?,`status` = ?,`requirements` = ?,`progress` = ?,`createdAt` = ?,`deliveryDate` = ? WHERE `orderId` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Order entity) {
        if (entity.getOrderId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getOrderId());
        }
        if (entity.getGigId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getGigId());
        }
        if (entity.getGigTitle() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getGigTitle());
        }
        if (entity.getBuyerId() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getBuyerId());
        }
        if (entity.getSellerId() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getSellerId());
        }
        if (entity.getSellerName() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getSellerName());
        }
        if (entity.getPackageName() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getPackageName());
        }
        statement.bindLong(8, entity.getPrice());
        if (entity.getStatus() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getStatus());
        }
        if (entity.getRequirements() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getRequirements());
        }
        statement.bindLong(11, entity.getProgress());
        statement.bindLong(12, entity.getCreatedAt());
        statement.bindLong(13, entity.getDeliveryDate());
        if (entity.getOrderId() == null) {
          statement.bindNull(14);
        } else {
          statement.bindString(14, entity.getOrderId());
        }
      }
    };
  }

  @Override
  public Object insertOrder(final Order order, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfOrder.insert(order);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateOrder(final Order order, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfOrder.handle(order);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<Order>> getOrdersForUser(final String uid) {
    final String _sql = "SELECT * FROM orders WHERE buyerId = ? OR sellerId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    if (uid == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, uid);
    }
    _argIndex = 2;
    if (uid == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, uid);
    }
    return CoroutinesRoom.createFlow(__db, false, new String[] {"orders"}, new Callable<List<Order>>() {
      @Override
      @NonNull
      public List<Order> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfOrderId = CursorUtil.getColumnIndexOrThrow(_cursor, "orderId");
          final int _cursorIndexOfGigId = CursorUtil.getColumnIndexOrThrow(_cursor, "gigId");
          final int _cursorIndexOfGigTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "gigTitle");
          final int _cursorIndexOfBuyerId = CursorUtil.getColumnIndexOrThrow(_cursor, "buyerId");
          final int _cursorIndexOfSellerId = CursorUtil.getColumnIndexOrThrow(_cursor, "sellerId");
          final int _cursorIndexOfSellerName = CursorUtil.getColumnIndexOrThrow(_cursor, "sellerName");
          final int _cursorIndexOfPackageName = CursorUtil.getColumnIndexOrThrow(_cursor, "packageName");
          final int _cursorIndexOfPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "price");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfRequirements = CursorUtil.getColumnIndexOrThrow(_cursor, "requirements");
          final int _cursorIndexOfProgress = CursorUtil.getColumnIndexOrThrow(_cursor, "progress");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfDeliveryDate = CursorUtil.getColumnIndexOrThrow(_cursor, "deliveryDate");
          final List<Order> _result = new ArrayList<Order>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Order _item;
            final String _tmpOrderId;
            if (_cursor.isNull(_cursorIndexOfOrderId)) {
              _tmpOrderId = null;
            } else {
              _tmpOrderId = _cursor.getString(_cursorIndexOfOrderId);
            }
            final String _tmpGigId;
            if (_cursor.isNull(_cursorIndexOfGigId)) {
              _tmpGigId = null;
            } else {
              _tmpGigId = _cursor.getString(_cursorIndexOfGigId);
            }
            final String _tmpGigTitle;
            if (_cursor.isNull(_cursorIndexOfGigTitle)) {
              _tmpGigTitle = null;
            } else {
              _tmpGigTitle = _cursor.getString(_cursorIndexOfGigTitle);
            }
            final String _tmpBuyerId;
            if (_cursor.isNull(_cursorIndexOfBuyerId)) {
              _tmpBuyerId = null;
            } else {
              _tmpBuyerId = _cursor.getString(_cursorIndexOfBuyerId);
            }
            final String _tmpSellerId;
            if (_cursor.isNull(_cursorIndexOfSellerId)) {
              _tmpSellerId = null;
            } else {
              _tmpSellerId = _cursor.getString(_cursorIndexOfSellerId);
            }
            final String _tmpSellerName;
            if (_cursor.isNull(_cursorIndexOfSellerName)) {
              _tmpSellerName = null;
            } else {
              _tmpSellerName = _cursor.getString(_cursorIndexOfSellerName);
            }
            final String _tmpPackageName;
            if (_cursor.isNull(_cursorIndexOfPackageName)) {
              _tmpPackageName = null;
            } else {
              _tmpPackageName = _cursor.getString(_cursorIndexOfPackageName);
            }
            final int _tmpPrice;
            _tmpPrice = _cursor.getInt(_cursorIndexOfPrice);
            final String _tmpStatus;
            if (_cursor.isNull(_cursorIndexOfStatus)) {
              _tmpStatus = null;
            } else {
              _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            }
            final String _tmpRequirements;
            if (_cursor.isNull(_cursorIndexOfRequirements)) {
              _tmpRequirements = null;
            } else {
              _tmpRequirements = _cursor.getString(_cursorIndexOfRequirements);
            }
            final int _tmpProgress;
            _tmpProgress = _cursor.getInt(_cursorIndexOfProgress);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpDeliveryDate;
            _tmpDeliveryDate = _cursor.getLong(_cursorIndexOfDeliveryDate);
            _item = new Order(_tmpOrderId,_tmpGigId,_tmpGigTitle,_tmpBuyerId,_tmpSellerId,_tmpSellerName,_tmpPackageName,_tmpPrice,_tmpStatus,_tmpRequirements,_tmpProgress,_tmpCreatedAt,_tmpDeliveryDate);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
