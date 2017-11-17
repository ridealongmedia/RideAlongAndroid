package com.ridealongpivot;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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
import java.util.List;

import static com.ridealongpivot.GlobalClass.SubmitFeedback;
import static com.ridealongpivot.GlobalClass.Webservice_Url;
import static com.ridealongpivot.GlobalClass.alertDialog;
import static com.ridealongpivot.GlobalClass.loadingDialog;

public class DriverProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RatingBar ratingBar;
    RelativeLayout rl_main;
    LinearLayout ll_menu;
    EditText et_feedback;
    RoundedImageView iv_driver_img;
    TextView tv_driver_name,tv_driver_add,tv_car_model,tv_licence,tv_car_color;
    Button bt_submit_feedback;
    String currentRating;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    private OnFragmentInteractionListener mListener;

    public DriverProfileFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static DriverProfileFragment newInstance(String param1, String param2) {
        DriverProfileFragment fragment = new DriverProfileFragment();
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
        View view               = inflater.inflate(R.layout.fragment_driver_profile, container, false);

        et_feedback             = (EditText) view.findViewById(R.id.et_feedback);
        iv_driver_img           = (RoundedImageView) view.findViewById(R.id.iv_driver_img);
        tv_driver_name          = (TextView) view.findViewById(R.id.tv_driver_name);
        tv_driver_add           = (TextView) view.findViewById(R.id.tv_driver_add);

        tv_car_model            = (TextView) view.findViewById(R.id.tv_car_model);
        tv_licence              = (TextView) view.findViewById(R.id.tv_car_licence);
        tv_car_color            = (TextView) view.findViewById(R.id.tv_car_color);
        ratingBar               = (RatingBar) view.findViewById(R.id.rating_bar);

        bt_submit_feedback      = (Button) view.findViewById(R.id.bt_submit_feedback);

        setProfileData();

        executeDelayed();
        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                //Log.e("Rating", String.valueOf(rating));
                currentRating= String.valueOf(rating);
            }
        });

        bt_submit_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitFeedbackDialog();
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
       /* if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void setProfileData(){
        try {
            tv_driver_name.setText(GlobalClass.callSavedPreferences("fname", getContext()) + " " + GlobalClass.callSavedPreferences("lname", getContext()).charAt(0));
            tv_driver_add.setText(GlobalClass.callSavedPreferences("short_bio", getContext()));

            tv_car_model.setText(GlobalClass.callSavedPreferences("car_model", getContext()));
            tv_licence.setText(GlobalClass.callSavedPreferences("car_licence", getContext()));
            tv_car_color.setText(GlobalClass.callSavedPreferences("car_color", getContext()));

            Picasso.with(getActivity()).load(GlobalClass.callSavedPreferences("profile_pic", getContext())).error(R.drawable.app_icon_new).placeholder(R.drawable.app_icon_new).resize(150, 150).centerCrop().into(iv_driver_img);
            currentRating = GlobalClass.callSavedPreferences("driver_rate", getContext());
            try {
                ratingBar.setRating(Float.parseFloat(currentRating));
            } catch (NumberFormatException ignored) {

            }
        }catch (Exception ignored){

        }
    }

    private void submitFeedbackDialog(){
        final Dialog dialog;
        dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.user_feedback_dialog);
        dialog.setCancelable(false);
        final TextInputLayout input_name,input_email,input_phone;
        final EditText et_name,et_email,et_phone;
        Button bt_submit;
        ImageView iv_cancel;

        input_name   = (TextInputLayout) dialog.findViewById(R.id.input_p_name);
        input_email  = (TextInputLayout) dialog.findViewById(R.id.input_p_email);
        input_phone  = (TextInputLayout) dialog.findViewById(R.id.input_p_phone);

        et_name      = (EditText) dialog.findViewById(R.id.et_p_name);
        et_email     = (EditText) dialog.findViewById(R.id.et_p_email);
        et_phone     = (EditText) dialog.findViewById(R.id.et_p_phone);

        bt_submit    = (Button) dialog.findViewById(R.id.bt_submit);

        iv_cancel   = (ImageView) dialog.findViewById(R.id.iv_cancel);

        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_email.getText().toString().trim().length()<=0 || !et_email.getText().toString().trim().matches(emailPattern)){
                    input_email.setError(getResources().getString(R.string.valid_email));
                }
                else {
                    new SubmitFeedback().execute(GlobalClass.callSavedPreferences("id", getContext()), currentRating, et_feedback.getText().toString().trim(), et_name.getText().toString().trim(), et_email.getText().toString().trim(), et_phone.getText().toString().trim());

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

    private class SubmitFeedback extends AsyncTask<String, String, String> {
        String result = "";
        Dialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog=loadingDialog(getActivity());
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = Webservice_Url + SubmitFeedback;
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);

            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("driver_id",params[0]));
                nameValuePairs.add(new BasicNameValuePair("driver_rate",params[1]));
                nameValuePairs.add(new BasicNameValuePair("rate_feedback",params[2]));
                nameValuePairs.add(new BasicNameValuePair("p_name",params[3]));
                nameValuePairs.add(new BasicNameValuePair("p_email",params[4]));
                nameValuePairs.add(new BasicNameValuePair("p_phone",params[5]));

                // Log.e("Name value of feedback", String.valueOf(nameValuePairs));

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);

                result = EntityUtils.toString(response.getEntity());

                //Log.e("result feedback", result);

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
                    JSONObject feedback_object=new JSONObject(responseStr);
                    if (feedback_object.getBoolean("status")){
                        JSONArray data_array = feedback_object.getJSONArray("data");
                        for (int i=0; i<data_array.length();i++) {
                            JSONObject driver_data = data_array.getJSONObject(i);
                            GlobalClass.savePreferences("id", driver_data.getString("driver_id"), getContext());
                            GlobalClass.savePreferences("driver_rate", driver_data.getString("driver_rate"), getContext());

                            et_feedback.setText("");
                            alertDialog(getContext(),getResources().getString(R.string.feedback_text));
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //  Log.e("Result",responseStr);

            }
        }
    }
}
