package edu.lapinamk.holli.rawcatcare;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;

/*
AddDailyMenusActivity represents the view where the user can add meals to the menu
 */

public class AddDailyMenusActivity extends ActionBarActivity implements SlidingDayTabsFragment.OnDaySelectedListener
{
    private String mSelectedMeat;
    private String mSelectedPart;
    private TextView mCaloriesView;
    private TextView mProteinView;
    private TextView mFatView;
    private TimePicker mFeedingTimePicker;
    private EditText mEditGrams;
    private String[] mWeekdays;
    private Spinner mMeatTypeSpinner;
    private Spinner mEdiblePartsSpinner;
    private LinearLayout mMealInfoContainer;
    private String mDayOfWeek;
    private int mCurrentPet;
    private int mCurrentMenu;
    private ArrayAdapter mEdiblePartAdapter;
    private String[] mParsableMushOptions;
    private DbHelper mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_menu);


        if (savedInstanceState == null)
        {
            // creating the weekday tabs
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            SlidingDayTabsFragment fragment = new SlidingDayTabsFragment();
            transaction.replace(R.id.sample_content_fragment, fragment);
            transaction.commit();
        }

        mHelper = new DbHelper(this);
        mCurrentMenu = PreferencesHelper.getCurrentMenu(this);
        mCurrentPet = PreferencesHelper.getCurrentPet(this);

        this.initializeForm();
        this.handleListeners();

        if (mHelper.weeklyMenuExists(mCurrentMenu))
            populateDailyMealsView();

        mDayOfWeek = PreferencesHelper.getCurrentDay(getApplicationContext());

        // make sure the weekday is not null
        if (mDayOfWeek == null)
            mDayOfWeek = mWeekdays[0];
    }

    private void handleListeners()
    {
        mMeatTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                // update the choices shown by the part spinner when something is picked in the meat spinner
                mSelectedMeat = (parent.getItemAtPosition(position).toString());
                mEdiblePartAdapter = AdapterSelector.getEdiblePartAdapter(mEdiblePartAdapter, mSelectedMeat, parent.getContext());
                mEdiblePartAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mEdiblePartsSpinner.setAdapter(mEdiblePartAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        mEdiblePartsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                mSelectedPart = parent.getItemAtPosition(position).toString();

                if (mSelectedMeat.equalsIgnoreCase("mush"))
                    mSelectedPart = mParsableMushOptions[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_daily_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Action bar item presses are handled here
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }
        else if (id == R.id.pet_status)
        {
            Intent intent = new Intent(this, PetStatusActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public void saveMealToDb(View view)
    {
        mHelper = new DbHelper(getApplicationContext());

        // the weekly menu is only saved if it doesn't already exist
        if (!mHelper.weeklyMenuExists(mCurrentMenu))
            saveWeekToDb();

        int hour = mFeedingTimePicker.getCurrentHour();
        String minute = mFeedingTimePicker.getCurrentMinute().toString();

        // formatting the shown feeding time
        if (minute.length() < 2)
            minute = "0" + minute;
        String timeAsString = "" + hour + ":" + minute;

        try
        {
            try
            {
                Float amount = Float.parseFloat(mEditGrams.getText().toString());

                if (amount <= 0)
                    throw new InvalidInputException(InvalidInputException.INVALID_MEAL_AMOUNT);
                else
                {
                    mHelper = new DbHelper(getApplicationContext());
                    mDayOfWeek = PreferencesHelper.getCurrentDay(this);

                    // making sure yet again the day isn't null, causing the DB operation to fail
                    if (mDayOfWeek == null)
                        mDayOfWeek = "Monday";

                    Meal meal = new Meal(mSelectedMeat, mSelectedPart, amount, timeAsString, mDayOfWeek, PreferencesHelper.getCurrentMenu(this));
                    mHelper.addMealToDailyMenu(meal);

                    clearEdits();

                    Toast toast = Toast.makeText(getApplicationContext(), "Meal saved", Toast.LENGTH_SHORT);
                    toast.show();

                    populateDailyMealsView();
                }

            } catch (Exception e)
            {
                e.printStackTrace();
                throw new InvalidInputException(InvalidInputException.INVALID_MEAL_AMOUNT);
            }
        } catch (Exception e)
        {
            e.printStackTrace();

            Toast toast = Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void saveWeekToDb()
    {
        mHelper = new DbHelper(this);

        // save the weekly menu to the database
        long weekId = mHelper.addWeeklyMenuToDb(null, mCurrentPet);

        // set the created menu as the current menu
        PreferencesHelper.setCurrentMenu(this, (int) weekId);
    }


    private void clearEdits()
    {
        mEditGrams.setText("");
        mCaloriesView.setText("");
        mFatView.setText("");
        mProteinView.setText("");
    }

    private void initializeForm() // prepare the layout elements for being accessed in code
    {
        mMealInfoContainer = (LinearLayout) findViewById(R.id.mealInfoContainer);
        mParsableMushOptions = getResources().getStringArray(R.array.mush_parsable_options);
        mWeekdays = getResources().getStringArray(R.array.days_of_week);
        mEditGrams = (EditText) findViewById(R.id.editGrams);
        mCaloriesView = (TextView) findViewById(R.id.caloriesView);
        mFatView = (TextView) findViewById(R.id.fatView);
        mProteinView = (TextView) findViewById(R.id.proteinView);
        mFeedingTimePicker = (TimePicker) findViewById(R.id.feedingTimePicker);
        mFeedingTimePicker.setIs24HourView(true);
        mEdiblePartAdapter = ArrayAdapter.createFromResource(this, R.array.mush_readable_options, android.R.layout.simple_spinner_item);
        mMeatTypeSpinner = (Spinner) findViewById(R.id.meatTypeSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.types_of_meat, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mMeatTypeSpinner.setAdapter(adapter);
        mEdiblePartsSpinner = (Spinner) findViewById(R.id.ediblePartSpinner);
    }

    @Override
    public void onDaySelected(int index, String day)
    {
        PreferencesHelper.setCurrentDay(getApplicationContext(), day);

        // if the user selects a day, the shown meals are reset
        clearEdits();
        populateDailyMealsView();
    }

    private void clearDailyMealsView()
    {
        mMealInfoContainer.removeAllViews();
    }

    private void populateDailyMealsView()
    {
        // remove the previous meals from view
        clearDailyMealsView();
        mCurrentMenu = PreferencesHelper.getCurrentMenu(this);

        mHelper = new DbHelper(this);

        // get the day's meals
        ArrayList<Meal> meals = mHelper.selectMealsOfDay(mCurrentMenu, PreferencesHelper.getCurrentDay(this));

        // add the meals to the view
        for (int i = 0; i < meals.size(); i++)
        {
            TextView info = new TextView(this);

            String mealInfo = meals.get(i).toString();

            info.setText(mealInfo);
            info.setTextAppearance(this, R.style.Base_TextAppearance_AppCompat_Small);

            mMealInfoContainer.addView(info);
        }
    }
}


