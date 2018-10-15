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

package me.panpf.androidx.app;

import android.app.Activity;
import android.arch.lifecycle.Lifecycle;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.util.Size;
import android.util.SizeF;
import android.util.SparseArray;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("WeakerAccess")
public class Fragmentx {

    /**
     * Return true if the fragment has been destroyed
     */
    public static boolean isDestroyedCompat(@NonNull android.app.Fragment fragment) {
        return fragment.getActivity() == null;
    }

    /**
     * Return true if the fragment has been destroyed
     */
    public static boolean isDestroyedCompat(@NonNull android.support.v4.app.Fragment fragment) {
        return fragment.getLifecycle().getCurrentState() == Lifecycle.State.DESTROYED;
    }

    /**
     * If the own or parent Fragment implements the specified [clazz], it returns its implementation.
     */
    @Nullable
    public static <T> T getImplWithParent(@NonNull android.support.v4.app.Fragment fragment, @NonNull Class<T> clazz) {
        android.support.v4.app.Fragment parent = fragment;
        while (parent != null) {
            if (clazz.isAssignableFrom(parent.getClass())) {
                //noinspection unchecked
                return (T) clazz;
            } else {
                parent = parent.getParentFragment();
            }
        }
        Activity parentActivity = fragment.getActivity();
        while (parentActivity != null) {
            if (clazz.isAssignableFrom(parentActivity.getClass())) {
                //noinspection unchecked
                return (T) clazz;
            } else {
                parentActivity = parentActivity.getParent();
            }
        }
        return null;
    }

