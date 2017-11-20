package com.ridealongpivot;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.content.res.Configuration;
import android.content.res.Resources;

import android.graphics.Color;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.View;

import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.inputmethod.InputMethodManager;

import android.widget.EditText;

import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.RelativeLayout;
import android.widget.Toast;

import com.admarvel.android.ads.AdMarvelUtils;
import com.admarvel.android.ads.AdMarvelView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;

import com.google.android.gms.maps.GoogleMap;

import com.google.android.gms.maps.model.LatLng;

import com.ridealongpivot.googleplaces.Http;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static com.ridealongpivot.GlobalClass.AdMarvelBannerPartnerId;
import static com.ridealongpivot.GlobalClass.AdMarvelBannerSite90;
import static com.ridealongpivot.GlobalClass.AdMarvelBannerSiteId;
import static com.ridealongpivot.GlobalClass.CategoryIcon;
import static com.ridealongpivot.GlobalClass.GetCategoryListB;
import static com.ridealongpivot.GlobalClass.GoogleBusiness;
import static com.ridealongpivot.GlobalClass.GoogleBusiness1;
import static com.ridealongpivot.GlobalClass.ImageViewAnimatedChange;
import static com.ridealongpivot.GlobalClass.ProfilePic;
import static com.ridealongpivot.GlobalClass.Webservice_Url;
import static com.ridealongpivot.GlobalClass.correctCategoryName;
import static com.ridealongpivot.GlobalClass.loadingDialog;

