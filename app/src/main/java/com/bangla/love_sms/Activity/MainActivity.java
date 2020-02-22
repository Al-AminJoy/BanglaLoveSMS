package com.bangla.love_sms.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.bangla.love_sms.Helper.DatabaseHelper;
import com.bangla.love_sms.Helper.SharedPref;
import com.bangla.love_sms.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {
    private DatabaseHelper databaseHelper;
    private InputStream inputStream;
    private String[] data, second;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseHelper = new DatabaseHelper(this);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
        inputData();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                //thread inside thread :/
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        dowork();
                    }
                });
                thread.start();

            }
        });
        thread.start();

    }

    private void setSharedPref() {
        SharedPreferences sharedPreferences = getSharedPreferences(SharedPref.AppPra, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SharedPref.Inserted, "inserted");
        editor.apply();
    }

    private void dowork() {
        try {

            Thread.sleep(3000);

            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void inputData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SharedPref.AppPra, Context.MODE_PRIVATE);
        String inserted = sharedPreferences.getString(SharedPref.Inserted, "");
        if (!inserted.equals("inserted")) {
            inputStream = getResources().openRawResource(R.raw.sm);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            try {
                String csvReader;
                setSharedPref();
                while ((csvReader = reader.readLine()) != null) {
                    data = csvReader.split("@");
                    String sms = data[0];
                    String catagoryC = data[1];
                    second = catagoryC.split(",");
                    String catagory = second[1];

                    String favourite = "false";
                    long rowId = databaseHelper.insertData(sms, catagory, favourite);
                    if (rowId == -1) {
                        //Toast.makeText(getApplicationContext(), "Can't Be Insert", Toast.LENGTH_LONG).show();
                    } else {
                        //Toast.makeText(getApplicationContext(), "Inserted", Toast.LENGTH_LONG).show();
                    }
                }


            } catch (Exception e) {

            }

        }

    }

}
