package bible.englishbible.lugandabible;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MenuItem;
import android.widget.TextView;

public class NotesViewActivity extends AppCompatActivity {
    TextView titleMessage;
    DBHelper dbhelper = new DBHelper(this);
    public static final String SHARED_PREF_NOTES = "notes_preference";
    public static final String SELECTED_NOTES = "Selected_Notes";
    SharedPreferences sharedpreferences,sharedPreferencesFont,sharedPreferencesReadMode;
    public static final String SHARED_PREF_FONT_SIZE = "font_size";
    public static final float TEXT_FONT_SIZE = 13;
    public static final String TEXT_FONT_SIZE_VAR = "text_float_size";
    public static final String SHARED_PREF_NIGHT_DAY_MODE = "Night_Day_Mode";
    public static final String TEXT_COLOUR_VAR = "Text_Colour_Var";
    public static final String BACKROUND_COLOUR_VAR = "Background_Colour_Var";
    public static final int BLACK_COLOUR = Color.parseColor("#000000");
    public static final int WHITE_COLOUR = Color.parseColor("#f2f2f2");
    String message = "Holy God";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_view);
        titleMessage = (TextView) findViewById(R.id.titleMessage);
        sharedpreferences = getSharedPreferences(SHARED_PREF_NOTES, Context.MODE_PRIVATE);
        String notes_selected = sharedpreferences.getString(SELECTED_NOTES, "Holy");
        sharedPreferencesFont = getSharedPreferences(SHARED_PREF_FONT_SIZE, Context.MODE_PRIVATE);
        sharedPreferencesReadMode = getSharedPreferences(SHARED_PREF_NIGHT_DAY_MODE, Context.MODE_PRIVATE);
        try {
            message = dbhelper.getNotesById(notes_selected);
        } catch (Exception e) {
        }
        titleMessage.setText(message);
        titleMessage.setTextSize(TypedValue.COMPLEX_UNIT_DIP, sharedPreferencesFont.getFloat(TEXT_FONT_SIZE_VAR,TEXT_FONT_SIZE));
        titleMessage.setBackgroundColor(sharedPreferencesReadMode.getInt(BACKROUND_COLOUR_VAR, WHITE_COLOUR));
        titleMessage.setTextColor(sharedPreferencesReadMode.getInt(TEXT_COLOUR_VAR, BLACK_COLOUR));
        // Back button starts
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        // Back button ends
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
