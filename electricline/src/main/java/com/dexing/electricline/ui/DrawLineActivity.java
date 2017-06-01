package com.dexing.electricline.ui;

import android.graphics.Point;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
import com.dexing.electricline.R;
import com.dexing.electricline.model.EPoint;
import com.dexing.electricline.model.Village;
import com.dexing.electricline.view.CustomEditDialog;
import com.dexing.electricline.view.MarkerPoleNumView;
import com.dexing.electricline.view.MarkerView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class DrawLineActivity extends BaseActivity implements AMap.OnMarkerClickListener {

    private String TAG = DrawLineActivity.class.getSimpleName();
    private AMap aMap;
    private Marker marker;

    @BindView(R.id.map)
    MapView mapView;
    @BindView(R.id.img_center)
    ImageView img_center;

    @BindView(R.id.progress)
    ProgressBar progress;
    @BindView(R.id.txt_pole)
    TextView txt_pole;
    @BindView(R.id.txt_box)
    TextView txt_box;
    @BindView(R.id.rlTop)
    RelativeLayout rlTop;
    @BindView(R.id.floatingActionButton)
    FloatingActionButton floatingActionButton;

    private int point_type = 0;
    private Village currentVillage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_line);
        mapView.onCreate(savedInstanceState);// 此方法必须重写

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            currentVillage = (Village) bundle.getSerializable("village");
        }
        if (currentVillage != null) {
            init();
        } else {
            Toast.makeText(this, "数据参数错误", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
        }
        aMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器

        setTitle(currentVillage.getName());

        final LatLngBounds.Builder bounds = LatLngBounds.builder();
        //获取该村节点
        BmobQuery<EPoint> bmobQuery = new BmobQuery<EPoint>();
        bmobQuery.addWhereEqualTo("villageId", currentVillage.getObjectId());
        progress.setVisibility(View.VISIBLE);
        bmobQuery.findObjects(new FindListener<EPoint>() {
            @Override
            public void done(List<EPoint> list, BmobException e) {
                progress.setVisibility(View.GONE);
                for (int i = 0; i < list.size(); i++) {
                    EPoint point = list.get(i);
                    LatLng latLng = point.getLatLng();
                    bounds.include(latLng);
                    MarkerView markerView = new MarkerView(DrawLineActivity.this);
                    if (point.getType() == 1) {
                        markerView.isPolePoint(true);
                    } else {
                        markerView.isPolePoint(false);
                    }
                    markerView.setPointNum(point.getNumber());
                    marker = aMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromView(markerView)));
                    marker.setObject(point);
                }
                aMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 43));
            }
        });

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        EPoint point = (EPoint) marker.getObject();
        Toast.makeText(this, point.getNumber(), Toast.LENGTH_SHORT).show();


        return false;
    }

    @OnClick({R.id.floatingActionButton, R.id.btnCancel, R.id.btnOK, R.id.txt_box, R.id.txt_pole, R.id.btnHelp})
    void viewOnClick(View view) {
        switch (view.getId()) {
            case R.id.floatingActionButton:
                floatingActionButton.setVisibility(View.GONE);
                rlTop.setVisibility(View.VISIBLE);
                img_center.setVisibility(View.VISIBLE);
                viewOnClick(txt_box);
                break;
            case R.id.btnCancel:
                floatingActionButton.setVisibility(View.VISIBLE);
                rlTop.setVisibility(View.GONE);
                img_center.setVisibility(View.GONE);
                break;
            case R.id.txt_pole:
                point_type = 1;
                txt_pole.setBackground(getResources().getDrawable(R.color.colorPrimary));
                txt_pole.setTextColor(getResources().getColor(R.color.colorWhite));
                txt_box.setBackground(getResources().getDrawable(R.drawable.bg_recharge_money));
                txt_box.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;
            case R.id.txt_box:
                point_type = 2;
                txt_box.setBackground(getResources().getDrawable(R.color.colorPrimary));
                txt_box.setTextColor(getResources().getColor(R.color.colorWhite));
                txt_pole.setBackground(getResources().getDrawable(R.drawable.bg_recharge_money));
                txt_pole.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;
            case R.id.btnHelp:

                break;
            case R.id.btnOK:
                if (point_type == 0) {
                    Toast.makeText(this, R.string.toast_choose_point, Toast.LENGTH_SHORT).show();
                    return;
                }
                int x = img_center.getLeft() + img_center.getWidth() / 2;
                int y = img_center.getBottom();
                LatLng mLatlng = toGeoLocation(x, y);
//                if (mLatlng != null) {
//                    return;
//                }
                final EPoint point = new EPoint();
                point.setLatitude(mLatlng.latitude);
                point.setLongitude(mLatlng.longitude);
                point.setType(point_type);
                point.setVillageId(currentVillage.getObjectId());

                //输入编号
                CustomEditDialog dialog = new CustomEditDialog(DrawLineActivity.this);
                dialog.setTitle("编号");
                dialog.setHintEdit("请输入编号");
                dialog.setAlertDialogClickListener(new CustomEditDialog.DialogClickListener() {
                    @Override
                    public void onPositiveClick(String value) {
                        if (value.isEmpty()) {
                            Toast.makeText(DrawLineActivity.this, "编号不可为空", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        point.setNumber(value);
                        addPoint(point);
                    }
                });
                break;
        }
    }

    private LatLng toGeoLocation(int x, int y) {
        Point mPoint = new Point(x, y);
        LatLng mLatlng = aMap.getProjection().fromScreenLocation(mPoint);
        return mLatlng;
    }

    private void addPoint(final EPoint point) {

        point.save(new SaveListener<String>() {
            @Override
            public void done(String objectId, BmobException e) {
                if (e == null) {
                    MarkerView markerView = new MarkerView(DrawLineActivity.this);
                    markerView.isPolePoint(point.getType() == 1);
                    markerView.setPointNum(point.getNumber());

                    marker = aMap.addMarker(new MarkerOptions().position(point.getLatLng()).icon(BitmapDescriptorFactory.fromView(markerView)));

//                    village.setObjectId(objectId);
                    toast("添加数据成功，返回objectId为：" + objectId);
//                    adapter.addData(village);
                } else {
                    toast("创建数据失败：" + e.getMessage());
                }
            }
        });

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