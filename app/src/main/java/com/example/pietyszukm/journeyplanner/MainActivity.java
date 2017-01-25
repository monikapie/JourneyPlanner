package com.example.pietyszukm.journeyplanner;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.res.Resources;
import android.graphics.Rect;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private JourneysAdapter adapter;
    private List<Journey> journeysList;
    private JourneyDataSource datasource;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        initCollapsingToolbar();
        setSupportActionBar(toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        ImageButton plusButton = (ImageButton) findViewById(R.id.fab);
        plusButton.setOnClickListener(new View.OnClickListener() {
          public void onClick(View v) {
              Intent intent= new Intent(getApplicationContext(), CreateNewJourneyActivity.class);
              startActivity(intent);
          }});
        journeysList = new ArrayList<>();
        adapter = new JourneysAdapter(this, journeysList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        datasource = new JourneyDataSource(this);
        datasource.open();
        journeysList.addAll(datasource.getAllJourneys());
        adapter.notifyDataSetChanged();
        prepareAlbums();
        try {
            Glide.with(this).load(R.drawable.travelgirl).into((ImageView) findViewById(R.id.backdrop));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Initializing collapsing toolbar
     * Will show and hide the toolbar title on scroll
     */
    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(getString(R.string.app_name));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    /**
     * Adding few albums for testing
     */
    private void prepareAlbums() {
        int[] covers = new int[]{
                R.drawable.iceland,
                R.drawable.dolomites,
                R.drawable.shannon,
                R.drawable.traveller};
        datasource = new JourneyDataSource(this);
        datasource.open();
        if(datasource.getAllJourneys().isEmpty()){
            Log.d("Insert: ", "Inserting ..");
            Bitmap icon1 = BitmapFactory.decodeResource(getResources(), covers[0]);
            Bitmap icon2 = BitmapFactory.decodeResource(getResources(), covers[1]);
            Bitmap icon3 = BitmapFactory.decodeResource(getResources(), covers[2]);
            Bitmap icon4 = BitmapFactory.decodeResource(getResources(), covers[3]);

            datasource.addJourney(new Journey("Journey to freeze world","tralalalal", 200,"Islandia", "geo:64.1354, -21.8954" , ImageUtility.getBytes(icon1)));
            datasource.addJourney(new Journey("Journey to sunny world","djaskdsja", 300,"Bulgaria", "geo:42.6975, 23.3241", ImageUtility.getBytes(icon2)));
            datasource.addJourney(new Journey("Journey to rainy world","sdfakljdfa", 200,"Anglia", "geo:51.5085, -0.1257", ImageUtility.getBytes(icon3)));
            datasource.addJourney(new Journey("Journey to pizza world","sdasdasda", 600,"Włochy", "geo:41.8919, 12.5113", ImageUtility.getBytes(icon4)));
        }
        journeysList.clear();
        journeysList.addAll(datasource.getAllJourneys());
        adapter.notifyDataSetChanged();
    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
