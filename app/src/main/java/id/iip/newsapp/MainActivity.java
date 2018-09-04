package id.iip.newsapp;

import android.content.Intent;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>>{
    private String URL_NEWS = null;

    // variable resource
    private ListView newsListView;

    // adapter of list view
    private NewsAdapter mAdapter;

    /** TextView that is displayed when the list is empty */
    private TextView mEmptyStateTextView;

    /**
     * loading indicator
     */
    private View loadingIndicator;

    /**
     * Constant value for the news loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int news_LOADER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create a fake list of news locations.
        final ArrayList<News> newss = new ArrayList<>();

        // Find a reference to the {@link ListView} in the layout
        newsListView = (ListView) findViewById(R.id.list);
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        newsListView.setEmptyView(mEmptyStateTextView);
        // Hide loading indicator because the data has been loaded
        loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        // add click action to the list view
        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                News news = newss.get(position);
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(news.getWebUrl()));
                startActivity(browserIntent);
            }
        });

        // Create a new {@link ArrayAdapter} of newss
        mAdapter = new NewsAdapter(MainActivity.this, newss);

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        newsListView.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String query = PreferenceManager.getDefaultSharedPreferences(this).getString(getString(R.string.query_key), getString(R.string.debates));

        // default query = debates
        URL_NEWS = MainActivity.getNewsURL(query).toString();

        if (Util.networkConnectivity(this)){
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getSupportLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.restartLoader(news_LOADER_ID, null, this);
        } else {
            mAdapter.clear();
            mEmptyStateTextView.setText(R.string.no_inet);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {
        loadingIndicator.setVisibility(View.VISIBLE);
        // Create a new loader for the given URL
        return new GuardianAPIAsynTaskLoader(this, URL_NEWS);
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> newses) {
        loadingIndicator.setVisibility(View.GONE);

        // Set empty state text to display "No newss found."
        mEmptyStateTextView.setText(R.string.no_news);

        // Clear the adapter of previous news data
        mAdapter.clear();

        if (newses != null || !newses.isEmpty())
            mAdapter.addAll(newses);
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        mAdapter.clear();
    }

    public static Uri getNewsURL(String query){
        return new Uri.Builder().scheme("http")
                .authority("content.guardianapis.com")
                .appendPath("search")
                .appendQueryParameter("api-key", "key")
                .appendQueryParameter("show-tags", "contributor")
                .appendQueryParameter("q", query).build()
        ;
    }
}
