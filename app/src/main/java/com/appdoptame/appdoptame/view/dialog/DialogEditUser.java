package com.appdoptame.appdoptame.view.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.appdoptame.appdoptame.AppDoptameApp;
import com.appdoptame.appdoptame.R;
import com.appdoptame.appdoptame.data.firestore.UserRepositoryFS;
import com.appdoptame.appdoptame.data.listener.CompleteListener;
import com.appdoptame.appdoptame.model.User;
import com.appdoptame.appdoptame.util.EditTextExtractor;
import com.appdoptame.appdoptame.util.URLToByteArray;
import com.appdoptame.appdoptame.util.UriToByteArray;
import com.appdoptame.appdoptame.view.alert.AlertLoading;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class DialogEditUser extends BottomSheetDialogFragment {

    private TextInputEditText nameField;
    private TextInputEditText lastNameField;
    private TextInputEditText ageField;
    private TextInputEditText CCField;
    private TextInputEditText phoneField;
    private TextView          editButton;
    private ImageView         image;
    private ImageButton       imageDelete;
    private TextView          imagePick;

    private static final int PICK_CODE = 2;
    private byte[] userImage;

    private User user;

    public DialogEditUser(User user){
        this.user = user;
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
        return inflater.inflate(R.layout.dialog_edit_user, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadComponents();
    }

    private void loadComponents(){
        nameField      = requireView().findViewById(R.id.edit_user_name_field);
        lastNameField  = requireView().findViewById(R.id.edit_user_last_name_field);
        ageField       = requireView().findViewById(R.id.edit_user_age_field);
        phoneField     = requireView().findViewById(R.id.edit_user_phone_field);
        CCField        = requireView().findViewById(R.id.edit_user_identification_field);
        editButton     = requireView().findViewById(R.id.edit_user_edit_user_button);
        image          = requireView().findViewById(R.id.edit_user_image);
        imageDelete    = requireView().findViewById(R.id.edit_user_image_delete);
        imagePick      = requireView().findViewById(R.id.edit_user_image_button);

        loadImagePick();
        loadEditButton();
        loadUserData();
    }

    private void loadUserData(){
        nameField.setText(user.getName());
        lastNameField.setText(user.getLastName());
        ageField.setText(String.valueOf(user.getAge()));
        phoneField.setText(user.getPhone());
        CCField.setText(user.getIdentification());
        Glide.with(AppDoptameApp.getContext())
                .load(user.getImage())
                .placeholder(R.drawable.user_icon_ligthblue)
                .error(R.drawable.user_icon_ligthblue)
                .into(image);

        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> userImage = URLToByteArray.getByteImageFromURL(user.getImage()));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_CODE && resultCode== Activity.RESULT_OK){
            assert data != null;
            userImage = UriToByteArray.convert(data.getData());
            image.setImageURI(data.getData());
        }
    }

    private void loadImagePick(){
        imagePick.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_CODE);
        });

        imageDelete.setOnClickListener(v -> {
            userImage = null;
            image.setImageResource(R.drawable.user_icon_ligthblue);
        });
    }

    private void loadEditButton(){
        editButton.setOnClickListener(v -> {
            try {
                String name            = EditTextExtractor.get(nameField);
                String lastName        = EditTextExtractor.get(lastNameField);
                String phone           = EditTextExtractor.get(phoneField);
                long   age             = Long.parseLong(EditTextExtractor.get(ageField));
                String identification  = EditTextExtractor.get(CCField);
                String city            = "Medellin";  // Temporalmente mientras añadimos mas ciudades
                String department      = "Antioquia"; // Temporalmente mientras añadimos mas departamentos

                user.setName(name);
                user.setLastName(lastName);
                user.setPhone(phone);
                user.setAge(age);
                user.setIdentification(identification);
                user.setCity(city);
                user.setDepartment(department);

                AlertLoading alert = new AlertLoading(requireActivity());
                alert.show();

                // Se crea el usuario y se envía a la base de datos
                UserRepositoryFS.getInstance().editUser(user, userImage, new CompleteListener() {
                    @Override
                    public void onSuccess() {
                        alert.setSuccessfully();
                        DialogEditUser.this.dismiss();
                    }

                    @Override
                    public void onFailure() {
                        alert.setFailure();
                    }
                });
            } catch (Exception e){

            }
        });
    }
}
