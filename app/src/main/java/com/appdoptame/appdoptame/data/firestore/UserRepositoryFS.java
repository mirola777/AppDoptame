package com.appdoptame.appdoptame.data.firestore;

import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.appdoptame.appdoptame.data.firestore.services.LoginFS;
import com.appdoptame.appdoptame.data.firestore.services.LoginFacebookFS;
import com.appdoptame.appdoptame.data.firestore.services.LoginGoogleFS;
import com.appdoptame.appdoptame.data.firestore.services.SingUpFS;
import com.appdoptame.appdoptame.data.listener.CompleteListener;
import com.appdoptame.appdoptame.data.repository.UserRepository;
import com.appdoptame.appdoptame.data.service.ILogin;
import com.appdoptame.appdoptame.data.service.ILoginFacebook;
import com.appdoptame.appdoptame.data.service.ILoginGoogle;
import com.appdoptame.appdoptame.data.service.ISingUp;
import com.appdoptame.appdoptame.model.User;
import com.facebook.CallbackManager;

public class UserRepositoryFS implements UserRepository {
    private static UserRepositoryFS instance;
    private final ILogin iLogin;
    private final ILoginGoogle iLoginGoogle;
    private final ILoginFacebook iLoginFacebook;
    private final ISingUp iSingUp;

    private UserRepositoryFS(){
        this.iLogin         = new LoginFS();
        this.iLoginGoogle   = new LoginGoogleFS();
        this.iLoginFacebook = new LoginFacebookFS();
        this.iSingUp        = new SingUpFS();
    }

    public static UserRepositoryFS getInstance(){
        if(instance == null){
            instance = new UserRepositoryFS();
        }

        return instance;
    }

    @Override
    public void login(String email, String password, CompleteListener listener) {
        iLogin.login(email, password, listener);
    }

    @Override
    public void loginGoogle(Fragment fragment) {
        iLoginGoogle.loginGoogle(fragment);
    }

    @Override
    public void loginGoogleResult(Intent data, CompleteListener listener) {
        iLoginGoogle.loginGoogleResult(data, listener);
    }

    @Override
    public void loginFacebook(Fragment fragment, CallbackManager callbackManager, CompleteListener listener) {
        iLoginFacebook.loginFacebook(fragment, callbackManager, listener);
    }

    @Override
    public void singOut(CompleteListener listener) {
        iLogin.singOut(listener);
    }

    @Override
    public void singUp(String email, String password, String confirmPassword, User user, CompleteListener listener) {
        iSingUp.singUp(email, password, confirmPassword, user, listener);
    }
}
