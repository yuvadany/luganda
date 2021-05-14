package bibles.englishbible.lugandabible;

import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;

import static bibles.englishbible.lugandabible.BooksChapters.getAudioBiblelink;
import static bibles.englishbible.lugandabible.BooksChapters.getChaptersCount;

public class AudioActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, AdapterView.OnItemSelectedListener {
    Spinner book, chapter;
    private AdView mAdView;
    private Button play, pause;
    private String baseUrl = "http://wpkorg.wordproject.com/bibles/app/audio/42/";
    private String genesisOne = "http://wpkorg.wordproject.com/bibles/app/audio/42/1/1.mp3";
    public static MediaPlayer media = new MediaPlayer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);
        setContentView(R.layout.activity_audio);
        book = (Spinner) findViewById(R.id.books_spinner);
        chapter = (Spinner) findViewById(R.id.chapters_spinner);
        play = (Button) findViewById(R.id.play);
        pause = (Button) findViewById(R.id.pause);
        //book spinner starts
        String[] booksArray = new String[66];
        booksArray = loadBooks();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, booksArray);
//set the spinners adapter to the previously created one.
        book.setAdapter(adapter);
        //book spinner ends
        book.setOnItemSelectedListener(this);
        chapter.setOnItemSelectedListener(this);
        //media = new MediaPlayer();
        try {
            //you can change the path, here path is external directory(e.g. sdcard) /Music/maine.mp3
            media.setDataSource(genesisOne);
            media.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                media.start();
            }
        });
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (media.isPlaying()) {
                    //media.stop();
                    /* media.reset();*/
                    media.pause();
                    media.seekTo(0);
                    // media.release();
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

    // back option starts
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
    // back option ends

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View arg1, int arg2, long arg3) { /* TODO Auto-generated method stub*/
        String sp1 = String.valueOf(book.getSelectedItem());
        String sp2 = String.valueOf(chapter.getSelectedItem());
        switch (parent.getId()) {
            case R.id.books_spinner: {
                int chapters = getBooksChapter(sp1); /*  verses.setText(sp1);*/
                // Toast.makeText(getApplicationContext(), sp1, Toast.LENGTH_SHORT).show();
                List<String> list = new ArrayList<String>();
                for (int i = 0; i < chapters; i++) {
                    list.add(Integer.toString(i + 1));
                }
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                dataAdapter.notifyDataSetChanged();
                chapter.setAdapter(dataAdapter);
                try {
                    if (media.isPlaying()) {
                        media.reset();/*
                        media.pause();
                        media.seekTo(0);*/
                       /* media.release();
                        media = new MediaPlayer();*/
                    }
                    String mp3Url = baseUrl + getAudioBiblelink(sp1) + "/01.mp3";
                    media.setDataSource(mp3Url);
                    media.prepare();
                } catch (Exception e) {
                    System.out.println("books_spinner Error " + e);
                }
            }
            case R.id.chapters_spinner: {
                try {
                    if (media.isPlaying()) {
                        media.reset();
                        /*media.pause();
                        media.seekTo(0);*/
                        //media.release();
                        //  media = new MediaPlayer();
                    }
                    String mp3Url = baseUrl + getAudioBiblelink(sp1) + "/" + sp2 + ".mp3";
                    Toast.makeText(getApplicationContext(), "Loading " + sp1 + " Chapter " + sp2, Toast.LENGTH_LONG).show();
                    media.reset();
                    media.setDataSource(mp3Url);
                    media.prepare();
                    //media.start();
                } catch (Exception e) {
                    System.out.println("chapters_spinner Error " + e);
                }
            }
            break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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

}
