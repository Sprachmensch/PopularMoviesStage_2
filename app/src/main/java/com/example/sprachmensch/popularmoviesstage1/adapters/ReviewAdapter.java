package com.example.sprachmensch.popularmoviesstage1.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sprachmensch.popularmoviesstage1.R;

import java.util.List;

public class ReviewAdapter extends BaseAdapter {
    private final Context context;
    private final List<String> reviews;

    public ReviewAdapter(Context context, List<String> reviews) {
        this.context = context;
        this.reviews = reviews;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final String review = reviews.get(position);

        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.detail_review, null);
        }

        final TextView textView= convertView.findViewById(R.id.review_tv);
        textView.setText(review);
//        Log.d("Reviews", "review: " + review);

        return convertView;
    }

    @Override
    public int getCount() {
        return reviews.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


}
