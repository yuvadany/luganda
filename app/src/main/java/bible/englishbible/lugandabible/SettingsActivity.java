package bible.englishbible.lugandabible;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class SettingsActivity extends AppCompatActivity {
    private AdView mAdView;
    private Switch readMode;
    public static SharedPreferences sharedpreferences, sharedPreferencesReadMode, sharedPreferencesFont, englishBiblePrefrences;
    public static final String SHARED_PREF_ENGLISH_BIBLE = "english_bible";
    public static final String BIBLE_ENGLISH = "bible";
    public static final String kjv_textfiles = "kjv_";
    public static final String niv_textfiles = "niv_";
    public static String englishBible_file = niv_textfiles;
    public static final String SHARED_PREF_FONT_SIZE = "font_size";
    public static final float TEXT_FONT_SIZE = 13;
    public static final String TEXT_FONT_SIZE_VAR = "text_float_size";
    public static final String TEXT_FONT_SIZE_SELECTED = "text_font_size_selected";
    public static final String SHARED_PREF_NIGHT_DAY_MODE = "Night_Day_Mode";
    public static final String TEXT_COLOUR_VAR = "Text_Colour_Var";
    public static final String BACKROUND_COLOUR_VAR = "Background_Colour_Var";
    public static final int BLACK_COLOUR = Color.parseColor("#000000"); //f2f2f2
    public static final int WHITE_COLOUR = Color.parseColor("#f2f2f2");
    public static final boolean NIGHT_MODE = true;
    private TextView fontsizeprogress;
    Spinner fontSizeSpinner;
    RadioButton niv, kjv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle("Settings");
        fontSizeSpinner = (Spinner) findViewById(R.id.fontSizespinner);
        niv = (RadioButton) findViewById(R.id.niv);
        kjv = (RadioButton) findViewById(R.id.kjv);
        englishBiblePrefrences = getSharedPreferences(SHARED_PREF_ENGLISH_BIBLE, Context.MODE_PRIVATE);
        englishBible_file = englishBiblePrefrences.getString(BIBLE_ENGLISH, niv_textfiles);
        if (englishBible_file.equalsIgnoreCase(niv_textfiles)) {
            niv.setChecked(true);
        } else if (englishBible_file.equalsIgnoreCase(kjv_textfiles)) {
            kjv.setChecked(true);
        }
        sharedPreferencesFont = getSharedPreferences(SHARED_PREF_FONT_SIZE, Context.MODE_PRIVATE);
        fontSizeSpinner.setSelection(sharedPreferencesFont.getInt(TEXT_FONT_SIZE_SELECTED, 0));
        fontSizeSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                        sharedPreferencesFont = getSharedPreferences(SHARED_PREF_FONT_SIZE, Context.MODE_PRIVATE);
                        int old = sharedPreferencesFont.getInt(TEXT_FONT_SIZE_SELECTED, 0);
                        SharedPreferences.Editor editor = sharedPreferencesFont.edit();
                        editor.putFloat(TEXT_FONT_SIZE_VAR, Float.valueOf(String.valueOf(fontSizeSpinner.getSelectedItem())));
                        editor.putInt(TEXT_FONT_SIZE_SELECTED, position);
                        editor.commit();
                        if (old == sharedPreferencesFont.getInt(TEXT_FONT_SIZE_SELECTED, 0)) {

                        } else {
                            reloadMainActivity();
                        }
                    }

                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
        readMode = (Switch) findViewById(R.id.NightReadMode);
        sharedPreferencesReadMode = getSharedPreferences(SHARED_PREF_NIGHT_DAY_MODE, Context.MODE_PRIVATE);
        readMode.setChecked(sharedPreferencesReadMode.getBoolean(SHARED_PREF_NIGHT_DAY_MODE, NIGHT_MODE));
        readMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    setNightMode();
                } else {
                    setDayMode();
                }
            }
        });
        niv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((RadioButton) v).isChecked();
                // Check which radiobutton was pressed
                if (checked) {
                    englishBiblePrefrences = getSharedPreferences(SHARED_PREF_ENGLISH_BIBLE, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editorBibleEnglish = englishBiblePrefrences.edit();
                    editorBibleEnglish.putString(BIBLE_ENGLISH, niv_textfiles);
                    editorBibleEnglish.commit();
                    reloadMainActivity();
                }
            }
        });
        kjv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((RadioButton) v).isChecked();
                // Check which radiobutton was pressed
                if (checked) {
                    englishBiblePrefrences = getSharedPreferences(SHARED_PREF_ENGLISH_BIBLE, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editorBibleEnglish = englishBiblePrefrences.edit();
                    editorBibleEnglish.putString(BIBLE_ENGLISH, kjv_textfiles);
                    editorBibleEnglish.commit();
                    reloadMainActivity();
                }
            }
        });
        mAdView = (AdView) findViewById(R.id.adView);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
            }

            @Override
            public void onAdClosed() {

            }

            @Override
            public void onAdFailedToLoad(int errorCode) {

            }

            @Override
            public void onAdLeftApplication() {

            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        // Back button starts
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        // Back button ends
    }

    public void setNightMode() {
        sharedPreferencesReadMode = getSharedPreferences(SHARED_PREF_NIGHT_DAY_MODE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editorNightMode = sharedPreferencesReadMode.edit();
        editorNightMode.putInt(TEXT_COLOUR_VAR, WHITE_COLOUR);
        editorNightMode.putInt(BACKROUND_COLOUR_VAR, BLACK_COLOUR);
        editorNightMode.putBoolean(SHARED_PREF_NIGHT_DAY_MODE, true);
        editorNightMode.commit();
        startActivity(new Intent(this, MainActivity.class));
    }


    public void setDayMode() {
        sharedPreferencesReadMode = getSharedPreferences(SHARED_PREF_NIGHT_DAY_MODE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editorNightMode = sharedPreferencesReadMode.edit();
        editorNightMode.putInt(TEXT_COLOUR_VAR, BLACK_COLOUR);
        editorNightMode.putInt(BACKROUND_COLOUR_VAR, WHITE_COLOUR);
        editorNightMode.putBoolean(SHARED_PREF_NIGHT_DAY_MODE, false);
        editorNightMode.commit();
        startActivity(new Intent(this, MainActivity.class));
    }

    // back option starts
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
    // back option ends


    public void reloadMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
    }
}
