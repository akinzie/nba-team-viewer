package com.interview.nbateamviewer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TeamPageActivity extends Activity {
    private static final String LOGGER_TAG = "TeamViewer:TeamPageAct";
    private static final String TEAM_EXTRAS_KEY = "team";
    Team team;
    RosterRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.team_page);
        team = (Team) getIntent().getExtras().getSerializable(TEAM_EXTRAS_KEY);
        if (team == null) {
            Log.e(LOGGER_TAG, "TeamPageActivity created without a team.");
            return;
        }
        ((TextView)findViewById(R.id.teamPageNameLabel)).setText(team.getFullName());
        ((TextView)findViewById(R.id.teamPageWins)).setText(Integer.toString(team.getWins()));
        ((TextView)findViewById(R.id.teamPageLosses)).setText(Integer.toString(team.getLosses()));
        RecyclerView recyclerView = findViewById(R.id.teamPageRosterRecycler);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RosterRecyclerAdapter(team.getPlayers());
        recyclerView.setAdapter(adapter);
    }

    @NonNull
    public static Intent getStartIntent(@NonNull Activity activity, @NonNull Team team) {
        Intent intent = new Intent(activity, TeamPageActivity.class);
        intent.putExtra(TEAM_EXTRAS_KEY, team);
        return intent;
    }
}
