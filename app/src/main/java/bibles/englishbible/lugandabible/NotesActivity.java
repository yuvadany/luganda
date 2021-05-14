package bibles.englishbible.lugandabible;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class NotesActivity extends AppCompatActivity {
    private Button  closePopupBtn;
    PopupWindow popupWindow;
    LinearLayout linearLayout1;
    private TextView notes_text_popup;
    DBHelper dbhelper = new DBHelper(this);
    ListView notesListView;
    String[] notesArray = {"No  Bookmark found"};
    SharedPreferences sharedpreferences, sharedPreferencesReadMode;
    public static final String SHARED_PREF_FONT_SIZE = "font_size";
    public static final float TEXT_FONT_SIZE = 13;
    public static final String TEXT_FONT_SIZE_VAR = "text_float_size";
    public static final String SHARED_PREF_NIGHT_DAY_MODE = "Night_Day_Mode";
    public static final String TEXT_COLOUR_VAR = "Text_Colour_Var";
    public static final String BACKROUND_COLOUR_VAR = "Background_Colour_Var";
    public static final String SHARED_PREF_NOTES = "notes_preference";
    public static final String SELECTED_NOTES = "Selected_Notes";
    public static final String NOTES_DELETED = "Notes deleted";
    String header = "";
    public static final int BLACK_COLOUR = Color.parseColor("#000000");
    public static final int WHITE_COLOUR = Color.parseColor("#f2f2f2");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        notesListView = (ListView) findViewById(R.id.notesList);
        ArrayAdapter notesAdapter;
        PopupWindow popupWindow;
        LinearLayout linearLayout1;
        notes_text_popup = (TextView) findViewById(R.id.notes_text_popup);
        try {
            this.dbhelper.openDataBase();
            notesArray = dbhelper.getAllNotes();
            notesAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, notesArray) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    /// Get the Item from ListView
                    View view = super.getView(position, convertView, parent);
                    TextView tv = (TextView) view.findViewById(android.R.id.text1);
                    sharedpreferences = getSharedPreferences(SHARED_PREF_FONT_SIZE, Context.MODE_PRIVATE);
                    sharedPreferencesReadMode = getSharedPreferences(SHARED_PREF_NIGHT_DAY_MODE, Context.MODE_PRIVATE);
                    tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, sharedpreferences.getFloat(TEXT_FONT_SIZE_VAR, TEXT_FONT_SIZE));
                    tv.setBackgroundColor(sharedPreferencesReadMode.getInt(BACKROUND_COLOUR_VAR, WHITE_COLOUR));
                    tv.setTextColor(sharedPreferencesReadMode.getInt(TEXT_COLOUR_VAR, BLACK_COLOUR));
                    // Return the view
                    return view;
                }
            };
            notesListView.setAdapter(notesAdapter);
            registerForContextMenu(notesListView);
            // Back button starts
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
            }
            // Back button ends
        } catch (Exception e) {

        }
    }

    // back option starts
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
    // back option ends

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.notes_menu, menu);
        ListView notesList = (ListView) v;
        AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuInfo;
        String notes = (String) notesList.getItemAtPosition(acmi.position);
        int numberDot = notes.indexOf(".");
        notes = notes.substring(0, numberDot);
        sharedpreferences = getSharedPreferences(SHARED_PREF_NOTES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(SELECTED_NOTES, notes);
        editor.commit();
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        sharedpreferences = getSharedPreferences(SHARED_PREF_NOTES, Context.MODE_PRIVATE);
        String notes_selected = sharedpreferences.getString(SELECTED_NOTES, "Holy");
        if (item.getItemId() == R.id.menu_share_notes) {
            try {
                String message = "Holy God";
                message = dbhelper.getNotesById(notes_selected);
                Intent localIntent = new Intent("android.intent.action.SEND");
                localIntent.setType("text/plain");
                localIntent.putExtra("android.intent.extra.SUBJECT", "Message  #");
                localIntent.putExtra("android.intent.extra.TEXT", message);
                startActivity(Intent.createChooser(localIntent, header));

            } catch (Exception e) {
            }
        }
        if (item.getItemId() == R.id.menu_delete_notes) {
            try {
                dbhelper.deleteNote(notes_selected);
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);
                Toast.makeText(getApplicationContext(), NOTES_DELETED, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {

            }
        } if (item.getItemId() == R.id.menu_view_notes) {
            try {
                startActivity(new Intent(this, NotesViewActivity.class));
            } catch (Exception e) {

            }
        }if (item.getItemId() == R.id.menu_edit_notes) {
            try {

                startActivity(new Intent(this, EditNotesActivity.class));
            } catch (Exception e) {

            }
        }
        return true;
    }

}
