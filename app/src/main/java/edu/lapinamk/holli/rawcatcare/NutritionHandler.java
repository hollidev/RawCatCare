package edu.lapinamk.holli.rawcatcare;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/*
NutritionHandler handles the collecting and adjusting of nutrients contained in meals
 */

public class NutritionHandler
{
    @Deprecated
    public static Nutrients prepareNutritionalValues(Context context, String meat, String part)
    {
        HashMap<String, String> values = new HashMap<>();
        Nutrients nutrients;

        try
        {
            values = NutrientParser.parseNutrients(values, context, meat, part);
        } catch (Exception e)
        {
            e.printStackTrace();
            Log.e("Catch block", Log.getStackTraceString(e));
        }

        nutrients = new Nutrients(values);

        return nutrients;
    }

    private static HashMap<String, Double> mapMealNutrition(Context context, String meat, String part)
    {
        // stringValuesMap will contain the string values returned by NutritionParser
        HashMap<String, String> stringValuesMap = new HashMap<>();
        HashMap<String, Double> doubleValuesMap = new HashMap<>();

        try
        {
            stringValuesMap = NutrientParser.parseNutrients(stringValuesMap, context, meat, part);
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        for (String key : stringValuesMap.keySet())
        {
            try
            {
                doubleValuesMap.put(key, Double.parseDouble(stringValuesMap.get(key)));
            } catch (NumberFormatException e)
            {
                Log.e(e.getMessage(), "Value not present");
            }
        }

        return doubleValuesMap;
    }

    public static float calculateMultiplier(float amount)
    {
        float multiplier = 1;

        if (amount > 100 || amount < 100)
            multiplier = amount / 100;

        return multiplier;
    }

    public static HashMap<String, Double> sumWeeklyNutrients(Context context, ArrayList<Meal> meals)
    {
        HashMap<String, Double> weeklyNutritionMap = new HashMap<>();
        HashMap<String, Double> mealNutritionMap;
        ArrayList<HashMap<String, Double>> mealNutritionMaps = new ArrayList<>(meals.size());

        ArrayList<String> nutrientNames = new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.nutrients)));

        for (int i = 0; i < nutrientNames.size(); i++)
        {
            // populating the map with all nutrient names
            weeklyNutritionMap.put(nutrientNames.get(i), 0.0);
        }


        for (int i = 0; i < meals.size(); i++)
        {
            mealNutritionMap = mapMealNutrition(context, meals.get(i).getType(), meals.get(i).getPart());

            float multiplier = calculateMultiplier(meals.get(i).getAmount());

            // adjust the values with the multiplier
            for (String key : nutrientNames)
            {
                if (mealNutritionMap.containsKey(key))
                    mealNutritionMap.put(key, mealNutritionMap.get(key) * multiplier);
            }

            mealNutritionMaps.add(mealNutritionMap);
        }

        // loop through the collection of meal nutrients and add them to the weeklyNutritionMap
        for (int i = 0; i < mealNutritionMaps.size(); i++)
        {
            for (String key : nutrientNames)
            {
                if (mealNutritionMaps.get(i).containsKey(key))
                {
                    weeklyNutritionMap.put(key, mealNutritionMaps.get(i).get(key) + weeklyNutritionMap.get(key));
                }
            }
        }

        weeklyNutritionMap.put("caToPRatio", weeklyNutritionMap.get("calcium") / weeklyNutritionMap.get("phosphorus"));
        weeklyNutritionMap.put("fatProteinRatio", weeklyNutritionMap.get("fat") / weeklyNutritionMap.get("protein"));

        return weeklyNutritionMap;

    }

    @Deprecated
    private static Nutrients adjustByAmount(Nutrients n, float amount)
    {
        float multiplier = calculateMultiplier(amount);

        n.setCalories(n.getCalories() * multiplier);
        n.setProtein(n.getProtein() * multiplier);
        n.setCalcium(n.getCalcium() * multiplier);
        n.setFat(n.getFat() * multiplier);
        n.setPhosphorus(n.getPhosphorus() * multiplier);

        return n;
    }

}
