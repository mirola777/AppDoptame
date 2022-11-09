package com.appdoptame.appdoptame.view.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appdoptame.appdoptame.R;
import com.appdoptame.appdoptame.data.firestore.CommentRepositoryFS;
import com.appdoptame.appdoptame.data.firestore.UserRepositoryFS;
import com.appdoptame.appdoptame.data.listener.CommentInserterListener;
import com.appdoptame.appdoptame.data.listener.CommentListLoaderListener;
import com.appdoptame.appdoptame.data.listener.CompleteListener;
import com.appdoptame.appdoptame.model.Comment;
import com.appdoptame.appdoptame.model.Post;
import com.appdoptame.appdoptame.model.User;
import com.appdoptame.appdoptame.util.DisplayManager;
import com.appdoptame.appdoptame.util.EditTextExtractor;
import com.appdoptame.appdoptame.view.adapter.CommentListAdapter;
import com.appdoptame.appdoptame.view.fragmentcontroller.FragmentController;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class FragmentComment extends Fragment implements CommentInserterListener {

    // Elements
    private ConstraintLayout   toolbar;
    private RecyclerView       commentsList;
    private CommentListAdapter commentsAdapter;
    private ImageButton        backButton;
    private ImageButton        sendButton;
    private TextInputEditText  input;

    private final Post post;

    public FragmentComment(Post post){
        this.post = post;
    }

    @SuppressLint("InflateParams") @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        postponeEnterTransition(1, TimeUnit.MILLISECONDS);
        return inflater.inflate(R.layout.fragment_comment, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadComponents();
    }

    private void loadComponents(){
        toolbar       = requireView().findViewById(R.id.comment_toolbar);
        commentsList  = requireView().findViewById(R.id.comment_list);
        backButton    = requireView().findViewById(R.id.comment_back);
        input         = requireView().findViewById(R.id.comment_input);
        sendButton    = requireView().findViewById(R.id.comment_send);

        addToolbarFunction();
        addBackFunction();
        addCommentsListFunction();
        addSendFunction();
        loadComments();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        commentsAdapter.onDetach();
    }

    private void addBackFunction(){
        backButton.setOnClickListener(v -> FragmentController.onBackPressed());
    }

    private void addCommentsListFunction(){
        commentsAdapter = new CommentListAdapter(requireContext(), post, commentsList);
        commentsList.setLayoutManager(new LinearLayoutManager(
                getContext(), LinearLayoutManager.VERTICAL, false)
        );
        commentsList.setAdapter(commentsAdapter);
    }

    private void loadComments(){
        CommentRepositoryFS.getInstance().getPostComments(post.getID(), new CommentListLoaderListener() {
            @Override
            public void onSuccess(List<Comment> comments) {
                commentsAdapter.setData(comments);
                commentsList.scrollToPosition(comments.size() - 1);
            }

            @Override
            public void onFailure() {

            }
        });
    }

    private void addSendFunction(){
        sendButton.setOnClickListener(v -> {
            String commentText = EditTextExtractor.get(input);
            if(commentText.length() > 0){
                User userSession   = UserRepositoryFS.getInstance().getUserSession();
                Comment comment    = new Comment(post.getID(), userSession, commentText);
                input.setText("");

                CommentRepositoryFS.getInstance().createComment(comment, new CompleteListener() {
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

    private void addToolbarFunction(){
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) toolbar.getLayoutParams();
        params.topMargin = DisplayManager.getStatusBarHeight();
        toolbar.setLayoutParams(params);
    }

    @Override
    public void onNewComment(Comment comment) {

    }
}