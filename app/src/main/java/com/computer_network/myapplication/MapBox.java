package com.computer_network.myapplication;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.offline.OfflineTilePyramidRegionDefinition;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.utils.BitmapUtils;

public class MapBox extends AppCompatActivity {

    Mapbox mapboxMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this,getResources().getString(R.string.mapbox_access_token));
        setContentView(R.layout.activity_map_box);

        double latitudeNorth = 40.6136;  // Northern boundary of Missouri
        double longitudeEast = -89.0988;  // Eastern boundary of Missouri
        double latitudeSouth = 35.9957;  // Southern boundary of Missouri
        double longitudeWest = -95.7747;  // Western boundary of Missouri

        MapView mapView = findViewById(R.id.mapView);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull com.mapbox.mapboxsdk.maps.MapboxMap mapboxMap) {
                mapboxMap.setStyle(Style.MAPBOX_STREETS, new com.mapbox.mapboxsdk.maps.Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull com.mapbox.mapboxsdk.maps.Style style) {
                        style.addImage("red-pin-icon-id", BitmapUtils.getBitmapFromDrawable(getResources().getDrawable(R.drawable.baseline_location_on_24)));

                        style.addLayer(new SymbolLayer("icon-layer-id","icon-source-id").
                                withProperties(iconImage("red-pin-icon-id"),
                                        iconIgnorePlacement(true),
                                        iconAllowOverlap(true),
                                        iconOffset(new Float[]{0f,-9f})


                                ));

                        GeoJsonSource iconGeoJsonSource = new GeoJsonSource("icon-source-id", Feature.fromGeometry(Point.fromLngLat(107.6345122,-6.9249233)));
                        style.addSource(iconGeoJsonSource);
                        mapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-6.9249233,107.6345122),15.7));
                    }
                });
            }
        });
    }
}