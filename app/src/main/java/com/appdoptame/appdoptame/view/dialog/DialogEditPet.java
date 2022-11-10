package com.appdoptame.appdoptame.view.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appdoptame.appdoptame.R;
import com.appdoptame.appdoptame.data.firestore.PostRepositoryFS;
import com.appdoptame.appdoptame.data.listener.CompleteListener;
import com.appdoptame.appdoptame.data.listener.PickImageAdapterListener;
import com.appdoptame.appdoptame.data.observer.PostObserver;
import com.appdoptame.appdoptame.model.Pet;
import com.appdoptame.appdoptame.model.Post;
import com.appdoptame.appdoptame.util.EditTextExtractor;
import com.appdoptame.appdoptame.util.PetSexConstants;
import com.appdoptame.appdoptame.util.PetTypeConstants;
import com.appdoptame.appdoptame.util.URLToByteArray;
import com.appdoptame.appdoptame.util.UriToByteArray;
import com.appdoptame.appdoptame.view.adapter.PickImageAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.facebook.FacebookSdk.getApplicationContext;

public class DialogEditPet extends BottomSheetDialogFragment implements PickImageAdapterListener {
    private EditText          nameField;
    private RadioButton       sexMaleField;
    private RadioButton       sexFemaleField;
    private RadioButton       typeSterilizedField;
    private RadioButton       typeNotSterilizedField;
    private boolean           sterilizedField;
    private String            sexField;
    private TextInputEditText breedField;
    private TextInputEditText ageField;
    private TextInputEditText sizeField;
    private TextInputEditText weightField;
    private TextInputEditText descriptionField;
    private TextView          editButton;
    private RecyclerView      pickImagesList;
    private TextView          imagesCountText;
    private TextView          petTypeText;
    private ImageView         petTypeImage;
    private PickImageAdapter  pickImagesAdapter;

    private static final int PICK_CODE       = 1;

    // DATA
    private final Post post;

    public DialogEditPet(Post post){
        this.post = post;
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
        return inflater.inflate(R.layout.dialog_edit_pet, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadComponents();
    }

    private void loadComponents(){
        nameField               = requireView().findViewById(R.id.edit_pet_name_field);
        breedField              = requireView().findViewById(R.id.edit_pet_breed_field);
        ageField                = requireView().findViewById(R.id.edit_pet_age_field);
        sexMaleField            = requireView().findViewById(R.id.edit_pet_male_field);
        sexFemaleField          = requireView().findViewById(R.id.edit_pet_female_field);
        sizeField               = requireView().findViewById(R.id.edit_pet_size_field);
        typeSterilizedField     = requireView().findViewById(R.id.edit_pet_sterilized_yes_field);
        typeNotSterilizedField  = requireView().findViewById(R.id.edit_pet_sterilized_no_field);
        weightField             = requireView().findViewById(R.id.edit_pet_weight_field);
        descriptionField        = requireView().findViewById(R.id.edit_pet_description_field);
        editButton              = requireView().findViewById(R.id.edit_pet_edit_pet_button);
        imagesCountText         = requireView().findViewById(R.id.edit_pet_images_count);
        pickImagesList          = requireView().findViewById(R.id.edit_pet_pick_images_list);
        petTypeText             = requireView().findViewById(R.id.edit_pet_type);
        petTypeImage            = requireView().findViewById(R.id.edit_pet_type_image);

        loadPickImages();
        loadHeader();
        loadEditButton();
        loadPetData();
    }

    private void loadPetData(){
        Pet pet = post.getPet();

        nameField.setText(pet.getName());
        ageField.setText(String.valueOf(pet.getAge()));
        breedField.setText(pet.getBreed());
        sizeField.setText(String.valueOf(pet.getSize()));
        weightField.setText(String.valueOf(pet.getWeight()));
        descriptionField.setText(pet.getDescription());
        typeNotSterilizedField.setChecked(!pet.isSterilized());
        typeSterilizedField.setChecked(pet.isSterilized());

        if(PetSexConstants.FEMALE.equals(pet.getSex())){
            sexFemaleField.setChecked(true);
            sexMaleField.setChecked(false);
        } else if(PetSexConstants.MALE.equals(pet.getSex())){
            sexFemaleField.setChecked(false);
            sexMaleField.setChecked(true);
        }
    }

    private void loadHeader(){
        switch (post.getPet().getType()){
            case  PetTypeConstants.TURTLE:
                petTypeImage.setImageResource(R.drawable.image_turtle);
                break;

            case  PetTypeConstants.BIRD:
                petTypeImage.setImageResource(R.drawable.image_bird);
                break;

            case  PetTypeConstants.BUNNY:
                petTypeImage.setImageResource(R.drawable.image_bunny);
                break;

            case  PetTypeConstants.CAT:
                petTypeImage.setImageResource(R.drawable.image_cat_2);
                break;

            case  PetTypeConstants.DOG:
                petTypeImage.setImageResource(R.drawable.image_dog_4);
                break;

            case  PetTypeConstants.FISH:
                petTypeImage.setImageResource(R.drawable.image_fish);
                break;

            case  PetTypeConstants.HAMSTER:
                petTypeImage.setImageResource(R.drawable.image_hamster);
                break;

            case  PetTypeConstants.SNAKE:
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

        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            List<byte[]> petImages = new ArrayList<>();
            for(String url: post.getPet().getImages()){
                petImages.add(URLToByteArray.getByteImageFromURL(url));
            }

            pickImagesAdapter = new PickImageAdapter(requireContext(), this, petImages, true);

            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(() -> {
                updateImagesCount(0);
                pickImagesList.setLayoutManager(new GridLayoutManager(requireContext(), 3));
                pickImagesList.setAdapter(pickImagesAdapter);
            });

        });
    }

    private void loadEditButton(){
        editButton.setOnClickListener(v -> {
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
                String type             = post.getPet().getType();
                String sex              = sexField;
                String description      = EditTextExtractor.get(descriptionField);
                String city             = "Medellin";  // Temporalmente mientras añadimos mas ciudades
                String department       = "Antioquia"; // Temporalmente mientras añadimos mas departamentos
                String breed            = EditTextExtractor.get(breedField);
                boolean stray           = false;
                boolean sterilized      = sterilizedField;
                boolean adopted         = post.getPet().isAdopted();
                long age                = Long.parseLong(EditTextExtractor.get(ageField));
                long size               = Long.parseLong(EditTextExtractor.get(sizeField));
                long weight             = Long.parseLong(EditTextExtractor.get(weightField));

                // Se actualiza el post
                post.getPet().setName(name);
                post.getPet().setType(type);
                post.getPet().setSex(sex);
                post.getPet().setDescription(description);
                post.getPet().setCity(city);
                post.getPet().setDepartment(department);
                post.getPet().setBreed(breed);
                post.getPet().setStray(stray);
                post.getPet().setAdopted(adopted);
                post.getPet().setSterilized(sterilized);
                post.getPet().setAge(age);
                post.getPet().setSize(size);
                post.getPet().setWeight(weight);

                PostRepositoryFS.getInstance().updatePost(post, pickImagesAdapter.getImages(), new CompleteListener() {
                    @Override
                    public void onSuccess() {
                        PostObserver.notifyPostEdited(post);
                        Toast.makeText(getApplicationContext(), "Mascota editada", Toast.LENGTH_LONG).show();
                        DialogEditPet.this.dismiss();
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