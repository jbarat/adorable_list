package uk.co.jbarat.data.user.response;

import com.google.gson.annotations.SerializedName;

class CompanyResponse {
    private String name;
    private String catchPhrase;
    @SerializedName("bs")
    private String bullShit;

    public CompanyResponse(String name, String catchPhrase, String bullShit) {
        this.name = name;
        this.catchPhrase = catchPhrase;
        this.bullShit = bullShit;
    }

    public String getName() {
        return name;
    }

    public String getCatchPhrase() {
        return catchPhrase;
    }

    public String getBullShit() {
        return bullShit;
    }
}
