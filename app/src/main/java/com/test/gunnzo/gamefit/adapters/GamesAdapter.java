package com.test.gunnzo.gamefit.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.gunnzo.gamefit.MainActivity;
import com.test.gunnzo.gamefit.R;
import com.test.gunnzo.gamefit.dataclasses.GameData;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Gunnar on 25.2.2018.
 */

public class GamesAdapter extends RecyclerView.Adapter<GamesAdapter.ViewHolder> {

    private ArrayList<GameData> mGameData;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView gameName;
        public final ImageView gameType;
        public ViewHolder(View view) {
            super(view);
            gameName = (TextView) view.findViewById(R.id.games_item_game_name);
            gameType = (ImageView) view.findViewById(R.id.games_item_sport_icon);
        }
    }

    @Override
    public GamesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForReviewItem = R.layout.games_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForReviewItem, parent, false);

        return new GamesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GamesAdapter.ViewHolder holder, int position) {
        holder.gameName.setText(mGameData.get(position).getGameName());
        // TODO: Check the sport type and set the image according to that
        holder.gameType.setImageResource(R.mipmap.ic_basketball);
    }

    @Override
    public int getItemCount() {
        if (mGameData == null) return 0;
        return mGameData.size();
    }

    public void setGameData(ArrayList<GameData> gameDatas) {
        mGameData = new ArrayList<>(gameDatas);
        notifyDataSetChanged();
    }
}
