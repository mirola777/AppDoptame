package com.appdoptame.appdoptame.view.alert;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;

import com.airbnb.lottie.LottieAnimationView;
import com.appdoptame.appdoptame.AppDoptameApp;
import com.appdoptame.appdoptame.R;

public class AlertLoading {
    private AlertDialog         dialog;
    private final LottieAnimationView animationView;
    private final TextView            okButton;
    private final CardView            cardView;

    public AlertLoading(@NonNull Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        final View layout = activity.getLayoutInflater().inflate(R.layout.alert_loading, null);

        okButton      = layout.findViewById(R.id.loading_button);
        animationView = layout.findViewById(R.id.loading_animation);
        cardView      = layout.findViewById(R.id.loading_card);

        okButton.setOnClickListener(v -> dialog.dismiss());
        okButton.setClickable(false);

        builder.setCancelable(false);
        builder.setView(layout);

        dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    public void show(){
        dialog.show();
    }

    public void setFailure(){
        okButton.setClickable(true);
        cardView.setCardBackgroundColor(AppDoptameApp.getColorById(R.color.red));
        animationView.setAnimation(R.raw.loading_failure);
        animationView.loop(false);
        animationView.playAnimation();
    }

    public void setSuccessfully(){
        okButton.setClickable(true);
        cardView.setCardBackgroundColor(AppDoptameApp.getColorById(R.color.green));
        animationView.setAnimation(R.raw.loading_succesfully);
        animationView.loop(false);
        animationView.playAnimation();
    }
}
