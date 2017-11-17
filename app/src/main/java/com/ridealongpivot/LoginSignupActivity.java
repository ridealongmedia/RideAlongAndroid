package com.ridealongpivot;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.StrictMode;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
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
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.admarvel.android.ads.AdMarvelUtils;
import com.admarvel.android.ads.AdMarvelView;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

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
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static com.ridealongpivot.GlobalClass.AdMarvelBannerPartnerId;
import static com.ridealongpivot.GlobalClass.AdMarvelBannerSiteId;
import static com.ridealongpivot.GlobalClass.ForgotPassword;
import static com.ridealongpivot.GlobalClass.GetCityState;
import static com.ridealongpivot.GlobalClass.LoginDriver;
import static com.ridealongpivot.GlobalClass.LoginDriverFB_GL;
import static com.ridealongpivot.GlobalClass.ProfilePic;
import static com.ridealongpivot.GlobalClass.RegisterDriver;
import static com.ridealongpivot.GlobalClass.UpdatePassword;
import static com.ridealongpivot.GlobalClass.Webservice_Url;
import static com.ridealongpivot.GlobalClass.alertDialog;
import static com.ridealongpivot.GlobalClass.convDpToPx;
import static com.ridealongpivot.GlobalClass.loadingDialog;

public class LoginSignupActivity extends AppCompatActivity implements TextWatcher, GoogleApiClient.OnConnectionFailedListener {

    Context context;
    LinearLayout ll_login,ll_signup;
    TextView tv_signup,tv_already;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    //Login
    TextInputLayout input_email_login,input_pass_login;
    TextInputEditText et_email_login,et_pass_login;

    Button bt_login;
    TextView tv_forgot;

    LinearLayout ll_google;
    SignInButton signInButton;

    //Signup
    TextInputLayout input_f_name,input_l_name,input_email_sign,input_address,input_city,input_state,input_zip,input_dob,input_pass1,input_pass2,input_referral;
    TextInputEditText et_f_name,et_l_name,et_email_sign,et_address,et_city,et_state,et_zip,et_dob,et_pass1,et_pass2,et_referral;

    Spinner sp_provider,sp_level;
    EditText et_provider,et_level;

    CheckBox cb_term;
    CheckBox cb_level_other;

    RecyclerView rv_levels;

    Button bt_signup;
    ProviderLevelAdapter level_adapter;
    ArrayList<ReportModel> list_level;

    private GoogleSignInOptions gso;
    private GoogleApiClient mGoogleApiClient;
    private int RC_SIGN_IN = 100;

    CallbackManager callbackManager;
    LoginButton loginButton;
    private List<String> PERMISSIONSREAD = Arrays.asList("public_profile", "email");

    RelativeLayout rl_main;
    Calendar myCalendar = Calendar.getInstance();
    ArrayList<String> provider_list;
    ArrayList<String> level_list;
    int selectedProvider=0;
    String otherProvider="",otherLevel="";
    String forgot_email="";
    Dialog newPassDialog;
    //AdMarvelView adMarvelView;

