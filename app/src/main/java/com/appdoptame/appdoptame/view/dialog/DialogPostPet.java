package com.appdoptame.appdoptame.view.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appdoptame.appdoptame.R;
import com.appdoptame.appdoptame.data.firestore.PetRepositoryFS;
import com.appdoptame.appdoptame.data.listener.CompleteListener;
import com.appdoptame.appdoptame.data.listener.PetPredictorListener;
import com.appdoptame.appdoptame.data.listener.PickImageAdapterListener;
import com.appdoptame.appdoptame.model.Pet;
import com.appdoptame.appdoptame.util.EditTextExtractor;
import com.appdoptame.appdoptame.util.PetSexConstants;
import com.appdoptame.appdoptame.util.PetTypeConstants;
import com.appdoptame.appdoptame.util.UriToByteArray;
import com.appdoptame.appdoptame.view.adapter.PickImageAdapter;
import com.appdoptame.appdoptame.view.alert.AlertLoading;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DialogPostPet extends BottomSheetDialogFragment implements PickImageAdapterListener {
    private EditText          nameField;
    private RadioButton       sexMaleField;
    private RadioButton       sexFemaleField;
    private RadioButton       typeSterilizedField;
    private RadioButton       typeNotSterilizedField;
    private boolean           sterilizedField;
    private String            sexField;
    private TextInputEditText breedField;
    private TextInputLayout   breedLayout;
    private TextInputEditText breedDogField;
    private TextInputEditText ageField;
    private TextInputEditText sizeField;
    private TextInputEditText weightField;
    private TextInputEditText descriptionField;
    private TextView          registerButton;
    private RecyclerView      pickImagesList;
    private TextView          imagesCountText;
    private TextView          petTypeText;
    private ImageView         petTypeImage;
    private PickImageAdapter  pickImagesAdapter;
    private ConstraintLayout  predictDogLayout;
    private LinearLayout      predictDogButton;

    private static final int PICK_CODE       = 1;

    // DATA
    private final String petType;

    public DialogPostPet(String petType){
        this.petType = petType;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        setStyle(DialogFragment.STYLE_NORMAL, R.style.dialogStyle);
        return super.onCreateDialog(savedInstanceState);
    }

    @SuppressLint("InflateParams") @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        postponeEnterTransition(1, TimeUnit.MILLISECONDS);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.dialogStyle);
        return inflater.inflate(R.layout.dialog_post_pet, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadComponents();
    }

    private void loadComponents(){
        nameField               = requireView().findViewById(R.id.create_pet_name_field);
        breedField              = requireView().findViewById(R.id.create_pet_breed_field);
        breedLayout             = requireView().findViewById(R.id.create_pet_breed_layout);
        breedDogField           = requireView().findViewById(R.id.create_pet_breed_dog_field);
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
        petTypeText             = requireView().findViewById(R.id.create_pet_type);
        petTypeImage            = requireView().findViewById(R.id.create_pet_type_image);
        predictDogLayout        = requireView().findViewById(R.id.create_pet_predict_dog_layout);
        predictDogButton        = requireView().findViewById(R.id.create_pet_predict_dog_button);
        pickImagesAdapter       = new PickImageAdapter(requireContext(), this, true);

        loadHeader();
        loadBreed();
        loadPickImages();
        loadRegisterButton();
    }

    private void loadBreed(){
        predictDogButton.setOnClickListener(v -> {
            if(pickImagesAdapter.getImages().size() > 0){
                byte[] image = pickImagesAdapter.getImages().get(0);

                PetRepositoryFS.getInstance().predictRace(image, new PetPredictorListener() {
                    @Override
                    public void onSuccess(String prediction) {
                        breedDogField.setText(prediction);
                    }

                    @Override
                    public void onFailure() {
                        Toast.makeText(requireContext(), "Hubo un error cargando la imagen", Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                Toast.makeText(requireContext(), "Carga al menos una imagen", Toast.LENGTH_LONG).show();
            }
        });

        if(petType.equals(PetTypeConstants.DOG)){
            breedLayout.setVisibility(View.GONE);
        } else {
            predictDogLayout.setVisibility(View.GONE);
        }
    }

    private void loadHeader(){
        switch (petType){
            case  PetTypeConstants.TURTLE:
                petTypeText.setText(R.string.post_turtle);
                petTypeImage.setImageResource(R.drawable.image_turtle);
                break;

            case  PetTypeConstants.BIRD:
                petTypeText.setText(R.string.post_bird);
                petTypeImage.setImageResource(R.drawable.image_bird);
                break;

            case  PetTypeConstants.BUNNY:
                petTypeText.setText(R.string.post_bunny);
                petTypeImage.setImageResource(R.drawable.image_bunny);
                break;

            case  PetTypeConstants.CAT:
                petTypeText.setText(R.string.post_cat);
                petTypeImage.setImageResource(R.drawable.image_cat_2);
                break;

            case  PetTypeConstants.DOG:
                petTypeText.setText(R.string.post_dog);
                petTypeImage.setImageResource(R.drawable.image_dog_4);
                break;

            case  PetTypeConstants.FISH:
                petTypeText.setText(R.string.post_fish);
                petTypeImage.setImageResource(R.drawable.image_fish);
                break;

            case  PetTypeConstants.HAMSTER:
                petTypeText.setText(R.string.post_hamster);
                petTypeImage.setImageResource(R.drawable.image_hamster);
                break;

            case  PetTypeConstants.SNAKE:
                petTypeText.setText(R.string.post_snake);
                petTypeImage.setImageResource(R.drawable.image_snake);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_CODE && resultCode== Activity.RESULT_OK){
            assert data != null;
            if (data.getClipData() != null){
                // varias imagenes seleccionadas
                List<byte[]> images = new ArrayList<>();
                int imagesCount = data.getClipData().getItemCount();
                for(int i = 0; i < imagesCount; i++){
                    byte[] imageBytes = UriToByteArray.convert(data.getClipData().getItemAt(i).getUri());
                    images.add(imageBytes);
                }
                pickImagesAdapter.addImages(images);

            } else if(data.getData() !=null){
                // una sola imagen seleccionada
                byte[] imageBytes = UriToByteArray.convert(data.getData());
                pickImagesAdapter.addImage(imageBytes);
            }
        }
    }

    private void loadPickImages(){
        updateImagesCount(0);
        pickImagesList.setLayoutManager(new GridLayoutManager(requireContext(), 3));
        pickImagesList.setAdapter(pickImagesAdapter);
    }

    private void loadRegisterButton(){
        registerButton.setOnClickListener(v -> {
            try {

                if(sexMaleField.isChecked()){
                    sexField = PetSexConstants.MALE;
                }

                if(sexFemaleField.isChecked()){
                    sexField = PetSexConstants.FEMALE;
                }

                if(typeSterilizedField.isChecked()){
                    sterilizedField = true;
                }

                else if(typeNotSterilizedField.isChecked()){
                    sterilizedField = false;
                }

                //String ID;
                String name             = EditTextExtractor.get(nameField);
                String type             = petType;
                String sex              = sexField;
                String description      = EditTextExtractor.get(descriptionField);
                String city             = "Medellin";  // Temporalmente mientras añadimos mas ciudades
                String department       = "Antioquia"; // Temporalmente mientras añadimos mas departamentos
                String breed            = EditTextExtractor.get(breedField);
                if(petType.equals(PetTypeConstants.DOG)) breed = EditTextExtractor.get(breedDogField);
                boolean stray           = false;
                boolean sterilized      = sterilizedField;
                boolean adopted         = false;
                long age                = Long.parseLong(EditTextExtractor.get(ageField));
                long size               = Long.parseLong(EditTextExtractor.get(sizeField));
                long weight             = Long.parseLong(EditTextExtractor.get(weightField));
                List<String> images = new ArrayList<>();

                Pet newPet = new Pet(
                        name,  type,       sex,     description, city, department, breed,
                        stray, sterilized, adopted, age,         size, weight,     images);

                AlertLoading alert = new AlertLoading(requireActivity());
                alert.show();

                // Se crea la mascota y se envía a la base de datos
                PetRepositoryFS.getInstance().createPet(newPet, pickImagesAdapter.getImages(),  new CompleteListener() {
                    @Override
                    public void onSuccess() {
                        alert.setSuccessfully();
                        DialogPostPet.this.dismiss();
                    }

                    @Override
                    public void onFailure() {
                        alert.setFailure();
                    }
                });
            } catch (Exception e){
                System.out.println(e.toString());
            }
        });
    }

    @SuppressLint("SetTextI18n")
    void updateImagesCount(int count){
        imagesCountText.setText("Photos (" + count + ")");
    }

    @Override
    public void onImageDeleted(int imagesCount) {
        updateImagesCount(imagesCount);
    }

    @Override
    public void onImagesDeleted() {
        updateImagesCount(0);
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
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_CODE);
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
    }
}