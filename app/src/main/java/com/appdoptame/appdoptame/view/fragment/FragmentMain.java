package com.appdoptame.appdoptame.view.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appdoptame.appdoptame.R;
import com.appdoptame.appdoptame.data.listener.PostListLoaderListener;
import com.appdoptame.appdoptame.model.Person;
import com.appdoptame.appdoptame.model.Pet;
import com.appdoptame.appdoptame.model.Post;
import com.appdoptame.appdoptame.model.User;
import com.appdoptame.appdoptame.view.adapter.PostAdapter;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class FragmentMain extends Fragment {
    private PostAdapter   feedAdapter;
    private RecyclerView  feedRecyclerView;

    public FragmentMain(){

    }

    @SuppressLint("InflateParams") @Nullable @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        postponeEnterTransition(1, TimeUnit.MILLISECONDS);
        return inflater.inflate(R.layout.fragment_feed, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadComponents(savedInstanceState);
    }

    private void setListFunction(){
        feedAdapter = new PostAdapter(requireContext(), new ArrayList<>());
        feedRecyclerView.setLayoutManager(new LinearLayoutManager(
                getContext(), LinearLayoutManager.VERTICAL, false)
        );
        feedRecyclerView.setAdapter(feedAdapter);
    }

    private void loadComponents(Bundle savedState){
        feedRecyclerView = requireView().findViewById(R.id.feed_list);

        setListFunction();

        getPosts(new PostListLoaderListener() {
            @Override
            public void onSuccess(List<Post> posts) {
                feedAdapter.setPosts(posts);
            }

            @Override
            public void onFailure() {

            }
        });
    }


    /**
     * TEMPORAL
     */
    private void getPosts(PostListLoaderListener listener){
        ArrayList<Post> posts = new ArrayList<>();
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        CollectionReference collectionPost = database.collection("post");
        collectionPost.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot document : task.getResult()) {
                    posts.add(parsePost(document.getData()));
                }
                listener.onSuccess(posts);
            } else {
                listener.onFailure();
            }

        });
    }

    private User parseUser(Map<String, Object> doc){
        long   age            = (long) doc.get("AGE");
        String department     = (String) doc.get("DEPARTMENT");
        String phone          = (String) doc.get("PHONE");
        String city           = (String) doc.get("CITY");
        String name           = (String) doc.get("NAME");
        String image          = (String) doc.get("IMAGE");
        String lastName       = (String) doc.get("LAST_NAME");
        String identification = (String) doc.get("CC");
        String ID             = (String) doc.get("ID");

        return new Person(ID, identification, name, lastName, phone, city, department, image, age);
    }

    private Post parsePost(Map<String, Object> doc){
        List<String> likes  = (List<String>) doc.get("LIKES");
        Date         date   = ((Timestamp) doc.get("DATE")).toDate();
        String       ID     = (String) doc.get("ID");
        Pet          pet    = parsePet((Map<String, Object>) doc.get("PET"));
        User         person = parseUser((Map<String, Object>) doc.get("PERSON"));

        return new Post(ID, date, likes, person, pet);
    }

    private Pet parsePet(Map<String, Object> doc){
        long         age            = (long) doc.get("AGE");
        String       department     = (String) doc.get("DEPARTMENT");
        String       description    = (String) doc.get("DESCRIPTION");
        String       city           = (String) doc.get("CITY");
        String       name           = (String) doc.get("NAME");
        String       type           = (String) doc.get("TYPE");
        String       breed          = (String) doc.get("BREED");
        String       sex            = (String) doc.get("SEX");
        String       ID             = (String) doc.get("ID");
        boolean      stray          = (boolean) doc.get("STRAY");
        boolean      adopted        = (boolean) doc.get("ADOPTED");
        boolean      sterilized     = (boolean) doc.get("STERILIZED");
        long         size           = (long) doc.get("SIZE");
        long         weight         = (long) doc.get("WEIGHT");
        List<String> images         = (List<String>) doc.get("IMAGES");

        return new Pet(ID, name, type, sex, description, city, department, breed, stray, sterilized, adopted, age, size, weight, images);
    }
}