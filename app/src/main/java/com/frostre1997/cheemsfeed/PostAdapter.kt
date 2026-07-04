package com.frostre1997.cheemsfeed

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.frostre1997.cheemsfeed.databinding.ItemPostBinding
import com.frostre1997.cheemsfeed.model.PostData
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PostAdapter(
    private val onPostClick: (PostData) -> Unit
) : ListAdapter<PostData, PostAdapter.PostViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = ItemPostBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return PostViewHolder(binding, onPostClick)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class PostViewHolder(
        private val binding: ItemPostBinding,
        private val onPostClick: (PostData) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(post: PostData) {
            binding.postTitle.text = post.title
            binding.postAuthor.text = "r/${post.subreddit ?: "unknown"} • u/${post.author ?: "[deleted]"}"
            binding.postDate.text = formatTime(post.created_utc ?: 0L)
            binding.scoreText.text = "▲ ${post.score ?: 0}"
            binding.commentsText.text = "💬 ${post.num_comments ?: 0}"
            binding.root.setOnClickListener { onPostClick(post) }
        }

        private fun formatTime(timestamp: Long): String {
            if (timestamp == 0L) return ""
            val diff = (System.currentTimeMillis() / 1000) - timestamp
            return when {
                diff < 60 -> "now"
                diff < 3600 -> "${diff / 60}m"
                diff < 86400 -> "${diff / 3600}h"
                diff < 604800 -> "${diff / 86400}d"
                else -> SimpleDateFormat("MMM dd", Locale.getDefault())
                    .format(Date(timestamp * 1000))
            }
        }
    }

    object DiffCallback : DiffUtil.ItemCallback<PostData>() {
        override fun areItemsTheSame(old: PostData, new: PostData) =
            old.id == new.id

        override fun areContentsTheSame(old: PostData, new: PostData) =
            old == new
    }
}
