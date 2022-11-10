package com.appdoptame.appdoptame.view.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import androidx.recyclerview.widget.GridLayoutManager;
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
import com.appdoptame.appdoptame.data.listener.PickImageAdapterListener;
import com.appdoptame.appdoptame.data.observer.ChatObserver;
import com.appdoptame.appdoptame.data.observer.MessageObserver;
import com.appdoptame.appdoptame.model.Chat;
import com.appdoptame.appdoptame.model.Message;
import com.appdoptame.appdoptame.model.User;
import com.appdoptame.appdoptame.model.message.MessageAdopt;
import com.appdoptame.appdoptame.util.DisplayManager;
import com.appdoptame.appdoptame.util.EditTextExtractor;
import com.appdoptame.appdoptame.util.MessageConstants;
import com.appdoptame.appdoptame.util.UriToByteArray;
import com.appdoptame.appdoptame.util.UserNameGetter;
import com.appdoptame.appdoptame.view.adapter.MessageListAdapter;
import com.appdoptame.appdoptame.view.adapter.PickImageAdapter;
import com.appdoptame.appdoptame.view.alert.AlertAdopt;
import com.appdoptame.appdoptame.view.alert.AlertAdoptWaiting;
import com.appdoptame.appdoptame.view.fragmentcontroller.FragmentController;
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class FragmentChat extends Fragment implements MessageInserterListener, ChatEditorListener, PickImageAdapterListener {

    // Elements
    private ConstraintLayout   toolbar;
    private RecyclerView       messagesList;
    private MessageListAdapter messagesAdapter;
    private RecyclerView       imagesList;
    private PickImageAdapter   imagesAdapter;
    private TextView           imagesText;
    private ImageView          image;
    private TextView           name;
    private ImageButton        backButton;
    private ImageButton        sendButton;
    private ImageButton        fileButton;
    private ImageButton        imageButton;
    private LinearLayout       adoptButton;
    private TextInputEditText  input;
    private AlertAdoptWaiting  dialogWaiting;
    private AlertAdopt         alertAdopt;

    private static final int PICK_IMAGE = 2;
    private static final int PICK_FILE  = 3;

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
        imageButton   = requireView().findViewById(R.id.chat_send_image);
        fileButton    = requireView().findViewById(R.id.chat_send_file);
        imagesList    = requireView().findViewById(R.id.chat_image_list);
        imagesText    = requireView().findViewById(R.id.chat_image_text);
        imagesAdapter = new PickImageAdapter(requireContext(), this, false);
        dialogWaiting = new AlertAdoptWaiting(requireActivity());
        alertAdopt    = new AlertAdopt(requireActivity(), chat);

        addToolbarFunction();
        addBackFunction();
        addMessagesListFunction();
        addImagesListFunction();
        addData();
        addSendFunction();
        addSendFileFunction();
        addSendImageFunction();
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
        messagesList.setPadding(0, DisplayManager.getStatusBarHeight() + getResources().getDimensionPixelSize(R.dimen.dp5), 0, 0);
        messagesList.setLayoutManager(new LinearLayoutManager(
                getContext(), LinearLayoutManager.VERTICAL, false)
        );
        messagesList.setAdapter(messagesAdapter);
    }

    private void addImagesListFunction(){
        imagesList.setLayoutManager(new GridLayoutManager(requireContext(), 5));
        imagesList.setAdapter(imagesAdapter);
        imagesText.setVisibility(View.GONE);
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
                checkLastMessage();
            }

            @Override
            public void onFailure() {

            }
        });
    }

    private void checkLastMessage(){
        Message message = messagesAdapter.getLastMessage();
        if (message != null){
            if(chat.getID().equals(message.getChatID()) && message instanceof MessageAdopt) {

                User userSession = UserRepositoryFS.getInstance().getUserSession();
                if(!userSession.getID().equals(message.getWriterID()) && message.getMessage().equals(MessageConstants.ADOPT_START)){
                    alertAdopt.show();
                }
            }
        }


    }

    private void addSendFunction(){
        sendButton.setOnClickListener(v -> {
            String messageText = EditTextExtractor.get(input);
            if(messageText.length() > 0){
                User userSession   = UserRepositoryFS.getInstance().getUserSession();
                Message message    = new Message(chat.getID(), userSession.getID(), messageText);
                input.setText("");

                if(imagesAdapter.getImages().size() > 0){
                    List<byte[]> images = new ArrayList<>(imagesAdapter.getImages());
                    ChatRepositoryFS.getInstance().sendMessage(message, images, new CompleteListener() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onFailure() {

                        }
                    });

                    imagesAdapter.deleteImages();

                } else {
                    ChatRepositoryFS.getInstance().sendMessage(message, new CompleteListener() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onFailure() {

                        }
                    });
                }
            }
        });
    }

    private void addSendFileFunction(){
        fileButton.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("application/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,false);
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent,"Select a File"), PICK_FILE);
        });
    }

    private void addSendImageFunction(){
        imageButton.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_IMAGE);
        });
    }
    private final Handler  timerHandler  = new Handler(Looper.getMainLooper());
    private final Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            Message lastMessage = messagesAdapter.getLastMessage();
            if(lastMessage instanceof MessageAdopt && lastMessage.getMessage().equals(MessageConstants.ADOPT_START)){
                dialogWaiting.setTimeout();
                User userSession     = UserRepositoryFS.getInstance().getUserSession();
                MessageAdopt messageTimeout = new MessageAdopt(chat.getID(), userSession.getID(), MessageConstants.ADOPT_TIMEOUT);
                ChatRepositoryFS.getInstance().sendMessage(messageTimeout, new CompleteListener() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onFailure() {

                    }
                });
            }
        }
    };

    private void addAdoptFunction(){
        adoptButton.setOnClickListener(v -> {
            User userSession     = UserRepositoryFS.getInstance().getUserSession();
            MessageAdopt message = new MessageAdopt(chat.getID(), userSession.getID(), MessageConstants.ADOPT_START);

            ChatRepositoryFS.getInstance().sendMessage(message, new CompleteListener() {
                @Override
                public void onSuccess() {
                    dialogWaiting = new AlertAdoptWaiting(requireActivity());
                    dialogWaiting.show();
                    timerHandler.removeCallbacks(timerRunnable);
                    timerHandler.postDelayed(timerRunnable, 10000);
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
        if(chat.getID().equals(message.getChatID()) && message instanceof MessageAdopt) {

            User userSession = UserRepositoryFS.getInstance().getUserSession();
            if(!userSession.getID().equals(message.getWriterID())){
                timerHandler.removeCallbacks(timerRunnable);
                if(dialogWaiting.isStarted()){

                    switch (message.getMessage()) {
                        case MessageConstants.ADOPT_YES:
                            dialogWaiting.setSuccess();
                            break;

                        case MessageConstants.ADOPT_TIMEOUT:
                            dialogWaiting.setTimeout();
                            break;
                        case MessageConstants.ADOPT_NO:
                        default:
                            dialogWaiting.setFailure();
                            break;
                    }

                } else {
                    if(!message.getMessage().equals(MessageConstants.ADOPT_START)){
                        alertAdopt.dismiss();
                    } else {
                        alertAdopt.show();
                    }
                }
            }
        }
    }

    @Override
    public void onEdited(Chat chat) {
        if(this.chat.getID().equals(chat.getID())) this.chat = chat;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){

            if(requestCode == PICK_IMAGE){
                assert data != null;
                if (data.getClipData() != null){
                    // varias imagenes seleccionadas
                    List<byte[]> images = new ArrayList<>();
                    int imagesCount = data.getClipData().getItemCount();
                    for(int i = 0; i < imagesCount; i++){
                        byte[] imageBytes = UriToByteArray.convert(data.getClipData().getItemAt(i).getUri());
                        images.add(imageBytes);
                    }
                    imagesAdapter.addImages(images);

                } else if(data.getData() !=null){
                    // una sola imagen seleccionada
                    byte[] imageBytes = UriToByteArray.convert(data.getData());
                    imagesAdapter.addImage(imageBytes);
                }
            } else if(requestCode == PICK_FILE) {
                assert data != null;
                if(data.getData() !=null){

                    Uri file = data.getData();
                    User userSession   = UserRepositoryFS.getInstance().getUserSession();
                    Message message    = new Message(chat.getID(), userSession.getID(), "");

                    ChatRepositoryFS.getInstance().sendMessage(message, file, new CompleteListener() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onFailure() {

                        }
                    });

                }
            }
        }
    }

    private void changeImageLayout(){
        if(imagesAdapter.getImages().size() > 0) {
            imagesText.setVisibility(View.VISIBLE);
        } else {
            imagesText.setVisibility(View.GONE);
        }
    }

    @Override
    public void onImageDeleted(int imagesCount) {
        changeImageLayout();
    }

    @Override
    public void onImagesDeleted() {
        changeImageLayout();
    }

    @Override
    public void onImageAdded(int imagesCount) {
        changeImageLayout();
    }

    @Override
    public void onImagesAdded(int imagesCount) {
        changeImageLayout();
    }

    @Override
    public void onAdd() {

    }
}
