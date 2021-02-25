package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import org.parceler.Parcels;
import android.content.Intent;
import android.util.Log;
import android.text.TextWatcher;
import android.widget.TextView;
import android.widget.EditText;
import com.codepath.apps.restclienttemplate.models.Tweet;
import org.json.JSONException;

import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import okhttp3.Headers;

public class ComposeActivity extends AppCompatActivity {

    public static final int MAX_TWEET_LENGTH = 280;
    public static final String TAG = "ComposeActivity";
    public static final String outOf280 = "/280";

    Button btnTweet;
    TextView charCount;
    EditText etCompose;

    TweetClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        client = TwitterApp.getRestClient(this);
        btnTweet = findViewById(R.id.btnTweet);

        //set click listener on button
        btnTweet.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){

                final String tweetContent = etCompose.getText().toString();
                if(tweetContent.isEmpty()){

                    Toast.makeText(ComposeActivity.this, "Tweet can not be empty", Toast.LENGTH_LONG).show();
                    return;
                }

                Toast.makeText(ComposeActivity.this, tweetContent, Toast.LENGTH_SHORT).show();

                //make API call to Twitter to publish tweet
                client.publishTweet(tweetContent, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {

                        Log.i(TAG, "onSuccess to publish tweet");
                        try{

                            Tweet tweet = Tweet.fromJson(json.jsonObject);
                            Log.i(TAG, "Published tweet says: " + tweet);
                            Intent intent = new Intent();
                            intent.putExtra("tweet", Parcels.wrap(tweet));
                            //set result code and bundle data for response
                            setResult(RESULT_OK, intent);
                            //closes activity, pass data to parent
                            finish();

                        } catch(JSONException e){

                            e.printStackTrace();

                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {

                        Log.e(TAG, "onFailure to publish tweet", throwable);

                    }
                });
            }
        });

        etCompose = findViewById(R.id.etCompose);
        charCount = findViewById(R.id.charCount);
        etCompose.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Fires right as the text is being changed (even supplies the range of text)
                int countDown = etCompose.length();

                if(countDown > MAX_TWEET_LENGTH){

                    btnTweet.setEnabled(false);
                    Toast.makeText(ComposeActivity.this, "Tweet exceeds 280 characters", Toast.LENGTH_LONG).show();

                }
                //set to TextView
                charCount.setText(countDown + outOf280);
            }

    });
}
}