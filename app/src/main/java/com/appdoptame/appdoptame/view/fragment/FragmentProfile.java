package com.appdoptame.appdoptame.view.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appdoptame.appdoptame.AppDoptameApp;
import com.appdoptame.appdoptame.R;
import com.appdoptame.appdoptame.data.firestore.PostRepositoryFS;
import com.appdoptame.appdoptame.data.firestore.UserRepositoryFS;
import com.appdoptame.appdoptame.data.listener.PostListLoaderListener;
import com.appdoptame.appdoptame.data.listener.UserEditorListener;
import com.appdoptame.appdoptame.data.observer.UserObserver;
import com.appdoptame.appdoptame.model.Post;
import com.appdoptame.appdoptame.model.User;
import com.appdoptame.appdoptame.util.UserNameGetter;
import com.appdoptame.appdoptame.view.adapter.PostGridAdapter;
import com.appdoptame.appdoptame.view.dialog.DialogEditUser;
import com.appdoptame.appdoptame.view.fragmentcontroller.FragmentController;
import com.bumptech.glide.Glide;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class FragmentProfile extends Fragment implements UserEditorListener {
    private ImageView       image;
    private ImageButton     editOptions;
    private TextView        nameText;
    private TextView        ageText;
    private TextView        idText;
    private TextView        phoneText;
    private TextView        cityText;
    private PostGridAdapter postAdapter;
    private RecyclerView    postList;

    private User user;

    public FragmentProfile(User user){
        this.user = user;
    }

    @SuppressLint("InflateParams")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        postponeEnterTransition(1, TimeUnit.MILLISECONDS);
        return inflater.inflate(R.layout.fragment_profile, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadComponents();
    }

    private void loadComponents(){
        image       = requireView().findViewById(R.id.profile_image);
        nameText    = requireView().findViewById(R.id.profile_name);
        ageText     = requireView().findViewById(R.id.profile_age);
        cityText    = requireView().findViewById(R.id.profile_city);
        idText      = requireView().findViewById(R.id.profile_id);
        phoneText   = requireView().findViewById(R.id.profile_phone);
        postList    = requireView().findViewById(R.id.profile_post_list);
        editOptions = requireView().findViewById(R.id.profile_edit_button);

        loadEditButton();
        loadPetsList();
        loadUserData();

        UserObserver.attachUserEditorListener(this);
    }

    private void loadEditButton(){
        User userSession = UserRepositoryFS.getInstance().getUserSession();
        if(user.getID().equals(userSession.getID())){
            editOptions.setVisibility(View.VISIBLE);
            editOptions.setOnClickListener(v -> {
                DialogEditUser dialog = new DialogEditUser(user);
                FragmentController.showDialog(dialog);
            });
        } else {
            editOptions.setVisibility(View.GONE);
        }
    }

    @SuppressLint("SetTextI18n")
    private void loadUserData(){
        Glide.with(AppDoptameApp.getContext())
                .load(user.getImage())
                .placeholder(R.drawable.user_icon_orange)
                .error(R.drawable.user_icon_orange)
                .into(image);

        nameText.setText(UserNameGetter.get(user));
        phoneText.setText(user.getPhone());
        idText.setText(user.getIdentification());
        ageText.setText(String.valueOf(user.getAge()));
        cityText.setText(user.getCity() + ", " + user.getDepartment());

        PostRepositoryFS.getInstance().getUserPosts(user.getID(), new PostListLoaderListener() {
            @Override
            public void onSuccess(List<Post> posts) {
                postAdapter.setPosts(posts);
            }

            @Override
            public void onFailure() {
                System.out.println("ERROR");
            }
        });
    }

    private void loadPetsList(){
        postAdapter = new PostGridAdapter(requireContext());
        postList.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        postList.setAdapter(postAdapter);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        postAdapter.onDetach();
        UserObserver.detachUserEditorListener(this);
    }

    @Override
    public void onUserEdited(User user) {
        if(this.user.getID().equals(user.getID())){
            this.user = user;
            loadUserData();
        }
    }
}