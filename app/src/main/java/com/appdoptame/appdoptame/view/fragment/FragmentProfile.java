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
import com.appdoptame.appdoptame.data.firestore.PetRepositoryFS;
import com.appdoptame.appdoptame.data.firestore.UserRepositoryFS;
import com.appdoptame.appdoptame.data.listener.PetListLoaderListener;
import com.appdoptame.appdoptame.model.Pet;
import com.appdoptame.appdoptame.model.User;
import com.appdoptame.appdoptame.util.UserNameGetter;
import com.appdoptame.appdoptame.view.adapter.PetsListAdapter;
import com.bumptech.glide.Glide;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class FragmentProfile extends Fragment {
    private ImageView       image;
    private ImageView       coverImage;
    private TextView        nameText;
    private PetsListAdapter petsAdapter;
    private RecyclerView    petsList;

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
        image      = requireView().findViewById(R.id.profile_image);
        coverImage = requireView().findViewById(R.id.profile_cover_image);
        nameText   = requireView().findViewById(R.id.profile_name);
        petsList   = requireView().findViewById(R.id.profile_pets_list);

        loadPetsList();
        loadUserData();
    }

    private void loadUserData(){
        User user = UserRepositoryFS.getInstance().getUserSession();

        Glide.with(AppDoptameApp.getContext())
                .load(user.getImage())
                .placeholder(R.drawable.user_icon_orange)
                .error(R.drawable.user_icon_orange)
                .into(image);

        nameText.setText(UserNameGetter.get(user));

        PetRepositoryFS.getInstance().getUserPets(user.getID(), new PetListLoaderListener() {
            @Override
            public void onSuccess(List<Pet> pets) {
                petsAdapter.setData(pets);
            }

            @Override
            public void onFailure() {
                System.out.println("ERROR");
            }
        });
    }

    private void loadPetsList(){
        petsAdapter = new PetsListAdapter(requireContext());
        petsList.setLayoutManager(new GridLayoutManager(requireContext(), 3));
        petsList.setAdapter(petsAdapter);
    }
}