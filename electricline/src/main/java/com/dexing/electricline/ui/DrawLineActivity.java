package com.dexing.electricline.ui;

import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.dexing.electricline.R;

import butterknife.BindView;
import butterknife.OnClick;

public class DrawLineActivity extends BaseActivity implements AMap.OnMapClickListener {

    private String TAG = DrawLineActivity.class.getSimpleName();
    private AMap aMap;
    private Marker marker;

    @BindView(R.id.map)
    MapView mapView;
    @BindView(R.id.img_center)
    ImageView img_center;
    @BindView(R.id.btnOK)
    Button btnOK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_line);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        init();

    }

    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
        }
        aMap.setOnMapClickListener(this);
        aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(37.060657, 118.450843), 45));

    }

    @Override
    public void onMapClick(LatLng latLng) {
        Log.i(TAG, "onMapClick: " + latLng.latitude + " " + latLng.longitude);
    }

    @OnClick({R.id.btnOK})
    void viewOnClick(View view) {
        switch (view.getId()) {
            case R.id.btnOK:
                int x = img_center.getLeft() + img_center.getWidth() / 2;
                int y = img_center.getBottom();
                toGeoLocation(x, y);
                break;
        }
    }

    private void toGeoLocation(int x, int y) {
        Point mPoint = new Point(x, y);
        LatLng mLatlng = aMap.getProjection().fromScreenLocation(mPoint);
        if (mLatlng != null) {
            marker = aMap.addMarker(new MarkerOptions().position(mLatlng));
        }
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mapView != null) {
            mapView.onDestroy();
        }
    }
}
