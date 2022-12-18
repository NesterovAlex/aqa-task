package com.test.framework.api.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class Language {
    private String code;
    private String name;
    @SerializedName("native")
    private String nativeName;
}
