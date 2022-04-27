package com.rushikeshembadwar.newsapp

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.rushikeshembadwar.newsapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), NewsItemClicked {

    private lateinit var mAdapter: NewsListAdapter
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setting the layout manager for our recyclerview
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        // create an adapter for recyclerview
        fetchData()
        mAdapter = NewsListAdapter(this)
        binding.recyclerView.adapter = mAdapter
    }

    private fun fetchData(){

        val url = "https://saurav.tech/NewsAPI/everything/cnn.json"

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            {
                val newsJsonArray = it.getJSONArray("articles")
                val newsArray = ArrayList<News>()
                for(i in 0 until newsJsonArray.length()){
                    val newsJSONObject = newsJsonArray.getJSONObject(i)
                    val news = News(newsJSONObject.getString("title"), newsJSONObject.getString("author")
                                    ,newsJSONObject.getString("url"), newsJSONObject.getString("urlToImage"))
                    newsArray.add(news)
                }
                mAdapter.updateNews(newsArray)
            },
            {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show()
            })
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)

    }

    override fun onItemClicked(item: News) {
       // use chrome custom tabs to open news items in Chrome
        val builder = CustomTabsIntent.Builder();
        val customTabsIntent = builder.build();
        customTabsIntent.launchUrl(this, Uri.parse(item.url))

    }
}