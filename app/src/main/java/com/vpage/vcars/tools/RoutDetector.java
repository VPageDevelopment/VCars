package com.vpage.vcars.tools;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.vpage.vcars.R;
import com.vpage.vcars.tools.utils.LogFlag;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RoutDetector {

    private static String TAG = RoutDetector.class.getName();

    private Activity activity;
    private LatLng start;
    private LatLng end;

    public RoutDetector(Activity activity, LatLng start, LatLng end) {
        this.activity = activity;
        this.start = start;
        this.end = end;
    }

    private final List<LatLng> lstLatLng = new ArrayList<>();

    public PolylineOptions showRoute() {

        try {
            String source = start.latitude + "," + start.longitude;
            String destination =end.latitude + "," + end.longitude;
/*
            final StringBuilder url = new StringBuilder("http://maps.googleapis.com/maps/api/directions/xml?origin=" + start.latitude +
                    "," + start.longitude + "&destination=" + end.latitude + "," + end.longitude + "&sensor=false&units=metric&mode=driving");
            final InputStream stream = new URL(url.toString()).openStream();*/



             String path_tracking_url = activity.getResources().getString(R.string.path_tracking_url);

             path_tracking_url = path_tracking_url.replace("{startlat,startlng}",source);
             path_tracking_url = path_tracking_url.replace("{endlat,endlng}",destination);
            final InputStream stream = new URL(path_tracking_url).openStream();


            final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setIgnoringComments(true);

            final DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

            Document document = documentBuilder.parse(stream);
            document.getDocumentElement().normalize();


            final String status = document.getElementsByTagName("status").item(0).getTextContent();
            if (!"OK".equals(status)) {
                return null;
            }


            final Element elementLeg = (Element) document.getElementsByTagName("leg").item(0);
            final NodeList nodeListStep = elementLeg.getElementsByTagName("step");
            final int length = nodeListStep.getLength();

            for (int i = 0; i < length; i++) {
                final Node nodeStep = nodeListStep.item(i);
                if (nodeStep.getNodeType() == Node.ELEMENT_NODE) {
                    final Element elementStep = (Element) nodeStep;

                    decodePolylines(elementStep.getElementsByTagName("points").item(0).getTextContent());

                }
            }

        } catch (Exception e) {
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    String message = activity.getResources().getString(R.string.AlertMessage);
                    VTools.showAlertDialog(activity,message);
                }
            });
        }



        final PolylineOptions polylines = new PolylineOptions();
        polylines.color(Color.BLUE);


        for(final LatLng latLng : lstLatLng)
        {
            polylines.add(latLng);
        }

        return polylines;
    }


    /** METHODE QUI DECODE LES POINTS EN LAT-LONG**/
    private void decodePolylines(final String encodedPoints)
    {
        int index = 0;
        int lat = 0, lng = 0;

        while (index < encodedPoints.length())
        {
            int b, shift = 0, result = 0;

            do
            {
                b = encodedPoints.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);

            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;
            shift = 0;
            result = 0;

            do
            {
                b = encodedPoints.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);

            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;
            lstLatLng.add(new LatLng((double)lat/1E5, (double)lng/1E5));
        }
    }




    private int getNodeIndex(NodeList nodeList, String nodeName) {
        for(int i = 0 ; i < nodeList.getLength() ; i++) {
            if(nodeList.item(i).getNodeName().equals(nodeName))
                return i;
        }
        return -1;
    }

}
