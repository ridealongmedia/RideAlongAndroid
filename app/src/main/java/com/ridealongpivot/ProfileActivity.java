package com.ridealongpivot;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.admarvel.android.ads.AdMarvelUtils;
import com.admarvel.android.ads.AdMarvelView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static com.ridealongpivot.GlobalClass.AdMarvelBannerPartnerId;
import static com.ridealongpivot.GlobalClass.AdMarvelBannerSiteId;
import static com.ridealongpivot.GlobalClass.GetCityState;
import static com.ridealongpivot.GlobalClass.ProfilePic;
import static com.ridealongpivot.GlobalClass.UpdateProfile;
import static com.ridealongpivot.GlobalClass.Webservice_Url;
import static com.ridealongpivot.GlobalClass.convDpToPx;
import static com.ridealongpivot.GlobalClass.loadingDialog;

public class ProfileActivity extends AppCompatActivity implements TextWatcher {
    Context context;

    TextInputLayout input_f_name,input_l_name,input_bio,input_address,input_city,input_state,input_zip,input_dob,input_car_model,input_car_no,input_car_color,input_referral;
    TextInputEditText et_f_name,et_l_name,et_bio,et_address,et_city,et_state,et_zip,et_dob,et_car_model,et_car_no,et_car_color,et_referral;

    Spinner sp_provider;
    EditText et_provider,et_level;
    RelativeLayout rl_main;

    ImageView iv_user,iv_camera;
    RatingBar ratingBar;

    CheckBox cb_level_other;

    RecyclerView rv_levels;

    Button bt_update;
    ProviderLevelAdapter level_adapter;
    ArrayList<ReportModel> list_level;

    //RelativeLayout rl_main;
    Calendar myCalendar = Calendar.getInstance();
    ArrayList<String> provider_list;
    ArrayList<String> level_list;
    int selectedProvider=0;
    String otherProvider="",otherLevel="";

    String userChoosenTask;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    String encoded_image="";
    String selected_checkbox;
    //AdMarvelView adMarvelView;
    AdView adView;
    private Timer mTimer1;
    private TimerTask mTt1;
    private Handler mTimerHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        context             = this;
        rl_main             = (RelativeLayout) findViewById(R.id.rl_main);
        input_f_name        = (TextInputLayout) findViewById(R.id.input_f_name);
        input_l_name        = (TextInputLayout) findViewById(R.id.input_l_name);
        input_bio           = (TextInputLayout) findViewById(R.id.input_bio);
        input_address       = (TextInputLayout) findViewById(R.id.input_address);
        input_city          = (TextInputLayout) findViewById(R.id.input_city);
        input_state         = (TextInputLayout) findViewById(R.id.input_state);
        input_zip           = (TextInputLayout) findViewById(R.id.input_zip);
        input_dob           = (TextInputLayout) findViewById(R.id.input_dob);
        input_referral      = (TextInputLayout) findViewById(R.id.input_referral);

        input_car_model     = (TextInputLayout) findViewById(R.id.input_car_model);
        input_car_no        = (TextInputLayout) findViewById(R.id.input_car_no);
        input_car_color     = (TextInputLayout) findViewById(R.id.input_car_color);

        et_f_name           = (TextInputEditText) findViewById(R.id.et_f_name);
        et_l_name           = (TextInputEditText) findViewById(R.id.et_l_name);
        et_bio              = (TextInputEditText) findViewById(R.id.et_bio);

        et_address          = (TextInputEditText) findViewById(R.id.et_address);
        et_city             = (TextInputEditText) findViewById(R.id.et_city);
        et_state            = (TextInputEditText) findViewById(R.id.et_state);
        et_zip              = (TextInputEditText) findViewById(R.id.et_zip);
        et_dob              = (TextInputEditText) findViewById(R.id.et_dob);

        et_car_model        = (TextInputEditText) findViewById(R.id.et_car_model);
        et_car_no           = (TextInputEditText) findViewById(R.id.et_car_no);
        et_car_color        = (TextInputEditText) findViewById(R.id.et_car_color);
        et_referral         = (TextInputEditText) findViewById(R.id.et_referral);

        sp_provider         = (Spinner) findViewById(R.id.sp_ride_provider);

