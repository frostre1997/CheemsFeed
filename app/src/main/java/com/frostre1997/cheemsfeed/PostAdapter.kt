package com.frostre1997.cheemsfeed

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.frostre1997.cheemsfeed.databinding.ItemPostBinding
import com.frostre1997.cheemsfeed.network.PublicPost
import java.text.SimpleDateFormat
import java.util.*

class PostAdapter(
    private val onItemClick: (PublicPost) -> Unit
) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    private var posts: List<PublicPost> = emptyList()

    fun submitList(newPosts: List<PublicPost>) {
        posts = newPosts
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = posts[position]
        holder.bind(post)
        holder.itemView.setOnClickListener { onItemClick(post) }
    }

    override fun getItemCount(): Int = posts.size

    class PostViewHolder(private val binding: ItemPostBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(post: PublicPost) {
            // Match your layout IDs
            binding.postTitle.text = post.title
            
            // Author (handle null)
            binding.postAuthor.text = post.author ?: "Unknown"
            
            // Date (format the timestamp)
            post.createdUtc?.let { timestamp ->
                val date = Date((timestamp * 1000).toLong())
                val formatter = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
                binding.postDate.text = formatter.format(date)
            } ?: run {
                binding.postDate.text = ""
            }
            
            // Score
            binding.scoreText.text = "⬆ ${post.score}"
            
            // Comments
            binding.commentsText.text = "${post.numComments} comments"
        }
    }
}
