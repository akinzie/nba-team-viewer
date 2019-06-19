package com.interview.nbateamviewer;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * This class is in charge of the application data.  Data can come from 3 sources: the server, the
 * network cache, and the cache file.  The network cache simply follows the HTTP caching rules.  The
 * cache file is to allow the app to be usable in the case the server is not reachable and the
 * network cache has expired.  When an instance of DataModel is constructed, it will immediately
 * attempt to restore data from the cache file.  An attempt to get data from the server is only done
 * when {@link DataModel#fetchData(Context)} is called (the response may be from the network cache).
 */
public class DataModel {
    static final String LOGGER_TAG = "TeamViewer:DataModel";
    static final String DEFAULT_DATA_URL = "https://raw.githubusercontent.com/scoremedia/nba-team-viewer/master/input.json";
    static final String DATA_MODEL_SHARED_PREFERENCES_NAME = "data model shared preferences";
    static final String CACHE_FILE_NAME = "data.json";
    static final String CACHE_FILE_ABSOLUTE_PATH_KEY = "cache file absolute path";
    static final int NETWORK_CACHE_SIZE_BYTES = 500000;
    private OkHttpClient okHttpClient;
    private volatile Team[] data;
    private volatile DataChangeListener dataChangeListener;
    private final String DATA_URL;

    /**
     * Create a DataModel.
     * @param context The context is required for file access (required for the network cache and
     *                the cache file).
     */
    public DataModel(@NonNull Context context) {
        DATA_URL = DEFAULT_DATA_URL;
        okHttpClient = new OkHttpClient.Builder()
                .cache(new Cache(context.getCacheDir(), NETWORK_CACHE_SIZE_BYTES))
                .build();
        restoreDataFromCache(context);
    }

    /**
     * Create a DataModel.
     * @param context The context is required for file access (required for the network cache and
     *                the cache file).
     * @param dataUrl The url of the data endpoint.
     */
    DataModel(@NonNull Context context, @NonNull String dataUrl) {
        DATA_URL = dataUrl;
        okHttpClient = new OkHttpClient.Builder()
                .cache(new Cache(context.getCacheDir(), NETWORK_CACHE_SIZE_BYTES))
                .build();
        restoreDataFromCache(context);
    }

    /**
     * Gets the currently available data.  No requests will be made to the server.  No data will be
     * restored from the cache file.
     * @return The currently available data.
     */
    @Nullable
    public Team[] getData() {
        return data;
    }

    /**
     * Initiates a request to get data from the server.  Networking cache policies will be followed,
     * so there might be no request sent over the network.  The request is sent on a worker thread,
     * this method returns immediately after submitting the runnable to the worker thread.  To be
     * notified when the data is available, register a listener by calling
     * {@link DataModel#setDataChangeListener(DataChangeListener)}.
     * @param context The context is needed to be able to write the cache file.
     */
    public void fetchData(final @NonNull Context context) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> {
            Request request = new Request.Builder()
                    .url(DATA_URL)
                    .build();
            Call call = okHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e(LOGGER_TAG, "Data fetch failed due to exception.", e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseAsString = response.body().string();
                    Gson gson = new Gson();
                    Team[] teamData = gson.fromJson(responseAsString, Team[].class);
                    data = teamData;
                    notifyChangeListener();
                    cacheData(context, responseAsString);
                }
            });
        });
    }

    /**
     * Set the listener that will be invoked when data is updated.  Data is updated when restored
     * from the cache file or upon a (possible cached) response from the server.  Note that the
     * updated data may be identical to the previous data.
     * @param dataChangeListener
     */
    public void setDataChangeListener(@Nullable DataChangeListener dataChangeListener) {
        this.dataChangeListener = dataChangeListener;
    }

    /**
     * Cache data so it will be available next time data is required, even if the server is not
     * reachable.  Note this is different from the network cache, which will expire (or at least
     * require a round-trip to the server to confirm no change) after a certain length of time.
     * @param context
     * @param jsonData
     */
    private void cacheData(Context context, String jsonData) {
        if (data == null) {
            // Nothing to persist.
            return;
        }
        synchronized (DATA_MODEL_SHARED_PREFERENCES_NAME) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(DATA_MODEL_SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
            String existingFileName = sharedPreferences.getString(CACHE_FILE_ABSOLUTE_PATH_KEY, "");
            try {
                File dataFile;
                if (existingFileName.isEmpty()) {
                    dataFile = File.createTempFile(CACHE_FILE_NAME, null, context.getCacheDir());
                    sharedPreferences.edit().putString(CACHE_FILE_ABSOLUTE_PATH_KEY, dataFile.getAbsolutePath()).apply();
                } else {
                    dataFile = new File(existingFileName);
                }
                Log.d(LOGGER_TAG, "Persisting dataFile: " + dataFile.getAbsolutePath());
                try (
                        FileOutputStream fileOutputStream = new FileOutputStream(dataFile);
                        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
                ) {
                    outputStreamWriter.write(jsonData);
                }
            } catch (IOException e) {
                Log.e(LOGGER_TAG, "Error while persisting data.", e);
            }
        }
    }

    /**
     * Restore data from a cache file for offline access until data from the server is retrieved.
     * @param context
     */
    private void restoreDataFromCache(Context context) {
        synchronized (DATA_MODEL_SHARED_PREFERENCES_NAME) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(DATA_MODEL_SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
            String existingFileName = sharedPreferences.getString(CACHE_FILE_ABSOLUTE_PATH_KEY, "");
            if (existingFileName.isEmpty()) {
                Log.d(LOGGER_TAG, "No cached data to restore.");
            }
            try {
                File dataFile = new File(existingFileName);
                Log.d(LOGGER_TAG, "Restoring dataFile: " + dataFile.getAbsolutePath());
                try (
                        FileInputStream fileInputStream = new FileInputStream(dataFile);
                        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                ) {
                    Gson gson = new Gson();
                    data = gson.fromJson(inputStreamReader, Team[].class);
                    notifyChangeListener();
                }
            } catch (IOException e) {
                Log.e(LOGGER_TAG, "Error while restoring data.", e);
            }
        }
    }

    private void notifyChangeListener() {
        if (dataChangeListener != null) {
            dataChangeListener.onDataChanged();
        }
    }
}
