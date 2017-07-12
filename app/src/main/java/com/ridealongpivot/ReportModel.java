package com.ridealongpivot;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class ReportModel implements Parcelable {

    private String sno;
    private String id;

    private String provider_name,provider_level;

    private String imageId,img_name,img_lat,img_long;
    private String image_url,createDate;
    private String tag_id;

    private String cat_id,cat_name,cat_img;
    private String buss_id,buss_cat_id,buss_name,buss_con_name,buss_contact,buss_img;
    private String buss_address,buss_lat,buss_long;
    private int buss_icon_type;
    private String buss_marker_icon;
    private int dayStatus,socialFeeds,buss_from;

  //  ArrayList<AddressModel> listAddress;
    ArrayList<BusinessesModel> listBusiness;
  //  ArrayList<OffersModel> businessofferList;

    private boolean isSelected;

    public ReportModel(){

    }

   /* public ReportModel(int imageId, String title_pranayama, String subtitle_pranayama, int position) {
        this.imageId = imageId;
        this.title_pranayama = title_pranayama;
        this.subtitle_pranayama = subtitle_pranayama;
        this.position = position;
    }*/

    protected ReportModel(Parcel in) {
        sno = in.readString();
        id = in.readString();
        provider_name = in.readString();
        provider_level = in.readString();
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
        buss_address = in.readString();
        buss_lat = in.readString();
        buss_long = in.readString();

        buss_marker_icon = in.readString();
        buss_icon_type = in.readInt();
        dayStatus = in.readInt();
        socialFeeds = in.readInt();
        buss_from = in.readInt();

        listBusiness = in.createTypedArrayList(BusinessesModel.CREATOR);
        isSelected = in.readByte() != 0;
    }

    public static final Creator<ReportModel> CREATOR = new Creator<ReportModel>() {
        @Override
        public ReportModel createFromParcel(Parcel in) {
            return new ReportModel(in);
        }

        @Override
        public ReportModel[] newArray(int size) {
            return new ReportModel[size];
        }
    };

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getSno() {
        return sno;
    }
    public void setSno(String sno) {
        this.sno = sno;
    }

    public String getImageId() {
        return imageId;
    }
    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getImage_url() {
        return image_url;
    }
    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getTag_id() {
        return tag_id;
    }
    public void setTag_id(String tag_id) {
        this.tag_id = tag_id;
    }

    public String getCreateDate() {
        return createDate;
    }
    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getImg_name() {
        return img_name;
    }
    public void setImg_name(String img_name) {
        this.img_name = img_name;
    }

    public String getImg_lat() {
        return img_lat;
    }
    public void setImg_lat(String img_lat) {
        this.img_lat = img_lat;
    }

    public String getImg_long() {
        return img_long;
    }
    public void setImg_long(String img_long) {
        this.img_long = img_long;
    }

    public boolean getIsSelected() {
        return isSelected;
    }
    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public String getProvider_name() {
        return provider_name;
    }
    public void setProvider_name(String provider_name) {
        this.provider_name = provider_name;
    }

    public String getProvider_level() {
        return provider_level;
    }
    public void setProvider_level(String provider_level) {
        this.provider_level = provider_level;
    }

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

    public String getBuss_marker_icon() {
        return buss_marker_icon;
    }
    public void setBuss_marker_icon(String buss_marker_icon) {
        this.buss_marker_icon = buss_marker_icon;
    }

   /* public ArrayList<AddressModel> getListAddress() {
        return listAddress;
    }
    public void setListAddress(ArrayList<AddressModel> listAddress) {
        this.listAddress = listAddress;
    }*/

    public ArrayList<BusinessesModel> getListBusiness() {
        return listBusiness;
    }
    public void setListBusiness(ArrayList<BusinessesModel> listBusiness) {
        this.listBusiness = listBusiness;
    }

   /* public ArrayList<OffersModel> getBusinessofferList() {
        return businessofferList;
    }
    public void setBusinessofferList(ArrayList<OffersModel> businessofferList) {
        this.businessofferList = businessofferList;
    }*/

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(sno);
        dest.writeString(id);
        dest.writeString(provider_name);
        dest.writeString(provider_level);
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

        dest.writeString(buss_address);
        dest.writeString(buss_lat);
        dest.writeString(buss_long);

        dest.writeInt(buss_icon_type);
        dest.writeInt(dayStatus);
        dest.writeInt(socialFeeds);
        dest.writeInt(buss_from);

        dest.writeString(buss_marker_icon);
        dest.writeTypedList(listBusiness);
        dest.writeByte((byte) (isSelected ? 1 : 0));
    }
}
