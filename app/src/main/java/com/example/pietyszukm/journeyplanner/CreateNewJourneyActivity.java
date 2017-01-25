package com.example.pietyszukm.journeyplanner;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by pietyszukm on 24.01.2017.
 */

public class CreateNewJourneyActivity extends AppCompatActivity {

    private static int PICK_PHOTO = 22;
    private ImageView image;
    private EditText nameEditText, descriptionEditText, costEditText, URIEditText, countryEditText;
    private Button button;
    private JourneyDataSource journeyDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_journey);
        image = (ImageView) findViewById(R.id.ivImage);
        nameEditText = (EditText)findViewById(R.id.editText);
        descriptionEditText = (EditText)findViewById(R.id.editText2);
        costEditText = (EditText)findViewById(R.id.editText3);
        countryEditText = (EditText)findViewById(R.id.editText4);
        URIEditText = (EditText)findViewById(R.id.editText5);
        button = (Button) findViewById(R.id.saveButton);
        journeyDataSource = new JourneyDataSource(getApplicationContext());
        journeyDataSource.open();
        final Intent intent = getIntent();
        final boolean notHasExtras = intent.getExtras().isEmpty();
        if(!notHasExtras){
            nameEditText.setText(intent.getStringExtra("journey_name"));
            descriptionEditText.setText(intent.getStringExtra("journey_description"));
            costEditText.setText(String.valueOf(intent.getIntExtra("journey_cost",0)));
            countryEditText.setText(intent.getStringExtra("journey_country"));
            URIEditText.setText(intent.getStringExtra("journey_uri"));
            image.setImageBitmap(ImageUtility.getImage(intent.getByteArrayExtra("journey_image")));
        }
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Drawable drawable = image.getDrawable();
                BitmapDrawable bitmapDrawable = ((BitmapDrawable) drawable);
                Bitmap bitmap = bitmapDrawable.getBitmap();
                byte[] photoBytes = ImageUtility.getBytes(bitmap);
                Integer cost = Integer.parseInt(costEditText.getText().toString());
                Journey journey = new Journey(nameEditText.getText().toString(),
                        descriptionEditText.getText().toString(),
                        cost,
                        countryEditText.getText().toString(),
                        URIEditText.getText().toString(),
                        photoBytes
                );
                if(notHasExtras){
                    journeyDataSource.addJourney(journey);
                } else {
                    Integer id = intent.getIntExtra("journey_id", 0);
                    journeyDataSource.updateJourney(id, journey);
                }
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public void pickImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_PHOTO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PHOTO && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                return;
            }
            try {
                InputStream inputStream = getContentResolver().openInputStream(data.getData());
                Bitmap icon = BitmapFactory.decodeStream(inputStream);
                image.setImageBitmap(icon);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
