package com.sccreporte.reporte.utilities;

import android.net.Uri;
import android.util.Base64;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import org.json.JSONObject;

/**
 * Created by simpson on 2/4/2018.
 * These utilities will be used to communicate with the network.
 */

public class NetworkUtils {
    final static String Base_URL = "http://18.216.203.184";

    final static String BASE_REPORTS_URL = Base_URL + "/reports";

    final static String PARAM_QUERY_USER_ID = "user_id";

    final static String BASE_CREATE_USER_URL = Base_URL + "/user";

    /**
     * Builds the URL used to query sccreporte.
     *
     * @param userId The user id that will be queried for.
     * @return The URL to use to query the sccreporte get reports.
     */
    public static URL buildReportsUrl(String userId) {
        Uri builtUri = Uri.parse(BASE_REPORTS_URL).buildUpon()
                .appendQueryParameter(PARAM_QUERY_USER_ID, userId)
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
    public static String getReportsFromHttpUrl(URL url, String username, String password) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        urlConnection.setRequestMethod("GET");
        String basicAuth = buildBasicAuthorizationString(username, password);
        urlConnection.setRequestProperty("Authorization", basicAuth);
        urlConnection.setRequestProperty("Accept","application/json");

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

    /**
     * Builds the URL used to query sccreporte.
     * @return The URL to use to query the sccreporte create user.
     */
    public static URL buildCreateUserUrl() {
        Uri builtUri = Uri.parse(BASE_CREATE_USER_URL).buildUpon()
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
     * Returns the entire result from the HTTP response after create user.
     *
     * @param url The URL to fetch the HTTP response from.
     * @param jsonParam The json object to sent it with the request.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String geCreateUserFromHttpUrl(URL url, JSONObject jsonParam) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        urlConnection.setRequestProperty("Accept","application/json");
        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);

        DataOutputStream out = new DataOutputStream(urlConnection.getOutputStream());
        out.writeBytes(jsonParam.toString());
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

    private static String buildBasicAuthorizationString(String username, String password) {

        String credentials = username + ":" + password;
        return "Basic " + new String(Base64.encode(credentials.getBytes(), Base64.DEFAULT));
    }
}
