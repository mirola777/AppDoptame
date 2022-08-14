package com.appdoptame.appdoptame.data.firestore;

import com.appdoptame.appdoptame.data.firestore.services.LoginFS;
import com.appdoptame.appdoptame.data.firestore.services.LoginGoogleFS;
import com.appdoptame.appdoptame.data.firestore.services.SingUpFS;
import com.appdoptame.appdoptame.data.listener.CompleteListener;
import com.appdoptame.appdoptame.data.repository.UserRepository;
import com.appdoptame.appdoptame.data.service.ILogin;
import com.appdoptame.appdoptame.data.service.ILoginGoogle;
import com.appdoptame.appdoptame.data.service.ISingUp;
import com.appdoptame.appdoptame.model.User;

public class UserRepositoryFS implements UserRepository {
    private static UserRepositoryFS instance;
    private final ILogin iLogin;
    private final ILoginGoogle iLoginGoogle;
    private final ISingUp iSingUp;

    private UserRepositoryFS(){
        this.iLogin       = new LoginFS();
        this.iLoginGoogle = new LoginGoogleFS();
        this.iSingUp      = new SingUpFS();
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
    public void loginGoogle(CompleteListener listener) {
        iLoginGoogle.loginGoogle(listener);
    }

    @Override
    public void singOut(CompleteListener listener) {
        iLogin.singOut(listener);
    }

    @Override
    public void singUp(String email, String password, User user, CompleteListener listener) {
        iSingUp.singUp(email, password, user, listener);
    }
}
