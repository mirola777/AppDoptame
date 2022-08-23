package com.appdoptame.appdoptame.view.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.appdoptame.appdoptame.R;
import com.appdoptame.appdoptame.data.firestore.UserRepositoryFS;
import com.appdoptame.appdoptame.data.listener.LoginListener;
import com.appdoptame.appdoptame.util.EditTextExtractor;
import com.appdoptame.appdoptame.view.fragmentcontroller.FragmentController;
import com.appdoptame.appdoptame.view.fragmentcontroller.SetFragmentCreateUser;
import com.appdoptame.appdoptame.view.fragmentcontroller.SetFragmentMain;
import com.appdoptame.appdoptame.view.fragmentcontroller.SetFragmentSingUp;
import com.facebook.CallbackManager;

import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.facebook.FacebookSdk.getApplicationContext;

public class FragmentLogin extends Fragment {
    // CODES FOR SIGN IN
    public static final int GOOGLE   = 0;

    // Elements
    private EditText        emailField;
    private EditText        passwordField;
    private TextView        loginButton;
    private TextView        singUpButton;
    private CheckBox        rememberCheckBox;
    private CircleImageView facebookButton;
    private CircleImageView googleButton;

    // CALLBACK FOR FACEBOOK LOGIN
    private CallbackManager callbackManager;

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
        callbackManager  = CallbackManager.Factory.create();

        loadFacebookButton();
        loadGoogleButton();
        loadLoginButton();
        loadSingUpButton();
    }

    private void loadLoginButton(){
        loginButton.setOnClickListener(v -> {
            String email    = EditTextExtractor.get(emailField);
            String password = EditTextExtractor.get(passwordField);

            UserRepositoryFS.getInstance().login(email, password, new LoginListener() {
                @Override
                public void onSuccess() {
                    FragmentController.removeFragment(FragmentLogin.this);
                    SetFragmentMain.set();
                }

                @Override
                public void onNewAccount() {
                    SetFragmentCreateUser.set();
                }

                @Override
                public void onFailure() {
                    Toast.makeText(getApplicationContext(), "Algo salio mal, intentalo  nuevo", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void loadSingUpButton(){
        singUpButton.setOnClickListener(v -> SetFragmentSingUp.set());
    }

    private void loadGoogleButton(){
        googleButton.setOnClickListener(v -> {
            UserRepositoryFS.getInstance().loginGoogle(this);
        });
    }

    private void loadFacebookButton(){
        facebookButton.setOnClickListener(v -> {
            UserRepositoryFS.getInstance().loginFacebook(this, callbackManager, new LoginListener() {
                @Override
                public void onSuccess() {
                    FragmentController.removeFragment(FragmentLogin.this);
                    SetFragmentMain.set();
                }

                @Override
                public void onNewAccount() {
                    SetFragmentCreateUser.set();
                }

                @Override
                public void onFailure() {
                    Toast.makeText(getApplicationContext(), "Algo salio mal, intentalo  nuevo", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case GOOGLE:
                UserRepositoryFS.getInstance().loginGoogleResult(data, new LoginListener() {
                    @Override
                    public void onSuccess() {
                        FragmentController.removeFragment(FragmentLogin.this);
                        SetFragmentMain.set();
                    }

                    @Override
                    public void onNewAccount() {
                        SetFragmentCreateUser.set();
                    }

                    @Override
                    public void onFailure() {
                        Toast.makeText(getApplicationContext(), "Algo salio mal, intentalo  nuevo", Toast.LENGTH_SHORT).show();

                    }
                });
                break;
        }
    }
}