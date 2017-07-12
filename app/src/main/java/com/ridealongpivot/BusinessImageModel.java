package com.ridealongpivot;

import android.os.Parcel;
import android.os.Parcelable;

public class BusinessImageModel implements Parcelable {

    String buss_id,buss_images,buss_image_id,buss_video,buss_img_type,buss_video_available;


    public BusinessImageModel(){

    }

   /* public ReportModel(int imageId, String title_pranayama, String subtitle_pranayama, int position) {
        this.imageId = imageId;
        this.title_pranayama = title_pranayama;
        this.subtitle_pranayama = subtitle_pranayama;
        this.position = position;
    }*/

    protected BusinessImageModel(Parcel in) {

        buss_id = in.readString();
        buss_images = in.readString();
        buss_image_id = in.readString();
        buss_video = in.readString();
        buss_img_type = in.readString();
        buss_video_available = in.readString();

    }

    public static final Creator<BusinessImageModel> CREATOR = new Creator<BusinessImageModel>() {
        @Override
        public BusinessImageModel createFromParcel(Parcel in) {
            return new BusinessImageModel(in);
        }

        @Override
        public BusinessImageModel[] newArray(int size) {
            return new BusinessImageModel[size];
        }
    };



    public String getBuss_id() {
        return buss_id;
    }
    public void setBuss_id(String buss_id) {
        this.buss_id = buss_id;
    }

    public String getBuss_images() {
        return buss_images;
    }
    public void setBuss_images(String buss_images) {
        this.buss_images = buss_images;
    }

    public String getBuss_image_id() {
        return buss_image_id;
    }
    public void setBuss_image_id(String buss_image_id) {
        this.buss_images = buss_image_id;
    }

    public String getBuss_video() {
        return buss_video;
    }
    public void setBuss_video(String buss_video) {
        this.buss_video = buss_video;
    }

    public String getBuss_img_type() {
        return buss_img_type;
    }
    public void setBuss_img_type(String buss_img_type) {
        this.buss_img_type = buss_img_type;
    }

    public String getBuss_video_available() {
        return buss_video_available;
    }
    public void setBuss_video_available(String buss_video_available) {
        this.buss_video_available= buss_video_available;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(buss_id);
        dest.writeString(buss_images);
        dest.writeString(buss_image_id);
        dest.writeString(buss_video);
        dest.writeString(buss_img_type);
        dest.writeString(buss_video_available);
    }
}
