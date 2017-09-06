package uk.co.jbarat.data.user;

import com.google.gson.annotations.SerializedName;

class UserResponse {
    private int id;
    private String name;
    @SerializedName("username")
    private String userName;
    private String email;
    private AddressResponse address;
    private String phone;
    private String website;
    private CompanyResponse company;

    public UserResponse(int id, String name, String userName, String email, AddressResponse address, String phone, String website, CompanyResponse company) {
        this.id = id;
        this.name = name;
        this.userName = userName;
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.website = website;
        this.company = company;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public AddressResponse getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getWebsite() {
        return website;
    }

    public CompanyResponse getCompany() {
        return company;
    }
}
