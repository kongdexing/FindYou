/*
 * Copyright (C) 2016 Jared Rummler <jared.rummler@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.android.widget.spinner;

import android.content.Context;

import java.util.List;
import java.util.StringTokenizer;

public class MaterialSpinnerAdapter<SpinnerModel> extends MaterialSpinnerBaseAdapter {

    private final List<SpinnerModel> items;

    public MaterialSpinnerAdapter(Context context, List<SpinnerModel> items) {
        super(context);
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size() - 1;
    }

    @Override
    public Object getItem(int position) {
        if (position >= getSelectedIndex()) {
            return items.get(position + 1);
        } else {
            return items.get(position);
        }
    }

    @Override
    public SpinnerModel get(int position) {
        return items.get(position);
    }

    @Override
    public List<SpinnerModel> getItems() {
        return items;
    }

}