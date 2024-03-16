package com.example.flixster
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.RequestParams
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.Headers
import org.json.JSONArray
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.random.Random


data class MovieItem(val name: String, val description: String, val image: String)

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL ,false)
        requestMovies()

    }

    private suspend fun fetchMovies(): MutableList<MovieItem>? = suspendCancellableCoroutine { continuation ->
        try {
            val client = AsyncHttpClient()
            val params = RequestParams()
            val movieItems = mutableListOf<MovieItem>()
            client.get(
                "https://api.themoviedb.org/3/movie/now_playing?&api_key=$apiKey",
                params, object : JsonHttpResponseHandler() {
                    override fun onSuccess(
                        statusCode: Int,
                        headers: Headers,
                        json: JSON
                    ) {
                        val jsonArray = json.jsonObject.getJSONArray("results")
//                        Log.e("json", jsonArray.toString())
                        for (i in 0 until jsonArray.length()){
                            val item = jsonArray.getJSONObject(i)
                            Log.e("item", item.toString())
                            val movie = MovieItem(
                                item.getString("original_title"),
                                item.getString("overview"),
                                item.getString("poster_path"),
                            )
                            movieItems.add(movie)
                            Log.e("movies", movieItems.toString())
                        }
                        continuation.resume(movieItems)
                    }

                    override fun onFailure(
                        statusCode: Int,
                        headers: Headers?,
                        errorResponse: String,
                        t: Throwable?
                    ) {
                        continuation.resume(null)
                    }
                })
        } catch (e: Exception) {
            continuation.resumeWithException(e)
        }
    }

    private fun updateUI(result: List<MovieItem>) {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.adapter = ArtworkAdapter(result)
    }


    private fun requestMovies() {
        GlobalScope.launch(Dispatchers.Main) {
            val movieItems = fetchMovies()
            if (movieItems != null) {
                updateUI(movieItems)
            }
        }
    }
}




