package com.hustl.app.data.repository;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u001a\u0010\t\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u000b0\n2\u0006\u0010\r\u001a\u00020\u000eJ\u001a\u0010\u000f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00100\u000b0\n2\u0006\u0010\u0011\u001a\u00020\u000eJ\u000e\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00100\u000bH\u0002J\u0016\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\f0\u000b2\u0006\u0010\r\u001a\u00020\u000eH\u0002J,\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00160\u00152\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u0017\u001a\u00020\fH\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u0018\u0010\u0019R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u000b\n\u0002\b!\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006\u001a"}, d2 = {"Lcom/hustl/app/data/repository/ChatRepository;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "chatDao", "Lcom/hustl/app/data/local/ChatDao;", "messageDao", "Lcom/hustl/app/data/local/MessageDao;", "getMessages", "Lkotlinx/coroutines/flow/Flow;", "", "Lcom/hustl/app/data/model/Message;", "chatId", "", "getMyChats", "Lcom/hustl/app/data/model/Chat;", "userId", "getSampleChats", "getSampleMessages", "sendMessage", "Lkotlin/Result;", "", "message", "sendMessage-0E7RQCE", "(Ljava/lang/String;Lcom/hustl/app/data/model/Message;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public final class ChatRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.hustl.app.data.local.ChatDao chatDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.hustl.app.data.local.MessageDao messageDao = null;
    
    public ChatRepository(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.hustl.app.data.model.Chat>> getMyChats(@org.jetbrains.annotations.NotNull()
    java.lang.String userId) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.hustl.app.data.model.Message>> getMessages(@org.jetbrains.annotations.NotNull()
    java.lang.String chatId) {
        return null;
    }
    
    private final java.util.List<com.hustl.app.data.model.Chat> getSampleChats() {
        return null;
    }
    
    private final java.util.List<com.hustl.app.data.model.Message> getSampleMessages(java.lang.String chatId) {
        return null;
    }
}