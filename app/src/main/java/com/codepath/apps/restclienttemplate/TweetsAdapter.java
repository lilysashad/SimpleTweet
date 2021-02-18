package com.codepath.apps.restclienttemplate;

import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import androidx.annotation.NonNull;
import com.codepath.apps.restclienttemplate.models.Tweet;
import android.content.Context;
import java.util.List;
import android.widget.RelativeLayout;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder> {

    Context context;

    List<Tweet> tweets;

    //Pass in context and list of tweets

    public TweetsAdapter(Context context, List<Tweet> tweets) {
        this.context = context;
        this.tweets = tweets;
    }

    //For each row, inflate layout
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_tweet, parent, false);
        return new ViewHolder(view);
    }

    //Bind values based on position of element
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        //Get data at position
        Tweet tweet = tweets.get(position);

        //Bind tweet with viewholder
        holder.bind(tweet);

    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    //clean all elements of recycler
    public void clear(){

        tweets.clear();

        notifyDataSetChanged();

    }

    //Add list of items to dataset
    public void addAll(List<Tweet> tweetList){

        tweets.addAll(tweetList);

        notifyDataSetChanged();

    }

    //Define viewholder
    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView ivProfileImage;
        TextView tvBody;
        TextView tvScreenName;
        TextView timeStamp;
        RelativeLayout relativeLayout;
        int radius = 20;
        int margin = 0;

        public ViewHolder(@NonNull View itemView){

            super(itemView);

            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);

            tvBody = itemView.findViewById(R.id.tvBody);

            tvScreenName = itemView.findViewById(R.id.tvScreenName);

            timeStamp = itemView.findViewById(R.id.timeStamp);

            relativeLayout = itemView.findViewById(R.id.relativeLayout);

        }

        public void bind(Tweet tweet){

            tvBody.setText(tweet.body);

            tvScreenName.setText(tweet.user.screenName);

            Glide.with(context).load(tweet.user.profileImageUrl).transform(new RoundedCornersTransformation(radius, margin)).into(ivProfileImage);

            timeStamp.setText(tweet.getFormattedTimestamp());

            // 1. Register click listener on whole row
            relativeLayout.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v){

                    // 2. Navigate to new activity on tap
                    Intent i = new Intent(context, DetailActivity.class);
                    context.startActivity(i);

                }

            });

        }

    }

}
