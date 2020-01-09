package bible.englishbible.lugandabible;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import java.util.ArrayList;

public class SongsActivity extends  AppCompatActivity {
    ArrayList<String> listitems;
    Bundle bundle = new Bundle();
    DBHelperNoBookmark dbhelper = new DBHelperNoBookmark(this);
    String[] arrayOfString= { "Jesus, name above all names" };
    ArrayAdapter localArrayAdapter;
    private AdView mAdView;
    String defaulthint = "Search here";
    ArrayList songsList = new ArrayList();
    SharedPreferences sharedpreferences,sharedPreferencesReadMode;
    public static final String SHARED_PREF_FONT_SIZE = "font_size";
    public static final float TEXT_FONT_SIZE = 13;
    public static final String TEXT_FONT_SIZE_VAR = "text_float_size";
    public static final String SHARED_PREF_NIGHT_DAY_MODE = "Night_Day_Mode";
    public static final String TEXT_COLOUR_VAR = "Text_Colour_Var";
    public static final String BACKROUND_COLOUR_VAR = "Background_Colour_Var";
    public static final int TEXT_COLOUR = Color.parseColor("#000000");
    public static final int BACKROUND_COLOUR = Color.parseColor("#FFFFFF");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_songs);
        setTitle("Worship Songs");
        ListView localListView;
        try
        {
            Object localObject[] = { "No Songs Found" };
            this.dbhelper.openDataBase();
            arrayOfString = dbhelper.getSongDetails();
            localListView = (ListView)findViewById(R.id.listviewallsongs);
            localArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayOfString){
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    /// Get the Item from ListView
                    View view = super.getView(position, convertView, parent);
                    TextView tv = (TextView) view.findViewById(android.R.id.text1);
                    sharedpreferences = getSharedPreferences(SHARED_PREF_FONT_SIZE, Context.MODE_PRIVATE);
                    sharedPreferencesReadMode = getSharedPreferences(SHARED_PREF_NIGHT_DAY_MODE, Context.MODE_PRIVATE);
                    tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, sharedpreferences.getFloat(TEXT_FONT_SIZE_VAR,TEXT_FONT_SIZE));
                    tv.setBackgroundColor(sharedPreferencesReadMode.getInt(BACKROUND_COLOUR_VAR, BACKROUND_COLOUR));
                    tv.setTextColor(sharedPreferencesReadMode.getInt(TEXT_COLOUR_VAR, TEXT_COLOUR));
                    // Return the view
                    return view;
                }
            };
            localListView.setAdapter(localArrayAdapter);
            localListView.setOnItemClickListener(this.myOnItemClickListener);
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

// search starts

        SearchView localSearchView = (SearchView)findViewById(R.id.searchSong);
        localSearchView.setQueryHint(this.defaulthint);
        localSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            public boolean onQueryTextChange(String paramAnonymousString) {
                SearchView localSearchView = (SearchView) SongsActivity.this.findViewById(R.id.searchSong);
                if ((paramAnonymousString == null) || ("".equals(paramAnonymousString))) {
                    localSearchView.setQueryHint("Please Search with other word");
                    songsList = new ArrayList();
                    songsList.add("Please Search with different word");
                    ArrayAdapter localArrayAdapter = new ArrayAdapter(SongsActivity.this, android.R.layout.simple_list_item_1, SongsActivity.this.songsList){
                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            /// Get the Item from ListView
                            View view = super.getView(position, convertView, parent);
                            TextView tv = (TextView) view.findViewById(android.R.id.text1);
                            sharedpreferences = getSharedPreferences(SHARED_PREF_FONT_SIZE, Context.MODE_PRIVATE);
                            sharedPreferencesReadMode = getSharedPreferences(SHARED_PREF_NIGHT_DAY_MODE, Context.MODE_PRIVATE);
                            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, sharedpreferences.getFloat(TEXT_FONT_SIZE_VAR,TEXT_FONT_SIZE));
                            tv.setBackgroundColor(sharedPreferencesReadMode.getInt(BACKROUND_COLOUR_VAR, BACKROUND_COLOUR));
                            tv.setTextColor(sharedPreferencesReadMode.getInt(TEXT_COLOUR_VAR, TEXT_COLOUR));
                            // Return the view
                            return view;
                        }
                    };
                    ((ListView) SongsActivity.this.findViewById(R.id.searchresult)).setAdapter(localArrayAdapter);
                }
                return true;
            }

            public boolean onQueryTextSubmit(String paramAnonymousString) {
                Object localObject[] = {"No matches Found"};
                songsList = new ArrayList();
                View view = getCurrentFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                try {
                    //  SongsActivity.this.dbhelper.openDataBase();
                 /*   dbhelper.openDataBase();                   */
                    songsList = dbhelper.searchSong(paramAnonymousString);
                } catch (Exception localException) {
                    Log.d("Db Open issue ", localException.getMessage());
                }
                ArrayAdapter localArrayAdapter;
                ListView localListView;
                //localArrayAdapter = new ArrayAdapter(SongsActivity.this, android.R.layout.simple_list_item_1, SongsActivity.this.listitems);
                localArrayAdapter = new ArrayAdapter(SongsActivity.this, android.R.layout.simple_list_item_1, songsList){
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        /// Get the Item from ListView
                        View view = super.getView(position, convertView, parent);
                        TextView tv = (TextView) view.findViewById(android.R.id.text1);
                        sharedpreferences = getSharedPreferences(SHARED_PREF_FONT_SIZE, Context.MODE_PRIVATE);
                        sharedPreferencesReadMode = getSharedPreferences(SHARED_PREF_NIGHT_DAY_MODE, Context.MODE_PRIVATE);
                        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, sharedpreferences.getFloat(TEXT_FONT_SIZE_VAR,TEXT_FONT_SIZE));
                        tv.setBackgroundColor(sharedPreferencesReadMode.getInt(BACKROUND_COLOUR_VAR, BACKROUND_COLOUR));
                        tv.setTextColor(sharedPreferencesReadMode.getInt(TEXT_COLOUR_VAR, TEXT_COLOUR));
                        // Return the view
                        return view;
                    }
                };
                localListView = (ListView) SongsActivity.this.findViewById(R.id.searchresult);
                localListView.setAdapter(localArrayAdapter);
                localListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Bundle localBundle = new Bundle();
                        String song = ((TextView) view).getText().toString();
                        //Toast.makeText(getBaseContext(), story, Toast.LENGTH_LONG).show();
                        Intent localIntent = new Intent(SongsActivity.this, LyricsActivity.class);
                        localBundle.putString("title", song);
                        String lyrics = dbhelper.getLyrics(song);
                        localBundle.putString("lyrics", lyrics);
                        localIntent.putExtras(localBundle);
                        SongsActivity.this.startActivity(localIntent);
                    }
                });
                return true;
            }
        });
        //search ends

    }

    AdapterView.OnItemClickListener myOnItemClickListener = new AdapterView.OnItemClickListener()
    {
        public void onItemClick(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong)
        {
            String title = paramAnonymousAdapterView.getItemAtPosition(paramAnonymousInt).toString();
            int number = title.indexOf(".");
            title = title.substring(number+1);
            Bundle localBundle = new Bundle();
            String lyrics = dbhelper.getLyrics(title);
            Intent localIntent = new Intent(SongsActivity.this, LyricsActivity.class);
            localBundle.putString("title", title);
            localBundle.putString("lyrics", lyrics);
            localIntent.putExtras(localBundle);
            SongsActivity.this.startActivity(localIntent);
        }
    };
    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }
    // back option starts
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
    // back option ends
}
