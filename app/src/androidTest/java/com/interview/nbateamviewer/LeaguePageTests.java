package com.interview.nbateamviewer;

import android.os.SystemClock;

import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.interview.nbateamviewer.Utils.childAtIndex;

/**
 * UI tests.
 * Note these are online tests, requiring access to the data url: DataModel.DEFAULT_DATA_URL
 */
@RunWith(AndroidJUnit4.class)
public class LeaguePageTests {
    @Rule
    public ActivityTestRule<LeaguePageActivity> activityRule
            = new ActivityTestRule<>(LeaguePageActivity.class);

    @Test
    public void testSorting() {
        checkSortingIndicator(R.id.sortAscendingName);
        onView(childAtIndex(withId(R.id.teamRecyclerView), 0))
                .check(matches(hasDescendant(withText("Atlanta Hawks"))));
        onView(withId(R.id.teamNameLabel)).perform(click());
        checkSortingIndicator(R.id.sortDescendingName);
        onView(childAtIndex(withId(R.id.teamRecyclerView), 0))
                .check(matches(hasDescendant(withText("Washington Wizards"))));
        onView(withId(R.id.teamNameLabel)).perform(click());
        checkSortingIndicator(R.id.sortAscendingName);
        onView(childAtIndex(withId(R.id.teamRecyclerView), 0))
                .check(matches(hasDescendant(withText("Atlanta Hawks"))));

        DataModel dataModel = new DataModel(activityRule.getActivity());
        onView(withId(R.id.teamWinsLabel)).perform(click());
        checkSortingIndicator(R.id.sortDescendingWins);
        onView(childAtIndex(withId(R.id.teamRecyclerView), 0))
                .check(matches(hasDescendant(withText(Integer.toString(maxWins(dataModel))))));
        onView(withId(R.id.teamWinsLabel)).perform(click());
        checkSortingIndicator(R.id.sortAscendingWins);
        onView(childAtIndex(withId(R.id.teamRecyclerView), 0))
                .check(matches(hasDescendant(withText(Integer.toString(minWins(dataModel))))));
        onView(withId(R.id.teamWinsLabel)).perform(click());
        checkSortingIndicator(R.id.sortDescendingWins);
        onView(childAtIndex(withId(R.id.teamRecyclerView), 0))
                .check(matches(hasDescendant(withText(Integer.toString(maxWins(dataModel))))));

        onView(withId(R.id.teamLossesLabel)).perform(click());
        checkSortingIndicator(R.id.sortDescendingLosses);
        onView(childAtIndex(withId(R.id.teamRecyclerView), 0))
                .check(matches(hasDescendant(withText(Integer.toString(maxLosses(dataModel))))));
        onView(withId(R.id.teamLossesLabel)).perform(click());
        checkSortingIndicator(R.id.sortAscendingLosses);
        onView(childAtIndex(withId(R.id.teamRecyclerView), 0))
                .check(matches(hasDescendant(withText(Integer.toString(minLosses(dataModel))))));
        onView(withId(R.id.teamLossesLabel)).perform(click());
        checkSortingIndicator(R.id.sortDescendingLosses);
        onView(childAtIndex(withId(R.id.teamRecyclerView), 0))
                .check(matches(hasDescendant(withText(Integer.toString(maxLosses(dataModel))))));
    }

    @Test
    public void testClickTeamLaunchesTeamPage() {
        onView(childAtIndex(withId(R.id.teamRecyclerView), 0)).perform(click());
        onView(withId(R.id.teamPageNameLabel)).check(matches(isDisplayed()));
    }

    private void checkSortingIndicator(int visibleId) {
        onView(withId(visibleId)).check(matches(isDisplayed()));
        if (R.id.sortAscendingName != visibleId) {
            onView(withId(R.id.sortAscendingName)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        }
        if (R.id.sortDescendingName != visibleId) {
            onView(withId(R.id.sortDescendingName)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        }
        if (R.id.sortAscendingWins != visibleId) {
            onView(withId(R.id.sortAscendingWins)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        }
        if (R.id.sortDescendingWins != visibleId) {
            onView(withId(R.id.sortDescendingWins)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        }
        if (R.id.sortAscendingLosses != visibleId) {
            onView(withId(R.id.sortAscendingLosses)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        }
        if (R.id.sortDescendingLosses != visibleId) {
            onView(withId(R.id.sortDescendingLosses)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        }
    }

    private int maxWins(DataModel dataModel) {
        int maxWins = 0;
        for (Team team : dataModel.getData()) {
            if (team.getWins() > maxWins) {
                maxWins = team.getWins();
            }
        }
        return maxWins;
    }

    private int minWins(DataModel dataModel) {
        int minWins = Integer.MAX_VALUE;
        for (Team team : dataModel.getData()) {
            if (team.getWins() < minWins) {
                minWins = team.getWins();
            }
        }
        return minWins;
    }

    private int maxLosses(DataModel dataModel) {
        int maxLosses = 0;
        for (Team team : dataModel.getData()) {
            if (team.getLosses() > maxLosses) {
                maxLosses = team.getLosses();
            }
        }
        return maxLosses;
    }

    private int minLosses(DataModel dataModel) {
        int minLosses = Integer.MAX_VALUE;
        for (Team team : dataModel.getData()) {
            if (team.getLosses() < minLosses) {
                minLosses = team.getLosses();
            }
        }
        return minLosses;
    }
}
