package com.ridealongpivot;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.Timer;
import java.util.TimerTask;

import cn.fanrunqi.waveprogress.WaveProgressView;


public class GlobalClass {

    public static String APP_ID_ADCOLONY      = "appdb1d0488a47b46dc9d";
    public static String ZONE_ID_AFTER_SELFIE = "vz04b0895194414a07ae";
    public static String ZONE_ID_IN_MAP       = "vz3099d46d64e6455188";

    public static String TWITTER_KEY          = "5kGbr8Nxw9NXS43DjuDQdCzVW";
    public static String TWITTER_SECRET       = "UGEUejkMLrl1qitVCo8IGq2rXktDbVMbDebTOU9UTQGCPoYOOj";
    /*public static String APP_ID_ADCOLONY      = "app185a7e71e1714831a49ec7";
    public static String ZONE_ID_AFTER_SELFIE = "vz06e8c32a037749699e7050";*/

    public static String Webservice_Url         = "http://13.58.51.170/webservice/";
    public static String ProfilePic             = "http://13.58.51.170";
    public static String SubmitFeedback         = "driver_rating?apikey=ridealong";
    public static String UpdateProfile          = "driver_profile?apikey=ridealong";
    public static String GetCityState           = "city_state?apikey=ridealong";
    public static String GetCategoryList        = "category?apikey=ridealong";
    public static String GetCategoryListB       = "business?apikey=ridealong";
    public static String CategoryIcon           = "category?apikey=ridealong";

    public static String RegisterDriver         = "driver_signup?apikey=ridealong";
    public static String LoginDriver            = "driver_login?apikey=ridealong";
    public static String LoginDriverFB_GL       = "driver_signup_fb_gp?apikey=ridealong";

    public static String ForgotPassword         = "driver_forgot?apikey=ridealong";
    public static String UpdatePassword         = "driver_reset?apikey=ridealong";

    public static String CheckDriverStatus      = "driver_block_status?apikey=ridealong";

    public static String HelpCompanyInfo        = "help?apikey=ridealong";
    public static String GetDriverInfo          = "driver_info?apikey=ridealong";
    public static String FreeBusinessTrial      = "business_trial?apikey=ridealong";
    public static String SubmitFeedbackHelp     = "company_feedback?apikey=ridealong";
    public static String LogoutDriver           = "driver_logout?apikey=ridealong";

    public static String CompanyAdsImage        = "companypicture?apikey=ridealong";
    public static String GetSpecialOffer        = "get_special_offer?apikey=ridealong";

    public static String FacebookPrefix         = "http://www.facebook.com/plugins/likebox.php?href=";
    public static String FacebookPostix         = "&show_faces=true&colorscheme=light&small_header=true&stream=true&&hide_cover=true&show_border=false&header=false&width=300&height=800";

    public static String Category_Icons         = "category?apikey=ridealong";

    public static String USER_ID                = "photos@ridealong.media";
    public static String USER_PASS              = "@1westside1";

    public static String GoogleBusiness         = "food|airport|amusement_park|aquarium|art_gallery|bakery|bar|beauty_salon|" +
            "book_store|bowling_alley|cafe|casino|clothing_store|department_store|electronics_store|furniture_store|"+
            "gym|hair_care|home_goods_store|jewelry_store|meal_takeaway|" +
            "movie_theater|museum|night_club|park|pet_store|restaurant|shoe_store|shopping_mall|" +
            "spa|stadium|store|zoo";

    public static String GoogleBusiness1         = "food|airport|amusement_park|aquarium|art_gallery|bakery|bar|beauty_salon|book_store|bowling_alley|cafe|casino|clothing_store|department_store|electronics_store|gym|hair_care|jewelry_store|movie_theater|museum|night_club|park|restaurant|shopping_mall|spa|stadium|store|zoo";

