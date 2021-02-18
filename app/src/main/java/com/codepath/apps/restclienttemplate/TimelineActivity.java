package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import okhttp3.Headers;
import android.util.Log;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.codepath.apps.restclienttemplate.models.Tweet;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import org.json.JSONArray;
import org.json.JSONException;


public class TimelineActivity extends AppCompatActivity {

    public static final String TAG = "TimelineActivity";

    TweetClient client;
    RecyclerView rvTweets;
    List<Tweet> tweets;
    TweetsAdapter adapter;
    SwipeRefreshLayout swipeContainer;
    EndlessRecyclerViewScrollListener scrollListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        client = TwitterApp.getRestClient(this);

        swipeContainer = findViewById(R.id.swipeContainer);
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                Log.i(TAG, "fetching new data!");

                populateHomeTimeline();

            }
        });

        //Find recycler view
        rvTweets = findViewById(R.id.rvTweets);

        //Initialize list of tweets and adapter
        tweets = new ArrayList<>();
        adapter = new TweetsAdapter(this, tweets);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        //Recycler view setup: layout manager and adapter
        rvTweets.setLayoutManager(layoutManager);
        rvTweets.setAdapter(adapter);

        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {

                Log.i(TAG, "onLoadMore: " + page);

                loadMoreData();

            }
        };

        //Adds scroll listener to RecyclerView
        rvTweets.addOnScrollListener(scrollListener);

        populateHomeTimeline();

    }

    private void loadMoreData(){

        // 1. Send an API request to retrieve appropriate paginated data
        client.getNextPageOfTweets(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {

                Log.i(TAG, "onSuccess for loadMoreData!" + json.toString());

                // 2. Deserialize and construct new model objects from the API response
                JSONArray jsonArray = json.jsonArray;
                try{

                    List<Tweet> tweets = Tweet.fromJsonArray(jsonArray);

                    // 3. Append the new data objects to the existing set of items inside the array of items

                    // 4. Notify the adapter of the new items made with `notifyItemRangeInserted()`

                    adapter.addAll(tweets);

                } catch(JSONException e){

                    e.printStackTrace();

                }

            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {

                Log.e(TAG, "onFailure for loadMoreData!", throwable);

            }

        }, tweets.get(tweets.size() - 1).id);

    }

    private void populateHomeTimeline(){

        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {

                Log.i(TAG, "onSuccess!" + json.toString());

                JSONArray jsonArray = json.jsonArray;
                // Now we call setRefreshing(false) to signal refresh has finished
                swipeContainer.setRefreshing(false);

                try {
                    adapter.clear();
                    adapter.addAll(Tweet.fromJsonArray(jsonArray));

                } catch (JSONException e) {
                    Log.e(TAG, "Json exception", e);
                    e.printStackTrace();

                }

            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {

                Log.e(TAG, "onFailure! " + response, throwable);

            }
        });

    }
}