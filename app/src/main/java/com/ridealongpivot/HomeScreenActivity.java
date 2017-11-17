package com.ridealongpivot;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.admarvel.android.ads.AdMarvelUtils;
import com.admarvel.android.ads.AdMarvelView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.messaging.FirebaseMessaging;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
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
import static com.ridealongpivot.GlobalClass.CheckDriverStatus;
import static com.ridealongpivot.GlobalClass.LogoutDriver;
import static com.ridealongpivot.GlobalClass.Webservice_Url;
import static com.ridealongpivot.GlobalClass.alertDialog;
import static com.ridealongpivot.GlobalClass.loadingDialog;
import static com.ridealongpivot.GlobalClass.logout;

public class HomeScreenActivity extends AppCompatActivity {

    Context context;
    Button bt_profile,bt_back_ofc,bt_start,bt_logout;
    RelativeLayout rl_main;
    PackageInfo packageInfo;
    String key = null;
    String user_id;
    private static final int TIME_DELAY = 2000;
    private static long back_pressed;
    //AdMarvelView adMarvelView;
    AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        context             = this;
        bt_profile          = (Button) findViewById(R.id.bt_profile);
        bt_back_ofc         = (Button) findViewById(R.id.bt_back_office);
        bt_start            = (Button) findViewById(R.id.bt_start);
        bt_logout           = (Button) findViewById(R.id.bt_logout);
        rl_main             = (RelativeLayout) findViewById(R.id.rl_main);
        //adMarvelView        = (AdMarvelView) findViewById(R.id.ad);
        adView              = (AdView) findViewById(R.id.adView);
        user_id             = GlobalClass.callSavedPreferences("id",context);

        if (isNavigationBarAvailable()) rl_main.setPadding(0,0,0,navBarHeight());

        FirebaseMessaging.getInstance().subscribeToTopic("news");

        try {
            Permissions permissions = new Permissions(context);
            if (!permissions.checkPermission()) permissions.requestPermission();

            if (!user_id.equals("") && !user_id.equals("0") && !user_id.equals(null)) {
                new CheckDriverStatus().execute(user_id);
            } else {
                startActivity(new Intent(context, LoginSignupActivity.class));
                overridePendingTransition(0, 0);
            }
        }catch (Exception ignore){

        }

        //setAdmarvelAds();
        setAdmobAds();

