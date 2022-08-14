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
import com.appdoptame.appdoptame.data.firestore.PostRepositoryFS;
import com.appdoptame.appdoptame.data.listener.PostListLoaderListener;
import com.appdoptame.appdoptame.model.Post;
import com.appdoptame.appdoptame.view.adapter.PostAdapter;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class FragmentMain extends Fragment {
    private PostAdapter   feedAdapter;
    private RecyclerView  feedRecyclerView;

    public FragmentMain(){

    }

    @SuppressLint("InflateParams") @Nullable @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        postponeEnterTransition(1, TimeUnit.MILLISECONDS);
        return inflater.inflate(R.layout.fragment_feed, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadComponents();
    }

    private void loadComponents(){
        feedRecyclerView = requireView().findViewById(R.id.feed_list);

        setListFunction();

        PostRepositoryFS.getInstance().getFeedPosts(new PostListLoaderListener() {
            @Override
            public void onSuccess(List<Post> posts) {
                feedAdapter.setPosts(posts);
            }

            @Override
            public void onFailure() {

            }
        });
    }

    private void setListFunction(){
        feedAdapter = new PostAdapter(requireContext());
        feedRecyclerView.setLayoutManager(new LinearLayoutManager(
                getContext(), LinearLayoutManager.VERTICAL, false)
        );
        feedRecyclerView.setAdapter(feedAdapter);
    }

}