package com.hustl.app.ui.gigs;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000T\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0013\u001a\u00020\u0014H\u0002J\u0012\u0010\u0015\u001a\u00020\u00142\b\u0010\u0016\u001a\u0004\u0018\u00010\u0017H\u0014J\b\u0010\u0018\u001a\u00020\u0014H\u0002J\u0016\u0010\u0019\u001a\u00020\u00142\f\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u001c0\u001bH\u0002J\b\u0010\u001d\u001a\u00020\u0014H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0012X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001e"}, d2 = {"Lcom/hustl/app/ui/gigs/GigDetailActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "authRepo", "Lcom/hustl/app/data/repository/AuthRepository;", "binding", "Lcom/hustl/app/databinding/ActivityGigDetailBinding;", "currentGigTitle", "", "currentSellerName", "gigId", "gigRepo", "Lcom/hustl/app/data/repository/GigRepository;", "isFaved", "", "orderRepo", "Lcom/hustl/app/data/repository/OrderRepository;", "selectedPackageIdx", "", "loadGig", "", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "openChat", "showOrderSheet", "packages", "", "Lcom/hustl/app/data/model/GigPackage;", "toggleFav", "app_debug"})
public final class GigDetailActivity extends androidx.appcompat.app.AppCompatActivity {
    private com.hustl.app.databinding.ActivityGigDetailBinding binding;
    private com.hustl.app.data.repository.GigRepository gigRepo;
    private com.hustl.app.data.repository.OrderRepository orderRepo;
    private com.hustl.app.data.repository.AuthRepository authRepo;
    private int selectedPackageIdx = 0;
    @org.jetbrains.annotations.NotNull()
    private java.lang.String gigId = "";
    private boolean isFaved = false;
    @org.jetbrains.annotations.NotNull()
    private java.lang.String currentGigTitle = "";
    @org.jetbrains.annotations.NotNull()
    private java.lang.String currentSellerName = "";
    
    public GigDetailActivity() {
        super();
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void loadGig() {
    }
    
    private final void showOrderSheet(java.util.List<com.hustl.app.data.model.GigPackage> packages) {
    }
    
    private final void toggleFav() {
    }
    
    private final void openChat() {
    }
}