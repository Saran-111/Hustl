package com.hustl.app.data.local;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.hustl.app.data.model.Converters;
import com.hustl.app.data.model.Gig;
import com.hustl.app.data.model.GigPackage;
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
public final class GigDao_Impl implements GigDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Gig> __insertionAdapterOfGig;

  private final Converters __converters = new Converters();

  public GigDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfGig = new EntityInsertionAdapter<Gig>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `gigs` (`gigId`,`sellerId`,`sellerName`,`sellerImageUrl`,`title`,`description`,`category`,`tags`,`imageUrl`,`rating`,`reviewCount`,`packages`,`createdAt`,`isActive`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Gig entity) {
        if (entity.getGigId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getGigId());
        }
        if (entity.getSellerId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getSellerId());
        }
        if (entity.getSellerName() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getSellerName());
        }
        if (entity.getSellerImageUrl() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getSellerImageUrl());
        }
        if (entity.getTitle() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getTitle());
        }
        if (entity.getDescription() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getDescription());
        }
        if (entity.getCategory() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getCategory());
        }
        final String _tmp = __converters.fromStringList(entity.getTags());
        if (_tmp == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, _tmp);
        }
        if (entity.getImageUrl() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getImageUrl());
        }
        statement.bindDouble(10, entity.getRating());
        statement.bindLong(11, entity.getReviewCount());
        final String _tmp_1 = __converters.fromPackageList(entity.getPackages());
        if (_tmp_1 == null) {
          statement.bindNull(12);
        } else {
          statement.bindString(12, _tmp_1);
        }
        statement.bindLong(13, entity.getCreatedAt());
        final int _tmp_2 = entity.isActive() ? 1 : 0;
        statement.bindLong(14, _tmp_2);
      }
    };
  }

  @Override
  public Object insertGigs(final List<Gig> gigs, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfGig.insert(gigs);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<Gig>> getAllGigs() {
    final String _sql = "SELECT * FROM gigs";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"gigs"}, new Callable<List<Gig>>() {
      @Override
      @NonNull
      public List<Gig> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfGigId = CursorUtil.getColumnIndexOrThrow(_cursor, "gigId");
          final int _cursorIndexOfSellerId = CursorUtil.getColumnIndexOrThrow(_cursor, "sellerId");
          final int _cursorIndexOfSellerName = CursorUtil.getColumnIndexOrThrow(_cursor, "sellerName");
          final int _cursorIndexOfSellerImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "sellerImageUrl");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfTags = CursorUtil.getColumnIndexOrThrow(_cursor, "tags");
          final int _cursorIndexOfImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "imageUrl");
          final int _cursorIndexOfRating = CursorUtil.getColumnIndexOrThrow(_cursor, "rating");
          final int _cursorIndexOfReviewCount = CursorUtil.getColumnIndexOrThrow(_cursor, "reviewCount");
          final int _cursorIndexOfPackages = CursorUtil.getColumnIndexOrThrow(_cursor, "packages");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final List<Gig> _result = new ArrayList<Gig>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Gig _item;
            final String _tmpGigId;
            if (_cursor.isNull(_cursorIndexOfGigId)) {
              _tmpGigId = null;
            } else {
              _tmpGigId = _cursor.getString(_cursorIndexOfGigId);
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
            final String _tmpSellerImageUrl;
            if (_cursor.isNull(_cursorIndexOfSellerImageUrl)) {
              _tmpSellerImageUrl = null;
            } else {
              _tmpSellerImageUrl = _cursor.getString(_cursorIndexOfSellerImageUrl);
            }
            final String _tmpTitle;
            if (_cursor.isNull(_cursorIndexOfTitle)) {
              _tmpTitle = null;
            } else {
              _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            }
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final String _tmpCategory;
            if (_cursor.isNull(_cursorIndexOfCategory)) {
              _tmpCategory = null;
            } else {
              _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            }
            final List<String> _tmpTags;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfTags)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfTags);
            }
            _tmpTags = __converters.toStringList(_tmp);
            final String _tmpImageUrl;
            if (_cursor.isNull(_cursorIndexOfImageUrl)) {
              _tmpImageUrl = null;
            } else {
              _tmpImageUrl = _cursor.getString(_cursorIndexOfImageUrl);
            }
            final double _tmpRating;
            _tmpRating = _cursor.getDouble(_cursorIndexOfRating);
            final int _tmpReviewCount;
            _tmpReviewCount = _cursor.getInt(_cursorIndexOfReviewCount);
            final List<GigPackage> _tmpPackages;
            final String _tmp_1;
            if (_cursor.isNull(_cursorIndexOfPackages)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getString(_cursorIndexOfPackages);
            }
            _tmpPackages = __converters.toPackageList(_tmp_1);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final boolean _tmpIsActive;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp_2 != 0;
            _item = new Gig(_tmpGigId,_tmpSellerId,_tmpSellerName,_tmpSellerImageUrl,_tmpTitle,_tmpDescription,_tmpCategory,_tmpTags,_tmpImageUrl,_tmpRating,_tmpReviewCount,_tmpPackages,_tmpCreatedAt,_tmpIsActive);
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

  @Override
  public Flow<List<Gig>> getGigsByCategory(final String category) {
    final String _sql = "SELECT * FROM gigs WHERE category = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (category == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, category);
    }
    return CoroutinesRoom.createFlow(__db, false, new String[] {"gigs"}, new Callable<List<Gig>>() {
      @Override
      @NonNull
      public List<Gig> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfGigId = CursorUtil.getColumnIndexOrThrow(_cursor, "gigId");
          final int _cursorIndexOfSellerId = CursorUtil.getColumnIndexOrThrow(_cursor, "sellerId");
          final int _cursorIndexOfSellerName = CursorUtil.getColumnIndexOrThrow(_cursor, "sellerName");
          final int _cursorIndexOfSellerImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "sellerImageUrl");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfTags = CursorUtil.getColumnIndexOrThrow(_cursor, "tags");
          final int _cursorIndexOfImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "imageUrl");
          final int _cursorIndexOfRating = CursorUtil.getColumnIndexOrThrow(_cursor, "rating");
          final int _cursorIndexOfReviewCount = CursorUtil.getColumnIndexOrThrow(_cursor, "reviewCount");
          final int _cursorIndexOfPackages = CursorUtil.getColumnIndexOrThrow(_cursor, "packages");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final List<Gig> _result = new ArrayList<Gig>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Gig _item;
            final String _tmpGigId;
            if (_cursor.isNull(_cursorIndexOfGigId)) {
              _tmpGigId = null;
            } else {
              _tmpGigId = _cursor.getString(_cursorIndexOfGigId);
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
            final String _tmpSellerImageUrl;
            if (_cursor.isNull(_cursorIndexOfSellerImageUrl)) {
              _tmpSellerImageUrl = null;
            } else {
              _tmpSellerImageUrl = _cursor.getString(_cursorIndexOfSellerImageUrl);
            }
            final String _tmpTitle;
            if (_cursor.isNull(_cursorIndexOfTitle)) {
              _tmpTitle = null;
            } else {
              _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            }
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final String _tmpCategory;
            if (_cursor.isNull(_cursorIndexOfCategory)) {
              _tmpCategory = null;
            } else {
              _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            }
            final List<String> _tmpTags;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfTags)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfTags);
            }
            _tmpTags = __converters.toStringList(_tmp);
            final String _tmpImageUrl;
            if (_cursor.isNull(_cursorIndexOfImageUrl)) {
              _tmpImageUrl = null;
            } else {
              _tmpImageUrl = _cursor.getString(_cursorIndexOfImageUrl);
            }
            final double _tmpRating;
            _tmpRating = _cursor.getDouble(_cursorIndexOfRating);
            final int _tmpReviewCount;
            _tmpReviewCount = _cursor.getInt(_cursorIndexOfReviewCount);
            final List<GigPackage> _tmpPackages;
            final String _tmp_1;
            if (_cursor.isNull(_cursorIndexOfPackages)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getString(_cursorIndexOfPackages);
            }
            _tmpPackages = __converters.toPackageList(_tmp_1);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final boolean _tmpIsActive;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp_2 != 0;
            _item = new Gig(_tmpGigId,_tmpSellerId,_tmpSellerName,_tmpSellerImageUrl,_tmpTitle,_tmpDescription,_tmpCategory,_tmpTags,_tmpImageUrl,_tmpRating,_tmpReviewCount,_tmpPackages,_tmpCreatedAt,_tmpIsActive);
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

  @Override
  public Object getGigById(final String id, final Continuation<? super Gig> $completion) {
    final String _sql = "SELECT * FROM gigs WHERE gigId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (id == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, id);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Gig>() {
      @Override
      @Nullable
      public Gig call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfGigId = CursorUtil.getColumnIndexOrThrow(_cursor, "gigId");
          final int _cursorIndexOfSellerId = CursorUtil.getColumnIndexOrThrow(_cursor, "sellerId");
          final int _cursorIndexOfSellerName = CursorUtil.getColumnIndexOrThrow(_cursor, "sellerName");
          final int _cursorIndexOfSellerImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "sellerImageUrl");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfTags = CursorUtil.getColumnIndexOrThrow(_cursor, "tags");
          final int _cursorIndexOfImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "imageUrl");
          final int _cursorIndexOfRating = CursorUtil.getColumnIndexOrThrow(_cursor, "rating");
          final int _cursorIndexOfReviewCount = CursorUtil.getColumnIndexOrThrow(_cursor, "reviewCount");
          final int _cursorIndexOfPackages = CursorUtil.getColumnIndexOrThrow(_cursor, "packages");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final Gig _result;
          if (_cursor.moveToFirst()) {
            final String _tmpGigId;
            if (_cursor.isNull(_cursorIndexOfGigId)) {
              _tmpGigId = null;
            } else {
              _tmpGigId = _cursor.getString(_cursorIndexOfGigId);
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
            final String _tmpSellerImageUrl;
            if (_cursor.isNull(_cursorIndexOfSellerImageUrl)) {
              _tmpSellerImageUrl = null;
            } else {
              _tmpSellerImageUrl = _cursor.getString(_cursorIndexOfSellerImageUrl);
            }
            final String _tmpTitle;
            if (_cursor.isNull(_cursorIndexOfTitle)) {
              _tmpTitle = null;
            } else {
              _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            }
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final String _tmpCategory;
            if (_cursor.isNull(_cursorIndexOfCategory)) {
              _tmpCategory = null;
            } else {
              _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            }
            final List<String> _tmpTags;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfTags)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfTags);
            }
            _tmpTags = __converters.toStringList(_tmp);
            final String _tmpImageUrl;
            if (_cursor.isNull(_cursorIndexOfImageUrl)) {
              _tmpImageUrl = null;
            } else {
              _tmpImageUrl = _cursor.getString(_cursorIndexOfImageUrl);
            }
            final double _tmpRating;
            _tmpRating = _cursor.getDouble(_cursorIndexOfRating);
            final int _tmpReviewCount;
            _tmpReviewCount = _cursor.getInt(_cursorIndexOfReviewCount);
            final List<GigPackage> _tmpPackages;
            final String _tmp_1;
            if (_cursor.isNull(_cursorIndexOfPackages)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getString(_cursorIndexOfPackages);
            }
            _tmpPackages = __converters.toPackageList(_tmp_1);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final boolean _tmpIsActive;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp_2 != 0;
            _result = new Gig(_tmpGigId,_tmpSellerId,_tmpSellerName,_tmpSellerImageUrl,_tmpTitle,_tmpDescription,_tmpCategory,_tmpTags,_tmpImageUrl,_tmpRating,_tmpReviewCount,_tmpPackages,_tmpCreatedAt,_tmpIsActive);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
