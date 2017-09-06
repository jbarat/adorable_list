package uk.co.jbarat.data.user;

import com.google.gson.annotations.SerializedName;

class GeoLocationResponse {
    @SerializedName("lat")
    private String latitude;
    @SerializedName("lng")
    private String longitude;

    GeoLocationResponse(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }
}
