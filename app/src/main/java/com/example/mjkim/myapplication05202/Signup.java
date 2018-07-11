package com.example.mjkim.myapplication05202;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Signup  extends AppCompatActivity {

    private static String TAG = "phptest_MainActivity";

    private EditText mEditTextId;
    private EditText mEditTextName;
    private EditText mEditTextAddress;
    private EditText mEditTextPassword;
    private EditText mEditTextPasswordc;
    private EditText mEditTextGender;
    private EditText mEditTextEmail;
    private TextView mTextViewResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        mEditTextId = (EditText)findViewById(R.id.addId);
        mEditTextName = (EditText)findViewById(R.id.addName);
        mEditTextAddress= (EditText)findViewById(R.id.addAddress);
        mEditTextPassword = (EditText)findViewById(R.id.addPassword);
        mEditTextPasswordc = (EditText)findViewById(R.id.checkPassword);

        mEditTextGender = (EditText)findViewById(R.id.addGender);
        mEditTextEmail = (EditText)findViewById(R.id.addEmail);
        mTextViewResult = (TextView)findViewById(R.id.textView_db_result);

        Button buttonInsert = (Button)findViewById(R.id.membersubmit);
        buttonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String id = mEditTextId.getText().toString();
                String name = mEditTextName.getText().toString();
                String address = mEditTextAddress.getText().toString();
                String password = mEditTextPassword.getText().toString();
                String passwordc = mEditTextPasswordc.getText().toString();
                String gender = mEditTextGender.getText().toString();
                String email = mEditTextEmail.getText().toString();

                if (!id.equals("") && !password.equals("") && !name.equals("") && !address.equals("") && !gender.equals("") && !email.equals("")) {
                    if (password.equals(passwordc)) {
                        InsertData task = new InsertData();
                        task.execute(id, name, address, password, gender, email);


                        mEditTextId.setText("");
                        mEditTextName.setText("");
                        mEditTextAddress.setText("");
                        mEditTextPassword.setText("");
                        mEditTextGender.setText("");
                        mEditTextEmail.setText("");
                    } else {
                        Toast.makeText(getApplicationContext(), "비밀번호가 서로 일치하지않습니다", Toast.LENGTH_LONG).show();
                        Intent intent =new Intent(getApplicationContext(),Signup.class);
                        startActivity(intent);
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "회원님의 정보를 모두 입력해주세요", Toast.LENGTH_LONG).show();
                    Intent intent =new Intent(getApplicationContext(),Signup.class);
                    startActivity(intent);
                }

            }
        });
    }


    class InsertData extends AsyncTask<String, Void, String>{
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(Signup.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);


            Toast.makeText(getApplicationContext(), "회원가입에 성공했습니다", Toast.LENGTH_LONG).show();
            Intent intent =new Intent(getApplicationContext(),MapsActivity.class);
            startActivity(intent);
            progressDialog.dismiss();
        }


        @Override
        protected String doInBackground(String... params) {

            String id = (String)params[0];
            String name = (String)params[1];
            String address = (String)params[2];
            String password = (String)params[3];
            String gender = (String)params[4];
            String email = (String)params[5];

            String serverURL = "http://172.16.24.86/personl.php";
            String postParameters = "id=" + id + "&name=" + name +"&address=" + address+"&password=" + password+"&gender=" + gender+"&email=" + email;


            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                //httpURLConnection.setRequestProperty("content-type", "application/json");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();


                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "POST response code - " + responseStatusCode);

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
                String line = null;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }


                bufferedReader.close();


                return sb.toString();


            } catch (Exception e) {

                Log.d(TAG, "InsertData: Error ", e);

                return new String("Error: " + e.getMessage());
            }

        }
    }
}