    /**
     * If the own or parent Fragment implements the specified [clazz], it returns its implementation.
     */
    @Nullable
    public static <T> T getImplWithParent(@NonNull android.app.Fragment fragment, @NonNull Class<T> clazz) {
        android.app.Fragment parent = fragment;
        while (parent != null) {
            if (clazz.isAssignableFrom(parent.getClass())) {
                //noinspection unchecked
                return (T) clazz;
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    parent = parent.getParentFragment();
                } else {
                    parent = null;
                }
            }
        }
        Activity parentActivity = fragment.getActivity();
        while (parentActivity != null) {
            if (clazz.isAssignableFrom(parentActivity.getClass())) {
                //noinspection unchecked
                return (T) clazz;
            } else {
                parentActivity = parentActivity.getParent();
            }
        }
        return null;
    }

    /**
     * Instantiate a Fragment and set arguments
     */
    public static android.support.v4.app.Fragment instance(@NotNull Class<? extends android.support.v4.app.Fragment> clas, @Nullable Bundle arguments) {
        android.support.v4.app.Fragment fragment;
        //noinspection TryWithIdenticalCatches
        try {
            fragment = clas.newInstance();
        } catch (InstantiationException e) {
            throw new IllegalArgumentException(e);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException(e);
        }
        if (arguments != null) {
            fragment.setArguments(arguments);
        }
        return fragment;
    }

    /**
     * Instantiate a Fragment and set arguments
     */
    public static android.support.v4.app.Fragment instance(@NotNull Class<? extends android.support.v4.app.Fragment> clas) {
        return instance(clas, null);
    }

    /**
     * Instantiate a Fragment and set arguments
     */
    public static android.app.Fragment instanceOrigin(@NotNull Class<? extends android.app.Fragment> clas, @Nullable Bundle arguments) {
        android.app.Fragment fragment;
        //noinspection TryWithIdenticalCatches
        try {
            fragment = clas.newInstance();
        } catch (InstantiationException e) {
            throw new IllegalArgumentException(e);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException(e);
        }
        if (arguments != null) {
            fragment.setArguments(arguments);
        }
        return fragment;
    }

    /**
     * Instantiate a Fragment and set arguments
     */
    public static android.app.Fragment instanceOrigin(@NotNull Class<? extends android.app.Fragment> clas) {
        return instanceOrigin(clas, null);
    }

    /**
     * Find the visible fragments visible to the current user from the fragment list
     */
    @Nullable
    public static android.support.v4.app.Fragment findUserVisibleChildFragment(@Nullable List<android.support.v4.app.Fragment> fragmentList) {
        android.support.v4.app.Fragment userVisibleFragment = null;
        if (fragmentList != null) {
            for (android.support.v4.app.Fragment childFragment : fragmentList) {
                if (childFragment != null) {
                    if (childFragment.isResumed() && childFragment.getUserVisibleHint()) {
                        userVisibleFragment = childFragment;
                    } else {
                        List<android.support.v4.app.Fragment> childFragmentList = childFragment.getChildFragmentManager().getFragments();
                        userVisibleFragment = findUserVisibleChildFragment(childFragmentList);
                    }
                    if (userVisibleFragment != null) {
                        break;
                    }
                }
            }
        }
        return userVisibleFragment;
    }

    /**
     * Find the visible fragments visible to the current user from the FragmentActivity
     */
    @Nullable
    public static android.support.v4.app.Fragment findUserVisibleChildFragment(@Nullable FragmentActivity fragmentActivity) {
        return findUserVisibleChildFragment(fragmentActivity != null ? fragmentActivity.getSupportFragmentManager().getFragments() : null);
    }

    /**
     * Find the visible fragments visible to the current user from the fragment
     */
    @Nullable
    public static android.support.v4.app.Fragment findUserVisibleChildFragment(@Nullable android.support.v4.app.Fragment fragment) {
        return findUserVisibleChildFragment(fragment != null ? fragment.getChildFragmentManager().getFragments() : null);
    }

    /**
     * Find the target fragment from the specified fragment list based on the current Item of ViewPager
     */
    @Nullable
    public static android.support.v4.app.Fragment findFragmentByViewPagerCurrentItem(
            @Nullable List<android.support.v4.app.Fragment> fragmentList, int viewPagerCurrentItem) {
        if (fragmentList != null) {
            String viewPagerCurrentItemString = String.valueOf(viewPagerCurrentItem);
            for (android.support.v4.app.Fragment fragment : fragmentList) {
                String fragmentTag = fragment.getTag();
                if (fragmentTag != null) {
                    String[] tagItems = fragmentTag.split(":");
                    if (tagItems.length > 0 && tagItems[tagItems.length - 1].equals(viewPagerCurrentItemString)) {
                        return fragment;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Find the target fragment from the specified fragment list based on the current Item of ViewPager
     */
    @Nullable
    public static android.support.v4.app.Fragment findFragmentByViewPagerCurrentItem(@Nullable FragmentActivity fragmentActivity, int viewPagerCurrentItem) {
        return findFragmentByViewPagerCurrentItem(fragmentActivity != null ? fragmentActivity.getSupportFragmentManager().getFragments() : null, viewPagerCurrentItem);
    }

    /**
     * Find the target fragment from the specified fragment list based on the current Item of ViewPager
     */
    @Nullable
    public static android.support.v4.app.Fragment findFragmentByViewPagerCurrentItem(@Nullable android.support.v4.app.Fragment fragment, int viewPagerCurrentItem) {
        return findFragmentByViewPagerCurrentItem(fragment != null ? fragment.getChildFragmentManager().getFragments() : null, viewPagerCurrentItem);
    }


    /* ************************************* SupportFragment Args ***************************************** */


    public static byte readByteArg(@NotNull android.support.v4.app.Fragment fragment, @NotNull String argName, byte defaultValue) {
        Bundle arguments = fragment.getArguments();
        return arguments != null ? arguments.getByte(argName, defaultValue) : defaultValue;
    }

    @NotNull
    public static byte[] readByteArrayArg(@NotNull android.support.v4.app.Fragment fragment, @NotNull String argName) {
        Bundle arguments = fragment.getArguments();
        byte[] bytes = arguments != null ? arguments.getByteArray(argName) : null;
        if (bytes != null) return bytes;
        throw (new IllegalArgumentException("Param '" + argName + "' not found"));
    }

    @NotNull
    public static byte[] readByteArrayArg(@NotNull android.support.v4.app.Fragment fragment, @NotNull String argName, @NotNull byte[] defaultValue) {
        Bundle arguments = fragment.getArguments();
        byte[] bytes = arguments != null ? arguments.getByteArray(argName) : null;
        return bytes != null ? bytes : defaultValue;
    }

    @Nullable
    public static byte[] readOptionalByteArrayArg(@NotNull android.support.v4.app.Fragment fragment, @NotNull String argName) {
        Bundle arguments = fragment.getArguments();
        return arguments != null ? arguments.getByteArray(argName) : null;
    }


    public static short readShortArg(@NotNull android.support.v4.app.Fragment fragment, @NotNull String argName, short defaultValue) {
        Bundle arguments = fragment.getArguments();
        return arguments != null ? arguments.getShort(argName, defaultValue) : defaultValue;
    }

    @NotNull
    public static short[] readShortArrayArg(@NotNull android.support.v4.app.Fragment fragment, @NotNull String argName) {
        Bundle arguments = fragment.getArguments();
        short[] shorts = arguments != null ? arguments.getShortArray(argName) : null;
        if (shorts != null) return shorts;
        throw (new IllegalArgumentException("Param '" + argName + "' not found"));
    }

    @NotNull
    public static short[] readShortArrayArg(@NotNull android.support.v4.app.Fragment fragment, @NotNull String argName, @NotNull short[] defaultValue) {
        Bundle arguments = fragment.getArguments();
        short[] shorts = arguments != null ? arguments.getShortArray(argName) : null;
        return shorts != null ? shorts : defaultValue;
    }

    @Nullable
    public static short[] readOptionalShortArrayArg(@NotNull android.support.v4.app.Fragment fragment, @NotNull String argName) {
        Bundle arguments = fragment.getArguments();
        return arguments != null ? arguments.getShortArray(argName) : null;
    }


    public static int readIntArg(@NotNull android.support.v4.app.Fragment fragment, @NotNull String argName, int defaultValue) {
        Bundle arguments = fragment.getArguments();
        return arguments != null ? arguments.getInt(argName, defaultValue) : defaultValue;
    }

    @NotNull
    public static int[] readIntArrayArg(@NotNull android.support.v4.app.Fragment fragment, @NotNull String argName) {
        Bundle arguments = fragment.getArguments();
        int[] ints = arguments != null ? arguments.getIntArray(argName) : null;
        if (ints != null) return ints;
        throw (new IllegalArgumentException("Param '" + argName + "' not found"));
    }

    @NotNull
    public static int[] readIntArrayArg(@NotNull android.support.v4.app.Fragment fragment, @NotNull String argName, @NotNull int[] defaultValue) {
        Bundle arguments = fragment.getArguments();
        int[] ints = arguments != null ? arguments.getIntArray(argName) : null;
        return ints != null ? ints : defaultValue;
    }

    @Nullable
    public static int[] readOptionalIntArrayArg(@NotNull android.support.v4.app.Fragment fragment, @NotNull String argName) {
        Bundle arguments = fragment.getArguments();
        return arguments != null ? arguments.getIntArray(argName) : null;
    }

    @NotNull
    public static ArrayList<Integer> readIntArrayListArg(@NotNull android.support.v4.app.Fragment fragment, @NotNull String argName) {
        Bundle arguments = fragment.getArguments();
        ArrayList<Integer> ints = arguments != null ? arguments.getIntegerArrayList(argName) : null;
        if (ints != null) return ints;
        throw (new IllegalArgumentException("Param '" + argName + "' not found"));
    }

    @NotNull
    public static ArrayList<Integer> readIntArrayListArg(@NotNull android.support.v4.app.Fragment fragment, @NotNull String argName, @NotNull ArrayList<Integer> defaultValue) {
        Bundle arguments = fragment.getArguments();
        ArrayList<Integer> integers = arguments != null ? arguments.getIntegerArrayList(argName) : null;
        return integers != null ? integers : defaultValue;
    }

    @Nullable
    public static ArrayList<Integer> readOptionalIntArrayListArg(@NotNull android.support.v4.app.Fragment fragment, @NotNull String argName) {
        Bundle arguments = fragment.getArguments();
        return arguments != null ? arguments.getIntegerArrayList(argName) : null;
    }


    public static long readLongArg(@NotNull android.support.v4.app.Fragment fragment, @NotNull String argName, long defaultValue) {
        Bundle arguments = fragment.getArguments();
        return arguments != null ? arguments.getLong(argName, defaultValue) : defaultValue;
    }

    @NotNull
    public static long[] readLongArrayArg(@NotNull android.support.v4.app.Fragment fragment, @NotNull String argName) {
        Bundle arguments = fragment.getArguments();
        long[] longs = arguments != null ? arguments.getLongArray(argName) : null;
        if (longs != null) return longs;
        throw (new IllegalArgumentException("Param '" + argName + "' not found"));
    }

    @NotNull
    public static long[] readLongArrayArg(@NotNull android.support.v4.app.Fragment fragment, @NotNull String argName, @NotNull long[] defaultValue) {
        Bundle arguments = fragment.getArguments();
        long[] longs = arguments != null ? arguments.getLongArray(argName) : null;
        return longs != null ? longs : defaultValue;
    }

    @Nullable
    public static long[] readOptionalLongArrayArg(@NotNull android.support.v4.app.Fragment fragment, @NotNull String argName) {
        Bundle arguments = fragment.getArguments();
        return arguments != null ? arguments.getLongArray(argName) : null;
    }


    public static float readFloatArg(@NotNull android.support.v4.app.Fragment fragment, @NotNull String argName, float defaultValue) {
        Bundle arguments = fragment.getArguments();
        return arguments != null ? arguments.getFloat(argName, defaultValue) : defaultValue;
    }

    @NotNull
    public static float[] readFloatArrayArg(@NotNull android.support.v4.app.Fragment fragment, @NotNull String argName) {
        Bundle arguments = fragment.getArguments();
        float[] floats = arguments != null ? arguments.getFloatArray(argName) : null;
        if (floats != null) return floats;
        throw (new IllegalArgumentException("Param '" + argName + "' not found"));
    }

    @NotNull
    public static float[] readFloatArrayArg(@NotNull android.support.v4.app.Fragment fragment, @NotNull String argName, @NotNull float[] defaultValue) {
        Bundle arguments = fragment.getArguments();
        float[] floats = arguments != null ? arguments.getFloatArray(argName) : null;
        return floats != null ? floats : defaultValue;
    }

    @Nullable
    public static float[] readOptionalFloatArrayArg(@NotNull android.support.v4.app.Fragment fragment, @NotNull String argName) {
        Bundle arguments = fragment.getArguments();
        return arguments != null ? arguments.getFloatArray(argName) : null;
    }


    public static double readDoubleArg(@NotNull android.support.v4.app.Fragment fragment, @NotNull String argName, double defaultValue) {
        Bundle arguments = fragment.getArguments();
        return arguments != null ? arguments.getDouble(argName, defaultValue) : defaultValue;
    }

    @NotNull
    public static double[] readDoubleArrayArg(@NotNull android.support.v4.app.Fragment fragment, @NotNull String argName) {
        Bundle arguments = fragment.getArguments();
        double[] doubles = arguments != null ? arguments.getDoubleArray(argName) : null;
        if (doubles != null) return doubles;
        throw (new IllegalArgumentException("Param '" + argName + "' not found"));
    }

    @NotNull
    public static double[] readDoubleArrayArg(@NotNull android.support.v4.app.Fragment fragment, @NotNull String argName, @NotNull double[] defaultValue) {
        Bundle arguments = fragment.getArguments();
        double[] doubles = arguments != null ? arguments.getDoubleArray(argName) : null;
        return doubles != null ? doubles : defaultValue;
    }

    @Nullable
    public static double[] readOptionalDoubleArrayArg(@NotNull android.support.v4.app.Fragment fragment, @NotNull String argName) {
        Bundle arguments = fragment.getArguments();
        return arguments != null ? arguments.getDoubleArray(argName) : null;
    }


    public static boolean readBooleanArg(@NotNull android.support.v4.app.Fragment fragment, @NotNull String argName, boolean defaultValue) {
        Bundle arguments = fragment.getArguments();
        return arguments != null ? arguments.getBoolean(argName, defaultValue) : defaultValue;
    }

    @NotNull
    public static boolean[] readBooleanArrayArg(@NotNull android.support.v4.app.Fragment fragment, @NotNull String argName) {
        Bundle arguments = fragment.getArguments();
        boolean[] booleans = arguments != null ? arguments.getBooleanArray(argName) : null;
        if (booleans != null) return booleans;
        throw (new IllegalArgumentException("Param '" + argName + "' not found"));
    }

    @NotNull
    public static boolean[] readBooleanArrayArg(@NotNull android.support.v4.app.Fragment fragment, @NotNull String argName, @NotNull boolean[] defaultValue) {
        Bundle arguments = fragment.getArguments();
        boolean[] booleans = arguments != null ? arguments.getBooleanArray(argName) : null;
        return booleans != null ? booleans : defaultValue;
    }

    @Nullable
    public static boolean[] readOptionalBooleanArrayArg(@NotNull android.support.v4.app.Fragment fragment, @NotNull String argName) {
        Bundle arguments = fragment.getArguments();
        return arguments != null ? arguments.getBooleanArray(argName) : null;
    }


    public static char readCharArg(@NotNull android.support.v4.app.Fragment fragment, @NotNull String argName, char defaultValue) {
        Bundle arguments = fragment.getArguments();
        return arguments != null ? arguments.getChar(argName, defaultValue) : defaultValue;
    }

    @NotNull
    public static char[] readCharArrayArg(@NotNull android.support.v4.app.Fragment fragment, @NotNull String argName) {
        Bundle arguments = fragment.getArguments();
        char[] chars = arguments != null ? arguments.getCharArray(argName) : null;
        if (chars != null) return chars;
        throw (new IllegalArgumentException("Param '" + argName + "' not found"));
    }

    @NotNull
    public static char[] readCharArrayArg(@NotNull android.support.v4.app.Fragment fragment, @NotNull String argName, @NotNull char[] defaultValue) {
        Bundle arguments = fragment.getArguments();
        char[] chars = arguments != null ? arguments.getCharArray(argName) : null;
        return chars != null ? chars : defaultValue;
    }

    @Nullable
    public static char[] readOptionalCharArrayArg(@NotNull android.support.v4.app.Fragment fragment, @NotNull String argName) {
        Bundle arguments = fragment.getArguments();
        return arguments != null ? arguments.getCharArray(argName) : null;
    }


    @NotNull
    public static CharSequence readCharSequenceArg(@NotNull android.support.v4.app.Fragment fragment, @NotNull String argName) {
        Bundle arguments = fragment.getArguments();
        CharSequence charSequence = arguments != null ? arguments.getCharSequence(argName) : null;
        if (charSequence != null) return charSequence;
        throw (new IllegalArgumentException("Param '" + argName + "' not found"));
    }

    @NotNull
    public static CharSequence readCharSequenceArg(@NotNull android.support.v4.app.Fragment fragment, @NotNull String argName, @NotNull CharSequence defaultValue) {
        Bundle arguments = fragment.getArguments();
        CharSequence charSequence = arguments != null ? arguments.getCharSequence(argName, defaultValue) : null;
        return charSequence != null ? charSequence : defaultValue;
    }

    @Nullable
    public static CharSequence readOptionalCharSequenceArg(@NotNull android.support.v4.app.Fragment fragment, @NotNull String argName) {
        Bundle arguments = fragment.getArguments();
        return arguments != null ? arguments.getCharSequence(argName) : null;
    }

    @NotNull
    public static CharSequence[] readCharSequenceArrayArg(@NotNull android.support.v4.app.Fragment fragment, @NotNull String argName) {
        Bundle arguments = fragment.getArguments();
        CharSequence[] charSequences = arguments != null ? arguments.getCharSequenceArray(argName) : null;
        if (charSequences != null) return charSequences;
        throw (new IllegalArgumentException("Param '" + argName + "' not found"));
    }

    @NotNull
    public static CharSequence[] readCharSequenceArrayArg(@NotNull android.support.v4.app.Fragment fragment, @NotNull String argName, @NotNull CharSequence[] defaultValue) {
        Bundle arguments = fragment.getArguments();
        CharSequence[] charSequences = arguments != null ? arguments.getCharSequenceArray(argName) : null;
        return charSequences != null ? charSequences : defaultValue;
    }

    @Nullable
    public static CharSequence[] readOptionalCharSequenceArrayArg(@NotNull android.support.v4.app.Fragment fragment, @NotNull String argName) {
        Bundle arguments = fragment.getArguments();
        return arguments != null ? arguments.getCharSequenceArray(argName) : null;
    }

    @NotNull
    public static ArrayList<CharSequence> readCharSequenceArrayListArg(@NotNull android.support.v4.app.Fragment fragment, @NotNull String argName) {
        Bundle arguments = fragment.getArguments();
        ArrayList<CharSequence> charSequences = arguments != null ? arguments.getCharSequenceArrayList(argName) : null;
        if (charSequences != null) return charSequences;
        throw (new IllegalArgumentException("Param '" + argName + "' not found"));
    }

    @NotNull
    public static ArrayList<CharSequence> readCharSequenceArrayListArg(@NotNull android.support.v4.app.Fragment fragment, @NotNull String argName, @NotNull ArrayList<CharSequence> defaultValue) {
        Bundle arguments = fragment.getArguments();
        ArrayList<CharSequence> charSequences = arguments != null ? arguments.getCharSequenceArrayList(argName) : null;
        return charSequences != null ? charSequences : defaultValue;
    }

    @Nullable
    public static ArrayList<CharSequence> readOptionalCharSequenceArrayListArg(@NotNull android.support.v4.app.Fragment fragment, @NotNull String argName) {
        Bundle arguments = fragment.getArguments();
        return arguments != null ? arguments.getCharSequenceArrayList(argName) : null;
    }


    @NotNull
    public static String readStringArg(@NotNull android.support.v4.app.Fragment fragment, @NotNull String argName) {
        Bundle arguments = fragment.getArguments();
        String string = arguments != null ? arguments.getString(argName) : null;
        if (string != null) return string;
        throw (new IllegalArgumentException("Param '" + argName + "' not found"));
    }

    @NotNull
    public static String readStringArg(@NotNull android.support.v4.app.Fragment fragment, @NotNull String argName, @NotNull String defaultValue) {
        Bundle arguments = fragment.getArguments();
        String string = arguments != null ? arguments.getString(argName, defaultValue) : null;
        return string != null ? string : defaultValue;
    }

    @Nullable
    public static String readOptionalStringArg(@NotNull android.support.v4.app.Fragment fragment, @NotNull String argName) {
        Bundle arguments = fragment.getArguments();
        return arguments != null ? arguments.getString(argName) : null;
    }

    @NotNull
    public static String[] readStringArrayArg(@NotNull android.support.v4.app.Fragment fragment, @NotNull String argName) {
        Bundle arguments = fragment.getArguments();
        String[] strings = arguments != null ? arguments.getStringArray(argName) : null;
        if (strings != null) return strings;
        throw (new IllegalArgumentException("Param '" + argName + "' not found"));
    }

    @NotNull
    public static String[] readStringArrayArg(@NotNull android.support.v4.app.Fragment fragment, @NotNull String argName, @NotNull String[] defaultValue) {
        Bundle arguments = fragment.getArguments();
        String[] strings = arguments != null ? arguments.getStringArray(argName) : null;
        return strings != null ? strings : defaultValue;
    }

    @Nullable
    public static String[] readOptionalStringArrayArg(@NotNull android.support.v4.app.Fragment fragment, @NotNull String argName) {
        Bundle arguments = fragment.getArguments();
        return arguments != null ? arguments.getStringArray(argName) : null;
    }

    @NotNull
    public static ArrayList<String> readStringArrayListArg(@NotNull android.support.v4.app.Fragment fragment, @NotNull String argName) {
        Bundle arguments = fragment.getArguments();
        ArrayList<String> strings = arguments != null ? arguments.getStringArrayList(argName) : null;
        if (strings != null) return strings;
        throw (new IllegalArgumentException("Param '" + argName + "' not found"));
    }

    @NotNull
    public static ArrayList<String> readStringArrayListArg(@NotNull android.support.v4.app.Fragment fragment, @NotNull String argName, @NotNull ArrayList<String> defaultValue) {
        Bundle arguments = fragment.getArguments();
        ArrayList<String> strings = arguments != null ? arguments.getStringArrayList(argName) : null;
        return strings != null ? strings : defaultValue;
    }

    @Nullable
    public static ArrayList<String> readOptionalStringArrayListArg(@NotNull android.support.v4.app.Fragment fragment, @NotNull String argName) {
        Bundle arguments = fragment.getArguments();
        return arguments != null ? arguments.getStringArrayList(argName) : null;
    }


    @NotNull
    public static <V extends Parcelable> V readParcelableArg(@NotNull android.support.v4.app.Fragment fragment, @NotNull String argName) {
        Bundle arguments = fragment.getArguments();
        // Because the ClassLoader may be lost when the Bundle is passed in the Bundle, you need to restore it to deserialize the custom Parcelable.
        if (arguments != null) arguments.setClassLoader(Fragmentx.class.getClassLoader());
        //noinspection unchecked
        V parcelable = arguments != null ? (V) arguments.getParcelable(argName) : null;
        if (parcelable != null) return parcelable;
        throw (new IllegalArgumentException("Param '" + argName + "' not found"));
    }

    @NotNull
    public static <V extends Parcelable> V readParcelableArg(@NotNull android.support.v4.app.Fragment fragment, @NotNull String argName, @NotNull V defaultValue) {
        Bundle arguments = fragment.getArguments();
        // Because the ClassLoader may be lost when the Bundle is passed in the Bundle, you need to restore it to deserialize the custom Parcelable.
        if (arguments != null) arguments.setClassLoader(Fragmentx.class.getClassLoader());
        Parcelable parcelable = arguments != null ? arguments.getParcelable(argName) : null;
        //noinspection unchecked
        return parcelable != null ? (V) parcelable : defaultValue;
    }

    @Nullable
    public static <V extends Parcelable> V readOptionalParcelableArg(@NotNull android.support.v4.app.Fragment fragment, @NotNull String argName) {
        Bundle arguments = fragment.getArguments();
        // Because the ClassLoader may be lost when the Bundle is passed in the Bundle, you need to restore it to deserialize the custom Parcelable.
        if (arguments != null) arguments.setClassLoader(Fragmentx.class.getClassLoader());
        //noinspection unchecked
        return arguments != null ? (V) arguments.getParcelable(argName) : null;
    }

    @NotNull
    public static <V extends Parcelable> V[] readParcelableArrayArg(@NotNull android.support.v4.app.Fragment fragment, @NotNull String argName) {
        Bundle arguments = fragment.getArguments();
        // Because the ClassLoader may be lost when the Bundle is passed in the Bundle, you need to restore it to deserialize the custom Parcelable.
        if (arguments != null) arguments.setClassLoader(Fragmentx.class.getClassLoader());
        //noinspection unchecked
        V[] parcelables = arguments != null ? (V[]) arguments.getParcelableArray(argName) : null;
        if (parcelables != null) return parcelables;
        throw (new IllegalArgumentException("Param '" + argName + "' not found"));
    }

    @NotNull
    public static <V extends Parcelable> V[] readParcelableArrayArg(@NotNull android.support.v4.app.Fragment fragment, @NotNull String argName, @NotNull V[] defaultValue) {
        Bundle arguments = fragment.getArguments();
        // Because the ClassLoader may be lost when the Bundle is passed in the Bundle, you need to restore it to deserialize the custom Parcelable.
        if (arguments != null) arguments.setClassLoader(Fragmentx.class.getClassLoader());
        //noinspection unchecked
        V[] parcelables = arguments != null ? (V[]) arguments.getParcelableArray(argName) : null;
        return parcelables != null ? parcelables : defaultValue;
    }

    @Nullable
    public static <V extends Parcelable> V[] readOptionalParcelableArrayArg(@NotNull android.support.v4.app.Fragment fragment, @NotNull String argName) {
        Bundle arguments = fragment.getArguments();
        // Because the ClassLoader may be lost when the Bundle is passed in the Bundle, you need to restore it to deserialize the custom Parcelable.
        if (arguments != null) arguments.setClassLoader(Fragmentx.class.getClassLoader());
        //noinspection unchecked
        return arguments != null ? (V[]) arguments.getParcelableArray(argName) : null;
    }

    @NotNull
    public static <V extends Parcelable> ArrayList<V> readParcelableArrayListArg(@NotNull android.support.v4.app.Fragment fragment, @NotNull String argName) {
        Bundle arguments = fragment.getArguments();
        // Because the ClassLoader may be lost when the Bundle is passed in the Bundle, you need to restore it to deserialize the custom Parcelable.
        if (arguments != null) arguments.setClassLoader(Fragmentx.class.getClassLoader());
        //noinspection unchecked
        ArrayList<V> parcelable = arguments != null ? (ArrayList<V>) arguments.getParcelableArrayList(argName) : null;
        if (parcelable != null) return parcelable;
        throw (new IllegalArgumentException("Param '" + argName + "' not found"));
    }

    @NotNull
    public static <V extends Parcelable> ArrayList<V> readParcelableArrayListArg(@NotNull android.support.v4.app.Fragment fragment, @NotNull String argName, @NotNull ArrayList<V> defaultValue) {
        Bundle arguments = fragment.getArguments();
        // Because the ClassLoader may be lost when the Bundle is passed in the Bundle, you need to restore it to deserialize the custom Parcelable.
        if (arguments != null) arguments.setClassLoader(Fragmentx.class.getClassLoader());
        //noinspection unchecked
        ArrayList<V> parcelables = arguments != null ? (ArrayList<V>) arguments.getParcelableArrayList(argName) : null;
        return parcelables != null ? parcelables : defaultValue;
    }

    @Nullable
    public static <V extends Parcelable> ArrayList<V> readOptionalParcelableArrayListArg(@NotNull android.support.v4.app.Fragment fragment, @NotNull String argName) {
        Bundle arguments = fragment.getArguments();
        // Because the ClassLoader may be lost when the Bundle is passed in the Bundle, you need to restore it to deserialize the custom Parcelable.
        if (arguments != null) arguments.setClassLoader(Fragmentx.class.getClassLoader());
        //noinspection unchecked
        return arguments != null ? (ArrayList<V>) arguments.getParcelableArrayList(argName) : null;
    }

    @NotNull
    public static <V extends Parcelable> SparseArray<V> readSparseParcelableArrayArg(@NotNull android.support.v4.app.Fragment fragment, @NotNull String argName) {
        Bundle arguments = fragment.getArguments();
        // Because the ClassLoader may be lost when the Bundle is passed in the Bundle, you need to restore it to deserialize the custom Parcelable.
        if (arguments != null) arguments.setClassLoader(Fragmentx.class.getClassLoader());
        //noinspection unchecked
        SparseArray<V> sparseArray = arguments != null ? (SparseArray<V>) arguments.getSparseParcelableArray(argName) : null;
        if (sparseArray != null) return sparseArray;
        throw (new IllegalArgumentException("Param '" + argName + "' not found"));
    }

    @NotNull
    public static <V extends Parcelable> SparseArray<V> readSparseParcelableArrayArg(@NotNull android.support.v4.app.Fragment fragment, @NotNull String argName, @NotNull SparseArray<V> defaultValue) {
        Bundle arguments = fragment.getArguments();
        // Because the ClassLoader may be lost when the Bundle is passed in the Bundle, you need to restore it to deserialize the custom Parcelable.
        if (arguments != null) arguments.setClassLoader(Fragmentx.class.getClassLoader());
        //noinspection unchecked
        SparseArray<V> sparseArray = arguments != null ? (SparseArray<V>) arguments.getSparseParcelableArray(argName) : null;
        return sparseArray != null ? sparseArray : defaultValue;
    }

    @Nullable
    public static <V extends Parcelable> SparseArray<V> readOptionalSparseParcelableArrayArg(@NotNull android.support.v4.app.Fragment fragment, @NotNull String argName) {
        Bundle arguments = fragment.getArguments();
        // Because the ClassLoader may be lost when the Bundle is passed in the Bundle, you need to restore it to deserialize the custom Parcelable.
        if (arguments != null) arguments.setClassLoader(Fragmentx.class.getClassLoader());
        //noinspection unchecked
        return arguments != null ? (SparseArray<V>) arguments.getSparseParcelableArray(argName) : null;
    }


    @NotNull
    public static <V extends Serializable> V readSerializableArg(@NotNull android.support.v4.app.Fragment fragment, @NotNull String argName) {
        Bundle arguments = fragment.getArguments();
        //noinspection unchecked
        V serializable = arguments != null ? (V) arguments.getSerializable(argName) : null;
        if (serializable != null) return serializable;
        throw (new IllegalArgumentException("Param '" + argName + "' not found"));
    }

    @NotNull
    public static <V extends Serializable> V readSerializableArg(@NotNull android.support.v4.app.Fragment fragment, @NotNull String argName, @NotNull V defaultValue) {
        Bundle arguments = fragment.getArguments();
        //noinspection unchecked
        V serializable = arguments != null ? (V) arguments.getSerializable(argName) : null;
        return serializable != null ? serializable : defaultValue;
    }

    @Nullable
    public static <V extends Serializable> V readOptionalSerializableArg(@NotNull android.support.v4.app.Fragment fragment, @NotNull String argName) {
        Bundle arguments = fragment.getArguments();
        //noinspection unchecked
        return arguments != null ? (V) arguments.getSerializable(argName) : null;
    }


    @NotNull
    public static Bundle readBundleArg(@NotNull android.support.v4.app.Fragment fragment, @NotNull String argName) {
        Bundle arguments = fragment.getArguments();
        Bundle bundle = arguments != null ? arguments.getBundle(argName) : null;
        if (bundle != null) return bundle;
        throw (new IllegalArgumentException("Param '" + argName + "' not found"));
    }

    @NotNull
    public static Bundle readBundleArg(@NotNull android.support.v4.app.Fragment fragment, @NotNull String argName, @NotNull Bundle defaultValue) {
        Bundle arguments = fragment.getArguments();
        Bundle bundle = arguments != null ? arguments.getBundle(argName) : null;
        return bundle != null ? bundle : defaultValue;
    }

    @Nullable
    public static Bundle readOptionalBundleArg(@NotNull android.support.v4.app.Fragment fragment, @NotNull String argName) {
        Bundle arguments = fragment.getArguments();
        return arguments != null ? arguments.getBundle(argName) : null;
    }


    @NotNull
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static IBinder readBinderArg(@NotNull android.support.v4.app.Fragment fragment, @NotNull String argName) {
        Bundle arguments = fragment.getArguments();
        IBinder iBinder = arguments != null ? arguments.getBinder(argName) : null;
        if (iBinder != null) return iBinder;
        throw (new IllegalArgumentException("Param '" + argName + "' not found"));
    }

    @NotNull
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static IBinder readBinderArg(@NotNull android.support.v4.app.Fragment fragment, @NotNull String argName, @NotNull IBinder defaultValue) {
        Bundle arguments = fragment.getArguments();
        IBinder iBinder = arguments != null ? arguments.getBinder(argName) : null;
        return iBinder != null ? iBinder : defaultValue;
    }

    @Nullable
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static IBinder readOptionalBinderArg(@NotNull android.support.v4.app.Fragment fragment, @NotNull String argName) {
        Bundle arguments = fragment.getArguments();
        return arguments != null ? arguments.getBinder(argName) : null;
    }


    @NotNull
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    public static Size readSizeArg(@NotNull android.support.v4.app.Fragment fragment, @NotNull String argName) {
        Bundle arguments = fragment.getArguments();
        Size size = arguments != null ? arguments.getSize(argName) : null;
        if (size != null) return size;
        throw (new IllegalArgumentException("Param '" + argName + "' not found"));
    }

    @NotNull
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    public static Size readSizeArg(@NotNull android.support.v4.app.Fragment fragment, @NotNull String argName, @NotNull Size defaultValue) {
        Bundle arguments = fragment.getArguments();
        Size size = arguments != null ? arguments.getSize(argName) : null;
        return size != null ? size : defaultValue;
    }

    @Nullable
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    public static Size readOptionalSizeArg(@NotNull android.support.v4.app.Fragment fragment, @NotNull String argName) {
        Bundle arguments = fragment.getArguments();
        return arguments != null ? arguments.getSize(argName) : null;
    }


    @NotNull
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    public static SizeF readSizeFArg(@NotNull android.support.v4.app.Fragment fragment, @NotNull String argName) {
        Bundle arguments = fragment.getArguments();
        SizeF sizeF = arguments != null ? arguments.getSizeF(argName) : null;
        if (sizeF != null) return sizeF;
        throw (new IllegalArgumentException("Param '" + argName + "' not found"));
    }

    @NotNull
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    public static SizeF readSizeFArg(@NotNull android.support.v4.app.Fragment fragment, @NotNull String argName, @NotNull SizeF defaultValue) {
        Bundle arguments = fragment.getArguments();
        SizeF sizeF = arguments != null ? arguments.getSizeF(argName) : null;
        return sizeF != null ? sizeF : defaultValue;
    }

    @Nullable
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    public static SizeF readOptionalSizeFArg(@NotNull android.support.v4.app.Fragment fragment, @NotNull String argName) {
        Bundle arguments = fragment.getArguments();
        return arguments != null ? arguments.getSizeF(argName) : null;
    }


    /* ************************************* OriginFragment Args ***************************************** */


    public static byte readByteArg(@NotNull android.app.Fragment fragment, @NotNull String argName, byte defaultValue) {
        Bundle arguments = fragment.getArguments();
        return arguments != null ? arguments.getByte(argName, defaultValue) : defaultValue;
    }

    @NotNull
    public static byte[] readByteArrayArg(@NotNull android.app.Fragment fragment, @NotNull String argName) {
        Bundle arguments = fragment.getArguments();
        byte[] bytes = arguments != null ? arguments.getByteArray(argName) : null;
        if (bytes != null) return bytes;
        throw (new IllegalArgumentException("Param '" + argName + "' not found"));
    }

    @NotNull
    public static byte[] readByteArrayArg(@NotNull android.app.Fragment fragment, @NotNull String argName, @NotNull byte[] defaultValue) {
        Bundle arguments = fragment.getArguments();
        byte[] bytes = arguments != null ? arguments.getByteArray(argName) : null;
        return bytes != null ? bytes : defaultValue;
    }

    @Nullable
    public static byte[] readOptionalByteArrayArg(@NotNull android.app.Fragment fragment, @NotNull String argName) {
        Bundle arguments = fragment.getArguments();
        return arguments != null ? arguments.getByteArray(argName) : null;
    }


    public static short readShortArg(@NotNull android.app.Fragment fragment, @NotNull String argName, short defaultValue) {
        Bundle arguments = fragment.getArguments();
        return arguments != null ? arguments.getShort(argName, defaultValue) : defaultValue;
    }

    @NotNull
    public static short[] readShortArrayArg(@NotNull android.app.Fragment fragment, @NotNull String argName) {
        Bundle arguments = fragment.getArguments();
        short[] shorts = arguments != null ? arguments.getShortArray(argName) : null;
        if (shorts != null) return shorts;
        throw (new IllegalArgumentException("Param '" + argName + "' not found"));
    }

    @NotNull
    public static short[] readShortArrayArg(@NotNull android.app.Fragment fragment, @NotNull String argName, @NotNull short[] defaultValue) {
        Bundle arguments = fragment.getArguments();
        short[] shorts = arguments != null ? arguments.getShortArray(argName) : null;
        return shorts != null ? shorts : defaultValue;
    }

    @Nullable
    public static short[] readOptionalShortArrayArg(@NotNull android.app.Fragment fragment, @NotNull String argName) {
        Bundle arguments = fragment.getArguments();
        return arguments != null ? arguments.getShortArray(argName) : null;
    }


    public static int readIntArg(@NotNull android.app.Fragment fragment, @NotNull String argName, int defaultValue) {
        Bundle arguments = fragment.getArguments();
        return arguments != null ? arguments.getInt(argName, defaultValue) : defaultValue;
    }

    @NotNull
    public static int[] readIntArrayArg(@NotNull android.app.Fragment fragment, @NotNull String argName) {
        Bundle arguments = fragment.getArguments();
        int[] ints = arguments != null ? arguments.getIntArray(argName) : null;
        if (ints != null) return ints;
        throw (new IllegalArgumentException("Param '" + argName + "' not found"));
    }

    @NotNull
    public static int[] readIntArrayArg(@NotNull android.app.Fragment fragment, @NotNull String argName, @NotNull int[] defaultValue) {
        Bundle arguments = fragment.getArguments();
        int[] ints = arguments != null ? arguments.getIntArray(argName) : null;
        return ints != null ? ints : defaultValue;
    }

    @Nullable
    public static int[] readOptionalIntArrayArg(@NotNull android.app.Fragment fragment, @NotNull String argName) {
        Bundle arguments = fragment.getArguments();
        return arguments != null ? arguments.getIntArray(argName) : null;
    }

    @NotNull
    public static ArrayList<Integer> readIntArrayListArg(@NotNull android.app.Fragment fragment, @NotNull String argName) {
        Bundle arguments = fragment.getArguments();
        ArrayList<Integer> ints = arguments != null ? arguments.getIntegerArrayList(argName) : null;
        if (ints != null) return ints;
        throw (new IllegalArgumentException("Param '" + argName + "' not found"));
    }

    @NotNull
    public static ArrayList<Integer> readIntArrayListArg(@NotNull android.app.Fragment fragment, @NotNull String argName, @NotNull ArrayList<Integer> defaultValue) {
        Bundle arguments = fragment.getArguments();
        ArrayList<Integer> integers = arguments != null ? arguments.getIntegerArrayList(argName) : null;
        return integers != null ? integers : defaultValue;
    }

    @Nullable
    public static ArrayList<Integer> readOptionalIntArrayListArg(@NotNull android.app.Fragment fragment, @NotNull String argName) {
        Bundle arguments = fragment.getArguments();
        return arguments != null ? arguments.getIntegerArrayList(argName) : null;
    }


    public static long readLongArg(@NotNull android.app.Fragment fragment, @NotNull String argName, long defaultValue) {
        Bundle arguments = fragment.getArguments();
        return arguments != null ? arguments.getLong(argName, defaultValue) : defaultValue;
    }

    @NotNull
    public static long[] readLongArrayArg(@NotNull android.app.Fragment fragment, @NotNull String argName) {
        Bundle arguments = fragment.getArguments();
        long[] longs = arguments != null ? arguments.getLongArray(argName) : null;
        if (longs != null) return longs;
        throw (new IllegalArgumentException("Param '" + argName + "' not found"));
    }

    @NotNull
    public static long[] readLongArrayArg(@NotNull android.app.Fragment fragment, @NotNull String argName, @NotNull long[] defaultValue) {
        Bundle arguments = fragment.getArguments();
        long[] longs = arguments != null ? arguments.getLongArray(argName) : null;
        return longs != null ? longs : defaultValue;
    }

    @Nullable
    public static long[] readOptionalLongArrayArg(@NotNull android.app.Fragment fragment, @NotNull String argName) {
        Bundle arguments = fragment.getArguments();
        return arguments != null ? arguments.getLongArray(argName) : null;
    }


    public static float readFloatArg(@NotNull android.app.Fragment fragment, @NotNull String argName, float defaultValue) {
        Bundle arguments = fragment.getArguments();
        return arguments != null ? arguments.getFloat(argName, defaultValue) : defaultValue;
    }

    @NotNull
    public static float[] readFloatArrayArg(@NotNull android.app.Fragment fragment, @NotNull String argName) {
        Bundle arguments = fragment.getArguments();
        float[] floats = arguments != null ? arguments.getFloatArray(argName) : null;
        if (floats != null) return floats;
        throw (new IllegalArgumentException("Param '" + argName + "' not found"));
    }

    @NotNull
    public static float[] readFloatArrayArg(@NotNull android.app.Fragment fragment, @NotNull String argName, @NotNull float[] defaultValue) {
        Bundle arguments = fragment.getArguments();
        float[] floats = arguments != null ? arguments.getFloatArray(argName) : null;
        return floats != null ? floats : defaultValue;
    }

    @Nullable
    public static float[] readOptionalFloatArrayArg(@NotNull android.app.Fragment fragment, @NotNull String argName) {
        Bundle arguments = fragment.getArguments();
        return arguments != null ? arguments.getFloatArray(argName) : null;
    }


    public static double readDoubleArg(@NotNull android.app.Fragment fragment, @NotNull String argName, double defaultValue) {
        Bundle arguments = fragment.getArguments();
        return arguments != null ? arguments.getDouble(argName, defaultValue) : defaultValue;
    }

    @NotNull
    public static double[] readDoubleArrayArg(@NotNull android.app.Fragment fragment, @NotNull String argName) {
        Bundle arguments = fragment.getArguments();
        double[] doubles = arguments != null ? arguments.getDoubleArray(argName) : null;
        if (doubles != null) return doubles;
        throw (new IllegalArgumentException("Param '" + argName + "' not found"));
    }

    @NotNull
    public static double[] readDoubleArrayArg(@NotNull android.app.Fragment fragment, @NotNull String argName, @NotNull double[] defaultValue) {
        Bundle arguments = fragment.getArguments();
        double[] doubles = arguments != null ? arguments.getDoubleArray(argName) : null;
        return doubles != null ? doubles : defaultValue;
    }

    @Nullable
    public static double[] readOptionalDoubleArrayArg(@NotNull android.app.Fragment fragment, @NotNull String argName) {
        Bundle arguments = fragment.getArguments();
        return arguments != null ? arguments.getDoubleArray(argName) : null;
    }


    public static boolean readBooleanArg(@NotNull android.app.Fragment fragment, @NotNull String argName, boolean defaultValue) {
        Bundle arguments = fragment.getArguments();
        return arguments != null ? arguments.getBoolean(argName, defaultValue) : defaultValue;
    }

    @NotNull
    public static boolean[] readBooleanArrayArg(@NotNull android.app.Fragment fragment, @NotNull String argName) {
        Bundle arguments = fragment.getArguments();
        boolean[] booleans = arguments != null ? arguments.getBooleanArray(argName) : null;
        if (booleans != null) return booleans;
        throw (new IllegalArgumentException("Param '" + argName + "' not found"));
    }

    @NotNull
    public static boolean[] readBooleanArrayArg(@NotNull android.app.Fragment fragment, @NotNull String argName, @NotNull boolean[] defaultValue) {
        Bundle arguments = fragment.getArguments();
        boolean[] booleans = arguments != null ? arguments.getBooleanArray(argName) : null;
        return booleans != null ? booleans : defaultValue;
    }

    @Nullable
    public static boolean[] readOptionalBooleanArrayArg(@NotNull android.app.Fragment fragment, @NotNull String argName) {
        Bundle arguments = fragment.getArguments();
        return arguments != null ? arguments.getBooleanArray(argName) : null;
    }


    public static char readCharArg(@NotNull android.app.Fragment fragment, @NotNull String argName, char defaultValue) {
        Bundle arguments = fragment.getArguments();
        return arguments != null ? arguments.getChar(argName, defaultValue) : defaultValue;
    }

    @NotNull
    public static char[] readCharArrayArg(@NotNull android.app.Fragment fragment, @NotNull String argName) {
        Bundle arguments = fragment.getArguments();
        char[] chars = arguments != null ? arguments.getCharArray(argName) : null;
        if (chars != null) return chars;
        throw (new IllegalArgumentException("Param '" + argName + "' not found"));
    }

    @NotNull
    public static char[] readCharArrayArg(@NotNull android.app.Fragment fragment, @NotNull String argName, @NotNull char[] defaultValue) {
        Bundle arguments = fragment.getArguments();
        char[] chars = arguments != null ? arguments.getCharArray(argName) : null;
        return chars != null ? chars : defaultValue;
    }

    @Nullable
    public static char[] readOptionalCharArrayArg(@NotNull android.app.Fragment fragment, @NotNull String argName) {
        Bundle arguments = fragment.getArguments();
        return arguments != null ? arguments.getCharArray(argName) : null;
    }


    @NotNull
    public static CharSequence readCharSequenceArg(@NotNull android.app.Fragment fragment, @NotNull String argName) {
        Bundle arguments = fragment.getArguments();
        CharSequence charSequence = arguments != null ? arguments.getCharSequence(argName) : null;
        if (charSequence != null) return charSequence;
        throw (new IllegalArgumentException("Param '" + argName + "' not found"));
    }

    @NotNull
    public static CharSequence readCharSequenceArg(@NotNull android.app.Fragment fragment, @NotNull String argName, @NotNull CharSequence defaultValue) {
        Bundle arguments = fragment.getArguments();
        CharSequence charSequence = arguments != null ? arguments.getCharSequence(argName, defaultValue) : null;
        return charSequence != null ? charSequence : defaultValue;
    }

    @Nullable
    public static CharSequence readOptionalCharSequenceArg(@NotNull android.app.Fragment fragment, @NotNull String argName) {
        Bundle arguments = fragment.getArguments();
        return arguments != null ? arguments.getCharSequence(argName) : null;
    }

    @NotNull
    public static CharSequence[] readCharSequenceArrayArg(@NotNull android.app.Fragment fragment, @NotNull String argName) {
        Bundle arguments = fragment.getArguments();
        CharSequence[] charSequences = arguments != null ? arguments.getCharSequenceArray(argName) : null;
        if (charSequences != null) return charSequences;
        throw (new IllegalArgumentException("Param '" + argName + "' not found"));
    }

    @NotNull
    public static CharSequence[] readCharSequenceArrayArg(@NotNull android.app.Fragment fragment, @NotNull String argName, @NotNull CharSequence[] defaultValue) {
        Bundle arguments = fragment.getArguments();
        CharSequence[] charSequences = arguments != null ? arguments.getCharSequenceArray(argName) : null;
        return charSequences != null ? charSequences : defaultValue;
    }

    @Nullable
    public static CharSequence[] readOptionalCharSequenceArrayArg(@NotNull android.app.Fragment fragment, @NotNull String argName) {
        Bundle arguments = fragment.getArguments();
        return arguments != null ? arguments.getCharSequenceArray(argName) : null;
    }

    @NotNull
    public static ArrayList<CharSequence> readCharSequenceArrayListArg(@NotNull android.app.Fragment fragment, @NotNull String argName) {
        Bundle arguments = fragment.getArguments();
        ArrayList<CharSequence> charSequences = arguments != null ? arguments.getCharSequenceArrayList(argName) : null;
        if (charSequences != null) return charSequences;
        throw (new IllegalArgumentException("Param '" + argName + "' not found"));
    }

    @NotNull
    public static ArrayList<CharSequence> readCharSequenceArrayListArg(@NotNull android.app.Fragment fragment, @NotNull String argName, @NotNull ArrayList<CharSequence> defaultValue) {
        Bundle arguments = fragment.getArguments();
        ArrayList<CharSequence> charSequences = arguments != null ? arguments.getCharSequenceArrayList(argName) : null;
        return charSequences != null ? charSequences : defaultValue;
    }

    @Nullable
    public static ArrayList<CharSequence> readOptionalCharSequenceArrayListArg(@NotNull android.app.Fragment fragment, @NotNull String argName) {
        Bundle arguments = fragment.getArguments();
        return arguments != null ? arguments.getCharSequenceArrayList(argName) : null;
    }


    @NotNull
    public static String readStringArg(@NotNull android.app.Fragment fragment, @NotNull String argName) {
        Bundle arguments = fragment.getArguments();
        String string = arguments != null ? arguments.getString(argName) : null;
        if (string != null) return string;
        throw (new IllegalArgumentException("Param '" + argName + "' not found"));
    }

    @NotNull
    public static String readStringArg(@NotNull android.app.Fragment fragment, @NotNull String argName, @NotNull String defaultValue) {
        Bundle arguments = fragment.getArguments();
        String string = arguments != null ? arguments.getString(argName, defaultValue) : null;
        return string != null ? string : defaultValue;
    }

    @Nullable
    public static String readOptionalStringArg(@NotNull android.app.Fragment fragment, @NotNull String argName) {
        Bundle arguments = fragment.getArguments();
        return arguments != null ? arguments.getString(argName) : null;
    }

    @NotNull
    public static String[] readStringArrayArg(@NotNull android.app.Fragment fragment, @NotNull String argName) {
        Bundle arguments = fragment.getArguments();
        String[] strings = arguments != null ? arguments.getStringArray(argName) : null;
        if (strings != null) return strings;
        throw (new IllegalArgumentException("Param '" + argName + "' not found"));
    }

    @NotNull
    public static String[] readStringArrayArg(@NotNull android.app.Fragment fragment, @NotNull String argName, @NotNull String[] defaultValue) {
        Bundle arguments = fragment.getArguments();
        String[] strings = arguments != null ? arguments.getStringArray(argName) : null;
        return strings != null ? strings : defaultValue;
    }

    @Nullable
    public static String[] readOptionalStringArrayArg(@NotNull android.app.Fragment fragment, @NotNull String argName) {
        Bundle arguments = fragment.getArguments();
        return arguments != null ? arguments.getStringArray(argName) : null;
    }

    @NotNull
    public static ArrayList<String> readStringArrayListArg(@NotNull android.app.Fragment fragment, @NotNull String argName) {
        Bundle arguments = fragment.getArguments();
        ArrayList<String> strings = arguments != null ? arguments.getStringArrayList(argName) : null;
        if (strings != null) return strings;
        throw (new IllegalArgumentException("Param '" + argName + "' not found"));
    }

    @NotNull
    public static ArrayList<String> readStringArrayListArg(@NotNull android.app.Fragment fragment, @NotNull String argName, @NotNull ArrayList<String> defaultValue) {
        Bundle arguments = fragment.getArguments();
        ArrayList<String> strings = arguments != null ? arguments.getStringArrayList(argName) : null;
        return strings != null ? strings : defaultValue;
    }

    @Nullable
    public static ArrayList<String> readOptionalStringArrayListArg(@NotNull android.app.Fragment fragment, @NotNull String argName) {
        Bundle arguments = fragment.getArguments();
        return arguments != null ? arguments.getStringArrayList(argName) : null;
    }


    @NotNull
    public static <V extends Parcelable> V readParcelableArg(@NotNull android.app.Fragment fragment, @NotNull String argName) {
        Bundle arguments = fragment.getArguments();
        // Because the ClassLoader may be lost when the Bundle is passed in the Bundle, you need to restore it to deserialize the custom Parcelable.
        if (arguments != null) arguments.setClassLoader(Fragmentx.class.getClassLoader());
        //noinspection unchecked
        V parcelable = arguments != null ? (V) arguments.getParcelable(argName) : null;
        if (parcelable != null) return parcelable;
        throw (new IllegalArgumentException("Param '" + argName + "' not found"));
    }

    @NotNull
    public static <V extends Parcelable> V readParcelableArg(@NotNull android.app.Fragment fragment, @NotNull String argName, @NotNull V defaultValue) {
        Bundle arguments = fragment.getArguments();
        // Because the ClassLoader may be lost when the Bundle is passed in the Bundle, you need to restore it to deserialize the custom Parcelable.
        if (arguments != null) arguments.setClassLoader(Fragmentx.class.getClassLoader());
        Parcelable parcelable = arguments != null ? arguments.getParcelable(argName) : null;
        //noinspection unchecked
        return parcelable != null ? (V) parcelable : defaultValue;
    }

    @Nullable
    public static <V extends Parcelable> V readOptionalParcelableArg(@NotNull android.app.Fragment fragment, @NotNull String argName) {
        Bundle arguments = fragment.getArguments();
        // Because the ClassLoader may be lost when the Bundle is passed in the Bundle, you need to restore it to deserialize the custom Parcelable.
        if (arguments != null) arguments.setClassLoader(Fragmentx.class.getClassLoader());
        //noinspection unchecked
        return arguments != null ? (V) arguments.getParcelable(argName) : null;
    }

    @NotNull
    public static <V extends Parcelable> V[] readParcelableArrayArg(@NotNull android.app.Fragment fragment, @NotNull String argName) {
        Bundle arguments = fragment.getArguments();
        // Because the ClassLoader may be lost when the Bundle is passed in the Bundle, you need to restore it to deserialize the custom Parcelable.
        if (arguments != null) arguments.setClassLoader(Fragmentx.class.getClassLoader());
        //noinspection unchecked
        V[] parcelables = arguments != null ? (V[]) arguments.getParcelableArray(argName) : null;
        if (parcelables != null) return parcelables;
        throw (new IllegalArgumentException("Param '" + argName + "' not found"));
    }

    @NotNull
    public static <V extends Parcelable> V[] readParcelableArrayArg(@NotNull android.app.Fragment fragment, @NotNull String argName, @NotNull V[] defaultValue) {
        Bundle arguments = fragment.getArguments();
        // Because the ClassLoader may be lost when the Bundle is passed in the Bundle, you need to restore it to deserialize the custom Parcelable.
        if (arguments != null) arguments.setClassLoader(Fragmentx.class.getClassLoader());
        //noinspection unchecked
        V[] parcelables = arguments != null ? (V[]) arguments.getParcelableArray(argName) : null;
        return parcelables != null ? parcelables : defaultValue;
    }

    @Nullable
    public static <V extends Parcelable> V[] readOptionalParcelableArrayArg(@NotNull android.app.Fragment fragment, @NotNull String argName) {
        Bundle arguments = fragment.getArguments();
        // Because the ClassLoader may be lost when the Bundle is passed in the Bundle, you need to restore it to deserialize the custom Parcelable.
        if (arguments != null) arguments.setClassLoader(Fragmentx.class.getClassLoader());
        //noinspection unchecked
        return arguments != null ? (V[]) arguments.getParcelableArray(argName) : null;
    }

    @NotNull
    public static <V extends Parcelable> ArrayList<V> readParcelableArrayListArg(@NotNull android.app.Fragment fragment, @NotNull String argName) {
        Bundle arguments = fragment.getArguments();
        // Because the ClassLoader may be lost when the Bundle is passed in the Bundle, you need to restore it to deserialize the custom Parcelable.
        if (arguments != null) arguments.setClassLoader(Fragmentx.class.getClassLoader());
        //noinspection unchecked
        ArrayList<V> parcelable = arguments != null ? (ArrayList<V>) arguments.getParcelableArrayList(argName) : null;
        if (parcelable != null) return parcelable;
        throw (new IllegalArgumentException("Param '" + argName + "' not found"));
    }

    @NotNull
    public static <V extends Parcelable> ArrayList<V> readParcelableArrayListArg(@NotNull android.app.Fragment fragment, @NotNull String argName, @NotNull ArrayList<V> defaultValue) {
        Bundle arguments = fragment.getArguments();
        // Because the ClassLoader may be lost when the Bundle is passed in the Bundle, you need to restore it to deserialize the custom Parcelable.
        if (arguments != null) arguments.setClassLoader(Fragmentx.class.getClassLoader());
        //noinspection unchecked
        ArrayList<V> parcelables = arguments != null ? (ArrayList<V>) arguments.getParcelableArrayList(argName) : null;
        return parcelables != null ? parcelables : defaultValue;
    }

    @Nullable
    public static <V extends Parcelable> ArrayList<V> readOptionalParcelableArrayListArg(@NotNull android.app.Fragment fragment, @NotNull String argName) {
        Bundle arguments = fragment.getArguments();
        // Because the ClassLoader may be lost when the Bundle is passed in the Bundle, you need to restore it to deserialize the custom Parcelable.
        if (arguments != null) arguments.setClassLoader(Fragmentx.class.getClassLoader());
        //noinspection unchecked
        return arguments != null ? (ArrayList<V>) arguments.getParcelableArrayList(argName) : null;
    }

    @NotNull
    public static <V extends Parcelable> SparseArray<V> readSparseParcelableArrayArg(@NotNull android.app.Fragment fragment, @NotNull String argName) {
        Bundle arguments = fragment.getArguments();
        // Because the ClassLoader may be lost when the Bundle is passed in the Bundle, you need to restore it to deserialize the custom Parcelable.
        if (arguments != null) arguments.setClassLoader(Fragmentx.class.getClassLoader());
        //noinspection unchecked
        SparseArray<V> sparseArray = arguments != null ? (SparseArray<V>) arguments.getSparseParcelableArray(argName) : null;
        if (sparseArray != null) return sparseArray;
        throw (new IllegalArgumentException("Param '" + argName + "' not found"));
    }

    @NotNull
    public static <V extends Parcelable> SparseArray<V> readSparseParcelableArrayArg(@NotNull android.app.Fragment fragment, @NotNull String argName, @NotNull SparseArray<V> defaultValue) {
        Bundle arguments = fragment.getArguments();
        // Because the ClassLoader may be lost when the Bundle is passed in the Bundle, you need to restore it to deserialize the custom Parcelable.
        if (arguments != null) arguments.setClassLoader(Fragmentx.class.getClassLoader());
        //noinspection unchecked
        SparseArray<V> sparseArray = arguments != null ? (SparseArray<V>) arguments.getSparseParcelableArray(argName) : null;
        return sparseArray != null ? sparseArray : defaultValue;
    }

    @Nullable
    public static <V extends Parcelable> SparseArray<V> readOptionalSparseParcelableArrayArg(@NotNull android.app.Fragment fragment, @NotNull String argName) {
        Bundle arguments = fragment.getArguments();
        // Because the ClassLoader may be lost when the Bundle is passed in the Bundle, you need to restore it to deserialize the custom Parcelable.
        if (arguments != null) arguments.setClassLoader(Fragmentx.class.getClassLoader());
        //noinspection unchecked
        return arguments != null ? (SparseArray<V>) arguments.getSparseParcelableArray(argName) : null;
    }


    @NotNull
    public static <V extends Serializable> V readSerializableArg(@NotNull android.app.Fragment fragment, @NotNull String argName) {
        Bundle arguments = fragment.getArguments();
        //noinspection unchecked
        V serializable = arguments != null ? (V) arguments.getSerializable(argName) : null;
        if (serializable != null) return serializable;
        throw (new IllegalArgumentException("Param '" + argName + "' not found"));
    }

    @NotNull
    public static <V extends Serializable> V readSerializableArg(@NotNull android.app.Fragment fragment, @NotNull String argName, @NotNull V defaultValue) {
        Bundle arguments = fragment.getArguments();
        //noinspection unchecked
        V serializable = arguments != null ? (V) arguments.getSerializable(argName) : null;
        return serializable != null ? serializable : defaultValue;
    }

    @Nullable
    public static <V extends Serializable> V readOptionalSerializableArg(@NotNull android.app.Fragment fragment, @NotNull String argName) {
        Bundle arguments = fragment.getArguments();
        //noinspection unchecked
        return arguments != null ? (V) arguments.getSerializable(argName) : null;
    }


    @NotNull
    public static Bundle readBundleArg(@NotNull android.app.Fragment fragment, @NotNull String argName) {
        Bundle arguments = fragment.getArguments();
        Bundle bundle = arguments != null ? arguments.getBundle(argName) : null;
        if (bundle != null) return bundle;
        throw (new IllegalArgumentException("Param '" + argName + "' not found"));
    }

    @NotNull
    public static Bundle readBundleArg(@NotNull android.app.Fragment fragment, @NotNull String argName, @NotNull Bundle defaultValue) {
        Bundle arguments = fragment.getArguments();
        Bundle bundle = arguments != null ? arguments.getBundle(argName) : null;
        return bundle != null ? bundle : defaultValue;
    }

    @Nullable
    public static Bundle readOptionalBundleArg(@NotNull android.app.Fragment fragment, @NotNull String argName) {
        Bundle arguments = fragment.getArguments();
        return arguments != null ? arguments.getBundle(argName) : null;
    }


    @NotNull
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static IBinder readBinderArg(@NotNull android.app.Fragment fragment, @NotNull String argName) {
        Bundle arguments = fragment.getArguments();
        IBinder iBinder = arguments != null ? arguments.getBinder(argName) : null;
        if (iBinder != null) return iBinder;
        throw (new IllegalArgumentException("Param '" + argName + "' not found"));
    }

    @NotNull
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static IBinder readBinderArg(@NotNull android.app.Fragment fragment, @NotNull String argName, @NotNull IBinder defaultValue) {
        Bundle arguments = fragment.getArguments();
        IBinder iBinder = arguments != null ? arguments.getBinder(argName) : null;
        return iBinder != null ? iBinder : defaultValue;
    }

    @Nullable
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static IBinder readOptionalBinderArg(@NotNull android.app.Fragment fragment, @NotNull String argName) {
        Bundle arguments = fragment.getArguments();
        return arguments != null ? arguments.getBinder(argName) : null;
    }


    @NotNull
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    public static Size readSizeArg(@NotNull android.app.Fragment fragment, @NotNull String argName) {
        Bundle arguments = fragment.getArguments();
        Size size = arguments != null ? arguments.getSize(argName) : null;
        if (size != null) return size;
        throw (new IllegalArgumentException("Param '" + argName + "' not found"));
    }

    @NotNull
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    public static Size readSizeArg(@NotNull android.app.Fragment fragment, @NotNull String argName, @NotNull Size defaultValue) {
        Bundle arguments = fragment.getArguments();
        Size size = arguments != null ? arguments.getSize(argName) : null;
        return size != null ? size : defaultValue;
    }

    @Nullable
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    public static Size readOptionalSizeArg(@NotNull android.app.Fragment fragment, @NotNull String argName) {
        Bundle arguments = fragment.getArguments();
        return arguments != null ? arguments.getSize(argName) : null;
    }


    @NotNull
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    public static SizeF readSizeFArg(@NotNull android.app.Fragment fragment, @NotNull String argName) {
        Bundle arguments = fragment.getArguments();
        SizeF sizeF = arguments != null ? arguments.getSizeF(argName) : null;
        if (sizeF != null) return sizeF;
        throw (new IllegalArgumentException("Param '" + argName + "' not found"));
    }

    @NotNull
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    public static SizeF readSizeFArg(@NotNull android.app.Fragment fragment, @NotNull String argName, @NotNull SizeF defaultValue) {
        Bundle arguments = fragment.getArguments();
        SizeF sizeF = arguments != null ? arguments.getSizeF(argName) : null;
        return sizeF != null ? sizeF : defaultValue;
    }

    @Nullable
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    public static SizeF readOptionalSizeFArg(@NotNull android.app.Fragment fragment, @NotNull String argName) {
        Bundle arguments = fragment.getArguments();
        return arguments != null ? arguments.getSizeF(argName) : null;
    }
}
