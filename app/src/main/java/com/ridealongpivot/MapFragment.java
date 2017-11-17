package com.ridealongpivot;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.adcolony.sdk.AdColony;
import com.adcolony.sdk.AdColonyAdOptions;
import com.adcolony.sdk.AdColonyAdSize;
import com.adcolony.sdk.AdColonyAppOptions;
import com.adcolony.sdk.AdColonyNativeAdView;
import com.adcolony.sdk.AdColonyNativeAdViewListener;
import com.adcolony.sdk.AdColonyUserMetadata;
import com.adcolony.sdk.AdColonyZone;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ridealongpivot.MapInfoWindow.MapWrapperLayout;
import com.ridealongpivot.MapInfoWindow.OnInfoWindowElemTouchListener;
import com.ridealongpivot.googleplaces.Http;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;;
import static com.ridealongpivot.GlobalClass.APP_ID_ADCOLONY;
import static com.ridealongpivot.GlobalClass.GetCategoryListB;
import static com.ridealongpivot.GlobalClass.GoogleBusiness;
import static com.ridealongpivot.GlobalClass.GoogleBusiness1;
import static com.ridealongpivot.GlobalClass.ProfilePic;
import static com.ridealongpivot.GlobalClass.Webservice_Url;
import static com.ridealongpivot.GlobalClass.ZONE_ID_IN_MAP;
import static com.ridealongpivot.GlobalClass.correctCategoryName;


public class MapFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, OnMapReadyCallback {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String data;
    ArrayList<BusinessesModel> google_Markers;
    Spinner sp_category;

    private AdColonyNativeAdView ad_view;
    private LinearLayout ad_layout;
    private AdColonyNativeAdViewListener ad_listener;
    private AdColonyAdOptions ad_options;

    MapWrapperLayout wrapper_map;
    private GoogleMap mMap;
    LocationManager lm;
    boolean isMarkerRotating = false;
    private Button bt_info_offer;
    TextView tv_info_bus_name, tv_info_dis;
    RatingBar rb_info;
    private ViewGroup infoWindow;
    private OnInfoWindowElemTouchListener infoButtonListener;
    Bitmap markerIconBitmap;

    AutoCompleteTextView autoCompView;

    MapView mapFragment;
    ImageView iv_current;

    ImageView iv_search_place;
    AutoCompleteAdapter autoCompleteAdapter;

    Location location;
    double latitude = 26.865729;
    double longitude = 75.7524622;
    LatLng latLng = new LatLng(26.865729, 75.7524622);
    LatLng lastUpdatedLatLng = new LatLng(0, 0);

    float zoom_level = 16;
    int PROXIMITY_RADIUS = 2500;
    boolean isUpdated=false;
    long lastUpdatedTime=System.currentTimeMillis();

    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Marker mCurrLocation;

    ArrayList<BusinessesModel> businessListForMarker = new ArrayList<>();
    ArrayList<AddressModel> businessAddressList = new ArrayList<>();
    ArrayList<BusinessesModel> businessList=new ArrayList<>();
    ArrayList<ReportModel> categoryList=new ArrayList<>();
    ArrayList<OffersModel> businessOffersList=new ArrayList<>();

    List<String> expandableListTitle=new ArrayList<>();
    List<String> businessnameList=new ArrayList<>();
    List<String> group_icons=new ArrayList<>();
    HashMap<String, List<String>> expandableListDetail=new HashMap<>();

    private OnFragmentInteractionListener mListener;

    public MapFragment() {
        // Required empty public constructor
    }

    public static MapFragment newInstance(String param1, String param2) {
        MapFragment fragment = new MapFragment();
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
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        wrapper_map     = (MapWrapperLayout) view.findViewById(R.id.map_map);
        mapFragment     = (MapView) view.findViewById(R.id.map);
        iv_current      = (ImageView) view.findViewById(R.id.iv_current_location);
        sp_category     = (Spinner) view.findViewById(R.id.sp_category);
        ad_layout       = (LinearLayout) view.findViewById(R.id.ll_ads);

        autoCompView    = (AutoCompleteTextView) view.findViewById(R.id.autoCompleteTextView);
        iv_search_place = (ImageView) view.findViewById(R.id.iv_search);

        lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        mapFragment.onCreate(savedInstanceState);
        mapFragment.onResume();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());

