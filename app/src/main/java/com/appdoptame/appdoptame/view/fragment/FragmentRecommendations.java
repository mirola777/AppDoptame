package com.appdoptame.appdoptame.view.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.appdoptame.appdoptame.AppDoptameApp;
import com.appdoptame.appdoptame.R;
import com.appdoptame.appdoptame.data.firestore.PetRepositoryFS;
import com.appdoptame.appdoptame.data.listener.PostLoaderListener;
import com.appdoptame.appdoptame.model.Post;
import com.appdoptame.appdoptame.view.dialog.DialogCreateChat;
import com.appdoptame.appdoptame.view.fragmentcontroller.FragmentController;
import com.bumptech.glide.Glide;

import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

public class FragmentRecommendations extends Fragment {
    private CircleImageView     petImage;
    private TextView            petName;
    private TextView            recommendButton;
    private TextView            adoptButton;
    private CardView            adoptLayout;
    private CardView            recommendLayout;
    private LottieAnimationView animation;



    @SuppressLint("InflateParams")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        postponeEnterTransition(1, TimeUnit.MILLISECONDS);
        return inflater.inflate(R.layout.fragment_recommendations, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadComponents();
    }

    private void loadComponents(){
        petImage        = requireView().findViewById(R.id.recommendations_pet_image);
        petName         = requireView().findViewById(R.id.recommendations_pet_name);
        recommendButton = requireView().findViewById(R.id.recommendations_button);
        animation       = requireView().findViewById(R.id.recommendations_animation);
        adoptButton     = requireView().findViewById(R.id.recommendations_adopt_button);
        adoptLayout     = requireView().findViewById(R.id.recommendations_adopt_layout);
        recommendLayout = requireView().findViewById(R.id.recommendations_button_layout);

        petImage.setVisibility(View.INVISIBLE);
        petName.setVisibility(View.INVISIBLE);
        animation.setVisibility(View.INVISIBLE);
        adoptLayout.setVisibility(View.GONE);

        recommendButton.setOnClickListener(v -> {
            animation.cancelAnimation();
            animation.removeAllAnimatorListeners();
            animation.loop(true);
            animation.setAnimation(R.raw.loading_cat);
            animation.playAnimation();
            recommendLayout.setVisibility(View.GONE);
            animation.setVisibility(View.VISIBLE);

            PetRepositoryFS.getInstance().recommend(new PostLoaderListener() {
                @Override
                public void onSuccess(Post post) {
                    Glide.with(AppDoptameApp.getContext())
                            .load(post.getPet().getImages().get(0))
                            .placeholder(R.drawable.user_icon_orange)
                            .error(R.drawable.user_icon_orange)
                            .into(petImage);

                    petName.setText(post.getPet().getName());
                    petImage.setVisibility(View.VISIBLE);
                    petName.setVisibility(View.VISIBLE);
                    adoptButton.setOnClickListener(v -> {
                        DialogCreateChat dialog = new DialogCreateChat(post);
                        FragmentController.showDialog(dialog);
                    });
                    adoptLayout.setVisibility(View.VISIBLE);
                }

                @Override
                public void onFailure() {
                    animation.pauseAnimation();
                    animation.setAnimation(R.raw.loading_failure);
                    animation.loop(false);
                    animation.addAnimatorListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator anim) {
                            petImage.setVisibility(View.INVISIBLE);
                            petName.setVisibility(View.INVISIBLE);
                            animation.setVisibility(View.INVISIBLE);
                            recommendLayout.setVisibility(View.VISIBLE);
                            adoptLayout.setVisibility(View.GONE);
                        }
                    });

                    animation.playAnimation();
                }
            });
        });
    }
}