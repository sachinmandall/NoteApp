package com.example.noteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

public class NoteDetailsActivity extends AppCompatActivity {

    EditText titleEditText, contentEditText;
    ImageButton saveNoteButton;
    TextView pageTitleTextView;
    String title,content,docId;
    boolean isEditMode=false;
    TextView DeleteNoteTextViewButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);
        titleEditText = findViewById(R.id.Note_title_text);
        contentEditText = findViewById(R.id.Note_content_text);
        saveNoteButton = findViewById(R.id.save_note_button);
        pageTitleTextView=findViewById(R.id.page_title);
        DeleteNoteTextViewButton=findViewById(R.id.delete_note_text_view_button);


        //received Data
      title = getIntent().getStringExtra("title");
      content = getIntent().getStringExtra("content");
      docId = getIntent().getStringExtra("docId");

      if (docId!=null&& !docId.isEmpty()){
          isEditMode=true;
      }

      titleEditText.setText(title);
      contentEditText.setText(content);

      if (isEditMode){
          pageTitleTextView.setText("Edit Your Note");
          DeleteNoteTextViewButton.setVisibility(View.VISIBLE);
      }



        saveNoteButton.setOnClickListener((v) -> saveNote());
      DeleteNoteTextViewButton.setOnClickListener((v)-> deleteNoteFromFirebase());
    }

    void saveNote() {
        String noteTitle = titleEditText.getText().toString();
        String noteContent = contentEditText.getText().toString();

        // Validation
        if (noteTitle == null || noteTitle.isEmpty()) {
            // Setting error
            titleEditText.setError("Title is required");
            return;
        }

        Note note = new Note();
        note.setTitle(noteTitle);
        note.setContent(noteContent);
        note.setTimestamp(Timestamp.now());

        // Now, you can save the 'note' object to Firestore or perform any other desired action.
        saveNoteToFirebase(note);
    }
    void saveNoteToFirebase(Note note){
        DocumentReference documentReference;
        //update the note
        if (isEditMode){
            documentReference=utility.getcollectionReferenceForNotes().document(docId);

        }else{
            //create the note
            documentReference=utility.getcollectionReferenceForNotes().document();

        }

        documentReference.set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //note is Added
                    utility.showToast(NoteDetailsActivity.this,"Note Added Successfully");
                    finish();

                }else{
                    utility.showToast(NoteDetailsActivity.this,"Failed While Adding Note");

                }

            }
        });


    }
    void deleteNoteFromFirebase(){
        DocumentReference documentReference;

        documentReference=utility.getcollectionReferenceForNotes().document(docId);


        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //note is deleted
                    utility.showToast(NoteDetailsActivity.this,"Note Deleted Successfully");
                    finish();

                }else{
                    utility.showToast(NoteDetailsActivity.this,"Failed While Deleting Note");

                }

            }
        });

    }
}
