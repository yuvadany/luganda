package bible.englishbible.lugandabible;

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
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class BookmarkActivity extends AppCompatActivity {
    private AdView mAdView;
    DBHelper dbhelper = new DBHelper(this);
    String[] arrayOfString= { "No  Bookmark found" };
    ArrayAdapter localArrayAdapter;
    public static final String SHARED_PREF_BOOKMARK = "Book_Mark";
    public static final String BOOKMARKED_VERSE = "Book_Marked_Verse";
    String header = "";
    String verse_bookmarked = "";
    SharedPreferences sharedpreferences,sharedPreferencesReadMode;
    public static final String SHARED_PREF_FONT_SIZE = "font_size";
    public static final float TEXT_FONT_SIZE = 13;
    public static final String TEXT_FONT_SIZE_VAR = "text_float_size";
    public static final String SHARED_PREF_NIGHT_DAY_MODE = "Night_Day_Mode";
    public static final String TEXT_COLOUR_VAR = "Text_Colour_Var";
    public static final String BACKROUND_COLOUR_VAR = "Background_Colour_Var";
    public static final int BLACK_COLOUR = Color.parseColor("#000000");
    public static final int WHITE_COLOUR = Color.parseColor("#f2f2f2");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);
        setTitle("Bookmarks");
        ListView notesListView;
        try
        {
            this.dbhelper.openDataBase();
            arrayOfString = dbhelper.getAllBookmarks();
            notesListView = (ListView)findViewById(R.id.noteslist);
            localArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayOfString){
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    /// Get the Item from ListView
                    View view = super.getView(position, convertView, parent);
                    TextView tv = (TextView) view.findViewById(android.R.id.text1);
                    sharedpreferences = getSharedPreferences(SHARED_PREF_FONT_SIZE, Context.MODE_PRIVATE);
                    sharedPreferencesReadMode = getSharedPreferences(SHARED_PREF_NIGHT_DAY_MODE, Context.MODE_PRIVATE);
                    tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, sharedpreferences.getFloat(TEXT_FONT_SIZE_VAR,TEXT_FONT_SIZE));
                    tv.setBackgroundColor(sharedPreferencesReadMode.getInt(BACKROUND_COLOUR_VAR, WHITE_COLOUR));
                    tv.setTextColor(sharedPreferencesReadMode.getInt(TEXT_COLOUR_VAR, BLACK_COLOUR));
                    // Return the view
                    return view;
                }
            };
            notesListView.setAdapter(localArrayAdapter);
            registerForContextMenu(notesListView);
        }catch (Exception localException) {
            System.out.println("Error...  # " + localException.getMessage());
        }
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
        if(getSupportActionBar()!= null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        // Back button ends
    }
    // back option starts
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
    // back option ends

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bookmark_menu, menu);
        ListView bookmarkList = (ListView) v;
        AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuInfo;
        String  verse = (String) bookmarkList.getItemAtPosition(acmi.position);
        sharedpreferences = getSharedPreferences(SHARED_PREF_BOOKMARK, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(BOOKMARKED_VERSE, verse);
        editor.commit();
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        sharedpreferences = getSharedPreferences(SHARED_PREF_BOOKMARK, Context.MODE_PRIVATE);
        verse_bookmarked = sharedpreferences.getString(BOOKMARKED_VERSE,"Holy");
        if(item.getItemId()==R.id.sharebookmarkVerse){
            try {
                verse_bookmarked = sharedpreferences.getString(BOOKMARKED_VERSE,"Holy");
                String input = verse_bookmarked;
                int number = input.indexOf("#");
                input = input.substring(number+1).trim();
                Intent localIntent = new Intent("android.intent.action.SEND");
                localIntent.setType("text/plain");
                localIntent.putExtra("android.intent.extra.SUBJECT", "Word  #");
                localIntent.putExtra("android.intent.extra.TEXT", input);
                startActivity(Intent.createChooser(localIntent, header));

            } catch (Exception e) {
            }
        } else if(item.getItemId()==R.id.deletebookmark){
            try
            {
                String input = verse_bookmarked;
                int number = input.indexOf("#");
                input = input.substring(number+1).trim();
                this.dbhelper.openDataBase();
                dbhelper.deleteBookmark(input);
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);
            }catch (Exception localException) {
                System.out.println("Error...  # " + localException.getMessage());
            }

        }
        return true;
    }
}
