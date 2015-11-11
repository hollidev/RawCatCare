package edu.lapinamk.holli.rawcatcare;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;

public class DbHelper extends SQLiteOpenHelper
{
    // Database version handling is done with this value. It must be incremented if the database schema is edited
    public static final int DATABASE_VERSION = 1;

    // name of the database
    public static final String DATABASE_NAME = "CatsDB";

    // names of the tables
    public static final String TABLE_CATS = "cats";
    public static final String TABLE_BREEDS = "breeds";
    public static final String TABLE_WEIGHTS = "weights";
    public static final String TABLE_MEALS = "meals";
    public static final String TABLE_WEEKLY_MENUS = "weeklymenus";
    public static final String TABLE_DAILY_MENUS = "dailymenus";

    // the nullable column
    public static final String COLUMN_NULLABLE = "nullable";

    // table creation Strings
    public static final String CREATE_TABLE_CATS =
            "CREATE TABLE " + TABLE_CATS + "(" +
                CatEntry.COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE," +
                CatEntry.COLUMN_NAME_NAME + " TEXT NOT NULL," +
                CatEntry.COLUMN_NAME_DOB + " NUMERIC NOT NULL," +
                CatEntry.COLUMN_NAME_WEIGHT + " NUMERIC NOT NULL," +
                CatEntry.COLUMN_NAME_SEX + " INTEGER NOT NULL," +
                CatEntry.COLUMN_NAME_NEUTERED_STATE + " INTEGER NOT NULL," +
                CatEntry.COLUMN_NAME_BREED + " TEXT NOT NULL," +
                CatEntry.COLUMN_NAME_AVATAR + " TEXT," +
                CatEntry.COLUMN_NAME_ACTIVITY + " INTEGER," +
                CatEntry.COLUMN_NAME_SHAPE + " INTEGER, " +
                CatEntry.COLUMN_NAME_NURSING_STATE + " INTEGER, " +
                COLUMN_NULLABLE + " NUMERIC)";

    public static final String CREATE_TABLE_WEIGHTS =
            "CREATE TABLE " + TABLE_WEIGHTS +
                WeightEntry.COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE," +
                WeightEntry.COLUMN_NAME_VALUE + " NUMERIC NOT NULL," +
                WeightEntry.COLUMN_NAME_DATE + " NUMERIC NOT NULL," +
                WeightEntry.COLUMN_NAME_CAT_FK + " INTEGER NOT NULL, " +
                COLUMN_NULLABLE + " NUMERIC," +
                "FOREIGN KEY(" + WeightEntry.COLUMN_NAME_CAT_FK + ") REFERENCES " + TABLE_CATS + " (" + CatEntry._ID + ") ON DELETE CASCADE)";

    public static final String CREATE_TABLE_MEALS =
            "CREATE TABLE " + TABLE_MEALS + "(" +
                MealEntry.COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE," +
                MealEntry.COLUMN_NAME_NAME + " TEXT," +
                MealEntry.COLUMN_NAME_MEAT + " TEXT NOT NULL," +
                MealEntry.COLUMN_NAME_PART + " TEXT NOT NULL," +
                MealEntry.COLUMN_NAME_AMOUNT + " NUMERIC NOT NULL," +
                MealEntry.COLUMN_NAME_FEEDING_TIME + " TEXT," +
                MealEntry.COLUMN_NAME_WEEKLY_MENU_FK + " INTEGER NOT NULL, " +
                MealEntry.COLUMN_NAME_DAY_FK + " TEXT NOT NULL, " +
                COLUMN_NULLABLE + " NUMERIC," +
                " FOREIGN KEY(" + MealEntry.COLUMN_NAME_DAY_FK + ") REFERENCES " + TABLE_DAILY_MENUS + " (" + DailyMenuEntry.COLUMN_NAME_DAY + ") ON DELETE CASCADE," +
                " FOREIGN KEY(" + MealEntry.COLUMN_NAME_WEEKLY_MENU_FK + ") REFERENCES " + TABLE_WEEKLY_MENUS + " (" + WeeklyMenuEntry._ID + ") " + " ON DELETE CASCADE)";

