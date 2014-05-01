package org.lucasr.layoutsamples.util;

import android.content.Context;
import android.content.res.Resources;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;

public final class RawResource {
    public static JSONArray getAsJSON(Context context, int id) throws IOException {
        InputStreamReader reader = null;

        try {
            final Resources res = context.getResources();
            final InputStream is = res.openRawResource(id);
            if (is == null) {
                return null;
            }

            reader = new InputStreamReader(is);

            final char[] buffer = new char[1024];
            final StringWriter s = new StringWriter();

            int n;
            while ((n = reader.read(buffer, 0, buffer.length)) != -1) {
                s.write(buffer, 0, n);
            }

            return new JSONArray(s.toString());
        } catch (JSONException e) {
            return null;
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }
}