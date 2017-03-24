package com.vpage.vcars.tools;

import com.vpage.vcars.R;

public class TabMessage {
    public static String get(int menuItemId, boolean isReselection) {
        String message = "Content for ";

        switch (menuItemId) {
            case R.id.tab_user:
                message += "User";
                break;
            case R.id.tab_home:
                message += "User";
                break;
            case R.id.tab_favorites:
                message += "Favorites";
                break;

            case R.id.tab_search:
                message += "Search";
                break;
            case R.id.tab_overflow:
                message += "Overflow";
                break;
        }

        if (isReselection) {
            message += " WAS RESELECTED! YAY!";
        }

        return message;
    }
}
