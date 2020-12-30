package mm.pndaza.pitakasarupa.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.sdsmdg.tastytoast.TastyToast;

import mm.pndaza.pitakasarupa.R;
import mm.pndaza.pitakasarupa.database.DBOpenHelper;
import mm.pndaza.pitakasarupa.model.Detail;
import mm.pndaza.pitakasarupa.utils.MDetect;
import mm.pndaza.pitakasarupa.utils.Rabbit;
import mm.pndaza.pitakasarupa.utils.SharePref;
import mm.pndaza.pitakasarupa.utils.TextStyle;

public class DetailActivity extends AppCompatActivity {

//    private static final String TAG = DetailActivity.class.getSimpleName();
    private final DBOpenHelper dbOpenHelper = DBOpenHelper.getInstance(this);
    private int word_id;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        MDetect.init(this);
        setTitle(MDetect.getDeviceEncodedText(getString(R.string.detail_mm)));

        Intent intent = getIntent();
        int _id = intent.getIntExtra("_id", 0);
        word_id = _id;
        Detail detail = dbOpenHelper.getDetailById(_id);

        textView = findViewById(R.id.tv_detail);
        // TODO to check why text size seem a little bit on Display result.
        // when using  -4. get the same look as textView in recyclerView
        textView.setTextSize(SharePref.getInstance(this).getPrefFontSize()-4);
        textView.setText( TextStyle.getStyle(detail,this), TextView.BufferType.SPANNABLE);
        textView.setTextIsSelectable(true);

        manageRecent(word_id);

    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_detail, menu);
        MenuItem addBookmarkItem = menu.findItem(R.id.menu_add_bookmark);
        setIcon(addBookmarkItem); // set icon based on bookmark exist ot not
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_copy:
                copyToClipboard();
                showToast(getString(R.string.msg_copy),TastyToast.SUCCESS);

                return true;
            case R.id.menu_add_bookmark:
                manageBookmark(item);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void copyToClipboard(){
        String textToCopy = textView.getText().toString();
        ClipboardManager clipboard= (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("copy", textToCopy);
        clipboard.setPrimaryClip(clip);

    }

    private void manageBookmark(MenuItem item){
        if(checkBookmarkExist(word_id)){
            removeFromBookmark(word_id);
            item.setIcon(R.drawable.ic_add_bookmark);
            showToast(getString(R.string.msg_bookmark_removed), TastyToast.INFO);
        } else {
            addToBookmark(word_id);
            item.setIcon(R.drawable.ic_added_bookmark);
            showToast(getString(R.string.msg_bookmark_added), TastyToast.SUCCESS);
        }
    }

    private boolean checkBookmarkExist(int word_id){

        return dbOpenHelper.isBookmarkExist(word_id);
    }

    private void addToBookmark(int word_id) {
        dbOpenHelper.addToBookmark(word_id);
    }

    private void removeFromBookmark(int word_id){
        dbOpenHelper.removeBookmarkById(word_id);
    }

    private void setIcon(MenuItem item){
        item.setIcon(checkBookmarkExist(word_id)? R.drawable.ic_added_bookmark : R.drawable.ic_add_bookmark);
    }

    private void manageRecent(int word_id){
        if(!checkRecentExist(word_id)){
            addToRecent(word_id);
        }
    }

    private boolean checkRecentExist(int word_id){
        return dbOpenHelper.isRecentExist(word_id);
    }

    private void addToRecent(int word_id){
        dbOpenHelper.addToRecent(word_id);
    }

    private void showToast(String msg,int toastMode ){
        TastyToast.makeText(getApplicationContext(),
                MDetect.getDeviceEncodedText(msg), TastyToast.LENGTH_LONG, toastMode);
    }

}