    public static final String CREATE_TABLE_DAILY_MENUS =
            "CREATE TABLE " + TABLE_DAILY_MENUS + "(" +
                DailyMenuEntry.COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE," +
                DailyMenuEntry.COLUMN_NAME_DAY + " TEXT NOT NULL," +
                DailyMenuEntry.COLUMN_NAME_WEEKLY_MENU_FK + " INTEGER NOT NULL," +
                COLUMN_NULLABLE + " NUMERIC," + "FOREIGN KEY(" +
                DailyMenuEntry.COLUMN_NAME_WEEKLY_MENU_FK + ") REFERENCES " + TABLE_WEEKLY_MENUS + " (" + WeeklyMenuEntry._ID + ") " + " ON DELETE CASCADE)";

    public static final String CREATE_TABLE_WEEKLY_MENUS =
            "CREATE TABLE " + TABLE_WEEKLY_MENUS + "(" +
                WeeklyMenuEntry.COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE," +
                WeeklyMenuEntry.COLUMN_NAME_NAME + " TEXT," +
                WeeklyMenuEntry.COLUMN_NAME_CAT_FK + " INTEGER NOT NULL," +
                COLUMN_NULLABLE + " NUMERIC," +
                "FOREIGN KEY(" + WeeklyMenuEntry.COLUMN_NAME_CAT_FK + ") REFERENCES " + TABLE_CATS + " (" + CatEntry._ID + ") ON DELETE CASCADE)";

    public DbHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {

        db.execSQL(CREATE_TABLE_CATS);
        db.execSQL(CREATE_TABLE_WEEKLY_MENUS);
        db.execSQL(CREATE_TABLE_DAILY_MENUS);
        db.execSQL(CREATE_TABLE_MEALS);

    }

    public long addCatToDb(Cat cat)
    {
        long newRowId;

        try
        {
            // create a new map of values where column names are the keys
            ContentValues values = new ContentValues();
            values.put(CatEntry.COLUMN_NAME_NAME, cat.getName());
            values.put(CatEntry.COLUMN_NAME_BREED, cat.getBreed());
            values.put(CatEntry.COLUMN_NAME_DOB, cat.getDateOfBirth().getTime());
            values.put(CatEntry.COLUMN_NAME_WEIGHT, cat.getWeight());
            values.put(CatEntry.COLUMN_NAME_SEX, cat.getGender());
            values.put(CatEntry.COLUMN_NAME_AVATAR, cat.getAvatarPath());
            values.put(CatEntry.COLUMN_NAME_ACTIVITY, cat.getActivityLevel());
            values.put(CatEntry.COLUMN_NAME_NEUTERED_STATE, cat.getNeuteredState());
            values.put(CatEntry.COLUMN_NAME_SHAPE, cat.getShape());
            values.put(CatEntry.COLUMN_NAME_NURSING_STATE, cat.getNursingState());

            // Insert the new row, returning the primary key value of the new row

            newRowId = getWritableDatabase().insert(DbHelper.TABLE_CATS, DbHelper.COLUMN_NULLABLE, values);


        } catch (Exception e)
        {
            newRowId = 0;
        }

        getWritableDatabase().close();

        return newRowId;
    }

    public long addWeeklyMenuToDb(String name, int catId)
    {
        ContentValues values = new ContentValues();
        values.put(WeeklyMenuEntry.COLUMN_NAME_NAME, name);
        values.put(WeeklyMenuEntry.COLUMN_NAME_CAT_FK, catId);

        long newRowId;
        newRowId = getWritableDatabase().insert(TABLE_WEEKLY_MENUS, COLUMN_NULLABLE, values);

        getWritableDatabase().close();

        return newRowId;
    }

    @Deprecated
    public long addDailyMenuToWeeklyMenu(int weeklyMenuId, String day)
    {
        ContentValues values = new ContentValues();
        values.put(DailyMenuEntry.COLUMN_NAME_DAY, day);
        values.put(DailyMenuEntry.COLUMN_NAME_WEEKLY_MENU_FK, weeklyMenuId);

        Log.d("weekly menu fk", String.valueOf(weeklyMenuId));


        long newRowId;

        try
        {
            newRowId = getWritableDatabase().insertOrThrow(TABLE_DAILY_MENUS, COLUMN_NULLABLE, values);
        } catch (Exception e)
        {
            Log.e("Catch block dailymenu", e.getMessage());
            newRowId = 0;
        }

        getWritableDatabase().close();

        return newRowId;
    }

