package com.hustl.app.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.hustl.app.data.model.Gig
import com.hustl.app.data.model.Review
import kotlinx.coroutines.tasks.await

class GigRepository {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    suspend fun getAllGigs(): List<Gig> {
        return try {
            val snapshot = db.collection("gigs")
                .whereEqualTo("active", true)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get().await()
            snapshot.toObjects(Gig::class.java)
        } catch (e: Exception) {
            getSampleGigs()
        }
    }

    suspend fun getGigsByCategory(category: String): List<Gig> {
        return try {
            val snapshot = db.collection("gigs")
                .whereEqualTo("category", category)
                .get().await()
            snapshot.toObjects(Gig::class.java)
        } catch (e: Exception) {
            getSampleGigs().filter { it.category == category }
        }
    }

    suspend fun getGigById(gigId: String): Gig? {
        return try {
            val doc = db.collection("gigs").document(gigId).get().await()
            doc.toObject(Gig::class.java)
        } catch (e: Exception) {
            getSampleGigs().find { it.gigId == gigId }
        }
    }

    suspend fun createGig(gig: Gig): Result<String> {
        return try {
            val ref = db.collection("gigs").document()
            val gigWithId = gig.copy(gigId = ref.id)
            ref.set(gigWithId).await()
            Result.success(ref.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getReviews(gigId: String): List<Review> {
        return try {
            val snapshot = db.collection("reviews")
                .whereEqualTo("gigId", gigId)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get().await()
            snapshot.toObjects(Review::class.java)
        } catch (e: Exception) {
            getSampleReviews(gigId)
        }
    }

    // Sample data for demo (used when Firebase not configured)
    fun getSampleGigs(): List<Gig> = listOf(
        Gig(gigId = "gig1", sellerId = "seller1", sellerName = "Priya S.", title = "I will design a stunning logo with unlimited revisions", description = "Get a professional, memorable logo that represents your brand identity. I specialize in minimalist, modern, and bold styles for startups, businesses, and personal brands.\n\nWhat you'll get:\n• Multiple unique concepts\n• Unlimited revisions until satisfied\n• All file formats (AI, EPS, PNG, SVG, PDF)\n• Full commercial rights\n• Brand color palette", category = "Design", tags = listOf("logo", "branding", "design"), rating = 4.9, reviewCount = 142,
            packages = listOf(
                com.hustl.app.data.model.GigPackage("Basic", 999, "1 concept, 2 revisions", 3, 2, listOf("1 Logo Concept", "2 Revisions", "PNG & JPG", "3 Day Delivery")),
                com.hustl.app.data.model.GigPackage("Standard", 2499, "3 concepts, unlimited revisions", 2, -1, listOf("3 Logo Concepts", "Unlimited Revisions", "All File Formats", "Brand Colors", "2 Day Delivery")),
                com.hustl.app.data.model.GigPackage("Premium", 4999, "5 concepts + full brand kit", 1, -1, listOf("5 Logo Concepts", "Full Brand Kit", "Source Files", "Social Media Kit", "1 Day Priority Delivery"))
            )),
        Gig(gigId = "gig2", sellerId = "seller2", sellerName = "Rahul D.", title = "I will build a full-stack React + Node.js web application", description = "I build scalable, high-performance web applications using modern technologies. From landing pages to complex dashboards — clean code, responsive design, and SEO optimized.\n\nTech Stack:\n• React.js / Next.js frontend\n• Node.js / Express backend\n• MongoDB / PostgreSQL database\n• AWS / Vercel deployment", category = "Development", tags = listOf("react", "nodejs", "fullstack"), rating = 4.8, reviewCount = 98,
            packages = listOf(
                com.hustl.app.data.model.GigPackage("Basic", 4999, "Landing page, 5 pages", 7, 2, listOf("5 Page Website", "Responsive Design", "Contact Form", "7 Day Delivery")),
                com.hustl.app.data.model.GigPackage("Standard", 12999, "Full app with auth & database", 14, 3, listOf("Full Web App", "Auth System", "Database", "Admin Panel", "Deployment")),
                com.hustl.app.data.model.GigPackage("Premium", 24999, "Enterprise-grade application", 21, -1, listOf("Everything in Standard", "Payment Integration", "API Integrations", "6 Months Support"))
            )),
        Gig(gigId = "gig3", sellerId = "seller3", sellerName = "Ananya K.", title = "I will write SEO-optimized blog posts and articles", description = "Compelling, well-researched content that ranks on Google and converts readers. I write for SaaS, tech, lifestyle, health, and finance niches with proven results.", category = "Writing", tags = listOf("seo", "blog", "content", "writing"), rating = 5.0, reviewCount = 76,
            packages = listOf(
                com.hustl.app.data.model.GigPackage("Basic", 599, "500 word article", 2, 1, listOf("500 Words", "SEO Keywords", "1 Revision", "2 Day Delivery")),
                com.hustl.app.data.model.GigPackage("Standard", 1499, "1500 word in-depth article", 1, 3, listOf("1500 Words", "Meta Description", "3 Revisions", "Internal Links", "1 Day Delivery")),
                com.hustl.app.data.model.GigPackage("Premium", 3999, "5 article content bundle", 5, -1, listOf("5 × 1500 Words", "Full SEO Audit", "Unlimited Revisions", "Content Calendar", "Social Snippets"))
            )),
        Gig(gigId = "gig4", sellerId = "seller4", sellerName = "Dev M.", title = "I will create scroll-stopping social media content & strategy", description = "Full social media management including content creation, scheduling, and growth strategy for Instagram, LinkedIn, Twitter, and YouTube.", category = "Marketing", tags = listOf("social media", "instagram", "marketing", "content"), rating = 4.7, reviewCount = 55,
            packages = listOf(
                com.hustl.app.data.model.GigPackage("Basic", 1499, "10 posts for 1 platform", 7, 2, listOf("10 Posts", "Captions", "Hashtag Research", "1 Platform", "7 Day Delivery")),
                com.hustl.app.data.model.GigPackage("Standard", 3999, "30 posts for 3 platforms", 14, 3, listOf("30 Posts", "3 Platforms", "Story Templates", "Reels Scripts", "Monthly Strategy")),
                com.hustl.app.data.model.GigPackage("Premium", 8999, "Full monthly management", 30, -1, listOf("Unlimited Posts", "All Platforms", "Community Management", "Analytics Report", "Ad Creatives"))
            )),
        Gig(gigId = "gig5", sellerId = "seller5", sellerName = "Sana T.", title = "I will edit your YouTube video with pro motion graphics", description = "Cinema-quality video editing for YouTube, ads, corporate videos, and reels. After Effects animations, color grading, sound design — all included.", category = "Video", tags = listOf("video editing", "youtube", "motion graphics", "after effects"), rating = 4.9, reviewCount = 203,
            packages = listOf(
                com.hustl.app.data.model.GigPackage("Basic", 2499, "Up to 5 min basic edit", 3, 2, listOf("Up to 5 Minutes", "Basic Cuts", "Background Music", "3 Day Delivery")),
                com.hustl.app.data.model.GigPackage("Standard", 5999, "Up to 15 min with graphics", 2, 3, listOf("Up to 15 Minutes", "Motion Graphics", "Color Grading", "Subtitles", "2 Day Delivery")),
                com.hustl.app.data.model.GigPackage("Premium", 11999, "Full production edit", 1, -1, listOf("Any Length", "3D Graphics", "Sound Design", "Thumbnail Design", "1 Day Delivery"))
            )),
        Gig(gigId = "gig6", sellerId = "seller1", sellerName = "Priya S.", title = "I will compose original music & sound design for your project", description = "Original, royalty-free music composition for films, podcasts, games, ads, and apps. All genres — cinematic, lo-fi, electronic, acoustic.", category = "Music", tags = listOf("music", "composition", "sound design", "royalty free"), rating = 4.8, reviewCount = 34,
            packages = listOf(
                com.hustl.app.data.model.GigPackage("Basic", 1999, "30 second track", 5, 1, listOf("30 Second Track", "1 Revision", "WAV + MP3", "5 Day Delivery")),
                com.hustl.app.data.model.GigPackage("Standard", 4999, "2 minute full track with stems", 3, 3, listOf("2 Minute Track", "Stem Files", "3 Revisions", "Commercial License", "3 Day Delivery")),
                com.hustl.app.data.model.GigPackage("Premium", 9999, "Full soundtrack", 7, -1, listOf("Up to 10 Minutes", "Full Stems", "Unlimited Revisions", "Exclusive Rights", "2 Day Delivery"))
            )),
        Gig(gigId = "gig7", sellerId = "seller3", sellerName = "Ananya K.", title = "I will design a complete brand identity & style guide", description = "End-to-end brand identity design — logo, colors, typography, iconography, and a complete brand style guide for startups and growing businesses.", category = "Design", tags = listOf("branding", "identity", "logo", "style guide"), rating = 4.9, reviewCount = 67,
            packages = listOf(
                com.hustl.app.data.model.GigPackage("Basic", 3499, "Logo + color palette", 3, 2, listOf("Logo Design", "Color Palette", "Font Selection", "PDF Guide", "3 Day Delivery")),
                com.hustl.app.data.model.GigPackage("Standard", 7999, "Full brand identity", 5, -1, listOf("Everything in Basic", "Business Card", "Letterhead", "Social Media Kit", "2 Day Delivery")),
                com.hustl.app.data.model.GigPackage("Premium", 14999, "Complete brand system", 7, -1, listOf("Full Identity", "Brand Manifesto", "Web Style Guide", "Pitch Deck Template", "Unlimited Revisions"))
            )),
        Gig(gigId = "gig8", sellerId = "seller2", sellerName = "Rahul D.", title = "I will do professional product photography & retouching", description = "Studio-quality product photography for e-commerce, Amazon, Shopify, and social media. Lifestyle shots, white background, and creative compositions.", category = "Design", tags = listOf("photography", "product", "ecommerce", "retouching"), rating = 4.6, reviewCount = 41,
            packages = listOf(
                com.hustl.app.data.model.GigPackage("Basic", 1299, "5 white background photos", 2, 1, listOf("5 Product Photos", "White Background", "Basic Retouching", "2 Day Delivery")),
                com.hustl.app.data.model.GigPackage("Standard", 2999, "15 photos with lifestyle shots", 3, 3, listOf("15 Photos", "Lifestyle Shots", "Advanced Retouching", "Social Formats", "3 Day Delivery")),
                com.hustl.app.data.model.GigPackage("Premium", 5999, "50 photo full catalog", 5, -1, listOf("50 Photos", "360 View", "Video Clips", "Commercial Use", "Same Day Delivery"))
            ))
    )

    fun getSampleReviews(gigId: String): List<Review> = listOf(
        Review(reviewId = "r1", gigId = gigId, buyerName = "Vikram T.", rating = 5f, comment = "Incredible work! Perfectly understood my vision and delivered beyond expectations. The quality is outstanding."),
        Review(reviewId = "r2", gigId = gigId, buyerName = "Meera R.", rating = 5f, comment = "Fast, professional, and super responsive. Will definitely hire again!"),
        Review(reviewId = "r3", gigId = gigId, buyerName = "Amar J.", rating = 4f, comment = "Great quality work, delivered on time. Minor revisions were handled quickly.")
    )
}
