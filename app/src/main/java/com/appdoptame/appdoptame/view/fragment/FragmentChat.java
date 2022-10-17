package com.appdoptame.appdoptame.view.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appdoptame.appdoptame.AppDoptameApp;
import com.appdoptame.appdoptame.R;
import com.appdoptame.appdoptame.data.firestore.ChatRepositoryFS;
import com.appdoptame.appdoptame.data.firestore.UserRepositoryFS;
import com.appdoptame.appdoptame.data.listener.ChatEditorListener;
import com.appdoptame.appdoptame.data.listener.CompleteListener;
import com.appdoptame.appdoptame.data.listener.MessageInserterListener;
import com.appdoptame.appdoptame.data.listener.MessageListLoaderListener;
import com.appdoptame.appdoptame.data.observer.ChatObserver;
import com.appdoptame.appdoptame.data.observer.MessageObserver;
import com.appdoptame.appdoptame.model.Chat;
import com.appdoptame.appdoptame.model.Message;
import com.appdoptame.appdoptame.model.User;
import com.appdoptame.appdoptame.util.DisplayManager;
import com.appdoptame.appdoptame.util.EditTextExtractor;
import com.appdoptame.appdoptame.util.MessageConstants;
import com.appdoptame.appdoptame.util.UserNameGetter;
import com.appdoptame.appdoptame.view.adapter.MessageListAdapter;
import com.appdoptame.appdoptame.view.alert.AlertAdopt;
import com.appdoptame.appdoptame.view.alert.AlertAdoptWaiting;
import com.appdoptame.appdoptame.view.fragmentcontroller.FragmentController;
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class FragmentChat extends Fragment implements MessageInserterListener, ChatEditorListener {

    // Elements
    private ConstraintLayout   toolbar;
    private RecyclerView       messagesList;
    private MessageListAdapter messagesAdapter;
    private ImageView          image;
    private TextView           name;
    private ImageButton        backButton;
    private ImageButton        sendButton;
    private LinearLayout       adoptButton;
    private TextInputEditText  input;
    private AlertAdoptWaiting  dialogWaiting;

    private Chat chat;

    public FragmentChat(Chat chat){
        this.chat = chat;
    }

    @SuppressLint("InflateParams") @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        postponeEnterTransition(1, TimeUnit.MILLISECONDS);
        return inflater.inflate(R.layout.fragment_chat, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadComponents();
    }

    private void loadComponents(){
        toolbar       = requireView().findViewById(R.id.chat_toolbar);
        messagesList  = requireView().findViewById(R.id.chat_list);
        image         = requireView().findViewById(R.id.chat_user_image);
        name          = requireView().findViewById(R.id.chat_user_name);
        backButton    = requireView().findViewById(R.id.chat_back);
        adoptButton   = requireView().findViewById(R.id.chat_adopt_button);
        input         = requireView().findViewById(R.id.chat_input);
        sendButton    = requireView().findViewById(R.id.chat_send);
        dialogWaiting = new AlertAdoptWaiting(requireActivity());

        addToolbarFunction();
        addBackFunction();
        addMessagesListFunction();
        addData();
        addSendFunction();
        addAdoptFunction();
        loadMessages();

        MessageObserver.attachMessageInserterListener(this);
        ChatObserver.attachChatEditorListener(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        MessageObserver.detachMessageInserterListener(this);
        ChatObserver.detachChatEditorListener(this);
        messagesAdapter.onDetach();
    }

    private void addBackFunction(){
        backButton.setOnClickListener(v -> FragmentController.onBackPressed());
    }

    private void addMessagesListFunction(){
        messagesAdapter = new MessageListAdapter(requireContext(), chat, messagesList);
        messagesList.setPadding(0, DisplayManager.getStatusBarHeight() + getResources().getDimensionPixelSize(R.dimen.actionBarSize), 0, 0);
        messagesList.setLayoutManager(new LinearLayoutManager(
                getContext(), LinearLayoutManager.VERTICAL, false)
        );
        messagesList.setAdapter(messagesAdapter);
    }

    @SuppressLint("SetTextI18n")
    private void addData(){
        User user = UserRepositoryFS.getInstance().getUserSession();

        Glide.with(AppDoptameApp.getContext())
                .load(chat.getPet().getImages().get(0))
                .placeholder(R.drawable.user_icon_orange)
                .error(R.drawable.user_icon_ligthblue)
                .into(image);

        if(user.getID().equals(chat.getOwner().getID())){
            name.setText("[" + chat.getPet().getName() + "] " + UserNameGetter.get(chat.getAdopter()));
        } else if(user.getID().equals(chat.getAdopter().getID())){
            name.setText("[" + chat.getPet().getName() + "] " + UserNameGetter.get(chat.getOwner()));
        }
    }

    private void loadMessages(){
        ChatRepositoryFS.getInstance().getChatMessages(chat.getID(), new MessageListLoaderListener() {
            @Override
            public void onSuccess(List<Message> messages) {
                messagesAdapter.setData(messages);
                messagesList.scrollToPosition(messages.size() - 1);
            }

            @Override
            public void onFailure() {

            }
        });
    }

    private void addSendFunction(){
        sendButton.setOnClickListener(v -> {
            String messageText = EditTextExtractor.get(input);
            if(messageText.length() > 0){
                User userSession   = UserRepositoryFS.getInstance().getUserSession();
                Message message    = new Message(chat.getID(), userSession.getID(), messageText);
                input.setText("");

                ChatRepositoryFS.getInstance().sendMessage(message, new CompleteListener() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onFailure() {

                    }
                });
            }
        });
    }

    private void addAdoptFunction(){
        adoptButton.setOnClickListener(v -> {
            User userSession   = UserRepositoryFS.getInstance().getUserSession();
            Message message    = new Message(chat.getID(), userSession.getID(), "");

            ChatRepositoryFS.getInstance().sendAdoptMessage(message, new CompleteListener() {
                @Override
                public void onSuccess() {
                    dialogWaiting.show();
                }

                @Override
                public void onFailure() {

                }
            });
        });
    }

    private void addToolbarFunction(){
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) toolbar.getLayoutParams();
        params.topMargin = DisplayManager.getStatusBarHeight();
        toolbar.setLayoutParams(params);
    }

    @Override
    public void onNewMessage(Message message) {

    }

    @Override
    public void onNewAdoptMessage(Message message) {
        if(chat.getID().equals(message.getChatID())) {

            User userSession = UserRepositoryFS.getInstance().getUserSession();
            if(!userSession.getID().equals(message.getWriterID())){
                if(dialogWaiting.isShowing()){
                    if(message.getMessage().equals(MessageConstants.ADOPT_YES)){
                        dialogWaiting.setSuccess();

                    } else if(message.getMessage().equals(MessageConstants.ADOPT_NO)) {
                        dialogWaiting.setFailure();

                    } else {
                        dialogWaiting.setFailure();
                    }

                } else {
                    AlertAdopt alertAdopt = new AlertAdopt(requireActivity(), chat);
                    alertAdopt.show();
                }
            }
        }
    }

    @Override
    public void onEdited(Chat chat) {
        if(this.chat.getID().equals(chat.getID())) this.chat = chat;
    }
}