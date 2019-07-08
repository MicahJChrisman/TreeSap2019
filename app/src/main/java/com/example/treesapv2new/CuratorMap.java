package com.example.treesapv2new;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

public class CuratorMap extends Fragment {//implements OnMapReadyCallback {

    public MapView mapView;
    public ImageButton directionsButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);
    }

////    @Override
//    public void onCreate(){
//
//    }
//    @Override
//    public static CuratorMap newInstance() {
//
//        Bundle args = new Bundle();
//
//        CuratorMap fragment = new CuratorMap();
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.curator_map, container, false);
        mapView = view.findViewById(R.id.map);
        directionsButton = view.findViewById(R.id.directions_button);

        return view;
    }

    public MapView getMapView(){
        return mapView;
    }

    public ImageButton getDirectionsButton(){
        return directionsButton;
    }


//    @Override
//    public void onResume() {
//        super.onResume();
//
//        SupportMapFragment smf = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map));
//
//        smf.getMapAsync(this);
//    }
//
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//
//    }
}
