package com.ridealongpivot;

import android.os.Parcel;
import android.os.Parcelable;

public class OffersModel implements Parcelable {

    String offer_id,offer_Image,offer_title;


    public OffersModel(){

    }

   /* public ReportModel(int imageId, String title_pranayama, String subtitle_pranayama, int position) {
        this.imageId = imageId;
        this.title_pranayama = title_pranayama;
        this.subtitle_pranayama = subtitle_pranayama;
        this.position = position;
    }*/

    protected OffersModel(Parcel in) {

        offer_id = in.readString();
        offer_Image = in.readString();
        offer_title = in.readString();

    }

    public static final Creator<OffersModel> CREATOR = new Creator<OffersModel>() {
        @Override
        public OffersModel createFromParcel(Parcel in) {
            return new OffersModel(in);
        }

        @Override
        public OffersModel[] newArray(int size) {
            return new OffersModel[size];
        }
    };



    public String getOffer_id() {
        return offer_id;
    }
    public void setOffer_id(String offer_id) {
        this.offer_id = offer_id;
    }

    public String getOffer_Image() {
        return offer_Image;
    }
    public void setOffer_Image(String offer_Image) {
        this.offer_Image = offer_Image;
    }

    public String getOffer_title() {
        return offer_title;
    }
    public void setOffer_title(String offer_title) {
        this.offer_title = offer_title;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(offer_id);
        dest.writeString(offer_Image);
        dest.writeString(offer_title);

    }
}
