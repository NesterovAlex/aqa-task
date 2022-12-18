package com.test.framework.api.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class Location {
    @SerializedName("geoname_id")
    private int geonameId;
    private String capital;
    private Language[] languages;
    @SerializedName("country_flag")
    private String countryFlag;
    @SerializedName("country_flag_emoji")
    private String countryFlagEmoji;
    @SerializedName("country_flag_emoji_unicode")
    private String countryFlagEmojiUnicode;
    @SerializedName("calling_code")
    private String callingCode;
    @SerializedName("is_eu")
    private boolean isEu;
}

