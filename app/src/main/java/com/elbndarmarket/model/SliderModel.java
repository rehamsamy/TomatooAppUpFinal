package com.elbndarmarket.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SliderModel {

    @SerializedName("status")
    private Boolean status;
    @SerializedName("slider")
    private List<Slider> slider;

    // Getter ...
    public Boolean getStatus() {
        return status;
    }

    public List<Slider> getSlider() {
        return slider;
    }


    // Class Slider ...
    public static class Slider {
        @SerializedName("Slider_href")
        private String Slider_href;
        @SerializedName("slider_image")
        private String slider_image;


        //Getter ...
        public String getSlider_href() {
            return Slider_href;
        }

        public String getSlider_image() {
            return slider_image;
        }
    }

}
