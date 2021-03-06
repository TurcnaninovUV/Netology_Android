package ru.netology.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.netology.BuildConfig
import ru.netology.R
import ru.netology.databinding.CardPostBinding
import ru.netology.dto.AttachmentType
import ru.netology.dto.Post
import ru.netology.view.load
import ru.netology.view.loadCircleCrop

interface OnInteractionListener {
    fun onLike(post: Post) {}
    fun onEdit(post: Post) {}
    fun onRemove(post: Post) {}
    fun onShare(post: Post) {}
    fun onPlay(post: Post) {}
    fun onShowPost(post: Post) {}
    fun onViewImage(post: Post) {}
}

class PostsAdapter(private val onInteractionListener: OnInteractionListener) : ListAdapter<Post, PostViewHolder>(PostViewHolder.PostDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, onInteractionListener)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }
}

class PostViewHolder(
        private val binding: CardPostBinding,
        private val onInteractionListener: OnInteractionListener
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(post: Post) {
        binding.apply {
            author.text = post.author
            published.text = post.published
            content.text = post.content
            avatar.loadCircleCrop("${BuildConfig.BASE_URL}/avatars/${post.authorAvatar}")
            like.text = reduction(post.likes)
            repost.text = reduction(post.repost)
            like.isChecked = post.likedByMe

            if (post.attachment != null && post.attachment!!.type == AttachmentType.IMAGE) {
                videoImage.visibility = View.VISIBLE
                videoImage.load("${BuildConfig.BASE_URL}/media/${post.attachment?.url}")
            } else videoImage.visibility = View.GONE

            menu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.options_post)
                    // TODO: if we don't have other options, just remove dots
                    menu.setGroupVisible(R.id.owned, post.ownedByMe)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.remove -> {
                                onInteractionListener.onRemove(post)
                                true
                            }
                            R.id.edit -> {
                                onInteractionListener.onEdit(post)
                                true
                            }
                            else -> false
                        }
                    }
                }.show()
            }

            like.setOnClickListener {
                onInteractionListener.onLike(post)
            }
            repost.setOnClickListener {
                onInteractionListener.onShare(post)
            }
            play.setOnClickListener {
                onInteractionListener.onPlay(post)
            }
            videoImage.setOnClickListener {
                onInteractionListener.onPlay(post)
            }
            listPost.setOnClickListener {
                onInteractionListener.onShowPost(post)
            }
            content.setOnClickListener {
                onInteractionListener.onShowPost(post)
            }

            videoImage.setOnClickListener {
                onInteractionListener.onViewImage(post)
            }
            if (post.video != null) groupVideo.visibility = View.VISIBLE else groupVideo.visibility = View.GONE
        }
    }

    private fun reduction(count: Int): String {
        return when {
            (count >= 1_000_000) -> "${"%.1f".format(count / 1_000_000.toDouble())}M"
            (count in 1000..9_999) -> "${"%.1f".format(count / 1_000.toDouble())}K"
            (count in 10_000..999_999) -> "${count / 1000}K"
            else -> count
        }.toString()
    }

    class PostDiffCallback : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem == newItem
        }
    }
}

