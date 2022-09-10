package com.appdoptame.appdoptame.view.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.appdoptame.appdoptame.R;
import com.appdoptame.appdoptame.data.firestore.UserRepositoryFS;
import com.appdoptame.appdoptame.data.listener.CompleteListener;
import com.appdoptame.appdoptame.model.User;
import com.appdoptame.appdoptame.util.EditTextExtractor;
import com.appdoptame.appdoptame.util.StatusBarHeightGetter;
import com.appdoptame.appdoptame.view.fragmentcontroller.FragmentController;
import com.appdoptame.appdoptame.view.fragmentcontroller.SetFragmentMain;

import java.util.concurrent.TimeUnit;

public class FragmentCreateUser extends Fragment {
    private ConstraintLayout statusBar;
    private ImageButton      backButton;
    private EditText         nameField;
    private EditText         lastNameField;
    private EditText         ageField;
    private EditText         CCField;
    private EditText         phoneField;
    private TextView         registerButton;

    public FragmentCreateUser(){

    }

    @SuppressLint("InflateParams") @Nullable @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        postponeEnterTransition(1, TimeUnit.MILLISECONDS);
        return inflater.inflate(R.layout.fragment_create_user, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadComponents();
    }

    private void loadComponents(){
        statusBar            = requireView().findViewById(R.id.create_user_status_bar);
        backButton           = requireView().findViewById(R.id.create_user_back_button);
        nameField            = requireView().findViewById(R.id.create_user_name_field);
        lastNameField        = requireView().findViewById(R.id.create_user_last_name_field);
        ageField             = requireView().findViewById(R.id.create_user_age_field);
        phoneField           = requireView().findViewById(R.id.create_user_phone_field);
        CCField              = requireView().findViewById(R.id.create_user_identification_field);
        registerButton       = requireView().findViewById(R.id.create_user_create_user_button);

        loadStatusBar();
        loadBackButton();
        loadRegisterButton();
    }

    private void loadRegisterButton(){
        registerButton.setOnClickListener(v -> {
            try {
                String name            = EditTextExtractor.get(nameField);
                String lastName        = EditTextExtractor.get(lastNameField);
                String phone           = EditTextExtractor.get(phoneField);
                long   age             = Long.parseLong(EditTextExtractor.get(ageField));
                String identification  = EditTextExtractor.get(CCField);
                String city            = "Medellin";  // Temporalmente mientras añadimos mas ciudades
                String department      = "Antioquia"; // Temporalmente mientras añadimos mas departamentos

                User newUser = new User(
                        identification,
                        name,
                        lastName,
                        phone,
                        city,
                        department,
                        null, // Imagen nula de momento
                        age
                        );

                // Se crea el usuario y se envía a la base de datos
                UserRepositoryFS.getInstance().createUser(newUser, new CompleteListener() {
                    @Override
                    public void onSuccess() {
                        FragmentController.removeAllFragments();
                        SetFragmentMain.set();
                    }

                    @Override
                    public void onFailure() {

                    }
                });
            } catch (Exception e){

            }
        });
    }

    private void loadBackButton(){
        backButton.setOnClickListener(v -> FragmentController.onBackPressed());
    }

    private void loadStatusBar(){
        // Añadiendo padding superior en base a la altura de la barra de notificaciones
        ConstraintLayout.LayoutParams params =
                (ConstraintLayout.LayoutParams) statusBar.getLayoutParams();
        params.topMargin = StatusBarHeightGetter.get();
        statusBar.setLayoutParams(params);
    }
}