package com.dexing.electricline.ui;

import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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
import com.amap.api.maps2d.overlay.PoiOverlay;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.dexing.electricline.R;
import com.dexing.electricline.model.BoxUser;
import com.dexing.electricline.model.EPoint;
import com.dexing.electricline.model.GreenDaoHelper;
import com.dexing.electricline.model.Help;
import com.dexing.electricline.model.Village;
import com.dexing.electricline.view.BottomPointView;
import com.dexing.electricline.view.BoxMarkerView;
import com.dexing.electricline.view.CustomDialog;
import com.dexing.electricline.view.CustomEditDialog;
import com.dexing.electricline.view.MarkerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class DrawLineActivity extends BaseLineActivity implements AMap.OnMapClickListener,
        AMap.OnMarkerClickListener, AMap.OnInfoWindowClickListener, PoiSearch.OnPoiSearchListener {

    private String TAG = DrawLineActivity.class.getSimpleName();
    private AMap aMap;
    private Marker marker;

    @BindView(R.id.map)
    MapView mapView;
    @BindView(R.id.img_center)
    ImageView img_center;

    @BindView(R.id.progress)
    ProgressBar progress;
    @BindView(R.id.rlTop)
    RelativeLayout rlTop;
    @BindView(R.id.floatingActionButton)
    FloatingActionButton floatingActionButton;

    @BindView(R.id.spinnerPoint)
    Spinner spinnerPoint;

    private int point_type = TYPE_POLE_12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_line);
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
        aMap.setOnMapClickListener(this);
        aMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
        aMap.setOnInfoWindowClickListener(this);

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
                if (e == null) {
                    if (list.size() == 0) {
                        Log.i(TAG, "done: point size is 0");
                        //检索
                        doSearchQuery();
                        return;
                    }
                    for (int i = 0; i < list.size(); i++) {
                        EPoint point = list.get(i);
                        LatLng latLng = point.getLatLng();
                        bounds.include(latLng);
                        MarkerOptions markerOption = getMarkerOptions(point);
                        marker = aMap.addMarker(markerOption);
                        marker.setObject(point);
                    }
                    aMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 20));
                } else {

                }
            }
        });

        BmobQuery<BoxUser> userQuery = new BmobQuery<BoxUser>();
        userQuery.addWhereEqualTo("VillageId", currentVillage.getObjectId());
        userQuery.findObjects(new FindListener<BoxUser>() {
            @Override
            public void done(List<BoxUser> list, BmobException e) {
                if (e == null) {
                    Toast.makeText(DrawLineActivity.this, "user size " + list.size(), Toast.LENGTH_SHORT).show();
                    GreenDaoHelper.getInstance().insertBoxUser(currentVillage, list);
                }
            }
        });
    }

    /**
     * 开始进行poi搜索
     */
    protected void doSearchQuery() {
        Log.i(TAG, "doSearchQuery: ");
        query = new PoiSearch.Query(currentVillage.getName(), "", "山东");// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query.setPageSize(10);// 设置每页最多返回多少条poiitem
        query.setPageNum(0);// 设置查第一页
        query.setCityLimit(true);

        poiSearch = new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.searchPOIAsyn();
    }


    /**
     * POI信息查询回调方法
     */
    @Override
    public void onPoiSearched(PoiResult result, int rCode) {
        Log.i(TAG, "onPoiSearched: " + rCode);
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getQuery() != null) {// 搜索poi的结果
                if (result.getQuery().equals(query)) {// 是否是同一条
                    poiResult = result;
                    // 取得搜索到的poiitems有多少页
                    List<PoiItem> poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
                    List<SuggestionCity> suggestionCities = poiResult
                            .getSearchSuggestionCitys();// 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息
                    Log.i(TAG, "onPoiSearched: poiItems size " + poiItems.size());
                    Log.i(TAG, "onPoiSearched: suggestionCities size " + suggestionCities.size());

                    if (poiItems != null && poiItems.size() > 0) {
                        aMap.clear();// 清理之前的图标
                        PoiOverlay poiOverlay = new PoiOverlay(aMap, poiItems);
                        poiOverlay.removeFromMap();
                        poiOverlay.addToMap();
                        poiOverlay.zoomToSpan();
                    } else if (suggestionCities != null
                            && suggestionCities.size() > 0) {
                        showSuggestCity(suggestionCities);
                    } else {
//                        Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
//                Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
            }
        } else {
//            Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onPoiItemSearched(PoiItem item, int rCode) {

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
        return false;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        //弹出popup，删除、查看
        marker.hideInfoWindow();
        showBottomView(mapView, marker);
    }

    @OnClick({R.id.floatingActionButton, R.id.btnCancel, R.id.btnOK, R.id.btnHelp})
    void viewOnClick(View view) {
        switch (view.getId()) {
            case R.id.floatingActionButton:
                floatingActionButton.setVisibility(View.GONE);
                rlTop.setVisibility(View.VISIBLE);
                img_center.setVisibility(View.VISIBLE);
                break;
            case R.id.btnCancel:
                floatingActionButton.setVisibility(View.VISIBLE);
                rlTop.setVisibility(View.GONE);
                img_center.setVisibility(View.GONE);
                break;
            case R.id.btnHelp:
                BmobQuery<Help> bmobQuery = new BmobQuery<Help>();
                bmobQuery.findObjects(new FindListener<Help>() {
                    @Override
                    public void done(List<Help> list, BmobException e) {
                        if (list.size() > 0) {
                            String content = list.get(0).getContent();
                            CustomDialog dialog = new CustomDialog(DrawLineActivity.this);
                            dialog.setTitle("帮助");
                            dialog.setMessage(content);
                        }
                    }
                });
                break;
            case R.id.btnOK:
                int x = img_center.getLeft() + img_center.getWidth() / 2;
                int y = img_center.getBottom();
                LatLng mLatlng = toGeoLocation(x, y);
//                if (mLatlng != null) {
//                    return;
//                }
                point_type = spinnerPoint.getSelectedItemPosition();
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

    public void showBottomView(View view, final Marker marker) {
        BottomPointView albumSourceView = new BottomPointView(this);
        final EPoint point = (EPoint) marker.getObject();
        final PopupWindow picPopup = new PopupWindow(albumSourceView,
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);

        albumSourceView.setOnBottomChatClickListener(new BottomPointView.OnBottomChatClickListener() {
            @Override
            public void onLookClick() {
                picPopup.dismiss();
                if (point.getType() == 2) {
                    //查看电表箱用户信息
                    Intent intent = new Intent(DrawLineActivity.this, BoxUserActivity.class);
                    intent.putExtra("point", point);
                    startActivity(intent);
                } else {
                    Toast.makeText(DrawLineActivity.this, R.string.toast_pole_noinfo, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onDeleteClick() {
                picPopup.dismiss();
                point.delete(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            Toast.makeText(DrawLineActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                            //移除marker
                            marker.remove();
                        } else {
                            Toast.makeText(DrawLineActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void onBack() {
                picPopup.dismiss();
            }
        });

        picPopup.setTouchable(true);
        picPopup.setBackgroundDrawable(new ColorDrawable());
        picPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0f);
            }
        });
        backgroundAlpha(0.5f);
        picPopup.showAtLocation(view, Gravity.BOTTOM, 0, 0);
    }

    private void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(lp);
    }

    private void addPoint(final EPoint point) {
        point.save(new SaveListener<String>() {
            @Override
            public void done(String objectId, BmobException e) {
                if (e == null) {
                    MarkerOptions markerOption = getMarkerOptions(point);
                    marker = aMap.addMarker(markerOption);
                    marker.setObject(point);
                    toast("添加数据成功");
                } else {
                    toast("创建数据失败：" + e.getMessage());
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
        } else {
            markerOption.title("电表箱：" + point.getNumber());
        }

        if (beamPointId != null && point.getObjectId().equals(beamPointId)) {
            // 动画效果
            BoxMarkerView box1 = new BoxMarkerView(this);
            BoxMarkerView box2 = new BoxMarkerView(this);
            BoxMarkerView box3 = new BoxMarkerView(this);
            BoxMarkerView box4 = new BoxMarkerView(this);
            box1.setPointNum(point.getNumber());
            box1.setBackResource(R.drawable.icon_box_1);
            box2.setPointNum(point.getNumber());
            box2.setBackResource(R.drawable.icon_box_2);
            box3.setPointNum(point.getNumber());
            box3.setBackResource(R.drawable.icon_box_3);
            box4.setPointNum(point.getNumber());
            box4.setBackResource(R.drawable.icon_box_4);

            ArrayList<BitmapDescriptor> giflist = new ArrayList<BitmapDescriptor>();
            giflist.add(BitmapDescriptorFactory.fromView(box1));
            giflist.add(BitmapDescriptorFactory.fromView(box2));
            giflist.add(BitmapDescriptorFactory.fromView(box3));
            giflist.add(BitmapDescriptorFactory.fromView(box4));
//            markerOption.icon(null);
            markerOption.icons(giflist).period(2);
        } else {
            MarkerView markerView = new MarkerView(DrawLineActivity.this);
            markerView.isPolePoint(point, beamPointId);
            markerView.setPointNum(point.getNumber());
            markerOption.icon(BitmapDescriptorFactory.fromView(markerView));
        }
        return markerOption;
    }

    /**
     * poi没有搜索到数据，返回一些推荐城市的信息
     */
    private void showSuggestCity(List<SuggestionCity> cities) {
        String infomation = "推荐城市\n";
        for (int i = 0; i < cities.size(); i++) {
            infomation += "城市名称:" + cities.get(i).getCityName() + "城市区号:"
                    + cities.get(i).getCityCode() + "城市编码:"
                    + cities.get(i).getAdCode() + "\n";
        }
//        ToastUtil.show(PoiKeywordSearchActivity.this, infomation);
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
