package com.interview.nbateamviewer;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import static com.interview.nbateamviewer.DataModel.CACHE_FILE_ABSOLUTE_PATH_KEY;
import static com.interview.nbateamviewer.DataModel.CACHE_FILE_NAME;
import static com.interview.nbateamviewer.DataModel.DATA_MODEL_SHARED_PREFERENCES_NAME;
import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(RobolectricTestRunner.class)
public class DataModelUnitTests {

    static final String TEST_JSON_DATA =
            "[\n" +
            "  {\n" +
            "    \"wins\": 45,\n" +
            "    \"losses\": 20,\n" +
            "    \"full_name\": \"Boston Celtics\",\n" +
            "    \"id\": 1,\n" +
            "    \"players\": [\n" +
            "      {\n" +
            "        \"id\": 37729,\n" +
            "        \"first_name\": \"Kadeem\",\n" +
            "        \"last_name\": \"Allen\",\n" +
            "        \"position\": \"SG\",\n" +
            "        \"number\": 45\n" +
            "      },\n" +
            "      {\n" +
            "        \"id\": 30508,\n" +
            "        \"first_name\": \"Aron\",\n" +
            "        \"last_name\": \"Baynes\",\n" +
            "        \"position\": \"C\",\n" +
            "        \"number\": 46\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"wins\": 20,\n" +
            "    \"losses\": 44,\n" +
            "    \"full_name\": \"Brooklyn Nets\",\n" +
            "    \"id\": 2,\n" +
            "    \"players\": [\n" +
            "      {\n" +
            "        \"id\": 802,\n" +
            "        \"first_name\": \"Quincy\",\n" +
            "        \"last_name\": \"Acy\",\n" +
            "        \"position\": \"F\",\n" +
            "        \"number\": 13\n" +
            "      }\n" +
            "    ]\n" +
            "  }\n" +
            "]";

    @Test
    public void testConstructor() {
        try {
            DataModel dataModel = new DataModel(null);
            fail("No null pointer exception when context was null.");
        } catch (NullPointerException e) {
            // expected
        }
        DataModel dataModel = new DataModel(ApplicationProvider.getApplicationContext());
        assertNotNull(dataModel);
        assertNull(dataModel.getData());
    }

    /**
     * First, create a cache file with the test data, then construct DataModel and make sure it
     * restores that data.
     */
    @Test
    public void testRestoresCachedData() throws IOException {
        Context context = ApplicationProvider.getApplicationContext();
        SharedPreferences sharedPreferences = context.getSharedPreferences(DATA_MODEL_SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        File dataFile;
        dataFile = File.createTempFile(CACHE_FILE_NAME, null, context.getCacheDir());
        sharedPreferences.edit().putString(CACHE_FILE_ABSOLUTE_PATH_KEY, dataFile.getAbsolutePath()).apply();
        try (
            FileOutputStream fileOutputStream = new FileOutputStream(dataFile);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
        ) {
            outputStreamWriter.write(TEST_JSON_DATA);
        }
        DataModel dataModel = new DataModel(context);
        assertNotNull(dataModel);
        Team[] data = dataModel.getData();
        verifyTestData(data);
    }

    /**
     * Mock the server with MockWebServer to return the TEST_JSON_DATA.
     * Ensure the data model gets the data from the mocked server.
     * Ensure the next time a data model is created it restores the cached data.
     */
    @Test
    public void testFetchAndCacheData() throws IOException, InterruptedException {
        Context context = ApplicationProvider.getApplicationContext();
        MockWebServer server = new MockWebServer();
        server.enqueue(new MockResponse().setBody(TEST_JSON_DATA));
        server.start();
        DataModel dataModel = new DataModel(context, server.url("/scoremedia/nba-team-viewer/master/input.json").toString());
        CountDownLatch latch = new CountDownLatch(1);
        dataModel.setDataChangeListener(() -> latch.countDown());
        dataModel.fetchData(context);
        latch.await(1000, TimeUnit.MILLISECONDS);
        verifyTestData(dataModel.getData());
        server.shutdown();
        DataModel secondDataModel = new DataModel(context, server.url("/scoremedia/nba-team-viewer/master/input.json").toString());
        // Even though the server isn't reachable, the next data model should have access to the
        // cache file created by the first.
        verifyTestData(secondDataModel.getData());
    }

    /**
     * Mock the server with MockWebServer to return an unexpected result.
     * Ensure the data model doesn't claim to have data.
     */
    @Test
    public void testFetchInvalidData() throws IOException, InterruptedException {
        Context context = ApplicationProvider.getApplicationContext();
        MockWebServer server = new MockWebServer();
        server.enqueue(new MockResponse().setBody("Not the expected response."));
        server.start();
        DataModel dataModel = new DataModel(context, server.url("/scoremedia/nba-team-viewer/master/input.json").toString());
        CountDownLatch latch = new CountDownLatch(1);
        dataModel.setDataChangeListener(() -> latch.countDown());
        dataModel.fetchData(context);
        assertFalse("Data change listener invoked for invalid data.", latch.await(1000, TimeUnit.MILLISECONDS));
        assertNull(dataModel.getData());
    }

    /**
     * Verifies the given data matches TEST_JSON_DATA.
     * @param data
     */
    private void verifyTestData(Team[] data) {
        assertEquals("Boston Celtics", data[0].getFullName());
        assertEquals(45, data[0].getWins());
        assertEquals(20, data[0].getLosses());
        assertEquals(1, data[0].getId());
        assertEquals(37729, data[0].getPlayers()[0].getId());
        assertEquals("Kadeem", data[0].getPlayers()[0].getFirstName());
        assertEquals("Allen", data[0].getPlayers()[0].getLastName());
        assertEquals("SG", data[0].getPlayers()[0].getPosition());
        assertEquals(45, data[0].getPlayers()[0].getNumber());
        assertEquals(30508, data[0].getPlayers()[1].getId());
        assertEquals("Aron", data[0].getPlayers()[1].getFirstName());
        assertEquals("Baynes", data[0].getPlayers()[1].getLastName());
        assertEquals("C", data[0].getPlayers()[1].getPosition());
        assertEquals(46, data[0].getPlayers()[1].getNumber());
        assertEquals("Brooklyn Nets", data[1].getFullName());
        assertEquals(20, data[1].getWins());
        assertEquals(44, data[1].getLosses());
        assertEquals(2, data[1].getId());
        assertEquals(802, data[1].getPlayers()[0].getId());
        assertEquals("Quincy", data[1].getPlayers()[0].getFirstName());
        assertEquals("Acy", data[1].getPlayers()[0].getLastName());
        assertEquals("F", data[1].getPlayers()[0].getPosition());
        assertEquals(13, data[1].getPlayers()[0].getNumber());
    }
}
