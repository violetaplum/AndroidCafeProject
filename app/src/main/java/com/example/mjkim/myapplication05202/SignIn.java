package com.example.mjkim.myapplication05202;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class SignIn extends AppCompatActivity {
    private static String TAG = "phptest_MainActivity";

    private static final String TAG_JSON = "mjkim";
    private static final String TAG_ID = "id";
    private static final String TAG_PASSWORD = "password";
    private EditText EditTextId;
    private EditText EditTextPassword;
    String signId, signPw;
    String data = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        EditTextId = (EditText) findViewById(R.id.IdInsert);
        EditTextPassword = (EditText) findViewById(R.id.PasswordInsert);

        Button buttonInsert = (Button) findViewById(R.id.aa);
        buttonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signId = EditTextId.getText().toString();
                signPw = EditTextPassword.getText().toString();
                login lDB = new login();
                lDB.execute();

            }

        });


    }

    public class login extends AsyncTask<Void, Integer, Void> {

        @Override
        protected Void doInBackground(Void... unused) {

            /* 인풋 파라메터값 생성 */
            String param = "id=" + signId + "" + "&password=" + signPw + "";
            Log.e("POST", param);
            String serverURL = "http://172.16.24.86/loginOk.php";
            try {
                /* 서버연결 */
                URL url = new URL(serverURL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.connect();

                /* 안드로이드 -> 서버 파라메터값 전달 */
                OutputStream outs = conn.getOutputStream();
                outs.write(param.getBytes("UTF-8"));
                outs.flush();
                outs.close();

                /* 서버 -> 안드로이드 파라메터값 전달 */
                InputStream is = null;
                BufferedReader in = null;


                is = conn.getInputStream();
                in = new BufferedReader(new InputStreamReader(is), 8 * 1024);
                String line = null;
                StringBuffer buff = new StringBuffer();
                while ((line = in.readLine()) != null) {
                    buff.append(line + "\n");
                }
                data = buff.toString().trim();

                /* 서버에서 응답 */
                Log.e("RECV DATA", data);



            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(SignIn.this);



                if (data.equals("1")) {

                    Toast.makeText(getApplicationContext(), "로그인에 성공했습니다", Toast.LENGTH_LONG).show();
                    Intent intent =new Intent(getApplicationContext(),MapsActivity.class);
                    startActivity(intent);
                } else if (data.equals("0")) {

                    Toast.makeText(getApplicationContext(), "아이디나 비밀번호를 다시 확인해주세요", Toast.LENGTH_LONG).show();
                    Intent intent =new Intent(getApplicationContext(),SignIn.class);
                    startActivity(intent);
                } else {
                    Log.e("RESULT", "에러 발생! ERRCODE = " + data);
                    alertBuilder
                            .setTitle("알림")
                            .setMessage("등록중 에러가 발생했습니다! errcode : " + data)
                            .setCancelable(true)
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //finish();
                                }
                            });
                    AlertDialog dialog = alertBuilder.create();
                    dialog.show();
                }
            }
        }


    }








