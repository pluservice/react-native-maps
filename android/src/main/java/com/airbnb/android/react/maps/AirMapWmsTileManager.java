package com.airbnb.android.react.maps;

import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;

public class AirMapWmsTileManager extends ViewGroupManager<AirMapWmsTile> {
    private DisplayMetrics metrics;

    public AirMapWmsTileManager(ReactApplicationContext reactContext) {
        super();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            metrics = new DisplayMetrics();
            ((WindowManager) reactContext.getSystemService(Context.WINDOW_SERVICE))
                    .getDefaultDisplay()
                    .getRealMetrics(metrics);
        } else {
            metrics = reactContext.getResources().getDisplayMetrics();
        }
    }

    @Override
    public String getName() {
        return "AIRMapWmsTile";
    }

    @Override
    public AirMapWmsTile createViewInstance(ThemedReactContext context) {
        return new AirMapWmsTile(context);
    }

    @ReactProp(name = "urlTemplate")
    public void setUrlTemplate(AirMapWmsTile view, String urlTemplate) {
        view.setUrlTemplate(urlTemplate);
    }

    @ReactProp(name = "zIndex", defaultFloat = -1.0f)
    public void setZIndex(AirMapWmsTile view, float zIndex) {
        view.setZIndex(zIndex);
    }

}
