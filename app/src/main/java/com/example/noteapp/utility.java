package com.example.noteapp;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.Firebase;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;

public class utility {
    static void showToast(Context context,String message){
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();

    }
    static CollectionReference getcollectionReferenceForNotes(){
        FirebaseUser currentUser=FirebaseAuth.getInstance().getCurrentUser();
       return FirebaseFirestore.getInstance().collection("notes").document(currentUser.getUid()).collection("my_notes");

    }
    static String timestampToString(Timestamp timestamp){
      return   new SimpleDateFormat("MM/DD/yyyy").format(timestamp.toDate());

    }
}