package edu.lapinamk.holli.rawcatcare;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.Toast;

import java.util.ArrayList;


public class StartActivity extends ActionBarActivity
{
    private Button mCreateCatButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        mCreateCatButton = (Button) findViewById(R.id.newCatButton);
        DbHelper helper = new DbHelper(getApplicationContext());

        LinearLayout ll = (LinearLayout) findViewById(R.id.ll);

        ArrayList<Cat> cats = helper.selectCats();
        PreferencesHelper.setNumberOfCats(getApplicationContext(), cats.size());
        int numberOfCats = cats.size();

        for (int i = 0; i < cats.size(); i++)
        {
            Cat currentCat = cats.get(i);
            Button button = new Button(this);
            ImageView view = new ImageView(this);

            // specify the layout params of the button
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.CENTER;

            button.setLayoutParams(params);

            // the id of each cat is set as the id of their namebutton
            button.setId(currentCat.getId());
            button.setText(currentCat.getName());

            button.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    petChosen(v);
                }
            });

            // adding the cats image to the view if one has been assigned
            if(currentCat.getAvatarPath() != null && !currentCat.getAvatarPath().isEmpty())
            {
                Bitmap bitmap = BitmapFactory.decodeFile(currentCat.getAvatarPath());

                // resize the image
                bitmap = CreateCatActivity.getResizedBitmap(bitmap, 200, 120);
                BitmapDrawable bmDrawable = new BitmapDrawable(super.getResources(), bitmap);
                view.setImageDrawable(bmDrawable);

                ll.addView(view);
            }

            ll.addView(button);
        }

        // disable the button if the number of cats is 6 or more
        if (numberOfCats >= 6)
        {
            mCreateCatButton.setEnabled(false);

            Toast toast = Toast.makeText(this, "Max number of cats reached", Toast.LENGTH_LONG);
            toast.show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_start, menu);
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

    public void createNewCat(View view)
    {
        Intent intent = new Intent(this, CreateCatActivity.class);
        startActivity(intent);
    }

    private void petChosen(View view)
    {
        // when a cat is chosen, get the pressed button's id
        int id = view.getId();

        PreferencesHelper.setCurrentPet(getApplicationContext(), id);
        Intent intent = new Intent(this, WeeklyMenuListActivity.class);

        startActivity(intent);
    }


}
