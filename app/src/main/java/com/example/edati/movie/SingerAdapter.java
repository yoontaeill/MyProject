package com.example.edati.movie;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class SingerAdapter extends RecyclerView.Adapter<SingerAdapter.ViewHolder>{

    Context context;
    List<Movie> items = new ArrayList<>();

    OnItemClickListener listener;

    public SingerAdapter(Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.activity_movie, viewGroup ,false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        Movie item = items.get(i);
        holder.setItem(item);

        holder.setOnItemClickListener(listener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static interface OnItemClickListener {
        public void onItemClick(ViewHolder holder, View view, int position);
    }

    public SingerAdapter(Context context, List<Movie> items) {
        this.context = context;
        this.items = items;
    }

    public void addItem(Movie item) {
        items.add(item);
    }

    public void addItems(List<Movie> items) {
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public void clearItems() {
        items.clear();
        notifyDataSetChanged();
    }

    public Movie getItem(int position) {
        return items.get(position);
    }



    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    class ViewHolder extends RecyclerView.ViewHolder {  //이벤트 처리하는 곳
        TextView title;
        TextView pubDate;
        TextView director;
        TextView actor;
        ImageView image;
        RatingBar userRating;
        String link;

        OnItemClickListener listener;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = (TextView)itemView.findViewById(R.id.title);
            userRating = (RatingBar)itemView.findViewById(R.id.userRating);
            pubDate = (TextView)itemView.findViewById(R.id.pubDate);
            director = (TextView)itemView.findViewById(R.id.director);
            actor = (TextView)itemView.findViewById(R.id.actor);
            image = (ImageView)itemView.findViewById(R.id.image);

            itemView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(listener != null){
                        listener.onItemClick(ViewHolder.this, view, position);
                    }
                }
            });
        }

        public void setItem(Movie item) {
            title.setText(Html.fromHtml(item.getTitle()));
            userRating.setRating(item.getUserRating() / 2);
            pubDate.setText(Html.fromHtml(item.getPubDate()));
            director.setText(Html.fromHtml(item.getDirector()));
            actor.setText(Html.fromHtml(item.getActor()));
            Glide.with(context).load(item.getImage()).into(image);
        }

        public void setOnItemClickListener(OnItemClickListener listener) {
            this.listener = listener;
        }
    }

}
