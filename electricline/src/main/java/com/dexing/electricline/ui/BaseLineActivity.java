package com.dexing.electricline.ui;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.dexing.electricline.R;
import com.dexing.electricline.model.Village;

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
            this.startActivity(intent);
//            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
