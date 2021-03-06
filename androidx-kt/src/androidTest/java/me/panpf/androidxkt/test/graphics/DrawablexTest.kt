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

package me.panpf.androidxkt.test.graphics

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import me.panpf.androidxkt.graphics.drawable.toBitmapWithBoundsSize
import me.panpf.androidxkt.graphics.drawable.toBitmapWithIntrinsicSize
import me.panpf.androidxkt.graphics.drawable.toDrawableByColor
import me.panpf.androidxkt.graphics.drawable.toDrawableByColorFromDrawableRes
import me.panpf.androidxkt.test.R
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DrawablexTest {

    @Test
    fun testIntrinsic() {
        val context = InstrumentationRegistry.getContext()

        val drawable = context.resources.getDrawable(R.drawable.ic_opera)
        val bitmap = drawable.toBitmapWithIntrinsicSize()
        bitmap.recycle()
    }

    @Test
    fun testIntrinsicReuse() {
        val context = InstrumentationRegistry.getContext()

        val drawable = context.resources.getDrawable(R.drawable.ic_opera)

        val reuseBitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val bitmap = drawable.toBitmapWithIntrinsicSize(reuseBitmap = reuseBitmap)
        bitmap.recycle()
    }

    @Test
    fun testIntrinsicConfig() {
        val context = InstrumentationRegistry.getContext()

        val drawable = context.resources.getDrawable(R.drawable.ic_opera)

        val bitmap = drawable.toBitmapWithIntrinsicSize(Bitmap.Config.RGB_565)
        bitmap.recycle()
    }

    @Test
    fun testIntrinsicError() {
        val drawable = GradientDrawable()

        var bitmap: Bitmap? = null
        try {
            bitmap = drawable.toBitmapWithIntrinsicSize()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        Assert.assertNull(bitmap)
    }

    @Test
    fun testBounds() {
        val drawable = GradientDrawable()
        drawable.setColor(Color.parseColor("#88FF0000"))
        drawable.setBounds(0, 0, 100, 100)
        val bitmap = drawable.toBitmapWithBoundsSize()
        bitmap.recycle()
    }

    @Test
    fun testBoundsReuse() {
        val drawable = GradientDrawable()
        drawable.setColor(Color.parseColor("#88FF0000"))
        drawable.setBounds(0, 0, 100, 100)
        val reuseBitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
        val bitmap = drawable.toBitmapWithBoundsSize(reuseBitmap = reuseBitmap)
        bitmap.recycle()
    }

    @Test
    fun testConfig() {
        val drawable = GradientDrawable()
        drawable.setColor(Color.parseColor("#88FF0000"))
        drawable.setBounds(0, 0, 100, 100)
        val bitmap = drawable.toBitmapWithBoundsSize(Bitmap.Config.RGB_565)
        bitmap.recycle()
    }

    @Test
    fun testError() {
        val drawable = GradientDrawable()
        drawable.setColor(Color.parseColor("#88FF0000"))

        var bitmap: Bitmap? = null
        try {
            bitmap = drawable.toBitmapWithIntrinsicSize()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        Assert.assertNull(bitmap)
    }

    @Test
    fun testOther() {
        val drawable = GradientDrawable()
        drawable.setColor(Color.parseColor("#88FF0000"))
        drawable.setBounds(50, 50, 100, 100)
        val bitmap = drawable.toBitmapWithBoundsSize(Bitmap.Config.RGB_565)
        bitmap.recycle()
    }

    @Test
    fun testOther2() {
        val drawable = GradientDrawable()
        drawable.setColor(Color.parseColor("#88FF0000"))
        drawable.setBounds(-50, -50, 100, 100)
        val bitmap = drawable.toBitmapWithBoundsSize(Bitmap.Config.RGB_565)
        bitmap.recycle()
    }

    @Test
    fun testModifyColor() {
        val context = InstrumentationRegistry.getContext()

        val drawable = context.resources.getDrawable(R.drawable.ic_opera).toDrawableByColor(Color.parseColor("#0000FF"))
        val bitmap = drawable.toBitmapWithIntrinsicSize()
        bitmap.recycle()

        val drawable2 = context.toDrawableByColorFromDrawableRes(R.drawable.ic_opera, Color.parseColor("#0000FF"))
        val bitmap2 = drawable2.toBitmapWithIntrinsicSize()
        bitmap2.recycle()
    }
}
