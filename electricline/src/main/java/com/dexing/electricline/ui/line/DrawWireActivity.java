package com.dexing.electricline.ui.line;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptor;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.LatLngBounds;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.PolylineOptions;
import com.dexing.electricline.R;
import com.dexing.electricline.model.BoxUser;
import com.dexing.electricline.model.EPoint;
import com.dexing.electricline.model.GreenDaoHelper;
import com.dexing.electricline.model.Line;
import com.dexing.electricline.model.Village;
import com.dexing.electricline.ui.BaseActivity;
import com.dexing.electricline.ui.DrawLineActivity;
import com.dexing.electricline.view.BoxMarkerView;
import com.dexing.electricline.view.MarkerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class DrawWireActivity extends BaseActivity implements AMap.OnMapClickListener,
        AMap.OnMarkerClickListener {

    @BindView(R.id.map)
    MapView mapView;

    @BindView(R.id.btnRemove)
    Button btnRemove;
    @BindView(R.id.btnAdd)
    Button btnAdd;

    private AMap aMap;
    private Marker marker;
    private Village currentVillage;
    private List<LatLng> listLatLng = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);
        mapView.onCreate(savedInstanceState);// 此方法必须重写

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            currentVillage = (Village) bundle.getSerializable("village");
            init();
        } else {
            Toast.makeText(this, "数据参数错误", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
        }
        aMap.setOnMarkerClickListener(this);
        aMap.setOnMapClickListener(this);

        setTitle(currentVillage.getName());

        List<EPoint> points = GreenDaoHelper.getInstance().getEPointsByVillageId(currentVillage.getObjectId());
        drawPointLine(points);

        getLineToDrawWire();
    }

    @OnClick({R.id.btnRemove, R.id.btnAdd})
    void viewOnClick(View view) {
        switch (view.getId()) {
            case R.id.btnRemove:
                if (listLatLng.size() > 0) {
                    listLatLng.remove(listLatLng.size() - 1);
                    drawWire();
                }
                break;
            case R.id.btnAdd:
                if (listLatLng.size() > 0) {
                    int size = listLatLng.size();
                    String latlngs = "";
                    for (int i = 0; i < size; i++) {
                        LatLng latLng = listLatLng.get(i);
                        latlngs += latLng.longitude + "," + latLng.latitude + ";";
                    }

                    if (latlngs.length() > 0) {
                        latlngs = latlngs.substring(0, latlngs.length() - 1);
                    }
                    Line line = new Line();
                    line.setVillageId(currentVillage.getObjectId());
                    line.setLinePoint(latlngs);
                    line.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e == null) {
                                Toast.makeText(DrawWireActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                                listLatLng.clear();
                            } else {
                                Toast.makeText(DrawWireActivity.this, "添加失败：" + s, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                break;
        }
    }

    private void drawPointLine(List<EPoint> list) {
        int size = list.size();
        Toast.makeText(this, size + "", Toast.LENGTH_SHORT).show();
        if (size == 0) {
            Log.i(TAG, "done: point size is 0");
            return;
        }
        LatLngBounds.Builder bounds = LatLngBounds.builder();
        for (int i = 0; i < size; i++) {
            EPoint point = list.get(i);
            if (point.getType() != 2) {
                LatLng latLng = point.getLatLng();
                bounds.include(latLng);
                MarkerOptions markerOption = getMarkerOptions(point);
                marker = aMap.addMarker(markerOption);
                marker.setObject(point);
            }
        }
        aMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 20));
    }

    private void getLineToDrawWire() {
        //获取该村节点
        BmobQuery<Line> bmobQuery = new BmobQuery<Line>();
        bmobQuery.addWhereEqualTo("VillageId", currentVillage.getObjectId());
        bmobQuery.findObjects(new FindListener<Line>() {
            @Override
            public void done(List<Line> list, BmobException e) {
                if (e == null) {
                    int size = list.size();
                    if (size != 0) {
                        for (int i = 0; i < size; i++) {
                            Line line = list.get(i);
                            String lls = line.getLinePoint();
                            String[] lglts = lls.split(";");

                            List<LatLng> latLngs = new ArrayList<LatLng>();

                            for (int j = 0; j < lglts.length; j++) {
                                String[] ll = lglts[j].split(",");
                                LatLng latLng = new LatLng(Double.parseDouble(ll[1]), Double.parseDouble(ll[0]));
                                latLngs.add(latLng);
                            }
                            aMap.addPolyline((new PolylineOptions()).addAll(latLngs).color(
                                    Color.RED));
                        }
                    }
                } else {
                    Toast.makeText(DrawWireActivity.this, "获取线路失败", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @NonNull
    private MarkerOptions getMarkerOptions(EPoint point) {
        MarkerOptions markerOption = new MarkerOptions().position(point.getLatLng());

        if (point.getType() == 0) {
            markerOption.title("【12米】电线杆：" + point.getNumber());
        } else if (point.getType() == 1) {
            markerOption.title("【15米】电线杆：" + point.getNumber());
        }

        MarkerView markerView = new MarkerView(DrawWireActivity.this);
        //绑定电线杆图标
        markerView.isPolePoint(point, null);
        //电线杆编号
        markerView.setPointNum(point.getNumber());
        markerOption.icon(BitmapDescriptorFactory.fromView(markerView));
        return markerOption;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        Log.i(TAG, "onMapClick: ");
        if (marker != null) {
            marker.hideInfoWindow();
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        this.marker = marker;
        EPoint point = (EPoint) marker.getObject();
        listLatLng.add(point.getLatLng());
        drawWire();
        return false;
    }

    private void drawWire() {
        Log.i(TAG, "drawWire: " + listLatLng.size());
//        aMap.clear();
        aMap.addPolyline((new PolylineOptions()).addAll(listLatLng).color(
                Color.RED));
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