        bt_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (user_id.equals("") || user_id.equals("0") || user_id.equals(null)) {
                        startActivity(new Intent(context, LoginSignupActivity.class));
                        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                    } else {
                        startActivity(new Intent(context, Main_StartActivity.class));
                    }
                }catch (Exception ignore){

                }
            }
        });

        bt_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (user_id.equals("") || user_id.equals("0") || user_id.equals(null)) {
                        startActivity(new Intent(context, LoginSignupActivity.class));
                    } else {
                        startActivity(new Intent(context, ProfileActivity.class));
                    }
                    overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                }catch (Exception ignore){

                }
            }
        });

        bt_back_ofc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (user_id.equals("") || user_id.equals("0") || user_id.equals(null)) {
                        startActivity(new Intent(context, LoginSignupActivity.class));
                        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                    } else {
                        try {
                            Uri uri = Uri.parse("http://ridealongmedia.leaddyno.com/"); // missing 'http://' will cause crashed
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(intent);
                        } catch (ActivityNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }catch (Exception ignore){

                }
            }
        });

        bt_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    new LogoutDriver().execute();
                   /* logout(context);
                    startActivity(new Intent(context, LoginSignupActivity.class));
                    overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                    finish();*/

                }catch (Exception ignore){

                }
            }
        });

       /* String packageName = context.getApplicationContext().getPackageName();
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        for (Signature signature : packageInfo.signatures) {
            MessageDigest md = null;
            try {
                md = MessageDigest.getInstance("SHA");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            md.update(signature.toByteArray());
            key = new String(Base64.encode(md.digest(), 0));

            // String key = new String(Base64.encodeBytes(md.digest()));
            Log.e("Key Hash=", key);
        }*/

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
            AdMarvelUtils.initialize(HomeScreenActivity.this, publisherIds);

            final Map<String, Object> targetParams = new HashMap<String, Object>();
            //targetParams.put("KEYWORDS", "games");
            targetParams.put("APP_VERSION", "1.0.0"); //version of your app
            adMarvelView.requestNewAd(targetParams, AdMarvelBannerPartnerId, AdMarvelBannerSiteId);
            adMarvelView.setListener( new AdMarvelView.AdMarvelViewListener() {
                @Override
                public void onRequestAd(AdMarvelView arg0) {
                    Log.e("adrequested","addddd");
                }

                @Override
                public void onReceiveAd(AdMarvelView arg0) {
                    Log.e("adrequested","add received");
                }

                @Override
                public void onFailedToReceiveAd(AdMarvelView arg0, int arg1, AdMarvelUtils.ErrorReason arg2) {
                    Log.e("adrequested","failed to receive");
                }

                @Override
                public void onExpand(AdMarvelView arg0) {
                    Log.e("adrequested","expand");
                }

                @Override
                public void onClose(AdMarvelView arg0) {
                    Log.e("adrequested","close");
                }

                @Override
                public void onClickAd( AdMarvelView arg0, String arg1) {

                }
            } );
        }catch (Exception ignored){

        }


    }*/

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

    private class CheckDriverStatus extends AsyncTask<String, String, String> {
        String result = "";
        Dialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog=loadingDialog(HomeScreenActivity.this);
            if (dialog != null)
                dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            String url = Webservice_Url + CheckDriverStatus;
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);

            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("driver_id",params[0]));

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);

                result = EntityUtils.toString(response.getEntity());

                //Log.e("result status", result);

            } catch (IOException e) {

            }

            return result;
        }

        @Override
        protected void onPostExecute(String responseStr) {
            super.onPostExecute(responseStr);
            if (dialog!=null && dialog.isShowing())
                dialog.dismiss();

            if (responseStr.length() > 0) {
                try {
                    JSONObject login_object=new JSONObject(responseStr);
                    if (!login_object.getBoolean("status")) {
                        alertDialog(context,login_object.getString("message"));
                        logout(context);
                        user_id="";
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //  Log.e("Result",responseStr);
            }
        }
    }

    private class LogoutDriver extends AsyncTask<String, String, String> {
        String result = "";
        Dialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog=loadingDialog(HomeScreenActivity.this);
            if (dialog != null)
                dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = Webservice_Url + LogoutDriver;
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);

            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("activity_id", GlobalClass.callSavedPreferences("activity_id",context)));
                nameValuePairs.add(new BasicNameValuePair("driver_id", GlobalClass.callSavedPreferences("id",context)));
                //nameValuePairs.add(new BasicNameValuePair("Country", "country name"));
                /*StringEntity se = new StringEntity(nameValuePairs);
                se.setContentType("application/json;charset=UTF-8");
                se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json;charset=UTF-8"));*/
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                Log.e("Name value logout ", String.valueOf(nameValuePairs));

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);

                result = EntityUtils.toString(response.getEntity());

                Log.e("result Logout", result);

            } catch (IOException e) {

            }

            return result;
        }

        @Override
        protected void onPostExecute(String responseStr) {
            super.onPostExecute(responseStr);
            if (dialog!= null && dialog.isShowing()) dialog.dismiss();

            if (responseStr.length() > 0) {
                try {
                    JSONObject city_object=new JSONObject(responseStr);
                    if (city_object.getBoolean("status")) {
                        logout(context);
                        startActivity(new Intent(context, LoginSignupActivity.class));
                        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                        finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //  Log.e("Result",responseStr);
            }
        }

        @Override
        protected void onCancelled(String s) {
            super.onCancelled(s);
            if (dialog.isShowing()) dialog.dismiss();
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        user_id  = GlobalClass.callSavedPreferences("id",context);
       /* try {
           // AdMarvelView adMarvelView = (AdMarvelView) findViewById(R.id.ad);
            adMarvelView.resume(this);
        }catch (Exception ignored){

        }*/
    }

    @Override
    protected void onPause() {
        super.onPause();
        /*try {
           // AdMarvelView adMarvelView = (AdMarvelView) findViewById(R.id.ad);
            adMarvelView.pause(this);
        }catch (Exception ignored){

        }*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*try {
           // AdMarvelView adMarvelView = (AdMarvelView) findViewById(R.id.ad);
            adMarvelView.destroy();
        }catch (Exception ignored){

        }*/
    }

    @Override
    public void onBackPressed() {
        if (back_pressed + TIME_DELAY > System.currentTimeMillis()) {
            moveTaskToBack(true);
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                finishAffinity();
            }
            else ActivityCompat.finishAffinity(this);
            System.exit(0);
        } else {
            View view1 = findViewById(android.R.id.content);
            Snackbar snackbar = Snackbar.make(view1,getResources().getString(R.string.press_again), Snackbar.LENGTH_SHORT);
            snackbar.show();
            View view=snackbar.getView();
            view.setBackgroundColor(getResources().getColor(R.color.logo_color));
        }
        back_pressed = System.currentTimeMillis();

    }
}