    public static String correctCategoryName(String cat_name){
        String cat="";
        switch (cat_name) {
            case "food":
                cat = "Food";
                cat = "Restaurant";
                break;
            case "airport":
                cat = "Airport";
                cat = "Transportation";
                break;
            case "amusement_park":
                cat = "Amusement Park";
                cat = "Entertainment";
                break;
            case "aquarium":
                cat = "Aquarium";
                cat = "Entertainment";
                break;
            case "art_gallery":
                cat = "Art Gallery";
                cat = "Arts & Culture";
                break;
            case "bakery":
                cat = "Bakery";
                cat = "Restaurant";
                break;
            case "bar":
                cat = "Bar";
                cat = "Bars/Nightlife";
                break;
            case "beauty_salon":
                cat = "Beauty Salon";
                cat = "Health & Beauty";
                break;
            case "book_store":
                cat = "Book Store";
                cat = "Retail";
                break;
            case "bowling_alley":
                cat = "Bowling Alley";
                cat = "Entertainment";
                break;
            case "cafe":
                cat = "Cafe";
                cat = "Restaurant";
                break;
            case "casino":
                cat = "Casino";
                cat = "Entertainment";
                break;
            case "clothing_store":
                cat = "Clothing Store";
                cat = "Retail";
                break;
            case "department_store":
                cat = "Department Store";
                cat = "Retail";
                break;
            case "electronics_store":
                cat = "Electronics Store";
                cat = "Retail";
                break;
            case "furniture_store":
                cat = "Electronics Store";
                cat = "Retail";
                break;
            case "gym":
                cat = "Gym";
                cat = "Health & Beauty";
                break;
            case "hair_care":
                cat = "Hair Care";
                cat = "Health & Beauty";
                break;
            case "home_goods_store":
                cat = "Home Goods Store";
                cat = "Retail";
                break;
            case "jewelry_store":
                cat = "Jewelry Store";
                cat = "Retail";
                break;
            case "meal_takeaway":
                cat = "Meal Takeaway";
                cat = "Carryout";
                break;
            case "movie_theater":
                cat = "Movie Theater";
                cat = "Entertainment";
                break;
            case "museum":
                cat = "Museum";
                cat = "Arts & Culture";
                break;
            case "night_club":
                cat = "Night Club";
                cat = "Bars/Nightlife";
                break;
            case "park":
                cat = "Park";
                cat = "Entertainment";
                break;
            case "pet_store":
                cat = "Pet Store";
                cat = "Retail";
                break;
            case "restaurant":
                cat = "Restaurant";
                break;
            case "shoe_store":
                cat = "Shoe Store";
                cat = "Retail";
                break;
            case "shopping_mall":
                cat = "Shopping Mall";
                cat = "Retail";
                break;
            case "spa":
                cat = "Spa";
                cat = "Health & Beauty";
                break;
            case "stadium":
                cat = "Stadium";
                cat = "Entertainment";
                break;
            case "store":
                cat = "Store";
                cat = "Retail";
                break;
            case "zoo":
                cat = "Zoo";
                cat = "Entertainment";
                break;
        }

        return cat;
    }

    public static String AdMarvelBannerSiteId   = "205918";
    public static String AdMarvelBannerPartnerId= "5f1189b453b4d67f";

