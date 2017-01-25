package com.example.pietyszukm.journeyplanner;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by pietyszukm on 15.01.2017.
 */

public class DetailsActivity extends Activity{
    private ImageView image;
    private TextView title;
    private TextView description;
    private Button button;
    protected Uri gmmIntentUri;
    private JourneyDataSource datasource;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        datasource = new JourneyDataSource(this);
        image = (ImageView) findViewById(R.id.main_image);
        title = (TextView) findViewById(R.id.title);
        description = (TextView) findViewById(R.id.description);
        button = (Button) findViewById(R.id.map_button);
        Intent intent = getIntent();
        int id = intent.getIntExtra("id",0);
        List<Journey> journeys = datasource.getAllJourneys();
        Journey journey = journeys.get(id);
        Glide.with(this).load(journey.getThumbnail()).into(image);
        title.setText(journey.getName());
        description.setText(journey.getDescription());
        gmmIntentUri = Uri.parse(journey.getUri());

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }}
        );
    }

}
