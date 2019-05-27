package com.example.hetkataria.alarmapp.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

import com.example.hetkataria.alarmapp.model.AutocompleteCity;
import com.example.hetkataria.alarmapp.GoogleAutocompleteAPI;
import com.example.hetkataria.alarmapp.model.Place;
import com.example.hetkataria.alarmapp.adapter.PlaceAdapter;
import com.example.hetkataria.alarmapp.PlaceNameInterface;
import com.example.hetkataria.alarmapp.R;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PlacePickerActivity extends AppCompatActivity implements PlaceNameInterface {

    private List<Place> placeList = new ArrayList<>();
    private RecyclerView recyclerView;
    private PlaceAdapter placeAdapter;

    private Retrofit retrofit;


    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_picker);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1874CD")));
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        recyclerView = findViewById(R.id.recycler_view2);

        placeAdapter = new PlaceAdapter(placeList,this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(placeAdapter);

        //editText.addTextChangedListener(filterTextWatcher);

        retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://maps.googleapis.com/maps/api/place/autocomplete/")
                .build();
    }

    private TextWatcher filterTextWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            placeList.clear();
            RetrofitCall(s.toString());
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    private void RetrofitCall(String input){

        retrofit.create(GoogleAutocompleteAPI.class).getCity(input)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AutocompleteCity>() {

                    @Override
                    public final void onError(Throwable e) {
                        Log.e("uh oh", e.getMessage());
                    }

                    @Override
                    public final void onNext(AutocompleteCity response) {
                        for(AutocompleteCity.Predict predict: response.Predictions){
                            Log.d("ok",predict.description);
                            Place place = new Place(predict.description);
                            placeList.add(place);
                            placeAdapter.notifyDataSetChanged();
                            Log.d("ok",placeList.toString());
                        }
                    }

                    @Override
                    public final void onSubscribe(Disposable disposable){ }

                    @Override
                    public final void onComplete(){ }
                });
        }

    @Override
    public void onRowClick(String placeName){
        Intent i = new Intent();
        i.putExtra("placeName",placeName);
        setResult(1,i);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconified(false);
        searchView.setQueryHint("Search...");
        searchView.setMaxWidth(Integer.MAX_VALUE);

        Drawable yourdrawable = menu.getItem(0).getIcon(); // change 0 with 1,2 ...
        yourdrawable.mutate();
        yourdrawable.setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_IN);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String query) {
                placeList.clear();
                RetrofitCall(query);
                return true;
            }
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
        });

        return true;

    }
    public void doThis(MenuItem item){
        Intent i = new Intent();
        setResult(2,i);
        finish();

    }

}
