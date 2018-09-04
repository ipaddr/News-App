package id.iip.newsapp;

import android.content.Context;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class NewsAdapter extends ArrayAdapter<News> {

    private boolean showAuthor = true;
    private boolean showName = true;
    private boolean showTitle = true;
    private boolean showPublicationDate = true;

    /**
     *
     * @param context is context of this class
     * @param newses is list of news
     */
    public NewsAdapter(Context context, List<News> newses){
        super(context, 0, newses);
    }

    public void updateData(){
        Context context = getContext();
        Resources r = context.getResources();
        showAuthor = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(r.getString(R.string.author_key), true);
        showName = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(r.getString(R.string.name_key), true);
        showTitle = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(r.getString(R.string.title_key), true);
        showPublicationDate = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(r.getString(R.string.publication_date_key), true);
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
            holder.publicationDate = (TextView)convertView.findViewById(R.id.publicationDate);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        News news = getItem(position);

        if (showAuthor) {
            holder.author.setVisibility(View.VISIBLE);
            holder.author.setText(news.getContibutor().toString().replace("[","").replace("]", ""));
        }  else holder.author.setVisibility(View.GONE);

        if (showName){
            holder.sectionName.setVisibility(View.VISIBLE);
            holder.sectionName.setText(news.getSectionName());
        } else holder.sectionName.setVisibility(View.GONE);

        if (showTitle){
            holder.webTitle.setVisibility(View.VISIBLE);
            holder.webTitle.setText(news.getWebTitle());
        } else holder.webTitle.setVisibility(View.GONE);

        if (showPublicationDate){
            holder.publicationDate.setVisibility(View.VISIBLE);
            holder.publicationDate.setText(Util.dateToString(news.getPublicationDate()));
        } else holder.publicationDate.setVisibility(View.GONE);

        return convertView;
    }

    // somewhere else in your class definition
    static class ViewHolder {
        TextView author;
        TextView sectionName;
        TextView webTitle;
        TextView publicationDate;
    }
}
