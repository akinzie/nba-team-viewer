package com.interview.nbateamviewer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.Objects;

/**
 * This class just holds information about a team.
 */
public class Team implements Serializable {
    private int id;
    private String full_name;
    private int wins;
    private int losses;
    private Player[] players;

    public Team() {}

    public Team(int id, @NonNull String full_name, int wins, int losses, @NonNull Player[] players) {
        Objects.requireNonNull(full_name);
        Objects.requireNonNull(players);
        this.id = id;
        this.full_name = full_name;
        this.wins = wins;
        this.losses = losses;
        this.players = players;
    }

    public int getId() {
        return id;
    }

    @Nullable
    public String getFullName() {
        return full_name;
    }

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }

    @Nullable
    public Player[] getPlayers() {
        return players;
    }
}
