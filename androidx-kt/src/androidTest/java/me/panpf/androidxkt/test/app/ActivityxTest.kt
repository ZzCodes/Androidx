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

@file:Suppress("DEPRECATION")

package me.panpf.androidxkt.test.app

import android.app.Activity
import android.app.Application
import android.arch.lifecycle.ViewModelStoreOwner
import android.content.Intent
import android.os.Build
import android.support.test.InstrumentationRegistry
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import me.panpf.androidxkt.app.*
import me.panpf.androidxkt.test.app.activity.ActivityxNoRegisterTestActivity
import me.panpf.androidxkt.test.app.activity.ActivityxTestActivity
import me.panpf.androidxkt.test.app.activity.ActivityxTestFragmentActivity
import me.panpf.androidxkt.test.app.activity.ImplTestInterface
import me.panpf.androidxkt.waitRunInUIResult
import me.panpf.javax.lang.Throwablex
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ActivityxTest {

    @get:Rule
    val activityTestRule = ActivityTestRule(ActivityxTestActivity::class.java)

    @get:Rule
    val fragmentActivityTestRule = ActivityTestRule(ActivityxTestFragmentActivity::class.java)

    @Test
    fun testActivityDestroyed() {
        val activity = activityTestRule.activity

        Assert.assertFalse(activity.isDestroyedCompat())

        activityTestRule.finishActivity()

        try {
            Thread.sleep(2000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        Assert.assertTrue(activity.isDestroyedCompat())
    }

    @Test
    fun testActivityNormal() {
        val activity = activityTestRule.activity

        Assert.assertFalse(activity.isDestroyedCompat())

        try {
            Thread.sleep(2000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        Assert.assertFalse(activity.isDestroyedCompat())
    }

    @Test
    fun testFragmentActivityDestroyed() {
        val activity = fragmentActivityTestRule.activity

        Assert.assertFalse(activity.isDestroyedCompat())

        fragmentActivityTestRule.finishActivity()

        try {
            Thread.sleep(2000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        Assert.assertTrue(activity.isDestroyedCompat())
    }

    @Test
    fun testFragmentActivityNormal() {
        val activity = fragmentActivityTestRule.activity

        Assert.assertFalse(activity.isDestroyedCompat())

        try {
            Thread.sleep(2000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        Assert.assertFalse(activity.isDestroyedCompat())
    }

    @Test
    fun testConvertTranslucent() {
        val activity = activityTestRule.activity

        try {
            Thread.sleep(2000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        var result = waitRunInUIResult { activity.convertToTranslucentCompat() }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Assert.assertTrue(result)
        } else {
            Assert.assertFalse(result)
        }

        try {
            Thread.sleep(2000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        result = waitRunInUIResult { activity.convertFromTranslucentCompat() }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Assert.assertTrue(result)
        } else {
            Assert.assertFalse(result)
        }

        try {
            Thread.sleep(2000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

    }

    @Test
    fun testGetImplWithParent() {
        val activity = activityTestRule.activity
        Assert.assertNotNull(activity.getImplWithParent(ImplTestInterface::class.java))
        Assert.assertNull(activity.getImplWithParent(ViewModelStoreOwner::class.java))

        val activity2 = fragmentActivityTestRule.activity
        Assert.assertNull(activity2.getImplWithParent(ImplTestInterface::class.java))
        Assert.assertNotNull(activity2.getImplWithParent(ViewModelStoreOwner::class.java))
    }

    @Test
    fun testAppContext() {
        val activity = activityTestRule.activity
        Assert.assertTrue(activity.appContext() is Application)
        Assert.assertFalse(activity.appContext() is Activity)
    }

    @Test
    fun testCanStart() {
        val context = InstrumentationRegistry.getContext()

        Assert.assertFalse(context.canStartActivity(Intent(context, ActivityxTest::class.java)))
        Assert.assertTrue(context.canStartActivity(Intent(context, ActivityxTestActivity::class.java)))
    }

    @Test
    fun testStartActivityByIntentActivity() {
        val activity = activityTestRule.activity

        try {
            activity.startActivity(Intent(activity, ActivityxTestActivity::class.java))
        } catch (e: Exception) {
            e.printStackTrace()
            Assert.fail(Throwablex.stackTraceToString(e))
        }

        try {
            activity.appContext().startActivity(Intent(activity, ActivityxTest::class.java))
            Assert.fail()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        try {
            activity.startActivity(ActivityxTestActivity::class.java, null)
        } catch (e: Exception) {
            e.printStackTrace()
            Assert.fail(Throwablex.stackTraceToString(e))
        }

        try {
            activity.startActivity(ActivityxTestActivity::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            Assert.fail(Throwablex.stackTraceToString(e))
        }

        try {
            activity.appContext().startActivity(ActivityxNoRegisterTestActivity::class.java)
            Assert.fail()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Test
    fun testStartActivityByIntentSupportFragment() {
        val activity = fragmentActivityTestRule.activity

        try {
            activity.getFragment().startActivity(Intent(activity, ActivityxTestActivity::class.java))
        } catch (e: Exception) {
            e.printStackTrace()
            Assert.fail(Throwablex.stackTraceToString(e))
        }

        try {
            activity.getFragment().startActivity(Intent(activity, ActivityxTest::class.java))
            Assert.fail()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        try {
            activity.getFragment().startActivity(ActivityxTestActivity::class.java, null)
        } catch (e: Exception) {
            e.printStackTrace()
            Assert.fail(Throwablex.stackTraceToString(e))
        }

        try {
            activity.getFragment().startActivity(ActivityxTestActivity::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            Assert.fail(Throwablex.stackTraceToString(e))
        }

        try {
            activity.getFragment().startActivity(ActivityxNoRegisterTestActivity::class.java)
            Assert.fail()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    @Test
    fun testStartActivityByIntentOriginFragment() {
        val activity = activityTestRule.activity

        try {
            activity.getFragment().startActivity(Intent(activity, ActivityxTestActivity::class.java))
        } catch (e: Exception) {
            e.printStackTrace()
            Assert.fail(Throwablex.stackTraceToString(e))
        }

        try {
            activity.getFragment().startActivity(Intent(activity, ActivityxTest::class.java))
            Assert.fail()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        try {
            activity.getFragment().startActivity(ActivityxTestActivity::class.java, null)
        } catch (e: Exception) {
            e.printStackTrace()
            Assert.fail(Throwablex.stackTraceToString(e))
        }

        try {
            activity.getFragment().startActivity(ActivityxTestActivity::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            Assert.fail(Throwablex.stackTraceToString(e))
        }

        try {
            activity.getFragment().startActivity(ActivityxNoRegisterTestActivity::class.java)
            Assert.fail()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    @Test
    fun testStartActivityByIntentView() {
        val activity = activityTestRule.activity

        try {
            activity.getView().startActivity(Intent(activity, ActivityxTestActivity::class.java))
        } catch (e: Exception) {
            e.printStackTrace()
            Assert.fail(Throwablex.stackTraceToString(e))
        }

        try {
            activity.getView().startActivity(Intent(activity, ActivityxTest::class.java))
            Assert.fail()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        try {
            activity.getView().startActivity(ActivityxTestActivity::class.java, null)
        } catch (e: Exception) {
            e.printStackTrace()
            Assert.fail(Throwablex.stackTraceToString(e))
        }

        try {
            activity.getView().startActivity(ActivityxTestActivity::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            Assert.fail(Throwablex.stackTraceToString(e))
        }

        try {
            activity.getView().startActivity(ActivityxNoRegisterTestActivity::class.java)
            Assert.fail()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Test
    fun testSafeStartActivityByIntentActivity() {
        val activity = activityTestRule.activity

        Assert.assertTrue(activity.safeStartActivity(Intent(activity, ActivityxTestActivity::class.java)))
        Assert.assertFalse(activity.appContext().safeStartActivity(Intent(activity, ActivityxTest::class.java)))
        Assert.assertTrue(activity.safeStartActivity(ActivityxTestActivity::class.java, null))
        Assert.assertTrue(activity.safeStartActivity(ActivityxTestActivity::class.java))
        Assert.assertFalse(activity.appContext().safeStartActivity(ActivityxNoRegisterTestActivity::class.java))
    }

    @Test
    fun testSafeStartActivityByIntentSupportFragment() {
        val activity = fragmentActivityTestRule.activity

        Assert.assertTrue(activity.getFragment().safeStartActivity(Intent(activity, ActivityxTestActivity::class.java)))
        Assert.assertFalse(activity.getFragment().safeStartActivity(Intent(activity, ActivityxTest::class.java)))
        Assert.assertTrue(activity.getFragment().safeStartActivity(ActivityxTestActivity::class.java, null))
        Assert.assertTrue(activity.getFragment().safeStartActivity(ActivityxTestActivity::class.java))
        Assert.assertFalse(activity.getFragment().safeStartActivity(ActivityxNoRegisterTestActivity::class.java))
    }

    @Test
    fun testSafeStartActivityByIntentOriginFragment() {
        val activity = activityTestRule.activity

        Assert.assertTrue(activity.getFragment().safeStartActivity(Intent(activity, ActivityxTestActivity::class.java)))
        Assert.assertFalse(activity.getFragment().safeStartActivity(Intent(activity, ActivityxTest::class.java)))
        Assert.assertTrue(activity.getFragment().safeStartActivity(ActivityxTestActivity::class.java, null))
        Assert.assertTrue(activity.getFragment().safeStartActivity(ActivityxTestActivity::class.java))
        Assert.assertFalse(activity.getFragment().safeStartActivity(ActivityxNoRegisterTestActivity::class.java))
    }

    @Test
    fun testSafeStartActivityByIntentView() {
        val activity = activityTestRule.activity

        Assert.assertTrue(activity.getView().safeStartActivity(Intent(activity, ActivityxTestActivity::class.java)))
        Assert.assertFalse(activity.getView().safeStartActivity(Intent(activity, ActivityxTest::class.java)))
        Assert.assertTrue(activity.getView().safeStartActivity(ActivityxTestActivity::class.java, null))
        Assert.assertTrue(activity.getView().safeStartActivity(ActivityxTestActivity::class.java))
        Assert.assertFalse(activity.getView().safeStartActivity(ActivityxNoRegisterTestActivity::class.java))
    }
}
