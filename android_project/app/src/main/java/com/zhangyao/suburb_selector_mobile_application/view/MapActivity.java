package com.zhangyao.suburb_selector_mobile_application.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.mapbox.android.gestures.MoveGestureDetector;
import com.mapbox.geojson.Point;
import com.mapbox.maps.CameraOptions;
import com.mapbox.maps.MapView;
import com.mapbox.maps.MapboxMap;
import com.mapbox.maps.Style;
import com.mapbox.maps.plugin.Plugin;
import com.mapbox.maps.plugin.animation.CameraAnimationsPlugin;
import com.mapbox.maps.plugin.animation.MapAnimationOptions;
import com.mapbox.maps.plugin.attribution.AttributionPlugin;
import com.mapbox.maps.plugin.compass.CompassPlugin;
import com.mapbox.maps.plugin.gestures.GesturesPlugin;
import com.mapbox.maps.plugin.gestures.OnMoveListener;
import com.mapbox.maps.plugin.locationcomponent.LocationComponentPlugin;
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener;
import com.mapbox.maps.plugin.logo.LogoPlugin;
import com.mapbox.maps.plugin.scalebar.ScaleBarPlugin;
import com.zhangyao.suburb_selector_mobile_application.R;
import com.zhangyao.suburb_selector_mobile_application.utils.UIutils;

import org.jetbrains.annotations.NotNull;

public class MapActivity extends AppCompatActivity {
    MapView mMapView;
    MapboxMap mMapboxMap;
    Context context;
    BottomSheetBehavior mBottomSheetBehavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, universities);
        AutoCompleteTextView textView = (AutoCompleteTextView)
                findViewById(R.id.selectUniversity);
        textView.setAdapter(adapter);

        View form = findViewById(R.id.enterLocationsPanel);
        mBottomSheetBehavior = BottomSheetBehavior.from(form);
        context = MapActivity.this;
        mMapView = findViewById(R.id.mapView);
        mMapboxMap = mMapView.getMapboxMap();
        mMapboxMap.loadStyleUri(Style.MAPBOX_STREETS, style -> {
            //map ready
            UIutils.toast(context, "map ready!");
            initPlugin();

        });


    }
    public void submit_preferences(View view) {
        startActivity(new Intent(MapActivity.this, MapActivity.class));
        finish();
    }
    private static final String[] universities = new String[] {
            "Belgium", "France", "Italy", "Germany", "Spain"
    };

    private void initPlugin() {
        LogoPlugin logoPlugin = mMapView.getPlugin(Plugin.MAPBOX_LOGO_PLUGIN_ID);

        if (logoPlugin != null) {
            logoPlugin.setEnabled(true);
        }
        AttributionPlugin attributionPlugin = mMapView.getPlugin(Plugin.MAPBOX_ATTRIBUTION_PLUGIN_ID);
        if (attributionPlugin != null) {
            attributionPlugin.setEnabled(true);
        }
        ScaleBarPlugin scaleBarPlugin = mMapView.getPlugin(Plugin.MAPBOX_SCALEBAR_PLUGIN_ID);
        if (scaleBarPlugin != null) {
            scaleBarPlugin.setEnabled(true);
        }
        CompassPlugin compassPlugin = mMapView.getPlugin(Plugin.MAPBOX_COMPASS_PLUGIN_ID);
        if (compassPlugin != null) {
            compassPlugin.setEnabled(true);
            // compassPlugin.setImage(drawable);	// change compass icon
        }

        GesturesPlugin gesturesPlugin = mMapView.getPlugin(Plugin.MAPBOX_GESTURES_PLUGIN_ID);
        if (gesturesPlugin != null) {
            gesturesPlugin.addOnMoveListener(onMoveListener);
        }
        LocationComponentPlugin locationPlugin = mMapView.getPlugin(Plugin.MAPBOX_LOCATION_COMPONENT_PLUGIN_ID);
        locationPlugin.updateSettings(locationComponentSettings -> {
            locationComponentSettings.setEnabled(true);
            locationComponentSettings.setPulsingEnabled(true);
            return null;
        });
// add listener of user's location

        locationPlugin.addOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener);


    }

    // user's location changing listener
    private final OnIndicatorPositionChangedListener onIndicatorPositionChangedListener =
            point -> {
                // point is user's location
                Log.e("TAG", "onIndicatorPositionChanged: " + point.latitude());
                moveCameraTo(point,9,2);
            };
    // map movement listener
    private final OnMoveListener onMoveListener = new OnMoveListener() {
        @Override
        public void onMoveBegin(@NotNull MoveGestureDetector moveGestureDetector) {
            // move start
            UIutils.toast(context, "move start");
        }

        @Override
        public boolean onMove(@NotNull MoveGestureDetector moveGestureDetector) {
            //  UIutils.toast(context,"move ing");
            return false;
        }

        @Override
        public void onMoveEnd(@NotNull MoveGestureDetector moveGestureDetector) {
            // stop moving
            UIutils.toast(context, "move stop");
        }
    };

    public void showPanel(View view) {
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    /**
     *
     *
     * @param point    change the coordinator of target
     * @param zoom     target scale ratio
     * @param duration
     */
    private void moveCameraTo(Point point, double zoom, int duration) {
        if (mMapView == null) {
            return;
        }
        CameraAnimationsPlugin cameraAnimationsPlugin = mMapView.getPlugin(Plugin.MAPBOX_CAMERA_PLUGIN_ID);
        if (duration != 0 && cameraAnimationsPlugin != null) {
            cameraAnimationsPlugin.flyTo(new CameraOptions.Builder()
                            .center(point)
                            .zoom(zoom)
                            .build(),
                    new MapAnimationOptions.Builder().duration(duration).build());
        } else {
            mMapboxMap.setCamera(new CameraOptions.Builder()
                    .center(point)
                    .zoom(zoom)
                    .build());
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();

    }


}