    public void addMealToDailyMenu(Meal meal)
    {
        ContentValues values = new ContentValues();
        values.put(MealEntry.COLUMN_NAME_NAME, meal.getName());
        values.put(MealEntry.COLUMN_NAME_MEAT, meal.getType());
        values.put(MealEntry.COLUMN_NAME_PART, meal.getPart());
        values.put(MealEntry.COLUMN_NAME_AMOUNT, meal.getAmount());
        values.put(MealEntry.COLUMN_NAME_FEEDING_TIME, meal.getFeedingTime());
        values.put(MealEntry.COLUMN_NAME_DAY_FK, meal.getDayOfWeek());
        values.put(MealEntry.COLUMN_NAME_WEEKLY_MENU_FK, meal.getWeekId());

        long newRowId;
        newRowId = getWritableDatabase().insert(TABLE_MEALS, COLUMN_NULLABLE, values);


        getWritableDatabase().close();
    }

    public ArrayList<Integer> selectDailyMenus(int menuId)
    {

        ArrayList<Integer> dailyMenuIds = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_DAILY_MENUS + " WHERE " + DailyMenuEntry.COLUMN_NAME_WEEKLY_MENU_FK + " = " + menuId;
        //



        Cursor cursor = getReadableDatabase().rawQuery(selectQuery, null);

        if (cursor != null && cursor.moveToFirst())
        {
            for (int i = 0; i < cursor.getCount(); i++)
            {
                dailyMenuIds.add(cursor.getInt(cursor.getColumnIndex("_id")));

                cursor.moveToNext();
            }

            cursor.close();
        }

        getReadableDatabase().close();

        return dailyMenuIds;
    }


    public Boolean weeklyMenuExists(int menuId)
    {
        String selectMenuQuery;
        Boolean menuExists;

        selectMenuQuery = "SELECT * FROM " + TABLE_WEEKLY_MENUS + " WHERE " + WeeklyMenuEntry.COLUMN_NAME_ID + " = " + menuId;

        Cursor cursor = getReadableDatabase().rawQuery(selectMenuQuery, null);

        menuExists = cursor.getCount() != 0;

        cursor.close();
        getReadableDatabase().close();

        return menuExists;

    }

    @Deprecated
    public ArrayList<Meal> selectWeeklyMeals(int menuId, ArrayList<Meal> mealList)
    {
        String selectMealsQuery;
        Cursor cursor;
        String meat;
        String part;
        float amount;
        String feedingTime;
        int id;
        String day;

        ArrayList<Integer> dailyMenus = selectDailyMenus(menuId);

        for (int i = 0; i < dailyMenus.size(); i++)
        {
            selectMealsQuery = "SELECT * FROM " + TABLE_MEALS + " WHERE " + MealEntry.COLUMN_NAME_DAY_FK + " = " + dailyMenus.get(i);

            cursor = getReadableDatabase().rawQuery(selectMealsQuery, null);

            if (cursor != null && cursor.moveToFirst())
            {
                for (int j = 0; j < cursor.getCount(); j++)
                {
                    id = cursor.getInt(cursor.getColumnIndex(MealEntry.COLUMN_NAME_ID));
                    meat = cursor.getString(cursor.getColumnIndex(MealEntry.COLUMN_NAME_MEAT));
                    part = cursor.getString(cursor.getColumnIndex(MealEntry.COLUMN_NAME_PART));
                    amount = cursor.getFloat(cursor.getColumnIndex(MealEntry.COLUMN_NAME_AMOUNT));
                    feedingTime = cursor.getString(cursor.getColumnIndex(MealEntry.COLUMN_NAME_FEEDING_TIME));
                    day = cursor.getString(cursor.getColumnIndex(MealEntry.COLUMN_NAME_DAY_FK));

                    mealList.add(new Meal(id, meat, part, amount, feedingTime, day));

                    cursor.moveToNext();
                }


                cursor.close();
            }


        }

        getReadableDatabase().close();

        return mealList;
    }

