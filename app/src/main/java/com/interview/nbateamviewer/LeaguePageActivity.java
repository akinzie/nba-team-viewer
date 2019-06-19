package com.interview.nbateamviewer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

/**
 * This is the entry point of the app.  This activity shows a list of NBA teams along with their
 * wins and losses.
 */
public class LeaguePageActivity extends AppCompatActivity {

    SortOrder currentSortOrder = SortOrder.NAME_ASCENDING;
    TeamListRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.league_page);
        DataModel dataModel = new DataModel(this);
        dataModel.fetchData(this);
        RecyclerView recyclerView = findViewById(R.id.teamRecyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new TeamListRecyclerAdapter(this, dataModel);
        recyclerView.setAdapter(adapter);
    }

    /**
     * Respond to a press on the team name.  Default sort order is ascending (A to Z).  If sorting
     * was already done by name, reverse the direction.
     * @param v
     */
    public void onTeamNameLabelClicked(View v) {
        hideSortDirectionIcon();
        if (currentSortOrder == SortOrder.NAME_ASCENDING) {
            currentSortOrder = SortOrder.NAME_DESCENDING;
            findViewById(R.id.sortDescendingName).setVisibility(View.VISIBLE);
        } else {
            currentSortOrder = SortOrder.NAME_ASCENDING;
            findViewById(R.id.sortAscendingName).setVisibility(View.VISIBLE);
        }
        adapter.setSortOrder(currentSortOrder);
    }

    /**
     * Respond to a press on the wins header.  Default sort order is descending (most wins on top).
     * If sorting was already done by wins, reverse the direction.
     * @param v
     */
    public void onTeamWinsLabelClicked(View v) {
        hideSortDirectionIcon();
        if (currentSortOrder == SortOrder.WINS_DESCENDING) {
            currentSortOrder = SortOrder.WINS_ASCENDING;
            findViewById(R.id.sortAscendingWins).setVisibility(View.VISIBLE);
        } else {
            currentSortOrder = SortOrder.WINS_DESCENDING;
            findViewById(R.id.sortDescendingWins).setVisibility(View.VISIBLE);
        }
        adapter.setSortOrder(currentSortOrder);
    }

    /**
     * Respond to a press on the losses header.  Default sort order is descending (most losses on
     * top).  If sorting was already done by losses, reverse the direction.
     * @param v
     */
    public void onTeamLossesLabelClicked(View v) {
        hideSortDirectionIcon();
        if (currentSortOrder == SortOrder.LOSSES_DESCENDING) {
            currentSortOrder = SortOrder.LOSSES_ASCENDING;
            findViewById(R.id.sortAscendingLosses).setVisibility(View.VISIBLE);
        } else {
            currentSortOrder = SortOrder.LOSSES_DESCENDING;
            findViewById(R.id.sortDescendingLosses).setVisibility(View.VISIBLE);
        }
        adapter.setSortOrder(currentSortOrder);
    }

    /**
     * Helper to hide all sort direction arrows.
     */
    private void hideSortDirectionIcon() {
        findViewById(R.id.sortAscendingName).setVisibility(View.GONE);
        findViewById(R.id.sortDescendingName).setVisibility(View.GONE);
        findViewById(R.id.sortAscendingWins).setVisibility(View.GONE);
        findViewById(R.id.sortDescendingWins).setVisibility(View.GONE);
        findViewById(R.id.sortAscendingLosses).setVisibility(View.GONE);
        findViewById(R.id.sortDescendingLosses).setVisibility(View.GONE);
    }
}
