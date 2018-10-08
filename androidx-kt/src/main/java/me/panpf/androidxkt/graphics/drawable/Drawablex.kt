/*
 * Copyright (C) 2018 Peng fei Pan <panpfpanpf@outlook.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:Suppress("NOTHING_TO_INLINE")

package me.panpf.androidxkt.graphics.drawable

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.support.annotation.ColorInt
import android.support.annotation.DrawableRes
import me.panpf.androidx.graphics.drawable.Drawablex

/*
 * Drawable related extension methods
 */

/**
 * Convert Drawable to bitmap, use intrinsic size as the size of the new bitmap
 *
 * @receiver Source Drawable
 * @param config      Bitmap configuration, default value Bitmap.Config.ARGB_8888
 * @param reuseBitmap Reusable Bitmap
 */
inline fun Drawable.toBitmapWithIntrinsicSize(config: Bitmap.Config = Bitmap.Config.ARGB_8888, reuseBitmap: Bitmap? = null): Bitmap =
        Drawablex.toBitmapWithIntrinsicSize(this, config, reuseBitmap)

/**
 * Convert Drawable to bitmap, use bounds size as the size of the new bitmap
 *
 * @receiver Source Drawable
 * @param config      Bitmap configuration, default value Bitmap.Config.ARGB_8888
 * @param reuseBitmap Reusable Bitmap
 */
inline fun Drawable.toBitmapWithBoundsSize(config: Bitmap.Config = Bitmap.Config.ARGB_8888, reuseBitmap: Bitmap? = null): Bitmap =
        Drawablex.toBitmapWithBoundsSize(this, config, reuseBitmap)


/**
 * Change the color of the drawable
 */
inline fun <T : Drawable> T.toDrawableByColor(@ColorInt color: Int): T = Drawablex.toDrawableByColor(this, color)

/**
 * Change the color of the resource drawable
 *
 * @param resId Drawable resource id
 */
inline fun Context.toDrawableByColorFromDrawableRes(@DrawableRes resId: Int, @ColorInt color: Int): Drawable =
        Drawablex.toDrawableByColorFromDrawableRes(this, resId, color)