    public ArrayList<Meal> selectWeeklyMealsNew(int menuId, ArrayList<Meal> mealList)
    {
        String selectMealsQuery;
        Cursor cursor;
        String meat;
        String part;
        float amount;
        String feedingTime;
        int id;
        String day;


        selectMealsQuery = "SELECT * FROM " + TABLE_MEALS + " WHERE " + MealEntry.COLUMN_NAME_WEEKLY_MENU_FK + " = " + menuId;

        cursor = getReadableDatabase().rawQuery(selectMealsQuery, null);

        if (cursor != null && cursor.moveToFirst())
        {
            for (int j = 0; j < cursor.getCount(); j++)
            {
                id = cursor.getInt(cursor.getColumnIndex(MealEntry.COLUMN_NAME_ID));
                meat = cursor.getString(cursor.getColumnIndex(MealEntry.COLUMN_NAME_MEAT));
                part = cursor.getString(cursor.getColumnIndex(MealEntry.COLUMN_NAME_PART));
                amount = cursor.getFloat(cursor.getColumnIndex(MealEntry.COLUMN_NAME_AMOUNT));
                feedingTime = cursor.getString(cursor.getColumnIndex(MealEntry.COLUMN_NAME_FEEDING_TIME));
                day = cursor.getString(cursor.getColumnIndex(MealEntry.COLUMN_NAME_DAY_FK));

                mealList.add(new Meal(id, meat, part, amount, feedingTime, day));

                cursor.moveToNext();
            }


            cursor.close();
        }


        getReadableDatabase().close();

        return mealList;
    }

    public ArrayList<Meal> selectMealsOfDay(int menuId, String day)
    {
        ArrayList<Meal> mealList = new ArrayList<>();

        String selectMealsQuery;
        Cursor cursor;
        String meat;
        String part;
        String feedingTime;
        float amount;
        int id;

        selectMealsQuery = "SELECT * FROM " + TABLE_MEALS + " WHERE " + MealEntry.COLUMN_NAME_DAY_FK + " = " + "'" + day + "'" + " AND " + MealEntry.COLUMN_NAME_WEEKLY_MENU_FK + " = " + menuId;

        cursor = getReadableDatabase().rawQuery(selectMealsQuery, null);

        if (cursor != null && cursor.moveToFirst())
        {
            for (int j = 0; j < cursor.getCount(); j++)
            {
                id = cursor.getInt(cursor.getColumnIndex(MealEntry.COLUMN_NAME_ID));
                meat = cursor.getString(cursor.getColumnIndex(MealEntry.COLUMN_NAME_MEAT));
                part = cursor.getString(cursor.getColumnIndex(MealEntry.COLUMN_NAME_PART));
                amount = cursor.getFloat(cursor.getColumnIndex(MealEntry.COLUMN_NAME_AMOUNT));
                day = cursor.getString(cursor.getColumnIndex(MealEntry.COLUMN_NAME_DAY_FK));
                feedingTime = cursor.getString(cursor.getColumnIndex(MealEntry.COLUMN_NAME_FEEDING_TIME));

                mealList.add(new Meal(id, meat, part, amount, feedingTime, day));

                cursor.moveToNext();
            }


            cursor.close();
        }

        getReadableDatabase().close();

        return mealList;
    }

    public Cat getCurrentCatInfo(int catId)
    {
        Cat currentCat = null;
        String selectQuery = "SELECT " + " " + " * FROM " + TABLE_CATS + " WHERE " + CatEntry.COLUMN_NAME_ID + " = " + catId;

        Cursor cursor = getReadableDatabase().rawQuery(selectQuery, null);

        if (cursor != null && cursor.moveToFirst())
        {
            int id = cursor.getInt(cursor.getColumnIndex(CatEntry.COLUMN_NAME_ID));
            float weight = cursor.getFloat(cursor.getColumnIndex(CatEntry.COLUMN_NAME_WEIGHT));
            long dob = cursor.getLong(cursor.getColumnIndex(CatEntry.COLUMN_NAME_DOB));
            int shape = cursor.getInt(cursor.getColumnIndex(CatEntry.COLUMN_NAME_SHAPE));
            int activityLevel = cursor.getInt(cursor.getColumnIndex(CatEntry.COLUMN_NAME_ACTIVITY));
            String breed = cursor.getString(cursor.getColumnIndex(CatEntry.COLUMN_NAME_BREED));
            String name = cursor.getString(cursor.getColumnIndex(CatEntry.COLUMN_NAME_NAME));
            int gender = cursor.getInt(cursor.getColumnIndex(CatEntry.COLUMN_NAME_SEX));
            String avatarPath = cursor.getString(cursor.getColumnIndex(CatEntry.COLUMN_NAME_AVATAR));
            int neuteredState = cursor.getInt(cursor.getColumnIndex(CatEntry.COLUMN_NAME_NEUTERED_STATE));
            int nursingState = cursor.getInt(cursor.getColumnIndex(CatEntry.COLUMN_NAME_NURSING_STATE));

            currentCat = new Cat(name, weight, new Date(dob), shape, activityLevel, gender, neuteredState, nursingState, breed, avatarPath);

        }

        cursor.close();
        getReadableDatabase().close();

        return currentCat;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // todo complete before release
    }

