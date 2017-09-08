package uk.co.jbarat.data.user.response;

import com.google.gson.annotations.SerializedName;

class AddressResponse {
    private String street;
    private String suite;
    private String city;
    @SerializedName("zipcode")
    private String zipCode;
    @SerializedName("geo")
    private GeoLocationResponse geoLocation;

    public AddressResponse(String street, String suite, String city, String zipCode, GeoLocationResponse geoLocation) {
        this.street = street;
        this.suite = suite;
        this.city = city;
        this.zipCode = zipCode;
        this.geoLocation = geoLocation;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getSuite() {
        return suite;
    }

    public void setSuite(String suite) {
        this.suite = suite;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public GeoLocationResponse getGeoLocation() {
        return geoLocation;
    }

    public void setGeoLocation(GeoLocationResponse geoLocation) {
        this.geoLocation = geoLocation;
    }
}
