package com.vpage.vcars.httputils;


import android.os.Looper;
import android.util.Log;
import com.loopj.android.http.*;
import com.vpage.vcars.tools.VCarsApplication;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.entity.StringEntity;

public class HttpManager {

    private static final String TAG = HttpManager.class.getName();
    private static int DEFAULT_TIMEOUT = 20 * 1000;


   private static final String BASE_URL = " ";

    private static final String CONTENT_TYPE_JSON = "application/json";

    // A SyncHttpClient is an AsyncHttpClient
    public static AsyncHttpClient syncHttpClient = new SyncHttpClient(true, 80, 8443);
    public static AsyncHttpClient asyncHttpClient = new AsyncHttpClient(true, 80, 8443);

    public static void setCookieStore(PersistentCookieStore cookieStore) {
        getClient().setCookieStore(cookieStore);
    }

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        getClient().get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, StringEntity parsedJsonParams, AsyncHttpResponseHandler responseHandler) {
        getClient().post(VCarsApplication.getContext(), getAbsoluteUrl(url), parsedJsonParams, CONTENT_TYPE_JSON, responseHandler);
    }

    public static void putMethode(String url, StringEntity parsedJsonParams, AsyncHttpResponseHandler responseHandler) {
        getClient().put(VCarsApplication.getContext(), getAbsoluteUrl(url), parsedJsonParams, CONTENT_TYPE_JSON, responseHandler);
    }

    public static void put(String url, RequestParams params, ResponseHandlerInterface responseHandler) {
        getClient().put(VCarsApplication.getContext(), getAbsoluteUrl(url), params, responseHandler);
    }

    public static void putwithEntity(String url, HttpEntity httpEntity, ResponseHandlerInterface responseHandler) {
        getClient().put(VCarsApplication.getContext(), getAbsoluteUrl(url), httpEntity, CONTENT_TYPE_JSON, responseHandler);
    }


    public static void delete(String url, ResponseHandlerInterface responseHandler) {
        getClient().delete(VCarsApplication.getContext(), getAbsoluteUrl(url), responseHandler);
    }

    /**
     * @return an async client when calling from the main thread, otherwise a sync client.
     */
    private static AsyncHttpClient getClient() {
        // Return the synchronous HTTP client when the thread is not prepared
        if (Looper.myLooper() == null) {
            syncHttpClient.setTimeout(DEFAULT_TIMEOUT);
            syncHttpClient.setConnectTimeout(DEFAULT_TIMEOUT);
            syncHttpClient.setResponseTimeout(DEFAULT_TIMEOUT);

/*            // SSL
            OustSSLSocketFactory oustSSLSocketFactory = null;
            try {

                KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
                trustStore.load(null, null);
                oustSSLSocketFactory = new OustSSLSocketFactory(trustStore);
                oustSSLSocketFactory.setHostnameVerifier(OustSSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            } catch (Exception ex) {
                Log.e(TAG, ex.toString());
            }
            syncHttpClient.setSSLSocketFactory(oustSSLSocketFactory);*/

            return syncHttpClient;
        }

        asyncHttpClient.setTimeout(DEFAULT_TIMEOUT);
        asyncHttpClient.setConnectTimeout(DEFAULT_TIMEOUT);
        asyncHttpClient.setResponseTimeout(DEFAULT_TIMEOUT);

/*        // SSL
        OustSSLSocketFactory oustSSLSocketFactory = null;
        try {

            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);
            oustSSLSocketFactory = new OustSSLSocketFactory(trustStore);
            oustSSLSocketFactory.setHostnameVerifier(OustSSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        } catch (Exception ex) {
            Log.e(TAG, ex.toString());
        }
        asyncHttpClient.setSSLSocketFactory(oustSSLSocketFactory);*/

        return asyncHttpClient;
    }


    private static String getAbsoluteUrl(String relativeUrl) {
        Log.d(TAG, BASE_URL + relativeUrl);
        return BASE_URL + relativeUrl;
    }


}
