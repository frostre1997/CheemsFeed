package com.frostre1997.cheemsfeed

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.frostre1997.cheemsfeed.databinding.ItemPostBinding
import com.frostre1997.cheemsfeed.network.PublicPost

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
            // MAKE SURE THESE IDs EXACTLY MATCH YOUR item_post.xml
            binding.tvTitle.text = post.title
            binding.tvSubreddit.text = post.subreddit
            binding.tvScore.text = post.score.toString()
            binding.tvComments.text = "${post.numComments} comments"
        }
    }
}
