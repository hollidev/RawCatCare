package edu.lapinamk.holli.rawcatcare;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/*
This activity represents the view where the user can see the meals they've added to the menu
 */

public class ViewWeeklyMealsActivity extends ActionBarActivity implements SlidingDayTabsFragment.OnDaySelectedListener, ConfirmDeleteDialogFragment.DeleteDialogListener, View.OnClickListener
{
    private int mCurrentMenu;
    private DbHelper mHelper;
    private ArrayList<Meal> mMeals;
    private String mDayOfWeek = "Monday";
    private LinearLayout mMealTab;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_weekly_meals);

        PreferencesHelper.setCurrentDay(this, mDayOfWeek);
        if (savedInstanceState == null)
        {
            // add the day tabs to the view
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            SlidingDayTabsFragment fragment = new SlidingDayTabsFragment();
            transaction.replace(R.id.sample_content_fragment, fragment);
            transaction.commit();
        }


        mMealTab = (LinearLayout) findViewById(R.id.mealTab);

        populateDailyMealsView();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_weekly_meals, menu);
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
        else if (id == R.id.deleteMenu)
            confirmDelete();
        else if (id == R.id.pet_status)
        {
            Intent intent = new Intent(this, PetStatusActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.add_meal)
        {
            Intent intent = new Intent(this, AddDailyMenusActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private void confirmDelete()
    {
        DialogFragment deleteDialog = new ConfirmDeleteDialogFragment();
        deleteDialog.show(getFragmentManager(), "delete");
    }

    @Override
    public void onDaySelected(int index, String day)
    {
        mDayOfWeek = day;
        PreferencesHelper.setCurrentDay(getApplicationContext(), day);


        clearDailyMealsView();
        populateDailyMealsView();
    }

    private void clearDailyMealsView()
    {
        mMealTab.removeAllViews();
    }

    private void populateDailyMealsView()
    {
        mCurrentMenu = PreferencesHelper.getCurrentMenu(this);
        mHelper = new DbHelper(this);

        mMeals = mHelper.selectMealsOfDay(mCurrentMenu, mDayOfWeek);

        String buttonText = "Edit meal";
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        for (int i = 0; i < mMeals.size(); i++)
        {
            TextView label = new TextView(this);
            TextView timeView = new TextView(this);

            TextView part = new TextView(this);
            TextView amount = new TextView(this);

            label.setText("Meal " + (i + 1));
            label.setTextAppearance(this, R.style.Base_TextAppearance_AppCompat_Subhead);

            timeView.setText(mMeals.get(i).getFeedingTime());

            String meat = mMeals.get(i).getType();
            String partText = (mMeals.get(i).getType() + " " + mMeals.get(i).getPart().replace('_', ' '));


            if (meat.equalsIgnoreCase("mush"))
                partText = partText.replace("Mush ", "");


            part.setText(partText);
            amount.setText(String.valueOf(mMeals.get(i).getAmount()) + " grams");

            mMealTab.addView(label);
            mMealTab.addView(timeView);

            mMealTab.addView(part);
            mMealTab.addView(amount);

            Button editButton = new Button(getApplicationContext());
            editButton.setText(buttonText + " " + (i + 1));
            editButton.setLayoutParams(buttonParams);
            editButton.setTextAppearance(getApplicationContext(), R.style.Base_Widget_AppCompat_Button_Small);
            editButton.setId(mMeals.get(i).getId());

            mMealTab.addView(editButton);

            editButton.setOnClickListener(this);
        }

    }

    public void startReviewWeeklyMenu(View view)
    {
        Intent intent = new Intent(this, NutritionReviewActivity.class);

        startActivity(intent);
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog)
    {
        mHelper.deleteWeeklyMenu(mCurrentMenu);

        Intent intent = new Intent(this, WeeklyMenuListActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog)
    {
        dialog.dismiss();
    }

    @Override
    public void onClick(View view)
    {
        int mealId = view.getId();
        PreferencesHelper.setCurrentMeal(getApplicationContext(), mealId);

        Intent intent = new Intent(getApplicationContext(), EditMealActivity.class);
        startActivity(intent);
    }
}
