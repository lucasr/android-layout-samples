/*
 * Copyright (C) 2014 Lucas Rocha
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.lucasr.layoutsamples.canvas;

import android.os.Build;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.TextDirectionHeuristic;
import android.text.TextDirectionHeuristics;
import android.text.TextPaint;
import android.text.TextUtils.TruncateAt;
import android.util.Log;

import java.lang.reflect.Constructor;

public class StaticLayoutWithMaxLines {
    private static final String LOGTAG = "StaticLayoutWithMaxLines";

    private static final String TEXT_DIR_CLASS = "android.text.TextDirectionHeuristic";
    private static final String TEXT_DIRS_CLASS = "android.text.TextDirectionHeuristics";
    private static final String TEXT_DIR_FIRSTSTRONG_LTR = "FIRSTSTRONG_LTR";

    private static boolean sInitialized;

    private static Constructor<StaticLayout> sConstructor;
    private static Object[] sConstructorArgs;
    private static Object sTextDirection;

    public static synchronized void ensureInitialized() {
        if (sInitialized) {
            return;
        }

        try {
            final Class<?> textDirClass;
            if (Build.VERSION.SDK_INT >= 18) {
                textDirClass = TextDirectionHeuristic.class;
                sTextDirection = TextDirectionHeuristics.FIRSTSTRONG_LTR;
            } else {
                final ClassLoader loader = StaticLayoutWithMaxLines.class.getClassLoader();
                textDirClass = loader.loadClass(TEXT_DIR_CLASS);

                final Class<?> textDirsClass = loader.loadClass(TEXT_DIRS_CLASS);
                sTextDirection = textDirsClass.getField(TEXT_DIR_FIRSTSTRONG_LTR)
                                              .get(textDirsClass);
            }

            final Class<?>[] signature = new Class[] {
                    CharSequence.class,
                    int.class,
                    int.class,
                    TextPaint.class,
                    int.class,
                    Alignment.class,
                    textDirClass,
                    float.class,
                    float.class,
                    boolean.class,
                    TruncateAt.class,
                    int.class,
                    int.class
            };

            // Make the StaticLayout constructor with max lines public
            sConstructor = StaticLayout.class.getDeclaredConstructor(signature);
            sConstructor.setAccessible(true);
            sConstructorArgs = new Object[signature.length];
        } catch (NoSuchMethodException e) {
            Log.e(LOGTAG, "StaticLayout constructor with max lines not found.", e);
        } catch (ClassNotFoundException e) {
            Log.e(LOGTAG, "TextDirectionHeuristic class not found.", e);
        } catch (NoSuchFieldException e) {
            Log.e(LOGTAG, "TextDirectionHeuristics.FIRSTSTRONG_LTR not found.", e);
        } catch (IllegalAccessException e) {
            Log.e(LOGTAG, "TextDirectionHeuristics.FIRSTSTRONG_LTR not accessible.", e);
        } finally {
            sInitialized = true;
        }
    }

    public static boolean isSupported() {
        if (Build.VERSION.SDK_INT < 14) {
            return false;
        }

        ensureInitialized();
        return (sConstructor != null);
    }

    public static synchronized StaticLayout create(CharSequence source, int bufstart, int bufend,
                                                   TextPaint paint, int outerWidth, Alignment align,
                                                   float spacingMult, float spacingAdd,
                                                   boolean includePad, TruncateAt ellipsize,
                                                   int ellipsisWidth, int maxLines) {
        ensureInitialized();

        try {
            sConstructorArgs[0] = source;
            sConstructorArgs[1] = bufstart;
            sConstructorArgs[2] = bufend;
            sConstructorArgs[3] = paint;
            sConstructorArgs[4] = outerWidth;
            sConstructorArgs[5] = align;
            sConstructorArgs[6] = sTextDirection;
            sConstructorArgs[7] = spacingMult;
            sConstructorArgs[8] = spacingAdd;
            sConstructorArgs[9] = includePad;
            sConstructorArgs[10] = ellipsize;
            sConstructorArgs[11] = ellipsisWidth;
            sConstructorArgs[12] = maxLines;

            return sConstructor.newInstance(sConstructorArgs);
        } catch (Exception e) {
            throw new IllegalStateException("Error creating StaticLayout with max lines: " + e);
        }
    }
}
