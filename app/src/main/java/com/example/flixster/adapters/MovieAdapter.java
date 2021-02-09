package com.example.flixster.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.Image;
import android.os.Parcel;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.example.flixster.DetailedActivity;
import com.example.flixster.MainActivity;
import com.example.flixster.R;
import com.example.flixster.models.Movie;

import org.parceler.Parcels;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MovieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final String TAG = "MovieAdapter";
    public static final double STAR_LIMIT = 6.5;
    private final int POPULAR = 1;
    private final int NORMAL = 0;

    Context context;
    List<Movie> movies;

    public MovieAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    //Returns the view type of the item at position for the purposes of view recycling.
    @Override
    public int getItemViewType(int position) {
        Movie movie = movies.get(position);
        if (movie.getStars() > STAR_LIMIT) {
            return POPULAR;
        } else {
            return NORMAL;
        }
    }

    // Usually involves inflating a layout from XML and returning the holder
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(context);

        Log.d(TAG, "onCreateViewHolder");
        if (viewType == POPULAR) {
            // Inflate XML file
            View view1 = inflater.inflate(R.layout.item_movie_popular, parent, false);
            // Wrap this view inside ViewHolder
            viewHolder = new PopularViewHolder(view1);
        } else {
            // Inflate XML file
            View view2 = inflater.inflate(R.layout.item_movie, parent, false);
            // Wrap this view inside ViewHolder
            viewHolder = new NormalViewHolder(view2);
        }
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        // Get movie at the passed position
        Movie movie = movies.get(position);

        Log.d(TAG, "onBindHolder " + position);
        if (viewHolder.getItemViewType() == POPULAR) {
            PopularViewHolder holder = (PopularViewHolder) viewHolder;
            // Bind the movie data into the ViewHolder
            holder.bind(movie);
        } else {
            NormalViewHolder holder = (NormalViewHolder) viewHolder;
            // Bind the movie data into the ViewHolder
            holder.bind(movie);
        }
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class PopularViewHolder extends RecyclerView.ViewHolder {

        ImageView ivBackdrop;
        RelativeLayout container;

        public PopularViewHolder(@NonNull View itemView) {
            super(itemView);
            ivBackdrop = itemView.findViewById(R.id.ivBackdrop);
            container = itemView.findViewById(R.id.container);
        }

        public void bind(Movie movie) {
            // Load image into ImageView
            int radius = 25; // corner radius, higher value = more rounded
            int margin = 10; // crop margin, set to 0 for corners with no crop
            Glide.with(context)
                    .load(movie.getBackdropPath())
                    .placeholder(R.drawable.ic_launcher_background)
                    .transform(new RoundedCornersTransformation(radius, margin))
                    .into(ivBackdrop);

            // Register click listener on the whole row
            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Navigate to a new activity on tap
                    Intent i = new Intent(context, DetailedActivity.class);
                    i.putExtra("movie", Parcels.wrap(movie));
                    context.startActivity(i);
                }
            });
        }
    }

    public class NormalViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout container;
        TextView tvTitle;
        TextView tvOverview;
        ImageView ivPoster;
        ImageView ivProfile;

        public NormalViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvOverview = itemView.findViewById(R.id.tvOverview);
            ivPoster = itemView.findViewById(R.id.ivPoster);
            container = itemView.findViewById(R.id.container);
            ivProfile = itemView.findViewById(R.id.ivProfile);
        }

        public void bind(Movie movie) {
            tvTitle.setText(movie.getTitle());
            tvOverview.setText(movie.getOverview());
            String imageUrl;
            // if phone is in landscape, then imageUrl = backdrop
            if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                imageUrl = movie.getBackdropPath();
            } else {
                //else imageUrl = poster
                imageUrl = movie.getPosterPath();
            }
            // Load image into ImageView
            int radius = 25; // corner radius, higher value = more rounded
            int margin = 10; // crop margin, set to 0 for corners with no crop
            Glide.with(context)
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_launcher_background)
                    .transform(new RoundedCornersTransformation(radius, margin))
                    .into(ivPoster);

            // Register click listener on the whole row
            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Navigate to a new activity on tap
                    // Toast.makeText(context, movie.getTitle(), Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(context, DetailedActivity.class);
                    i.putExtra("movie", Parcels.wrap(movie));

                    context.startActivity(i);
                }
            });
        }
    }
}