        rv_levels           = (RecyclerView) findViewById(R.id.rv_level);

        et_provider         = (EditText) findViewById(R.id.et_ride_provider);
        et_level            = (EditText) findViewById(R.id.et_level);

        iv_user             = (ImageView) findViewById(R.id.iv_user);
        iv_camera           = (ImageView) findViewById(R.id.iv_camera);

        ratingBar           = (RatingBar) findViewById(R.id.rating_bar);

        cb_level_other      = (CheckBox) findViewById(R.id.cb_level_other);
        bt_update           = (Button) findViewById(R.id.bt_update_profile);

        //adMarvelView        = (AdMarvelView) findViewById(R.id.ad);
        adView              = (AdView) findViewById(R.id.adView);

        if (isNavigationBarAvailable()) rl_main.setPadding(0,0,0,navBarHeight());

        String android_id = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
        Log.e("device id",android_id);

        //setAdmarvelAds();
        //startTimer();
        setAdmobAds();

        list_level          = new ArrayList<ReportModel>();

        sp_provider.getBackground().setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_ATOP);

        provider_list = new ArrayList<String>();
        provider_list.add("Uber");
        provider_list.add("Lyft");
        provider_list.add("Both");
        provider_list.add("Other");

        level_list=new ArrayList<String>();
        level_list.add("X");
        level_list.add("XL");
        level_list.add("Pool");
        level_list.add("Black");
        level_list.add("Select");
        level_list.add("Lyft");
        level_list.add("LyftPlus");
        level_list.add("Line");
        level_list.add("Premier");
        //level_list.add("Other ");

        ArrayAdapter<String> ad=new ArrayAdapter<String>(this,R.layout.spinner_item,provider_list);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_provider.setAdapter(ad);

        setDefaultData();

        for (int i=0;i<level_list.size();i++){
            ReportModel reportModel=new ReportModel();
            reportModel.setIsSelected(false);

            String[] numbers=selected_checkbox.split(",");
            for (String number : numbers) {
                try {
                    int select = Integer.parseInt(number) - 1;
                    if (select == i) {

                        reportModel.setIsSelected(true);
                    }
                } catch (NumberFormatException ignored) {

                }
            }
            reportModel.setProvider_level(level_list.get(i));
            reportModel.setSno(String.valueOf(i+1));
            list_level.add(reportModel);
        }
        level_adapter=new ProviderLevelAdapter(list_level,context);
        rv_levels.setAdapter(level_adapter);

        rv_levels.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, convDpToPx(context,level_list.size()*30)));

        et_bio.addTextChangedListener(this);
        et_f_name.addTextChangedListener(this);
        et_l_name.addTextChangedListener(this);
        et_address.addTextChangedListener(this);
        et_city.addTextChangedListener(this);
        et_state.addTextChangedListener(this);
        et_zip.addTextChangedListener(this);
        et_dob.addTextChangedListener(this);
        et_car_model.addTextChangedListener(this);
        et_car_no.addTextChangedListener(this);
        et_car_color.addTextChangedListener(this);
        et_referral.addTextChangedListener(this);

        sp_provider.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position==provider_list.size()-1){
                    et_provider.setVisibility(View.VISIBLE);
                }
                else et_provider.setVisibility(View.GONE);

                selectedProvider=position+1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedProvider=1;

            }
        });

        cb_level_other.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) et_level.setVisibility(View.VISIBLE);
                else et_level.setVisibility(View.GONE);
            }
        });

        et_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDob();
                //openDatePicker();
            }
        });

        iv_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //selectImage();
                selectProfileDialog();
            }
        });

        bt_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateSignup() && validateProvider() && validateLevel()){
                    new UpdateProfile().execute(GlobalClass.callSavedPreferences("id",context),et_f_name.getText().toString().trim(),
                            et_l_name.getText().toString().trim(), et_bio.getText().toString().trim(), et_dob.getText().toString().trim(),
                            et_address.getText().toString().trim(), et_zip.getText().toString().trim(), et_city.getText().toString().trim(),
                            et_state.getText().toString().trim(), et_car_model.getText().toString().trim(),
                            et_car_no.getText().toString().trim(),et_car_color.getText().toString().trim(),encoded_image,et_referral.getText().toString().trim()
                    );
                }
            }
        });

        et_f_name.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                et_f_name.clearFocus();
                et_l_name.requestFocus();
                return true;
            }
        });

        et_l_name.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                et_l_name.clearFocus();
                et_bio.requestFocus();
                return true;
            }
        });

        et_bio.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                et_bio.clearFocus();
                et_address.requestFocus();
                return true;
            }
        });


        et_address.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                et_address.clearFocus();
                et_zip.requestFocus();
                return true;
            }
        });

        et_zip.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                et_zip.clearFocus();
                et_city.requestFocus();
                return true;
            }
        });

        et_city.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                et_city.clearFocus();
                et_state.requestFocus();
                return true;
            }
        });

        et_state.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                et_state.clearFocus();
                setDob();
                return true;
            }
        });

        et_car_model.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                et_car_model.clearFocus();
                et_car_no.requestFocus();
                return true;
            }
        });

        et_car_no.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                et_car_no.clearFocus();
                et_car_color.requestFocus();
                return true;
            }
        });

        et_car_color.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                et_car_color.clearFocus();
                et_referral.requestFocus();
                return true;
            }
        });

        et_referral.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                et_car_color.clearFocus();
                if (validateSignup() && validateProvider() && validateLevel()){
                    new UpdateProfile().execute(GlobalClass.callSavedPreferences("id",context),et_f_name.getText().toString().trim(),
                            et_l_name.getText().toString().trim(), et_bio.getText().toString().trim(), et_dob.getText().toString().trim(),
                            et_address.getText().toString().trim(), et_zip.getText().toString().trim(), et_city.getText().toString().trim(),
                            et_state.getText().toString().trim(), et_car_model.getText().toString().trim(),
                            et_car_no.getText().toString().trim(),et_car_color.getText().toString().trim(),encoded_image,et_referral.getText().toString().trim()
                    );
                }
                //et_car_color.requestFocus();
                return true;
            }
        });

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        input_bio.setError(null);
        input_f_name.setError(null);
        input_l_name.setError(null);
        input_address.setError(null);
        input_city.setError(null);
        input_state.setError(null);
        input_zip.setError(null);
        input_dob.setError(null);
        input_car_model.setError(null);
        input_car_no.setError(null);
        input_car_color.setError(null);
    }

    @Override
    public void afterTextChanged(Editable s) {
        input_bio.setError(null);
        input_f_name.setError(null);
        input_l_name.setError(null);
        input_address.setError(null);
        input_city.setError(null);
        input_state.setError(null);
        input_zip.setError(null);
        input_dob.setError(null);
        input_car_model.setError(null);
        input_car_no.setError(null);
        input_car_color.setError(null);

        if (et_zip.getText().hashCode()==s.hashCode()){
            if (et_zip.getText().toString().length()>=5) new GetCityState().execute(et_zip.getText().toString().trim());
        }

    }

    /*private void stopTimer(){
        if(mTimer1 != null){
            mTimer1.cancel();
            mTimer1.purge();
        }
    }

    private void startTimer(){
        mTimer1 = new Timer();
        mTt1 = new TimerTask() {
            public void run() {
                mTimerHandler.post(new Runnable() {
                    public void run(){
                        //TODO
                        //setAdmarvelAds();
                        setAdmobAds();
                    }
                });
            }
        };

        mTimer1.schedule(mTt1, 1, 30000);
    }*/

    private void setDefaultData(){
        otherProvider = GlobalClass.callSavedPreferences("rs_provider_othr",context);
        otherLevel    = GlobalClass.callSavedPreferences("provider_level_othr",context);

        et_f_name.setText(GlobalClass.callSavedPreferences("fname",context));
        et_l_name.setText(GlobalClass.callSavedPreferences("lname",context));
        et_dob.setText(GlobalClass.callSavedPreferences("dob",context));
        et_address.setText(GlobalClass.callSavedPreferences("address",context));
        et_zip.setText(GlobalClass.callSavedPreferences("zip_code",context));
        et_city.setText(GlobalClass.callSavedPreferences("city",context));
        et_state.setText(GlobalClass.callSavedPreferences("state",context));
        selected_checkbox=GlobalClass.callSavedPreferences("provider_level",context);

        et_bio.setText(GlobalClass.callSavedPreferences("short_bio",context));
        et_car_model.setText(GlobalClass.callSavedPreferences("car_model",context));
        et_car_no.setText(GlobalClass.callSavedPreferences("car_licence",context));
        et_car_color.setText(GlobalClass.callSavedPreferences("car_color",context));
        et_referral.setText(GlobalClass.callSavedPreferences("referral",context));

        String driver_rating = GlobalClass.callSavedPreferences("driver_rate",context);

        try {
            sp_provider.setSelection(Integer.parseInt(GlobalClass.callSavedPreferences("rs_provider",context))-1);
            selectedProvider = Integer.parseInt(GlobalClass.callSavedPreferences("rs_provider",context));
            ratingBar.setRating(Float.parseFloat(driver_rating));
        }catch (NumberFormatException e){

        }

        if (!otherLevel.equals("")){
            cb_level_other.setChecked(true);
            et_level.setText(otherLevel);
            et_level.setVisibility(View.VISIBLE);
        }

        et_provider.setText(GlobalClass.callSavedPreferences("rs_provider_othr",context));

        Picasso.with(context).load(GlobalClass.callSavedPreferences("profile_pic",context)).error(R.drawable.app_icon_new).placeholder(R.drawable.app_icon_new).resize(150,150).centerCrop().into(iv_user);

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

    public boolean validateSignup(){
        boolean isValid=false;
        if (et_f_name.getText().toString().trim().length()<=0){
            input_f_name.setError(getResources().getString(R.string.f_name)+" "+getResources().getString(R.string.can_not));
            isValid=false;
            et_f_name.requestFocus();
        }
        else if (et_l_name.getText().toString().trim().length()<=0){
            input_l_name.setError(getResources().getString(R.string.l_name)+" "+getResources().getString(R.string.can_not));
            isValid=false;
            et_l_name.requestFocus();
        }
        else if (et_bio.getText().toString().trim().length()<=0){
            input_bio.setError(getResources().getString(R.string.short_bio)+" "+getResources().getString(R.string.can_not));
            isValid=false;
            et_bio.requestFocus();
        }
        else if (et_address.getText().toString().trim().length()<=0){
            input_address.setError(getResources().getString(R.string.address)+" "+getResources().getString(R.string.can_not));
            et_address.requestFocus();
            isValid=false;
        }
        else if (et_city.getText().toString().trim().length()<=0){
            input_city.setError(getResources().getString(R.string.city)+" "+getResources().getString(R.string.can_not));
            et_city.requestFocus();
            isValid=false;
        }else if (et_state.getText().toString().trim().length()<=0){
            input_state.setError(getResources().getString(R.string.state)+" "+getResources().getString(R.string.can_not));
            et_state.requestFocus();
            isValid=false;
        }
        else if (et_zip.getText().toString().trim().length()<=0){
            input_zip.setError(getResources().getString(R.string.zip)+" "+getResources().getString(R.string.can_not));
            et_zip.requestFocus();
            isValid=false;
        }
        else if (et_dob.getText().toString().trim().length()<=0){
            input_dob.setError(getResources().getString(R.string.dob)+" "+getResources().getString(R.string.can_not));
            isValid=false;
        }
        else if (et_car_model.getText().toString().trim().length()<=0){
            input_car_model.setError(getResources().getString(R.string.car_model)+" "+getResources().getString(R.string.can_not));
            et_car_model.requestFocus();
            isValid=false;
        } else if (et_car_no.getText().toString().trim().length()<=0){
            input_car_no.setError(getResources().getString(R.string.car_number)+" "+getResources().getString(R.string.can_not));
            et_car_no.requestFocus();
            isValid=false;
        }
        else if (et_car_color.getText().toString().trim().length()<=0){
            input_car_color.setError(getResources().getString(R.string.car_color)+" "+getResources().getString(R.string.can_not));
            et_car_color.requestFocus();
            isValid=false;
        }
        else {
            isValid=true;
        }

        return isValid;
    }

    public boolean validateProvider(){
        boolean isValidProvider=false;
        if (selectedProvider != provider_list.size()){
            isValidProvider=true;
            otherProvider="";
        }
        else if (et_provider.getText().toString().trim().length() > 0){
            isValidProvider = true;
            otherProvider   = et_provider.getText().toString().trim();
        }
        else {
            isValidProvider=false;
            et_provider.setError("Enter provider name");
            otherProvider="";
        }

        return isValidProvider;
    }

    public boolean validateLevel(){
        boolean isValidLevel=false;
        if (cb_level_other.isChecked() && et_level.getText().toString().trim().length()>0) {
            isValidLevel = true;
            otherLevel = et_level.getText().toString().trim();
        }
        else if (cb_level_other.isChecked() && et_level.getText().toString().trim().length()<=0) {
            isValidLevel = false;
            otherLevel="";
            et_level.setError("Enter provider level");
        }
        else if (level_adapter!=null && level_adapter.getCheckedItems().size()>0) {
            isValidLevel = true;
            otherLevel="";
        }
        else {
            isValidLevel=false;
            otherLevel="";
            Toast.makeText(context, "Please select al least one level", Toast.LENGTH_SHORT).show();
        }

        return isValidLevel;
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Gallery",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.MyDialogTheme);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                // boolean result=Utility.checkPermission(MainActivity.this);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    cameraIntent();

                } else if (items[item].equals("Choose from Gallery")) {
                    userChoosenTask = "Choose from Gallery";
                    galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void selectProfileDialog(){
        final Dialog dialog;
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.select_photo_dialog);
        TextView tv_gellery,tv_camera;
        ImageView iv_cancel;

        tv_gellery  = (TextView) dialog.findViewById(R.id.tv_gallery);
        tv_camera   = (TextView) dialog.findViewById(R.id.tv_camera);
        iv_cancel   = (ImageView) dialog.findViewById(R.id.iv_cancel);

        tv_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userChoosenTask = "Take Photo";
                dialog.dismiss();
                cameraIntent();

            }
        });
        tv_gellery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userChoosenTask = "Choose from Gallery";
                dialog.dismiss();
                galleryIntent();
            }
        });
        iv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
    }

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        encoded_image = getStringImage(thumbnail);
        iv_user.setImageBitmap(thumbnail);

    }
    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(context.getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // filePath = data.getData();
        //GlobalClass.savePreferences("profile_img", String.valueOf(getPath(context,data.getData())),context);
        // encoded_image = getStringImage(Bitmap.createScaledBitmap(bm,(int)(bm.getWidth()*0.9), (int)(bm.getHeight()*0.9), true));
        encoded_image = getStringImage(bm);
        iv_user.setImageBitmap(bm);
    }
    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 70, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public void setDob(){
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();

            }
        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(ProfileActivity.this,R.style.Picker, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate((System.currentTimeMillis()));
        datePickerDialog.show();
    }
    private void updateLabel() {
        String myFormat = "MM/dd/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        et_dob.setText(sdf.format(myCalendar.getTime()));
        et_dob.clearFocus();
        et_car_model.requestFocus();
    }

    private class GetCityState extends AsyncTask<String, String, String> {
        String result = "";
        Dialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog=loadingDialog(ProfileActivity.this);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            String url = Webservice_Url + GetCityState;
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);

            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("zip_code", params[0]));
                //nameValuePairs.add(new BasicNameValuePair("Country", "country name"));
                /*StringEntity se = new StringEntity(nameValuePairs);
                se.setContentType("application/json;charset=UTF-8");
                se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json;charset=UTF-8"));*/
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);

                result = EntityUtils.toString(response.getEntity());

                Log.e("result city state", result);

            } catch (ClientProtocolException e) {

            } catch (IOException e) {

            }

            return result;
        }

        @Override
        protected void onPostExecute(String responseStr) {
            super.onPostExecute(responseStr);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            if (responseStr.length() > 0) {
                try {
                    JSONObject city_object=new JSONObject(responseStr);
                    if (city_object.getBoolean("status")) {
                        JSONArray cityArray = city_object.getJSONArray("data");
                        for (int i = 0; i < cityArray.length(); i++) {
                            JSONObject city_data = cityArray.getJSONObject(i);
                            et_state.setText(city_data.getString("zip_state"));
                            et_city.setText(city_data.getString("zip_city"));
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //  Log.e("Result",responseStr);

            }
        }
    }

    private class ProviderLevelAdapter extends RecyclerView.Adapter<ProviderLevelAdapter.ViewHolder>{

        private Context context;
        private List<ReportModel> reportModelList;
        private SparseBooleanArray mSparseBooleanArray;

        public ProviderLevelAdapter(List<ReportModel> reportModelList1,Context context) {
            reportModelList = new ArrayList<ReportModel>();
            this.reportModelList = reportModelList1;
            mSparseBooleanArray = new SparseBooleanArray();
            this.context    = context;
            //AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        }

        public class ViewHolder extends RecyclerView.ViewHolder{

            private CheckBox checkBox;
            LinearLayout ll_main;

            private ViewHolder(View v){

                super(v);
                checkBox  = (CheckBox) v.findViewById(R.id.checkBox1);
                ll_main   = (LinearLayout) v.findViewById(R.id.ll_items);
            }
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        CompoundButton.OnCheckedChangeListener mCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mSparseBooleanArray.put((Integer) buttonView.getTag(), isChecked);
            }
        };


        @Override
        public ProviderLevelAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

            View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.provider_level_items,parent,false);

            ProviderLevelAdapter.ViewHolder viewHolder1 = new ProviderLevelAdapter.ViewHolder(view1);

            return viewHolder1;
        }


        @Override
        public void onBindViewHolder(final ProviderLevelAdapter.ViewHolder holder, int position){

            final ReportModel reportModel_obj = reportModelList.get(position);

            String imageUrl = reportModel_obj.getImage_url();

            holder.checkBox.setText(reportModel_obj.getProvider_level());
            holder.checkBox.setTag(position);
            holder.checkBox.setChecked(mSparseBooleanArray.get(position));
            holder.checkBox.setOnCheckedChangeListener(mCheckedChangeListener);
            holder.checkBox.setChecked(reportModel_obj.getIsSelected());

        }
        public ArrayList<ReportModel> getCheckedItems() {
            ArrayList<ReportModel> mTempArry = new ArrayList<ReportModel>();

            for(int i=0;i<reportModelList.size();i++) {
                if(mSparseBooleanArray.get(i)) {
                    mTempArry.add(reportModelList.get(i));
                }
            }
            return mTempArry;
        }

        @Override
        public int getItemCount(){

            return reportModelList.size();
        }

    }

    private class UpdateProfile extends AsyncTask<String, String, String> {
        String result = "";
        Dialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog=loadingDialog(ProfileActivity.this);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            String url = Webservice_Url + UpdateProfile;
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);

            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("driver_id",params[0]));
                nameValuePairs.add(new BasicNameValuePair("fname",params[1]));
                nameValuePairs.add(new BasicNameValuePair("lname",params[2]));
                nameValuePairs.add(new BasicNameValuePair("short_bio",params[3]));
                nameValuePairs.add(new BasicNameValuePair("dob",params[4]));
                nameValuePairs.add(new BasicNameValuePair("address",params[5]));
                nameValuePairs.add(new BasicNameValuePair("zip_code",params[6]));
                nameValuePairs.add(new BasicNameValuePair("city",params[7]));
                nameValuePairs.add(new BasicNameValuePair("state",params[8]));

                nameValuePairs.add(new BasicNameValuePair("car_model",params[9]));
                nameValuePairs.add(new BasicNameValuePair("car_licence",params[10]));
                nameValuePairs.add(new BasicNameValuePair("car_color",params[11]));

                nameValuePairs.add(new BasicNameValuePair("profile_pic",params[12]));
                nameValuePairs.add(new BasicNameValuePair("referral",params[13]));

                nameValuePairs.add(new BasicNameValuePair("rs_provider",String.valueOf(selectedProvider)));
                nameValuePairs.add(new BasicNameValuePair("rs_provider_othr",otherProvider));

                //ArrayList<ReportModel> arrayList = level_adapter.getCheckedItems();
                for (int i=0; i<level_adapter.getCheckedItems().size();i++){
                    ReportModel report=level_adapter.getCheckedItems().get(i);
                    nameValuePairs.add(new BasicNameValuePair("provider_level[]",report.getSno()));
                }
                if (!otherLevel.equals("")){
                    nameValuePairs.add(new BasicNameValuePair("provider_level[]",String.valueOf(level_list.size()+1)));
                }
                nameValuePairs.add(new BasicNameValuePair("provider_level_othr",otherLevel));

               // Log.e("Name value of update", String.valueOf(nameValuePairs));


                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);

                result = EntityUtils.toString(response.getEntity());

               // Log.e("result update", result);

            } catch (IOException ignored) {

            }

            return result;
        }

        @Override
        protected void onPostExecute(String responseStr) {
            super.onPostExecute(responseStr);
            if (dialog.isShowing()) dialog.dismiss();

            if (responseStr.length() > 0) {
                try {
                    JSONObject register_object=new JSONObject(responseStr);
                    if (register_object.getBoolean("status")){
                        JSONArray data_array = register_object.getJSONArray("data");
                        if (data_array.length()>0) {
                            JSONObject driver_data = data_array.getJSONObject(0);
                            GlobalClass.savePreferences("id", driver_data.getString("id"), context);
                            GlobalClass.savePreferences("fname", driver_data.getString("fname"), context);
                            GlobalClass.savePreferences("lname", driver_data.getString("lname"), context);
                            GlobalClass.savePreferences("email", driver_data.getString("email"), context);
                            GlobalClass.savePreferences("dob", driver_data.getString("dob"), context);
                            GlobalClass.savePreferences("dob", driver_data.getString("dob"), context);
                            GlobalClass.savePreferences("address", driver_data.getString("address"), context);
                            GlobalClass.savePreferences("zip_code", driver_data.getString("zip_code"), context);
                            GlobalClass.savePreferences("city", driver_data.getString("city"), context);
                            GlobalClass.savePreferences("state", driver_data.getString("state"), context);
                            GlobalClass.savePreferences("rs_provider", driver_data.getString("rs_provider"), context);
                            GlobalClass.savePreferences("rs_provider_othr", driver_data.getString("rs_provider_othr"), context);
                            GlobalClass.savePreferences("provider_level", driver_data.getString("provider_level"), context);
                            GlobalClass.savePreferences("provider_level_othr", driver_data.getString("provider_level_othr"), context);
                            GlobalClass.savePreferences("referral", driver_data.getString("referral"), context);
                            GlobalClass.savePreferences("driver_rate", driver_data.getString("driver_rate"), context);

                            GlobalClass.savePreferences("profile_pic", ProfilePic+driver_data.getString("profile_pic"), context);

                           // Log.e("profile pic",ProfilePic+driver_data.getString("profile_pic"));

                            GlobalClass.savePreferences("short_bio", driver_data.getString("short_bio"), context);
                            GlobalClass.savePreferences("car_model", driver_data.getString("car_model"), context);
                            GlobalClass.savePreferences("car_licence", driver_data.getString("car_licence"), context);
                            GlobalClass.savePreferences("car_color", driver_data.getString("car_color"), context);

                            startActivity(new Intent(context,HomeScreenActivity.class));
                            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                            finish();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //  Log.e("Result",responseStr);
            }
        }
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
                Log.e("adloaded","adloaded");
            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
                Log.e("onAdClosed","onAdClosed");
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                Log.e("onAdFailedToLoad","onAdFailedToLoad");
            }

            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
                Log.e("onAdLeftApplication","onAdLeftApplication");
            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();
                Log.e("onAdClicked","onAdClicked");
            }

            @Override
            public void onAdImpression() {
                super.onAdImpression();
                Log.e("onAdImpression","onAdImpression");
            }
        });
    }
    /*public void setAdmarvelAds(){
        try {
            Map<AdMarvelUtils.SDKAdNetwork, String> publisherIds = new HashMap<AdMarvelUtils.SDKAdNetwork, String>();
            publisherIds.put(null, null);
            AdMarvelUtils.initialize(ProfileActivity.this, publisherIds);

            Map<String, Object> targetParams = new HashMap<String, Object>();
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
       /* try {
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
        super.onBackPressed();
        startActivity(new Intent(context,HomeScreenActivity.class));
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
        finish();
    }
}
