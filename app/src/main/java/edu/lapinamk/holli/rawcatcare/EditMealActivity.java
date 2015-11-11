package edu.lapinamk.holli.rawcatcare;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;


public class EditMealActivity extends ActionBarActivity implements ConfirmDeleteDialogFragment.DeleteDialogListener
{
    private String mSelectedMeat;
    private String mSelectedPart;
    private TimePicker mFeedingTimePicker;
    private EditText mEditGrams;
    private Spinner mMeatTypeSpinner;
    private Spinner mEdiblePartSpinner;
    private ArrayAdapter mEdiblePartsAdapter;
    private String[] mParsableMushOptions;
    private Meal mMeal;
    private int currentMeal;
    private ArrayAdapter<CharSequence> mMeatTypeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_meal);

        currentMeal = PreferencesHelper.getCurrentMeal(this);

        DbHelper helper = new DbHelper(getApplicationContext());

        mMeal = helper.getCurrentMealInfo(currentMeal);

        this.initializeMembers();
        this.handleListeners();
        this.fillMealInfo();

    }

    private void fillMealInfo()
    {
        // populate the meal fields when the user enters the activity
        try
        {
            int amount = (int) mMeal.getAmount();
            String amountAsString = String.valueOf(amount);

            mEditGrams.setText(amountAsString);

            String feedingTime = mMeal.getFeedingTime();
            String hour = feedingTime.substring(0, feedingTime.indexOf(":"));
            String minute = feedingTime.substring(feedingTime.indexOf(":") + 1);

            // set the timepickers time as the meal's feeding time
            mFeedingTimePicker.setCurrentHour(Integer.parseInt(hour));
            mFeedingTimePicker.setCurrentMinute(Integer.parseInt(minute));

            mMeatTypeSpinner.setSelection(mMeatTypeAdapter.getPosition(mMeal.getType()));
            mEdiblePartSpinner.setSelection(mEdiblePartsAdapter.getPosition(mMeal.getPart()));

        } catch (NullPointerException e)
        {
            e.printStackTrace();
        }
    }

    private void handleListeners()
    {
        mMeatTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                mSelectedMeat = (parent.getItemAtPosition(position).toString());

                mEdiblePartsAdapter = AdapterSelector.getEdiblePartAdapter(mEdiblePartsAdapter, mSelectedMeat, parent.getContext());
                mEdiblePartsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mEdiblePartSpinner.setAdapter(mEdiblePartsAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        mEdiblePartSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
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
        getMenuInflater().inflate(R.menu.menu_edit_meal, menu);
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
        else if (id == R.id.deleteMeal)
            this.confirmDeleteMeal();

        return super.onOptionsItemSelected(item);
    }

    private void confirmDeleteMeal()
    {
        DialogFragment deleteDialog = new ConfirmDeleteDialogFragment();
        deleteDialog.show(getFragmentManager(), "delete");
    }

    public void updateMeal(View view)
    {
        int hour = mFeedingTimePicker.getCurrentHour();
        String minute = mFeedingTimePicker.getCurrentMinute().toString();

        if (minute.length() < 2)
            minute = "0" + minute;

        String timeAsString = "" + hour + ":" + minute;

        try{
            try
            {
                Float amount = Float.parseFloat(mEditGrams.getText().toString());

                if (amount <= 0) // make sure the meal has a weight
                    throw new InvalidInputException(InvalidInputException.INVALID_MEAL_AMOUNT);
                else
                {
                    mMeal = new Meal(currentMeal, mSelectedMeat, mSelectedPart, amount, timeAsString);

                    DbHelper helper = new DbHelper(getApplicationContext());
                    helper.updateMealInfo(mMeal);

                    Toast toast = Toast.makeText(getApplicationContext(), "Meal updated", Toast.LENGTH_SHORT);
                    toast.show();

                    Intent intent = new Intent(getApplicationContext(), ViewWeeklyMealsActivity.class);
                    startActivity(intent);
                }
            }catch (Exception e)
            {
                throw new InvalidInputException(InvalidInputException.INVALID_MEAL_AMOUNT);
            }


        }catch(Exception e)
        {
            e.printStackTrace();

            Toast toast = Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT);
            toast.show();
        }

    }


    private void initializeMembers()
    {
        mEdiblePartsAdapter = ArrayAdapter.createFromResource(this, R.array.mush_readable_options, android.R.layout.simple_spinner_item);
        mParsableMushOptions = getResources().getStringArray(R.array.mush_parsable_options);
        mEditGrams = (EditText) findViewById(R.id.editGrams);
        mFeedingTimePicker = (TimePicker) findViewById(R.id.feedingTimePicker);
        mFeedingTimePicker.setIs24HourView(true);
        mMeatTypeSpinner = (Spinner) findViewById(R.id.meatTypeSpinner);
        mMeatTypeAdapter = ArrayAdapter.createFromResource(this, R.array.types_of_meat, android.R.layout.simple_spinner_item);
        mMeatTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mMeatTypeSpinner.setAdapter(mMeatTypeAdapter);
        mEdiblePartSpinner = (Spinner) findViewById(R.id.ediblePartSpinner);
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog)
    {
        // if user has confirmed deletion of the meal, it is deleted
        DbHelper helper = new DbHelper(getApplicationContext());
        helper.deleteMeal(currentMeal);

        // going back to the previous activity
        Intent intent = new Intent(this, ViewWeeklyMealsActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog)
    {
        dialog.dismiss();
    }
}


