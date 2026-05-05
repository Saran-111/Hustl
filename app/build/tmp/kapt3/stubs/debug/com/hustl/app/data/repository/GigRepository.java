package com.hustl.app.data.repository;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J$\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\b2\u0006\u0010\n\u001a\u00020\u000bH\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\f\u0010\rJ\u0012\u0010\u000e\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000b0\u00100\u000fJ\u0018\u0010\u0011\u001a\u0004\u0018\u00010\u000b2\u0006\u0010\u0012\u001a\u00020\tH\u0086@\u00a2\u0006\u0002\u0010\u0013J\u001a\u0010\u0014\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000b0\u00100\u000f2\u0006\u0010\u0015\u001a\u00020\tJ\u0014\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00170\u00102\u0006\u0010\u0012\u001a\u00020\tJ\f\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u000b0\u0010J\u0014\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u00170\u00102\u0006\u0010\u0012\u001a\u00020\tR\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u000b\n\u0002\b!\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006\u001a"}, d2 = {"Lcom/hustl/app/data/repository/GigRepository;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "gigDao", "Lcom/hustl/app/data/local/GigDao;", "createGig", "Lkotlin/Result;", "", "gig", "Lcom/hustl/app/data/model/Gig;", "createGig-gIAlu-s", "(Lcom/hustl/app/data/model/Gig;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllGigs", "Lkotlinx/coroutines/flow/Flow;", "", "getGigById", "gigId", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getGigsByCategory", "category", "getReviews", "Lcom/hustl/app/data/model/Review;", "getSampleGigs", "getSampleReviews", "app_debug"})
public final class GigRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.hustl.app.data.local.GigDao gigDao = null;
    
    public GigRepository(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.hustl.app.data.model.Gig>> getAllGigs() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.hustl.app.data.model.Gig>> getGigsByCategory(@org.jetbrains.annotations.NotNull()
    java.lang.String category) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getGigById(@org.jetbrains.annotations.NotNull()
    java.lang.String gigId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.hustl.app.data.model.Gig> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.hustl.app.data.model.Review> getReviews(@org.jetbrains.annotations.NotNull()
    java.lang.String gigId) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.hustl.app.data.model.Gig> getSampleGigs() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.hustl.app.data.model.Review> getSampleReviews(@org.jetbrains.annotations.NotNull()
    java.lang.String gigId) {
        return null;
    }
}