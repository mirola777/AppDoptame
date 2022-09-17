package com.appdoptame.appdoptame.view.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appdoptame.appdoptame.R;
import com.appdoptame.appdoptame.util.PetTypeConstants;
import com.appdoptame.appdoptame.util.Selectable;
import com.appdoptame.appdoptame.view.adapter.SelectableGridAdapter;
import com.appdoptame.appdoptame.view.dialog.DialogPostPet;
import com.appdoptame.appdoptame.view.fragmentcontroller.FragmentController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class FragmentPostPet extends Fragment {

    private RecyclerView          chooseTypeList;
    private SelectableGridAdapter chooseTypeAdapter;

    @SuppressLint("InflateParams")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        postponeEnterTransition(1, TimeUnit.MILLISECONDS);
        return inflater.inflate(R.layout.fragment_post_pet, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadComponents();
    }

    private void loadComponents(){
        chooseTypeList  = requireView().findViewById(R.id.create_pet_choose_type_list);

        loadChooseType();
    }

    private void loadChooseType(){
        List<Selectable> selectables = new ArrayList<>();

        selectables.add(new Selectable(R.drawable.image_dog_3) {
            @Override
            public void onClick() {
                DialogPostPet dialog = new DialogPostPet(PetTypeConstants.DOG);
                FragmentController.showDialog(dialog);
            }
        });

        selectables.add(new Selectable(R.drawable.image_cat) {
            @Override
            public void onClick() {
                DialogPostPet dialog = new DialogPostPet(PetTypeConstants.CAT);
                FragmentController.showDialog(dialog);
            }
        });

        selectables.add(new Selectable(R.drawable.image_hamster_2) {
            @Override
            public void onClick() {
                DialogPostPet dialog = new DialogPostPet(PetTypeConstants.HAMSTER);
                FragmentController.showDialog(dialog);
            }
        });

        selectables.add(new Selectable(R.drawable.image_snake) {
            @Override
            public void onClick() {
                DialogPostPet dialog = new DialogPostPet(PetTypeConstants.SNAKE);
                FragmentController.showDialog(dialog);
            }
        });

        selectables.add(new Selectable(R.drawable.image_fish) {
            @Override
            public void onClick() {
                DialogPostPet dialog = new DialogPostPet(PetTypeConstants.FISH);
                FragmentController.showDialog(dialog);
            }
        });


        selectables.add(new Selectable(R.drawable.image_bird_2) {
            @Override
            public void onClick() {
                DialogPostPet dialog = new DialogPostPet(PetTypeConstants.BIRD);
                FragmentController.showDialog(dialog);
            }
        });

        selectables.add(new Selectable(R.drawable.image_bunny) {
            @Override
            public void onClick() {
                DialogPostPet dialog = new DialogPostPet(PetTypeConstants.BUNNY);
                FragmentController.showDialog(dialog);
            }
        });

        selectables.add(new Selectable(R.drawable.image_turtle) {
            @Override
            public void onClick() {
                DialogPostPet dialog = new DialogPostPet(PetTypeConstants.TURTLE);
                FragmentController.showDialog(dialog);
            }
        });

        chooseTypeAdapter = new SelectableGridAdapter(requireContext(), selectables);
        chooseTypeList.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        chooseTypeList.setAdapter(chooseTypeAdapter);
    }
}