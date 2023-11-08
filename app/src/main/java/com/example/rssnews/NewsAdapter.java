package com.example.rssnews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class NewsAdapter extends BaseAdapter {
    ArrayList<News> list;
    Context context;
    int layout;

    public NewsAdapter(ArrayList<News> list, Context context, int layout) {
        this.list = list;
        this.context = context;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder viewHolder;
        if(view==null){
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(layout,null);

            viewHolder.img = view.findViewById(R.id.imageView);
            viewHolder.txtTitle = view.findViewById(R.id.title);
            viewHolder.txtDesc = view.findViewById(R.id.desc);

            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }

        News news = list.get(position);
        viewHolder.txtTitle.setText(news.getTitle());
        viewHolder.txtDesc.setText(news.getDesc());
        Picasso.get().load(news.getImage()).into(viewHolder.img);
        return view;
    }

    class ViewHolder{
        TextView txtTitle;
        TextView txtDesc;
        ImageView img;
    }
}