public class Main_StartActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    Context context;
    ImageView iv_help, iv_profile, iv_dictionary, iv_map, iv_selfie;
    LinearLayout ll_menu;
    RelativeLayout rl_main;
    //Fragment fragment;
    FragmentManager fmanager;
    FragmentTransaction ftransaction;
    String responseData="";
    String google_Data="";
    LinearLayout ll_frame;
    int selectedMenu=1;
    boolean isFirstTime=true;
    ArrayList<ReportModel> catIcons;

    final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_FULLSCREEN
            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

    LocationManager lm;

    int PROXIMITY_RADIUS = 2500;

    boolean gps_enabled = false;
    boolean network_enabled = false;
    Location location;
    double latitude = 26.865729;
    double longitude = 75.7524622;
    LatLng latLng=new LatLng(26.865729,75.7524622);
    LatLng lastUpdatedLatLng=new LatLng(26.865729,75.7524622);

    boolean isUpdated=false;

    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;

    ArrayList<ReportModel> businessListForMarker;
    ArrayList<ReportModel> businessAddressList;
    ArrayList<BusinessesModel> google_businessses;

    private Tracker mTracker;
    Dialog mainDialog;
    long lastUpdatedTime=System.currentTimeMillis();

    //AdMarvelView adMarvelView;
    AdView adView;

    private static GoogleAnalytics sAnalytics;
    private static Tracker sTracker;

    private int clickedCount=0;
    boolean isAlreadyClicked=false;
    WindowManager mWindowManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(flags);
        /*requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        setContentView(R.layout.activity_main_start);
        sAnalytics = GoogleAnalytics.getInstance(this);

        executeDelayed();

      //  MultiDexApplication application = (MultiDexApplication) getApplication();
        mTracker = getDefaultTracker();

        context                 = this;
        iv_help                 = (ImageView) findViewById(R.id.iv_help);
        iv_profile              = (ImageView) findViewById(R.id.iv_profile);
        iv_dictionary           = (ImageView) findViewById(R.id.iv_dictionary);
        iv_map                  = (ImageView) findViewById(R.id.iv_map);
        iv_selfie               = (ImageView) findViewById(R.id.iv_selfie);

        rl_main                 = (RelativeLayout) findViewById(R.id.rl_main);
        ll_menu                 = (LinearLayout) findViewById(R.id.ll_menu);
        ll_frame                = (LinearLayout) findViewById(R.id.ll_frame);

        //adMarvelView            = (AdMarvelView) findViewById(R.id.ad);
        adView                  = (AdView) findViewById(R.id.adView);

        mWindowManager          = (WindowManager) getSystemService(Context.WINDOW_SERVICE);


        lm                      = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        fmanager                = getSupportFragmentManager();
       // if (isNavigationBarAvailable()) rl_main.setPadding(0, 0, 0, navBarHeight());
       // mapFragment.getMapAsync(this);
        mainDialog=loadingDialog(Main_StartActivity.this);

        businessListForMarker   = new ArrayList<ReportModel>();
        businessAddressList     = new ArrayList<ReportModel>();
        google_businessses      = new ArrayList<BusinessesModel>();
        catIcons                = new ArrayList<ReportModel>();

        //setAdmarvelAds();
        //startTimer();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            DevicePolicyManager myDevicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
            // get this app package name
          //  ComponentName mDPM = new ComponentName(this, MyAdmin.class);
            //startLockTask();
            if (myDevicePolicyManager.isDeviceOwnerApp(this.getPackageName())) {
                // get this app package name
                String[] packages = {this.getPackageName()};
                // mDPM is the admin package, and allow the specified packages to lock task
                //myDevicePolicyManager.setLockTaskPackages(mDPM, packages);
                startLockTask();

            } else {
                //Toast.makeText(getApplicationContext(), "Not owner", Toast.LENGTH_LONG).show();
            }

          //  setVolumMax();

           startLockTask();
        }
        new GetCategoryIcons().execute();

        checkGPSandNetwork();

        buildGoogleApiClient();
        mGoogleApiClient.connect();

        setAdmobAds();

       // mTracker.setSessionTimeout();

        iv_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    selectedMenu=4;
                    setAnalyticsData();
                    ImageViewAnimatedChange(context, iv_profile, R.drawable.profile_enable);
                    iv_dictionary.setImageResource(R.drawable.dictionary_disable);
                    iv_map.setImageResource(R.drawable.map_disable);
                    iv_selfie.setImageResource(R.drawable.selfie_disable);

                    ftransaction = getSupportFragmentManager().beginTransaction();
                    ftransaction.replace(R.id.fragment_all, new DriverProfileFragment());
                    ftransaction.addToBackStack(null);
                    ftransaction.commit();
                }catch (Exception ignored){
                }
            }
        });

        iv_dictionary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    setAnalyticsData();
                    ImageViewAnimatedChange(context, iv_dictionary, R.drawable.dictionary_enable);
                    iv_profile.setImageResource(R.drawable.profile_disable);
                    iv_map.setImageResource(R.drawable.map_disable);
                    iv_selfie.setImageResource(R.drawable.selfie_disable);

                    ftransaction = getSupportFragmentManager().beginTransaction();
                    DirectoryFragment directoryFragment = new DirectoryFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("data", responseData);
                    bundle.putParcelableArrayList("data_google", google_businessses);
                    directoryFragment.setArguments(bundle);
                    ftransaction.replace(R.id.fragment_all, directoryFragment);
                    ftransaction.addToBackStack(null);
                    ftransaction.commit();
                }catch (Exception ignored){

                }
            }
        });

        iv_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    selectedMenu=2;
                    setAnalyticsData();
                    ImageViewAnimatedChange(context, iv_map, R.drawable.map_enable);
                    iv_profile.setImageResource(R.drawable.profile_disable);
                    iv_dictionary.setImageResource(R.drawable.dictionary_disable);
                    iv_selfie.setImageResource(R.drawable.selfie_disable);

                    ll_menu.setBackgroundColor(Color.parseColor("#80FFFFFF"));

                    ftransaction = getSupportFragmentManager().beginTransaction();
                    MapFragment mapFragment = new MapFragment();
                   /* Bundle bundle = new Bundle();
                    bundle.putString("data", responseData);
                    bundle.putParcelableArrayList("data_google", google_businessses);
                    mapFragment.setArguments(bundle);*/
                    ftransaction.replace(R.id.fragment_all, mapFragment);
                    ftransaction.addToBackStack(null);
                    ftransaction.commit();
                }catch (Exception ignored){

                }

            }
        });
        iv_selfie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    selectedMenu=3;
                    setAnalyticsData();
                    ImageViewAnimatedChange(context, iv_selfie, R.drawable.selfie_enable);
                    iv_profile.setImageResource(R.drawable.profile_disable);
                    iv_dictionary.setImageResource(R.drawable.dictionary_disable);
                    iv_map.setImageResource(R.drawable.map_disable);
                    ll_menu.setBackgroundResource(0);

                    ftransaction = getSupportFragmentManager().beginTransaction();
                    ftransaction.replace(R.id.fragment_all, new SelfieFragment());
                    ftransaction.addToBackStack(null);
                    ftransaction.commit();

                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });

        iv_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    setAnalyticsData();
                    iv_profile.setImageResource(R.drawable.profile_disable);
                    iv_dictionary.setImageResource(R.drawable.dictionary_disable);
                    iv_map.setImageResource(R.drawable.map_disable);
                    iv_selfie.setImageResource(R.drawable.selfie_disable);

                    ftransaction = getSupportFragmentManager().beginTransaction();
                    ftransaction.replace(R.id.fragment_all, new HelpFragment());
                    ftransaction.addToBackStack(null);
                    ftransaction.commit();
                }catch (Exception ignored){

                }
            }
        });

    }


    public void setAnalyticsData(){
        mTracker.set("uid",GlobalClass.callSavedPreferences("email",context));
        mTracker.setClientId(GlobalClass.callSavedPreferences("id",context));
       // mTracker.setSessionTimeout(-1);

        if (selectedMenu == 1){
            mTracker.setScreenName(getResources().getString(R.string.directory_screen));
            mTracker.setTitle(getResources().getString(R.string.directory_screen));
            mTracker.send(new HitBuilders.ScreenViewBuilder().build());
            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory(GlobalClass.callSavedPreferences("email",context))
                    .setCustomDimension(1,GlobalClass.callSavedPreferences("email",context))
                    .setAction(getResources().getString(R.string.directory_screen))
                    .build());

        } else if (selectedMenu == 2){
            mTracker.setScreenName(getResources().getString(R.string.map_screen));
            mTracker.setTitle(getResources().getString(R.string.map_screen));
            mTracker.send(new HitBuilders.ScreenViewBuilder().build());
            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory(GlobalClass.callSavedPreferences("email",context))
                    .setCustomDimension(1,GlobalClass.callSavedPreferences("email",context))
                    .setAction(getResources().getString(R.string.map_screen))
                    .build());
        } else if (selectedMenu == 3){
            mTracker.setScreenName(getResources().getString(R.string.selfie_screen));
            mTracker.setTitle(getResources().getString(R.string.selfie_screen));
            mTracker.send(new HitBuilders.ScreenViewBuilder().build());
            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory(GlobalClass.callSavedPreferences("email",context))
                    .setCustomDimension(1,GlobalClass.callSavedPreferences("email",context))
                    .setAction(getResources().getString(R.string.selfie_screen))
                    .build());
        }
        else if (selectedMenu == 4){
            mTracker.setScreenName(getResources().getString(R.string.driver_info_screen));
            mTracker.setTitle(getResources().getString(R.string.driver_info_screen));
            mTracker.send(new HitBuilders.ScreenViewBuilder().build());
            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory(GlobalClass.callSavedPreferences("email",context))
                    .setCustomDimension(1,GlobalClass.callSavedPreferences("email",context))
                    .setAction(getResources().getString(R.string.driver_info_screen))
                    .build());
        }
        else if (selectedMenu == 5){
            mTracker.setScreenName(getResources().getString(R.string.help_screen));
            mTracker.setTitle(getResources().getString(R.string.help_screen));
            mTracker.send(new HitBuilders.ScreenViewBuilder().build());
            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory(GlobalClass.callSavedPreferences("email",context))
                    .setCustomDimension(1,GlobalClass.callSavedPreferences("email",context))
                    .setAction(getResources().getString(R.string.help_screen))
                    .build());
        }
    }

   /* @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
           // Log.e("Key event", String.valueOf(event));
        if (keyCode == KeyEvent.KEYCODE_BACK){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (!isAlreadyClicked) {
                    isAlreadyClicked=true;
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            isAlreadyClicked=false;
                            clickedCount = 0;
                        }
                    }, 3000);
                }else {
                    clickedCount+=1;
                }
                if (clickedCount >=6){
                    try{
                        stopLockTask();
                    *//*if (mGoogleApiClient != null) {
                        // LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
                        mGoogleApiClient.disconnect();
                    }*//*
                        finish();}
                    catch (Exception e){
                      //  Log.e("exception",e.getMessage());
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            }
            else {
                if (mGoogleApiClient != null) {
                    // LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
                    mGoogleApiClient.disconnect();
                }
                finish();
            }
            return true;
        }

        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP){
            // Toast.makeText(this, "Volume button is disabled", Toast.LENGTH_SHORT).show();
            return true;
        }

        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN){
            //Toast.makeText(this, "Volume button is disabled", Toast.LENGTH_SHORT).show();
            return true;
        }

        if (keyCode == KeyEvent.KEYCODE_MENU){
            // Toast.makeText(this, "Menu button pressed", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_MOVE_HOME){
            //  Toast.makeText(this, "Move home button pressed", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_APP_SWITCH){
            // Toast.makeText(this, "Recent clicked", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_POWER){
            // Toast.makeText(this, "Volume button is disabled", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }*/

    private void setVolumMax(){
        AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        am.setStreamVolume(
                AudioManager.STREAM_SYSTEM,
                am.getStreamMaxVolume(AudioManager.STREAM_SYSTEM),
                0);
    }

    private void executeDelayed() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // execute after 500ms
               hideSystemUI();
            }
        }, 1000);
    }
    private void hideSystemUI() {
        runOnUiThread(new Runnable() {
            public void run() {
                getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                                | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                                | View.SYSTEM_UI_FLAG_IMMERSIVE);

                //changeBackGroundColor();
            }
        });
    }

   /* @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        Log.e("Dispatch key", String.valueOf(event));
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            // keydown logic
            return true;
        }
        return false;
    }*/

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View view = getCurrentFocus();
        if (view != null && (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) && view instanceof EditText && !view.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            view.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + view.getLeft() - scrcoords[0];
            float y = ev.getRawY() + view.getTop() - scrcoords[1];
            if (x < view.getLeft() || x > view.getRight() || y < view.getTop() || y > view.getBottom())
                ((InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow((this.getWindow().getDecorView().getApplicationWindowToken()), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

    protected synchronized void buildGoogleApiClient() {
        //Toast.makeText(this, "buildGoogleApiClient", Toast.LENGTH_SHORT).show();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            latitude = mLastLocation.getLatitude();
            longitude = mLastLocation.getLongitude();
            lastUpdatedLatLng=latLng;
            isUpdated=true;
            new GetCategoryList().execute(String.valueOf(latitude),String.valueOf(longitude));
        }
        if (!isUpdated) new GetCategoryList().execute(String.valueOf(latitude),String.valueOf(longitude));

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000); //5 seconds
        mLocationRequest.setFastestInterval(3000); //3 seconds
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        //mLocationRequest.setSmallestDisplacement(0.1F); //1/10 meter

        if (mGoogleApiClient!=null) LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

       /* mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();*/
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        //remove previous current location marker and add new one at current position
        latLng = new LatLng(location.getLatitude(), location.getLongitude());
        latitude=location.getLatitude();
        longitude=location.getLongitude();

       // Log.e("Dis time", String.valueOf(((System.currentTimeMillis()-lastUpdatedTime) / (1000*60)) % 60)+" "+ String.valueOf((distanceInKm(latitude,longitude,lastUpdatedLatLng.latitude,lastUpdatedLatLng.longitude))/1000));

        if ((int) (((System.currentTimeMillis()-lastUpdatedTime) / (1000*60)) % 60) >= 1 && (distanceInKm(latitude,longitude,lastUpdatedLatLng.latitude,lastUpdatedLatLng.longitude))/1000 >= 1){
            lastUpdatedTime=System.currentTimeMillis();
            lastUpdatedLatLng = latLng;

            new GetCategoryList().execute(String.valueOf(latitude),String.valueOf(longitude));
        }
        if (!isUpdated) new GetCategoryList().execute(String.valueOf(latitude),String.valueOf(longitude));
    }

    public int navBarHeight(){
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    public boolean isNavigationBarAvailable(){
        boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
        boolean hasHomeKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_HOME);

        return (!hasBackKey && !hasHomeKey);
    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    private double distanceInKm(double lat1, double lon1, double lat2, double lon2){
        Location startPoint=new Location("locationA");
        startPoint.setLatitude(lat1);
        startPoint.setLongitude(lon1);

        Location endPoint=new Location("locationA");
        endPoint.setLatitude(lat2);
        endPoint.setLongitude(lon2);

        return (double) startPoint.distanceTo(endPoint);
    }


    public void checkGPSandNetwork(){
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        if (!gps_enabled && !network_enabled) {
            // notify user
            AlertDialog.Builder dialog = new AlertDialog.Builder(context,R.style.MyAlertDialogStyle);
            dialog.setMessage("GPS network not enabled");
            dialog.setPositiveButton("Open location setting", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    context.startActivity(myIntent);
                    //get gps
                }
            });
            dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub

                }
            });
            dialog.show();

        }
    }

    private class GetCategoryList extends AsyncTask<String, String, String> {
        String result = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (isFirstTime) {
                if (mainDialog != null)
                    mainDialog.show();
                isFirstTime=false;
            }
            isUpdated=true;
        }

        @Override
        protected String doInBackground(String... params) {
            String url = Webservice_Url + GetCategoryListB;
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);

            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("driver_id", GlobalClass.callSavedPreferences("id",context)));
                nameValuePairs.add(new BasicNameValuePair("business_lat", params[0]));
                nameValuePairs.add(new BasicNameValuePair("business_long", params[1]));
                /*nameValuePairs.add(new BasicNameValuePair("business_lat", "38.0323672"));
                nameValuePairs.add(new BasicNameValuePair("business_long", "-121.8839713"));*/

                Log.e("param",nameValuePairs.toString());

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                HttpResponse response = httpclient.execute(httppost);

                result = EntityUtils.toString(response.getEntity());

                //Log.e("result Businesses", result);

            } catch (IOException ignored) {

            }

            return result;
        }

        @Override
        protected void onPostExecute(String responseStr) {
            super.onPostExecute(responseStr);
          //  if (dialog.isShowing()) dialog.dismiss();

            if (responseStr.length() > 0) {
                responseData = responseStr;

                google_businessses = new ArrayList<BusinessesModel>();
                StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
                googlePlacesUrl.append("location=" + latitude + "," + longitude);
                googlePlacesUrl.append("&radius=" + PROXIMITY_RADIUS);
                googlePlacesUrl.append("&types=" + GoogleBusiness1);
                googlePlacesUrl.append("&sensor=true");
                googlePlacesUrl.append("&hasNextPage=true");
                googlePlacesUrl.append("&nextPage()=true");
                googlePlacesUrl.append("&key=" + getResources().getString(R.string.google_api_key1));

                GooglePlacesReadTask googlePlacesReadTask = new GooglePlacesReadTask();
                Object[] toPass = new Object[2];
                toPass[1] = googlePlacesUrl.toString();
                googlePlacesReadTask.execute(toPass);

                //  Log.e("Result",responseStr);
            }
        }
    }

    private class GooglePlacesReadTask extends AsyncTask<Object, Integer, String> {
        String googlePlacesData = null;
        GoogleMap googleMap;

        @Override
        protected String doInBackground(Object... inputObj) {
            try {
                googleMap = (GoogleMap) inputObj[0];
                String googlePlacesUrl = (String) inputObj[1];
                Http http = new Http();
                googlePlacesData = http.read(googlePlacesUrl);
              //  Log.e("Google places", googlePlacesData);
            } catch (Exception e) {
                Log.d("Google Place Read Task", e.toString());
            }
            return googlePlacesData;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                google_Data=result;
                final JSONObject places_Object=new JSONObject(result);
                JSONArray resultArray=places_Object.getJSONArray("results");
               // Log.e("Result Google place",String.valueOf(resultArray.length()) +result);
                for (int i=0;i<resultArray.length();i++){
                    JSONObject dataAtPosition=resultArray.getJSONObject(i);
                    JSONObject geometry=dataAtPosition.getJSONObject("geometry");
                    JSONObject locationPoints=geometry.getJSONObject("location");
                    Double latpoint  =locationPoints.getDouble("lat");
                    Double longpoint =locationPoints.getDouble("lng");

                    String businessIcon       = dataAtPosition.getString("icon");
                    String businessId         = dataAtPosition.getString("place_id");
                    String businessName       = dataAtPosition.getString("name");
                    String businessVicinity   = dataAtPosition.getString("vicinity");

                    JSONArray businessTypeArray= dataAtPosition.getJSONArray("types");
                    String business_Type=businessTypeArray.getString(0);

                    String catName = correctCategoryName(business_Type);

                    if (GoogleBusiness.contains(business_Type)) {
                        for (int j=0;j<catIcons.size();j++){
                            ReportModel catModel=new ReportModel();
                            catModel=catIcons.get(j);
                            if (catModel.getCat_name().equals(catName)){
                                businessIcon = catModel.getCat_img();
                                //Log.e("Cat name",catName+" "+businessIcon);
                            }
                        }
                        //int isAvailable=0;

                        BusinessesModel reportModel = new BusinessesModel();
                        reportModel.setBuss_id(businessId);
                        reportModel.setBuss_name(businessName);
                        reportModel.setBuss_img(businessIcon);
                        reportModel.setCat_name(catName);
                        reportModel.setBuss_from(1);
                        reportModel.setIsUnlocked(0);
                        reportModel.setBuss_lat(String.valueOf(latpoint));
                        reportModel.setBuss_long(String.valueOf(longpoint));
                        reportModel.setBuss_marker_icon(ProfilePic);
                        reportModel.setBuss_icon_type(0);

                        ArrayList<AddressModel> add=new ArrayList<>();
                        AddressModel addressModel=new AddressModel();
                        addressModel.setBuss_id(businessId);
                        addressModel.setBuss_name(businessName);
                        addressModel.setBuss_img(businessIcon);
                        addressModel.setCat_name(catName);
                        addressModel.setBuss_from(1);
                        addressModel.setBuss_lat(String.valueOf(latpoint));
                        addressModel.setBuss_long(String.valueOf(longpoint));
                        addressModel.setBuss_marker_icon(ProfilePic);
                        addressModel.setBuss_icon_type(0);
                        add.add(addressModel);

                        reportModel.setListAddress(add);
                        google_businessses.add(reportModel);
                    }

                    /*for (int j=0;j<businessTypeArray.length();j++){
                        String business_Type=businessTypeArray.getString(j);

                        if (GoogleBusiness.contains(business_Type)) {
                            //int isAvailable=0;
                            BusinessesModel reportModel = new BusinessesModel();
                            reportModel.setBuss_id(businessId);
                            reportModel.setBuss_name(businessName);
                            reportModel.setBuss_img(businessIcon);
                            reportModel.setCat_name(correctCategoryName(business_Type));
                            reportModel.setBuss_from(1);
                            reportModel.setBuss_lat(String.valueOf(latpoint));
                            reportModel.setBuss_long(String.valueOf(longpoint));
                            reportModel.setBuss_marker_icon(ProfilePic);
                            reportModel.setBuss_icon_type(0);

                            ArrayList<AddressModel> add=new ArrayList<>();
                            AddressModel addressModel=new AddressModel();
                            addressModel.setBuss_id(businessId);
                            addressModel.setBuss_name(businessName);
                            addressModel.setBuss_img(businessIcon);
                            addressModel.setCat_name(correctCategoryName(business_Type));
                            addressModel.setBuss_from(1);
                            addressModel.setBuss_lat(String.valueOf(latpoint));
                            addressModel.setBuss_long(String.valueOf(longpoint));
                            addressModel.setBuss_marker_icon(ProfilePic);
                            addressModel.setBuss_icon_type(0);
                            add.add(addressModel);

                            reportModel.setListAddress(add);
                            google_businessses.add(reportModel);
                        }
                    }*/

                }
                try {
                    final String nextpageToken = places_Object.getString("next_page_token");
                    if (!nextpageToken.equals("")|| !nextpageToken.equals(null)) {
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
                                googlePlacesUrl.append("location=" + latitude + "," + longitude);
                                googlePlacesUrl.append("&radius=" + PROXIMITY_RADIUS);
                                //googlePlacesUrl.append("&types=" + "all");
                                googlePlacesUrl.append("&sensor=false");
                                googlePlacesUrl.append("&hasNextPage=true");
                                googlePlacesUrl.append("&nextPage()=true");
                                googlePlacesUrl.append("&key=" + getResources().getString(R.string.google_api_key1));
                                googlePlacesUrl.append("&pagetoken=" + nextpageToken);
                                   // Log.e("Next page token", places_Object.getString("next_page_token"));
                                GooglePlacesReadTask googlePlacesReadTask = new GooglePlacesReadTask();
                                Object[] toPass = new Object[2];
                                toPass[1] = googlePlacesUrl.toString();
                                googlePlacesReadTask.execute(toPass);
                            }
                        }, 3000);
                    }
                }catch (Exception e){
                    //e.printStackTrace();
                    setAnalyticsData();
                    if (mainDialog.isShowing()) mainDialog.dismiss();
                    if (selectedMenu == 1) {
                        ftransaction = getSupportFragmentManager().beginTransaction();
                        DirectoryFragment directoryFragment = new DirectoryFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("data", responseData);
                        bundle.putParcelableArrayList("data_google", google_businessses);
                        directoryFragment.setArguments(bundle);
                        ftransaction.replace(R.id.fragment_all, directoryFragment);
                        ftransaction.addToBackStack(null);
                        ftransaction.commit();
                    }

                }

            }catch (Exception ignored){

            }
        }
    }

    private class GetCategoryIcons extends AsyncTask<String, String, String> {
        String result = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            String url = Webservice_Url + CategoryIcon;
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);
            try {
                HttpResponse response = httpclient.execute(httppost);
                result = EntityUtils.toString(response.getEntity());
                Log.e("result Businesses", result);

            } catch (IOException ignored) {
            }

            return result;
        }

        @Override
        protected void onPostExecute(String responseStr) {
            super.onPostExecute(responseStr);
            //  if (dialog.isShowing()) dialog.dismiss();

            if (responseStr.length() > 0) {
                try {
                    JSONObject iconObject=new JSONObject(responseStr);
                    if (iconObject.getBoolean("status")){
                        catIcons=new ArrayList<ReportModel>();
                        JSONArray iconList=iconObject.getJSONArray("data");
                        for (int i=0;i<iconList.length();i++){
                            ReportModel reportModel=new ReportModel();
                            JSONObject catIc=iconList.getJSONObject(i);
                            reportModel.setCat_img(ProfilePic+catIc.getString("cat_img"));
                            reportModel.setCat_name(catIc.getString("cat_name"));
                            catIcons.add(reportModel);
                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    synchronized public Tracker getDefaultTracker() {
        // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
        if (sTracker == null) {
            sTracker = sAnalytics.newTracker(R.xml.global_tracker);
        }

        return sTracker;
    }

    public void setAdmobAds(){
        MobileAds.initialize(getApplicationContext(), getResources().getString(R.string.banner_bottom));
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
            }
        });
    }

    /*public void setAdmarvelAds(){
        try {
            Map<AdMarvelUtils.SDKAdNetwork, String> publisherIds = new HashMap<AdMarvelUtils.SDKAdNetwork, String>();
            publisherIds.put(null, null);
            AdMarvelUtils.initialize(Main_StartActivity.this, publisherIds);

            Map<String, Object> targetParams = new HashMap<String, Object>();
            //targetParams.put("KEYWORDS", "games");
            targetParams.put("APP_VERSION", "1.0.0"); //version of your app
            adMarvelView.requestNewAd(targetParams, AdMarvelBannerPartnerId, AdMarvelBannerSite90);
        }catch (Exception ignored){

        }
    }*/


    @Override
    protected void onStop(){
        super.onStop();
    }

    @Override
    public void onResume(){
        super.onResume();
        /*try {
            adMarvelView.resume(this);
        }catch (Exception ignored){

        }*/
    }

    @Override
    protected void onPause() {
        super.onPause();
        //stopTimer();
        /*try {
            adMarvelView.pause(this);
        }catch (Exception ignored){

        }*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*try {
            adMarvelView.destroy();
        }catch (Exception ignored){

        }*/
    }


    @Override
    public void onBackPressed(){
        //super.onBackPressed();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (!isAlreadyClicked) {
                isAlreadyClicked=true;
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isAlreadyClicked=false;
                        clickedCount = 0;
                    }
                }, 3000);
            }else {
                clickedCount+=1;
            }
            if (clickedCount >=6){
                try {
                    stopLockTask();
                } catch (NullPointerException ignored){}
                if (mGoogleApiClient != null) {
                   // LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
                    mGoogleApiClient.disconnect();
                }
                finish();
            }
        }
        else {
            if (mGoogleApiClient != null) {
               // LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
                mGoogleApiClient.disconnect();
            }
            finish();
        }
    }
}
