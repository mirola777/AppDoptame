package com.appdoptame.appdoptame.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import com.appdoptame.appdoptame.AppDoptameApp;
import com.appdoptame.appdoptame.R;
import com.appdoptame.appdoptame.data.listener.CommentInserterListener;
import com.appdoptame.appdoptame.data.observer.CommentObserver;
import com.appdoptame.appdoptame.model.Comment;
import com.appdoptame.appdoptame.model.Post;
import com.appdoptame.appdoptame.util.DateTextGetter;
import com.appdoptame.appdoptame.util.UserNameGetter;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.ViewHolder> implements CommentInserterListener {

    private List<Comment>        comments;
    private final LayoutInflater inflater;
    private final Post           post;
    private final RecyclerView   list;
    private final Context        context;

    public CommentListAdapter(Context context, Post post, RecyclerView list) {
        this.inflater    = LayoutInflater.from(context);
        this.comments    = new ArrayList<>();
        this.post        = post;
        this.list        = list;
        this.context     = context;

        CommentObserver.attachCommentInserterListener(this);
    }

    public void setData(List<Comment> comments){
        this.comments = comments;
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(this::notifyDataSetChanged);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.adapter_comment_list, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comment comment      = comments.get(position);

        Glide.with(AppDoptameApp.getContext())
                .load(comment.getUser().getImage())
                .placeholder(R.drawable.user_icon_orange)
                .error(R.drawable.user_icon_orange)
                .into(holder.userImage);

        holder.timeText.setText(DateTextGetter.getDateText(comment.getDate()));
        holder.commentText.setText(comment.getComment());
        holder.nameText.setText(UserNameGetter.get(comment.getUser()));
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public void onDetach(){
        CommentObserver.detachCommentInserterListener(this);
    }

    @Override
    public void onNewComment(Comment comment) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            if(post.getID().equals(comment.getPostID())){
                comments.add(comment);

                RecyclerView.SmoothScroller smoothScroller = new LinearSmoothScroller(context) {
                    @Override protected int getVerticalSnapPreference() {
                        return LinearSmoothScroller.SNAP_TO_START;
                    }

                    @Override
                    protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                        return 125f / displayMetrics.densityDpi;
                    }
                };

                smoothScroller.setTargetPosition(comments.size() - 1);
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(() -> {
                    notifyItemInserted(comments.size() - 1);
                    try {
                        list.getLayoutManager().startSmoothScroll(smoothScroller);
                    } catch (Exception ignored){

                    }
                });
            }
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView        userImage;
        TextView         commentText;
        TextView         nameText;
        TextView         timeText;

        ViewHolder(View itemView) {
            super(itemView);
            userImage   = itemView.findViewById(R.id.adapter_comment_list_user_image);
            nameText    = itemView.findViewById(R.id.adapter_comment_list_user_name);
            timeText    = itemView.findViewById(R.id.adapter_comment_list_time);
            commentText = itemView.findViewById(R.id.adapter_comment_list_comment);
        }
    }
}