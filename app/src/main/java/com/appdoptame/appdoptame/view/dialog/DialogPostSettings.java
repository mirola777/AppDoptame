package com.appdoptame.appdoptame.view.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appdoptame.appdoptame.R;
import com.appdoptame.appdoptame.data.firestore.PostRepositoryFS;
import com.appdoptame.appdoptame.data.firestore.UserRepositoryFS;
import com.appdoptame.appdoptame.data.listener.CompleteListener;
import com.appdoptame.appdoptame.data.observer.PostObserver;
import com.appdoptame.appdoptame.model.Post;
import com.appdoptame.appdoptame.model.User;
import com.appdoptame.appdoptame.util.Selectable;
import com.appdoptame.appdoptame.view.adapter.SelectableAdapter;
import com.appdoptame.appdoptame.view.fragmentcontroller.FragmentController;
import com.appdoptame.appdoptame.view.fragmentcontroller.SetFragmentComment;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DialogPostSettings extends BottomSheetDialogFragment  {

    private RecyclerView          selectablesList;
    private SelectableAdapter     selectablesAdapter;

    // DATA
    private final Post post;

    public DialogPostSettings(Post post){
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
        return inflater.inflate(R.layout.dialog_post_settings, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadComponents();
    }

    private void loadComponents(){
        selectablesList = requireView().findViewById(R.id.dialog_post_settings_list);

        setSelectablesListFunction();
    }

    private void setSelectablesListFunction(){
        List<Selectable> selectables = new ArrayList<>();
        User user = UserRepositoryFS.getInstance().getUserSession();

        // Se mira si es el mismo usuario para bloquear o desbloquear ciertas opciones
        if(post.getUser().getID().equals(user.getID())){
            selectables.add(new Selectable(R.drawable.ic_edit, R.string.edit_pet) {
                @Override
                public void onClick() {
                    DialogEditPet dialog = new DialogEditPet(post);
                    FragmentController.showDialog(dialog);
                    DialogPostSettings.this.dismiss();
                }
            });
            selectables.add(new Selectable(R.drawable.ic_delete, R.string.delete_pet) {
                @Override
                public void onClick() {
                    PostRepositoryFS.getInstance().deletePost(post, new CompleteListener() {
                        @Override
                        public void onSuccess() {
                            PostObserver.notifyPostDeleted(post.getID());
                        }

                        @Override
                        public void onFailure() {

                        }
                    });
                    DialogPostSettings.this.dismiss();
                }
            });
        } else {
            selectables.add(new Selectable(R.drawable.ic_save, R.string.save_pet) {
                @Override
                public void onClick() {
                    DialogPostSettings.this.dismiss();
                }
            });
            selectables.add(new Selectable(R.drawable.ic_comment, R.string.comment) {
                @Override
                public void onClick() {
                    SetFragmentComment.set(post);
                    DialogPostSettings.this.dismiss();
                }
            });
        }

        selectablesAdapter = new SelectableAdapter(requireContext(), selectables);
        selectablesList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        selectablesList.setAdapter(selectablesAdapter);
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
    }
}