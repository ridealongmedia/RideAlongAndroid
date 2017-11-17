package com.ridealongpivot;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.adcolony.sdk.AdColony;
import com.adcolony.sdk.AdColonyAdOptions;
import com.adcolony.sdk.AdColonyAdSize;
import com.adcolony.sdk.AdColonyAppOptions;
import com.adcolony.sdk.AdColonyInterstitial;
import com.adcolony.sdk.AdColonyInterstitialListener;
import com.adcolony.sdk.AdColonyNativeAdView;
import com.adcolony.sdk.AdColonyUserMetadata;
import com.adcolony.sdk.AdColonyZone;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.ridealongpivot.gmailsender.SendMailTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static com.ridealongpivot.GlobalClass.APP_ID_ADCOLONY;
import static com.ridealongpivot.GlobalClass.USER_ID;
import static com.ridealongpivot.GlobalClass.USER_PASS;
import static com.ridealongpivot.GlobalClass.ZONE_ID_AFTER_SELFIE;
import static com.ridealongpivot.GlobalClass.ZONE_ID_IN_MAP;


public class SelfieFragment extends Fragment implements SurfaceHolder.Callback, RewardedVideoAdListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    ImageView iv_change_camera,iv_capture,iv_flash;
    //ImageView iv_retake,iv_right;
    Button bt_reake,bt_accept_send;
    Button bt_snap;
    LinearLayout ll_retake;
    SurfaceView surfaceCamera;

    boolean previewing = false;
    boolean camCondition = false;
    Camera camera;

    Camera.Parameters myParameters = null;
    Camera.Size myBestSize = null;
    boolean isFlashon=false;
    SurfaceHolder surfaceHolder;
    int currentCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;

    boolean isSelfieSent=false;

    private AdColonyInterstitial ads;
    private AdColonyInterstitialListener listener;
    private AdColonyAdOptions ad_options;

    private RewardedVideoAd mAd;

    private OnFragmentInteractionListener mListener;

    public SelfieFragment() {
        // Required empty public constructor
    }


    public static SelfieFragment newInstance(String param1, String param2) {
        SelfieFragment fragment = new SelfieFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view               = inflater.inflate(R.layout.fragment_selfie, container, false);
        mAd = MobileAds.getRewardedVideoAdInstance(getActivity());
        mAd.setRewardedVideoAdListener(this);
        iv_change_camera        = (ImageView) view.findViewById(R.id.iv_change_camera);

        bt_reake                = (Button) view.findViewById(R.id.bt_retake);
        bt_accept_send          = (Button) view.findViewById(R.id.bt_send);
        iv_capture              = (ImageView) view.findViewById(R.id.iv_capture);
        iv_flash                = (ImageView) view.findViewById(R.id.iv_flash);
        ll_retake               = (LinearLayout) view.findViewById(R.id.ll_retake);
        surfaceCamera           = (SurfaceView) view.findViewById(R.id.surface_camera);
        bt_snap                 = (Button) view.findViewById(R.id.bt_snap);

        loadRewardedVideoAd();
        ll_retake.setVisibility(View.GONE);

        setSelfieData();

        //setAdcolonyData();
        executeDelayed();

        start_camera(currentCameraId);

        iv_capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ll_retake.getVisibility()==View.GONE) {
                    camera.takePicture(shutterCallback,null,mPictureCallback);
                    ll_retake.setVisibility(View.VISIBLE);
                }
            }
        });

        bt_snap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ll_retake.getVisibility()==View.GONE) {
                    camera.takePicture(shutterCallback,null,mPictureCallback);
                    ll_retake.setVisibility(View.VISIBLE);
                }
            }
        });

        bt_reake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start_camera(currentCameraId);
                camera.startPreview();
                ll_retake.setVisibility(View.GONE);
            }
        });

        iv_flash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasFlash()) {
                    if (isFlashon) {
                        isFlashon = false;
                        iv_flash.setImageResource(R.drawable.flash_off);
                    } else {
                        isFlashon = true;
                        iv_flash.setImageResource(R.drawable.flsh_on);
                    }
                    start_camera(currentCameraId);
                }
            }
        });

        iv_change_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Camera.getNumberOfCameras() >= 2) {
                    stop_camera();
                    if (currentCameraId == Camera.CameraInfo.CAMERA_FACING_BACK) {
                        currentCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
                    } else {
                        currentCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
                    }
                    ll_retake.setVisibility(View.GONE);
                    start_camera(currentCameraId);
                }
            }
        });


        return view;
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
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                getActivity().getWindow().getDecorView().setSystemUiVisibility(
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
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.e("Attached","atached");
        /*if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.e("De Attached","De atached");
        mListener = null;
    }

    private void loadRewardedVideoAd() {
        if (!mAd.isLoaded())
            mAd.loadAd("ca-app-pub-7579462688885258/2238034509", new AdRequest.Builder().build());
    }

    @Override
    public void onRewardedVideoAdLoaded() {

    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoAdClosed() {

    }

    @Override
    public void onRewarded(RewardItem rewardItem) {

    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {

    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void setSelfieData(){
        try {
            surfaceHolder = surfaceCamera.getHolder();
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

            if (Camera.getNumberOfCameras() >= 2)
                currentCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
        }catch (Exception ignored){

        }
    }

    private void start_camera(int currentCameraId) {
        try{
            previewing=true;
            camera = Camera.open(currentCameraId);
        }catch(RuntimeException e){
            Log.e("Tag", "init_camera: " + e);
            return;
        }
        Camera.Parameters param;
        param = camera.getParameters();
        //modify parameter
        param.setPreviewFrameRate(20);
        //param.setPreviewSize(640, 480);
        if (hasFlash()){
            if (isFlashon) param.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            else param.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        }
        if(myBestSize != null){
            param.setPreviewSize(myBestSize.width, myBestSize.height);
        }
        //param.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        camera.setParameters(param);
        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
            //camera.takePicture(shutter, raw, jpeg)
        } catch (Exception e) {
            Log.e("Tag", "init_camera: " + e);
            return;
        }
    }

    private void stop_camera() {
        try {
            previewing = false;
            camera.stopPreview();
            camera.release();
        }catch (Exception ignored){

        }
    }

    private Camera.Size getBestPreviewSize(int width, int height, Camera.Parameters parameters){
        Camera.Size bestSize = null;
        try {
            List<Camera.Size> sizeList = parameters.getSupportedPreviewSizes();

            bestSize = sizeList.get(0);

            for (int i = 1; i < sizeList.size(); i++) {
                if ((sizeList.get(i).width * sizeList.get(i).height) >
                        (bestSize.width * bestSize.height)) {
                    bestSize = sizeList.get(i);
                }
            }

        }catch (Exception ignored){

        }
        return bestSize;
    }

    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    public boolean hasFlash() {
        if (camera == null) {
            return false;
        }

        Camera.Parameters parameters = camera.getParameters();

        if (parameters.getFlashMode() == null) {
            return false;
        }

        List<String> supportedFlashModes = parameters.getSupportedFlashModes();
        if (supportedFlashModes == null || supportedFlashModes.isEmpty() || supportedFlashModes.size() == 1 && supportedFlashModes.get(0).equals(Camera.Parameters.FLASH_MODE_OFF)) {
            return false;
        }

        return true;
    }

    Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
        public void onPictureTaken(final byte[] data, Camera c) {
            camera.stopPreview();
            previewing=false;
            bt_accept_send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FileOutputStream outStream = null;
                    try {

                        Bitmap bitmap      = BitmapFactory.decodeByteArray(data, 0, data.length);
                        Bitmap final_image = overlayBitmapToCenter(bitmap,BitmapFactory.decodeResource(getResources(), R.drawable.app_icon_new));

                        sendSelfieDialog(final_image);

                    }catch (Exception ignored){

                    }
                }
            });
        }
    };

    Camera.ShutterCallback shutterCallback = new Camera.ShutterCallback() {

        @Override
        public void onShutter() {
            try {
                Camera.Parameters params = camera.getParameters();
                params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                camera.setParameters(params);
            } catch (Exception ignored) {
            }
        }
    };


    private static String SaveImage(Bitmap finalBitmap) {

        String root = Environment.getExternalStorageDirectory().getAbsolutePath();
        File myDir = new File(root + "/saved_images");
        myDir.mkdirs();

        String fname = "Image-"+ System.currentTimeMillis() +".jpg";
        File file = new File (myDir, fname);
        if (file.exists ()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        //Log.e("Abs",file.getAbsolutePath());
        return file.getAbsolutePath();
    }

    public static Bitmap overlayBitmapToCenter(Bitmap bitmap1, Bitmap bitmap2) {
        int bitmap1Width = bitmap1.getWidth();
        int bitmap1Height = bitmap1.getHeight();
        int bitmap2Width = bitmap2.getWidth();
        int bitmap2Height = bitmap2.getHeight();

        float marginLeft = (float) (bitmap1Width - (bitmap2Width+10));
        float marginTop = (float) (bitmap1Height - (bitmap2Height+10));

       /* Log.e("margen left", String.valueOf(marginLeft));
        Log.e("margen top", String.valueOf(marginTop));

        Log.e("width logo", String.valueOf(bitmap2Width));
        Log.e("height logo", String.valueOf(bitmap2Height));

        Log.e("height full height", String.valueOf(bitmap1Height));
        Log.e("height full width", String.valueOf(bitmap1Width));
*/
        Bitmap overlayBitmap = Bitmap.createBitmap(bitmap1Width, bitmap1Height, bitmap1.getConfig());
        Canvas canvas = new Canvas(overlayBitmap);
        canvas.drawBitmap(bitmap1, new Matrix(), null);
        canvas.drawBitmap(bitmap2, marginLeft, marginTop, null);
        return overlayBitmap;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // TODO Auto-generated method stub
        // stop the camera
        Log.e("Surface changed","Changed");
        if (camera!=null) {
            myParameters = camera.getParameters();
            myBestSize = getBestPreviewSize(width, height, myParameters);

            if (camCondition) {
                camera.stopPreview(); // stop preview using stopPreview() method
                camCondition = false; // setting camera condition to false means stop
            }
            // condition to check whether your device have camera or not
            if (camera != null) {
                try {
                    Camera.Parameters parameters = camera.getParameters();
                    //parameters.setColorEffect(Camera.Parameters.EFFECT_SEPIA); //applying effect on camera
                    if (myBestSize != null) {
                        parameters.setPreviewSize(myBestSize.width, myBestSize.height);
                    }
                    camera.setParameters(parameters); // setting camera parameters
                    camera.setPreviewDisplay(surfaceHolder); // setting preview of camera
                    camera.startPreview();  // starting camera preview

                    camCondition = true; // setting camera to true which means having camera
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        else start_camera(currentCameraId);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        Log.e("Surface created","created");
        start_camera(currentCameraId);


    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        //Log.e("Surface destroy","destroy");
        if(previewing) {
            camera.stopPreview();  // stopping camera preview
            camera.release();       // releasing camera
            camera = null;          // setting camera to null when left
            camCondition = false;
        }// setting camera condition to false also when exit from application
    }

    private void sendSelfieDialog(final Bitmap final_image){
        final Dialog dialog;
        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.send_selfie_dialog);
        dialog.setCancelable(false);
        final TextInputLayout input_email,input_phone;
        final EditText et_email,et_phone;
        Button bt_submit;
        final CheckBox cb_term;
        ImageView iv_cancel;

        input_email  = (TextInputLayout) dialog.findViewById(R.id.input_p_email);
        input_phone  = (TextInputLayout) dialog.findViewById(R.id.input_p_phone);

        et_email     = (EditText) dialog.findViewById(R.id.et_p_email);
        et_phone     = (EditText) dialog.findViewById(R.id.et_p_phone);

        cb_term      = (CheckBox) dialog.findViewById(R.id.cb_term);

        bt_submit    = (Button) dialog.findViewById(R.id.bt_submit);

        iv_cancel   = (ImageView) dialog.findViewById(R.id.iv_cancel);

        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_email.getText().toString().trim().length()<=0 || !et_email.getText().toString().trim().matches(emailPattern)){
                    input_email.setError(getResources().getString(R.string.valid_email));
                }
                else if (cb_term.isChecked()){
                    try {
                        List<String> toEmailList = Arrays.asList(et_email.getText().toString().trim().split("\\s*,\\s*"));
                        new SendMailTask(getActivity()).execute(USER_ID,
                                USER_PASS, toEmailList, getResources().getString(R.string.email_subject), SaveImage(final_image));
                        dialog.dismiss();
                        mAd.show();
                        isSelfieSent = false;
                        start_camera(currentCameraId);
                        ll_retake.setVisibility(View.GONE);

                    }catch (Exception e){
                        Log.e("Exception",e.getMessage());

                    }
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

    private void setAdcolonyData(){
        try {
            AdColonyAppOptions app_options = new AdColonyAppOptions()
                    .setUserID(GlobalClass.callSavedPreferences("id", getContext()));

            AdColony.configure(getActivity(), app_options, APP_ID_ADCOLONY, ZONE_ID_AFTER_SELFIE);

            /** Optional user metadata sent with the ad options in each request */
            AdColonyUserMetadata metadata = new AdColonyUserMetadata()
                    .setUserAge(26)
                    .setUserEducation(AdColonyUserMetadata.USER_EDUCATION_BACHELORS_DEGREE)
                    .setUserGender(AdColonyUserMetadata.USER_MALE);

            /** Ad specific options to be sent with request */
            ad_options = new AdColonyAdOptions()
                    .setUserMetadata(metadata);

            listener = new AdColonyInterstitialListener() {

                @Override
                public void onRequestFilled(AdColonyInterstitial ad) {
                    ads = ad;
                    Log.e("Ads", "onRequestFilled");
                    //if (dialog.isShowing()) dialog.dismiss();

                }

                /**
                 * Ad request was not filled
                 */
                @Override
                public void onRequestNotFilled(AdColonyZone zone) {
                    Log.e("Ads", "onRequestNotFilled");
                    // if (dialog.isShowing()) dialog.dismiss();
                }

                /**
                 * Ad opened, reset UI to reflect state change
                 */
                @Override
                public void onOpened(AdColonyInterstitial ad) {
                    Log.e("Ads", "onOpened");
                    // dialog.show();
                }

                /**
                 * Request a new ad if ad is expiring
                 */
                @Override
                public void onExpiring(AdColonyInterstitial ad) {
                    Log.d("Ads", "onExpiring");
                    //dialog.show();
                    AdColony.requestInterstitial(ZONE_ID_AFTER_SELFIE, this, ad_options);
                }
            };



        }catch (Exception ignored){

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        start_camera(currentCameraId);
        ll_retake.setVisibility(View.GONE);
        try {
            Log.e("admob","admob");
            loadRewardedVideoAd();
        }catch (Exception ignored){

        }
    }

    @Override
    public void onPause() {
        super.onPause();
       //stop_camera();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stop_camera();
    }

}
