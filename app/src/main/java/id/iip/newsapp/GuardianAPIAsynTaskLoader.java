package id.iip.newsapp;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.List;

public class GuardianAPIAsynTaskLoader extends AsyncTaskLoader<List<News>> {

    /** Member variable to store url resource that we need to call */
    private String mUrl;

    /**
     * default contructur
     * @param context is context of this class
     * @param url is url to store url that we need to call
     */
    public GuardianAPIAsynTaskLoader(Context context, String url){
        super(context);
        this.mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * This is on a background thread.
     */
    @Override
    public List<News> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of newses.
        List<News> newses = Util.fetchNewsData(mUrl);
        return newses;
    }
}
