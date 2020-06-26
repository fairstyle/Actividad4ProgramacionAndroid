package com.example.actividad4programacinandroid.ui.gallery;

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

public class GalleryFragment extends Fragment {

    private GalleryViewModel galleryViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel = ViewModelProviders.of(this).get(GalleryViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_gallery, container, false);

        Button buttonOpc = root.findViewById(R.id.botonOpc);

        buttonOpc.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), MapsActivity.class);

                EditText latitudOrigenInput = root.findViewById(R.id.opc2latitudOrigen);
                EditText longitudOrigenInput = root.findViewById(R.id.opc2longitudOrigen);

                EditText latitudDestinoInput = root.findViewById(R.id.opc2latitudDestino);
                EditText longitudDestinoInput = root.findViewById(R.id.opc2longitudDestino);

                if(!latitudOrigenInput.getText().toString().equals("") && !longitudOrigenInput.getText().toString().equals("") && !latitudDestinoInput.getText().toString().equals("") && !longitudDestinoInput.getText().toString().equals("")) {
                    intent.putExtra("opc", 2);

                    intent.putExtra("LatitudOrigen", latitudOrigenInput.getText().toString().equals("") ? 0:Float.parseFloat(latitudOrigenInput.getText().toString()));
                    intent.putExtra("LongitudOrigen", longitudOrigenInput.getText().toString().equals("") ? 0:Float.parseFloat(longitudOrigenInput.getText().toString()));

                    intent.putExtra("LatitudDestino", latitudDestinoInput.getText().toString().equals("") ? 0:Float.parseFloat(latitudDestinoInput.getText().toString()));
                    intent.putExtra("LongitudDestino", longitudDestinoInput.getText().toString().equals("") ? 0:Float.parseFloat(longitudDestinoInput.getText().toString()));
                    startActivity(intent);
                }else{
                    Toast.makeText(getActivity(), "No puede haber un campo vacio", Toast.LENGTH_SHORT).show();
                }

            }

        });

        return root;
    }
}