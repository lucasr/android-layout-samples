/*
 * Copyright (C) 2007 The Android Open Source Project
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

import android.util.Log;
import android.view.InflateException;
import android.view.ViewGroup.LayoutParams;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.util.AttributeSet;
import android.util.Xml;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.HashMap;

public class UIElementInflater {
    private static final String LOGTAG = "UIElementInflater";
    private static final boolean DEBUG = false;

    private final Context mContext;
    private final Object[] mConstructorArgs = new Object[2];

    private static UIElementInflater sInstance;

    private static final Class<?>[] sConstructorSignature = new Class[] {
        UIElementHost.class,
        AttributeSet.class
    };

    private static final HashMap<String, Constructor<? extends UIElement>> sConstructorMap =
            new HashMap<String, Constructor<? extends UIElement>>();

    private static final String TAG_MERGE = "merge";

    public static synchronized UIElementInflater from(Context context) {
        if (sInstance == null) {
            sInstance = new UIElementInflater(context.getApplicationContext());
        }

        return sInstance;
    }

    protected UIElementInflater(Context context) {
        mContext = context;
    }

    public Context getContext() {
        return mContext;
    }

    public UIElement inflate(int resource, UIElementHost host, UIElementGroup root) {
        return inflate(resource, host, root, root != null);
    }

    public UIElement inflate(XmlPullParser parser, UIElementHost host, UIElementGroup root) {
        return inflate(parser, host, root, root != null);
    }

    public UIElement inflate(int resource, UIElementHost host, UIElementGroup root,
                             boolean attachToRoot) {
        if (DEBUG) {
            Log.d(LOGTAG, "INFLATING from resource: " + resource);
        }

        XmlResourceParser parser = getContext().getResources().getLayout(resource);
        try {
            return inflate(parser, host, root, attachToRoot);
        } finally {
            parser.close();
        }
    }

    public UIElement inflate(XmlPullParser parser, UIElementHost host,
                             UIElementGroup root, boolean attachToRoot) {
        synchronized (mConstructorArgs) {
            final AttributeSet attrs = Xml.asAttributeSet(parser);
            mConstructorArgs[0] = host;

            UIElement result = root;

            try {
                // Look for the root node.
                int type;
                while ((type = parser.next()) != XmlPullParser.START_TAG &&
                        type != XmlPullParser.END_DOCUMENT) {
                    // Empty
                }

                if (type != XmlPullParser.START_TAG) {
                    throw new InflateException(parser.getPositionDescription()
                            + ": No start tag found!");
                }

                final String name = parser.getName();

                if (DEBUG) {
                    Log.d(LOGTAG, "**************************");
                    Log.d(LOGTAG, "Creating root view: " + name);
                    Log.d(LOGTAG, "**************************");
                }

                if (TAG_MERGE.equals(name)) {
                    if (root == null || !attachToRoot) {
                        throw new InflateException("<merge /> can be used only with a valid "
                                + "ViewGroup root and attachToRoot=true");
                    }

                    rInflate(parser, root, attrs, false);
                } else {
                    // Temp is the root view that was found in the xml
                    UIElement temp = createViewFromTag(root, name, attrs);
                    LayoutParams params = null;

                    if (root != null) {
                        if (DEBUG) {
                            Log.d(LOGTAG, "Creating params from root: " + root);
                        }

                        // Create layout params that match root, if supplied
                        params = root.generateLayoutParams(attrs);
                        if (!attachToRoot) {
                            // Set the layout params for temp if we are not
                            // attaching. (If we are, we use addView, below)
                            temp.setLayoutParams(params);
                        }
                    }

                    if (DEBUG) {
                        Log.d(LOGTAG, "-----> start inflating children");
                    }

                    // Inflate all children under temp
                    rInflate(parser, temp, attrs, true);
                    if (DEBUG) {
                        Log.d(LOGTAG, "-----> done inflating children");
                    }

                    // We are supposed to attach all the views we found (int temp)
                    // to root. Do that now.
                    if (root != null && attachToRoot) {
                        root.addElement(temp, params);
                    }

                    // Decide whether to return the root that was passed in or the
                    // top view found in xml.
                    if (root == null || !attachToRoot) {
                        result = temp;
                    }
                }
            } catch (XmlPullParserException e) {
                InflateException ex = new InflateException(e.getMessage());
                ex.initCause(e);
                throw ex;
            } catch (IOException e) {
                InflateException ex = new InflateException(
                        parser.getPositionDescription()
                                + ": " + e.getMessage());
                ex.initCause(e);
                throw ex;
            } finally {
                // Don't retain static reference on host.
                mConstructorArgs[0] = null;
                mConstructorArgs[1] = null;
            }

            return result;
        }
    }

    public final UIElement createElement(String name, String prefix, AttributeSet attrs)
            throws ClassNotFoundException, InflateException {
        Constructor<? extends UIElement> constructor = sConstructorMap.get(name);
        Class<? extends UIElement> clazz = null;

        try {
            if (constructor == null) {
                // Class not found in the cache, see if it's real, and try to add it
                clazz = mContext.getClassLoader().loadClass(
                        prefix != null ? (prefix + name) : name).asSubclass(UIElement.class);

                constructor = clazz.getConstructor(sConstructorSignature);
                sConstructorMap.put(name, constructor);
            }

            Object[] args = mConstructorArgs;
            args[1] = attrs;

            return constructor.newInstance(args);
        } catch (NoSuchMethodException e) {
            InflateException ie = new InflateException(attrs.getPositionDescription()
                    + ": Error inflating class "
                    + (prefix != null ? (prefix + name) : name));
            ie.initCause(e);
            throw ie;
        } catch (ClassCastException e) {
            // If loaded class is not a View subclass
            InflateException ie = new InflateException(attrs.getPositionDescription()
                    + ": Class is not a View "
                    + (prefix != null ? (prefix + name) : name));
            ie.initCause(e);
            throw ie;
        } catch (ClassNotFoundException e) {
            // If loadClass fails, we should propagate the exception.
            throw e;
        } catch (Exception e) {
            InflateException ie = new InflateException(attrs.getPositionDescription()
                    + ": Error inflating class "
                    + (clazz == null ? "<unknown>" : clazz.getName()));
            ie.initCause(e);
            throw ie;
        }
    }

    protected UIElement onCreateElement(String name, AttributeSet attrs)
            throws ClassNotFoundException {
        return createElement(name, "org.lucasr.layoutsamples.canvas.", attrs);
    }

    protected UIElement onCreateElement(UIElement parent, String name, AttributeSet attrs)
            throws ClassNotFoundException {
        return onCreateElement(name, attrs);
    }

    UIElement createViewFromTag(UIElement parent, String name, AttributeSet attrs) {
        if (name.equals("element")) {
            name = attrs.getAttributeValue(null, "class");
        }

        if (DEBUG) {
            Log.d(LOGTAG, "******** Creating view: " + name);
        }

        try {
            final UIElement element;
            if (-1 == name.indexOf('.')) {
                element = onCreateElement(parent, name, attrs);
            } else {
                element = createElement(name, null, attrs);
            }

            if (DEBUG) {
                Log.d(LOGTAG, "Created view is: " + element);
            }

            return element;
        } catch (InflateException e) {
            throw e;
        } catch (ClassNotFoundException e) {
            InflateException ie = new InflateException(attrs.getPositionDescription()
                    + ": Error inflating class " + name);
            ie.initCause(e);
            throw ie;
        } catch (Exception e) {
            InflateException ie = new InflateException(attrs.getPositionDescription()
                    + ": Error inflating class " + name);
            ie.initCause(e);
            throw ie;
        }
    }

    void rInflate(XmlPullParser parser, UIElement parent, final AttributeSet attrs,
                  boolean finishInflate) throws XmlPullParserException, IOException {
        final int depth = parser.getDepth();
        int type;

        while (((type = parser.next()) != XmlPullParser.END_TAG ||
                parser.getDepth() > depth) && type != XmlPullParser.END_DOCUMENT) {

            if (type != XmlPullParser.START_TAG) {
                continue;
            }

            final String name = parser.getName();

            if (TAG_MERGE.equals(name)) {
                throw new InflateException("<merge /> must be the root element");
            } else {
                final UIElement element = createViewFromTag(parent, name, attrs);
                final UIElementGroup elementGroup = (UIElementGroup) parent;
                final LayoutParams params = elementGroup.generateLayoutParams(attrs);
                rInflate(parser, element, attrs, true);
                elementGroup.addElement(element, params);
            }
        }

        if (finishInflate) {
            parent.onFinishInflate();
        }
    }
}
