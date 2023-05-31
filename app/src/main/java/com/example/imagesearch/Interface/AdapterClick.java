package com.example.imagesearch.Interface;

import com.example.imagesearch.Model.Color;
import com.example.imagesearch.Model.Hits;

public interface AdapterClick {
    void itemClick(Hits hits, int position);
    void itemImageClick(String string, int position);

    void itemColorClick(Color color, int position);

}
