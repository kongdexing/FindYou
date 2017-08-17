package com.dexing.electricline.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.dexing.electricline.R;
import com.dexing.electricline.model.BoxUser;
import com.dexing.electricline.model.EPoint;
import com.dexing.electricline.model.Village;
import com.dexing.electricline.ui.line.DrawWireActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dexing on 2017/6/2.
 * No1
 */

public class BaseLineActivity extends BaseActivity {

    public int TYPE_POLE_12 = 0;
    public int TYPE_POLE_15 = 1;
    public int TYPE_BOX = 2;

    public PoiResult poiResult; // poi返回的结果
    public PoiSearch.Query query;// Poi查询条件类
    public PoiSearch poiSearch;// POI搜索

    public Village currentVillage;
    public String beamPointId = null;
    public List<EPoint> allPoint = new ArrayList<>();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.app_bar_search) {
            Intent intent = new Intent(this, SearchActivity.class);
            intent.putExtra("village", currentVillage);
            this.startActivityForResult(intent, 1000);
//            finish();
        }
//        else if (item.getItemId() == R.id.app_bar_draw) {
//            Intent intent = new Intent(this, DrawWireActivity.class);
//            intent.putExtra("village", currentVillage);
//            this.startActivity(intent);
//        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            if (resultCode == 1001) {
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    final BoxUser user = (BoxUser) bundle.getSerializable("user");
                    if (user != null) {
                        beamPointId = user.getEPointId();
                        drawPointLine(allPoint);
                    }
                }
            }
        }
    }

    public void drawPointLine(List<EPoint> list) {
    }

}
