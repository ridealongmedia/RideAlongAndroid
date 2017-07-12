package com.ridealongpivot;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.LayerDrawable;
import android.media.AudioManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;

import com.ridealongpivot.googleplaces.Http;
import com.squareup.picasso.Picasso;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.UserTimeline;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import io.fabric.sdk.android.Fabric;

import static com.ridealongpivot.GlobalClass.CompanyAdsImage;
import static com.ridealongpivot.GlobalClass.FacebookPostix;
import static com.ridealongpivot.GlobalClass.FacebookPrefix;
import static com.ridealongpivot.GlobalClass.GetSpecialOffer;
import static com.ridealongpivot.GlobalClass.ProfilePic;
import static com.ridealongpivot.GlobalClass.TWITTER_KEY;
import static com.ridealongpivot.GlobalClass.TWITTER_SECRET;
import static com.ridealongpivot.GlobalClass.Webservice_Url;
import static com.ridealongpivot.GlobalClass.loadingDialog;


public class DirectoryFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    ExpandableListView list_dictionary;
    ExpandableListAdapter expandableListAdapter;
    RecyclerView rv_offers;
    LinearLayout ll_social_feeds;
    ListView lv_twit;
    LinearLayout ll_sp_ofr_tag;
    Button bt_post_special;

    LinearLayout ll_realbusiness;

    List<String> expandableListTitle;
    List<String> businessnameList;
    List<String> group_icons;
    HashMap<String, List<String>> expandableListDetail;

    TextView tv_bus_name,tv_bus_con_name,tv_bus_contact,tv_bus_site,tv_bus_insta,tv_bus_fb,tv_bus_twit,tv_bus_snap,tv_bus_add,tv_bus_timing;
    WebView wv_fb,wv_video;
    RatingBar rb_business;
    TextView tv_sp_ofr;
    GridView gv_insta;


    ArrayList<ReportModel> categoryList;
    ArrayList<BusinessesModel> businessList;
    ArrayList<AddressModel> businessAddressList;
    ArrayList<BusinessesModel> businessListForMarker;
    ArrayList<BusinessesModel> googleBusiness;
    ArrayList<BusinessesModel> userReviewList;
    ArrayList<BusinessImageModel> businessAdsImage;

    SpecialOffersAdsAdapter offersAdsAdapter;
    ArrayList<BusinessImageModel> buss_imageLIst;
    ArrayList<OffersModel> offers_list;
    GoogleApiClient mGoogleApiClient;

    String data="";

    private OnFragmentInteractionListener mListener;

    public DirectoryFragment() {
        // Required empty public constructor
    }


    public static DirectoryFragment newInstance(String param1, String param2) {
        DirectoryFragment fragment = new DirectoryFragment();
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
        View view               = inflater.inflate(R.layout.fragment_directory, container, false);

        list_dictionary         = (ExpandableListView) view.findViewById(R.id.list_dictionary);
        rv_offers               = (RecyclerView) view.findViewById(R.id.list_offers);
        ll_social_feeds         = (LinearLayout) view.findViewById(R.id.ll_social_feeds);

        ll_realbusiness         = (LinearLayout) view.findViewById(R.id.ll_realbusiness);

        lv_twit                 = (ListView) view.findViewById(R.id.lv_twit);
        gv_insta                = (GridView) view.findViewById(R.id.gv_insta);

        tv_bus_name             = (TextView) view.findViewById(R.id.tv_bus_name);
        tv_bus_con_name         = (TextView) view.findViewById(R.id.tv_bus_con_name);
        tv_bus_contact          = (TextView) view.findViewById(R.id.tv_bus_contact);
        tv_bus_site             = (TextView) view.findViewById(R.id.tv_bus_site);
        tv_bus_insta            = (TextView) view.findViewById(R.id.tv_bus_insta);
        tv_bus_twit             = (TextView) view.findViewById(R.id.tv_bus_twitter);
        tv_bus_fb               = (TextView) view.findViewById(R.id.tv_bus_facebook);
        tv_bus_snap             = (TextView) view.findViewById(R.id.tv_bus_snap);
        tv_bus_add              = (TextView) view.findViewById(R.id.tv_bus_add);
        tv_bus_timing           = (TextView) view.findViewById(R.id.tv_bus_timing);

        tv_sp_ofr               = (TextView) view.findViewById(R.id.tv_sp_ofr);

        rb_business             = (RatingBar) view.findViewById(R.id.rb_business);

        ll_sp_ofr_tag           = (LinearLayout) view.findViewById(R.id.ll_sp_offer_tag);
        bt_post_special         = (Button) view.findViewById(R.id.bt_post_special_offer);

        wv_fb                   = (WebView) view.findViewById(R.id.web_fb);
        wv_video                = (WebView) view.findViewById(R.id.web_video);

        try {
            LayerDrawable stars = (LayerDrawable) rb_business.getProgressDrawable();
            stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
        }catch (Exception ignored){}

        executeDelayed();

        wv_fb.getSettings().setJavaScriptEnabled(true);
        wv_video.getSettings().setJavaScriptEnabled(true);
        wv_fb.getSettings().setDomStorageEnabled(true);
        wv_video.getSettings().setDomStorageEnabled(true);

        wv_fb.setWebViewClient(new WebViewClient());
        wv_video.setWebViewClient(new WebViewClient());

        categoryList            = new ArrayList<ReportModel>();
        businessList            = new ArrayList<BusinessesModel>();
        businessAddressList     = new ArrayList<AddressModel>();
        businessListForMarker   = new ArrayList<BusinessesModel>();
        googleBusiness          = new ArrayList<BusinessesModel>();
        offers_list             = new ArrayList<OffersModel>();
        buss_imageLIst          = new ArrayList<BusinessImageModel>();
        userReviewList          = new ArrayList<BusinessesModel>();
        businessAdsImage        = new ArrayList<BusinessImageModel>();

        expandableListTitle     = new ArrayList<>();
        group_icons             = new ArrayList<>();
        expandableListDetail    = new HashMap<>();

        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(getActivity(), new Twitter(authConfig));

        setDirectoryData();

        list_dictionary.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                /*Toast.makeText(getApplicationContext(),
                        expandableListTitle.get(groupPosition) + " List Expanded.",
                        Toast.LENGTH_SHORT).show();*/
            }
        });

        list_dictionary.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                /*Toast.makeText(getApplicationContext(),
                        expandableListTitle.get(groupPosition) + " List Collapsed.",
                        Toast.LENGTH_SHORT).show();*/

            }
        });

        list_dictionary.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                /*Toast.makeText(
                        getApplicationContext(),
                        expandableListTitle.get(groupPosition)
                                + " -> "
                                + expandableListDetail.get(
                                expandableListTitle.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT
                ).show();*/
                //Log.e("Position", String.valueOf(groupPosition)+" "+String.valueOf(childPosition));
                selectCategoryData(groupPosition,childPosition);
                return false;
            }
        });

        buildGoogleApiClient();
        mGoogleApiClient.connect();

      /*  if (getArguments()!=null) {
            data           = getArguments().getString("data");
           // google_data    = getArguments().getString("data_google");
            googleBusiness = getArguments().getParcelableArrayList("data_google");
            parseDataAndSet(data);


           //  new GetAccessTokenFacebook().execute();

          *//*  new GraphRequest(AccessToken.getCurrentAccessToken(),
                    "/{1742577192620397}/feed",
                    null,
                    HttpMethod.GET,
                    new GraphRequest.Callback() {
                        public void onCompleted(GraphResponse response) {
                            Log.e("Response", response.toString());
                            Log.e("Current access token", String.valueOf(AccessToken.getCurrentAccessToken()) + " ");

                        }
                    }
            ).executeAsync();*//*
        }*/

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
       // mListener = null;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        new BusinessAdsBanner().execute();

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    public void onConnectionFailed(ConnectionResult connectionResult) {
        // Toast.makeText(this,"onConnectionFailed",Toast.LENGTH_SHORT).show();
    }

    protected synchronized void buildGoogleApiClient() {
        //Toast.makeText(this, "buildGoogleApiClient", Toast.LENGTH_SHORT).show();
        mGoogleApiClient = new GoogleApiClient
                .Builder(getActivity())
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addConnectionCallbacks(this)
                .build();
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void setDirectoryData() {

        list_dictionary.setIndicatorBounds(550 - GetDipsFromPixel(35), 550 - GetDipsFromPixel(5));

        expandableListAdapter = new CustomExpandableListAdapter(getActivity(), group_icons, expandableListTitle, expandableListDetail);
        list_dictionary.setAdapter(expandableListAdapter);

        Log.e("Group icon",group_icons.toString());

        if (!expandableListTitle.isEmpty()) list_dictionary.expandGroup(0);

        selectCategoryData(0,0);
    }

    public int GetDipsFromPixel(float pixels) {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
    }

    private class CustomExpandableListAdapter extends BaseExpandableListAdapter {
        private Context context;
        private List<String> expandableListTitle;
        private List<String> group_icon;
        private HashMap<String, List<String>> expandableListDetail;

        public CustomExpandableListAdapter(Context context, List<String> group_icon, List<String> expandableListTitle,
                                           HashMap<String, List<String>> expandableListDetail) {
            this.context = context;
            this.expandableListTitle = expandableListTitle;
            this.expandableListDetail = expandableListDetail;
            this.group_icon = group_icon;
        }

        @Override
        public Object getChild(int listPosition, int expandedListPosition) {
            return this.expandableListDetail.get(this.expandableListTitle.get(listPosition))
                    .get(expandedListPosition);
        }

        @Override
        public long getChildId(int listPosition, int expandedListPosition) {
            return expandedListPosition;
        }

        @Override
        public View getChildView(int listPosition, final int expandedListPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {
            final String expandedListText = (String) getChild(listPosition, expandedListPosition);
            if (convertView == null) {
                LayoutInflater layoutInflater = (LayoutInflater) this.context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(R.layout.list_item, null);
            }
            TextView expandedListTextView = (TextView) convertView.findViewById(R.id.expandedListItem);
            expandedListTextView.setText(expandedListText);
            return convertView;
        }

        @Override
        public int getChildrenCount(int listPosition) {
            return this.expandableListDetail.get(this.expandableListTitle.get(listPosition))
                    .size();
        }

        @Override
        public Object getGroup(int listPosition) {
            return this.expandableListTitle.get(listPosition);
        }

        public String getGroupIcon(int listPosition) {
           // Log.e("Group iccccc",group_icon.get(listPosition));
            return this.group_icon.get(listPosition);
        }

        @Override
        public int getGroupCount() {
            return this.expandableListTitle.size();
        }

        @Override
        public long getGroupId(int listPosition) {
            return listPosition;
        }

        @Override
        public View getGroupView(int listPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            String listTitle = (String) getGroup(listPosition);
            if (convertView == null) {
                LayoutInflater layoutInflater = (LayoutInflater) this.context.
                        getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(R.layout.list_group, null);
            }
            TextView listTitleTextView = (TextView) convertView.findViewById(R.id.listTitle);
            listTitleTextView.setTypeface(null, Typeface.BOLD);
            listTitleTextView.setText(listTitle);

            ImageView iv_group = (ImageView) convertView.findViewById(R.id.iv_group);
            //iv_group.setImageResource(getGroupIcon(listPosition));
            Picasso.with(context).load(getGroupIcon(listPosition)).error(R.drawable.app_icon_new).placeholder(R.drawable.app_icon_new).resize(100,100).centerCrop().into(iv_group);
            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public boolean isChildSelectable(int listPosition, int expandedListPosition) {
            return true;
        }
    }

    private class SpecialOffersAdsAdapter extends RecyclerView.Adapter<SpecialOffersAdsAdapter.MyViewHolder> {

        private List<BusinessImageModel> reportModelList;
        private Context context;

        boolean flag = true;
        String called_Activity;

        public class MyViewHolder extends RecyclerView.ViewHolder {

            public TextView tv_name, tv_total;
            ImageView iv_special_ofrs;
            CardView cardView;
            VideoView wv_videos;

            public MyViewHolder(View view) {
                super(view);
                iv_special_ofrs = (ImageView) view.findViewById(R.id.iv_special_offers);
                cardView        = (CardView) view.findViewById(R.id.card_view);
                wv_videos       = (VideoView) view.findViewById(R.id.wv_video);

            }
        }

        public SpecialOffersAdsAdapter(ArrayList<BusinessImageModel> reportModelList) {
            this.reportModelList = reportModelList;

        }

        @Override
        public SpecialOffersAdsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.special_offers_item, parent, false);

            return new SpecialOffersAdsAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final SpecialOffersAdsAdapter.MyViewHolder holder, final int position) {
            final BusinessImageModel reportModel = reportModelList.get(position);
            if (reportModel.getBuss_img_type().equals("1")) {
                holder.iv_special_ofrs.setVisibility(View.VISIBLE);
                holder.wv_videos.setVisibility(View.GONE);
                Picasso.with(getActivity()).load(reportModel.getBuss_images()).error(R.drawable.app_icon_new).placeholder(R.drawable.app_icon_new).into(holder.iv_special_ofrs);
            }
            else {
                if (reportModel.getBuss_video_available()=="1") {

                    AudioManager audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);

                    MediaController mediaController = new MediaController(getActivity());
                    mediaController.setAnchorView(holder.wv_videos);
                    mediaController.requestFocus();
                    mediaController.setMediaPlayer(holder.wv_videos);
                    holder.iv_special_ofrs.setVisibility(View.GONE);
                    holder.wv_videos.setVisibility(View.VISIBLE);
                    holder.wv_videos.setVideoPath(reportModel.getBuss_video());
                    holder.wv_videos.start();
                    holder.wv_videos.stopPlayback();
                    holder.wv_videos.setMediaController(mediaController);




                }
                else {
                    holder.iv_special_ofrs.setVisibility(View.GONE);
                    holder.wv_videos.setVisibility(View.GONE);
                }
            }
        }

        @Override
        public int getItemCount() {
            return reportModelList.size();
        }

    }

    private void selectCategoryData(int group_position, int child_position){
        try {
            if (!categoryList.isEmpty()) {
                ReportModel modelCategory = new ReportModel();
                modelCategory = categoryList.get(group_position);
                ArrayList<BusinessesModel> businessListmodel = modelCategory.getListBusiness();
                if (!businessListmodel.isEmpty()) {
                    final BusinessesModel businessDetailsModel = businessListmodel.get(child_position);

                    if (businessDetailsModel.getBuss_from() == 0) {
                        ll_sp_ofr_tag.setVisibility(View.VISIBLE);
                        bt_post_special.setVisibility(View.GONE);
                        tv_bus_contact.setVisibility(View.VISIBLE);

                        tv_bus_name.setText(businessDetailsModel.getBuss_name());
                        tv_bus_con_name.setText(businessDetailsModel.getBuss_con_name());
                        tv_bus_contact.setText(businessDetailsModel.getBuss_contact());


                        float ratings=0;
                        try {
                            ratings=Float.parseFloat(businessDetailsModel.getBuss_rate());
                        }
                        catch (NumberFormatException e){
                            ratings=0;
                        }
                        rb_business.setRating(ratings);

                        if (businessDetailsModel.getBusinessImages().isEmpty()){
                            offersAdsAdapter = new SpecialOffersAdsAdapter(businessAdsImage);
                            rv_offers.setAdapter(offersAdsAdapter);
                            offersAdsAdapter.notifyDataSetChanged();
                        }
                        else {
                            offersAdsAdapter = new SpecialOffersAdsAdapter(businessDetailsModel.getBusinessImages());
                            rv_offers.setAdapter(offersAdsAdapter);
                            offersAdsAdapter.notifyDataSetChanged();
                        }

                        ArrayList<OffersModel> offr=new ArrayList<>();
                        offr=businessDetailsModel.getBusinessofferList();

                        if (offr.isEmpty()){
                            tv_sp_ofr.setText("");
                        }
                        else {
                            OffersModel offersModel=new OffersModel();
                            offersModel=offr.get(0);
                            tv_sp_ofr.setText(offersModel.getOffer_title());
                        }

                        ArrayList<AddressModel> addresses = businessDetailsModel.getListAddress();
                        if (!addresses.isEmpty()) {
                            AddressModel addr = addresses.get(0);
                            tv_bus_add.setText(addr.getBuss_address());
                        }

                        tv_bus_timing.setVisibility(View.VISIBLE);
                        tv_bus_timing.setText(businessDetailsModel.getBuss_time_from() + " To " + businessDetailsModel.getBuss_time_to());

                        if (businessDetailsModel.getBuss_site() == null || businessDetailsModel.getBuss_site().equals(""))
                            tv_bus_site.setVisibility(View.GONE);
                        else {
                            tv_bus_site.setVisibility(View.VISIBLE);
                            tv_bus_site.setText(businessDetailsModel.getBuss_site());
                            tv_bus_site.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    try {
                                        Uri uri = Uri.parse(businessDetailsModel.getBuss_site()); // missing 'http://' will cause crashed
                                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                        startActivity(intent);
                                    } catch (ActivityNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }

                        if (businessDetailsModel.getBuss_insata() == null || businessDetailsModel.getBuss_insata().equals("")) {
                            tv_bus_insta.setVisibility(View.GONE);
                        } else {
                            tv_bus_insta.setVisibility(View.VISIBLE);
                            String[] usrUserName = businessDetailsModel.getBuss_insata().split("@");
                            if (usrUserName.length > 1) tv_bus_insta.setText(usrUserName[1]);
                            tv_bus_insta.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    try {
                                        Uri uri = Uri.parse(businessDetailsModel.getBuss_insata()); // missing 'http://' will cause crashed
                                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                        startActivity(intent);
                                    } catch (ActivityNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }

                        if (businessDetailsModel.getBuss_twitter() == null || businessDetailsModel.getBuss_twitter().equals("")) {
                            tv_bus_twit.setVisibility(View.GONE);
                        } else {
                            tv_bus_twit.setVisibility(View.VISIBLE);
                            String[] usrUserName = businessDetailsModel.getBuss_twitter().split("@");
                            if (usrUserName.length > 1) {
                                tv_bus_twit.setText(usrUserName[1]);

                            }
                            tv_bus_twit.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    try {
                                        Uri uri = Uri.parse(businessDetailsModel.getBuss_twitter()); // missing 'http://' will cause crashed
                                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                        startActivity(intent);
                                    } catch (ActivityNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                        }

                        if (businessDetailsModel.getBuss_fb() == null || businessDetailsModel.getBuss_fb().equals("")) {
                            tv_bus_fb.setVisibility(View.GONE);
                        } else {
                            tv_bus_fb.setVisibility(View.VISIBLE);
                            String[] usrUserName = businessDetailsModel.getBuss_fb().split("@");
                            if (usrUserName.length > 1) tv_bus_fb.setText(usrUserName[1]);
                            tv_bus_fb.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    try {
                                        Uri uri = Uri.parse(businessDetailsModel.getBuss_fb()); // missing 'http://' will cause crashed
                                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                        startActivity(intent);
                                    } catch (ActivityNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }

                        if (businessDetailsModel.getBuss_snapchat() == null || businessDetailsModel.getBuss_snapchat().equals(""))
                            tv_bus_snap.setVisibility(View.GONE);
                        else {
                            String[] usrUserName = businessDetailsModel.getBuss_snapchat().split(".com/");
                            if (usrUserName.length > 1) tv_bus_snap.setText(usrUserName[1]);
                            tv_bus_snap.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    try {
                                        Uri uri = Uri.parse(businessDetailsModel.getBuss_snapchat()); // missing 'http://' will cause crashed
                                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                        startActivity(intent);
                                    } catch (ActivityNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }

                        wv_video.setVisibility(View.GONE);
                       /* if (businessDetailsModel.getBuss_video_type()==1 && !businessDetailsModel.getBuss_video().equals("")){
                            wv_video.setVisibility(View.VISIBLE);
                            wv_video.loadUrl(ProfilePic+businessDetailsModel.getBuss_video());
                        } else {
                            wv_video.setVisibility(View.GONE);
                        }*/

                        if (businessDetailsModel.getSocialFeeds()==1 && businessDetailsModel.getIsUnlocked()==1){
                            if (businessDetailsModel.getBuss_feed_type()==1){
                                lv_twit.setVisibility(View.VISIBLE);
                                gv_insta.setVisibility(View.GONE);
                                wv_fb.setVisibility(View.GONE);
                                String[] usrUserName = businessDetailsModel.getBuss_twit_feed().split(".com/");
                                if (usrUserName.length > 1) {
                                    tv_bus_twit.setText(usrUserName[1]);
                                    final UserTimeline userTimeline = new UserTimeline.Builder()
                                            .screenName(usrUserName[1])
                                            .build();
                                    final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter.Builder(getActivity())
                                            .setTimeline(userTimeline)
                                            .build();
                                    lv_twit.setAdapter(adapter);
                                }
                            }
                            else if (businessDetailsModel.getBuss_feed_type()==2){
                                lv_twit.setVisibility(View.GONE);
                                wv_fb.setVisibility(View.GONE);
                                gv_insta.setVisibility(View.VISIBLE);
                                new GetInstafeeds().execute(businessDetailsModel.getBuss_insta_feed());
                            }
                            else if (businessDetailsModel.getBuss_feed_type()==3){
                                lv_twit.setVisibility(View.GONE);
                                wv_fb.setVisibility(View.VISIBLE);
                                gv_insta.setVisibility(View.GONE);
                                wv_fb.loadUrl(FacebookPrefix+businessDetailsModel.getBuss_fb_feed()+FacebookPostix);
                            }
                        }
                        else {
                            lv_twit.setVisibility(View.GONE);
                            wv_fb.setVisibility(View.GONE);
                            gv_insta.setVisibility(View.GONE);
                        }

                    }else {
                       // ll_realbusiness.setVisibility(View.GONE);
                      //  ll_google_buss.setVisibility(View.VISIBLE);
                        ll_sp_ofr_tag.setVisibility(View.GONE);
                        bt_post_special.setVisibility(View.VISIBLE);
                        wv_video.setVisibility(View.GONE);
                        wv_fb.setVisibility(View.GONE);
                        gv_insta.setVisibility(View.GONE);

                        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/details/json?");
                        googlePlacesUrl.append("placeid=" + businessDetailsModel.getBuss_id() );
                        googlePlacesUrl.append("&key=" + getResources().getString(R.string.google_api_key1));
                        // Log.e("Next page token", places_Object.getString("next_page_token"));
                        GooglePlacesReadTask googlePlacesReadTask = new GooglePlacesReadTask();
                        Object[] toPass = new Object[2];
                        toPass[1] = googlePlacesUrl.toString();
                        googlePlacesReadTask.execute(toPass);

                        offersAdsAdapter = new SpecialOffersAdsAdapter(businessAdsImage);
                        rv_offers.setAdapter(offersAdsAdapter);
                        offersAdsAdapter.notifyDataSetChanged();
                    }
                }
            }
        }catch (Exception ignored){

        }
    }

    private void parseDataAndSet(String responseStr){
        if (responseStr.length() > 0) {
            Log.e("Response buss",responseStr);
            try {
                JSONObject businessDatas=new JSONObject(responseStr);
                if (businessDatas.getBoolean("status")) {
                    JSONArray categoryArray = businessDatas.getJSONArray("data");
                    categoryList            = new ArrayList<ReportModel>();
                    businessListForMarker   = new ArrayList<BusinessesModel>();
                    expandableListTitle     = new ArrayList<>();

                    for (int i = 0; i < categoryArray.length(); i++) {
                        JSONObject catObject = categoryArray.getJSONObject(i);
                        ReportModel categoryModel = new ReportModel();

                        categoryModel.setCat_id(catObject.getString("cat_id"));
                        categoryModel.setCat_name(catObject.getString("cat_name"));
                        categoryModel.setCat_img(ProfilePic + catObject.getString("cat_img"));

                        expandableListTitle.add(catObject.getString("cat_name"));
                        group_icons.add(ProfilePic + catObject.getString("cat_img"));

                        JSONArray catBusinessList = catObject.getJSONArray("cat_business");
                        businessList     = new ArrayList<BusinessesModel>();
                        businessnameList = new ArrayList<>();
                        for (int j = 0; j < catBusinessList.length(); j++) {
                            final JSONObject catBusinessObject = catBusinessList.getJSONObject(j);
                            BusinessesModel businessModel = new BusinessesModel();
                            businessModel.setBuss_id(catBusinessObject.getString("bid"));
                            businessModel.setBuss_from(0);
                            businessModel.setBuss_name(catBusinessObject.getString("bname"));
                            businessModel.setBuss_con_name(catBusinessObject.getString("contact_person"));
                            businessModel.setBuss_contact(catBusinessObject.getString("contact_phone"));
                            businessModel.setBuss_cat_id(catBusinessObject.getString("cat_id"));
                            //businessModel.setBuss_time_from(catBusinessObject.getString("op_hours_from"));
                           // businessModel.setBuss_time_to(catBusinessObject.getString("op_hours_to"));
                            businessModel.setBuss_site(catBusinessObject.getString("website"));
                            businessModel.setBuss_insata(catBusinessObject.getString("social_instagram"));
                            businessModel.setBuss_twitter(catBusinessObject.getString("social_twitter"));
                            businessModel.setBuss_snapchat(catBusinessObject.getString("social_snapchat"));
                            businessModel.setBuss_fb(catBusinessObject.getString("social_facebook"));
                            businessModel.setBuss_img(catBusinessObject.getString("bus_picture"));
                            //businessModel.setBuss_video(catBusinessObject.getString("bus_video"));
                            businessModel.setBuss_rate(catBusinessObject.getString("bus_yelp"));
                            businessModel.setBuss_email(catBusinessObject.getString("email"));
                            businessModel.setBuss_icon_type(catBusinessObject.getInt("icon_type"));
                            businessModel.setSocialFeeds(catBusinessObject.getInt("feed"));
                            businessModel.setBuss_feed_type(catBusinessObject.getInt("feed_type"));
                            businessModel.setBuss_video_type(catBusinessObject.getInt("video"));

                            businessModel.setBuss_twit_feed(catBusinessObject.getString("feed_twitter"));
                            businessModel.setBuss_fb_feed(catBusinessObject.getString("feed_facebook"));
                            businessModel.setBuss_insta_feed(catBusinessObject.getString("feed_instagram"));
                            businessModel.setIsUnlocked(catBusinessObject.getInt("unlocked"));

                            //Log.e("Video Name",catBusinessObject.getString("bname")+" "+catBusinessObject.getString("bus_video"));
                            JSONArray businessTimingArray = catBusinessObject.getJSONArray("business_days");
                            if (businessTimingArray.length()>0){
                                Calendar calendar = Calendar.getInstance();
                                int day = calendar.get(Calendar.DAY_OF_WEEK);
                                JSONObject timingObject=null;
                                // Log.e("Day", String.valueOf(day));
                                // String dayofWeek="";
                                switch (day) {
                                    case Calendar.SUNDAY:
                                        timingObject=businessTimingArray.getJSONObject(6);
                                        break;

                                    case Calendar.MONDAY:
                                        timingObject=businessTimingArray.getJSONObject(0);
                                        break;

                                    case Calendar.TUESDAY:
                                        timingObject=businessTimingArray.getJSONObject(1);
                                        break;
                                    case Calendar.WEDNESDAY:
                                        timingObject=businessTimingArray.getJSONObject(2);
                                        break;
                                    case Calendar.THURSDAY:
                                        timingObject=businessTimingArray.getJSONObject(3);
                                        break;
                                    case Calendar.FRIDAY:
                                        timingObject=businessTimingArray.getJSONObject(4);
                                        break;
                                    case Calendar.SATURDAY:
                                        timingObject=businessTimingArray.getJSONObject(5);
                                        break;

                                }
                                assert timingObject != null;
                                businessModel.setBuss_time_from(timingObject.getString("op_hours_from"));
                                businessModel.setBuss_time_to(timingObject.getString("op_hours_to"));
                                businessModel.setDayStatus(timingObject.getInt("day_status"));
                              //  Log.e("Day",timingObject.getString("bday"));
                            }


                            businessnameList.add(catBusinessObject.getString("bname"));

                            JSONArray businessAddressArray = catBusinessObject.getJSONArray("business_address");
                            businessAddressList = new ArrayList<AddressModel>();
                            for (int k = 0; k < businessAddressArray.length(); k++) {
                                final JSONObject businessAddressObject = businessAddressArray.getJSONObject(k);
                                AddressModel businessAddressModel = new AddressModel();

                                businessAddressModel.setBuss_address(businessAddressObject.getString("baddress"));
                                businessAddressModel.setBuss_lat(businessAddressObject.getString("blatitude"));
                                businessAddressModel.setBuss_long(businessAddressObject.getString("blongitude"));

                                businessAddressList.add(businessAddressModel);
                            }

                            buss_imageLIst                  = new ArrayList<BusinessImageModel>();
                            offers_list                     = new ArrayList<OffersModel>();

                            //Check business is unlocked or not
                            if (catBusinessObject.getInt("unlocked")==1 && catBusinessObject.getInt("video")==1) {
                                JSONArray businessVideoArray = catBusinessObject.getJSONArray("business_video");
                                for (int l = 0; l < businessVideoArray.length(); l++) {
                                    JSONObject offerObject = businessVideoArray.getJSONObject(l);
                                    BusinessImageModel reportModelImages = new BusinessImageModel();
                                    reportModelImages.setBuss_id(offerObject.getString("bid"));
                                    reportModelImages.setBuss_images("");
                                    reportModelImages.setBuss_img_type("0");
                                    reportModelImages.setBuss_video_available(String.valueOf(catBusinessObject.getInt("video")));
                                    reportModelImages.setBuss_video(ProfilePic + offerObject.getString("bvideo"));
                                    buss_imageLIst.add(reportModelImages);
                                }


                                JSONArray businessImagesArray = catBusinessObject.getJSONArray("business_picture");
                                for (int l = 0; l < businessImagesArray.length(); l++) {
                                    JSONObject offerObject = businessImagesArray.getJSONObject(l);
                                    BusinessImageModel reportModelImages = new BusinessImageModel();
                                    reportModelImages.setBuss_id(offerObject.getString("bid"));
                                    reportModelImages.setBuss_images(ProfilePic + offerObject.getString("bpicture"));
                                    reportModelImages.setBuss_img_type("1");
                                    reportModelImages.setBuss_video("");
                                    reportModelImages.setBuss_video_available(String.valueOf(catBusinessObject.getInt("video")));
                                    //Log.e("Offers image",ProfilePic+offerObject.getString("boffer_img"));
                                    buss_imageLIst.add(reportModelImages);
                                }
                            }
                            if (catBusinessObject.getInt("unlocked")==1) {

                                JSONArray businessOfferArray = catBusinessObject.getJSONArray("business_offer");

                                for (int l = 0; l < businessOfferArray.length(); l++) {
                                    final JSONObject offerObject = businessOfferArray.getJSONObject(l);
                                    OffersModel businessOfferModel = new OffersModel();

                                    businessOfferModel.setOffer_id(offerObject.getString("bid"));
                                    businessOfferModel.setOffer_title(offerObject.getString("boffer_title"));
                                    offers_list.add(businessOfferModel);
                                }
                            }

                            businessModel.setListAddress(businessAddressList);
                            businessModel.setBusinessofferList(offers_list);
                            businessModel.setBusinessImages(buss_imageLIst);
                            businessList.add(businessModel);
                            businessListForMarker.add(businessModel);
                        }
                        expandableListDetail.put(catObject.getString("cat_name"), businessnameList);
                        categoryModel.setListBusiness(businessList);
                        categoryList.add(categoryModel);
                        //setDirectoryData();
                    }

                }
                parseGoogleData();

            } catch (JSONException e) {
                e.printStackTrace();
            }
            //  Log.e("Result",responseStr);
        }
    }

    private void parseGoogleData(){
        Log.e("Google business",googleBusiness.toString());
        Log.e("sizeee", String.valueOf(categoryList.size()));
        if (googleBusiness.size() > 0) {
            try {
                Collections.sort(googleBusiness, new Comparator<BusinessesModel>(){
                    public int compare(BusinessesModel s1, BusinessesModel s2) {
                        return s1.getCat_name().compareToIgnoreCase(s2.getCat_name());
                    }
                });

                for (int i=0;i<googleBusiness.size();i++){
                    BusinessesModel reportModel=new BusinessesModel();
                    reportModel=googleBusiness.get(i);
                    String category_name = reportModel.getCat_name();

                    int present=0;
                    for (int k=0;k<expandableListTitle.size();k++){
                        // Log.e("Category name", category_name+" "+expandableListTitle.get(k));
                        if (category_name.equals(expandableListTitle.get(k))){
                            businessnameList = expandableListDetail.get(category_name);
                            // Log.e("Name Name list", expandableListTitle.get(k)+ " "+String.valueOf(businessnameList.get(0)));
                            ReportModel businessLst=categoryList.get(k);
                            businessList     = businessLst.getListBusiness();

                            businessnameList.add(reportModel.getBuss_name());
                            businessList.add(reportModel);

                            expandableListDetail.put(category_name,businessnameList);
                           // group_icons.set(k,reportModel.getCat_img());
                            present=1;
                        }
                    }
                    if (present==0){
                        businessnameList = new ArrayList<>();
                        businessList     = new ArrayList<BusinessesModel>();

                        businessnameList.add(reportModel.getBuss_name());
                        businessList.add(reportModel);

                        ReportModel categoryModel = new ReportModel();
                        categoryModel.setCat_id(reportModel.getCat_id());
                        categoryModel.setCat_name(reportModel.getCat_name());
                        categoryModel.setCat_img(reportModel.getBuss_img());
                        categoryModel.setListBusiness(businessList);
                        expandableListTitle.add(category_name);
                        group_icons.add(reportModel.getBuss_img());
                        expandableListDetail.put(category_name, businessnameList);
                        categoryList.add(categoryModel);
                    }
                }

            }catch (Exception ignored){

            }
            //  Log.e("Result",responseStr);
        }
        setDirectoryData();
    }

    private class GooglePlacesReadTask extends AsyncTask<Object, Integer, String> {
        String googlePlacesData = null;

        @Override
        protected String doInBackground(Object... inputObj) {
            try {
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
                final JSONObject places_Object=new JSONObject(result);
                if (places_Object.getString("status").equals("OK")) {
                    final JSONObject results = places_Object.getJSONObject("result");

                    try {
                        tv_bus_name.setText(results.getString("name"));
                    }catch (Exception ignored){}
                   /* try {
                        tv_bus_contact.setText(results.getString("formatted_phone_number"));
                    }catch (Exception ignored){}*/
                    try {
                        tv_bus_add.setText(results.getString("formatted_address"));
                    }catch (Exception ignored){}
                   /* try {
                        tv_bus_site.setText(results.getString("website"));
                        tv_bus_site.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    Uri uri = Uri.parse(results.getString("website")); // missing 'http://' will cause crashed
                                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                    startActivity(intent);
                                } catch (ActivityNotFoundException | JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }catch (Exception ignored){}*/


                    tv_bus_timing.setVisibility(View.GONE);
                    tv_bus_insta.setVisibility(View.GONE);
                    tv_bus_snap.setVisibility(View.GONE);
                    tv_bus_twit.setVisibility(View.GONE);
                    tv_bus_fb.setVisibility(View.GONE);
                    tv_bus_contact.setVisibility(View.GONE);
                    tv_bus_site.setVisibility(View.GONE);

                    try {
                        JSONArray userReviews = results.getJSONArray("reviews");
                        userReviewList = new ArrayList<BusinessesModel>();
                        for (int i = 0; i < userReviews.length(); i++) {
                            JSONObject reviewObject = userReviews.getJSONObject(i);
                            BusinessesModel reviewModel = new BusinessesModel();
                            reviewModel.setAuthName(reviewObject.getString("author_name"));
                            reviewModel.setAuthReview(reviewObject.getString("text"));
                            reviewModel.setAuthReviewTime(reviewObject.getString("relative_time_description"));
                            reviewModel.setAuthRating(reviewObject.getDouble("rating"));
                            userReviewList.add(reviewModel);
                        }
                    }catch (Exception ignored){}
                    try {
                        rb_business.setRating((float) results.getDouble("rating"));
                    }catch (Exception ignored){}

                    lv_twit.setAdapter(new RatingListAdapter(getContext(),R.layout.rating_list_item,userReviewList));
                    lv_twit.setVisibility(View.VISIBLE);

                    bt_post_special.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                new TapForSpecialOffer().execute(results.getString("place_id"),tv_bus_name.getText().toString().trim(),tv_bus_add.getText().toString().trim());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });

                    //Log.e("Result place Details", result);
                   // Log.e("Opening hours", results.getJSONObject("opening_hours").toString());
                }


            }catch (Exception e){
                e.printStackTrace();

            }
        }
    }

    private class BusinessAdsBanner extends AsyncTask<String, String, String> {
        String result = "";
        Dialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog=loadingDialog(getActivity());
            if (dialog != null)
                dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = Webservice_Url + CompanyAdsImage;
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);

            try {
                HttpResponse response = httpclient.execute(httppost);
                result = EntityUtils.toString(response.getEntity());

                // Log.e("result Businesses", result);

            } catch (IOException ignored) {

            }

            return result;
        }

        @Override
        protected void onPostExecute(String responseStr) {
            super.onPostExecute(responseStr);
              if (dialog != null && dialog.isShowing()) dialog.dismiss();

            if (responseStr.length() > 0) {
                try {
                    JSONObject adsObject=new JSONObject(responseStr);
                    if (adsObject.getBoolean("status")){
                        JSONArray imagesArray=adsObject.getJSONArray("data");
                        for (int i=0;i<imagesArray.length();i++){
                            JSONObject images_Obj=imagesArray.getJSONObject(i);
                            JSONArray images = images_Obj.getJSONArray("business_picture");
                            businessAdsImage=new ArrayList<BusinessImageModel>();

                            JSONArray videoArr = images_Obj.getJSONArray("business_video");
                            for (int j=0;j<videoArr.length();j++){
                                BusinessImageModel reportModel=new BusinessImageModel();
                                reportModel.setBuss_images("");
                                reportModel.setBuss_video(ProfilePic+videoArr.getJSONObject(j).getString("bvideo"));
                                reportModel.setBuss_img_type("0");
                                reportModel.setBuss_video_available("1");
                                businessAdsImage.add(reportModel);
                            }

                            for (int j=0;j<images.length();j++){
                                BusinessImageModel reportModel=new BusinessImageModel();
                                reportModel.setBuss_images(ProfilePic+images.getJSONObject(j).getString("bpicture"));
                                reportModel.setBuss_video("");
                                reportModel.setBuss_img_type("1");
                                reportModel.setBuss_video_available("1");
                                businessAdsImage.add(reportModel);
                            }
                        }
                        if (getArguments()!=null) {
                            data           = getArguments().getString("data");
                            // google_data    = getArguments().getString("data_google");
                            googleBusiness = getArguments().getParcelableArrayList("data_google");
                            parseDataAndSet(data);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //Log.e("Result adssss",responseStr);
            }
        }
    }

    private class RatingListAdapter extends ArrayAdapter<BusinessesModel> {

        Context con;
        ArrayList<BusinessesModel> mlist;
        BusinessesModel reportModel;


        public RatingListAdapter(Context con, int resourceId, ArrayList<BusinessesModel> mlist) {
            super(con, resourceId, mlist);
            this.con=con;
            this.mlist=mlist;
        }

        @NonNull
        @Override
        public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
            // TODO Auto-generated method stub

            ViewHolder holder=null;
            reportModel =mlist.get(position);

            LayoutInflater inflater=(LayoutInflater)con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if(convertView==null){


                convertView=inflater.inflate(R.layout.rating_list_item,parent,false);
                holder=new ViewHolder();

                //      holder.textid=(TextView)convertView.findViewById(R.id.tv_No);
                holder.tv_auth_name     = (TextView)convertView.findViewById(R.id.tv_auth_name);
                holder.tv_auth_review   = (TextView)convertView.findViewById(R.id.tv_auth_review);
                holder.tv_auth_time     = (TextView)convertView.findViewById(R.id.tv_auth_time);
                holder.rb_auth_rating   = (RatingBar) convertView.findViewById(R.id.rb_auth_rating);

                convertView.setTag(holder);
            }
            else{
                holder=(ViewHolder)convertView.getTag();
            }
            //      holder.textid.setText(reportModel.getId());
            try{
                holder.tv_auth_name.setText(reportModel.getAuthName());
                holder.tv_auth_review.setText(reportModel.getAuthReview());
                holder.tv_auth_time.setText(reportModel.getAuthReviewTime());
                LayerDrawable stars = (LayerDrawable) holder.rb_auth_rating.getProgressDrawable();
                stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);

                holder.rb_auth_rating.setRating((float) reportModel.getAuthRating());
            }catch (Exception e){

            }

            return convertView;
        }

        private class ViewHolder{

            TextView tv_auth_name,tv_auth_review,tv_auth_time;
            RatingBar rb_auth_rating;


        }

        @Override
        public void remove(BusinessesModel object) {
            mlist.remove(object);
            notifyDataSetChanged();
        }

        public List<BusinessesModel> getWorldPopulation() {
            return mlist;
        }

    }

    private class TapForSpecialOffer extends AsyncTask<String, String, String> {
        String result = "";
        Dialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog=loadingDialog(getActivity());
            if (dialog != null)
                dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            String url = Webservice_Url + GetSpecialOffer;
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);

            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("placeid",params[0]));
                nameValuePairs.add(new BasicNameValuePair("bname",params[1]));
                nameValuePairs.add(new BasicNameValuePair("baddress",params[2]));

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                Log.e("Name pair offer", String.valueOf(nameValuePairs));

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);
                result = EntityUtils.toString(response.getEntity());
                // Log.e("result forgot password", result);

            } catch (IOException ignored) {

            }

            return result;
        }

        @Override
        protected void onPostExecute(String responseStr) {
            super.onPostExecute(responseStr);
            if (dialog!=null && dialog.isShowing()) dialog.dismiss();

            Log.e("Result offer",responseStr);
            if (responseStr.length() > 0) {
                try {
                    JSONObject login_object=new JSONObject(responseStr);
                    if (login_object.getBoolean("status")){


                    }else {

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //  Log.e("Result",responseStr);

            }
        }
    }

    private class GetInstafeeds extends AsyncTask<String, String, String> {
        String result = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = params[0];
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httppost = new HttpGet(url);

            try {
                HttpResponse response = httpclient.execute(httppost);

                result = EntityUtils.toString(response.getEntity());

               // Log.e("result insta token", result);

            } catch (IOException ignored) {

            }

            return result;
        }

        @Override
        protected void onPostExecute(String responseStr) {
            super.onPostExecute(responseStr);
            if (responseStr.length() > 0) {
                try {
                    JSONObject instaObject=new JSONObject(responseStr);
                    JSONArray instaData=instaObject.getJSONArray("data");
                    final ArrayList<String > instaImages=new ArrayList<>();

                    for (int i=0; i<instaData.length();i++){

                        JSONObject object=instaData.getJSONObject(i);
                        JSONObject imges=object.getJSONObject("images");
                        JSONObject resolu=imges.getJSONObject("standard_resolution");
                        instaImages.add(resolu.getString("url"));
                    }

                    gv_insta.setVisibility(View.VISIBLE);
                    gv_insta.setAdapter(new InstaGridAdapter(instaImages));

                    gv_insta.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            try {
                               // Log.e("Url",instaImages.get(position));
                                Uri uri = Uri.parse(instaImages.get(position)); // missing 'http://' will cause crashed
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                startActivity(intent);
                            } catch (ActivityNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public class InstaGridAdapter extends BaseAdapter{
        private final ArrayList<String> images;

        public InstaGridAdapter(ArrayList<String> images ) {
            this.images = images;

        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return images.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            View grid;
            String imagePos=images.get(position);
            LayoutInflater inflater = (LayoutInflater) getActivity()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (convertView == null) {

                grid = new View(getActivity());
                grid = inflater.inflate(R.layout.insta_grid_item, null);

            } else {
                grid = (View) convertView;
            }

            ImageView imageView = (ImageView)grid.findViewById(R.id.iv_insta);

            Picasso.with(getActivity()).load(imagePos).centerCrop().resize(200,200).error(R.drawable.app_icon_new).placeholder(R.drawable.app_icon_new).into(imageView);

            return grid;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

}
