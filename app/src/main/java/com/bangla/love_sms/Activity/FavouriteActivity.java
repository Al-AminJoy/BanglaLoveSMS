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

import com.bangla.love_sms.Adapter.FavouriteAdapter;
import com.bangla.love_sms.Interface.ClickValuePass;
import com.bangla.love_sms.Helper.ConstantVariable;
import com.bangla.love_sms.Helper.DatabaseHelper;
import com.bangla.love_sms.Helper.SharedPref;
import com.bangla.love_sms.ModelClass.ModelClass;
import com.bangla.love_sms.Interface.NoMessageShowListener;
import com.bangla.love_sms.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;

public class FavouriteActivity extends AppCompatActivity implements ClickValuePass, NoMessageShowListener {
    private RecyclerView recyclerView;
    private DatabaseHelper databaseHelper;
    private ArrayList<ModelClass> datalist = new ArrayList<>();
    private FavouriteAdapter adapter;
    private TextView tvCatagoryName,tvNoFavMessage;
    private ImageView ivToolbarBack;
    private ClickValuePass clickValuePass;
    private InterstitialAd interstitialAd;
    private NoMessageShowListener noMessageShowListener;
    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);
        //Finding Id
        idFinder();
        //Banner Ad
        MobileAds.initialize(this, String.valueOf(R.string.admob_ad_id));
        AdRequest adRequest=new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        //NoMessage Show Interface
        noMessageShowListener=this;
        //Interestitial Ad
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
        tvCatagoryName.setText(R.string.favourite);
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
        recyclerView = findViewById(R.id.rvFavId);
        tvNoFavMessage=findViewById(R.id.tvFavNoMessageId);
        adView=findViewById(R.id.avBannerFavouriteId);
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
                if (favourite.equals("true")) {
                    ModelClass modelClass = new ModelClass(id, message, catagory, favourite);
                    datalist.add(modelClass);
                }
            }
            adapter = new FavouriteAdapter(FavouriteActivity.this, datalist,clickValuePass,noMessageShowListener);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            if (datalist.size()==0){
                tvNoFavMessage.setVisibility(View.VISIBLE);
            }
            else {
                tvNoFavMessage.setVisibility(View.INVISIBLE);
            }
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

    @Override
    public void noMessage(int num) {
        if (num==0){
            tvNoFavMessage.setVisibility(View.VISIBLE);
        }
        else {
            tvNoFavMessage.setVisibility(View.INVISIBLE);
        }
    }
}
