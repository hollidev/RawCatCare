package edu.lapinamk.holli.rawcatcare;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * PreferencesHelper contains a collection of helper methods for accessing the shared preferences' key-value-pairs
 */
public abstract class PreferencesHelper

{
    private static SharedPreferences sUserPref;

    private PreferencesHelper()
    {
    }

    public static void setNumberOfCats(Context c, int n)
    {
        sUserPref = getUserPref(c);

        SharedPreferences.Editor editor = sUserPref.edit();

        editor.putInt(c.getString(R.string.number_of_cats), n);
        editor.apply();
    }

    public static int getNumberOfCats(Context c)
    {
        sUserPref = getUserPref(c);

        int numberOfCats = sUserPref.getInt(c.getString(R.string.number_of_cats), 0);


        return numberOfCats;
    }

    private static SharedPreferences getUserPref(Context c)
    {
        sUserPref = c.getSharedPreferences(c.getString(R.string.catcare_user_preferences), Context.MODE_PRIVATE);

        return sUserPref;
    }


    public static void setCurrentPet(Context c, int id)
    {
        sUserPref = getUserPref(c);

        SharedPreferences.Editor editor = sUserPref.edit();

        editor.putInt(c.getString(R.string.current_pet), id);
        editor.apply();
    }

    public static int getCurrentPet(Context c)
    {
        sUserPref = getUserPref(c);

        int currentPet = sUserPref.getInt(c.getString(R.string.current_pet), 0);


        return currentPet;
    }

    public static void setCurrentMenu(Context c, int id)
    {
        sUserPref = getUserPref(c);
        SharedPreferences.Editor editor = sUserPref.edit();
        editor.putInt("current_menu", id);
        editor.apply();
    }

    public static int getCurrentMenu(Context c)
    {
        sUserPref = getUserPref(c);
        return sUserPref.getInt("current_menu", 0);
    }

    public static void setCurrentMeal(Context c, int id)
    {
        sUserPref = getUserPref(c);

        SharedPreferences.Editor editor = sUserPref.edit();

        editor.putInt("current_meal", id);
        editor.apply();
    }

    public static void setCurrentDay(Context context, String dayOfWeek)
    {
        sUserPref = getUserPref(context);

        SharedPreferences.Editor editor = sUserPref.edit();

        editor.putString("current_day_of_week", dayOfWeek);
        editor.apply();
    }

    public static String getCurrentDay(Context context)
    {
        sUserPref = getUserPref(context);

        String currentDay = sUserPref.getString("current_day_of_week", null);

        return currentDay;
    }

    public static void setCurrentDailyMenu(Context context, long dayOfWeek)
    {
        sUserPref = getUserPref(context);

        SharedPreferences.Editor editor = sUserPref.edit();

        editor.putLong("current_daily_menu", dayOfWeek);
        editor.apply();
    }

    public static long getCurrentDailyMenu(Context c)
    {
        sUserPref = getUserPref(c);

        long currentDailyMenu = sUserPref.getLong("current_daily_menu", 0);

        return currentDailyMenu;

    }

    public static int getCurrentMeal(Context c)
    {
        sUserPref = getUserPref(c);

        int currentMeal = sUserPref.getInt("current_meal", 0);

        return currentMeal;

    }

    public static int getWeekdayNumber(String day)
    {
        HashMap<String, Integer> week = new HashMap<>();

        week.put("Monday", 0);
        week.put("Tuesday", 1);
        week.put("Wednesday", 2);
        week.put("Thursday", 3);
        week.put("Friday", 4);
        week.put("Saturday", 5);
        week.put("Sunday", 6);

        return week.get(day);
    }
}


