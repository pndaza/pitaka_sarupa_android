package mm.pndaza.pitakasarupa.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.util.Log;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import mm.pndaza.pitakasarupa.R;
import mm.pndaza.pitakasarupa.utils.SharePref;

public class SplashScreenActivity extends AppCompatActivity {

    private static final String INPUT_PATH = "file:///android_asset/databases/pitaka_sarupa.db";
    private static final String DATABASE_FILENAME = "pitaka_sarupa.db";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        if (SharePref.getInstance(this).getPrefNightModeState()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        SharePref sharePref = SharePref.getInstance(this);
        if (sharePref.isFirstTime()) {
            Log.i("First Time : ", "yes");
            sharePref.noLongerFirstTime();
            sharePref.saveDefault();
        }

        if (isDatabaseCopied()) {
            startMainActivity();
        } else {
            setupDatabase();
        }

    }

    private String getOutputPath() {
        return getFilesDir() + "/databases/";
    }

    private boolean isDatabaseCopied() {
        return new File(getOutputPath() + DATABASE_FILENAME).exists();
    }

    private void setupDatabase() {
        new CopyDBAsync().execute();
    }

    public class CopyDBAsync extends AsyncTask<File, Double, Void> {


        protected Void doInBackground(File... files) {

            File path = new File(getOutputPath());
            // check database folder is exist and if not, make folder.
            if (!path.exists()) {
                path.mkdirs();
            }

            File outputFile = new File(getOutputPath() + DATABASE_FILENAME);

            try {
                InputStream input = getAssets().open("databases/" + DATABASE_FILENAME);
                OutputStream output = new FileOutputStream(outputFile);

                int bufferSize;
                final int size = input.available();
                long alreadyCopy = 0;

                byte[] buffer = new byte[1024];
                while ((bufferSize = input.read(buffer)) > 0) {
                    alreadyCopy += bufferSize;
                    output.write(buffer);
                    publishProgress(1.0d * alreadyCopy / size);
                }
                input.close();
                output.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e2) {
                e2.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(final Double... values) {
            super.onProgressUpdate(values);

            TextView progress = findViewById(R.id.tv_progress);
            int percentage = (int) (values[0] * 100);
            progress.setText(String.valueOf(percentage) + "%");


        }

        @Override
        protected void onPostExecute(Void result) {

            startMainActivity();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }

    private void startMainActivity() {

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 500);

    }

}