    public static void showToastMessage(final Activity context,
                                        final String message) {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                try {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                } catch (Exception e) {

                }
            }
        });

    }


    public static void savePreferences(String key, String value,
                                       final Context context) {

        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(key, value);

        editor.commit();
    }

    public static String callSavedPreferences(String key, Context context) {

        SharedPreferences sharedPreferences = PreferenceManager

                .getDefaultSharedPreferences(context);

        String name = sharedPreferences.getString(key, "");

        return name;
    }

    public static void savePreferencesInt(String key, int p,
                                          final Context context) {

        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(key, p);

        editor.commit();
    }

    public static int callSavedPreferencesInt(String key, int p, Context context) {

        SharedPreferences sharedPreferences = PreferenceManager

                .getDefaultSharedPreferences(context);

        p = sharedPreferences.getInt(key, p);

        return p;
    }

    public static void savePreferencesDouble(String key, double p,
                                             final Context context) {

        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putLong(key, Double.doubleToRawLongBits(p));

        editor.commit();
    }

    public static double callSavedPreferencesDouble(String key, double p, Context context) {

        SharedPreferences sharedPreferences = PreferenceManager

                .getDefaultSharedPreferences(context);

        p = Double.longBitsToDouble(sharedPreferences.getLong(key, Double.doubleToLongBits(p)));

        return p;
    }
   /* public static void activitySlideBackAnimation(Activity context) {
        context.overridePendingTransition(R.anim.slide_back_left_to_right,
                R.anim.slide_back_right_to_left);
    }

    public static void activitySlideForwardAnimation(Activity context) {
        context.overridePendingTransition(R.anim.slide_forward_left_to_right,
                R.anim.slide_forward_right_to_left);
    }   */

    public static void noConnection_dialog(final Context context) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder
                .setMessage("No internet connection on your device. Would you like to enable it?")
                .setTitle("No Internet Connection")
                .setCancelable(false)
                .setPositiveButton(" Enable Internet ",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int id) {
                                Intent dialogIntent = new Intent(android.provider.Settings.ACTION_SETTINGS);
                                dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                ((Activity) context).startActivity(dialogIntent);
                            }
                        });

        alertDialogBuilder.setNegativeButton(" Cancel ", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    public static void logout(Context context) {
        GlobalClass.savePreferences("id", "", context);
        GlobalClass.savePreferences("fname", "", context);
        GlobalClass.savePreferences("lname", "", context);
        GlobalClass.savePreferences("email", "", context);
        GlobalClass.savePreferences("dob", "", context);
        GlobalClass.savePreferences("dob", "", context);
        GlobalClass.savePreferences("address", "", context);
        GlobalClass.savePreferences("zip_code", "", context);
        GlobalClass.savePreferences("city", "", context);
        GlobalClass.savePreferences("state", "", context);
        GlobalClass.savePreferences("rs_provider", "", context);
        GlobalClass.savePreferences("rs_provider_othr", "", context);
        GlobalClass.savePreferences("provider_level", "", context);
        GlobalClass.savePreferences("provider_level_othr", "", context);
        GlobalClass.savePreferences("referral", "", context);
        GlobalClass.savePreferences("driver_rate", "", context);

        GlobalClass.savePreferences("profile_pic", "", context);
        GlobalClass.savePreferences("short_bio", "", context);
        GlobalClass.savePreferences("car_model", "", context);
        GlobalClass.savePreferences("car_licence", "", context);
        GlobalClass.savePreferences("car_color", "", context);

    }

    public static String getSecurePassword(String passwordToHash, byte[] salt) {
        String generatedPassword = null;
        try {
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            //Add password bytes to digest
            md.update(salt);
            //Get the hash's bytes
            byte[] bytes = md.digest(passwordToHash.getBytes());
            //This bytes[] has bytes in decimal format;
            //Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            //Get complete hashed password in hex format
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }

    //Add salt
    public static byte[] getSalt() throws NoSuchAlgorithmException, NoSuchProviderException {
        //Always use a SecureRandom generator
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        //Create array for salt
        byte[] salt = new byte[16];
        //Get a random salt
        sr.nextBytes(salt);
        //return salt
        return salt;
    }

    public static String md5(String input) {

        String md5 = null;

        if (null == input) return null;

        try {

            //Create MessageDigest object for MD5
            MessageDigest digest = MessageDigest.getInstance("MD5");

            //Update input string in message digest
            digest.update(input.getBytes(), 0, input.length());

            //Converts message digest value in base 16 (hex)
            md5 = new BigInteger(1, digest.digest()).toString(16);

        } catch (NoSuchAlgorithmException e) {

            e.printStackTrace();
        }
        //return md5;
        return input;
    }

    public static Dialog loadingDialog(Activity activity) {
        View child = activity.getLayoutInflater().inflate(R.layout.progressbar, null);
        final WaveProgressView waveProgressView = (WaveProgressView) child.findViewById(R.id.pp);
        final int[] progress = {30};

        final Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                waveProgressView.setCurrent(progress[0], "");
                waveProgressView.setWaveColor("#ff6600");
                progress[0]++;
                if (progress[0] >= 100)
                    timer.cancel();
            }
        }, 0, 100);
        Dialog alertDialog = new Dialog(activity);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setContentView(child);
        alertDialog.setCancelable(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return alertDialog;
    }

    public static int convDpToPx(Context context, float dp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, metrics);
    }

    public static void ImageViewAnimatedChange(Context c, final ImageView v, final int new_image) {
        final Animation anim_out = AnimationUtils.loadAnimation(c, android.R.anim.fade_out);
        final Animation anim_in  = AnimationUtils.loadAnimation(c, android.R.anim.fade_in);
        anim_out.setAnimationListener(new Animation.AnimationListener()
        {
            @Override public void onAnimationStart(Animation animation) {}
            @Override public void onAnimationRepeat(Animation animation) {}
            @Override public void onAnimationEnd(Animation animation)
            {
                v.setImageResource(new_image);
                anim_in.setAnimationListener(new Animation.AnimationListener() {
                    @Override public void onAnimationStart(Animation animation) {}
                    @Override public void onAnimationRepeat(Animation animation) {}
                    @Override public void onAnimationEnd(Animation animation) {}
                });
                v.startAnimation(anim_in);
            }
        });
        v.startAnimation(anim_out);
    }

    public static void alertDialog(Context context,String text){
        final Dialog dialog;
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.alert_dialog);
        dialog.setCancelable(false);
        ImageView iv_cancel;
        TextView tv_text;

        //bt_submit    = (Button) dialog.findViewById(R.id.bt_submit);

        iv_cancel   = (ImageView) dialog.findViewById(R.id.iv_cancel);
        tv_text     = (TextView) dialog.findViewById(R.id.tv_message);

        tv_text.setText(text);

        iv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}

