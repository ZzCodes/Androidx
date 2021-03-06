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

package me.panpf.androidx.test.os;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import me.panpf.androidx.os.Romx;

@RunWith(AndroidJUnit4.class)
public class RomxTest {

    @Test
    public void testRom() {
        System.out.println("Romx -> " + Romx.getInfo());

        if (Romx.isMIUI() && Romx.is(Romx.getType())) {
            Assert.assertEquals(Romx.getTypeName(), "MIUI");
        } else if (Romx.isEMUI() && Romx.is(Romx.getType())) {
            Assert.assertEquals(Romx.getTypeName(), "EMUI");
        } else if (Romx.isFlyme() && Romx.is(Romx.getType())) {
            Assert.assertEquals(Romx.getTypeName(), "Flyme");
        } else if (Romx.isColorOS() && Romx.is(Romx.getType())) {
            Assert.assertEquals(Romx.getTypeName(), "ColorOS");
        } else if (Romx.isFuntouchOS() && Romx.is(Romx.getType())) {
            Assert.assertEquals(Romx.getTypeName(), "FuntouchOS");
        } else if (Romx.isSmartisanOS() && Romx.is(Romx.getType())) {
            Assert.assertEquals(Romx.getTypeName(), "SmartisanOS");
        } else if (Romx.isH2OS() && Romx.is(Romx.getType())) {
            Assert.assertEquals(Romx.getTypeName(), "H2OS");
        } else if (Romx.isLineageOS() && Romx.is(Romx.getType())) {
            Assert.assertEquals(Romx.getTypeName(), "LineageOS");
        } else if (Romx.isAndroid() && Romx.is(Romx.getType())) {
            Assert.assertEquals(Romx.getTypeName(), "Android");
        } else if (Romx.isSamsung() && Romx.is(Romx.getType())) {
            Assert.assertEquals(Romx.getTypeName(), "Samsung");
        } else if (Romx.isUnknown() && Romx.is(Romx.getType())) {
            Assert.assertEquals(Romx.getTypeName(), "Unknown");
        }

        Assert.assertEquals(Romx.getInfo(), Romx.getTypeName() + ":" + Romx.getVersionName() + ":"
                + Romx.getVersionCode() + ":" + Romx.getVersionIncremental());
    }
}
