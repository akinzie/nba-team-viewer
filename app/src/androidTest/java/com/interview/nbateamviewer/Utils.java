package com.interview.nbateamviewer;

import android.view.View;
import android.view.ViewGroup;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class Utils {
    public static TypeSafeMatcher<View> childAtIndex(Matcher<View> parentMatcher, int index) {
        return new TypeSafeMatcher<View>() {
            @Override
            protected boolean matchesSafely(View item) {
                if (parentMatcher.matches(item.getParent())) {
                    if (item.getParent() instanceof ViewGroup) {
                        ViewGroup viewGroup = (ViewGroup) item.getParent();
                        return item.equals(viewGroup.getChildAt(index));
                    }
                }
                return false;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("Is at " + index + " index");
            }
        };
    }
}
