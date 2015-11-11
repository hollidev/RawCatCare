package edu.lapinamk.holli.rawcatcare;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class WeeklyMenuListActivity extends ActionBarActivity
{
    private int mCurrentPet;
    private ListView mWeeklyMenuListView;
    private ArrayList<Integer> mMenuIds;
    private ArrayList<String> mMenuNames;
    private int numberOfMenus;
    private DbHelper mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_menu);

        mCurrentPet = PreferencesHelper.getCurrentPet(this);
        showSavedMenus();

        this.setListListener();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_weekly_menu, menu);
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
        else if (id == R.id.pet_status)
        {
            Intent intent = new Intent(this, PetStatusActivity.class);
            startActivity(intent);
        }


        return super.onOptionsItemSelected(item);
    }

    public void createNewMenu(View view)
    {
        // max number of menus is currently 5
        if (numberOfMenus > 5)
        {
            Toast toast = Toast.makeText(getApplicationContext(), "Max number of menus reached", Toast.LENGTH_LONG);
            toast.show();
        }
        else
        {
            // if the user creates a new menu, set an invalid value as the current menu
            PreferencesHelper.setCurrentMenu(this, -1);

            Intent intent = new Intent(this, AddDailyMenusActivity.class);
            startActivity(intent);
        }
    }

    private void showSavedMenus()
    {
        mHelper = new DbHelper(this);

        mWeeklyMenuListView = (ListView) findViewById(R.id.weeklyMenuListView);

        // add the weekly menus to the list
        ArrayList<WeeklyMenu> menus = mHelper.selectWeeklyMenus(mCurrentPet);

        numberOfMenus = menus.size();

        mMenuIds = new ArrayList<>();
        mMenuNames = new ArrayList<>();

        if (numberOfMenus <= 0)
        {
            Toast toast = Toast.makeText(this, "No menus exist", Toast.LENGTH_SHORT);
            toast.show();
        }
        else
        {
            for (int i = 0; i < numberOfMenus; i++)
            {
                // prepare the names and id's of the menus
                mMenuIds.add(menus.get(i).getId());
                mMenuNames.add("Menu " + (i +1));
            }

            ListAdapter adapter;

            // create an adapter with the menu names
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mMenuNames);

            // populate the menu list through the adapter
            mWeeklyMenuListView.setAdapter(adapter);
        }
    }

    private void setListListener()
    {
        if (mWeeklyMenuListView != null)
        {
            mWeeklyMenuListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                {
                    // when the user selects a menu, get its id
                    int menuId = mMenuIds.get((int) id);
                    String menuName = mMenuNames.get((int) id);
                    startViewWeeklyMeals(menuId);
                }
            });
        }
    }

    private void startViewWeeklyMeals(int menuId)
    {
        // send the user to weekly meals activity
        Intent intent = new Intent(this, ViewWeeklyMealsActivity.class);
        PreferencesHelper.setCurrentMenu(getApplicationContext(), menuId);
        startActivity(intent);
    }
}
