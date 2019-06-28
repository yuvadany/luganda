package bible.englishbible.lugandabible;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddNotesActivity extends AppCompatActivity {
    Button save;
    DBHelper dbhelper = new DBHelper(this);
    String title = "holy";
    String message = "message";
    EditText titleEdit, messageEdit;
    String titleError = "Please type title";
    String messageError = "Please type message";
    String successMessage = "Notes added";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notes);
        save = (Button) findViewById(R.id.saveButton);
        titleEdit = (EditText) findViewById(R.id.title);
        messageEdit = (EditText) findViewById(R.id.message);
        // Back button starts
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        // Back button ends
        save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                title = String.valueOf(titleEdit.getText()).trim();
                message = String.valueOf(messageEdit.getText()).trim();
                if (0 < title.length() && 0 < message.length()) {
                    dbhelper.saveNote(title, message);
                    Toast.makeText(getApplicationContext(), successMessage, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AddNotesActivity.this, MainActivity.class));
                } else {
                    titleEdit.setError(titleError);
                    messageEdit.setError(messageError);
                }
            }

        });
    }

    // back option starts
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
    // back option ends
}
