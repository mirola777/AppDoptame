package com.appdoptame.appdoptame.view.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.appdoptame.appdoptame.R;
import com.appdoptame.appdoptame.data.firestore.UserRepositoryFS;
import com.appdoptame.appdoptame.data.listener.CompleteListener;
import com.appdoptame.appdoptame.view.fragmentcontroller.FragmentController;
import com.appdoptame.appdoptame.view.fragmentcontroller.SetFragmentLogin;

import java.util.concurrent.TimeUnit;

public class FragmentSettings extends Fragment {
    private TextView signOutButton;

    @SuppressLint("InflateParams") @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        postponeEnterTransition(1, TimeUnit.MILLISECONDS);
        return inflater.inflate(R.layout.fragment_settings, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadComponents();
    }

    private void loadComponents(){
        signOutButton = requireView().findViewById(R.id.settings_sign_out);

        loadSignOutButton();
    }

    private void loadSignOutButton(){
        signOutButton.setOnClickListener(v -> {
            UserRepositoryFS.getInstance().singOut(new CompleteListener() {
                @Override
                public void onSuccess() {
                    FragmentController.removeAllFragments();
                    SetFragmentLogin.set();
                }

                @Override
                public void onFailure() {

                }
            });
        });
    }
}
