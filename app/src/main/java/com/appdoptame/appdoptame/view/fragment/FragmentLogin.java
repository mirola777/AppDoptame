package com.appdoptame.appdoptame.view.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.concurrent.TimeUnit;

import com.appdoptame.appdoptame.R;
import com.appdoptame.appdoptame.view.fragmentcontroller.SetFragmentSingUp;

import de.hdodenhof.circleimageview.CircleImageView;

public class FragmentLogin extends Fragment {
    private EditText        emailField;
    private EditText        passwordField;
    private TextView        loginButton;
    private TextView        singUpButton;
    private CheckBox        rememberCheckBox;
    private CircleImageView facebookButton;
    private CircleImageView googleButton;

    @SuppressLint("InflateParams") @Nullable @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        postponeEnterTransition(1, TimeUnit.MILLISECONDS);
        return inflater.inflate(R.layout.fragment_login, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadComponents();
    }

    private void loadComponents(){
        emailField       = requireView().findViewById(R.id.login_email_field);
        passwordField    = requireView().findViewById(R.id.login_password_field);
        loginButton      = requireView().findViewById(R.id.login_login_button);
        singUpButton     = requireView().findViewById(R.id.login_sing_up_button);
        rememberCheckBox = requireView().findViewById(R.id.login_remember_me_checkbox);
        facebookButton   = requireView().findViewById(R.id.login_facebook_button);
        googleButton     = requireView().findViewById(R.id.login_google_button);

        loadFacebookButton();
        loadGoogleButton();
        loadLoginButton();
        loadSingUpButton();
    }

    private void loadLoginButton(){
        loginButton.setOnClickListener(v -> {
            // Do something
        });
    }

    private void loadSingUpButton(){
        singUpButton.setOnClickListener(v -> SetFragmentSingUp.set());
    }

    private void loadGoogleButton(){
        googleButton.setOnClickListener(v -> {
            // Do something
        });
    }

    private void loadFacebookButton(){
        facebookButton.setOnClickListener(v -> {
            // Do something
        });
    }
}