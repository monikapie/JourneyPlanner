package com.example.pietyszukm.journeyplanner;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by pietyszukm on 14.01.2017.
 */

public class JourneysAdapter extends RecyclerView.Adapter<JourneysAdapter.MyViewHolder> {

    private Context mContext;
    private List<Journey> journeyList;


    public JourneysAdapter(Context mContext, List<Journey> journeyList) {
        this.mContext = mContext;
        this.journeyList = journeyList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, count, overflow;
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            count = (TextView) view.findViewById(R.id.count);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            overflow = (TextView) view.findViewById(R.id.overflow);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.journey_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Journey journey = journeyList.get(position);
        holder.title.setText(journey.getName());
        holder.count.setText(journey.getCost() + " zł");

        Glide.with(mContext).load(journey.getThumbnail()).into(holder.thumbnail);
        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.overflow, position);
            }
        });
        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent= new Intent(mContext, DetailsActivity.class);
                intent.putExtra("id", position);
                mContext.startActivity(intent);
            }
        });
    }

    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view, int position) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_journey, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener(position));
        popup.show();
    }

    /**
     * Click listener for popup menu items
     *      */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        private int position;

        public MyMenuItemClickListener(int position) {
            this.position = position;
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            JourneyDataSource journeyDataSource = new JourneyDataSource(mContext);
            int itemId = menuItem.getItemId();
            switch (itemId) {
                case R.id.action_delete:
                    journeyDataSource.open();
                    Journey journey = journeyList.get(position);
                    journeyDataSource.deleteJourney(journey.getId());
                    journeyList.remove(position);
                    notifyDataSetChanged();
                    return true;
                case R.id.action_update:
                    Intent intent = new Intent(mContext, CreateNewJourneyActivity.class);
                    intent.putExtra("journey_id", journeyList.get(position).getId());
                    intent.putExtra("journey_image", journeyList.get(position).getThumbnail());
                    intent.putExtra("journey_name", journeyList.get(position).getName());
                    intent.putExtra("journey_description", journeyList.get(position).getDescription());
                    intent.putExtra("journey_cost", journeyList.get(position).getCost());
                    intent.putExtra("journey_country", journeyList.get(position).getCountry());
                    intent.putExtra("journey_uri", journeyList.get(position).getUri());
                    mContext.startActivity(intent);
                default:
            }
            return false;
        }
    }

    @Override
    public int getItemCount() {
       return journeyList.size();
    }
}