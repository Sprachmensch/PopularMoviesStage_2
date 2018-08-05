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

import com.example.sprachmensch.popularmoviesstage1.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TrailerAdapter extends BaseAdapter {
    private final Context context;
    private final List<String> trailers;

    public TrailerAdapter(Context context, List<String> trailers) {
        this.context = context;
        this.trailers = trailers;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final String trailer = trailers.get(position);

        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.detail_trailer, null);
        }

        final ImageView imageView = convertView.findViewById(R.id.poster_iv);
        final String YTThumb = "https://img.youtube.com/vi/" + trailer + "/mqdefault.jpg";
        ;
        Log.d("trailers", "trailer YTThumb: " + trailer);
        final String YTKey = "vn9mMeWcgoM";

        imageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent ytIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://www.youtube.com/watch?v=" + trailer));

                context.startActivity(ytIntent);
            }
        });


        Picasso.get()
                .load(YTThumb)
                .placeholder(R.drawable.ic_sync_black_24dp)
                .error(R.drawable.ic_sync_problem_24dp)
                .into(imageView);

        return convertView;
    }


    @Override
    public int getCount() {
        return trailers.size();
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
