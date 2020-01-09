package bible.englishbible.lugandabible;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import static bible.englishbible.lugandabible.BooksChapters.getChaptersCount;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, AdapterView.OnItemSelectedListener {
    //  DBHelper dbhelper = new DBHelper(this);
    public int book_number = 1;
    ClipboardManager myClipboard;
    SharedPreferences sharedpreferences, sharedPreferencesReadMode, englishBiblePrefrences;
    public static final String SHARED_PREF_ENGLISH_BIBLE = "english_bible";
    public static final String BIBLE_ENGLISH = "bible";
    public static final String kjv_textfiles = "kjv_";
    public static final String niv_textfiles = "niv_";
    public static String englishBible_file = niv_textfiles;
    final Context context = this;
    ScrollView first, second, third;
    public HashMap chaptersMap = new HashMap<String, Integer>();
    TextView hindi_verses, english_verses, single_view;
    ListView englishList, singleList, hindiList;
    Spinner book, chapter;
    String language = "both";
    Bundle bundle = new Bundle();
    ScrollView englishview;
    private Animation fab_open, fab_close, rotate_forward, rotate_backward;
    private FloatingActionButton fabShare, addNotes, fab1, fab2, fab3, fab4;
    private Boolean isFabOpen = false;
    BooksChapters chapters = new BooksChapters();
    String defaulthint = "Search here";
    ArrayList<String> listitems, verseDate;
    Button closePopupBtn, shareVerse, shareVerseOnly, shareEearchResult, closeShareResult;
    PopupWindow popupWindow, popupWindowSearch;
    DrawerLayout verseLayout;
    TextView todayverse;
    String verseToday;
    DBHelper dbhelper = new DBHelper(this);
    DBHelperNoBookmark dbhelperNobookmark = new DBHelperNoBookmark(this);
    String searchResult = "test";
    String verseSelected = "Amen";
    String verse_selected = "Amen";
    String book_name = "Genesis";
    String chapter_number = "1";
    String header = "";
    private AdView mAdView;
    public static final String SHARED_PREF = "Last_Read";
    public static final String BOOK_NUMBER = "book";
    public static final String BOOK_NAME = "book_name";
    public static final String CHAPTER_NUMBER = "chapter";
    public static final String CHAPTER_NUMBER_BOOKMARK = "chapter";
    public static final String SHARED_PREF_BOOKMARK = "Book_Mark";
    public static final String SELECTED_VERSE = "Selected_Verse";
    public static final String SHARED_PREF_FONT_SIZE = "font_size";
    public static final float TEXT_FONT_SIZE = 13;
    public static final String TEXT_FONT_SIZE_VAR = "text_float_size";
    public static final String SHARED_PREF_NIGHT_DAY_MODE = "Night_Day_Mode";
    public static final String TEXT_COLOUR_VAR = "Text_Colour_Var";
    public static final String BACKROUND_COLOUR_VAR = "Background_Colour_Var";
    public static final int BLACK_COLOUR = Color.parseColor("#000000");
    public static final int WHITE_COLOUR = Color.parseColor("#f2f2f2");
    public static final String app_url = "https://play.google.com/store/apps/details?id=bible.englishbible.lugandabible";
    public static final String developer_id ="https://play.google.com/store/apps/developer?id=YUVARAJ+PALANISAMY";
    public static final String extraSubject = "The Holy Bible Luganda & English Bible Parallel";
    public static final String extraText ="\nHi,\n Check on this Holy Bible Luganda & English Parallel App\n\n" + app_url + " \n\n";
    public static final String bibleShare = "Luganda & English Bible Share";
    public static final String pay_app_url = "https://play.google.com/store/apps/details?id=noads.englishbible.lugandabible";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        // Popup Verse Starts
        verseToday = "Amen";
        /*sharedpreferences = getSharedPreferences(SHARED_PREF_FONT_SIZE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putFloat(TEXT_FONT_SIZE_VAR, TEXT_FONT_SIZE);
        editor.commit();*/
        /*sharedPreferencesReadMode = getSharedPreferences(SHARED_PREF_NIGHT_DAY_MODE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editorReadMode = sharedPreferencesReadMode.edit();
        editorReadMode.putInt(TEXT_COLOUR_VAR, TEXT_COLOUR);
        editorReadMode.putInt(BACKROUND_COLOUR_VAR, BACKROUND_COLOUR);
        editorReadMode.commit();*/
        Calendar cal = Calendar.getInstance();
        int doy = cal.get(Calendar.DAY_OF_YEAR);
        LayoutInflater layoutInflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = layoutInflater.inflate(R.layout.versepopup, null);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Screen width set starts Jan 23 -2018 by Yuvaraj Palanisamy
        int width = getWindowManager().getDefaultDisplay().getWidth();
        singleList = ((ListView) findViewById(R.id.SingleText));
        singleList.setVisibility(View.GONE);
        hindiList = ((ListView) findViewById(R.id.hindi_text));
        englishList = ((ListView) findViewById(R.id.english_text));
        book = (Spinner) findViewById(R.id.books_spinner);
        chapter = (Spinner) findViewById(R.id.chapters_spinner);
        englishBiblePrefrences = getSharedPreferences(SHARED_PREF_ENGLISH_BIBLE, Context.MODE_PRIVATE);
        englishBible_file = englishBiblePrefrences.getString(BIBLE_ENGLISH, niv_textfiles);
        //book spinner starts
        String[] booksArray = new String[66];
        booksArray = loadBooks();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, booksArray);
