package com.ridealongpivot;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import static com.ridealongpivot.GlobalClass.FreeBusinessTrial;
import static com.ridealongpivot.GlobalClass.GetDriverInfo;
import static com.ridealongpivot.GlobalClass.HelpCompanyInfo;
import static com.ridealongpivot.GlobalClass.ProfilePic;
import static com.ridealongpivot.GlobalClass.SubmitFeedbackHelp;
import static com.ridealongpivot.GlobalClass.Webservice_Url;
import static com.ridealongpivot.GlobalClass.alertDialog;
import static com.ridealongpivot.GlobalClass.loadingDialog;


public class HelpFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    TextView tv_company_no1,tv_company_no2,tv_company_email,tv_company_about,tv_address;
    Button bt_get_driver,bt_free_trial,bt_submit_help;
    ImageView iv_company_img,iv_site,iv_fb,iv_twitter,iv_insta,iv_google;
    EditText et_feedback_help;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public HelpFragment() {
        // Required empty public constructor
    }


    public static HelpFragment newInstance(String param1, String param2) {
        HelpFragment fragment = new HelpFragment();
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
        View view               = inflater.inflate(R.layout.fragment_help, container, false);

        tv_company_no1          = (TextView) view.findViewById(R.id.tv_company_no1);
        tv_company_no2          = (TextView) view.findViewById(R.id.tv_company_no2);
        tv_company_email        = (TextView) view.findViewById(R.id.tv_company_email);
        tv_company_about        = (TextView) view.findViewById(R.id.tv_about);
        tv_address              = (TextView) view.findViewById(R.id.tv_address);

        bt_get_driver           = (Button) view.findViewById(R.id.bt_get_driver);
        bt_free_trial           = (Button) view.findViewById(R.id.bt_free_trial);
        bt_submit_help          = (Button) view.findViewById(R.id.bt_submit_help_feed);
        iv_company_img          = (ImageView) view.findViewById(R.id.iv_company);

        iv_site                 = (ImageView) view.findViewById(R.id.iv_site);
        iv_fb                   = (ImageView) view.findViewById(R.id.iv_fb);
        iv_twitter              = (ImageView) view.findViewById(R.id.iv_twitter);
        iv_google               = (ImageView) view.findViewById(R.id.iv_google);
        iv_insta                = (ImageView) view.findViewById(R.id.iv_insta);

        et_feedback_help        = (EditText) view.findViewById(R.id.et_feedback_help);

        bt_get_driver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInfoDialog(GetDriverInfo);
            }
        });

        bt_free_trial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInfoDialog(FreeBusinessTrial);
            }
        });

        bt_submit_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_feedback_help.getText().toString().trim().length()<=0) et_feedback_help.setError(getResources().getString(R.string.feedback)+" "+getResources().getString(R.string.can_not));
                else submitFeedbackDialog();
            }
        });

        new ProfileInfoHelp().execute();

        return view;
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
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void getInfoDialog(final String linkButtons){
        final Dialog dialog;
        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.forgot_password_dialog);
        dialog.setCancelable(false);
        final TextInputLayout input_email;
        final EditText et_email;
        TextView tv_dialog_head;
        Button bt_submit;
        ImageView iv_cancel;

        input_email         = (TextInputLayout) dialog.findViewById(R.id.input_email);
        tv_dialog_head      = (TextView) dialog.findViewById(R.id.tv_dialog_head);

        et_email            = (EditText) dialog.findViewById(R.id.et_email);

        bt_submit           = (Button) dialog.findViewById(R.id.bt_submit);

        iv_cancel           = (ImageView) dialog.findViewById(R.id.iv_cancel);

        tv_dialog_head.setText(getResources().getString(R.string.enter_email_address));

        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_email.getText().toString().trim().length()<=0 || !et_email.getText().toString().trim().matches(emailPattern)){
                    input_email.setError(getResources().getString(R.string.valid_email));
                }
                else {
                    new GetInformationTask().execute(linkButtons,GlobalClass.callSavedPreferences("id",getContext()),et_email.getText().toString().trim());
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

    private void submitFeedbackDialog(){
        final Dialog dialog;
        dialog = new Dialog(getActivity());
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
                    new SubmitFeedbackHelp().execute(et_feedback_help.getText().toString().trim(), et_name.getText().toString().trim(), et_email.getText().toString().trim(), et_phone.getText().toString().trim());
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

    private class ProfileInfoHelp extends AsyncTask<String, String, String> {
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

            String url = Webservice_Url + HelpCompanyInfo;
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);
            try {
                HttpResponse response = httpclient.execute(httppost);

                result = EntityUtils.toString(response.getEntity());

                Log.e("result Help", result);

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
                    JSONObject help_object=new JSONObject(responseStr);
                    if (help_object.getBoolean("status")){
                        JSONArray data_array = help_object.getJSONArray("data");
                        for (int i=0; i<data_array.length();i++) {
                            final JSONObject help_data = data_array.getJSONObject(i);
                            tv_company_email.setText(help_data.getString("cmp_email"));
                            tv_company_no1.setText(help_data.getString("cmp_phone1"));
                            tv_company_no2.setText(help_data.getString("cmp_phone2"));
                            tv_company_about.setText(help_data.getString("cmp_about"));
                            tv_address.setText(help_data.getString("cmp_address"));

                            iv_site.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    try {
                                        Uri uri = Uri.parse(help_data.getString("cmp_website")); // missing 'http://' will cause crashed
                                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                        startActivity(intent);
                                    }catch (ActivityNotFoundException | JSONException e){
                                        e.printStackTrace();
                                    }
                                }
                            });

                            iv_fb.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    try {
                                        Uri uri = Uri.parse(help_data.getString("cmp_social_fb")); // missing 'http://' will cause crashed
                                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                        startActivity(intent);
                                    }catch (ActivityNotFoundException | JSONException e){
                                        e.printStackTrace();
                                    }
                                }
                            });

                            iv_twitter.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    try {
                                        Uri uri = Uri.parse(help_data.getString("cmp_social_twit")); // missing 'http://' will cause crashed
                                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                        startActivity(intent);
                                    }catch (ActivityNotFoundException | JSONException e){
                                        e.printStackTrace();
                                    }
                                }
                            });

                            iv_google.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    try {
                                        Uri uri = Uri.parse(help_data.getString("cmp_social_gp")); // missing 'http://' will cause crashed
                                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                        startActivity(intent);
                                    }catch (ActivityNotFoundException | JSONException e){
                                        e.printStackTrace();
                                    }
                                }
                            });

                            iv_insta.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    try {
                                        Uri uri = Uri.parse(help_data.getString("cmp_social_insta")); // missing 'http://' will cause crashed
                                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                        startActivity(intent);
                                    }catch (ActivityNotFoundException | JSONException e){
                                        e.printStackTrace();
                                    }
                                }
                            });

                            Picasso.with(getActivity()).load(ProfilePic+help_data.getString("cmp_img")).fit().into(iv_company_img);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class SubmitFeedbackHelp extends AsyncTask<String, String, String> {
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

            String url = Webservice_Url + SubmitFeedbackHelp;
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);

            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("p_feedback",params[0]));
                nameValuePairs.add(new BasicNameValuePair("p_name",params[1]));
                nameValuePairs.add(new BasicNameValuePair("p_email",params[2]));
                nameValuePairs.add(new BasicNameValuePair("p_phone",params[3]));

                // Log.e("Name value of feedback", String.valueOf(nameValuePairs));

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);

                result = EntityUtils.toString(response.getEntity());

               // Log.e("result feedback help", result);

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
                        et_feedback_help.setText("");
                        alertDialog(getActivity(),getResources().getString(R.string.feedback_text));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //  Log.e("Result",responseStr);

            }
        }
    }

    private class GetInformationTask extends AsyncTask<String, String, String> {
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

            String url = Webservice_Url + params[0];
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);

            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("driver_id",params[1]));
                nameValuePairs.add(new BasicNameValuePair("clt_email",params[2]));

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                Log.e("key pair gett info", String.valueOf(nameValuePairs));

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);

                result = EntityUtils.toString(response.getEntity());

                Log.e("result Get info", result);

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
                    JSONObject help_object=new JSONObject(responseStr);
                    alertDialog(getActivity(),help_object.getString("message"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //  Log.e("Result",responseStr);

            }
        }
    }
}
