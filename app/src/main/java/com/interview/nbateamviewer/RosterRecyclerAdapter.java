package com.interview.nbateamviewer;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;

public class RosterRecyclerAdapter extends RecyclerView.Adapter {
    private Player[] players;

    public RosterRecyclerAdapter(@NonNull Player[] players) {
        Objects.requireNonNull(players);
        this.players = players;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout rosterView = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.roster_view, parent, false);
        return new RosterViewHolder(rosterView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        RosterViewHolder rosterViewHolder = (RosterViewHolder) holder;
        LinearLayout rosterView = rosterViewHolder.rosterView;
        Player player = players[position];
        ((TextView)rosterView.findViewById(R.id.playerFirstName)).setText(player.getFirstName());
        ((TextView)rosterView.findViewById(R.id.playerLastName)).setText(player.getLastName());
        ((TextView)rosterView.findViewById(R.id.playerPosition)).setText(player.getPosition());
        ((TextView)rosterView.findViewById(R.id.playerNumber)).setText(Integer.toString(player.getNumber()));
    }

    @Override
    public int getItemCount() {
        return players.length;
    }
}