    public void deleteCatFromDb(int catId)
    {

        String deleteCatQuery = "DELETE FROM " + TABLE_CATS + " WHERE " + CatEntry.COLUMN_NAME_ID + " = " + catId;

        try
        {
            getWritableDatabase().execSQL(deleteCatQuery);
        } catch (SQLiteException e)
        {
            e.printStackTrace();
        }

        getWritableDatabase().close();
    }

    public void deleteWeeklyMenu(int weeklyMenuId)
    {

        String deleteMenuQuery = "DELETE FROM " + TABLE_WEEKLY_MENUS + " WHERE " + WeeklyMenuEntry.COLUMN_NAME_ID + " = " + weeklyMenuId;

        getWritableDatabase().execSQL(deleteMenuQuery);

        getWritableDatabase().close();
    }

    public void deleteMeal(int mealId)
    {

        String deleteMealQuery = "DELETE FROM " + TABLE_MEALS + " WHERE " + MealEntry.COLUMN_NAME_ID + " = " + mealId;

        getWritableDatabase().execSQL(deleteMealQuery);

        getWritableDatabase().close();
    }

    public ArrayList<Cat> selectCats()
    {
        String selectCatsQuery = "SELECT * FROM cats";

        ArrayList<Cat> cats = new ArrayList<>();

        Cursor cursor = getReadableDatabase().rawQuery(selectCatsQuery, null);

        if (cursor != null && cursor.moveToFirst())
        {
            for (int i = 0; i < cursor.getCount(); i++)
            {
                String name = cursor.getString(cursor.getColumnIndex(CatEntry.COLUMN_NAME_NAME));
                int id = cursor.getInt(cursor.getColumnIndex(CatEntry.COLUMN_NAME_ID));
                String avatarPath = cursor.getString(cursor.getColumnIndex(CatEntry.COLUMN_NAME_AVATAR));


                Cat cat = new Cat(id, name, avatarPath);
                cats.add(cat);

                cursor.moveToNext();
            }


            cursor.close();
        }

        getReadableDatabase().close();


        return cats;
    }

    public ArrayList<WeeklyMenu> selectWeeklyMenus(int currentPet)
    {
        ArrayList<WeeklyMenu> menus = new ArrayList<>();
        String selectMenusQuery = "SELECT * FROM " + DbHelper.TABLE_WEEKLY_MENUS + " WHERE " + DbHelper.WeeklyMenuEntry.COLUMN_NAME_CAT_FK + " = " + currentPet;

        Cursor cursor = getReadableDatabase().rawQuery(selectMenusQuery, null);

        if (cursor != null && cursor.moveToFirst())
        {
            for (int i = 0; i < cursor.getCount(); i++)
            {
                int menuId = cursor.getInt(cursor.getColumnIndex(WeeklyMenuEntry.COLUMN_NAME_ID));
                String menuName = cursor.getString(cursor.getColumnIndex(WeeklyMenuEntry.COLUMN_NAME_NAME));


                WeeklyMenu menu = new WeeklyMenu(menuId, menuName);

                menus.add(menu);

                cursor.moveToNext();
            }


            cursor.close();
        }

        getReadableDatabase().close();

        return menus;

    }

    public void updateCatInfo(Cat cat, int id)
    {
        String updateQuery = "UPDATE " + TABLE_CATS + " SET " +
                CatEntry.COLUMN_NAME_NEUTERED_STATE + " = " + cat.getNeuteredState() + ", " +
                CatEntry.COLUMN_NAME_ACTIVITY + " = " + cat.getActivityLevel() + ", " +
                CatEntry.COLUMN_NAME_NURSING_STATE + " = " + cat.getNursingState() + ", " +
                CatEntry.COLUMN_NAME_WEIGHT + " = " + cat.getWeight() +
                " WHERE " + CatEntry.COLUMN_NAME_ID + " = " + id;

        getWritableDatabase().execSQL(updateQuery);


    }

