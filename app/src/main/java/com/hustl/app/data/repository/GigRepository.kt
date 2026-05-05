package com.hustl.app.data.repository

import android.content.Context
import com.hustl.app.data.local.AppDatabase
import com.hustl.app.data.model.Gig
import com.hustl.app.data.model.Review
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onStart

class GigRepository(context: Context) {
    private val gigDao = AppDatabase.getDatabase(context).gigDao()

    fun getAllGigs(): Flow<List<Gig>> {
        return gigDao.getAllGigs().onStart {
            // Seed data if empty
            val currentGigs = gigDao.getAllGigs().first()
            if (currentGigs.isEmpty()) {
                gigDao.insertGigs(getSampleGigs())
            }
        }
    }

    fun getGigsByCategory(category: String): Flow<List<Gig>> {
        return if (category == "All") getAllGigs() else gigDao.getGigsByCategory(category)
    }

    suspend fun getGigById(gigId: String): Gig? {
        return gigDao.getGigById(gigId)
    }

    suspend fun createGig(gig: Gig): Result<String> {
        return try {
            val gigId = "gig_${System.currentTimeMillis()}"
            val gigWithId = gig.copy(gigId = gigId)
            gigDao.insertGigs(listOf(gigWithId))
            Result.success(gigId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getReviews(gigId: String): List<Review> = getSampleReviews(gigId)

    fun getSampleGigs(): List<Gig> = listOf(
        Gig(gigId = "gig1", sellerId = "seller1", sellerName = "Priya S.", title = "I will design a stunning logo with unlimited revisions", description = "Get a professional, memorable logo that represents your brand identity.", category = "Design", tags = listOf("logo", "branding", "design"), rating = 4.9, reviewCount = 142,
            packages = listOf(
                com.hustl.app.data.model.GigPackage("Basic", 999, "1 concept, 2 revisions", 3, 2, listOf("1 Logo Concept", "2 Revisions", "PNG & JPG")),
                com.hustl.app.data.model.GigPackage("Standard", 2499, "3 concepts, unlimited revisions", 2, -1, listOf("3 Logo Concepts", "Unlimited Revisions", "All File Formats")),
                com.hustl.app.data.model.GigPackage("Premium", 4999, "5 concepts + full brand kit", 1, -1, listOf("5 Logo Concepts", "Full Brand Kit", "Source Files"))
            )),
        Gig(gigId = "gig2", sellerId = "seller2", sellerName = "Rahul D.", title = "I will build a full-stack React + Node.js web application", description = "I build scalable, high-performance web applications using modern technologies.", category = "Development", tags = listOf("react", "nodejs", "fullstack"), rating = 4.8, reviewCount = 98,
            packages = listOf(
                com.hustl.app.data.model.GigPackage("Basic", 4999, "Landing page, 5 pages", 7, 2, listOf("5 Page Website", "Responsive Design")),
                com.hustl.app.data.model.GigPackage("Standard", 12999, "Full app with auth & database", 14, 3, listOf("Full Web App", "Auth System", "Database")),
                com.hustl.app.data.model.GigPackage("Premium", 24999, "Enterprise-grade application", 21, -1, listOf("Everything in Standard", "Payment Integration"))
            )),
        Gig(gigId = "gig3", sellerId = "seller3", sellerName = "Ananya K.", title = "I will write SEO-optimized blog posts and articles", description = "Compelling, well-research content that ranks on Google.", category = "Writing", tags = listOf("seo", "blog", "content", "writing"), rating = 5.0, reviewCount = 76,
            packages = listOf(
                com.hustl.app.data.model.GigPackage("Basic", 599, "500 word article", 2, 1, listOf("500 Words", "SEO Keywords")),
                com.hustl.app.data.model.GigPackage("Standard", 1499, "1500 word in-depth article", 1, 3, listOf("1500 Words", "Meta Description")),
                com.hustl.app.data.model.GigPackage("Premium", 3999, "5 article content bundle", 5, -1, listOf("5 × 1500 Words", "Full SEO Audit"))
            )),
        Gig(gigId = "gig4", sellerId = "seller4", sellerName = "Dev M.", title = "I will create scroll-stopping social media content", description = "Full social media management and strategy.", category = "Marketing", tags = listOf("social media", "instagram", "marketing"), rating = 4.7, reviewCount = 55,
            packages = listOf(
                com.hustl.app.data.model.GigPackage("Basic", 1499, "10 posts for 1 platform", 7, 2, listOf("10 Posts", "Captions")),
                com.hustl.app.data.model.GigPackage("Standard", 3999, "30 posts for 3 platforms", 14, 3, listOf("30 Posts", "3 Platforms")),
                com.hustl.app.data.model.GigPackage("Premium", 8999, "Full monthly management", 30, -1, listOf("Unlimited Posts", "All Platforms"))
            )),
        Gig(gigId = "gig5", sellerId = "seller5", sellerName = "Sana T.", title = "I will edit your YouTube video with pro motion graphics", description = "Cinema-quality video editing for YouTube and ads.", category = "Video", tags = listOf("video editing", "youtube", "motion graphics"), rating = 4.9, reviewCount = 203,
            packages = listOf(
                com.hustl.app.data.model.GigPackage("Basic", 2499, "Up to 5 min basic edit", 3, 2, listOf("Up to 5 Minutes", "Basic Cuts")),
                com.hustl.app.data.model.GigPackage("Standard", 5999, "Up to 15 min with graphics", 2, 3, listOf("Up to 15 Minutes", "Motion Graphics")),
                com.hustl.app.data.model.GigPackage("Premium", 11999, "Full production edit", 1, -1, listOf("Any Length", "3D Graphics"))
            )),
        Gig(gigId = "gig6", sellerId = "seller1", sellerName = "Priya S.", title = "I will compose original music & sound design", description = "Original, royalty-free music composition.", category = "Music", tags = listOf("music", "composition", "sound design"), rating = 4.8, reviewCount = 34,
            packages = listOf(
                com.hustl.app.data.model.GigPackage("Basic", 1999, "30 second track", 5, 1, listOf("30 Second Track", "1 Revision")),
                com.hustl.app.data.model.GigPackage("Standard", 4999, "2 minute track with stems", 3, 3, listOf("2 Minute Track", "Stem Files")),
                com.hustl.app.data.model.GigPackage("Premium", 9999, "Full soundtrack", 7, -1, listOf("Up to 10 Minutes", "Full Stems"))
            ))
    )

    fun getSampleReviews(gigId: String): List<Review> = listOf(
        Review(reviewId = "r1", gigId = gigId, buyerName = "Vikram T.", rating = 5f, comment = "Incredible work!"),
        Review(reviewId = "r2", gigId = gigId, buyerName = "Meera R.", rating = 5f, comment = "Fast and professional."),
        Review(reviewId = "r3", gigId = gigId, buyerName = "Amar J.", rating = 4f, comment = "Great quality work.")
    )
}
