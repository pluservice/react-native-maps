package com.airbnb.android.react.maps;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.UrlTileProvider;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;


public class AirMapWmsTile extends AirMapFeature {

//    final String OSGEO_WMS ="http://www.mobilitami.it/tp/MOBILITAMI/xml/hostMOBI.ashx"+
//            "?SERVICE=WMS&REQUEST=GetMap&VERSION=1.1.1"+
//            "&LAYERS=iSosta:parcheggi_test&STYLES="+
//            "&FORMAT=image/png&TRANSPARENT=true&HEIGHT=256&WIDTH=256&ZINDEX=50000"+
//            "&SRS=EPSG:900913"+
//            "&BBOX=%f,%f,%f,%f";

    class AIRMapWmsTileProvider extends UrlTileProvider
    {
        private String urlTemplate;
        // Web Mercator n/w corner of the map.
        private final double[] TILE_ORIGIN = {-20037508.34789244, 20037508.34789244};
        //array indexes for that data
        private static final int ORIG_X = 0;
        private static final int ORIG_Y = 1; // "

        // Size of square world map in meters, using WebMerc projection.
        private static final double MAP_SIZE = 20037508.34789244 * 2;

        // array indexes for array to hold bounding boxes.
        static final int MINX = 0;
        static final int MAXX = 1;
        static final int MINY = 2;
        static final int MAXY = 3;

        public AIRMapWmsTileProvider(int width, int height, String urlTemplate) {
            super(width, height);
            this.urlTemplate = urlTemplate;

        }

        // Return a web Mercator bounding box given tile x/y indexes and a zoom
        // level.
        double[] getBoundingBox(int x, int y, int zoom) {
            double tileSize = MAP_SIZE / Math.pow(2, zoom);
            double minx = TILE_ORIGIN[ORIG_X] + x * tileSize;
            double maxx = TILE_ORIGIN[ORIG_X] + (x+1) * tileSize;
            double miny = TILE_ORIGIN[ORIG_Y] - (y+1) * tileSize;
            double maxy = TILE_ORIGIN[ORIG_Y] - y * tileSize;

            double[] bbox = new double[4];
            bbox[MINX] = minx;
            bbox[MINY] = miny;
            bbox[MAXX] = maxx;
            bbox[MAXY] = maxy;

            return bbox;
        }

        @Override
        public synchronized URL getTileUrl(int x, int y, int zoom) {
            double[] bbox = getBoundingBox(x, y, zoom);
            String s = String.format(Locale.US, this.urlTemplate,  bbox[MINX],
                    bbox[MINY], bbox[MAXX], bbox[MAXY]);

            URL url = null;
            try {
                url = new URL(s);
            } catch (MalformedURLException e) {
                throw new AssertionError(e);
            }
            return url;
        }

        public void setUrlTemplate(String urlTemplate) {
            this.urlTemplate = urlTemplate;
        }
    }

    private TileOverlayOptions tileOverlayOptions;
    private TileOverlay tileOverlay;
    private AIRMapWmsTileProvider tileProvider;

    private String urlTemplate;
    private float zIndex;

    public AirMapWmsTile(Context context) {
        super(context);
    }

    public void setUrlTemplate(String urlTemplate) {
        this.urlTemplate = urlTemplate;
        if (tileProvider != null) {
            tileProvider.setUrlTemplate(urlTemplate);
        }
        if (tileOverlay != null) {
            tileOverlay.clearTileCache();
        }
    }

    public void setZIndex(float zIndex) {
        this.zIndex = zIndex;
        if (tileOverlay != null) {
            tileOverlay.setZIndex(zIndex);
        }
    }

    public TileOverlayOptions getTileOverlayOptions() {
        if (tileOverlayOptions == null) {
            tileOverlayOptions = createTileOverlayOptions();
        }
        return tileOverlayOptions;
    }

    private TileOverlayOptions createTileOverlayOptions() {
        TileOverlayOptions options = new TileOverlayOptions();
        options.zIndex(zIndex);
        this.tileProvider = new AIRMapWmsTileProvider(256, 256, this.urlTemplate);
        options.tileProvider(this.tileProvider);
        return options;
    }

    @Override
    public Object getFeature() {
        return tileOverlay;
    }

    @Override
    public void addToMap(GoogleMap map) {
        this.tileOverlay = map.addTileOverlay(getTileOverlayOptions());
    }

    @Override
    public void removeFromMap(GoogleMap map) {
        tileOverlay.remove();
    }
}
