package com.example.actividad4programacinandroid.ui.slideshow;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.actividad4programacinandroid.MapsActivity;
import com.example.actividad4programacinandroid.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.concurrent.Executor;

public class SlideshowFragment extends Fragment {

    private SlideshowViewModel slideshowViewModel;

    private static final int PERMISSIONS_FINE_LOCATION = 99;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;
    LatLng opc3Origen;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                ViewModelProviders.of(this).get(SlideshowViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_slideshow, container, false);

        Button buttonOpc = root.findViewById(R.id.botonOpc);

        buttonOpc.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), MapsActivity.class);

                EditText latitudDestinoInput = root.findViewById(R.id.opc3Latitud);
                EditText longitudDestinoInput = root.findViewById(R.id.opc3Longitud);

                if(!latitudDestinoInput.getText().toString().equals("") && !longitudDestinoInput.getText().toString().equals("")) {
                    intent.putExtra("opc", 3);

                    // CORDENADAS MEDIANTE GPS, me manda a la punta del cerro en el emulador, pero las cordenadas se obtienen.
                    //System.out.print("Cordenadas Origen Mediante GPS: ");
                    //System.out.println(opc3Origen.toString());

                    intent.putExtra("LatitudOrigen", (float)opc3Origen.latitude);
                    intent.putExtra("LongitudOrigen", (float)opc3Origen.longitude);

                    intent.putExtra("LatitudDestino", latitudDestinoInput.getText().toString().equals("") ? 0:Float.parseFloat(latitudDestinoInput.getText().toString()));
                    intent.putExtra("LongitudDestino", longitudDestinoInput.getText().toString().equals("") ? 0:Float.parseFloat(longitudDestinoInput.getText().toString()));
                    startActivity(intent);
                }else{
                    Toast.makeText(getActivity(), "No puede haber un campo vacio", Toast.LENGTH_SHORT).show();
                }

            }

        });

        locationRequest = new LocationRequest();
        locationRequest.setInterval(30000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        actualizarGPS();

        return root;
    }

    private void actualizarGPS(){
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    opc3Origen = new LatLng(location.getLatitude(), location.getLongitude());
                }
            });
        }else{
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_FINE_LOCATION);
            }
            actualizarGPS();
        }
    }
}