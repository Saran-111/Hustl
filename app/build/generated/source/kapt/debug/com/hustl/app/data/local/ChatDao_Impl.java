package com.hustl.app.data.local;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.hustl.app.data.model.Chat;
import com.hustl.app.data.model.Converters;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class ChatDao_Impl implements ChatDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Chat> __insertionAdapterOfChat;

  private final Converters __converters = new Converters();

  private final EntityDeletionOrUpdateAdapter<Chat> __updateAdapterOfChat;

  public ChatDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfChat = new EntityInsertionAdapter<Chat>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `chats` (`chatId`,`participants`,`participantNames`,`gigId`,`gigTitle`,`lastMessage`,`lastMessageTime`,`unreadCount`) VALUES (?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Chat entity) {
        if (entity.getChatId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getChatId());
        }
        final String _tmp = __converters.fromStringList(entity.getParticipants());
        if (_tmp == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, _tmp);
        }
        final String _tmp_1 = __converters.fromStringMap(entity.getParticipantNames());
        if (_tmp_1 == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, _tmp_1);
        }
        if (entity.getGigId() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getGigId());
        }
        if (entity.getGigTitle() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getGigTitle());
        }
        if (entity.getLastMessage() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getLastMessage());
        }
        statement.bindLong(7, entity.getLastMessageTime());
        statement.bindLong(8, entity.getUnreadCount());
      }
    };
    this.__updateAdapterOfChat = new EntityDeletionOrUpdateAdapter<Chat>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `chats` SET `chatId` = ?,`participants` = ?,`participantNames` = ?,`gigId` = ?,`gigTitle` = ?,`lastMessage` = ?,`lastMessageTime` = ?,`unreadCount` = ? WHERE `chatId` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Chat entity) {
        if (entity.getChatId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getChatId());
        }
        final String _tmp = __converters.fromStringList(entity.getParticipants());
        if (_tmp == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, _tmp);
        }
        final String _tmp_1 = __converters.fromStringMap(entity.getParticipantNames());
        if (_tmp_1 == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, _tmp_1);
        }
        if (entity.getGigId() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getGigId());
        }
        if (entity.getGigTitle() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getGigTitle());
        }
        if (entity.getLastMessage() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getLastMessage());
        }
        statement.bindLong(7, entity.getLastMessageTime());
        statement.bindLong(8, entity.getUnreadCount());
        if (entity.getChatId() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getChatId());
        }
      }
    };
  }

  @Override
  public Object insertChats(final List<Chat> chats, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfChat.insert(chats);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateChat(final Chat chat, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfChat.handle(chat);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<Chat>> getAllChats() {
    final String _sql = "SELECT * FROM chats ORDER BY lastMessageTime DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"chats"}, new Callable<List<Chat>>() {
      @Override
      @NonNull
      public List<Chat> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfChatId = CursorUtil.getColumnIndexOrThrow(_cursor, "chatId");
          final int _cursorIndexOfParticipants = CursorUtil.getColumnIndexOrThrow(_cursor, "participants");
          final int _cursorIndexOfParticipantNames = CursorUtil.getColumnIndexOrThrow(_cursor, "participantNames");
          final int _cursorIndexOfGigId = CursorUtil.getColumnIndexOrThrow(_cursor, "gigId");
          final int _cursorIndexOfGigTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "gigTitle");
          final int _cursorIndexOfLastMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "lastMessage");
          final int _cursorIndexOfLastMessageTime = CursorUtil.getColumnIndexOrThrow(_cursor, "lastMessageTime");
          final int _cursorIndexOfUnreadCount = CursorUtil.getColumnIndexOrThrow(_cursor, "unreadCount");
          final List<Chat> _result = new ArrayList<Chat>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Chat _item;
            final String _tmpChatId;
            if (_cursor.isNull(_cursorIndexOfChatId)) {
              _tmpChatId = null;
            } else {
              _tmpChatId = _cursor.getString(_cursorIndexOfChatId);
            }
            final List<String> _tmpParticipants;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfParticipants)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfParticipants);
            }
            _tmpParticipants = __converters.toStringList(_tmp);
            final Map<String, String> _tmpParticipantNames;
            final String _tmp_1;
            if (_cursor.isNull(_cursorIndexOfParticipantNames)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getString(_cursorIndexOfParticipantNames);
            }
            _tmpParticipantNames = __converters.toStringMap(_tmp_1);
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
            final String _tmpLastMessage;
            if (_cursor.isNull(_cursorIndexOfLastMessage)) {
              _tmpLastMessage = null;
            } else {
              _tmpLastMessage = _cursor.getString(_cursorIndexOfLastMessage);
            }
            final long _tmpLastMessageTime;
            _tmpLastMessageTime = _cursor.getLong(_cursorIndexOfLastMessageTime);
            final int _tmpUnreadCount;
            _tmpUnreadCount = _cursor.getInt(_cursorIndexOfUnreadCount);
            _item = new Chat(_tmpChatId,_tmpParticipants,_tmpParticipantNames,_tmpGigId,_tmpGigTitle,_tmpLastMessage,_tmpLastMessageTime,_tmpUnreadCount);
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
  public Object getChatById(final String id, final Continuation<? super Chat> $completion) {
    final String _sql = "SELECT * FROM chats WHERE chatId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (id == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, id);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Chat>() {
      @Override
      @Nullable
      public Chat call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfChatId = CursorUtil.getColumnIndexOrThrow(_cursor, "chatId");
          final int _cursorIndexOfParticipants = CursorUtil.getColumnIndexOrThrow(_cursor, "participants");
          final int _cursorIndexOfParticipantNames = CursorUtil.getColumnIndexOrThrow(_cursor, "participantNames");
          final int _cursorIndexOfGigId = CursorUtil.getColumnIndexOrThrow(_cursor, "gigId");
          final int _cursorIndexOfGigTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "gigTitle");
          final int _cursorIndexOfLastMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "lastMessage");
          final int _cursorIndexOfLastMessageTime = CursorUtil.getColumnIndexOrThrow(_cursor, "lastMessageTime");
          final int _cursorIndexOfUnreadCount = CursorUtil.getColumnIndexOrThrow(_cursor, "unreadCount");
          final Chat _result;
          if (_cursor.moveToFirst()) {
            final String _tmpChatId;
            if (_cursor.isNull(_cursorIndexOfChatId)) {
              _tmpChatId = null;
            } else {
              _tmpChatId = _cursor.getString(_cursorIndexOfChatId);
            }
            final List<String> _tmpParticipants;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfParticipants)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfParticipants);
            }
            _tmpParticipants = __converters.toStringList(_tmp);
            final Map<String, String> _tmpParticipantNames;
            final String _tmp_1;
            if (_cursor.isNull(_cursorIndexOfParticipantNames)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getString(_cursorIndexOfParticipantNames);
            }
            _tmpParticipantNames = __converters.toStringMap(_tmp_1);
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
            final String _tmpLastMessage;
            if (_cursor.isNull(_cursorIndexOfLastMessage)) {
              _tmpLastMessage = null;
            } else {
              _tmpLastMessage = _cursor.getString(_cursorIndexOfLastMessage);
            }
            final long _tmpLastMessageTime;
            _tmpLastMessageTime = _cursor.getLong(_cursorIndexOfLastMessageTime);
            final int _tmpUnreadCount;
            _tmpUnreadCount = _cursor.getInt(_cursorIndexOfUnreadCount);
            _result = new Chat(_tmpChatId,_tmpParticipants,_tmpParticipantNames,_tmpGigId,_tmpGigTitle,_tmpLastMessage,_tmpLastMessageTime,_tmpUnreadCount);
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