    private static final int TIME_DELAY = 2000;
    private static long back_pressed;
    private Timer mTimer1;
    private TimerTask mTt1;
    private Handler mTimerHandler = new Handler();
    AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_signup);

        context             = this;
        rl_main             = (RelativeLayout) findViewById(R.id.rl_main);
        ll_login            = (LinearLayout) findViewById(R.id.ll_login);
        ll_signup           = (LinearLayout) findViewById(R.id.ll_signup);

    //Login
        input_email_login   = (TextInputLayout) findViewById(R.id.input_email_login);
        input_pass_login    = (TextInputLayout) findViewById(R.id.input_pass_login);

        et_email_login      = (TextInputEditText) findViewById(R.id.et_email_login);
        et_pass_login       = (TextInputEditText) findViewById(R.id.et_pass_login);

        bt_login            = (Button) findViewById(R.id.bt_login);
        tv_forgot           = (TextView) findViewById(R.id.tv_forgot);

        ll_google           = (LinearLayout) findViewById(R.id.ll_google_login);
        signInButton        = (SignInButton) findViewById(R.id.sign_in_button);
        loginButton         = (LoginButton) findViewById(R.id.login_button);

    //Signup
        input_f_name        = (TextInputLayout) findViewById(R.id.input_f_name);
        input_l_name        = (TextInputLayout) findViewById(R.id.input_l_name);
        input_email_sign    = (TextInputLayout) findViewById(R.id.input_email_sign);
        input_address       = (TextInputLayout) findViewById(R.id.input_address);
        input_city          = (TextInputLayout) findViewById(R.id.input_city);
        input_state         = (TextInputLayout) findViewById(R.id.input_state);
        input_zip           = (TextInputLayout) findViewById(R.id.input_zip);
        input_dob           = (TextInputLayout) findViewById(R.id.input_dob);
        input_pass1         = (TextInputLayout) findViewById(R.id.input_pass1_sign);
        input_pass2         = (TextInputLayout) findViewById(R.id.input_pass2_sign);
        input_referral      = (TextInputLayout) findViewById(R.id.input_referral);

        et_f_name           = (TextInputEditText) findViewById(R.id.et_f_name);
        et_l_name           = (TextInputEditText) findViewById(R.id.et_l_name);
        et_email_sign       = (TextInputEditText) findViewById(R.id.et_email_sign);
        et_address          = (TextInputEditText) findViewById(R.id.et_address);
        et_city             = (TextInputEditText) findViewById(R.id.et_city);
        et_state            = (TextInputEditText) findViewById(R.id.et_state);
        et_zip              = (TextInputEditText) findViewById(R.id.et_zip);
        et_dob              = (TextInputEditText) findViewById(R.id.et_dob);
        et_pass1            = (TextInputEditText) findViewById(R.id.et_pass1_sign);
        et_pass2            = (TextInputEditText) findViewById(R.id.et_pass2_sign);
        et_referral         = (TextInputEditText) findViewById(R.id.et_referral);

        sp_provider         = (Spinner) findViewById(R.id.sp_ride_provider);
        sp_level            = (Spinner) findViewById(R.id.sp_level);

        rv_levels           = (RecyclerView) findViewById(R.id.rv_level);

        et_provider         = (EditText) findViewById(R.id.et_ride_provider);
        et_level            = (EditText) findViewById(R.id.et_level);

        cb_term             = (CheckBox) findViewById(R.id.cb_term);
        cb_level_other      = (CheckBox) findViewById(R.id.cb_level_other);
        bt_signup           = (Button) findViewById(R.id.bt_signup);

        tv_already          = (TextView) findViewById(R.id.tv_already);
        tv_signup           = (TextView) findViewById(R.id.tv_signup);

        //adMarvelView        = (AdMarvelView) findViewById(R.id.ad);
        adView              = (AdView) findViewById(R.id.adView);

        list_level          = new ArrayList<ReportModel>();

        sp_provider.getBackground().setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_ATOP);

        if (isNavigationBarAvailable()) rl_main.setPadding(0,0,0,navBarHeight());

        et_email_login.addTextChangedListener(this);
        et_pass_login.addTextChangedListener(this);
        et_f_name.addTextChangedListener(this);
        et_l_name.addTextChangedListener(this);
        et_email_sign.addTextChangedListener(this);
        et_address.addTextChangedListener(this);
        et_city.addTextChangedListener(this);
        et_state.addTextChangedListener(this);
        et_zip.addTextChangedListener(this);
        et_dob.addTextChangedListener(this);
        et_pass1.addTextChangedListener(this);
        et_pass2.addTextChangedListener(this);
        et_referral.addTextChangedListener(this);

        //setAdmarvelAds();
        setAdmobAds();

        setSpinnerProviderItems();

        try {
            signInButton.setSize(SignInButton.SIZE_STANDARD);
            gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this, this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();

            callbackManager = CallbackManager.Factory.create();

            loginButton.setReadPermissions(PERMISSIONSREAD);
        }catch (Exception ignore){}

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
               // requestUserProfile(loginResult);
                RequestData();
                LoginManager.getInstance().logOut();

            }

            @Override
            public void onCancel() {
                Toast.makeText(getBaseContext(),"Login Cancelled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException e) {
                Toast.makeText(getBaseContext(),"Problem connecting to Facebook", Toast.LENGTH_SHORT).show();
            }
        });


        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });


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

        et_email_login.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                et_email_login.clearFocus();
                et_pass_login.requestFocus();
                return true;
            }
        });

        et_pass_login.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                et_pass_login.clearFocus();
                if (et_email_login.getText().toString().trim().length()<=0){
                    input_email_login.setError(getResources().getString(R.string.valid_email));
                    et_email_login.requestFocus();
                }
                else if (et_pass_login.getText().toString().trim().length()<=0){
                    input_pass_login.setError(getResources().getString(R.string.password)+" "+getResources().getString(R.string.can_not));
                    et_pass_login.requestFocus();
                }
                else {
                    new LoginDriver().execute(et_email_login.getText().toString().trim(),et_pass_login.getText().toString().trim());
                }

                return true;
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
                et_email_sign.requestFocus();
                return true;
            }
        });

        et_email_sign.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                et_email_sign.clearFocus();
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

        et_pass1.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                et_pass1.clearFocus();
                et_pass2.requestFocus();
                return true;
            }
        });

        et_pass2.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                et_pass2.clearFocus();
                et_referral.requestFocus();
                return true;
            }
        });

        et_referral.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                et_referral.clearFocus();
                if (validateSignup() && cb_term.isChecked() && validateProvider() && validateLevel()){
                    new RegisterDriverTask().execute(et_f_name.getText().toString().trim(), et_l_name.getText().toString().trim(),
                            et_email_sign.getText().toString().trim(), et_dob.getText().toString().trim(), et_address.getText().toString().trim(),
                            et_zip.getText().toString().trim(), et_city.getText().toString().trim(), et_state.getText().toString().trim(),
                            et_pass1.getText().toString().trim(), et_referral.getText().toString().trim()
                    );
                }

                return true;
            }
        });

        et_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDob();
                //openDatePicker();
            }
        });

        bt_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateSignup() && cb_term.isChecked() && validateProvider() && validateLevel()){
                    new RegisterDriverTask().execute(et_f_name.getText().toString().trim(), et_l_name.getText().toString().trim(),
                            et_email_sign.getText().toString().trim(), et_dob.getText().toString().trim(), et_address.getText().toString().trim(),
                            et_zip.getText().toString().trim(), et_city.getText().toString().trim(), et_state.getText().toString().trim(),
                            et_pass1.getText().toString().trim(), et_referral.getText().toString().trim()
                    );
                }

            }
        });

        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_email_login.getText().toString().trim().length()<=0){
                    input_email_login.setError(getResources().getString(R.string.valid_email));
                    et_email_login.requestFocus();
                }
                else if (et_pass_login.getText().toString().trim().length()<=0){
                    input_pass_login.setError(getResources().getString(R.string.password)+" "+getResources().getString(R.string.can_not));
                    et_pass_login.requestFocus();
                }
                else {
                    new LoginDriver().execute(et_email_login.getText().toString().trim(),et_pass_login.getText().toString().trim());
                }

            }
        });

        et_pass_login.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    if(event.getRawX() >= (et_pass_login.getRight() - et_pass_login.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        et_pass_login.setInputType(InputType.TYPE_CLASS_TEXT);

                        return true;
                    }
                }
                else if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (et_pass_login.getRight() - et_pass_login.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        et_pass_login.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        return true;
                    }
                }
                return false;
            }
        });

        tv_already.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_login.setVisibility(View.VISIBLE);
                ll_signup.setVisibility(View.GONE);
            }
        });

        tv_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_login.setVisibility(View.GONE);
                ll_signup.setVisibility(View.VISIBLE);
            }
        });

        tv_forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotPasswordDialog();
            }
        });
    }

    public void setSpinnerProviderItems(){
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

        for (int i=0;i<level_list.size();i++){
            ReportModel reportModel=new ReportModel();
            reportModel.setProvider_level(level_list.get(i));
            reportModel.setSno(String.valueOf(i+1));
            list_level.add(reportModel);
        }
        level_adapter=new ProviderLevelAdapter(list_level,context);
        rv_levels.setAdapter(level_adapter);

        rv_levels.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, convDpToPx(context,level_list.size()*30)));

        ArrayAdapter<String> ad=new ArrayAdapter<String>(this,R.layout.spinner_item,provider_list);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_provider.setAdapter(ad);
    }

    public void RequestData(){
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object,GraphResponse response) {

                JSONObject json = response.getJSONObject();
                try {
                    if(json != null){
                        String f_name="";
                        String l_name="";
                        String profileImg="";
                        String user_email="";

                        if (json.getString("name")!=null){
                            String[] name = json.getString("name").split(" ");
                            if (name.length >= 2) {
                                f_name=name[0];
                                l_name=name[1];
                            }
                            else f_name=json.getString("name");
                        }
                        if (json.getString("email")!=null) user_email=json.getString("email");

                        if (json.has("picture")) {
                            String profilePicUrl = json.getJSONObject("picture").getJSONObject("data").getString("url");

                            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                            StrictMode.setThreadPolicy(policy);

                            Bitmap profilePic= BitmapFactory.decodeStream(new URL(profilePicUrl).openConnection().getInputStream());
                            //mImageView.setBitmap(profilePic);
                            profileImg=getStringImage(profilePic);
                            //Log.e("Facebook pic base64",getStringImage(profilePic));
                        }
                        new LoginWithFBGoogle().execute(f_name,l_name,user_email,profileImg);
                    }

                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link,email,picture.type(large)");
        request.setParameters(parameters);
        request.executeAsync();
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 70, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Adding the login result back to the button
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            //Calling a new function to handle signin
            handleSignInResult(result);
        }
        else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }

    }


    private void handleSignInResult(GoogleSignInResult result) {
        //If the login succeed
        if (result.isSuccess()) {
            //Getting google account
            GoogleSignInAccount acct = result.getSignInAccount();
            String f_name="";
            String l_name="";
            String profileImg="";
            String user_email="";

            if (acct!=null) {
                if (acct.getEmail()!=null) user_email=acct.getEmail();
                String[] name = acct.getDisplayName().split(" ");
                if (name.length >= 2) {
                    f_name=name[0];
                    l_name=name[1];
                }
                else f_name=acct.getDisplayName();

                if (acct.getPhotoUrl() != null) {
                    try {
                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                        StrictMode.setThreadPolicy(policy);
                        Bitmap profilePic = BitmapFactory.decodeStream(new URL(acct.getPhotoUrl().toString()).openConnection().getInputStream());
                        //Log.e("base64 google image", getStringImage(profilePic));
                        profileImg = getStringImage(profilePic);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                new LoginWithFBGoogle().execute(f_name,l_name,user_email,profileImg);
            }

        } else {
            //If login fails
            Toast.makeText(this, "Login Failed", Toast.LENGTH_LONG).show();
        }
    }


    private void signIn() {
        //Creating an intent
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);

        //Starting intent for result
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

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
        else if (et_email_sign.getText().toString().trim().length()<=0){
            input_email_sign.setError(getResources().getString(R.string.email_address)+" "+getResources().getString(R.string.can_not));
            isValid=false;
            et_email_sign.requestFocus();
        }
        /*else if (!et_email_sign.getText().toString().trim().matches(emailPattern)) {
            input_email_sign.setError(getResources().getString(R.string.valid_email));
            isValid = false;
            et_email_sign.requestFocus();
        }*/
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
        else if (et_pass1.getText().toString().trim().length()<=0){
            input_pass1.setError(getResources().getString(R.string.password)+" "+getResources().getString(R.string.can_not));
            et_pass1.requestFocus();
            isValid=false;
        }else if (!et_pass2.getText().toString().trim().equals(et_pass1.getText().toString().trim())){
            input_pass2.setError(getResources().getString(R.string.same_pass));
            et_pass2.requestFocus();
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
        DatePickerDialog datePickerDialog = new DatePickerDialog(LoginSignupActivity.this,R.style.Picker, date, myCalendar
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
        et_pass1.requestFocus();
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

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        input_email_login.setError(null);
        input_pass_login.setError(null);
        input_f_name.setError(null);
        input_l_name.setError(null);
        input_email_sign.setError(null);
        input_address.setError(null);
        input_city.setError(null);
        input_state.setError(null);
        input_zip.setError(null);
        input_dob.setError(null);
        input_pass1.setError(null);
        input_pass2.setError(null);
        input_referral.setError(null);
        /*if (et_zip.getText().hashCode()==s.hashCode()){
            if (et_zip.getText().toString().length()>=5) new GetCityState().execute(et_zip.getText().toString().trim());
        }*/
    }

    @Override
    public void afterTextChanged(Editable s) {
        input_email_login.setError(null);
        input_pass_login.setError(null);
        input_f_name.setError(null);
        input_l_name.setError(null);
        input_email_sign.setError(null);
        input_address.setError(null);
        input_city.setError(null);
        input_state.setError(null);
        input_zip.setError(null);
        input_dob.setError(null);
        input_pass1.setError(null);
        input_pass2.setError(null);
        input_referral.setError(null);

        if (et_zip.getText().hashCode()==s.hashCode()){
            if (et_zip.getText().toString().length()>=5) new GetCityState().execute(et_zip.getText().toString().trim());
        }
    }

    private void forgotPasswordDialog(){
        final Dialog dialog;
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.forgot_password_dialog);
        dialog.setCancelable(false);
        final TextInputLayout input_email;
        final EditText et_name,et_email,et_phone;
        Button bt_submit;
        ImageView iv_cancel;

        input_email  = (TextInputLayout) dialog.findViewById(R.id.input_email);

        et_email     = (EditText) dialog.findViewById(R.id.et_email);

        bt_submit    = (Button) dialog.findViewById(R.id.bt_submit);

        iv_cancel    = (ImageView) dialog.findViewById(R.id.iv_cancel);

        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_email.getText().toString().trim().length()<=0 ){
                    input_email.setError(getResources().getString(R.string.valid_email));
                }
                else {
                    resetPasswordDialog();
                    forgot_email = et_email.getText().toString().trim();
                    new ForgotPassword().execute(forgot_email);
                    dialog.dismiss();
                }

            }
        });

        et_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                input_email.setError(null);

            }

            @Override
            public void afterTextChanged(Editable s) {
                input_email.setError(null);
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

    private void resetPasswordDialog(){
        //final Dialog dialog;
        newPassDialog = new Dialog(context);
        newPassDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        newPassDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        newPassDialog.setContentView(R.layout.new_password_dialog);
        newPassDialog.setCancelable(false);
        final TextInputLayout input_otp,input_pass1,input_pass2;
        final EditText et_otp,et_pass1,et_pass2;
        Button bt_resend,bt_update;
        ImageView iv_cancel;

        input_otp    = (TextInputLayout) newPassDialog.findViewById(R.id.input_otp);
        input_pass1  = (TextInputLayout) newPassDialog.findViewById(R.id.input_pass1);
        input_pass2  = (TextInputLayout) newPassDialog.findViewById(R.id.input_pass2);

        et_otp       = (EditText) newPassDialog.findViewById(R.id.et_otp);
        et_pass1     = (EditText) newPassDialog.findViewById(R.id.et_pass1);
        et_pass2     = (EditText) newPassDialog.findViewById(R.id.et_pass2);

        bt_resend    = (Button) newPassDialog.findViewById(R.id.bt_resend);
        bt_update    = (Button) newPassDialog.findViewById(R.id.bt_update);

        iv_cancel   = (ImageView) newPassDialog.findViewById(R.id.iv_cancel);

        bt_resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ForgotPassword().execute(forgot_email);
            }
        });

        bt_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_otp.getText().toString().trim().length()<=0){
                    input_otp.setError(getResources().getString(R.string.otp)+" "+getResources().getString(R.string.can_not));
                } else if (et_pass1.getText().toString().trim().length()<=0){
                    input_pass1.setError(getResources().getString(R.string.password)+" "+getResources().getString(R.string.can_not));
                } else if (!et_pass2.getText().toString().trim().equals(et_pass1.getText().toString().trim())){
                    input_pass2.setError(getResources().getString(R.string.same_pass));
                }
                else {
                    new UpdatePassword().execute(forgot_email,et_otp.getText().toString().trim(),et_pass1.getText().toString().trim());
                    //newPassDialog.dismiss();
                }

            }
        });

        et_otp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                input_otp.setError(null);
                input_pass1.setError(null);
                input_pass2.setError(null);

            }

            @Override
            public void afterTextChanged(Editable s) {
                input_otp.setError(null);
                input_pass1.setError(null);
                input_pass2.setError(null);
            }
        });

        et_pass1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                input_otp.setError(null);
                input_pass1.setError(null);
                input_pass2.setError(null);

            }

            @Override
            public void afterTextChanged(Editable s) {
                input_otp.setError(null);
                input_pass1.setError(null);
                input_pass2.setError(null);
            }
        });

        et_pass2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                input_otp.setError(null);
                input_pass1.setError(null);
                input_pass2.setError(null);

            }

            @Override
            public void afterTextChanged(Editable s) {
                input_otp.setError(null);
                input_pass1.setError(null);
                input_pass2.setError(null);
            }
        });


        iv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newPassDialog.dismiss();
            }
        });
        newPassDialog.show();
    }

    private class GetCityState extends AsyncTask<String, String, String> {
        String result = "";
        Dialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog=loadingDialog(LoginSignupActivity.this);
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
            if (dialog.isShowing()) dialog.dismiss();

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

    private class RegisterDriverTask extends AsyncTask<String, String, String> {
        String result = "";
        Dialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog=loadingDialog(LoginSignupActivity.this);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            String url = Webservice_Url + RegisterDriver;
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);

            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("fname",params[0]));
                nameValuePairs.add(new BasicNameValuePair("lname",params[1]));
                nameValuePairs.add(new BasicNameValuePair("email",params[2]));
                nameValuePairs.add(new BasicNameValuePair("dob",params[3]));
                nameValuePairs.add(new BasicNameValuePair("address",params[4]));
                nameValuePairs.add(new BasicNameValuePair("zip_code",params[5]));
                nameValuePairs.add(new BasicNameValuePair("city",params[6]));
                nameValuePairs.add(new BasicNameValuePair("state",params[7]));
                nameValuePairs.add(new BasicNameValuePair("password",params[8]));
                nameValuePairs.add(new BasicNameValuePair("confirm_password",params[8]));
                nameValuePairs.add(new BasicNameValuePair("referral",params[9]));

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

                nameValuePairs.add(new BasicNameValuePair("terms","1"));

                Log.e("Name value of register", String.valueOf(nameValuePairs));


                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);

                result = EntityUtils.toString(response.getEntity());

                Log.e("result register", result);

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
                        ll_login.setVisibility(View.VISIBLE);
                        ll_signup.setVisibility(View.GONE);
                        alertDialog(context,register_object.getString("message"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //  Log.e("Result",responseStr);
            }
        }
    }

    private class LoginDriver extends AsyncTask<String, String, String> {
        String result = "";
        Dialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog=loadingDialog(LoginSignupActivity.this);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            String url = Webservice_Url + LoginDriver;
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);

            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("email",params[0]));
                nameValuePairs.add(new BasicNameValuePair("password",params[1]));

                //Log.e("Name value of login", String.valueOf(nameValuePairs));

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);

                result = EntityUtils.toString(response.getEntity());


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
                    JSONObject login_object=new JSONObject(responseStr);
                    Log.e("result login", responseStr);
                    if (login_object.getBoolean("status")){
                        GlobalClass.savePreferences("activity_id", login_object.getString("activity_id"), context);
                        JSONArray data_array = login_object.getJSONArray("data");
                        for (int i=0; i<data_array.length();i++) {
                            JSONObject driver_data = data_array.getJSONObject(i);
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
                            GlobalClass.savePreferences("short_bio", driver_data.getString("short_bio"), context);
                            GlobalClass.savePreferences("car_model", driver_data.getString("car_model"), context);
                            GlobalClass.savePreferences("car_licence", driver_data.getString("car_licence"), context);
                            GlobalClass.savePreferences("car_color", driver_data.getString("car_color"), context);

                            startActivity(new Intent(context, ProfileActivity.class));
                            finish();
                            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                            break;
                            // String driver_id=driver_data.getString("driverid");
                        }
                    }else {
                        alertDialog(context,login_object.getString("message"));
                       /* View view1 = findViewById(android.R.id.content);
                        Snackbar snackbar = Snackbar.make(view1,login_object.getString("message"), Snackbar.LENGTH_SHORT);
                        snackbar.show();
                        View view=snackbar.getView();
                        view.setBackgroundColor(getResources().getColor(R.color.logo_color));*/
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //  Log.e("Result",responseStr);

            }
        }
    }

    private class LoginWithFBGoogle extends AsyncTask<String, String, String> {
        String result = "";
        Dialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog=loadingDialog(LoginSignupActivity.this);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = Webservice_Url + LoginDriverFB_GL;
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);

            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("fname",params[0]));
                nameValuePairs.add(new BasicNameValuePair("lname",params[1]));
                nameValuePairs.add(new BasicNameValuePair("email",params[2]));
                nameValuePairs.add(new BasicNameValuePair("profile_pic",params[3]));

                //Log.e("login fb google", String.valueOf(nameValuePairs));


                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);

                result = EntityUtils.toString(response.getEntity());

                Log.e("result login fb google", result);

            } catch (ClientProtocolException e) {

            } catch (IOException e) {

            }

            return result;
        }

        @Override
        protected void onPostExecute(String responseStr) {
            super.onPostExecute(responseStr);
            if (dialog.isShowing()) dialog.dismiss();

            if (responseStr.length() > 0) {
                try {
                    JSONObject login_object=new JSONObject(responseStr);
                    if (login_object.getBoolean("status")){
                        GlobalClass.savePreferences("activity_id", login_object.getString("activity_id"), context);
                        JSONArray data_array = login_object.getJSONArray("data");
                        for (int i=0; i<data_array.length();i++) {
                            JSONObject driver_data = data_array.getJSONObject(i);
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
                            GlobalClass.savePreferences("short_bio", driver_data.getString("short_bio"), context);
                            GlobalClass.savePreferences("car_model", driver_data.getString("car_model"), context);
                            GlobalClass.savePreferences("car_licence", driver_data.getString("car_licence"), context);
                            GlobalClass.savePreferences("car_color", driver_data.getString("car_color"), context);

                            startActivity(new Intent(context, ProfileActivity.class));
                            finish();
                            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                            break;
                            // String driver_id=driver_data.getString("driverid");
                        }
                    }else {
                        View view1 = findViewById(android.R.id.content);
                        Snackbar snackbar = Snackbar.make(view1,login_object.getString("message"), Snackbar.LENGTH_SHORT);
                        snackbar.show();
                        View view=snackbar.getView();
                        view.setBackgroundColor(getResources().getColor(R.color.logo_color));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //  Log.e("Result",responseStr);

            }
        }
    }

    private class ForgotPassword extends AsyncTask<String, String, String> {
        String result = "";
        Dialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog=loadingDialog(LoginSignupActivity.this);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            String url = Webservice_Url + ForgotPassword;
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);

            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("email",params[0]));
               // nameValuePairs.add(new BasicNameValuePair("password",params[1]));

               // Log.e("Name value of login", String.valueOf(nameValuePairs));

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);

                result = EntityUtils.toString(response.getEntity());

                //Log.e("result forgot password", result);

            } catch (ClientProtocolException e) {

            } catch (IOException e) {

            }

            return result;
        }

        @Override
        protected void onPostExecute(String responseStr) {
            super.onPostExecute(responseStr);
            if (dialog.isShowing()) dialog.dismiss();

            if (responseStr.length() > 0) {
                try {
                    JSONObject login_object=new JSONObject(responseStr);
                    if (login_object.getBoolean("status")){
                        JSONArray data_array = login_object.getJSONArray("data");

                    }else {
                        View view1 = findViewById(android.R.id.content);
                        Snackbar snackbar = Snackbar.make(view1,login_object.getString("message"), Snackbar.LENGTH_SHORT);
                        snackbar.show();
                        View view=snackbar.getView();
                        view.setBackgroundColor(getResources().getColor(R.color.logo_color));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //  Log.e("Result",responseStr);

            }
        }
    }

    private class UpdatePassword extends AsyncTask<String, String, String> {
        String result = "";
        Dialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog=loadingDialog(LoginSignupActivity.this);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            String url = Webservice_Url + UpdatePassword;
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);

            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("email",params[0]));
                nameValuePairs.add(new BasicNameValuePair("otp",params[1]));
                nameValuePairs.add(new BasicNameValuePair("password",params[2]));
                nameValuePairs.add(new BasicNameValuePair("confirm_password",params[2]));
                // nameValuePairs.add(new BasicNameValuePair("password",params[1]));

                // Log.e("Name value of login", String.valueOf(nameValuePairs));

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);
                result = EntityUtils.toString(response.getEntity());
               // Log.e("result forgot password", result);

            } catch (ClientProtocolException e) {

            } catch (IOException e) {

            }

            return result;
        }

        @Override
        protected void onPostExecute(String responseStr) {
            super.onPostExecute(responseStr);
            if (dialog.isShowing()) dialog.dismiss();

            if (responseStr.length() > 0) {
                try {
                    JSONObject login_object=new JSONObject(responseStr);
                    if (login_object.getBoolean("status")){
                        JSONArray data_array = login_object.getJSONArray("data");
                        newPassDialog.dismiss();
                        View view1 = findViewById(android.R.id.content);
                        Snackbar snackbar = Snackbar.make(view1,login_object.getString("message"), Snackbar.LENGTH_LONG);
                        snackbar.show();
                        View view=snackbar.getView();
                        view.setBackgroundColor(getResources().getColor(R.color.logo_color));

                    }else {
                        View view1 = findViewById(android.R.id.content);
                        Snackbar snackbar = Snackbar.make(view1,login_object.getString("message"), Snackbar.LENGTH_SHORT);
                        snackbar.show();
                        View view=snackbar.getView();
                        view.setBackgroundColor(getResources().getColor(R.color.logo_color));
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

            ViewHolder viewHolder1 = new ViewHolder(view1);

            return viewHolder1;
        }


        @Override
        public void onBindViewHolder(final ViewHolder holder, int position){

            final ReportModel reportModel_obj = reportModelList.get(position);

            String imageUrl = reportModel_obj.getImage_url();

            holder.checkBox.setText(reportModel_obj.getProvider_level());
            holder.checkBox.setTag(position);
            holder.checkBox.setChecked(mSparseBooleanArray.get(position));
            holder.checkBox.setOnCheckedChangeListener(mCheckedChangeListener);

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
            AdMarvelUtils.initialize(LoginSignupActivity.this, publisherIds);

            Map<String, Object> targetParams = new HashMap<String, Object>();
            //targetParams.put("KEYWORDS", "games");
            targetParams.put("APP_VERSION", "1.0.0"); //version of your app
            adMarvelView.requestNewAd(targetParams, AdMarvelBannerPartnerId, AdMarvelBannerSiteId);
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
