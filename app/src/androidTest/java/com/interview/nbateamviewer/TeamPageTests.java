package com.interview.nbateamviewer;

import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.interview.nbateamviewer.Utils.childAtIndex;
import static junit.framework.TestCase.assertTrue;

/**
 * UI tests.
 * Note these are online tests, requiring access to the data url: DataModel.DEFAULT_DATA_URL
 */
@RunWith(AndroidJUnit4.class)
public class TeamPageTests {
    @Rule
    public ActivityTestRule<LeaguePageActivity> activityRule
            = new ActivityTestRule<>(LeaguePageActivity.class);

    @Test
    public void testShowsTeamInfo() {
        onView(childAtIndex(withId(R.id.teamRecyclerView), 0)).check(matches(isDisplayed()));
        RecyclerView teamListRecyclerView = activityRule.getActivity().findViewById(R.id.teamRecyclerView);
        LinearLayout linearLayout = (LinearLayout) teamListRecyclerView.getChildAt(0);
        TextView teamName = linearLayout.findViewById(R.id.teamListNameLabel);
        TextView teamWins = linearLayout.findViewById(R.id.teamListWins);
        TextView teamLosses = linearLayout.findViewById(R.id.teamListLosses);
        onView(childAtIndex(withId(R.id.teamRecyclerView), 0)).perform(click());
        onView(withId(R.id.teamPageNameLabel)).check(matches(isDisplayed()));
        onView(withId(R.id.teamPageNameLabel)).check(matches(withText(teamName.getText().toString())));
        onView(withId(R.id.teamPageWins)).check(matches(withText(teamWins.getText().toString())));
        onView(withId(R.id.teamPageLosses)).check(matches(withText(teamLosses.getText().toString())));
        onView(childAtIndex(withId(R.id.teamPageRosterRecycler), 0)).check(matches(isDisplayed()));
    }
}
