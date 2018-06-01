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

    final static String BASE_CREATE_REPORT_URL = Base_URL + "/addreport";

    final static String BASE_EDIT_REPORT_URL = Base_URL + "/editreport";

    final static String BASE_GET_USER_URL = Base_URL + "/getuser";

    final static String BASE_CREATE_USER_URL = Base_URL + "/adduser";

    final static String PARAM_QUERY_USER_ID = "user_id";

    final static String BASE_BIBLICALS_URL = Base_URL + "/biblicals";

    final static String BASE_CREATE_BIBLICAL_URL = Base_URL + "/addbiblical";

    final static String BASE_DELETE_BIBLICAL_URL = Base_URL + "/deletebiblical";

    final static String BASE_ITISTIME_URL = Base_URL + "/ask";

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
     * @return The URL to use to query the sccreporte create report.
     */
    public static URL buildCreateReportUrl() {
        Uri builtUri = Uri.parse(BASE_CREATE_REPORT_URL).buildUpon()
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
     * Returns the entire result from the HTTP response after create report.
     *
     * @param url The URL to fetch the HTTP response from.
     * @param jsonParam The json object to sent it with the request.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String geCreateReportFromHttpUrl(URL url, JSONObject jsonParam,
                                                   String username, String password) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        String basicAuth = buildBasicAuthorizationString(username, password);
        urlConnection.setRequestProperty("Authorization", basicAuth);
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

    /**
     * Builds the URL used to query sccreporte.
     * @return The URL to use to query the sccreporte edit report.
     */
    public static URL buildEditReportUrl(int report_id) {
        Uri builtUri = Uri.parse(BASE_EDIT_REPORT_URL + "/" + report_id).buildUpon()
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
     * Returns the entire result from the HTTP response after edit report.
     *
     * @param url The URL to fetch the HTTP response from.
     * @param jsonParam The json object to sent it with the request.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String geEditReportFromHttpUrl(URL url, JSONObject jsonParam,
                                                   String username, String password) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        String basicAuth = buildBasicAuthorizationString(username, password);
        urlConnection.setRequestProperty("Authorization", basicAuth);
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

    /**
     * Builds the URL used to query sccreporte.
     * @return The URL to use to query the sccreporte create user.
     */
    public static URL buildGetUserUrl() {
        Uri builtUri = Uri.parse(BASE_GET_USER_URL).buildUpon()
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
    public static String getUserFromHttpUrl(URL url, String username, String password) throws IOException {
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
     *
     * @param userId The user id that will be queried for.
     * @return The URL to use to query the sccreporte get biblicals.
     */
    public static URL buildBiblicalsUrl(String userId) {
        Uri builtUri = Uri.parse(BASE_BIBLICALS_URL).buildUpon()
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
    public static String getBiblicalsFromHttpUrl(URL url, String username, String password) throws IOException {
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
     * @return The URL to use to query the sccreporte create biblical.
     */
    public static URL buildCreateBiblicalUrl() {
        Uri builtUri = Uri.parse(BASE_CREATE_BIBLICAL_URL).buildUpon()
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
     * Returns the entire result from the HTTP response after create biblical.
     *
     * @param url The URL to fetch the HTTP response from.
     * @param jsonParam The json object to sent it with the request.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String geCreateBiblicalFromHttpUrl(URL url, JSONObject jsonParam,
                                                   String username, String password) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        String basicAuth = buildBasicAuthorizationString(username, password);
        urlConnection.setRequestProperty("Authorization", basicAuth);
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

    /**
     * Builds the URL used to query sccreporte.
     * @return The URL to use to query the sccreporte delete biblical.
     */
    public static URL buildDeleteBiblicalUrl(int biblical_id) {
        Uri builtUri = Uri.parse(BASE_DELETE_BIBLICAL_URL + "/" + biblical_id).buildUpon()
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
     * Returns the entire result from the HTTP response after delete biblical.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String geDeleteBiblicalFromHttpUrl(URL url,
                                                 String username, String password) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        urlConnection.setRequestMethod("POST");
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
     * @return The URL to use to query the sccreporte ItIsTime To New Report.
     */
    public static URL buildItIsTimeUrl() {
        Uri builtUri = Uri.parse(BASE_ITISTIME_URL).buildUpon()
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
    public static String getItIsTimeFromHttpUrl(URL url, String username, String password) throws IOException {
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

    //TODO : Fusionar todas los get FromHTTpURl, y build urls dado que son lo mismo

    private static String buildBasicAuthorizationString(String username, String password) {

        String credentials = username + ":" + password;
        return "Basic " + new String(Base64.encode(credentials.getBytes(), Base64.DEFAULT));
    }
}
