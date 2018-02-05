package com.sccreporte.reporte.utilities;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by simpson on 2/4/2018.
 * These utilities will be used to communicate with the network.
 */

public class NetworkUtils {

    final static String BASE_REPORTS_URL = "https://api.github.com/search/repositories";

    final static String PARAM_QUERY_USER_ID = "q";

    /**
     * Builds the URL used to query sccreporte.
     *
     * @param reportsIdUserQuery The user id that will be queried for.
     * @return The URL to use to query the sccreporte.
     */
    public static URL buildReportsUrl(String reportsIdUserQuery) {
        Uri builtUri = Uri.parse(BASE_REPORTS_URL).buildUpon()
                .appendQueryParameter(PARAM_QUERY_USER_ID, reportsIdUserQuery)
                .appendQueryParameter("sort", "stars")
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    /**
     * Returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
