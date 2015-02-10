package com.zms.pulltorefresh;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;

import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;

public class Main extends ListActivity {
    private LinkedList<String> mListItems;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // Set a listener to be invoked when the list should be refreshed.
        ((PullListView) getListView()).setOnRefreshListener(new PullListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Do work to refresh the list here.
                new GetDataTask().execute();
            }
        });

        mListItems = new LinkedList<String>();
        mListItems.addAll(Arrays.asList(mStrings));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.list_item, mListItems);

        setListAdapter(adapter);
    }

    private class GetDataTask extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... params) {
            // Simulates a background job.
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return mStrings;
        }

        @Override
        protected void onPostExecute(String[] result) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date = sdf.format(new Date(System.currentTimeMillis()));
            ((PullListView) getListView()).setLastUpdated("Last Update: " + date);
            mListItems.addFirst("Update at " + date);

            // Call onRefreshComplete when the list has been refreshed.
            ((PullListView) getListView()).onRefreshComplete();
            super.onPostExecute(result);
        }
    }

    private String[] mStrings = {
            // At least 12 items before refresh, or header will visible
            "Item 0", "Item 1", "Item 2", "Item 3", "Item 4", "Item 5",
            "Item 6", "Item 7", "Item 8", "Item 9", "Item A", "Item B"};
}
