package com.example.imagesearch.Interface;

import com.example.imagesearch.Model.Response;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MyApi {
    @GET("?key=665810-5a038a5dea3b9e94510de87d5")
    Call<Response> getImages(@Query("page") int page,
                            @Query("per_page") int per_page,
                            @Query("q") String query,
                            @Query("orientation") String orientation,
                            @Query("category") String category,
                            @Query("min_width") int min_width,
                            @Query("min_height") int min_height,
                            @Query("colors") String color,
                             @Query("order") String order
    );
}