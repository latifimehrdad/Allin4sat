package ir.bppir.allin4sat.models;

import com.google.gson.annotations.SerializedName;

public class MD_Education {

    @SerializedName("Id")
    Integer Id;

    @SerializedName("Title")
    String Title;

    @SerializedName("Resource")
    MD_Resource Resource;


    public MD_Education(Integer id, String title, MD_Resource resource) {
        Id = id;
        Title = title;
        Resource = resource;
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public MD_Resource getResource() {
        return Resource;
    }

    public void setResource(MD_Resource resource) {
        Resource = resource;
    }
}
