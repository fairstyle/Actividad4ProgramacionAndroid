package com.example.actividad4programacinandroid;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int PERMISSIONS_FINE_LOCATION = 99;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Bundle extras = getIntent().getExtras();
        if(extras != null){

            Toast.makeText(this, "Buscando cordenadas", Toast.LENGTH_SHORT).show();

            LatLng origen = new LatLng(extras.getFloat("LatitudOrigen"),extras.getFloat("LongitudOrigen"));

            switch (extras.getInt("opc")){
                case 1:{break;}
                case 2:{}
                case 3:{
                    float latitudDestino = extras.getFloat("LatitudDestino");
                    float longitudDestino = extras.getFloat("LongitudDestino");
                    LatLng destino = new LatLng(latitudDestino, longitudDestino);

                    mMap.addMarker(new MarkerOptions().position(destino).title("Destino"));
                    TaskRequestDirections taskRequestDirections = new TaskRequestDirections();
                    taskRequestDirections.execute(getURL(origen, destino));

                    System.out.println("Origen :"+origen.toString());
                    System.out.println("Destino :"+destino.toString());
                    break;
                }

                default:
                    Toast.makeText(this, "Esto no deberia pasar. intentalo nuevamente", Toast.LENGTH_SHORT).show();
            }

            if(extras.getInt("opc") == 1 || extras.getInt("opc") == 2 || extras.getInt("opc") == 3){
                mMap.addMarker(new MarkerOptions().position(origen).title("Origen"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(origen));
                mMap.animateCamera(CameraUpdateFactory.zoomIn());
                mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(origen)      // Sets the center of the map to Mountain View
                        .zoom(17)                   // Sets the zoom
                        .bearing(90)                // Sets the orientation of the camera to east
                        .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                        .build();                   // Creates a CameraPosition from the builder
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }


        }else
            Toast.makeText(this, "No ingresaste cordenadas, vuelve atras y intentalo nuevamente.", Toast.LENGTH_SHORT).show();

    }

    private String getURL(LatLng origen, LatLng destino){

        String url = "";

        url += "https://maps.googleapis.com/maps/api/directions/"; //URL BASE
        url += "json"; //OUTPUT
        url += "?origin="+origen.latitude+","+origen.longitude;; //Origen
        url += "&destination="+destino.latitude+","+destino.longitude; //Destino
        url += "&sensor=false"; //Sensor
        url += "&mode=driving"; //Modo
        url += "&key="+getString(R.string.google_maps_key); //KEY

        System.out.println(url);
        return url;

    }

    private String getDataFromURL(String strUrl) throws IOException {

        String data = "";
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;

        try{

            URL url = new URL(strUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();

            inputStream = urlConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuffer stringBuffer = new StringBuffer();
            String linea = "";

            while((linea = bufferedReader.readLine()) != null)
                stringBuffer.append(linea);


            data = stringBuffer.toString();
            bufferedReader.close();
            inputStreamReader.close();

        }catch(Exception e){
            e.printStackTrace();
        }finally {
            if(inputStream != null)
                inputStream.close();

            urlConnection.disconnect();
        }

        return data;

    }

    public class TaskRequestDirections extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            String responseString = "";

            try {
                responseString = getDataFromURL(strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return responseString;
        }

        @Override
        protected void onPostExecute(String s){
            super.onPostExecute(s);

            TaskParser taskParser = new TaskParser();
            taskParser.execute(s);
        }

    }

    public class TaskParser extends AsyncTask<String, Void, List<List<HashMap<String, String>>>>{

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... strings) {

            JSONObject jsonObject = null;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jsonObject = new JSONObject(strings[0]);
                DirectionsParser directionsParser = new DirectionsParser();
                routes = directionsParser.parse(jsonObject);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return routes;

        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> lists){
            super.onPostExecute(lists);

            ArrayList points = null;
            PolylineOptions polylineOptions = null;

            for (List<HashMap<String, String>> path : lists){
                points = new ArrayList();
                polylineOptions = new PolylineOptions();

                for(HashMap<String, String> point : path){
                    double lat = Double.parseDouble(point.get("lat"));
                    double lon = Double.parseDouble(point.get("lon"));
                    points.add(new LatLng(lat, lon));
                }

                polylineOptions.addAll(points);
                polylineOptions.width(15);
                polylineOptions.color(Color.RED);
                polylineOptions.geodesic(true);

            }

            if(polylineOptions != null)
                mMap.addPolyline(polylineOptions);

            else
                Toast.makeText(getApplicationContext(), "Destino no encontrada.", Toast.LENGTH_SHORT).show();

        }
    }

}