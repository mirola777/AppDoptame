package com.appdoptame.appdoptame.view.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appdoptame.appdoptame.R;
import com.appdoptame.appdoptame.data.firestore.ChatRepositoryFS;
import com.appdoptame.appdoptame.data.firestore.UserRepositoryFS;
import com.appdoptame.appdoptame.data.listener.ChatListLoaderListener;
import com.appdoptame.appdoptame.model.Chat;
import com.appdoptame.appdoptame.model.User;
import com.appdoptame.appdoptame.view.adapter.ChatListAdapter;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class FragmentMessages extends Fragment {
    private RecyclerView    chatsList;
    private ChatListAdapter chatsAdapter;

    @SuppressLint("InflateParams")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        postponeEnterTransition(1, TimeUnit.MILLISECONDS);
        return inflater.inflate(R.layout.fragment_messages, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadComponents();
    }

    private void loadComponents(){
        chatsList = requireView().findViewById(R.id.messages_list);

        addChatsListFunction();
        refreshChats();
    }

    private void addChatsListFunction(){
        chatsAdapter = new ChatListAdapter(requireContext());
        chatsList.setLayoutManager(new LinearLayoutManager(
                getContext(), LinearLayoutManager.VERTICAL, false)
        );
        chatsList.setAdapter(chatsAdapter);
    }

    private void refreshChats(){
        User user = UserRepositoryFS.getInstance().getUserSession();
        ChatRepositoryFS.getInstance().getUserChats(user.getID(), new ChatListLoaderListener() {
            @Override
            public void onSuccess(List<Chat> chats) {
                chatsAdapter.setData(chats);
            }

            @Override
            public void onFailure() {

            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        chatsAdapter.onDetach();
    }
}