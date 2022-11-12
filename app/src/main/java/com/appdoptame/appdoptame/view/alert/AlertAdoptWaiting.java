package com.appdoptame.appdoptame.view.alert;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;

import com.airbnb.lottie.LottieAnimationView;
import com.appdoptame.appdoptame.R;
import com.appdoptame.appdoptame.data.firestore.ChatRepositoryFS;
import com.appdoptame.appdoptame.data.firestore.UserRepositoryFS;
import com.appdoptame.appdoptame.data.listener.CompleteListener;
import com.appdoptame.appdoptame.model.Chat;
import com.appdoptame.appdoptame.model.User;
import com.appdoptame.appdoptame.model.message.MessageAdopt;
import com.appdoptame.appdoptame.util.MessageConstants;

public class AlertAdoptWaiting {
    private final AlertDialog         dialog;
    private final LottieAnimationView animationView;
    private final TextView            waitingText;
    private final TextView            waitingCancel;
    private final CardView            waitingCancelCard;
    private       boolean             isStarted;

    public AlertAdoptWaiting(@NonNull Activity activity, Chat chat) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        final View layout = activity.getLayoutInflater().inflate(R.layout.alert_adopt_waiting, null);


        animationView     = layout.findViewById(R.id.adopt_waiting_animation);
        waitingText       = layout.findViewById(R.id.adopt_waiting_text);
        waitingCancel     = layout.findViewById(R.id.adopt_waiting_cancel);
        waitingCancelCard = layout.findViewById(R.id.adopt_waiting_cancel_card);
        isStarted         = true;

        builder.setCancelable(false);
        builder.setView(layout);

        dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        waitingCancel.setOnClickListener(v -> {
            User userSession  = UserRepositoryFS.getInstance().getUserSession();
            MessageAdopt noMessage = new MessageAdopt(
                    chat.getID(),
                    userSession.getID(),
                    MessageConstants.ADOPT_CANCEL);

            ChatRepositoryFS.getInstance().sendMessage(noMessage, new CompleteListener() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onFailure() {

                }
            });

            dialog.dismiss();
        });
    }

    public boolean isShowing(){
        return dialog.isShowing();
    }

    public void show(){
        dialog.show();
    }

    public boolean isStarted() {
        return isStarted;
    }

    public void setFailure(){
        if(dialog.isShowing()){
            isStarted = false;
            animationView.setAnimation(R.raw.loading_failure);
            animationView.loop(false);
            animationView.playAnimation();
            dialog.setCancelable(true);
            waitingCancel.setClickable(false);
            waitingCancelCard.setVisibility(View.INVISIBLE);
            waitingText.setText(R.string.waiting_adopt_failure);

        }
    }

    public void setTimeout(){
        if(dialog.isShowing()){
            isStarted = false;
            animationView.setAnimation(R.raw.timeout);
            animationView.loop(false);
            animationView.playAnimation();
            dialog.setCancelable(true);
            waitingCancel.setClickable(false);
            waitingCancelCard.setVisibility(View.INVISIBLE);
            waitingText.setText(R.string.waiting_adopt_timeout);
        }
    }

    public void setSuccess(){
        if(dialog.isShowing()){
            isStarted = false;
            animationView.setAnimation(R.raw.loading_succesfully);
            animationView.loop(false);
            animationView.playAnimation();
            dialog.setCancelable(true);
            waitingCancel.setClickable(false);
            waitingCancelCard.setVisibility(View.INVISIBLE);
            waitingText.setText(R.string.waiting_adopt_success);
        }
    }
}