        // Showing status
        if(status==ConnectionResult.SUCCESS){

        }
            //tvStatus.setText("Google Play Services are available");
        else{
            //tvStatus.setText("Google Play Services are not available");
            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, getActivity(), requestCode);
            dialog.show();
        }

        executeDelayed();
        mapFragment.getMapAsync(this);

        businessListForMarker = new ArrayList<BusinessesModel>();

        autoCompleteAdapter = new AutoCompleteAdapter(getContext(), R.layout.list_item_autocomplete, autocomplete(businessListForMarker));
        autoCompView.setAdapter(autoCompleteAdapter);
        autoCompleteAdapter.notifyDataSetChanged();

        requestAdView();

        autoCompView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    String str = (String) parent.getItemAtPosition(position);
                    InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    for (int i = 0; i < businessListForMarker.size(); i++) {
                        BusinessesModel model = new BusinessesModel();
                        model = businessListForMarker.get(i);
                        if (str.equals(model.getBuss_name())) {
                            ArrayList<AddressModel> address = new ArrayList<AddressModel>();
                            address = model.getListAddress();
                            if (!address.isEmpty()) {
                                AddressModel add = new AddressModel();
                                add = address.get(0);

                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(add.getBuss_lat()), Double.parseDouble(add.getBuss_long())), zoom_level));
                            }
                        }
                    }

                } catch (Exception ignored) {

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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mMap = googleMap;
        //mMap.setMyLocationEnabled(true);
        // mMap.getUiSettings().setMyLocationButtonEnabled(false);
        location = lm.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
          mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom_level));
        if (location != null) {
            latLng = new LatLng(location.getLatitude(), location.getLongitude());
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom_level));
        }

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                return marker.getTitle().equals("");

            }
        });

       // infoWindowData();
     /*   mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            public boolean onMarkerClick(Marker marker) {
                if (marker.getSnippet().equals("5")) {
                    *//*if (lastclicked != marker) {
                        if (lastclicked != null) {
                            lastclicked.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker_disselect));
                            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker_select));
                            lastclicked = marker;
                        } else {
                            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker_select));
                            lastclicked = marker;
                        }
                    }*//*
                    marker.showInfoWindow();
                    lastclicked = marker;
                    double camreaMoveposition = 0.035;
                    if (zoom_level < 5) camreaMoveposition = 0.05;
                    else if (zoom_level >= 5 && zoom_level <= 10) camreaMoveposition = 0.04;
                    else if (zoom_level > 10 && zoom_level <= 13) camreaMoveposition = 0.035;
                    else camreaMoveposition = 0.08;
                    CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(new LatLng(marker.getPosition().latitude + (double) 250 / Math.pow(2, zoom_level), marker.getPosition().longitude), zoom_level);
                    mMap.moveCamera(cu);
                }

                return true;
            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {
                if (lastclicked != null) {
                    // Hide the last opened
                    lastclicked.hideInfoWindow();
                    //  lastclicked.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker_disselect));

                    // Nullify lastOpened
                    lastclicked = null;
                }

                // Move the camera to the new position
                final CameraPosition cameraPosition = new CameraPosition.Builder().target(point).zoom(mMap.getCameraPosition().zoom).build();

            }
        });*/


        iv_current.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom_level));
            }
        });

        buildGoogleApiClient();
        mGoogleApiClient.connect();
    }

    public static int getPixelsFromDp(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public void infoWindowData() {
        wrapper_map.init(mMap, getPixelsFromDp(getContext(), 39 + 20));
        infoWindow = (ViewGroup) getActivity().getLayoutInflater().inflate(R.layout.info_window_layout, null);
        bt_info_offer = (Button) infoWindow.findViewById(R.id.bt_offerrs);
        tv_info_bus_name = (TextView) infoWindow.findViewById(R.id.tv_info_name);
        tv_info_dis = (TextView) infoWindow.findViewById(R.id.tv_info_distance);
        rb_info = (RatingBar) infoWindow.findViewById(R.id.rating_info);


        infoButtonListener = new OnInfoWindowElemTouchListener(bt_info_offer,
                null, //btn_default_normal_holo_light
                null) //btn_default_pressed_holo_light
        {
            @Override
            protected void onClickConfirmed(View v, Marker marker) {


            }
        };

        bt_info_offer.setOnTouchListener(infoButtonListener);

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
               /* if (!marker.getTitle().equals("Current Position")) {

                    *//*if (lastclicked != marker) {
                        if (lastclicked != null) {
                            lastclicked.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker_disselect));
                            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker_select));
                            lastclicked = marker;
                        } else {
                            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker_select));
                            lastclicked = marker;
                        }
                    }*//*
                }*/
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                if (marker.getSnippet().equals("5")) {
                    for (int i = 0; i < businessListForMarker.size(); i++) {
                        BusinessesModel modelBusinessMarker = new BusinessesModel();
                        modelBusinessMarker = businessListForMarker.get(i);
                        if (modelBusinessMarker.getBuss_id().equals(marker.getTitle())) {
                            tv_info_bus_name.setText(modelBusinessMarker.getBuss_name());
                            tv_info_dis.setText(modelBusinessMarker.getBuss_contact());
                            rb_info.setRating(Float.parseFloat(modelBusinessMarker.getBuss_rate()));
                            LayerDrawable stars = (LayerDrawable) rb_info.getProgressDrawable();
                            stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
                            break;
                        }
                    }
                    infoButtonListener.setMarker(marker);
                    wrapper_map.setMarkerWithInfoWindow(marker, infoWindow);
                    marker.isInfoWindowShown();
                    return infoWindow;
                } else {
                    return null;
                }
            }
        });
    }

    protected synchronized void buildGoogleApiClient() {
        //Toast.makeText(this, "buildGoogleApiClient", Toast.LENGTH_SHORT).show();
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            //place marker at current position
            mMap.clear();
            latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("Current Position");
            markerOptions.snippet("Current Position");
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.car_marker));
            mCurrLocation = mMap.addMarker(markerOptions);
            latitude = mLastLocation.getLatitude();
            longitude = mLastLocation.getLongitude();

            isUpdated=true;
            new GetCategoryList().execute(String.valueOf(latitude),String.valueOf(longitude));
        }

       /* if (getArguments() != null) {
            data = getArguments().getString("data");
            // google_data    = getArguments().getString("data_google");
            google_Markers = getArguments().getParcelableArrayList("data_google");
            parseDataAndSet(data);
        }*/
        if (!isUpdated) new GetCategoryList().execute(String.valueOf(latitude),String.valueOf(longitude));

        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                zoom_level = cameraPosition.zoom;
            }
        });

        /*lm  = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,3000,0,this);*/

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000); //5 seconds
        mLocationRequest.setFastestInterval(3000); //3 seconds
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        //mLocationRequest.setSmallestDisplacement(0); //2 meter

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }


    @Override
    public void onConnectionSuspended(int i) {
        // Toast.makeText(this,"onConnectionSuspended",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // Toast.makeText(this,"onConnectionFailed",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(Location location) {
        latLng = new LatLng(location.getLatitude(), location.getLongitude());
        latitude=location.getLatitude();
        longitude=location.getLongitude();
        //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom_level));
       // Toast.makeText(getActivity(), "Location updated"+String.valueOf(location), Toast.LENGTH_SHORT).show();

       // Toast.makeText(getActivity(), "Location changed"+String.valueOf(location), Toast.LENGTH_SHORT).show();
       // Toast.makeText(getActivity(), "Accuracy:"+String.valueOf(location.hasAccuracy()), Toast.LENGTH_SHORT).show();
        if (location.hasAccuracy()) {
            if (mCurrLocation != null) {
                //mCurrLocation.remove();
                animateMarker(mCurrLocation, latLng, false);
                Location lastLocaion=new Location("Location Start");
                lastLocaion.setLatitude(lastUpdatedLatLng.latitude);
                lastLocaion.setLongitude(lastUpdatedLatLng.longitude);


                //rotateMarker(mCurrLocation, location.getBearing());
                rotateMarker(mCurrLocation, lastLocaion.bearingTo(location));

                if ((int) (((System.currentTimeMillis()-lastUpdatedTime) / (1000*60)) % 60) >= 1 && (distanceInKm(latitude,longitude,lastUpdatedLatLng.latitude,lastUpdatedLatLng.longitude))/1000 >= 1){
                    lastUpdatedTime=System.currentTimeMillis();
                    lastUpdatedLatLng = latLng;

                    new GetCategoryList().execute(String.valueOf(latitude),String.valueOf(longitude));
                }

                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom_level));
                lastUpdatedLatLng = latLng;

               // mMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(latLng, zoom_level, 0, lastLocaion.bearingTo(location))));

            } else {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title("Current Position");
                markerOptions.snippet("Current Position");
                markerOptions.flat(true);
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.car_marker));
                mCurrLocation = mMap.addMarker(markerOptions);
            }
        }
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

    public void animateMarker(final Marker marker, final LatLng toPosition,
                              final boolean hideMarker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = mMap.getProjection();
        Point startPoint = proj.toScreenLocation(marker.getPosition());
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
        final long duration = 500;
        final Interpolator interpolator = new LinearInterpolator();
        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                double lng = t * toPosition.longitude + (1 - t)
                        * startLatLng.longitude;
                double lat = t * toPosition.latitude + (1 - t)
                        * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));
                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                } else {
                    if (hideMarker) {
                        marker.setVisible(false);
                    } else {
                        marker.setVisible(true);
                    }
                }
            }
        });
    }

    private double bearingBetweenLocations(LatLng latLng1,LatLng latLng2) {

        double PI = 3.14159;
        double lat1 = latLng1.latitude * PI / 180;
        double long1 = latLng1.longitude * PI / 180;
        double lat2 = latLng2.latitude * PI / 180;
        double long2 = latLng2.longitude * PI / 180;

        double dLon = (long2 - long1);

        double y = Math.sin(dLon) * Math.cos(lat2);
        double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1)
                * Math.cos(lat2) * Math.cos(dLon);

        double brng = Math.atan2(y, x);

        brng = Math.toDegrees(brng);
        brng = (brng + 360) % 360;

        return brng;
    }

    private void rotateMarker(final Marker marker, final float toRotation) {
        if(!isMarkerRotating) {
            final Handler handler = new Handler();
            final long start = SystemClock.uptimeMillis();
            final float startRotation = marker.getRotation();
            final long duration = 1000;

            final Interpolator interpolator = new LinearInterpolator();

            handler.post(new Runnable() {
                @Override
                public void run() {
                    isMarkerRotating = true;

                    long elapsed = SystemClock.uptimeMillis() - start;
                    float t = interpolator.getInterpolation((float) elapsed / duration);

                    float rot = t * toRotation + (1 - t) * startRotation;

                    marker.setRotation(-rot > 180 ? rot / 2 : rot);
                    if (t < 1.0) {
                        // Post again 16ms later.
                        handler.postDelayed(this, 16);
                    } else {
                        isMarkerRotating = false;
                    }
                }
            });
        }
    }

    private void parseDataAndSet(String responseStr){
        if (responseStr.length() > 0) {
            try {
                JSONObject businessDatas=new JSONObject(responseStr);
               // Log.e("Buss Map",responseStr);
                if (businessDatas.getBoolean("status")) {

                    mMap.clear();
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLng);
                    markerOptions.title("Current Position");
                   // markerOptions.snippet("Current Position");
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.car_marker));
                    mCurrLocation = mMap.addMarker(markerOptions);

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
                            businessModel.setIsUnlocked(catBusinessObject.getInt("unlocked"));

                          //  Log.e("Name type",catBusinessObject.getString("bname")+" "+catBusinessObject.getInt("icon_type"));
                            businessModel.setSocialFeeds(catBusinessObject.getInt("feed"));

                            businessnameList.add(catBusinessObject.getString("bname"));

                            JSONArray businessAddressArray = catBusinessObject.getJSONArray("business_address");
                            businessAddressList = new ArrayList<AddressModel>();
                            for (int k = 0; k < businessAddressArray.length(); k++) {
                                final JSONObject businessAddressObject = businessAddressArray.getJSONObject(k);
                                AddressModel businessAddressModel = new AddressModel();

                                businessAddressModel.setBuss_address(businessAddressObject.getString("baddress"));
                                businessAddressModel.setBuss_lat(businessAddressObject.getString("blatitude"));
                                businessAddressModel.setBuss_long(businessAddressObject.getString("blongitude"));
                                businessAddressModel.setBuss_marker_icon(ProfilePic+catBusinessObject.getString("icon"));
                                businessAddressModel.setBuss_icon_type(catBusinessObject.getInt("icon_type"));
                                businessAddressModel.setBuss_id(catBusinessObject.getString("bid"));

                                businessAddressList.add(businessAddressModel);

                            }

                            JSONArray businessOfferArray = catBusinessObject.getJSONArray("business_offer");
                            businessOffersList = new ArrayList<OffersModel>();
                            for (int l = 0; l < businessOfferArray.length(); l++) {
                                final JSONObject offerObject = businessOfferArray.getJSONObject(l);
                                OffersModel businessOfferModel = new OffersModel();

                                businessOfferModel.setOffer_id(offerObject.getString("bid"));
                                businessOfferModel.setOffer_title(offerObject.getString("boffer_title"));
                                businessOffersList.add(businessOfferModel);
                            }

                            businessModel.setListAddress(businessAddressList);
                            businessModel.setBusinessofferList(businessOffersList);
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
        if (google_Markers.size() > 0) {
            try {
                Collections.sort(google_Markers, new Comparator<BusinessesModel>(){
                    public int compare(BusinessesModel s1, BusinessesModel s2) {
                        return s1.getCat_name().compareToIgnoreCase(s2.getCat_name());
                    }
                });
                for (int i=0;i<google_Markers.size();i++){
                    BusinessesModel reportModel=new BusinessesModel();
                    reportModel=google_Markers.get(i);
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
                            businessListForMarker.add(reportModel);

                            expandableListDetail.put(category_name,businessnameList);
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
                        businessListForMarker.add(reportModel);
                        expandableListTitle.add(category_name);
                        group_icons.add(reportModel.getBuss_img());
                        expandableListDetail.put(category_name, businessnameList);
                        categoryList.add(categoryModel);
                    }
                }
                setSpinnerData();
               // setMarkers(0);

            }catch (Exception ignored){

            }
            //  Log.e("Result",responseStr);
        }
    }

    private void setSpinnerData(){
        ArrayList<String> sp_data=new ArrayList<>();
        sp_data.add("All");
        sp_data.addAll(expandableListTitle);
        ArrayAdapter<String> ad=new ArrayAdapter<String>(getActivity(),R.layout.spinner_item,sp_data);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_category.setAdapter(ad);

        sp_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int index = parent.getSelectedItemPosition();
                ((TextView) sp_category.getSelectedView()).setTextColor(getResources().getColor(R.color.logo_color));
                setMarkers(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                setMarkers(0);

            }
        });
    }

    private void setMarkers(int position){
        if (position==0){
            mMap.clear();
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("Current Position");
            //markerOptions.snippet("Current Position");
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.car_marker));
            mCurrLocation = mMap.addMarker(markerOptions);
            for (int i=0;i<categoryList.size();i++){
                ReportModel all_Business_list=new ReportModel();
                all_Business_list=categoryList.get(i);
                ArrayList<BusinessesModel> businessList=new ArrayList<>();
                businessList=all_Business_list.getListBusiness();
               // Log.e("Business length", String.valueOf(businessList.size()));
                for (int j=0;j<businessList.size();j++){
                    BusinessesModel business=new BusinessesModel();
                    business=businessList.get(j);
                    ArrayList<AddressModel> addressList=business.getListAddress();
                    for (int k=0;k<addressList.size();k++){
                        AddressModel add = new AddressModel();
                        add = addressList.get(k);
                       // Log.e("name buss",business.getBuss_name());
                        if (business.getBuss_icon_type()==5 && business.getIsUnlocked()==1){
                            final Marker marker = mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(Double.parseDouble(add.getBuss_lat()), Double.parseDouble(add.getBuss_long())))
                                    .title("")
                                   // .snippet(String.valueOf(business.getBuss_icon_type()))
                                    .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(business.getBuss_name(), business.getBuss_contact(), add.getBuss_address(),business.getBuss_rate()))));
                        }
                        else if (business.getBuss_icon_type()==7 && business.getIsUnlocked()==1){
                            ArrayList<OffersModel> offers=new ArrayList<>();
                            offers=business.getBusinessofferList();
                            String offerName="";
                            if (!offers.isEmpty()) {
                                OffersModel offersModel=new OffersModel();
                                offersModel=offers.get(0);
                                offerName=offersModel.getOffer_title();
                                final Marker marker = mMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(Double.parseDouble(add.getBuss_lat()), Double.parseDouble(add.getBuss_long())))
                                        .title("")
                                        // .snippet(String.valueOf(business.getBuss_icon_type()))
                                        .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromViewOffer(business.getBuss_name(), offerName,business.getBuss_contact(),add.getBuss_address(),business.getBuss_rate()))));
                            }else {
                                final Marker marker = mMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(Double.parseDouble(add.getBuss_lat()), Double.parseDouble(add.getBuss_long())))
                                        .title("")
                                        // .snippet(String.valueOf(business.getBuss_icon_type()))
                                        .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(business.getBuss_name(), business.getBuss_contact(), add.getBuss_address(),business.getBuss_rate()))));
                            }
                        }
                        else {
                            try {
                                markerIconBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.demo_marker);


                                Picasso.with(getContext()).load(add.getBuss_marker_icon()).placeholder(R.drawable.demo_marker).error(R.drawable.demo_marker).into(new Target() {
                                    @Override
                                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                                        markerIconBitmap = bitmap;

                                    }

                                    @Override
                                    public void onBitmapFailed(Drawable errorDrawable) {
                                    }

                                    @Override
                                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                                        // Log.e("prepare", String.valueOf(placeHolderDrawable));
                                    }
                                });

                                final Marker marker = mMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(Double.parseDouble(add.getBuss_lat()), Double.parseDouble(add.getBuss_long())))
                                        .title(business.getBuss_name())
                                        // .snippet(String.valueOf(business.getBuss_icon_type()))
                                        .icon(BitmapDescriptorFactory.fromBitmap(markerIconBitmap)));

                            } catch (NumberFormatException ignored) {

                            }
                        }

                    }
                }
            }
        }
        else {
            mMap.clear();
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("Current Position");
           // markerOptions.snippet("Current Position");
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.car_marker));
            mCurrLocation = mMap.addMarker(markerOptions);

            ReportModel all_Business_list=new ReportModel();
            all_Business_list=categoryList.get(position-1);
            ArrayList<BusinessesModel> businessList=new ArrayList<>();
            businessList=all_Business_list.getListBusiness();
            for (int j=0;j<businessList.size();j++){
                BusinessesModel business=new BusinessesModel();
                business=businessList.get(j);
                ArrayList<AddressModel> addressList=business.getListAddress();
                for (int k=0;k<addressList.size();k++){
                    AddressModel add = new AddressModel();
                    add = addressList.get(k);
                    if (business.getBuss_icon_type()==5 && business.getIsUnlocked()==1){
                        final Marker marker = mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(Double.parseDouble(add.getBuss_lat()), Double.parseDouble(add.getBuss_long())))
                                .title("")
                                // .snippet(String.valueOf(business.getBuss_icon_type()))
                                .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(business.getBuss_name(), business.getBuss_contact(), add.getBuss_address(),business.getBuss_rate()))));
                    }
                    else if (business.getBuss_icon_type()==7 && business.getIsUnlocked()==1){
                        ArrayList<OffersModel> offers=new ArrayList<>();
                        offers=business.getBusinessofferList();
                        String offerName="";
                        if (!offers.isEmpty()) {
                            OffersModel offersModel=new OffersModel();
                            offersModel=offers.get(0);
                            offerName=offersModel.getOffer_title();
                            final Marker marker = mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(Double.parseDouble(add.getBuss_lat()), Double.parseDouble(add.getBuss_long())))
                                    .title("")
                                    // .snippet(String.valueOf(business.getBuss_icon_type()))
                                    .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromViewOffer(business.getBuss_name(), offerName,business.getBuss_contact(),add.getBuss_address(),business.getBuss_rate()))));
                        }else {
                            final Marker marker = mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(Double.parseDouble(add.getBuss_lat()), Double.parseDouble(add.getBuss_long())))
                                    .title("")
                                    // .snippet(String.valueOf(business.getBuss_icon_type()))
                                    .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(business.getBuss_name(), business.getBuss_contact(), add.getBuss_address(),business.getBuss_rate()))));
                        }
                    }
                    else {
                        try {
                            markerIconBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.demo_marker);

                            Picasso.with(getContext()).load(add.getBuss_marker_icon()).placeholder(R.drawable.demo_marker).error(R.drawable.demo_marker).into(new Target() {
                                @Override
                                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                                    markerIconBitmap = bitmap;

                                }

                                @Override
                                public void onBitmapFailed(Drawable errorDrawable) {
                                }

                                @Override
                                public void onPrepareLoad(Drawable placeHolderDrawable) {
                                    // Log.e("prepare", String.valueOf(placeHolderDrawable));
                                }
                            });

                            final Marker marker = mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(Double.parseDouble(add.getBuss_lat()), Double.parseDouble(add.getBuss_long())))
                                    .title(business.getBuss_name())
                                    // .snippet(String.valueOf(business.getBuss_icon_type()))
                                    .icon(BitmapDescriptorFactory.fromBitmap(markerIconBitmap)));

                        } catch (NumberFormatException ignore) {

                        }
                    }
                }
            }
        }
    }

    private Bitmap getMarkerBitmapFromView(String title,String phone,String add,String rating) {
        View customMarkerView = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.info_window_layout_patch, null);

        TextView tv_heading     = (TextView) customMarkerView.findViewById(R.id.tv_info_name);
        TextView tv_phone       = (TextView) customMarkerView.findViewById(R.id.tv_info_phone);
        TextView tv_addre       = (TextView) customMarkerView.findViewById(R.id.tv_info_address);
        RatingBar rb_info       = (RatingBar) customMarkerView.findViewById(R.id.rating_info);

        LayerDrawable stars = (LayerDrawable) rb_info.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);

        tv_heading.setText(title);
        tv_phone.setText(phone);
        tv_addre.setText(add);

        float ratingval=0;
        try {
            ratingval = Float.parseFloat(rating);
        }catch (NumberFormatException e){
            ratingval = 0;
        }
        rb_info.setRating(ratingval);

        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
        customMarkerView.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = customMarkerView.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        customMarkerView.draw(canvas);
        return returnedBitmap;
    }

    private Bitmap getMarkerBitmapFromViewOffer(String title,String offer,String phn,String add,String rating) {
        View customMarkerView = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.offer_window_layout, null);

        TextView tv_heading     = (TextView) customMarkerView.findViewById(R.id.tv_info_name);
        TextView tv_offer       = (TextView) customMarkerView.findViewById(R.id.tv_info_offers);
        TextView tv_phone       = (TextView) customMarkerView.findViewById(R.id.tv_info_phone);
        TextView tv_add         = (TextView) customMarkerView.findViewById(R.id.tv_info_address);
        RatingBar rb_info       = (RatingBar) customMarkerView.findViewById(R.id.rating_info);

        LayerDrawable stars = (LayerDrawable) rb_info.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);

        float ratingval=0;
        try {
            ratingval = Float.parseFloat(rating);
        }catch (NumberFormatException e){
            ratingval = 0;
        }
        rb_info.setRating(ratingval);

        tv_heading.setText(title);
        tv_offer.setText(offer);
        tv_phone.setText(phn);
        tv_add.setText(add);

        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
        customMarkerView.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = customMarkerView.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        customMarkerView.draw(canvas);
        return returnedBitmap;
    }

    public class AutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {

        ArrayList<String> _items = new ArrayList<String>();
        ArrayList<String> orig = new ArrayList<String>();

        public AutoCompleteAdapter(Context context, int resource, ArrayList<String> items) {
            super(context, resource, items);

            for (int i = 0; i < items.size(); i++) {
                orig.add(items.get(i));
            }
        }

        @Override
        public int getCount() {
            if (_items != null)
                return _items.size();
            else
                return 0;
        }

        @Override
        public String getItem(int arg0) {
            return _items.get(arg0);
        }


        @NonNull
        @Override

        public Filter getFilter() {

            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {

                    if(constraint != null)
                        Log.d("Constraints", constraint.toString());
                    FilterResults oReturn = new FilterResults();

                /*  if (orig == null){
                    for (int i = 0; i < items.size(); i++) {
                        orig.add(items.get(i));
                    }
                  }*/
                    String temp;
                    int counters = 0;
                    if (constraint != null){

                        _items.clear();
                        if (orig != null && orig.size() > 0) {
                            for(int i=0; i<orig.size(); i++)
                            {
                                temp = orig.get(i).toUpperCase();

                                if(temp.startsWith(constraint.toString().toUpperCase()))
                                {

                                    _items.add(orig.get(i));
                                    counters++;

                                }
                            }
                        }
                        Log.d("REsult size:" , String.valueOf(_items.size()));
                        /*if(counters==0)
                        {
                            _items.clear();
                            _items = orig;
                        }*/
                        oReturn.values = _items;
                        oReturn.count = _items.size();
                    }
                    return oReturn;
                }


                @SuppressWarnings("unchecked")
                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if(results != null && results.count > 0) {
                        notifyDataSetChanged();
                    }
                    else {
                        notifyDataSetInvalidated();
                    }
                }
            };
        }
    }

    public ArrayList<String> autocomplete(ArrayList<BusinessesModel> list) {
        ArrayList<String> resultList = new ArrayList<>();

        for (int i=0;i<list.size();i++){
            BusinessesModel businessModel=new BusinessesModel();
            businessModel=list.get(i);
            resultList.add(businessModel.getBuss_name());
        }

        return resultList;
    }

    private class GetCategoryList extends AsyncTask<String, String, String> {
        String result = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            isUpdated=true;
        }

        @Override
        protected String doInBackground(String... params) {
            String url = Webservice_Url + GetCategoryListB;
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);

            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("driver_id", GlobalClass.callSavedPreferences("id",getContext())));
                nameValuePairs.add(new BasicNameValuePair("business_lat", params[0]));
                nameValuePairs.add(new BasicNameValuePair("business_long", params[1]));
                /*nameValuePairs.add(new BasicNameValuePair("business_lat", "38.0323672"));
                nameValuePairs.add(new BasicNameValuePair("business_long", "-121.8839713"));*/

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                HttpResponse response = httpclient.execute(httppost);

                result = EntityUtils.toString(response.getEntity());


            } catch (IOException ignored) {

            }

            return result;
        }

        @Override
        protected void onPostExecute(String responseStr) {
            super.onPostExecute(responseStr);
            //  if (dialog.isShowing()) dialog.dismiss();
            if (responseStr.length() > 0 && isAdded() && getActivity()!=null) {
                data=responseStr;
                google_Markers = new ArrayList<BusinessesModel>();
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
                  //Log.e("Google places", googlePlacesData);
            } catch (Exception e) {
                Log.d("Google Place Read Task", e.toString());
            }
            return googlePlacesData;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                if (isAdded() && getActivity()!=null) {
                    final JSONObject places_Object = new JSONObject(result);
                    JSONArray resultArray = places_Object.getJSONArray("results");
                    // Log.e("Result Google place", result);
                    for (int i = 0; i < resultArray.length(); i++) {
                        JSONObject dataAtPosition = resultArray.getJSONObject(i);
                        JSONObject geometry = dataAtPosition.getJSONObject("geometry");
                        JSONObject locationPoints = geometry.getJSONObject("location");
                        Double latpoint = locationPoints.getDouble("lat");
                        Double longpoint = locationPoints.getDouble("lng");

                        String businessIcon = dataAtPosition.getString("icon");
                        String businessId = dataAtPosition.getString("place_id");
                        String businessName = dataAtPosition.getString("name");
                        String businessVicinity = dataAtPosition.getString("vicinity");

                        JSONArray businessTypeArray = dataAtPosition.getJSONArray("types");

                        String business_Type = businessTypeArray.getString(0);
                        if (GoogleBusiness.contains(business_Type)) {
                            //int isAvailable=0;
                            BusinessesModel reportModel = new BusinessesModel();
                            reportModel.setBuss_id(businessId);
                            reportModel.setBuss_name(businessName);
                            reportModel.setBuss_img(businessIcon);
                            reportModel.setCat_name(correctCategoryName(business_Type));
                            reportModel.setBuss_from(1);
                            reportModel.setIsUnlocked(0);
                            reportModel.setBuss_lat(String.valueOf(latpoint));
                            reportModel.setBuss_long(String.valueOf(longpoint));
                            reportModel.setBuss_marker_icon(ProfilePic);
                            reportModel.setBuss_icon_type(0);

                            ArrayList<AddressModel> add = new ArrayList<>();
                            AddressModel addressModel = new AddressModel();
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
                            google_Markers.add(reportModel);
                        }
                    }
                    try {

                            final String nextpageToken = places_Object.getString("next_page_token");
                            if (!nextpageToken.equals("") || !nextpageToken.equals(null)) {
                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (isAdded() && getActivity() != null) {
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
                                    }
                                }, 3000);
                            }
                    } catch (Exception e) {
                        parseDataAndSet(data);

                    }
                }

            }catch (Exception ignored){

            }
        }
    }

    public void requestAdView(){
        /** Construct optional app options object to be sent with configure */
        AdColonyAppOptions app_options = new AdColonyAppOptions()
                .setUserID(GlobalClass.callSavedPreferences("id",getContext()));
        AdColony.configure(getActivity(), app_options, APP_ID_ADCOLONY, ZONE_ID_IN_MAP);

        /** Instantiate ad specific listener for native ad view callbacks */
        ad_listener = new AdColonyNativeAdViewListener()
        {
            @Override
            public void onRequestFilled( AdColonyNativeAdView view ) {
                ad_view = view;
                ad_layout.removeAllViews();
                ad_layout.addView(ad_view);
                /*Toast.makeText( getActivity(),
                        "Instant-Feed ad added to layout. Scroll through your feed to find it.",
                        Toast.LENGTH_LONG).show();*/

                if (ad_view.isEngagementEnabled()) {
                    AdColonyNativeAdView.EngagementButton engagement_button = ad_view.getEngagementButton();
                    //ad_layout.addView( engagement_button );
                }
            }

            @Override
            public void onMuted( AdColonyNativeAdView view )
            {

            }

            @Override
            public void onUnmuted( AdColonyNativeAdView view )
            {

            }

            @Override
            public void onRequestNotFilled( AdColonyZone zone )
            {
                Log.d( "Tag", "Not Filled" );
            }

            @Override
            public void onNativeVideoFinished( AdColonyNativeAdView view )
            {
               // Log.d( "Tag", "onNativeVideoFinished" );
                AdColony.requestNativeAdView(ZONE_ID_IN_MAP, ad_listener, AdColonyAdSize.MEDIUM_RECTANGLE, ad_options );
            }
        };

        AdColonyUserMetadata metadata = new AdColonyUserMetadata()
                .setUserAge(26)
                .setUserEducation( AdColonyUserMetadata.USER_EDUCATION_BACHELORS_DEGREE )
                .setUserGender( AdColonyUserMetadata.USER_MALE );
        ad_options = new AdColonyAdOptions().setUserMetadata( metadata );
        AdColony.requestNativeAdView( ZONE_ID_IN_MAP, ad_listener, AdColonyAdSize.MEDIUM_RECTANGLE, ad_options );
    }


    @Override
    public void onResume() {
        super.onResume();
        mapFragment.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
        mapFragment.onPause();
        if(mGoogleApiClient != null && mGoogleApiClient.isConnected()){

            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapFragment.onDestroy();
        if (ad_view != null)
        {
            ad_view.destroy();
            ad_layout.removeView( ad_view );
            ad_view = null;
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapFragment.onLowMemory();
    }
}
