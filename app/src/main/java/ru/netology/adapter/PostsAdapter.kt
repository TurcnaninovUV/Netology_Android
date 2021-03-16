package ru.netology.adapter


import android.content.ContentValues.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.netology.R
import ru.netology.databinding.CardPostBinding
import ru.netology.dto.Post

typealias OnLikeListener = (post: Post) -> Unit
typealias OnRepostListener = (post: Post) -> Unit

class PostsAdapter(private val onLikeListener: OnLikeListener, private val onRepostListener: OnRepostListener) : RecyclerView.Adapter<PostViewHolder>() {

    var list = emptyList<Post>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, onLikeListener, onRepostListener)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = list[position]
        holder.bind(post)
    }

    override fun getItemCount(): Int = list.size
}


class PostViewHolder(
        private val binding: CardPostBinding,
        private val onLikeListener: OnLikeListener,
        private val onRepostListener: OnRepostListener
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(post: Post) {
        binding.apply {
            author.text = post.author
            published.text = post.published
            content.text = post.content
            likesCount.text = reduction(post.likes)
            repostCount.text = reduction(post.repost)
            like.setImageResource(
                    if (post.likedByMe) R.drawable.ic_baseline_favorite_24 else R.drawable.ic_baseline_favorite_border_24
            )

            like.setOnClickListener {
                onLikeListener(post)
            }
            repost.setOnClickListener {
                onRepostListener(post)
            }
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
}



