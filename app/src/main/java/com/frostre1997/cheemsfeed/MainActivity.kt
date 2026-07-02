package com.frostre1997.cheemsfeed

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Find the RecyclerView by ID
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        
        // Set the LayoutManager
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Create dummy data for testing
        val dummyData = listOf(
            "Welcome to CheemsFeed",
            "This is a test post",
            "Minimalist Reddit Client",
            "Android development from scratch"
        )

        // Attach the adapter to the RecyclerView
        recyclerView.adapter = PostAdapter(dummyData)
    }
}

