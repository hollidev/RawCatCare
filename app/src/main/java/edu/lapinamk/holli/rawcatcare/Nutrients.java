package edu.lapinamk.holli.rawcatcare;

import java.util.HashMap;

@Deprecated
public class Nutrients
{
    private float calories;
    private float calcium;
    private float phosphorus;
    private float fat;
    private float protein;

    @Deprecated
    public Nutrients(float calories, float protein, float fat, float phosphorus, float calcium)
    {
        this.calories = calories;
        this.protein = protein;
        this.fat = fat;
        this.phosphorus = phosphorus;
        this.calcium = calcium;
    }

    @Deprecated
    public Nutrients(HashMap<String, String> values)
    {
        try
        {
            calories = Float.parseFloat(values.get("calories"));
            calcium = Float.parseFloat(values.get("calcium"));
            phosphorus = Float.parseFloat(values.get("phosphorus"));
            protein = Float.parseFloat(values.get("protein"));
            fat = Float.parseFloat(values.get("fat"));
        } catch (Exception e)
        {

        }
    }



    public float getCalories()
    {
        return calories;
    }

    public void setCalories(float calories)
    {
        this.calories = calories;
    }


    public float getCalcium()
    {
        return calcium;
    }

    public void setCalcium(float calcium)
    {
        this.calcium = calcium;
    }

    public float getPhosphorus()
    {
        return phosphorus;
    }

    public void setPhosphorus(float phosphorus)
    {
        this.phosphorus = phosphorus;
    }

    public float getFat()
    {
        return fat;
    }

    public void setFat(float fat)
    {
        this.fat = fat;
    }

    public float getProtein()
    {
        return protein;
    }

    public void setProtein(float protein)
    {
        this.protein = protein;
    }
}
