package com.appdoptame.appdoptame.view.alert;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.airbnb.lottie.LottieAnimationView;
import com.appdoptame.appdoptame.R;

public class AlertAdoptWaiting {
    private final AlertDialog         dialog;
    private final LottieAnimationView animationView;
    private final TextView            waitingText;

    public AlertAdoptWaiting(@NonNull Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        final View layout = activity.getLayoutInflater().inflate(R.layout.alert_adopt_waiting, null);


        animationView = layout.findViewById(R.id.adopt_waiting_animation);
        waitingText   = layout.findViewById(R.id.adopt_waiting_text);

        builder.setCancelable(false);
        builder.setView(layout);

        dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    public boolean isShowing(){
        return dialog.isShowing();
    }

    public void show(){
        dialog.show();
    }

    public void setFailure(){
        if(dialog.isShowing()){
            animationView.setAnimation(R.raw.loading_failure);
            animationView.loop(false);
            animationView.playAnimation();
            dialog.setCancelable(true);
            waitingText.setText(R.string.waiting_adopt_failure);

        }
    }

    public void setSuccess(){
        if(dialog.isShowing()){
            animationView.setAnimation(R.raw.loading_succesfully);
            animationView.loop(false);
            animationView.playAnimation();
            dialog.setCancelable(true);
            waitingText.setText(R.string.waiting_adopt_success);
        }
    }
}