//set the spinners adapter to the previously created one.
        book.setAdapter(adapter);
        //book spinner ends
        book.setOnItemSelectedListener(this);
        chapter.setOnItemSelectedListener(this);
        fabShare = (FloatingActionButton) findViewById(R.id.share);
        fabShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    startActivity(new Intent(view.getContext(), SettingsActivity.class));
                } catch (Exception e) {

                }
            }
        });
        addNotes = (FloatingActionButton) findViewById(R.id.addNotesActivity);
        addNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    startActivity(new Intent(view.getContext(), AddNotesActivity.class));
                } catch (Exception e) {

                }
            }
        });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab3 = (FloatingActionButton) findViewById(R.id.fab3);
        fab4 = (FloatingActionButton) findViewById(R.id.fab4);
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_backward);
        fab1.setOnClickListener(this);
        fab2.setOnClickListener(this);
        fab3.setOnClickListener(this);
        fab4.setOnClickListener(this);
        SearchView localSearchView = (SearchView) findViewById(R.id.searchverse);
        localSearchView.setQueryHint(this.defaulthint);
        localSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            public boolean onQueryTextChange(String paramAnonymousString) {
                SearchView localSearchView = (SearchView) MainActivity.this.findViewById(R.id.searchverse);
                if ((paramAnonymousString == null) || ("".equals(paramAnonymousString))) {
                    localSearchView.setQueryHint("Type a word here");
                    listitems = new ArrayList();
                    ArrayAdapter localArrayAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, MainActivity.this.listitems) {
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
                    ((ListView) MainActivity.this.findViewById(R.id.searchresult)).setAdapter(localArrayAdapter);
                }
                return true;
            }

            public boolean onQueryTextSubmit(String paramAnonymousString) {
                Object localObject[] = {"No matches Found"};
                try {
                    String[] arrayOfString = searchVerse(paramAnonymousString);
                    localObject = arrayOfString;
                    searchResult = arrayToString(arrayOfString);
                } catch (Exception localException) {
                    Log.d("Db Open issue ", localException.getMessage());
                }
                ArrayAdapter localArrayAdapter;
                ListView localListView;
                MainActivity.this.listitems = new ArrayList(Arrays.asList((Object[]) localObject));
                listitems = new ArrayList(Arrays.asList((Object[]) localObject));
                localArrayAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, listitems) {
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
                localListView = (ListView) MainActivity.this.findViewById(R.id.searchresult);
                localListView.setAdapter(localArrayAdapter);
                return true;
            }
        });
        registerForContextMenu(singleList);
        registerForContextMenu(englishList);
        registerForContextMenu(hindiList);

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
    }

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

    @Override
    public void onItemSelected(AdapterView<?> parent, View arg1, int arg2, long arg3) { /* TODO Auto-generated method stub*/
        String sp1 = String.valueOf(book.getSelectedItem());
        String sp2 = String.valueOf(chapter.getSelectedItem());
        sharedpreferences = getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(BOOK_NUMBER, sp1);
        editor.putString(CHAPTER_NUMBER, sp2);
        editor.commit();
        switch (parent.getId()) {
            case R.id.books_spinner: {
                int chapters = getBooksChapter(sp1); /*  verses.setText(sp1);*/
                Toast.makeText(getApplicationContext(), sp1, Toast.LENGTH_SHORT).show();
                List<String> list = new ArrayList<String>();
                for (int i = 0; i < chapters; i++) {
                    list.add(Integer.toString(i + 1));
                }
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                dataAdapter.notifyDataSetChanged();
                chapter.setAdapter(dataAdapter);
                String hindi_verse = "No verses";
                //raw text test starts
                int num = 5;
                int id = 8;
                id = this.getResources().getIdentifier("lug_1_1", "raw", this.getPackageName());
                InputStream inputStream = getResources().openRawResource(id);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                int in;
                try {
                    in = inputStream.read();
                    while (in != -1) {
                        byteArrayOutputStream.write(in);
                        in = inputStream.read();
                    }
                    inputStream.close();
                    hindi_verse = byteArrayOutputStream.toString();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                sharedpreferences = getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
                ArrayAdapter praiseArrayAdapter5 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, getVerse(sharedpreferences.getString(BOOK_NUMBER, "1"), checkChaptersCount(sharedpreferences.getString(BOOK_NUMBER, "1"), sharedpreferences.getString(CHAPTER_NUMBER, "1")), "lug_")) {
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
                hindiList.setAdapter(praiseArrayAdapter5);
                // Register the ListView  for Context menu
                ArrayAdapter praiseArrayAdapter =
                        new ArrayAdapter(this, android.R.layout.simple_list_item_1,
                                getVerse(sharedpreferences.getString(BOOK_NUMBER, "1"), checkChaptersCount(sharedpreferences.getString(BOOK_NUMBER, "1"),
                                        sharedpreferences.getString(CHAPTER_NUMBER, "1")), englishBible_file)) {
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
                englishList.setAdapter(praiseArrayAdapter);
                registerForContextMenu(englishList);
                if ("hindi".equalsIgnoreCase(language)) {
                    praiseArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, getVerse(sharedpreferences.getString(BOOK_NUMBER, "1"), checkChaptersCount(sharedpreferences.getString(BOOK_NUMBER, "1"), sharedpreferences.getString(CHAPTER_NUMBER, "1")), "lug_")) {
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
                    singleList.setAdapter(praiseArrayAdapter);
                } else if ("niv".equalsIgnoreCase(language)) {
                    praiseArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, getVerse(sharedpreferences.getString(BOOK_NUMBER, "1"), checkChaptersCount(sharedpreferences.getString(BOOK_NUMBER, "1"), sharedpreferences.getString(CHAPTER_NUMBER, "1")), englishBible_file)) {
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
                    singleList.setAdapter(praiseArrayAdapter);
                }
                break;
            }
            case R.id.chapters_spinner: {
                sharedpreferences = getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
                String file = "lug_" + getBook_number(sp1) + "_" + Integer.parseInt(sp2);
                String hindi_verse = "test ";
                int id = 1;
                id = this.getResources().getIdentifier(file, "raw", this.getPackageName());
                InputStream inputStream = getResources().openRawResource(id);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                int in;
                try {
                    in = inputStream.read();
                    while (in != -1) {
                        byteArrayOutputStream.write(in);
                        in = inputStream.read();
                    }
                    inputStream.close();
                    hindi_verse = byteArrayOutputStream.toString();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                ArrayAdapter praiseArrayAdapter6 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, getVerse(sp1, sp2, "lug_")) {
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
                hindiList.setAdapter(praiseArrayAdapter6);
                /* hindi_verses.setText(hindi_verse);*/
                //String enlish_verses = dbhelper.getVerses("eng_bible", getBooks(sp1),Integer.parseInt(sp2));
                // english_verses.setText(enlish_verses);
                String enlish_verse = "Not Found";
                ArrayAdapter praiseArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, getVerse(sp1, checkChaptersCount(sharedpreferences.getString(BOOK_NUMBER, "1"), sharedpreferences.getString(CHAPTER_NUMBER, "1")), englishBible_file)) {
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
                englishList.setAdapter(praiseArrayAdapter);
                if ("hindi".equalsIgnoreCase(language)) {
                    ArrayAdapter praiseArrayAdapter1 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, getVerse(sp1, checkChaptersCount(sharedpreferences.getString(BOOK_NUMBER, "1"), sharedpreferences.getString(CHAPTER_NUMBER, "1")), "lug_")) {
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
                    singleList.setAdapter(praiseArrayAdapter1);
                    // single_text.setText(getVerse(sp1, sp2, "lug_"));
                } else if ("niv".equalsIgnoreCase(language)) {
                    ArrayAdapter praiseArrayAdapter2 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, getVerse(sp1, checkChaptersCount(sharedpreferences.getString(BOOK_NUMBER, "1"), sharedpreferences.getString(CHAPTER_NUMBER, "1")), englishBible_file)) {
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
                    singleList.setAdapter(praiseArrayAdapter2);
                }
                break;
            }

        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.many_options, menu);
        // menu.setHeaderTitle("Select The Action");
        //if (v.getId() == R.id.english_text) {
        book_name = String.valueOf(book.getSelectedItem());
        chapter_number = String.valueOf(chapter.getSelectedItem());
        ListView lv = (ListView) v;
        AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuInfo;
        String verse = (String) lv.getItemAtPosition(acmi.position);
        sharedpreferences = getSharedPreferences(SHARED_PREF_BOOKMARK, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(SELECTED_VERSE, verse);
        editor.putString(BOOK_NAME, book_name);
        editor.putString(CHAPTER_NUMBER_BOOKMARK, chapter_number);
        editor.commit();
        // }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        sharedpreferences = getSharedPreferences(SHARED_PREF_BOOKMARK, Context.MODE_PRIVATE);
        if (item.getItemId() == R.id.shareVerseMenu) {
            book_name = String.valueOf(book.getSelectedItem());
            chapter_number = String.valueOf(chapter.getSelectedItem());
            verse_selected = book_name + " :" + chapter_number + "\n" + sharedpreferences.getString(SELECTED_VERSE, "Holy");
            header = "Share " + book_name + " " + chapter_number + "'s verse via";
            try {
                Intent localIntent = new Intent("android.intent.action.SEND");
                localIntent.setType("text/plain");
                localIntent.putExtra("android.intent.extra.SUBJECT", "Word  #");
                localIntent.putExtra("android.intent.extra.TEXT", verse_selected);
                startActivity(Intent.createChooser(localIntent, header));
            } catch (Exception e) {
            }
        } else if (item.getItemId() == R.id.bookmark) {
            dbhelper.saveBookmark(sharedpreferences.getString(BOOK_NAME, "Genesis") + sharedpreferences.getString(CHAPTER_NUMBER_BOOKMARK, "1") + " : " + sharedpreferences.getString(SELECTED_VERSE, "Holy"));
            Toast.makeText(MainActivity.this, "Bookmarked", Toast.LENGTH_LONG).show();
        } else if (item.getItemId() == R.id.notes) {
            startActivity(new Intent(this, NotesActivity.class));
        } else if (item.getItemId() == R.id.addNotes) {
            startActivity(new Intent(this, AddNotesActivity.class));
        } else if (item.getItemId() == R.id.copy) {
            myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            ClipData copiedVerseClipData;
            String verse = verse_selected = book_name + " :" + chapter_number + "\n" + sharedpreferences.getString(SELECTED_VERSE, "Holy");
            copiedVerseClipData = ClipData.newPlainText("verse", verse);
            myClipboard.setPrimaryClip(copiedVerseClipData);
            Toast.makeText(MainActivity.this, "Copied", Toast.LENGTH_SHORT).show();
        }
        return true;
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent localIntent1;
        localIntent1 = new Intent(MainActivity.this, MainActivity.class);
        String sp1 = String.valueOf(book.getSelectedItem());
        String sp2 = String.valueOf(chapter.getSelectedItem());
        switch (id) {
            case R.id.fab1:
                animateFAB();
                break;
            case R.id.fab2: {
                singleList.setVisibility(View.VISIBLE);
                hindiList.setVisibility(View.GONE);
                englishList.setVisibility(View.GONE);
                ArrayAdapter praiseArrayAdapter2 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, getVerse(sp1, sp2, "lug_")) {
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
                singleList.setAdapter(praiseArrayAdapter2);
                language = "hindi";
                break;
            }
            case R.id.fab3: {
                this.bundle.putString("verses", "In the beginning God created the heaven and the earth.");
                singleList.setVisibility(View.VISIBLE);
                hindiList.setVisibility(View.GONE);
                englishList.setVisibility(View.GONE);
                language = "niv";
                ArrayAdapter praiseArrayAdapter2 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, getVerse(sp1, sp2, englishBible_file)) {
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
                singleList.setAdapter(praiseArrayAdapter2);
                break;
            }
            case R.id.fab4: {
                singleList.setVisibility(View.GONE);
                hindiList.setVisibility(View.VISIBLE);
                englishList.setVisibility(View.VISIBLE);
                ArrayAdapter praiseArrayAdapter2 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, getVerse(sp1, sp2, englishBible_file)) {
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
                englishList.setAdapter(praiseArrayAdapter2);
                ArrayAdapter praiseArrayAdapter3 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, getVerse(sp1, sp2, "lug_")) {
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
                hindiList.setAdapter(praiseArrayAdapter3);
                language = "both";
                break;
            }
        }
    }

    public void animateFAB() {

        if (isFabOpen) {
            fab2.startAnimation(rotate_backward);
            fab2.startAnimation(fab_open);
            fab3.startAnimation(fab_open);
            fab4.startAnimation(fab_open);
            fab2.setClickable(true);
            fab3.setClickable(true);
            fab4.setClickable(true);
            isFabOpen = false;
        } else {
            fab1.startAnimation(rotate_forward);
            fab2.startAnimation(fab_open);
            fab3.startAnimation(fab_open);
            fab4.startAnimation(fab_open);
            fab2.setClickable(true);
            fab3.setClickable(true);
            fab4.setClickable(true);
            isFabOpen = true;
        }
    }

    public ArrayList getVerse(String sp1, String sp2, String bible) {
        String verses = "Not Found";
        String file = bible + getBook_number(sp1) + "_" + Integer.parseInt(sp2);
        ArrayList<String> versesArrayList = new ArrayList();
        int id = 1;
        String line;
        id = this.getResources().getIdentifier(file, "raw", this.getPackageName());
        InputStream inputStream = getResources().openRawResource(id);
        int in;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            while ((line = br.readLine()) != null) {
                versesArrayList.add(line);
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return versesArrayList;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.bookmark) {
            startActivity(new Intent(this, BookmarkActivity.class));
        } else if (id == R.id.rate) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(app_url));
            startActivity(intent);
        } else if (id == R.id.audio) {
            startActivity(new Intent(this, AudioActivity.class));
        } else if (id == R.id.noAds) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(pay_app_url));
            startActivity(intent);
        } else if (id == R.id.praises) {
            startActivity(new Intent(this, PraisesActivity.class));
        } else if (id == R.id.notes) {
            startActivity(new Intent(this, NotesActivity.class));
        } else if (id == R.id.vod) {
            try {
                Calendar cal = Calendar.getInstance();
                int doy = cal.get(Calendar.DAY_OF_YEAR);
                //this.dbhelper.openDataBase();
                verseDate = dbhelperNobookmark.getVerse(doy);
                Toast.makeText(MainActivity.this, verseDate.get(0), Toast.LENGTH_LONG).show();
            } catch (Exception e) {
            }
        } else if (id == R.id.songs) {
            startActivity(new Intent(this, SongsActivity.class));
        } else if (id == R.id.more) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(developer_id));
            startActivity(intent);
        } else if (id == R.id.nav_share) {
            try {
                Intent localIntent2 = new Intent("android.intent.action.SEND");
                localIntent2.setType("text/plain");
                localIntent2.putExtra("android.intent.extra.SUBJECT", extraSubject);
                localIntent2.putExtra("android.intent.extra.TEXT", extraText);
                startActivity(Intent.createChooser(localIntent2, bibleShare));
            } catch (Exception e) {

            }
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


// }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stu

    }

    public String[] searchVerse(String verse) {
        View view = this.getCurrentFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        String verses = "Not Found";
        StringBuffer sb = new StringBuffer();
        String file = englishBible_file + "1_1";
        String[] words = new String[2];

        int id = 1;
        words[0] = verse.substring(0, 1).toUpperCase() + verse.substring(1).toLowerCase();
        words[1] = verse.substring(0, 1).toLowerCase() + verse.substring(1).toLowerCase();
        for (int i = 1; i <= 66; i++) {
            for (int j = 1; j <= getChaptersCount(i); j++) {
                file = englishBible_file + i + "_" + j;
                id = this.getResources().getIdentifier(file, "raw", this.getPackageName());
                //  Toast.makeText(MainActivity.this,  this.getPackageName(), Toast.LENGTH_SHORT).show();
                InputStream inputStream = getResources().openRawResource(id);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                int in;
                String line;
                String result;
                try {
                    BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                    while ((line = br.readLine()) != null) {
                        if (line.contains(words[0]) || line.contains(words[1])) {
                            sb.append(chapters.getBookName(i) + ":" + j + "\n" + line + "\n");
                        }
                    }
                    verses = sb.toString();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        for (int i = 1; i <= 66; i++) {
            for (int j = 1; j <= getChaptersCount(i); j++) {
                file = "lug_" + i + "_" + j;
                id = this.getResources().getIdentifier(file, "raw", this.getPackageName());
                InputStream inputStream = getResources().openRawResource(id);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                int in;
                String line;
                String result;
                try {
                    BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                    while ((line = br.readLine()) != null) {
                        if (line.contains(words[0]) || line.contains(words[1])) {
                            sb.append(chapters.getBookName(i) + ":" + j + "\n" + line + "\n");
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (null == sb.toString() || "".equalsIgnoreCase(sb.toString())) {
            Toast.makeText(MainActivity.this, " No match found", Toast.LENGTH_SHORT).show();
        } else {
            sb.append("\n\n\n");
        }
        return stringbufferToArray(sb);

    }

    public String[] stringbufferToArray(StringBuffer paramStringBuffer) {
        ArrayList localArrayList = new ArrayList();
        StringTokenizer localStringTokenizer = new StringTokenizer(paramStringBuffer.toString(), "\n");
        while (localStringTokenizer.hasMoreTokens()) {
            localArrayList.add(localStringTokenizer.nextToken());
        }
        int i = localArrayList.size();
        String[] arrayOfString = new String[i];
        Iterator localIterator = localArrayList.iterator();
        for (int j = 0; (localIterator.hasNext()) && (i - 1 >= 0); j++) {
            arrayOfString[j] = localIterator.next().toString();
            i--;
        }
        return arrayOfString;
    }


    public String arrayToString(String[] array) {
        StringBuilder builder = new StringBuilder();
        for (String s : array) {
            builder.append(s + "\n");
        }
        String searchresult = builder.toString();
        return searchresult;
    }

    public String[] loadBooks() {
        String[] books = new String[66];
        BooksChapters booksChapters = new BooksChapters();
        for (int i = 0; i < 66; i++) {
            books[i] = booksChapters.getBookName(i + 1);
        }
        return books;
    }


    public int getBooksChapter(String bookName) {
        BooksChapters booksChapters = new BooksChapters();
        String[] booksArray = new String[66];
        booksArray = loadBooks();
        for (int i = 0; i < 66; i++) {
            if (booksArray[i].equalsIgnoreCase(bookName)) {
                return getChaptersCount(i + 1);
            }
        }
        return 1;
    }

    public int getBook_number(String bookName) {
        BooksChapters booksChapters = new BooksChapters();
        String[] booksArray = new String[66];
        booksArray = loadBooks();
        for (int i = 0; i < 66; i++) {
            if (booksArray[i].equalsIgnoreCase(bookName)) {
                return i + 1;
            }
        }
        return 1;
    }


    public String checkChaptersCount(String bookSpinner, String chapterSpinner) {
        String chapterOne = "1";
        int bookNumber = getBook_number(bookSpinner);
        int chapterNumber = Integer.parseInt(chapterSpinner);
        if (1 < chapterNumber) {
            int totalChapters = getChaptersCount(bookNumber);
            if (totalChapters >= chapterNumber) {
                return String.valueOf(chapterNumber);
            } else if (totalChapters < chapterNumber) {
                return chapterOne;
            }
            return chapterOne;
        } else {
            return chapterOne;
        }
    }


}