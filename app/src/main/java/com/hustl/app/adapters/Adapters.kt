package com.hustl.app.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hustl.app.R
import com.hustl.app.data.model.Chat
import com.hustl.app.data.model.Gig
import com.hustl.app.data.model.GigPackage
import com.hustl.app.data.model.Message
import com.hustl.app.data.model.Order
import com.hustl.app.data.model.Review
import java.text.SimpleDateFormat
import java.util.*

// ─── GIG ADAPTER ───────────────────────────────────────────────────────────────
class GigAdapter(
    private val isHorizontal: Boolean,
    private val onClick: (Gig) -> Unit
) : ListAdapter<Gig, GigAdapter.VH>(GigDiffCallback()) {

    private val categoryColors = mapOf(
        "Design" to "#1a0533", "Development" to "#0d2233",
        "Writing" to "#1a2200", "Marketing" to "#330d00",
        "Video" to "#00233a", "Music" to "#220033"
    )
    private val categoryEmojis = mapOf(
        "Design" to "🎨", "Development" to "💻",
        "Writing" to "✍️", "Marketing" to "📣",
        "Video" to "🎬", "Music" to "🎵"
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val layout = if (isHorizontal) R.layout.item_gig_horizontal else R.layout.item_gig_grid
        val v = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(h: VH, pos: Int) {
        val gig = getItem(pos)
        val color = categoryColors[gig.category] ?: "#1a1a1a"
        val emoji = categoryEmojis[gig.category] ?: "⚡"

        h.tvEmoji?.text = emoji
        h.heroCard?.setCardBackgroundColor(Color.parseColor(color))
        h.tvTitle.text = gig.title
        h.tvSeller.text = gig.sellerName
        h.tvRating.text = "★ ${gig.rating} (${gig.reviewCount})"
        h.tvPrice.text = "₹${"%,d".format(gig.packages.minOfOrNull { it.price } ?: 0)}"
        h.tvInitials.text = gig.sellerName.split(" ").filter { it.isNotEmpty() }.take(2).joinToString("") { it.first().toString() }
        h.itemView.setOnClickListener { onClick(gig) }
    }

    class VH(v: View) : RecyclerView.ViewHolder(v) {
        val tvEmoji: TextView? = v.findViewById(R.id.tvGigEmoji)
        val heroCard: CardView? = v.findViewById(R.id.cardHero)
        val tvTitle: TextView = v.findViewById(R.id.tvGigTitle)
        val tvSeller: TextView = v.findViewById(R.id.tvSellerName)
        val tvRating: TextView = v.findViewById(R.id.tvRating)
        val tvPrice: TextView = v.findViewById(R.id.tvPrice)
        val tvInitials: TextView = v.findViewById(R.id.tvSellerInitials)
    }

    class GigDiffCallback : DiffUtil.ItemCallback<Gig>() {
        override fun areItemsTheSame(oldItem: Gig, newItem: Gig) = oldItem.gigId == newItem.gigId
        override fun areContentsTheSame(oldItem: Gig, newItem: Gig) = oldItem == newItem
    }
}

// ─── SELLER ADAPTER ────────────────────────────────────────────────────────────
class SellerAdapter(private val sellers: List<Triple<String, String, Double>>) :
    RecyclerView.Adapter<SellerAdapter.VH>() {

    private val bgColors = listOf("#2d1b4e","#0d2233","#1a3300","#330d00","#002233")

    override fun getItemCount() = sellers.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_seller, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(h: VH, pos: Int) {
        val (name, skill, rating) = sellers[pos]
        h.tvInitials.text = name.split(" ").filter { it.isNotEmpty() }.take(2).joinToString("") { it.first().toString() }
        h.tvName.text = name
        h.tvSkill.text = skill
        h.tvRating.text = "★ $rating"
        h.avatarCard.setCardBackgroundColor(Color.parseColor(bgColors[pos % bgColors.size]))
    }

    class VH(v: View) : RecyclerView.ViewHolder(v) {
        val tvInitials: TextView = v.findViewById(R.id.tvInitials)
        val tvName: TextView = v.findViewById(R.id.tvName)
        val tvSkill: TextView = v.findViewById(R.id.tvSkill)
        val tvRating: TextView = v.findViewById(R.id.tvRating)
        val avatarCard: CardView = v.findViewById(R.id.cardAvatar)
    }
}

// ─── PACKAGE ADAPTER ───────────────────────────────────────────────────────────
class PackageAdapter(
    private val packages: List<GigPackage>,
    private val onSelected: (Int) -> Unit
) : RecyclerView.Adapter<PackageAdapter.VH>() {

    private var selectedIdx = 0

    override fun getItemCount() = packages.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_package, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(h: VH, pos: Int) {
        val pkg = packages[pos]
        h.tvName.text = pkg.name
        h.tvPrice.text = "₹${"%,d".format(pkg.price)}"
        h.tvDesc.text = pkg.description
        h.tvDelivery.text = "Delivered in ${pkg.deliveryDays} days · ${if (pkg.revisions == -1) "Unlimited" else pkg.revisions.toString()} revisions"
        h.tvFeatures.text = pkg.features.joinToString("\n") { "✓  $it" }

        val isSelected = pos == selectedIdx
        h.card.setCardBackgroundColor(
            if (isSelected) Color.parseColor("#1a1a00") else Color.parseColor("#161616")
        )
        h.card.strokeColor = if (isSelected) Color.parseColor("#e8ff6b") else Color.parseColor("#2a2a2a")
        h.card.strokeWidth = if (isSelected) 3 else 1

        h.card.setOnClickListener {
            val old = selectedIdx
            selectedIdx = pos
            notifyItemChanged(old)
            notifyItemChanged(pos)
            onSelected(pos)
        }
    }

    class VH(v: View) : RecyclerView.ViewHolder(v) {
        val card: com.google.android.material.card.MaterialCardView = v.findViewById(R.id.packageCard)
        val tvName: TextView = v.findViewById(R.id.tvPackageName)
        val tvPrice: TextView = v.findViewById(R.id.tvPackagePrice)
        val tvDesc: TextView = v.findViewById(R.id.tvPackageDesc)
        val tvDelivery: TextView = v.findViewById(R.id.tvDelivery)
        val tvFeatures: TextView = v.findViewById(R.id.tvFeatures)
    }
}

// ─── REVIEW ADAPTER ────────────────────────────────────────────────────────────
class ReviewAdapter(private val reviews: List<Review>) :
    RecyclerView.Adapter<ReviewAdapter.VH>() {

    override fun getItemCount() = reviews.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_review, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(h: VH, pos: Int) {
        val r = reviews[pos]
        h.tvInitials.text = r.buyerName.split(" ").filter { it.isNotEmpty() }.take(2).joinToString("") { it.first().toString() }
        h.tvName.text = r.buyerName
        h.tvStars.text = "★".repeat(r.rating.toInt())
        h.tvComment.text = r.comment
    }

    class VH(v: View) : RecyclerView.ViewHolder(v) {
        val tvInitials: TextView = v.findViewById(R.id.tvInitials)
        val tvName: TextView = v.findViewById(R.id.tvName)
        val tvStars: TextView = v.findViewById(R.id.tvStars)
        val tvComment: TextView = v.findViewById(R.id.tvComment)
    }
}

// ─── ORDER ADAPTER ─────────────────────────────────────────────────────────────
class OrderAdapter(private var orders: List<Order>) :
    RecyclerView.Adapter<OrderAdapter.VH>() {

    fun updateList(newList: List<Order>) {
        orders = newList
        notifyDataSetChanged()
    }

    override fun getItemCount() = orders.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_order, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(h: VH, pos: Int) {
        val o = orders[pos]
        h.tvOrderId.text = "#${o.orderId}"
        h.tvTitle.text = o.gigTitle
        h.tvSeller.text = "Seller: ${o.sellerName}"
        h.tvPrice.text = "₹${"%,d".format(o.price)}"

        val (statusText, statusColor) = when (o.status) {
            "active" -> "Active" to "#6bffc8"
            "completed" -> "Completed" to "#e8ff6b"
            "pending" -> "Pending" to "#fb923c"
            else -> "Unknown" to "#9a9590"
        }
        h.tvStatus.text = statusText
        h.tvStatus.setTextColor(Color.parseColor(statusColor))

        if (o.status == "active") {
            h.progressBar.visibility = View.VISIBLE
            h.progressBar.progress = o.progress
            h.tvProgress.visibility = View.VISIBLE
            h.tvProgress.text = "${o.progress}% complete"
        } else {
            h.progressBar.visibility = View.GONE
            h.tvProgress.visibility = View.GONE
        }
    }

    class VH(v: View) : RecyclerView.ViewHolder(v) {
        val tvOrderId: TextView = v.findViewById(R.id.tvOrderId)
        val tvTitle: TextView = v.findViewById(R.id.tvOrderTitle)
        val tvSeller: TextView = v.findViewById(R.id.tvOrderSeller)
        val tvPrice: TextView = v.findViewById(R.id.tvOrderPrice)
        val tvStatus: TextView = v.findViewById(R.id.tvOrderStatus)
        val progressBar: android.widget.ProgressBar = v.findViewById(R.id.progressBar)
        val tvProgress: TextView = v.findViewById(R.id.tvProgress)
    }
}

// ─── CHAT LIST ADAPTER ─────────────────────────────────────────────────────────
class ChatListAdapter(
    private val chats: List<Chat>,
    private val currentUserName: String,
    private val onClick: (Chat) -> Unit
) : RecyclerView.Adapter<ChatListAdapter.VH>() {

    private val bgColors = mapOf("chat1" to "#2d1b4e","chat2" to "#1a3300","chat3" to "#002233","chat4" to "#0d2233")
    private val sdf = SimpleDateFormat("h:mm a", Locale.getDefault())

    override fun getItemCount() = chats.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_chat, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(h: VH, pos: Int) {
        val c = chats[pos]
        val otherName = c.participantNames.values.firstOrNull { it != currentUserName } ?: "User"
        h.tvInitials.text = otherName.split(" ").filter { it.isNotEmpty() }.take(2).joinToString("") { it.first().toString() }
        h.tvName.text = otherName
        h.tvPreview.text = c.lastMessage
        h.tvTime.text = if (c.lastMessageTime > 0) sdf.format(Date(c.lastMessageTime)) else ""
        h.tvUnread.visibility = if (c.unreadCount > 0) View.VISIBLE else View.GONE
        h.avatarCard.setCardBackgroundColor(Color.parseColor(bgColors[c.chatId] ?: "#1a1a1a"))
        h.itemView.setOnClickListener { onClick(c) }
    }

    class VH(v: View) : RecyclerView.ViewHolder(v) {
        val tvInitials: TextView = v.findViewById(R.id.tvInitials)
        val tvName: TextView = v.findViewById(R.id.tvName)
        val tvPreview: TextView = v.findViewById(R.id.tvPreview)
        val tvTime: TextView = v.findViewById(R.id.tvTime)
        val tvUnread: View = v.findViewById(R.id.tvUnread)
        val avatarCard: CardView = v.findViewById(R.id.cardAvatar)
    }
}

// ─── MESSAGE ADAPTER ───────────────────────────────────────────────────────────
class MessageAdapter(
    private val currentUserId: String
) : ListAdapter<Message, MessageAdapter.VH>(MessageDiffCallback()) {

    private val sdf = SimpleDateFormat("h:mm a", Locale.getDefault())

    override fun getItemViewType(pos: Int) = if (getItem(pos).senderId == currentUserId) 1 else 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val layout = if (viewType == 1) R.layout.item_message_sent else R.layout.item_message_recv
        val v = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(h: VH, pos: Int) {
        val m = getItem(pos)
        h.tvText.text = m.text
        h.tvTime.text = sdf.format(Date(m.timestamp))
    }

    class VH(v: View) : RecyclerView.ViewHolder(v) {
        val tvText: TextView = v.findViewById(R.id.tvMessageText)
        val tvTime: TextView = v.findViewById(R.id.tvMessageTime)
    }

    class MessageDiffCallback : DiffUtil.ItemCallback<Message>() {
        override fun areItemsTheSame(oldItem: Message, newItem: Message) = oldItem.messageId == newItem.messageId
        override fun areContentsTheSame(oldItem: Message, newItem: Message) = oldItem == newItem
    }
}
