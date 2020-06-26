package com.example.actividad4programacinandroid.ui.home;

import android.content.Intent;
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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.actividad4programacinandroid.MapsActivity;
import com.example.actividad4programacinandroid.R;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_home, container, false);

        Button buttonOpc = root.findViewById(R.id.botonOpc);

        buttonOpc.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), MapsActivity.class);

                EditText latitudInput = root.findViewById(R.id.opc1Latitud);
                EditText longitudInput = root.findViewById(R.id.opc1Longitud);

                if(!latitudInput.getText().toString().equals("") && !longitudInput.getText().toString().equals("")) {
                    intent.putExtra("opc", 1);
                    intent.putExtra("LatitudOrigen", latitudInput.getText().toString().equals("") ? 0:Float.parseFloat(latitudInput.getText().toString()));
                    intent.putExtra("LongitudOrigen", longitudInput.getText().toString().equals("") ? 0:Float.parseFloat(longitudInput.getText().toString()));
                    startActivity(intent);
                }else{
                    Toast.makeText(getActivity(), "No puede haber un campo vacio", Toast.LENGTH_SHORT).show();
                }

            }

        });

        return root;
    }
}