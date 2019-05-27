package com.example.hetkataria.alarmapp;

import com.example.hetkataria.alarmapp.model.AutocompleteCity;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GoogleAutocompleteAPI {

    @GET("json?types=(cities)&key=AIzaSyCQm9zSsQKNmsGiymVZ3NbQIjOtYRMjUs4")
    Observable<AutocompleteCity> getCity(@Query("input") String input);
}
