package com.codepath.apps.restclienttemplate;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {

    public static final String REST_CONSUMER_KEY = BuildConfig.CONSUMER_KEY;
    public static final String REST_CONSUMER_SECRET = BuildConfig.CONSUMER_SECRET;

    ImageView ivProfileImage;
    TextView tvBody;
    TextView tvScreenName;
    TextView timeStamp;
    RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ivProfileImage = findViewById(R.id.ivProfileImage);

        tvBody = findViewById(R.id.tvBody);

        tvScreenName = findViewById(R.id.tvScreenName);

        timeStamp = findViewById(R.id.timeStamp);

        relativeLayout = findViewById(R.id.relativeLayout);

    }

}
