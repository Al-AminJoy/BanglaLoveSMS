package com.bangla.love_sms.Activity;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bangla.love_sms.Helper.ConstantVariable;
import com.bangla.love_sms.Helper.SharedPref;
import com.bangla.love_sms.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.navigation.NavigationView;

import net.glxn.qrgen.android.QRCode;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener{
    private CardView cvMissing, cvWishing, cvFriendship, cvGoodMorning, cvLove, cvBirthday, cvFunny, cvEidMubarak, cvPohelaBoishakh, cvGoodNight, cvPoetSpeech;
    private ImageView ivToolbarFavIcon, ivToolbarMenuIcon;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle mToggle;
    private InterstitialAd interstitialAd;
    private AdView adView;

    //Navigation Menu Button
    private LinearLayout menuShareButton;
    private LinearLayout menuRateButton;
    private LinearLayout menuFeedbackButton;

    private String appname;
     //int clickCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        idFinder();
        cvMissing.setOnClickListener(this);
        cvWishing.setOnClickListener(this);
        cvFriendship.setOnClickListener(this);
        cvGoodMorning.setOnClickListener(this);
        cvLove.setOnClickListener(this);
        cvBirthday.setOnClickListener(this);
        cvFunny.setOnClickListener(this);
        cvEidMubarak.setOnClickListener(this);
        cvPohelaBoishakh.setOnClickListener(this);
        cvGoodNight.setOnClickListener(this);
        cvPoetSpeech.setOnClickListener(this);
        ivToolbarFavIcon.setOnClickListener(this);
        navDrawer();

        //Navigation Menu Button Call.
        navigaMenuButton();
        //Banner Ad
        MobileAds.initialize(this, String.valueOf(R.string.admob_ad_id));
        AdRequest adRequest=new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        //InterstitialAd Implimentation
        interstitialAd=new InterstitialAd(this);
        interstitialAd.setAdUnitId(getString(R.string.industrial_ad_id));
        interstitialAd.loadAd(new AdRequest.Builder().build());
    }

    /**
     * Navigation Menu Button Call.
     */
    private void navigaMenuButton() {
        menuRateButton = findViewById(R.id.rate_app_bn_nav_id);
        menuShareButton = findViewById(R.id.share_bn_nav_id);
        menuFeedbackButton = findViewById(R.id.menu_feed_bn_nav_id);

        appname = getResources().getString(R.string.app_name);
        // Rate Button Handler
        menuRateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("market://details?id=" + getBaseContext().getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_NEW_DOCUMENT | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                }else{
                    goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |  Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                }
                try { startActivity(goToMarket); } catch (ActivityNotFoundException e) { startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + getBaseContext().getPackageName()))); }            }
        });

        // Share Button Handler
        menuShareButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //QR generator
                Bitmap myBitmap = QRCode.from("https://play.google.com/store/apps/details?id="+getBaseContext().getPackageName()).bitmap();

                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);

                View shareview = getLayoutInflater().inflate(R.layout.qr_layout,null);
                ImageView qrimg = (ImageView)shareview.findViewById(R.id.qrimg);
                Button sharebutton = (Button)shareview.findViewById(R.id.sharebutton);
                qrimg.setImageBitmap(myBitmap);


                builder.setView(shareview);
                builder.create().show();

                sharebutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            Intent i = new Intent(Intent.ACTION_SEND);
                            i.setType("text/plain");
                            i.putExtra(Intent.EXTRA_SUBJECT, appname);
                            String sAux = "\nHey, I'm using this app."+ appname +" is awesome. Give it a try.\n\n";
                            sAux = sAux + "https://play.google.com/store/apps/details?id="+getBaseContext().getPackageName()+" \n\n";
                            i.putExtra(Intent.EXTRA_TEXT, sAux);
                            startActivity(Intent.createChooser(i, "choose one"));
                        } catch(Exception e) {
                            //e.toString();
                        }
                    }
                });            }
        });

        menuFeedbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, FeedbackActivity.class);
                startActivity(intent);
            }
        });

    }

    private void navDrawer() {
        mToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        ivToolbarMenuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

    }
    private void setSharedPref(int clickCount) {
        SharedPreferences sharedPreferences = getSharedPreferences(SharedPref.AppPra, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(String.valueOf(SharedPref.count),clickCount);
        editor.apply();
    }
    private void idFinder() {
        cvMissing = findViewById(R.id.cvHomeMissingId);
        cvWishing = findViewById(R.id.cvHomeWishingId);
        cvFriendship = findViewById(R.id.cvHomeFriendshipId);
        cvGoodMorning = findViewById(R.id.cvHomeGoodMorningId);
        cvLove = findViewById(R.id.cvHomeLoveId);
        cvBirthday = findViewById(R.id.cvHomeBirthdayId);
        cvFunny = findViewById(R.id.cvHomeFunnyId);
        cvEidMubarak = findViewById(R.id.cvHomeEidMubarakId);
        cvPohelaBoishakh = findViewById(R.id.cvHomePohelaBoishakhId);
        cvGoodNight = findViewById(R.id.cvHomeGoodNightId);
        cvPoetSpeech = findViewById(R.id.cvHomePoetSpeechId);
        ivToolbarFavIcon = findViewById(R.id.ivToolbarHomeFavId);
        ivToolbarMenuIcon = findViewById(R.id.ivToolbarMenuIconId);
        drawerLayout = findViewById(R.id.HomeDrawerId);
        navigationView = findViewById(R.id.HomeNavId);
        adView=findViewById(R.id.avBannerHomeId);
    }

    @Override
    public void onClick(View v) {
        SharedPreferences sharedPreferences = getSharedPreferences(SharedPref.AppPra, Context.MODE_PRIVATE);
         int clickCount = sharedPreferences.getInt(String.valueOf(SharedPref.count), 0);
        if (v.getId() == R.id.cvHomeMissingId) {
            clickCount=clickCount+1;
            int constantClick= ConstantVariable.AdPerClick;
            if (clickCount==constantClick) {
                if (interstitialAd.isLoaded())
                {
                    setSharedPref(0);
                    interstitialAd.show();

                    final int finalClickCount = clickCount;
                    interstitialAd.setAdListener(new AdListener() {
                        @Override
                        public void onAdClosed() {
                            interstitialAd.loadAd(new AdRequest.Builder().build());
                            startActivity(new Intent(HomeActivity.this, MissingActivity.class));

                        }
                    });
                }
                else {
                    setSharedPref(0);

                    Intent intent = new Intent(HomeActivity.this, MissingActivity.class);
                    startActivity(intent);
                }


            } else {
                setSharedPref(clickCount);
                Intent intent = new Intent(HomeActivity.this, MissingActivity.class);
                startActivity(intent);
            }
        } else if (v.getId() == R.id.cvHomeWishingId) {
            clickCount=clickCount+1;

            if (clickCount==4) {
                if (interstitialAd.isLoaded())
                {
                    setSharedPref(0);
                    interstitialAd.show();

                    interstitialAd.setAdListener(new AdListener() {
                        @Override
                        public void onAdClosed() {
                            startActivity(new Intent(HomeActivity.this, WishingActivity.class));
                            interstitialAd.loadAd(new AdRequest.Builder().build());
                        }
                    });
                }
                else {
                    setSharedPref(0);
                    Intent intent = new Intent(HomeActivity.this, WishingActivity.class);
                    startActivity(intent);
                }


            } else {
                setSharedPref(clickCount);
                Intent intent = new Intent(HomeActivity.this, WishingActivity.class);
                startActivity(intent);
            }  } else if (v.getId() == R.id.cvHomeFriendshipId) {
            clickCount=clickCount+1;

            if (clickCount==4) {
                if (interstitialAd.isLoaded())
                {
                    setSharedPref(0);
                    interstitialAd.show();

                    interstitialAd.setAdListener(new AdListener() {
                        @Override
                        public void onAdClosed() {
                            startActivity(new Intent(HomeActivity.this, FriendshipActivity.class));
                            interstitialAd.loadAd(new AdRequest.Builder().build());
                        }
                    });
                }
                else {
                    setSharedPref(0);
                    Intent intent = new Intent(HomeActivity.this, FriendshipActivity.class);
                    startActivity(intent);
                }


            } else {
                setSharedPref(clickCount);
                Intent intent = new Intent(HomeActivity.this, FriendshipActivity.class);
                startActivity(intent);
            }
        } else if (v.getId() == R.id.cvHomeGoodMorningId) {
            clickCount=clickCount+1;

            if (clickCount==4) {
                if (interstitialAd.isLoaded())
                {
                    setSharedPref(0);
                    interstitialAd.show();

                    interstitialAd.setAdListener(new AdListener() {
                        @Override
                        public void onAdClosed() {
                            startActivity(new Intent(HomeActivity.this, GoodMorningActivity.class));
                            interstitialAd.loadAd(new AdRequest.Builder().build());
                        }
                    });
                }
                else {
                    setSharedPref(0);
                    Intent intent = new Intent(HomeActivity.this, GoodMorningActivity.class);
                    startActivity(intent);
                }


            } else {
                setSharedPref(clickCount);
                Intent intent = new Intent(HomeActivity.this, GoodMorningActivity.class);
                startActivity(intent);
            }
        } else if (v.getId() == R.id.cvHomeLoveId) {
            clickCount=clickCount+1;

            if (clickCount==4) {
                if (interstitialAd.isLoaded())
                {
                    setSharedPref(0);
                    interstitialAd.show();

                    interstitialAd.setAdListener(new AdListener() {
                        @Override
                        public void onAdClosed() {
                            startActivity(new Intent(HomeActivity.this, LoveMessageActivity.class));
                            interstitialAd.loadAd(new AdRequest.Builder().build());
                        }
                    });
                }
                else {
                    setSharedPref(0);
                    Intent intent = new Intent(HomeActivity.this, LoveMessageActivity.class);
                    startActivity(intent);
                }


            } else {
                setSharedPref(clickCount);
                Intent intent = new Intent(HomeActivity.this, LoveMessageActivity.class);
                startActivity(intent);
            }
        } else if (v.getId() == R.id.cvHomeBirthdayId) {
            clickCount=clickCount+1;

            if (clickCount==4) {
                if (interstitialAd.isLoaded())
                {
                    setSharedPref(0);
                    interstitialAd.show();

                    interstitialAd.setAdListener(new AdListener() {
                        @Override
                        public void onAdClosed() {
                            startActivity(new Intent(HomeActivity.this, BirthdayActivity.class));
                            interstitialAd.loadAd(new AdRequest.Builder().build());
                        }
                    });
                }
                else {
                    setSharedPref(0);
                    Intent intent = new Intent(HomeActivity.this, BirthdayActivity.class);
                    startActivity(intent);
                }


            } else {
                setSharedPref(clickCount);
                Intent intent = new Intent(HomeActivity.this, BirthdayActivity.class);
                startActivity(intent);
            }
        } else if (v.getId() == R.id.cvHomeFunnyId) {
            clickCount=clickCount+1;

            if (clickCount==4) {
                if (interstitialAd.isLoaded())
                {
                    setSharedPref(0);
                    interstitialAd.show();

                    interstitialAd.setAdListener(new AdListener() {
                        @Override
                        public void onAdClosed() {
                            startActivity(new Intent(HomeActivity.this, FunnyMessageActivity.class));
                            interstitialAd.loadAd(new AdRequest.Builder().build());
                        }
                    });
                }
                else {
                    setSharedPref(0);
                    Intent intent = new Intent(HomeActivity.this, FunnyMessageActivity.class);
                    startActivity(intent);
                }


            } else {
                setSharedPref(clickCount);
                Intent intent = new Intent(HomeActivity.this, FunnyMessageActivity.class);
                startActivity(intent);
            }
        } else if (v.getId() == R.id.cvHomeEidMubarakId) {
            clickCount=clickCount+1;

            if (clickCount==4) {
                if (interstitialAd.isLoaded())
                {
                    setSharedPref(0);
                    interstitialAd.show();

                    interstitialAd.setAdListener(new AdListener() {
                        @Override
                        public void onAdClosed() {
                            startActivity(new Intent(HomeActivity.this, EidMubarakActivity.class));
                            interstitialAd.loadAd(new AdRequest.Builder().build());
                        }
                    });
                }
                else {
                    setSharedPref(0);
                    Intent intent = new Intent(HomeActivity.this, EidMubarakActivity.class);
                    startActivity(intent);
                }


            } else {
                setSharedPref(clickCount);
                Intent intent = new Intent(HomeActivity.this, EidMubarakActivity.class);
                startActivity(intent);
            }
        } else if (v.getId() == R.id.cvHomePohelaBoishakhId) {
            clickCount=clickCount+1;

            if (clickCount==4) {
                if (interstitialAd.isLoaded())
                {
                    setSharedPref(0);
                    interstitialAd.show();

                    interstitialAd.setAdListener(new AdListener() {
                        @Override
                        public void onAdClosed() {
                            startActivity(new Intent(HomeActivity.this, PohelaBoishakhActivity.class));
                            interstitialAd.loadAd(new AdRequest.Builder().build());
                        }
                    });
                }
                else {
                    setSharedPref(0);
                    Intent intent = new Intent(HomeActivity.this, PohelaBoishakhActivity.class);
                    startActivity(intent);
                }


            } else {
                setSharedPref(clickCount);
                Intent intent = new Intent(HomeActivity.this, PohelaBoishakhActivity.class);
                startActivity(intent);
            }
        } else if (v.getId() == R.id.cvHomeGoodNightId) {
            clickCount=clickCount+1;

            if (clickCount==4) {
                if (interstitialAd.isLoaded())
                {
                    setSharedPref(0);
                    interstitialAd.show();

                    interstitialAd.setAdListener(new AdListener() {
                        @Override
                        public void onAdClosed() {
                            startActivity(new Intent(HomeActivity.this, GoodNightActivity.class));
                            interstitialAd.loadAd(new AdRequest.Builder().build());
                        }
                    });
                }
                else {
                    setSharedPref(0);
                    Intent intent = new Intent(HomeActivity.this, GoodNightActivity.class);
                    startActivity(intent);
                }


            } else {
                setSharedPref(clickCount);
                Intent intent = new Intent(HomeActivity.this, GoodNightActivity.class);
                startActivity(intent);
            }
        } else if (v.getId() == R.id.cvHomePoetSpeechId) {
            clickCount=clickCount+1;

            if (clickCount==4) {
                if (interstitialAd.isLoaded())
                {
                    setSharedPref(0);
                    interstitialAd.show();

                    interstitialAd.setAdListener(new AdListener() {
                        @Override
                        public void onAdClosed() {
                            startActivity(new Intent(HomeActivity.this, PoetSpeechActivity.class));
                            interstitialAd.loadAd(new AdRequest.Builder().build());
                        }
                    });
                }
                else {
                    setSharedPref(0);
                    Intent intent = new Intent(HomeActivity.this, PoetSpeechActivity.class);
                    startActivity(intent);
                }


            } else {
                setSharedPref(clickCount);
                Intent intent = new Intent(HomeActivity.this, PoetSpeechActivity.class);
                startActivity(intent);
            }
        } else if (v.getId() == R.id.ivToolbarHomeFavId) {
            clickCount=clickCount+1;

            if (clickCount==4) {
                if (interstitialAd.isLoaded())
                {
                    setSharedPref(0);
                    interstitialAd.show();

                    interstitialAd.setAdListener(new AdListener() {
                        @Override
                        public void onAdClosed() {
                            startActivity(new Intent(HomeActivity.this, FavouriteActivity.class));
                            interstitialAd.loadAd(new AdRequest.Builder().build());
                        }
                    });
                }
                else {
                    setSharedPref(0);
                    Intent intent = new Intent(HomeActivity.this, FavouriteActivity.class);
                    startActivity(intent);
                }


            } else {
                setSharedPref(clickCount);
                Intent intent = new Intent(HomeActivity.this, FavouriteActivity.class);
                startActivity(intent);
            }
        }

    }

    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            AlertDialog.Builder alertbuilder = new AlertDialog.Builder(this);
            alertbuilder.setTitle("Exit !");
            alertbuilder.setIcon(R.drawable.ic_exit);
            alertbuilder.setMessage("Are You Sure Want To Exit ?");
            alertbuilder.setCancelable(false);
            alertbuilder.setPositiveButton(Html.fromHtml("<font color='#000'>Yes</font>"), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();

                }
            });
            alertbuilder.setNegativeButton(Html.fromHtml("<font color='#000'>No</font>"), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            AlertDialog alertDialog = alertbuilder.create();
            alertDialog.show();

        }

    }
}
