package com.appdoptame.appdoptame.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.appdoptame.appdoptame.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //addsfsfd


     //   PRUEBA FIRESTORE (LA BASE DE DATOS)


        FirebaseFirestore database = FirebaseFirestore.getInstance();
        CollectionReference aguapanela = database.collection("ggggg");


        /*
        aguapanela.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        System.out.println(document.getId() + " => " + document.getData());
                    }
                } else {
                    System.out.println("Error getting documents."+ task.getException());
                }

            }
        });

         */
        System.out.println(aguapanela.getId());
        System.out.println(aguapanela.getParent());
        System.out.println(aguapanela.getPath());


    }
}