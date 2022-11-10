package com.appdoptame.appdoptame.view.alert;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.appdoptame.appdoptame.AppDoptameApp;
import com.appdoptame.appdoptame.R;
import com.appdoptame.appdoptame.data.firestore.ChatRepositoryFS;
import com.appdoptame.appdoptame.data.firestore.PetRepositoryFS;
import com.appdoptame.appdoptame.data.firestore.UserRepositoryFS;
import com.appdoptame.appdoptame.data.listener.CompleteListener;
import com.appdoptame.appdoptame.model.Chat;
import com.appdoptame.appdoptame.model.Message;
import com.appdoptame.appdoptame.model.User;
import com.appdoptame.appdoptame.model.message.MessageAdopt;
import com.appdoptame.appdoptame.util.MessageConstants;
import com.bumptech.glide.Glide;

import java.util.UUID;

public class AlertAdopt {
    private AlertDialog     dialog;
    private final TextView  adoptNo;
    private final TextView  adoptYes;
    private final TextView  adoptText;
    private final ImageView adoptImage;

    public AlertAdopt(@NonNull Activity activity, Chat chat) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        final View layout = activity.getLayoutInflater().inflate(R.layout.alert_adopt, null);

        adoptNo    = layout.findViewById(R.id.adopt_no);
        adoptYes   = layout.findViewById(R.id.adopt_yes);
        adoptText  = layout.findViewById(R.id.adopt_text);
        adoptImage = layout.findViewById(R.id.adopt_image);

        Glide.with(AppDoptameApp.getContext())
                .load(chat.getPet().getImages().get(0))
                .placeholder(R.drawable.user_icon_orange)
                .error(R.drawable.user_icon_ligthblue)
                .into(adoptImage);

        adoptNo.setOnClickListener(v -> {
            User userSession  = UserRepositoryFS.getInstance().getUserSession();
            MessageAdopt noMessage = new MessageAdopt(
                    chat.getID(),
                    userSession.getID(),
                    MessageConstants.ADOPT_NO);

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

        adoptYes.setOnClickListener(v -> {
            User userSession  = UserRepositoryFS.getInstance().getUserSession();
            MessageAdopt yesMessage = new MessageAdopt(
                    chat.getID(),
                    userSession.getID(),
                    MessageConstants.ADOPT_YES);

            ChatRepositoryFS.getInstance().sendMessage(yesMessage, new CompleteListener() {
                @Override
                public void onSuccess() {
                    PetRepositoryFS.getInstance().changeState(chat.getPet(), new CompleteListener() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onFailure() {

                        }
                    });
                }

                @Override
                public void onFailure() {

                }
            });

            dialog.dismiss();
        });

        builder.setCancelable(false);
        builder.setView(layout);

        dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    public void dismiss(){
        dialog.dismiss();
    }

    public void show(){
        dialog.show();
    }
}
