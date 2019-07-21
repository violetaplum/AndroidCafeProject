package com.example.mjkim.myapplication05202;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class CafeMenu extends AppCompatActivity {
        private static String TAG = "phptest_MainActivity";

        private static final String TAG_JSON="mjkim";

        private static final String TAG_NAME = "name";
        private static final String TAG_PRICE ="price";

        private TextView mTextViewResult;
        ArrayList<HashMap<String, String>> mArrayList;
        ListView mlistView;
        String mJsonString;



        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);


            mTextViewResult = (TextView)findViewById(R.id.textView_main_result);
            mlistView = (ListView) findViewById(R.id.listView_main_list);
            mArrayList = new ArrayList<>();

            GetData task = new GetData();
            task.execute("http://192.168.35.119/Andamiro.php");
        }


public class GetData extends AsyncTask<String, Void, String> {
    ProgressDialog progressDialog;
    String errorString = null;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }


    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        mTextViewResult.setText(result);
        Log.d(TAG, "response  - " + result);

        if (result == null){

            mTextViewResult.setText(errorString);
        }
        else {

            mJsonString = result;
            showResult();
        }
    }


    @Override
    protected String doInBackground(String... params) {

        String serverURL = params[0];


        try {

            URL url = new URL(serverURL);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


            httpURLConnection.setReadTimeout(5000);
            httpURLConnection.setConnectTimeout(5000);
            httpURLConnection.connect();


            int responseStatusCode = httpURLConnection.getResponseCode();
            Log.d(TAG, "response code - " + responseStatusCode);

            InputStream inputStream;
            if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                inputStream = httpURLConnection.getInputStream();
            }
            else{
                inputStream = httpURLConnection.getErrorStream();
            }


            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuilder sb = new StringBuilder();
            String line;

            while((line = bufferedReader.readLine()) != null){
                sb.append(line);
            }


            bufferedReader.close();


            return sb.toString().trim();


        } catch (Exception e) {

            Log.d(TAG, "InsertData: Error ", e);
            errorString = e.toString();

            return null;
        }

    }
}


    private void showResult(){
        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for(int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);

                String name = item.getString(TAG_NAME);
                String price = item.getString(TAG_PRICE);

                HashMap<String,String> hashMap = new HashMap<>();


                hashMap.put(TAG_NAME, name);
                hashMap.put(TAG_PRICE, price);

                mArrayList.add(hashMap);
            }

            ListAdapter adapter = new SimpleAdapter(
                    CafeMenu.this, mArrayList, R.layout.item_list,
                    new String[]{TAG_NAME, TAG_PRICE},
                    new int[]{R.id.textView_list_id, R.id.textView_list_name}
            );

            mlistView.setAdapter(adapter);

        } catch (JSONException e) {

            Log.d(TAG, "showResult : ", e);
        }

    }

}