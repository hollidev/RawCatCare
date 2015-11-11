package edu.lapinamk.holli.rawcatcare;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Locale;


public class PetStatusActivity extends ActionBarActivity implements ConfirmDeleteDialogFragment.DeleteDialogListener
{
    private int mCurrentPet;
    private EditText mName;
    private EditText mDob;
    private EditText mWeight;
    private DbHelper mHelper;
    private Spinner mActivitySpinner;
    private Spinner mShapeSpinner;
    private Spinner mBreedSpinner;
    private RadioButton mMaleRadio;
    private RadioButton mFemaleRadio;
    private Cat mCat;
    private CheckBox mNursingStateBox;
    private CheckBox mNeuteredStateBox;
    private Button mSaveChangesButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_status);

        mCurrentPet = PreferencesHelper.getCurrentPet(getApplicationContext());

        initializeForm();
        disableEditing();

        fillCatForm();
    }

    private void initializeForm()
    {
        mSaveChangesButton = (Button) findViewById(R.id.status_button_save);
        mName = (EditText) findViewById(R.id.status_edit_name);
        mDob = (EditText) findViewById(R.id.status_edit_dob);
        mWeight = (EditText) findViewById(R.id.status_edit_weight);
        mActivitySpinner = (Spinner) findViewById(R.id.status_spinner_activity);
        mShapeSpinner = (Spinner) findViewById(R.id.status_spinner_shape);
        mFemaleRadio = (RadioButton) findViewById(R.id.status_radio_button_female);
        mMaleRadio = (RadioButton) findViewById(R.id.status_radio_button_male);
        mNursingStateBox = (CheckBox) findViewById(R.id.status_checkbox_nursing);
        mNeuteredStateBox = (CheckBox) findViewById(R.id.status_checkbox_neutered);
        mBreedSpinner = (Spinner) findViewById(R.id.status_spinner_breed);

        mNeuteredStateBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b)
            {
                if (compoundButton.isChecked()) // if the cat is neutered, it cannot be nursing (although technically it could)
                {
                    mNursingStateBox.setClickable(false);
                    mNursingStateBox.setChecked(false);
                }
                else
                    mNursingStateBox.setClickable(true);
            }
        });
    }

    private void fillCatForm() // fills the cat info form with values from the database
    {
        mHelper = new DbHelper(this);
        mCat = mHelper.getCurrentCatInfo(mCurrentPet);

        try
        {
            mName.setText(mCat.getName());

            String finFormat = "dd.MM.yyyy";
            SimpleDateFormat sdf = new SimpleDateFormat(finFormat, Locale.ENGLISH);
            mDob.setText(sdf.format(mCat.getDateOfBirth()));
            mWeight.setText(String.valueOf(mCat.getWeight()));

            if (mCat.getGender() == Cat.CAT_GENDER_FEMALE)
                mFemaleRadio.setChecked(true);
            else
                mMaleRadio.setChecked(true);

            mShapeSpinner.setSelection(mCat.getShape());
            mActivitySpinner.setSelection(mCat.getActivityLevel());

            handleAdapters();


        } catch (NullPointerException e)
        {
            e.printStackTrace();
        }
    }

    private void handleAdapters()
    {
        // Cceate an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> activityAdapter = ArrayAdapter.createFromResource(this, R.array.activity_array, android.R.layout.simple_spinner_item);
        // specify the layout to use when the list of choices appears
        activityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // apply the adapter to the spinner
        mActivitySpinner.setAdapter(activityAdapter);

        ArrayAdapter<CharSequence> shapeAdapter = ArrayAdapter.createFromResource(this, R.array.shapes_of_cat, android.R.layout.simple_spinner_item);
        shapeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mShapeSpinner.setAdapter(shapeAdapter);

        ArrayAdapter<CharSequence> breedAdapter = ArrayAdapter.createFromResource(this, R.array.breeds_array, android.R.layout.simple_spinner_item);
        breedAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBreedSpinner.setAdapter(breedAdapter);

        mBreedSpinner.setSelection(breedAdapter.getPosition(mCat.getBreed()));
        mShapeSpinner.setSelection(shapeAdapter.getPosition(Cat.CAT_SHAPES[mCat.getShape()]));
        mActivitySpinner.setSelection(activityAdapter.getPosition(Cat.CAT_ACTIVITY_LEVELS[mCat.getActivityLevel()]));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pet_status, menu);
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
        else if (id == R.id.deleteCat)
            this.confirmDeleteCat();
        else if (id == R.id.editCat)
            this.enableEditing();

        return super.onOptionsItemSelected(item);
    }

    private void confirmDeleteCat()
    {
        DialogFragment deleteDialog = new ConfirmDeleteDialogFragment();
        deleteDialog.show(getFragmentManager(), "delete");
    }

    private void enableEditing() // enable the fields for editing
    {
        mWeight.setEnabled(true);

        mActivitySpinner.setEnabled(true);

        mShapeSpinner.setEnabled(true);

        if (mCat.getNeuteredState() != Cat.CAT_STATE_NEUTERED)
            mNursingStateBox.setEnabled(true);
        if (mCat.getGender() == Cat.CAT_GENDER_MALE)
            mNursingStateBox.setEnabled(false);

        mSaveChangesButton.setVisibility(View.VISIBLE);
    }

    public void editCatInfo(View view)
    {
        mHelper = new DbHelper(this);
        int nursingState;
        int neuteredState;
        int activityLevel;
        int shape;
        float weight;

        if (mNursingStateBox.isChecked())
            nursingState = Cat.CAT_STATE_NURSING;
        else
            nursingState = Cat.CAT_STATE_NEGATIVE;

        if (mNeuteredStateBox.isChecked())
            neuteredState = Cat.CAT_STATE_NEUTERED;
        else
            neuteredState = Cat.CAT_STATE_NEGATIVE;

        activityLevel = mActivitySpinner.getSelectedItemPosition();
        shape = mShapeSpinner.getSelectedItemPosition();

        mCat.setNeuteredState(neuteredState);
        mCat.setNursingState(nursingState);
        mCat.setActivityLevel(activityLevel);
        mCat.setShape(shape);

        try
        {
            weight = Float.parseFloat(mWeight.getText().toString());
        } catch (Exception e)
        {
            weight = -1;
        }

        try
        {
            if (weight > 25)
                throw new InvalidInputException(InvalidInputException.INVALID_WEIGHT);
            else if (weight <= 0)
                throw new InvalidInputException(InvalidInputException.INVALID_WEIGHT);

            mCat.setWeight(weight);

            mHelper.updateCatInfo(mCat, PreferencesHelper.getCurrentPet(getApplicationContext()));

            disableEditing();

            Toast toast = Toast.makeText(this, "Cat info updated", Toast.LENGTH_SHORT);
            toast.show();

        } catch (Exception e)
        {
            e.printStackTrace();
            String errorText = e.getMessage();
            Toast toast = Toast.makeText(getApplicationContext(), errorText, Toast.LENGTH_SHORT);
            toast.show();
        }

    }

    private void disableEditing()
    {
        mName.setFocusable(false);
        mWeight.setEnabled(false);
        mActivitySpinner.setEnabled(false);
        mShapeSpinner.setEnabled(false);
        mNeuteredStateBox.setEnabled(false);
        mNursingStateBox.setEnabled(false);
        mBreedSpinner.setEnabled(false);
        mActivitySpinner.setEnabled(false);
        mShapeSpinner.setEnabled(false);
        mSaveChangesButton.setVisibility(View.INVISIBLE);
    }


    @Override
    public void onDialogPositiveClick(DialogFragment dialog)
    {
        mHelper = new DbHelper(this);

        mHelper.deleteCatFromDb(mCurrentPet);

        Intent intent = new Intent(this, StartActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog)
    {
        dialog.dismiss();
    }
}
