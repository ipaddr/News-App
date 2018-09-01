package id.iip.newsapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class NewsAdapter extends ArrayAdapter<News> {

    /**
     *
     * @param context is context of this class
     * @param newses is list of news
     */
    public NewsAdapter(Context context, List<News> newses){
        super(context, 0, newses);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder  holder;
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, null);

            holder = new ViewHolder();
            holder.author = (TextView)convertView.findViewById(R.id.authors);
            holder.sectionName = (TextView)convertView.findViewById(R.id.sectionName);
            holder.webTitle = (TextView)convertView.findViewById(R.id.webTitle);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        News news = getItem(position);

        holder.author.setText(news.getContibutor().toString().replace("[","").replace("]", ""));
        holder.sectionName.setText(news.getSectionName());
        holder.webTitle.setText(news.getWebTitle());

        return convertView;
    }

    // somewhere else in your class definition
    static class ViewHolder {
        TextView author;
        TextView sectionName;
        TextView webTitle;
    }
}
