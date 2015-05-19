package com.example.awanyogap.mediastorecamera;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.util.IllegalFormatCodePointException;


public class MainActivity extends ActionBarActivity {

    final static int CAMERA_RESULT = 0;
    Uri imageFileUri;


    ImageView returnImageView;
    Button buttonTake;
    Button buttonSave;
    TextView teksTitle;
    TextView teksDeskripsi;
    EditText editTextTitle;
    EditText editTextDeskripsi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        returnImageView = (ImageView) findViewById(R.id.returnImageView);
        buttonTake = (Button) findViewById(R.id.buttonTake);
        buttonSave = (Button) findViewById(R.id.buttonSave);
        teksTitle = (TextView) findViewById(R.id.teksTitle);
        teksDeskripsi = (TextView) findViewById(R.id.teksDeskripsi);
        editTextTitle = (EditText) findViewById(R.id.editTeksTitle);
        editTextDeskripsi = (EditText) findViewById(R.id.editTeksDeskripsi);

        returnImageView.setVisibility(View.GONE);
        buttonSave.setVisibility(View.GONE);
        teksTitle.setVisibility(View.GONE);
        teksDeskripsi.setVisibility(View.GONE);
        editTextTitle.setVisibility(View.GONE);
        editTextDeskripsi.setVisibility(View.GONE);

        buttonTake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageFileUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new ContentValues());
                Intent myIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                myIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageFileUri);
                startActivityForResult(myIntent, CAMERA_RESULT);
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues contentValues = new ContentValues(3);
                contentValues.put(MediaStore.Images.Media.TITLE, editTextTitle.getText().toString());
                contentValues.put(MediaStore.Images.Media.DESCRIPTION, editTextDeskripsi.getText().toString());
                getContentResolver().update(imageFileUri, contentValues, null, null);

                Toast bread = Toast.makeText(MainActivity.this, "Record Updated", Toast.LENGTH_SHORT);
                bread.show();

                buttonTake.setVisibility(View.VISIBLE);

                returnImageView.setVisibility(View.GONE);
                buttonSave.setVisibility(View.GONE);
                teksTitle.setVisibility(View.GONE);
                teksDeskripsi.setVisibility(View.GONE);
                editTextTitle.setVisibility(View.GONE);
                editTextDeskripsi.setVisibility(View.GONE);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK) {
            buttonTake.setVisibility(View.GONE);

            buttonSave.setVisibility(View.VISIBLE);
            returnImageView.setVisibility(View.VISIBLE);
            teksTitle.setVisibility(View.VISIBLE);
            teksDeskripsi.setVisibility(View.VISIBLE);
            editTextTitle.setVisibility(View.VISIBLE);
            editTextDeskripsi.setVisibility(View.VISIBLE);

            int iw = 200;
            int ih = 200;

            try {
                BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
                bmpFactoryOptions.inJustDecodeBounds = true;
                Bitmap bmp = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageFileUri), null, bmpFactoryOptions);

                int heightRatio = (int) Math.ceil(bmpFactoryOptions.outHeight / (float) ih);
                int widthRatio = (int) Math.ceil(bmpFactoryOptions.outWidth / (float) iw);

                Log.v("HEIGHTRATIO", ""+heightRatio);
                Log.v("WIDTHRATIO", ""+widthRatio);

                if (heightRatio > 1 && widthRatio > 1) {
                    if (heightRatio > widthRatio) {
                        bmpFactoryOptions.inSampleSize = heightRatio;
                    } else {
                        bmpFactoryOptions.inSampleSize = widthRatio;
                    }
                }

                bmpFactoryOptions.inJustDecodeBounds = false;
                bmp = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageFileUri), null, bmpFactoryOptions);

                returnImageView.setImageBitmap(bmp);
            } catch (FileNotFoundException e) {
                Log.v("ERROT", e.toString());
            }
        }
    }
}
