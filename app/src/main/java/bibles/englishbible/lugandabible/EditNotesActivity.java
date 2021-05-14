package bibles.englishbible.lugandabible;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.StringTokenizer;

public class EditNotesActivity extends AppCompatActivity {
    SharedPreferences sharedpreferencesNotes;
    public static final String SHARED_PREF_NOTES = "notes_preference";
    public static final String SELECTED_NOTES = "Selected_Notes";
    public static final String NOTES_DELETED = "Notes deleted";
    EditText titleEdit, messageEdit;
    DBHelper dbhelper = new DBHelper(this);
    TextView id;
    StringTokenizer token, title, message;
    String titleMessage = "message";
    String delimeter = "#", notes_selected;
    Button save;
    String successMessage = "Notes updated";
    String titleError = "Please type title";
    String messageError = "Please type message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_notes);
        sharedpreferencesNotes = getSharedPreferences(SHARED_PREF_NOTES, Context.MODE_PRIVATE);
        notes_selected = sharedpreferencesNotes.getString(SELECTED_NOTES, "Holy");
        titleEdit = (EditText) findViewById(R.id.titleEdit);
        messageEdit = (EditText) findViewById(R.id.messageEdit);
        id = (TextView) findViewById(R.id.id);
        id.setText("Id # " + notes_selected);
        save = (Button) findViewById(R.id.saveButton);
        try {
            titleMessage = dbhelper.getTitleMessageById(notes_selected);
        } catch (Exception e) {

        }
        token = new StringTokenizer(titleMessage, delimeter);
        titleEdit.setText(token.nextToken());
        int index = titleMessage.indexOf("#");
        messageEdit.setText(titleMessage.substring(index+1));

        save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String  title = String.valueOf(titleEdit.getText()).trim();
                String message = String.valueOf(messageEdit.getText()).trim();
                if (0 < title.length() && 0 < message.length()) {
                    dbhelper.updateNote(notes_selected, title, message);
                    Toast.makeText(getApplicationContext(), successMessage, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(EditNotesActivity.this, MainActivity.class));
                } else {
                    titleEdit.setError(titleError);
                    messageEdit.setError(messageError);
                }
            }

        });
    }
}
