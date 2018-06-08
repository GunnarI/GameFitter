package com.test.gunnzo.gamefit.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.gunnzo.gamefit.GameDataList;
import com.test.gunnzo.gamefit.MainActivity;
import com.test.gunnzo.gamefit.R;
import com.test.gunnzo.gamefit.dataclasses.GameData;

import java.util.ArrayList;

/**
 * Created by Gunnar on 25.2.2018.
 */

public class GamesAdapter extends RecyclerView.Adapter<GamesAdapter.ViewHolder> {
    private static final String TAG = GamesAdapter.class.getSimpleName();

    private ArrayList<GameDataList> mGameData;
    private OnLeaveGame mCallback;

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

        mCallback = (OnLeaveGame) context;

        return new GamesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final GamesAdapter.ViewHolder holder, int position) {
        holder.gameName.setText(mGameData.get(position).getGameName());
        // TODO: Check the sport type and set the image according to that
        holder.gameType.setImageResource(R.mipmap.ic_basketball);

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View view) {
                //Toast.makeText(view.getContext(), "Long click " + position, Toast.LENGTH_SHORT).show();
                final int gameItemClicked = holder.getAdapterPosition();

                AlertDialog.Builder deleteAlert = new AlertDialog.Builder(view.getContext());
                deleteAlert.setTitle("Leave Group");
                deleteAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Toast.makeText(view.getContext(), "Item " + holder.getAdapterPosition() + " deleted", Toast.LENGTH_SHORT).show();
                        //Log.i(TAG, Integer.toString(gameItemClicked));
                        mCallback.leaveGame(mGameData.get(gameItemClicked).getGameId());
                        dialogInterface.dismiss();
                    }
                });
                deleteAlert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                deleteAlert.show();

                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mGameData == null) return 0;
        return mGameData.size();
    }

    public void setGameData(ArrayList<GameDataList> gameDatas) {
        mGameData = new ArrayList<>(gameDatas);
        notifyDataSetChanged();
    }

    public interface OnLeaveGame {
        void leaveGame(String gameId);
    }
}
