package com.example.imagesearch.Model;

public class Color {
    String name;
    Integer colorInt;




    public Color(String name, Integer colorInt) {
        this.name = name;
        this.colorInt = colorInt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getColorInt() {
        return colorInt;
    }

    public void setColorInt(Integer colorInt) {
        this.colorInt = colorInt;
    }

}
