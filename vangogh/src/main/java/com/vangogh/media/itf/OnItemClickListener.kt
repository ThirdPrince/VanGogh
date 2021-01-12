/*
 * Copyright 2016 Yan Zhenjie.
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
 */
package com.vangogh.media.itf

import android.view.View

/**
 * @author dhl
 * Listens on the item's click.
 */
interface OnItemClickListener {
    /**
     * When Item is clicked.
     *
     * @param view     item view.
     * @param position item position.
     */
    fun onItemClick(view: View?, position: Int)
}