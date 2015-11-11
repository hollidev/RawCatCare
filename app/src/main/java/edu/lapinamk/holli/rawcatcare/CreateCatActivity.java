package edu.lapinamk.holli.rawcatcare;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;


public class CreateCatActivity extends ActionBarActivity {

    static final int RESULT_LOAD_IMAGE = 1;



    private String avatarPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_cat);

        Spinner breedSpinner = (Spinner) findViewById(R.id.breedSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.breeds_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        breedSpinner.setAdapter(adapter);

        Spinner activitySpinner = (Spinner) findViewById(R.id.activitySpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.activity_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        activitySpinner.setAdapter(adapter2);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_cat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void addPhoto(View view)
    {
        Intent intent = new Intent(
        Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(intent, RESULT_LOAD_IMAGE);

    }


    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch(requestCode) {
            case RESULT_LOAD_IMAGE:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(
                            selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String filePath = cursor.getString(columnIndex);
                    cursor.close();


                    Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                    setAvatarPath(filePath);

                    BitmapDrawable bmDrawable = new BitmapDrawable(super.getResources(), bitmap);

                    // Set the Image in ImageView after decoding the String
                    Button addPhotoButton = (Button) findViewById(R.id.addPhotoButton);
                    addPhotoButton.setBackgroundDrawable(bmDrawable);
                    addPhotoButton.setText("");
                }
        }
    }
    /*
    @Override

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
                Button addPhotoButton = (Button) findViewById(R.id.addPhotoButton);
                Bitmap bitmap = BitmapFactory.decodeFile(imgDecodableString);
                BitmapDrawable bmDrawable = new BitmapDrawable(super.getResources(), bitmap);

                // Set the Image in ImageView after decoding the String
                addPhotoButton.setBackgroundDrawable(bmDrawable);




            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }


    }
*/

    public void saveCatInfo(View view)
    {
        EditText mEditName = (EditText)findViewById(R.id.editName);
        String catName = mEditName.getText().toString();

        EditText mEditDob = (EditText)findViewById(R.id.editDob);
        Spinner spinner = (Spinner)findViewById(R.id.breedSpinner);

        String breed = spinner.getSelectedItem().toString();

        String dobString = mEditDob.getText().toString();
        EditText mEditWeight = (EditText)findViewById(R.id.editWeight);

        float weight = Float.parseFloat(mEditWeight.getText().toString());



        //Date dob = mEditDob.getText().toString();

        Log.v("catname ", catName);

        CatsDbHelper dbHelper = new CatsDbHelper(getApplicationContext());

        // Gets the data repository in write mode
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(CatDBEntry.CatEntry.COLUMN_NAME_NAME, catName);
        values.put(CatDBEntry.CatEntry.COLUMN_NAME_BREED, breed);
        values.put(CatDBEntry.CatEntry.COLUMN_NAME_DOB, dobString);
        values.put(CatDBEntry.CatEntry.COLUMN_NAME_WEIGHT, weight);
        values.put(CatDBEntry.CatEntry.COLUMN_NAME_SEX, "m");
        values.put(CatDBEntry.CatEntry.COLUMN_NAME_AVATAR, getAvatarPath());
        values.put(CatDBEntry.CatEntry.COLUMN_NAME_ACTIVITY, 1);
        values.put(CatDBEntry.CatEntry.COLUMN_NAME_NEUTEREDSTATE, 1);

        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                dbHelper.TABLE_CATS,
                dbHelper.COLUMN_NULLABLE,
                values);

        long id = newRowId;

        Intent intent = new Intent(this, StartActivity.class);
        startActivity(intent);
    }

    public String getAvatarPath() {
        return avatarPath;
    }

    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }
}
