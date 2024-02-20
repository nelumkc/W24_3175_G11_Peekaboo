package com.example.w24_3175_g11_peekaboo;

import android.content.Context;
import android.content.res.AssetManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class JsonUtils {
    public static String loadJSONFromAsset(Context context, String fileName) {
        String json;
        try {
            InputStream is = context.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public static JSONArray loadJSONArrayFromAsset(Context context, String fileName) {
        try {
            String json = loadJSONFromAsset(context, fileName);
            if (json != null) {
                return new JSONArray(json);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
