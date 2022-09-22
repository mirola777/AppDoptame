package com.appdoptame.appdoptame.view.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.appdoptame.appdoptame.AppDoptameApp;
import com.appdoptame.appdoptame.R;
import com.appdoptame.appdoptame.data.firestore.ChatRepositoryFS;
import com.appdoptame.appdoptame.data.firestore.UserRepositoryFS;
import com.appdoptame.appdoptame.data.listener.ChatCreatorListener;
import com.appdoptame.appdoptame.model.Chat;
import com.appdoptame.appdoptame.model.Message;
import com.appdoptame.appdoptame.model.Pet;
import com.appdoptame.appdoptame.model.Post;
import com.appdoptame.appdoptame.model.User;
import com.appdoptame.appdoptame.util.EditTextExtractor;
import com.appdoptame.appdoptame.view.fragmentcontroller.SetFragmentChat;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;

import java.util.concurrent.TimeUnit;

public class DialogCreateChat extends BottomSheetDialogFragment {

    private TextInputEditText messageField;
    private TextView          headerText;
    private TextView          createChatButton;
    private ImageView         user1image;
    private ImageView         user2image;

    private Post post;

    public DialogCreateChat(Post post){
        this.post = post;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        setStyle(DialogFragment.STYLE_NORMAL, R.style.dialogStyle);
        return super.onCreateDialog(savedInstanceState);
    }

    @SuppressLint("InflateParams") @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        postponeEnterTransition(1, TimeUnit.MILLISECONDS);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.dialogStyle);
        return inflater.inflate(R.layout.dialog_create_chat, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadComponents();
    }

    private void loadComponents(){
        messageField     = requireView().findViewById(R.id.create_chat_message_field);
        headerText       = requireView().findViewById(R.id.create_chat_text);
        createChatButton = requireView().findViewById(R.id.create_chat_button);
        user1image       = requireView().findViewById(R.id.create_chat_image_1);
        user2image       = requireView().findViewById(R.id.create_chat_image_2);

        loadCreateChatButton();
        loadData();
    }

    @SuppressLint("SetTextI18n")
    private void loadData(){
        User owner       = post.getUser();
        User userSession = UserRepositoryFS.getInstance().getUserSession();

        Glide.with(AppDoptameApp.getContext())
                .load(userSession.getImage())
                .placeholder(R.drawable.user_icon_orange)
                .error(R.drawable.user_icon_orange)
                .into(user1image);

        Glide.with(AppDoptameApp.getContext())
                .load(owner.getImage())
                .placeholder(R.drawable.user_icon_orange)
                .error(R.drawable.user_icon_orange)
                .into(user2image);

        headerText.setText(
                AppDoptameApp.getStringById(R.string.create_chat_text_1) +
                        " " +
                        owner.getName() +
                        " " +
                        AppDoptameApp.getStringById(R.string.create_chat_text_2) +
                        " " +
                        post.getPet().getName() +
                        "?"
        );
    }

    private void loadCreateChatButton(){
        createChatButton.setOnClickListener(v -> {
           String message = EditTextExtractor.get(messageField);
           if(message.length() > 0){
               Pet  pet     = post.getPet();
               User owner   = post.getUser();
               User adopter = UserRepositoryFS.getInstance().getUserSession();

               Chat    newChat    = new Chat(owner, adopter, pet);
               Message newMessage = new Message(adopter.getID(), message);

               ChatRepositoryFS.getInstance().createChat(newChat, newMessage, new ChatCreatorListener() {
                   @Override
                   public void onChatCreated(Chat chat) {
                       DialogCreateChat.this.dismiss();
                       SetFragmentChat.set(chat);
                   }

                   @Override
                   public void onFailure() {

                   }
               });
           }
        });
    }
}
