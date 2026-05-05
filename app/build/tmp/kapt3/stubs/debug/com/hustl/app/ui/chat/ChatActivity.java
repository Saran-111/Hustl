package com.hustl.app.ui.chat;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0011\u001a\u00020\u0012H\u0002J\u0012\u0010\u0013\u001a\u00020\u00122\b\u0010\u0014\u001a\u0004\u0018\u00010\u0015H\u0014J\b\u0010\u0016\u001a\u00020\u0012H\u0002J\b\u0010\u0017\u001a\u00020\u0012H\u0002J\b\u0010\u0018\u001a\u00020\u0012H\u0002J\b\u0010\u0019\u001a\u00020\u0012H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082.\u00a2\u0006\u0002\n\u0000R\u0014\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001a"}, d2 = {"Lcom/hustl/app/ui/chat/ChatActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "authRepo", "Lcom/hustl/app/data/repository/AuthRepository;", "binding", "Lcom/hustl/app/databinding/ActivityChatBinding;", "chatId", "", "chatRepo", "Lcom/hustl/app/data/repository/ChatRepository;", "messageAdapter", "Lcom/hustl/app/adapters/MessageAdapter;", "messages", "", "Lcom/hustl/app/data/model/Message;", "otherName", "observeMessages", "", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "scrollToBottom", "sendMessage", "setupInput", "simulateReply", "app_debug"})
public final class ChatActivity extends androidx.appcompat.app.AppCompatActivity {
    private com.hustl.app.databinding.ActivityChatBinding binding;
    private com.hustl.app.data.repository.ChatRepository chatRepo;
    private com.hustl.app.data.repository.AuthRepository authRepo;
    private com.hustl.app.adapters.MessageAdapter messageAdapter;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.hustl.app.data.model.Message> messages = null;
    @org.jetbrains.annotations.NotNull()
    private java.lang.String chatId = "";
    @org.jetbrains.annotations.NotNull()
    private java.lang.String otherName = "";
    
    public ChatActivity() {
        super();
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void observeMessages() {
    }
    
    private final void setupInput() {
    }
    
    private final void sendMessage() {
    }
    
    private final void simulateReply() {
    }
    
    private final void scrollToBottom() {
    }
}