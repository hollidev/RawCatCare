package edu.lapinamk.holli.rawcatcare;

import android.content.Context;
import android.widget.ArrayAdapter;

/**
 * AdapterSelector is a helper class whose only purpose in life is to initialize an adapter with an array
 */
public class AdapterSelector
{
    // returns an adapter for the part spinner in AddDailyMenusActivity
    public static ArrayAdapter getEdiblePartAdapter(ArrayAdapter adapter, String meat, Context c)
    {
        meat = meat.toLowerCase();

        // this structure initializes the adapter with the array of the selected meat
        switch (meat)
        {
            case "mush":
                adapter = ArrayAdapter.createFromResource(c, R.array.mush_readable_options, android.R.layout.simple_spinner_item);
                break;
            case "chicken":
                adapter = ArrayAdapter.createFromResource(c, R.array.parts_of_chicken, android.R.layout.simple_spinner_item);
                break;
            case "duck":
                adapter = ArrayAdapter.createFromResource(c, R.array.parts_of_duck, android.R.layout.simple_spinner_item);
                break;
            case "rabbit":
                adapter = ArrayAdapter.createFromResource(c, R.array.parts_of_rabbit, android.R.layout.simple_spinner_item);
                break;
            case "horse":
                adapter = ArrayAdapter.createFromResource(c, R.array.parts_of_horse, android.R.layout.simple_spinner_item);
                break;
            case "pig":
                adapter = ArrayAdapter.createFromResource(c, R.array.parts_of_pig, android.R.layout.simple_spinner_item);
                break;
            case "turkey":
                adapter = ArrayAdapter.createFromResource(c, R.array.parts_of_turkey, android.R.layout.simple_spinner_item);
                break;
            case "moose":
                adapter = ArrayAdapter.createFromResource(c, R.array.parts_of_moose, android.R.layout.simple_spinner_item);
                break;
            case "reindeer":
                adapter = ArrayAdapter.createFromResource(c, R.array.parts_of_reindeer, android.R.layout.simple_spinner_item);
                break;
            case "sheep":
                adapter = ArrayAdapter.createFromResource(c, R.array.parts_of_sheep, android.R.layout.simple_spinner_item);
                break;
            case "cow":
                adapter = ArrayAdapter.createFromResource(c, R.array.parts_of_cow, android.R.layout.simple_spinner_item);
                break;
        }


        return adapter;
    }

}
