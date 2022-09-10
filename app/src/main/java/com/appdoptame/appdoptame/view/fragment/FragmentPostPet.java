package com.appdoptame.appdoptame.view.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appdoptame.appdoptame.R;
import com.appdoptame.appdoptame.data.firestore.PetRepositoryFS;
import com.appdoptame.appdoptame.data.listener.CompleteListener;
import com.appdoptame.appdoptame.data.listener.PickImageAdapterListener;
import com.appdoptame.appdoptame.model.Pet;
import com.appdoptame.appdoptame.util.EditTextExtractor;
import com.appdoptame.appdoptame.util.StatusBarHeightGetter;
import com.appdoptame.appdoptame.view.adapter.PickImageAdapter;
import com.appdoptame.appdoptame.view.fragmentcontroller.FragmentController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.facebook.FacebookSdk.getApplicationContext;

public class FragmentPostPet extends Fragment implements PickImageAdapterListener {

    private ConstraintLayout statusBar;
    private ImageButton      backButton;
    private EditText         nameField;
    private RadioButton      sexMaleField;
    private RadioButton      sexFemaleField;
    private RadioButton      typeCatField;
    private RadioButton      typeDogField;
    private RadioButton      typeSterilizedField;
    private RadioButton      typeNotSterilizedField;
    private String           typeField;
    private boolean          sterilizedField;
    private EditText         breedField;
    private EditText         ageField;
    private String           sexField;
    private EditText         sizeField;
    private EditText         weightField;
    private EditText         descriptionField;
    private TextView         registerButton;
    private RecyclerView     pickImagesList;
    private TextView         imagesCountText;
    private PickImageAdapter pickImagesAdapter;

    private static final int PICK_CODE       = 1;

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
        statusBar               = requireView().findViewById(R.id.create_pet_status_bar);
        backButton              = requireView().findViewById(R.id.create_pet_back_button);
        nameField               = requireView().findViewById(R.id.create_pet_name_field);
        typeCatField            = requireView().findViewById(R.id.select_pet_cat_field);
        typeDogField            = requireView().findViewById(R.id.select_pet_dog_field);
        breedField              = requireView().findViewById(R.id.create_pet_breed_field);
        ageField                = requireView().findViewById(R.id.create_pet_age_field);
        sexMaleField            = requireView().findViewById(R.id.select_pet_male_field);
        sexFemaleField          = requireView().findViewById(R.id.select_pet_female_field);
        sizeField               = requireView().findViewById(R.id.create_pet_size_field);
        typeSterilizedField     = requireView().findViewById(R.id.select_pet_sterilized_yes_field);
        typeNotSterilizedField  = requireView().findViewById(R.id.select_pet_sterilized_no_field);
        weightField             = requireView().findViewById(R.id.create_pet_weight_field);
        descriptionField        = requireView().findViewById(R.id.create_pet_description_field);
        registerButton          = requireView().findViewById(R.id.create_pet_create_pet_button);
        imagesCountText         = requireView().findViewById(R.id.create_pet_images_count);
        pickImagesList          = requireView().findViewById(R.id.create_pet_pick_images_list);
        pickImagesAdapter       = new PickImageAdapter(requireContext(), this);

        loadPickImages();
        loadStatusBar();
        loadBackButton();
        loadRegisterButton();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_CODE && resultCode== Activity.RESULT_OK){
            if (data.getClipData() != null){
                // varias imagenes seleccionadas
                List<Uri> images = new ArrayList<>();
                int imagesCount = data.getClipData().getItemCount();
                for(int i = 0; i < imagesCount; i++){
                    images.add(data.getClipData().getItemAt(i).getUri());
                }
                pickImagesAdapter.addImages(images);

            } else if(data.getData() !=null){
                // una sola imagen seleccionada
                String imageURL = data.getData().getPath();
                pickImagesAdapter.addImage(Uri.parse(imageURL));
            }
        }
    }

    private void loadPickImages(){
        pickImagesList.setLayoutManager(new GridLayoutManager(requireContext(), 3));
        pickImagesList.setAdapter(pickImagesAdapter);
    }

    private void loadRegisterButton(){
        registerButton.setOnClickListener(v -> {
            try {

                if(typeCatField.isSelected()){
                    typeField = "Gato";
                }
                if(typeDogField.isSelected()){
                    typeField = "Perro";
                }
                if(sexMaleField.isSelected()){
                    sexField = "Macho";
                }
                if(sexFemaleField.isSelected()){
                    sexField = "Hembra";
                }

                typeField = "Gato";
                sexField = "Macho";

                if(typeSterilizedField.isSelected()){
                    sterilizedField = true;
                }
                else if(typeNotSterilizedField.isSelected()){
                    sterilizedField = false;
                }

                //String ID;
                String name             = EditTextExtractor.get(nameField);
                String type             = typeField;
                String sex              = sexField;
                String description      = EditTextExtractor.get(descriptionField);
                String city             = "Medellin";  // Temporalmente mientras añadimos mas ciudades
                String department       = "Antioquia"; // Temporalmente mientras añadimos mas departamentos
                String breed            = EditTextExtractor.get(breedField);
                boolean stray           =false;
                boolean sterilized      = sterilizedField;
                boolean adopted         = false;
                long age                = Long.parseLong(EditTextExtractor.get(ageField));
                long size               = Long.parseLong(EditTextExtractor.get(sizeField));
                long weight             = Long.parseLong(EditTextExtractor.get(weightField));
                List<String> images = new ArrayList<>();

                Pet newPet = new Pet(
                        name,  type,       sex,     description, city, department, breed,
                        stray, sterilized, adopted, age,         size, weight,     images);

                // Se crea la mascota y se envía a la base de datos

                PetRepositoryFS.getInstance().createPet(newPet, pickImagesAdapter.getImages(),  new CompleteListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(getApplicationContext(), "Mascota publicada", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure() {
                        Toast.makeText(getApplicationContext(), "Algo salio mal, intentalo  nuevo", Toast.LENGTH_LONG).show();
                    }
                });
            } catch (Exception e){
                System.out.println(e.toString());
            }
        });
    }

    private void loadBackButton(){
        backButton.setOnClickListener(v -> FragmentController.onBackPressed());
    }

    private void loadStatusBar(){
        // Añadiendo padding superior en base a la altura de la barra de notificaciones
        ConstraintLayout.LayoutParams params =
                (ConstraintLayout.LayoutParams) statusBar.getLayoutParams();
        params.topMargin = StatusBarHeightGetter.get();
        statusBar.setLayoutParams(params);
    }

    void updateImagesCount(int count){
        imagesCountText.setText("Photos (" + count + ")");
    }

    @Override
    public void onImageDeleted(int imagesCount) {
        updateImagesCount(imagesCount);
    }

    @Override
    public void onImageAdded(int imagesCount) {
        updateImagesCount(imagesCount);
    }

    @Override
    public void onImagesAdded(int imagesCount) {
        updateImagesCount(imagesCount);
    }

    @Override
    public void onAdd() {
        Intent intent = new Intent();
        intent.setType("image/");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_CODE);
    }
}