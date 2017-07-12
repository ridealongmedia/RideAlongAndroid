package com.ridealongpivot;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class BusinessesModel implements Parcelable {

    private String imageId,img_name,img_lat,img_long;
    private String image_url,createDate;
    private String tag_id;

    private String cat_id,cat_name,cat_img;
    private String buss_id,buss_cat_id,buss_name,buss_con_name,buss_contact,buss_img,buss_time_to,buss_time_from,buss_site,buss_insata,buss_fb,buss_twitter,buss_snapchat,buss_video,buss_rate,buss_email;
    private String buss_twit_feed,buss_fb_feed,buss_insta_feed;
    private int buss_feed_type,buss_video_type;
    private String buss_address,buss_lat,buss_long;
    private int buss_icon_type;
    private String buss_marker_icon;
    private int dayStatus,socialFeeds,buss_from,isUnlocked;

    private String authName,authReview,authReviewTime;
    private double authRating;

    ArrayList<AddressModel> listAddress;
    ArrayList<OffersModel> businessofferList;
    ArrayList<BusinessImageModel> businessImages;

    private boolean isSelected;

    public BusinessesModel(){

    }


    protected BusinessesModel(Parcel in) {
        imageId = in.readString();
        img_name = in.readString();
        img_lat = in.readString();
        img_long = in.readString();
        image_url = in.readString();
        createDate = in.readString();
        tag_id = in.readString();
        cat_id = in.readString();
        cat_name = in.readString();
        cat_img = in.readString();
        buss_id = in.readString();
        buss_cat_id = in.readString();
        buss_name = in.readString();
        buss_con_name = in.readString();
        buss_contact = in.readString();
        buss_img = in.readString();
        buss_time_to = in.readString();
        buss_time_from = in.readString();
        buss_site = in.readString();
        buss_insata = in.readString();
        buss_fb = in.readString();
        buss_twitter = in.readString();
        buss_snapchat = in.readString();
        buss_video = in.readString();
        buss_rate = in.readString();
        buss_email = in.readString();
        buss_address = in.readString();
        buss_lat = in.readString();
        buss_long = in.readString();
        buss_marker_icon = in.readString();
        buss_icon_type = in.readInt();
        dayStatus = in.readInt();
        socialFeeds = in.readInt();
        buss_from = in.readInt();

        authName   = in.readString();
        authReview = in.readString();
        authReviewTime = in.readString();
        authRating = in.readDouble();

        buss_twit_feed=in.readString();
        buss_fb_feed=in.readString();
        buss_insta_feed=in.readString();

        buss_feed_type=in.readInt();
        buss_video_type=in.readInt();
        isUnlocked=in.readInt();

        listAddress = in.createTypedArrayList(AddressModel.CREATOR);
        businessofferList = in.createTypedArrayList(OffersModel.CREATOR);
        businessImages = in.createTypedArrayList(BusinessImageModel.CREATOR);
        isSelected = in.readByte() != 0;
    }

    public static final Creator<BusinessesModel> CREATOR = new Creator<BusinessesModel>() {
        @Override
        public BusinessesModel createFromParcel(Parcel in) {
            return new BusinessesModel(in);
        }

        @Override
        public BusinessesModel[] newArray(int size) {
            return new BusinessesModel[size];
        }
    };

    public String getCat_id() {
        return cat_id;
    }
    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public String getCat_name() {
        return cat_name;
    }
    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }

    public String getCat_img() {
        return cat_img;
    }
    public void setCat_img(String cat_img) {
        this.cat_img = cat_img;
    }

    public String getBuss_id() {
        return buss_id;
    }
    public void setBuss_id(String buss_id) {
        this.buss_id = buss_id;
    }

    public String getBuss_cat_id() {
        return buss_cat_id;
    }
    public void setBuss_cat_id(String buss_cat_id) {
        this.buss_cat_id = buss_cat_id;
    }

    public String getBuss_name() {
        return buss_name;
    }
    public void setBuss_name(String buss_name) {
        this.buss_name = buss_name;
    }

    public String getBuss_con_name() {
        return buss_con_name;
    }
    public void setBuss_con_name(String buss_con_name) {
        this.buss_con_name = buss_con_name;
    }

    public String getBuss_contact() {
        return buss_contact;
    }
    public void setBuss_contact(String buss_contact) {
        this.buss_contact = buss_contact;
    }

    public String getBuss_img() {
        return buss_img;
    }
    public void setBuss_img(String buss_img) {
        this.buss_img = buss_img;
    }

    public String getBuss_time_from() {
        return buss_time_from;
    }
    public void setBuss_time_from(String buss_time_from) {
        this.buss_time_from = buss_time_from;
    }

    public String getBuss_time_to() {
        return buss_time_to;
    }
    public void setBuss_time_to(String buss_time_to) {
        this.buss_time_to = buss_time_to;
    }

    public String getBuss_site() {
        return buss_site;
    }
    public void setBuss_site(String buss_site) {
        this.buss_site = buss_site;
    }

    public String getBuss_insata() {
        return buss_insata;
    }
    public void setBuss_insata(String buss_insata) {
        this.buss_insata = buss_insata;
    }

    public String getBuss_fb() {
        return buss_fb;
    }
    public void setBuss_fb(String buss_fb) {
        this.buss_fb = buss_fb;
    }

    public String getBuss_twitter() {
        return buss_twitter;
    }
    public void setBuss_twitter(String buss_twitter) {
        this.buss_twitter = buss_twitter;
    }

    public String getBuss_snapchat() {
        return buss_snapchat;
    }
    public void setBuss_snapchat(String buss_snapchat) {
        this.buss_snapchat = buss_snapchat;
    }

    public String getBuss_video() {
        return buss_video;
    }
    public void setBuss_video(String buss_video) {
        this.buss_video = buss_video;
    }

    public String getBuss_rate() {
        return buss_rate;
    }
    public void setBuss_rate(String buss_rate) {
        this.buss_rate = buss_rate;
    }

    public String getBuss_email() {
        return buss_email;
    }
    public void setBuss_email(String buss_email) {
        this.buss_email = buss_email;
    }

    public String getBuss_address() {
        return buss_address;
    }
    public void setBuss_address(String buss_address) {
        this.buss_address = buss_address;
    }

    public String getBuss_lat() {
        return buss_lat;
    }
    public void setBuss_lat(String buss_lat) {
        this.buss_lat = buss_lat;
    }

    public String getBuss_long() {
        return buss_long;
    }
    public void setBuss_long(String buss_long) {
        this.buss_long = buss_long;
    }

    public int getBuss_icon_type() {
        return buss_icon_type;
    }
    public void setBuss_icon_type(int buss_icon_type) {
        this.buss_icon_type = buss_icon_type;
    }

    public int getDayStatus() {
        return dayStatus;
    }
    public void setDayStatus(int dayStatus) {
        this.dayStatus = dayStatus;
    }

    public int getSocialFeeds() {
        return socialFeeds;
    }
    public void setSocialFeeds(int socialFeeds) {
        this.socialFeeds = socialFeeds;
    }

    public int getBuss_from() {
        return buss_from;
    }
    public void setBuss_from(int buss_from) {
        this.buss_from = buss_from;
    }

    public String getAuthReview() {
        return authReview;
    }
    public void setAuthReview(String authReview) {
        this.authReview = authReview;
    }

    public String getAuthReviewTime() {
        return authReviewTime;
    }
    public void setAuthReviewTime(String authReviewTime) {
        this.authReviewTime = authReviewTime;
    }

    public String getAuthName() {
        return authName;
    }
    public void setAuthName(String authName) {
        this.authName = authName;
    }

    public String getBuss_marker_icon() {
        return buss_marker_icon;
    }
    public void setBuss_marker_icon(String buss_marker_icon) {
        this.buss_marker_icon = buss_marker_icon;
    }

    public double getAuthRating() {
        return authRating;
    }
    public void setAuthRating(double authRating) {
        this.authRating = authRating;
    }

    public ArrayList<AddressModel> getListAddress() {
        return listAddress;
    }
    public void setListAddress(ArrayList<AddressModel> listAddress) {
        this.listAddress = listAddress;
    }


    public ArrayList<OffersModel> getBusinessofferList() {
        return businessofferList;
    }
    public void setBusinessofferList(ArrayList<OffersModel> businessofferList) {
        this.businessofferList = businessofferList;
    }

    public ArrayList<BusinessImageModel> getBusinessImages() {
        return businessImages;
    }
    public void setBusinessImages(ArrayList<BusinessImageModel> businessImages) {
        this.businessImages = businessImages;
    }

    public String getBuss_twit_feed() {
        return buss_twit_feed;
    }
    public void setBuss_twit_feed(String buss_twit_feed) {
        this.buss_twit_feed = buss_twit_feed;
    }

    public String getBuss_fb_feed() {
        return buss_fb_feed;
    }
    public void setBuss_fb_feed(String buss_fb_feed) {
        this.buss_fb_feed = buss_fb_feed;
    }

    public String getBuss_insta_feed() {
        return buss_insta_feed;
    }
    public void setBuss_insta_feed(String buss_insta_feed) {
        this.buss_insta_feed = buss_insta_feed;
    }

    public int getBuss_feed_type() {
        return buss_feed_type;
    }
    public void setBuss_feed_type(int buss_feed_type) {
        this.buss_feed_type = buss_feed_type;
    }

    public int getBuss_video_type() {
        return buss_video_type;
    }
    public void setBuss_video_type(int buss_video_type) {
        this.buss_video_type = buss_video_type;
    }

    public int getIsUnlocked() {
        return isUnlocked;
    }
    public void setIsUnlocked(int isUnlocked) {
        this.isUnlocked = isUnlocked;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imageId);
        dest.writeString(img_name);
        dest.writeString(img_lat);
        dest.writeString(img_long);
        dest.writeString(image_url);
        dest.writeString(createDate);
        dest.writeString(tag_id);
        dest.writeString(cat_id);
        dest.writeString(cat_name);
        dest.writeString(cat_img);
        dest.writeString(buss_id);
        dest.writeString(buss_cat_id);
        dest.writeString(buss_name);
        dest.writeString(buss_con_name);
        dest.writeString(buss_contact);
        dest.writeString(buss_img);
        dest.writeString(buss_time_to);
        dest.writeString(buss_time_from);
        dest.writeString(buss_site);
        dest.writeString(buss_insata);
        dest.writeString(buss_fb);
        dest.writeString(buss_twitter);
        dest.writeString(buss_snapchat);
        dest.writeString(buss_video);
        dest.writeString(buss_rate);
        dest.writeString(buss_email);
        dest.writeString(buss_address);
        dest.writeString(buss_lat);
        dest.writeString(buss_long);
        dest.writeInt(buss_icon_type);
        dest.writeInt(dayStatus);
        dest.writeInt(socialFeeds);
        dest.writeInt(buss_from);
        dest.writeString(authName);
        dest.writeString(authReview);
        dest.writeString(authReviewTime);
        dest.writeString(buss_marker_icon);
        dest.writeString(buss_twit_feed);
        dest.writeString(buss_fb_feed);
        dest.writeString(buss_insta_feed);
        dest.writeInt(buss_feed_type);
        dest.writeInt(buss_video_type);
        dest.writeInt(isUnlocked);
        dest.writeDouble(authRating);
        dest.writeTypedList(listAddress);
        dest.writeTypedList(businessofferList);
        dest.writeTypedList(businessImages);
        dest.writeByte((byte) (isSelected ? 1 : 0));
    }
}
