package com.example.bookworld;

import android.content.ContentValues;
import android.os.AsyncTask;

public class NetworkTask extends AsyncTask<Void, Void, String> {
    private String url;
    private ContentValues values;

    public NetworkTask(String url, ContentValues values) {
        this.url = url;
        this.values = values;
    }

    @Override
    protected String doInBackground(Void... voids) {
        String result;
        RequestHttpConnection requestHttpConnection = new RequestHttpConnection();
        result = requestHttpConnection.request(url, values);
        return result;
    }

    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}