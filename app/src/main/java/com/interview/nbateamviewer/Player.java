package com.interview.nbateamviewer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.Objects;

/**
 * This class just holds information about a player.
 */
public class Player implements Serializable {
    private int id;
    private String first_name;
    private String last_name;
    private String position;
    private int number;

    public Player() {}

    public Player(int id, @NonNull String first_name, @NonNull String last_name, @NonNull String position, int number) {
        Objects.requireNonNull(first_name);
        Objects.requireNonNull(last_name);
        Objects.requireNonNull(position);
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.position = position;
        this.number = number;
    }

    public int getId() {
        return id;
    }

    @Nullable
    public String getFirstName() {
        return first_name;
    }

    @Nullable
    public String getLastName() {
        return last_name;
    }

    @Nullable
    public String getPosition() {
        return position;
    }

    public int getNumber() {
        return number;
    }
}
