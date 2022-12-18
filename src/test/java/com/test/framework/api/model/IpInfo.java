package com.test.framework.api.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class IpInfo {
    private String ip;
    private String type;
    @SerializedName("continent_code")
    private String continentCode;
    @SerializedName("continent_name")
    private String continentName;
    @SerializedName("country_code")
    private String countryCode;
    @SerializedName("country_name")
    private String countryName;
    @SerializedName("region_code")
    private String regionCode;
    @SerializedName("region_name")
    private String regionName;
    private String city;
    private String zip;
    private double latitude;
    private double longitude;
    private Location location;
}
