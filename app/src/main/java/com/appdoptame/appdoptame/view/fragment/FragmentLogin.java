package com.appdoptame.appdoptame.view.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.appdoptame.appdoptame.R;
import com.appdoptame.appdoptame.data.firestore.UserRepositoryFS;
import com.appdoptame.appdoptame.data.listener.CompleteListener;
import com.appdoptame.appdoptame.util.EditTextExtractor;
import com.appdoptame.appdoptame.view.fragmentcontroller.FragmentController;
import com.appdoptame.appdoptame.view.fragmentcontroller.SetFragmentMain;
import com.appdoptame.appdoptame.view.fragmentcontroller.SetFragmentSingUp;
import com.facebook.CallbackManager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

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

        try {

            PackageInfo info
                    = requireActivity().getPackageManager().getPackageInfo(
                    "com.appdoptame.appdoptame",
                    PackageManager.GET_SIGNATURES);

            for (Signature signature : info.signatures) {

                MessageDigest md
                        = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:",
                        Base64.encodeToString(
                                md.digest(),
                                Base64.DEFAULT));
            }
        }

        catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
            System.out.println("aa");
        }
    }

    private void loadLoginButton(){
        loginButton.setOnClickListener(v -> {
            String email    = EditTextExtractor.get(emailField);
            String password = EditTextExtractor.get(passwordField);

            UserRepositoryFS.getInstance().login(email, password, new CompleteListener() {
                @Override
                public void onSuccess() {
                    //FragmentController.onBackPressed();
                    SetFragmentMain.set();
                }

                @Override
                public void onFailure() {

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
            UserRepositoryFS.getInstance().loginFacebook(this, callbackManager, new CompleteListener() {
                @Override
                public void onSuccess() {
                    System.out.println("FACEBOOK");
                }

                @Override
                public void onFailure() {
                    System.out.println("FFFFFFFF");
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
                UserRepositoryFS.getInstance().loginGoogleResult(data, new CompleteListener() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onFailure() {

                    }
                });

                break;
        }
    }
}