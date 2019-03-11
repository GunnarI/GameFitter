package com.appelicious.gunnzo.gamefit.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.appelicious.gunnzo.gamefit.GameDataListItem;
import com.appelicious.gunnzo.gamefit.R;

import java.util.ArrayList;

/**
 * Created by Gunnar on 25.2.2018.
 */

public class GamesAdapter extends RecyclerView.Adapter<GamesAdapter.ViewHolder> {
    private static final String TAG = GamesAdapter.class.getSimpleName();

    private ArrayList<GameDataListItem> mGameData;
    private OnLeaveGame mCallback;

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView gameName;
        final ImageView gameType;
        ViewHolder(View view) {
            super(view);
            gameName = view.findViewById(R.id.games_item_game_name);
            gameType = view.findViewById(R.id.games_item_sport_icon);
        }
    }

    @Override
    @NonNull
    public GamesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForReviewItem = R.layout.games_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForReviewItem, parent, false);

        mCallback = (OnLeaveGame) context;

        return new GamesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final GamesAdapter.ViewHolder holder, int position) {
        holder.gameName.setText(mGameData.get(position).getGameName());
        // TODO: Check the sport type and set the image according to that
        holder.gameType.setImageResource(R.mipmap.ic_basketball);

        holder.itemView.setOnLongClickListener((View view) -> {
            final int gameItemClicked = holder.getAdapterPosition();

            AlertDialog.Builder deleteAlert = new AlertDialog.Builder(view.getContext());
            deleteAlert.setTitle("Leave Group");
            deleteAlert.setPositiveButton("OK", (dialog, which) -> {
                mCallback.leaveGame(mGameData.get(gameItemClicked).getGameId());
                dialog.dismiss();
            });
            deleteAlert.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

            deleteAlert.show();

            return true;
        });
    }

    @Override
    public int getItemCount() {
        if (mGameData == null) {
            return 0;
        }
        return mGameData.size();
    }

    public void setGameData(ArrayList<GameDataListItem> gameDatas) {
        mGameData = new ArrayList<>(gameDatas);
        notifyDataSetChanged();
    }

    public interface OnLeaveGame {
        void leaveGame(String gameId);
    }
}
