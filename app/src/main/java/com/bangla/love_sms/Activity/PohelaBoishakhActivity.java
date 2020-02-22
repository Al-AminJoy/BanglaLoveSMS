package com.bangla.love_sms.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bangla.love_sms.Adapter.MessageAdapter;
import com.bangla.love_sms.Interface.ClickValuePass;
import com.bangla.love_sms.Helper.ConstantVariable;
import com.bangla.love_sms.Helper.DatabaseHelper;
import com.bangla.love_sms.Helper.SharedPref;
import com.bangla.love_sms.ModelClass.ModelClass;
import com.bangla.love_sms.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;

public class PohelaBoishakhActivity extends AppCompatActivity implements ClickValuePass{
    private RecyclerView recyclerView;
    private DatabaseHelper databaseHelper;
    private ArrayList<ModelClass> datalist = new ArrayList<>();
    private MessageAdapter adapter;
    private TextView tvCatagoryName;
    private ImageView ivToolbarBack;
    private ClickValuePass clickValuePass;
    private InterstitialAd interstitialAd;
    private AdView adView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pohela_boishakh);
        //Finding Id
        idFinder();
        //Banner Ad
        MobileAds.initialize(this, String.valueOf(R.string.admob_ad_id));
        AdRequest adRequest=new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        //InterstitialAd Implimentation
        interstitialAd=new InterstitialAd(this);
        interstitialAd.setAdUnitId(getString(R.string.industrial_ad_id));
        interstitialAd.loadAd(new AdRequest.Builder().build());
        clickValuePass=(ClickValuePass) this;
        //Creating SQLite Class Object
        databaseHelper = new DatabaseHelper(this);
        //recyclerView
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //ReadingDataFromDatabase
        readData();
        toolbar();
    }

    private void toolbar() {
        tvCatagoryName.setText(R.string.pohela_boishakh);
        ivToolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void idFinder() {
        tvCatagoryName = findViewById(R.id.tvToolbarCatagoryTitleId);
        ivToolbarBack = findViewById(R.id.ivToolbarOtherBackId);
        recyclerView = findViewById(R.id.rvPohelaBoishakhMessageID);
        adView=findViewById(R.id.avBannerPohelaBoishakhId);

    }

    private void readData() {
        Cursor cursor = databaseHelper.readData();
        if (cursor.getCount() == 0) {
            return;
        } else {

            while (cursor.moveToNext()) {
                String id = cursor.getString(0);
                String message = cursor.getString(1);
                String catagory = cursor.getString(2);
                String favourite = cursor.getString(3);
                if (catagory.equals("Noboborsho")) {
                    ModelClass modelClass = new ModelClass(id, message, catagory, favourite);
                    datalist.add(modelClass);
                }
            }
            adapter = new MessageAdapter(getApplicationContext(), datalist,clickValuePass);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        }
    }
    @Override
    public void ShowAd() {
        SharedPreferences sharedPreferences = getSharedPreferences(SharedPref.AppPra, Context.MODE_PRIVATE);
        int clickCount = sharedPreferences.getInt(String.valueOf(SharedPref.count), 0);
        clickCount=clickCount+1;
        int constantClick= ConstantVariable.AdPerClick;
        if (clickCount==constantClick) {
            if (interstitialAd.isLoaded())
            {
                setSharedPref(0);
                interstitialAd.show();

                interstitialAd.setAdListener(new AdListener() {
                    @Override
                    public void onAdClosed() {

                        interstitialAd.loadAd(new AdRequest.Builder().build());
                    }
                });
            }
            else {
                setSharedPref(0);
            }
        } else {
            setSharedPref(clickCount);
        }
    }
    private void setSharedPref(int clickCount) {
        SharedPreferences sharedPreferences = getSharedPreferences(SharedPref.AppPra, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(String.valueOf(SharedPref.count),clickCount);
        editor.apply();
    }
}
