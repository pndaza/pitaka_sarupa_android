package mm.pndaza.pitakasarupa.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import mm.pndaza.pitakasarupa.R;
import mm.pndaza.pitakasarupa.fragment.BookmarkFragment;
import mm.pndaza.pitakasarupa.fragment.InfoFragment;
import mm.pndaza.pitakasarupa.fragment.RecentFragment;
import mm.pndaza.pitakasarupa.fragment.SettingFragment;
import mm.pndaza.pitakasarupa.fragment.WordListFragment;
import mm.pndaza.pitakasarupa.utils.MDetect;

public class MainActivity extends AppCompatActivity
        implements WordListFragment.OnWordlistSelectedListener,
        RecentFragment.OnRecentSelectedListener,
        BookmarkFragment.OnBookmarkSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setSupportActionBar(findViewById(R.id.toolbar));
        MDetect.init(this);

        if( savedInstanceState == null) {

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_layout, new WordListFragment());
            fragmentTransaction.commit();
        }

        BottomNavigationView navView = findViewById(R.id.navigation);
        navView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.navigation_home) {
                selectedFragment = new WordListFragment();
            } else if (itemId == R.id.navigation_bookmark) {
                selectedFragment = new BookmarkFragment();
            } else if (itemId == R.id.navigation_recent) {
                selectedFragment = new RecentFragment();
            } else if (itemId == R.id.navigation_setting) {
                selectedFragment = new SettingFragment();
            } else if (itemId == R.id.navigation_info) {
                selectedFragment = new InfoFragment();
            } else {
                selectedFragment = new WordListFragment(); // Default case
            }

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_layout, selectedFragment);
            fragmentTransaction.commit();
            return true; // Indicate that the item selection was handled
        });

    }

    @Override
    public void onWordlistSelected(int word_id) {
        startDetailActivity(word_id);
    }

    @Override
    public void onRecentSelected(int word_id) {
        startDetailActivity(word_id);
    }

    @Override
    public void onBookmarkSelected(int word_id) {
        startDetailActivity(word_id);
    }

    private void startDetailActivity(int word_id) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("_id", word_id);
        startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {

        super.onSaveInstanceState(outState);
    }
}
