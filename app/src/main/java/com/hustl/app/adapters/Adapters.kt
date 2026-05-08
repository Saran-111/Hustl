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
        h.tvPrice.text = "₹${"%,d".format(gig.minPrice)}"
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
class OrderAdapter(
    private val currentUserId: String,
    private var orders: List<Order>,
    private val onOrderClick: (Order, String) -> Unit
) :
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
        h.tvSeller.text = "Hustlr: ${o.sellerName}"
        h.tvPrice.text = "₹${"%,d".format(o.price)}"

        val (statusText, statusColor) = when (o.status) {
            "active" -> "Active" to "#6bffc8"
            "under_review" -> "In Review" to "#fb923c"
            "completed" -> "Completed" to "#e8ff6b"
            "cancelled" -> "Cancelled" to "#ff4d4d"
            "pending" -> "Pending" to "#fb923c"
            else -> "Unknown" to "#9a9590"
        }
        h.tvStatus.text = statusText
        h.tvStatus.setTextColor(Color.parseColor(statusColor))

        h.progressBar.visibility = View.GONE
        h.layoutActions.visibility = View.GONE
        h.btnPrimaryAction.visibility = View.GONE
        h.btnSecondaryAction.visibility = View.GONE
        h.tvRevisionNote.visibility = View.GONE

        if (o.revisionNote.isNotEmpty()) {
            h.tvRevisionNote.visibility = View.VISIBLE
            h.tvRevisionNote.text = "Revision Note: ${o.revisionNote}"
        }

        val isSeller = o.sellerId == currentUserId

        when (o.status) {
            "active" -> {
                h.progressBar.visibility = View.VISIBLE
                h.progressBar.progress = o.progress
                h.layoutActions.visibility = View.VISIBLE
                h.btnPrimaryAction.visibility = View.VISIBLE
                h.btnSecondaryAction.visibility = View.VISIBLE
                h.btnSecondaryAction.text = "Cancel Order"
                h.btnSecondaryAction.setOnClickListener { onOrderClick(o, "cancel") }

                if (isSeller) {
                    h.btnPrimaryAction.text = "Mark for Review"
                    h.btnPrimaryAction.setOnClickListener { onOrderClick(o, "mark_review") }
                } else {
                    h.btnPrimaryAction.text = "Order is Active"
                    h.btnPrimaryAction.setBackgroundResource(R.drawable.bg_btn_outline)
                    h.btnPrimaryAction.setTextColor(Color.parseColor("#9a9590"))
                    h.btnPrimaryAction.setOnClickListener(null)
                }
            }
            "under_review" -> {
                h.layoutActions.visibility = View.VISIBLE
                h.btnPrimaryAction.visibility = View.VISIBLE
                if (isSeller) {
                    h.btnPrimaryAction.text = "Waiting for Buyer"
                    h.btnPrimaryAction.setBackgroundResource(R.drawable.bg_btn_outline)
                    h.btnPrimaryAction.setTextColor(Color.parseColor("#9a9590"))
                    h.btnPrimaryAction.setOnClickListener(null)
                } else {
                    h.btnPrimaryAction.text = "Verify & Complete"
                    h.btnPrimaryAction.setBackgroundResource(R.drawable.bg_btn_accent)
                    h.btnPrimaryAction.setTextColor(Color.parseColor("#1a1a1a"))
                    h.btnPrimaryAction.setOnClickListener { onOrderClick(o, "verify") }

                    h.btnSecondaryAction.visibility = View.VISIBLE
                    h.btnSecondaryAction.text = "Request Revision"
                    h.btnSecondaryAction.setOnClickListener { onOrderClick(o, "revision") }
                }
            }
            "completed" -> {
                h.layoutActions.visibility = View.VISIBLE
                h.btnPrimaryAction.visibility = View.VISIBLE
                if (isSeller) {
                    h.btnPrimaryAction.text = "✓ Completed"
                    h.btnPrimaryAction.setBackgroundResource(R.drawable.bg_btn_outline)
                    h.btnPrimaryAction.setTextColor(Color.parseColor("#e8ff6b"))
                    h.btnPrimaryAction.setOnClickListener(null)
                } else {
                    h.btnPrimaryAction.text = "Leave a Review"
                    h.btnPrimaryAction.setBackgroundResource(R.drawable.bg_btn_outline)
                    h.btnPrimaryAction.setTextColor(Color.parseColor("#ffffff"))
                    // Review logic handled in detail view or separate flow
                    h.btnPrimaryAction.setOnClickListener(null)
                }
            }
        }
    }

    class VH(v: View) : RecyclerView.ViewHolder(v) {
        val tvOrderId: TextView = v.findViewById(R.id.tvOrderId)
        val tvTitle: TextView = v.findViewById(R.id.tvOrderTitle)
        val tvSeller: TextView = v.findViewById(R.id.tvOrderSeller)
        val tvPrice: TextView = v.findViewById(R.id.tvOrderPrice)
        val tvStatus: TextView = v.findViewById(R.id.tvOrderStatus)
        val progressBar: android.widget.ProgressBar = v.findViewById(R.id.progressBar)
        val layoutActions: View = v.findViewById(R.id.layoutActions)
        val btnPrimaryAction: TextView = v.findViewById(R.id.btnPrimaryAction)
        val btnSecondaryAction: TextView = v.findViewById(R.id.btnSecondaryAction)
        val tvRevisionNote: TextView = v.findViewById(R.id.tvRevisionNote)
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

// ─── NOTIFICATION ADAPTER ──────────────────────────────────────────────────────
class NotificationAdapter(
    private val notifications: List<com.hustl.app.data.repository.HustlNotification>
) : RecyclerView.Adapter<NotificationAdapter.VH>() {

    override fun getItemCount() = notifications.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_notification, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(h: VH, pos: Int) {
        val n = notifications[pos]
        h.tvTitle.text = n.title
        h.tvBody.text = n.body
        h.tvIcon.text = when (n.type) {
            "order" -> "📦"
            "review" -> "⭐"
            else -> "🔔"
        }
        
        val sdf = SimpleDateFormat("MMM d, h:mm a", Locale.getDefault())
        h.tvTime.text = sdf.format(Date(n.timestamp))
    }

    class VH(v: View) : RecyclerView.ViewHolder(v) {
        val tvIcon: TextView = v.findViewById(R.id.tvIcon)
        val tvTitle: TextView = v.findViewById(R.id.tvTitle)
        val tvBody: TextView = v.findViewById(R.id.tvBody)
        val tvTime: TextView = v.findViewById(R.id.tvTime)
    }
}
