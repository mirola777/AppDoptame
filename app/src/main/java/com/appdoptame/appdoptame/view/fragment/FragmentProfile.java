package com.appdoptame.appdoptame.view.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.appdoptame.appdoptame.model.Post;
import com.appdoptame.appdoptame.model.User;
import com.appdoptame.appdoptame.util.UserNameGetter;
import com.appdoptame.appdoptame.view.adapter.PostGridAdapter;
import com.bumptech.glide.Glide;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class FragmentProfile extends Fragment {
    private ImageView       image;
    private TextView        nameText;
    private TextView        ageText;
    private TextView        idText;
    private TextView        phoneText;
    private TextView        cityText;
    private PostGridAdapter postAdapter;
    private RecyclerView    postList;

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
        image     = requireView().findViewById(R.id.profile_image);
        nameText  = requireView().findViewById(R.id.profile_name);
        ageText   = requireView().findViewById(R.id.profile_age);
        cityText  = requireView().findViewById(R.id.profile_city);
        idText    = requireView().findViewById(R.id.profile_id);
        phoneText = requireView().findViewById(R.id.profile_phone);
        postList  = requireView().findViewById(R.id.profile_post_list);

        loadPetsList();
        loadUserData();
    }

    @SuppressLint("SetTextI18n")
    private void loadUserData(){
        User user = UserRepositoryFS.getInstance().getUserSession();

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
}