package edu.lapinamk.holli.rawcatcare;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.HashMap;


public class NutritionReviewActivity extends ActionBarActivity
{
    private int mCurrentMenu;
    private int mCurrentPet;
    private HashMap<String, Double> mNeeds;
    private HashMap<String, Double> mWeeklyNutrients;
    private DbHelper helper;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_nutrition_review);

        mCurrentMenu = PreferencesHelper.getCurrentMenu(this);
        mCurrentPet = PreferencesHelper.getCurrentPet(this);

        getWeeklyNutrition();

        helper = new DbHelper(this);

        Cat cat = helper.getCurrentCatInfo(mCurrentPet);
        mNeeds = cat.getWeeklyNeeds();

        ChartBuilder chartBuilder = new ChartBuilder(this);

        LinearLayout chartContainer = (LinearLayout) findViewById(R.id.chartContainer);
        ScrollView chartScroller = (ScrollView) findViewById(R.id.chartScrollView);

        chartBuilder.build(mNeeds, mWeeklyNutrients, chartContainer);
        chartScroller.invalidate();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_menu_nutrition_review, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getWeeklyNutrition()
    {

        ArrayList<Meal> meals = new ArrayList<>();
        helper = new DbHelper(getApplicationContext());

        // select the daily meals contained in the weekly menu
        meals = helper.selectWeeklyMealsNew(mCurrentMenu, meals);


        mWeeklyNutrients = NutritionHandler.sumWeeklyNutrients(getApplicationContext(), meals);


    }
}
