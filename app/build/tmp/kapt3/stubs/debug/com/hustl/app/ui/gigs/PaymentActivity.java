package com.hustl.app.ui.gigs;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\r\n\u0002\u0010\u000b\n\u0002\b\u0004\u0018\u00002\u00020\u0001:\u0001!B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u000f\u001a\u00020\u0010H\u0002J\u0012\u0010\u0011\u001a\u00020\u00102\b\u0010\u0012\u001a\u0004\u0018\u00010\fH\u0014J8\u0010\u0013\u001a\u00020\u00102\u0006\u0010\u0014\u001a\u00020\b2\u0006\u0010\u0015\u001a\u00020\b2\u0006\u0010\u0016\u001a\u00020\b2\u0006\u0010\u0017\u001a\u00020\b2\u0006\u0010\u0018\u001a\u00020\u000e2\u0006\u0010\u0019\u001a\u00020\bH\u0002J\b\u0010\u001a\u001a\u00020\u0010H\u0002J\b\u0010\u001b\u001a\u00020\u0010H\u0002J8\u0010\u001c\u001a\u00020\u00102\u0006\u0010\u0014\u001a\u00020\b2\u0006\u0010\u0015\u001a\u00020\b2\u0006\u0010\u0016\u001a\u00020\b2\u0006\u0010\u0017\u001a\u00020\b2\u0006\u0010\u0018\u001a\u00020\u000e2\u0006\u0010\u0019\u001a\u00020\bH\u0002J\u0010\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u0018\u001a\u00020\u000eH\u0002J\u0010\u0010\u001f\u001a\u00020\u00102\u0006\u0010 \u001a\u00020\bH\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0007\u001a\u0004\u0018\u00010\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082.\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000b\u001a\u0004\u0018\u00010\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\""}, d2 = {"Lcom/hustl/app/ui/gigs/PaymentActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "authRepo", "Lcom/hustl/app/data/repository/AuthRepository;", "binding", "Lcom/hustl/app/databinding/ActivityPaymentBinding;", "generatedKey", "", "orderRepo", "Lcom/hustl/app/data/repository/OrderRepository;", "pendingOrderData", "Landroid/os/Bundle;", "walletBalance", "", "loadWalletBalance", "", "onCreate", "savedInstanceState", "processPayment", "gigId", "gigTitle", "sellerName", "packageName", "price", "requirements", "setupPaymentMethods", "setupWebView", "startAuthentication", "validateInputs", "", "verifyAndProcess", "responseKey", "WebAppInterface", "app_debug"})
public final class PaymentActivity extends androidx.appcompat.app.AppCompatActivity {
    private com.hustl.app.databinding.ActivityPaymentBinding binding;
    private com.hustl.app.data.repository.OrderRepository orderRepo;
    private com.hustl.app.data.repository.AuthRepository authRepo;
    private int walletBalance = 0;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String generatedKey;
    @org.jetbrains.annotations.Nullable()
    private android.os.Bundle pendingOrderData;
    
    public PaymentActivity() {
        super();
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void setupWebView() {
    }
    
    private final void setupPaymentMethods() {
    }
    
    private final void loadWalletBalance() {
    }
    
    private final boolean validateInputs(int price) {
        return false;
    }
    
    private final void startAuthentication(java.lang.String gigId, java.lang.String gigTitle, java.lang.String sellerName, java.lang.String packageName, int price, java.lang.String requirements) {
    }
    
    private final void verifyAndProcess(java.lang.String responseKey) {
    }
    
    private final void processPayment(java.lang.String gigId, java.lang.String gigTitle, java.lang.String sellerName, java.lang.String packageName, int price, java.lang.String requirements) {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\u0004\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0007\u00a8\u0006\u0007"}, d2 = {"Lcom/hustl/app/ui/gigs/PaymentActivity$WebAppInterface;", "", "(Lcom/hustl/app/ui/gigs/PaymentActivity;)V", "onAuthFinished", "", "responseKey", "", "app_debug"})
    public final class WebAppInterface {
        
        public WebAppInterface() {
            super();
        }
        
        @android.webkit.JavascriptInterface()
        public final void onAuthFinished(@org.jetbrains.annotations.NotNull()
        java.lang.String responseKey) {
        }
    }
}