    public void updateMealInfo(Meal meal)
    {

        String updateQuery = "UPDATE " + TABLE_MEALS + " SET " +
                MealEntry.COLUMN_NAME_AMOUNT + " = " + meal.getAmount() + ", " +
                MealEntry.COLUMN_NAME_MEAT + " = '" + meal.getType() + "' , " +
                MealEntry.COLUMN_NAME_PART + " = '" + meal.getPart() + "' , " +
                MealEntry.COLUMN_NAME_FEEDING_TIME + " = '" + meal.getFeedingTime() +
                "' WHERE " + MealEntry.COLUMN_NAME_ID + " = " + meal.getId();

        getWritableDatabase().execSQL(updateQuery);


    }


    public Meal getCurrentMealInfo(int id)
    {
        Meal currentMeal = null;
        String selectQuery = "SELECT " + " " + " * FROM " + TABLE_MEALS + " WHERE " + MealEntry.COLUMN_NAME_ID + " = " + id;
        String meat;
        String part;
        float amount;
        String feedingTime;
        String day;


        Cursor cursor = getReadableDatabase().rawQuery(selectQuery, null);

        if (cursor != null && cursor.moveToFirst())
        {
            id = cursor.getInt(cursor.getColumnIndex(MealEntry.COLUMN_NAME_ID));
            meat = cursor.getString(cursor.getColumnIndex(MealEntry.COLUMN_NAME_MEAT));
            part = cursor.getString(cursor.getColumnIndex(MealEntry.COLUMN_NAME_PART));
            amount = cursor.getFloat(cursor.getColumnIndex(MealEntry.COLUMN_NAME_AMOUNT));
            feedingTime = cursor.getString(cursor.getColumnIndex(MealEntry.COLUMN_NAME_FEEDING_TIME));
            day = cursor.getString(cursor.getColumnIndex(MealEntry.COLUMN_NAME_DAY_FK));

            currentMeal = new Meal(id, meat, part, amount, feedingTime, day);
        }

        cursor.close();
        getReadableDatabase().close();

        return currentMeal;
    }


    public static abstract class CatEntry implements BaseColumns
    {
        public static final String COLUMN_NAME_ID = "_id";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_DOB = "date_of_birth";
        public static final String COLUMN_NAME_BREED = "breed";
        public static final String COLUMN_NAME_WEIGHT = "weight";
        public static final String COLUMN_NAME_SEX = "sex";
        public static final String COLUMN_NAME_NEUTERED_STATE = "neutered";
        public static final String COLUMN_NAME_AVATAR = "avatar";
        public static final String COLUMN_NAME_ACTIVITY = "activity";
        public static final String COLUMN_NAME_SHAPE = "shape";
        public static final String COLUMN_NAME_NURSING_STATE = "nursing_state";
    }

    public static abstract class WeeklyMenuEntry implements BaseColumns
    {
        public static final String COLUMN_NAME_ID = "_id";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_CAT_FK = "cat_id";
    }

    public static abstract class DailyMenuEntry implements BaseColumns
    {
        public static final String COLUMN_NAME_ID = "_id";
        public static final String COLUMN_NAME_DAY = "day_of_week";
        public static final String COLUMN_NAME_WEEKLY_MENU_FK = "weekly_menu_id";
    }

    public static abstract class MealEntry implements BaseColumns
    {
        public static final String COLUMN_NAME_ID = "_id";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_MEAT = "meat";
        public static final String COLUMN_NAME_PART = "part";
        public static final String COLUMN_NAME_AMOUNT = "amount";
        public static final String COLUMN_NAME_FEEDING_TIME = "feeding_time";
        public static final String COLUMN_NAME_DAY_FK = "day_of_week";
        public static final String COLUMN_NAME_WEEKLY_MENU_FK = "weekly_menu_id";

    }

    public static abstract class WeightEntry implements BaseColumns
    {
        public static final String COLUMN_NAME_ID = "_id";
        public static final String COLUMN_NAME_VALUE = "value";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_CAT_FK = "cat_id";
    }
}
