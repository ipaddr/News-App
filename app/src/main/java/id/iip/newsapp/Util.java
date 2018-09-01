package id.iip.newsapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Utility class with methods to help perform the HTTP request and
 * parse the response and also parse a date object
 */
public class Util {

    /** Tag for the log messages */
    public static final String LOG_TAG = Util.class.getSimpleName();

    /**
     * example of date format is Jan 29, 2015
     */
    private static final String DATE_FORMAT = "MMM d, yyyy";

    /**
     * example of time format is 9:25 AM
     */
    private static final String TIME_FORMAT = "H:mm a";

    /**
     * Never instante this class
     */
    private Util(){}

    /**
     *
     * @param dateInput is date represented on string as input
     * @return date object base on string of input
     */
    public static Date getDateFromString(String dateInput){
        DateFormat df = new SimpleDateFormat(Util.DATE_FORMAT);
        Date date = null;
        try {
            date = df.parse(dateInput);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     *
     * @param dateInput is date object as input
     * @return date object represented as string
     */
    public static String getDateAsStringFromDate(Date dateInput){
        DateFormat df = new SimpleDateFormat(Util.DATE_FORMAT);
        String date = df.format(dateInput);
        return date;
    }

    /**
     *
     * @param dateInput is time represented on string as input
     * @return time object base on string of input
     */
    public static Date getTimeFromString(String dateInput){
        DateFormat df = new SimpleDateFormat(Util.TIME_FORMAT);
        Date date = null;
        try {
            date = df.parse(dateInput);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     *
     * @param dateInput is time object as input
     * @return time object represented as string
     */
    public static String getTimeAsStringFromDate(Date dateInput){
        DateFormat df = new SimpleDateFormat(Util.TIME_FORMAT);
        String date = df.format(dateInput);
        return date;
    }

    /**
     * Query the URL dataset and return an {@link News} object to represent a single news.
     */
    public static List<News> fetchNewsData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        // Extract relevant fields from the JSON response and create an {@link Event} object
        List<News> newss = extractNews(jsonResponse);

        // Return the {@link Event}
        return newss;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the news JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    public static boolean networkConnectivity(Context context){
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return a list of {@link News} objects that has been built up from
     * parsing a JSON response.
     */
    public static ArrayList<News> extractNews(String jsonInput) {

        // Create an empty ArrayList that we can start adding newses to
        ArrayList<News> newses = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // TODO: Parse the response given by the SAMPLE_JSON_RESPONSE string and
            // build up a list of news objects with the corresponding data.
            JSONObject rootObject = new JSONObject(jsonInput);
            JSONObject jsonObject = rootObject.getJSONObject("response");
            JSONArray features = jsonObject.getJSONArray("results");
            for(int i=0; i<features.length();i++){
                JSONObject object = features.getJSONObject(i);
                String type = object.getString("type");
                String sectionName = object.getString("sectionName");
                String webTitle = object.getString("webTitle");
                String webUrl = object.getString("webUrl");
                JSONArray tags = object.getJSONArray("tags");
                List<String> authors = new ArrayList<>();
                for(int j=0; j < tags.length(); j++){
                    JSONObject tagObject = tags.getJSONObject(j);
                    authors.add(tagObject.getString("webTitle"));
                }
                newses.add(new News(type, sectionName, webTitle, webUrl, authors));
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the news JSON results", e);
        }

        // Return the list of newses
        return newses;
    }
}
