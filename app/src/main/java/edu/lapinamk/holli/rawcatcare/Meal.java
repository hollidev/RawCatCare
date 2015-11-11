package edu.lapinamk.holli.rawcatcare;

public class Meal
{
    private int id;
    private String name;
    private String type;
    private String part;
    private float amount;
    private Nutrients nutrients;
    private String feedingTime;
    private String dayOfWeek;
    private int dayId;
    private int weekId;

    public Meal(int id, String type, String part, float amount, String feedingTime)
    {
        this.feedingTime = feedingTime;
        this.id = id;
        this.type = type;
        this.part = part;
        this.amount = amount;
    }

    public Meal(int id, String type, String part, float amount, String feedingTime, String day)
    {
        this.feedingTime = feedingTime;
        this.id = id;
        this.type = type;
        this.part = part;
        this.amount = amount;
        this.dayOfWeek = day;
    }

    public Meal(String type, String part, float amount, String feedingTime, String dayOfWeek, int weekId)
    {
        this.type = type;
        this.part = part;
        this.amount = amount;
        this.feedingTime = feedingTime;
        this.dayOfWeek = dayOfWeek;
        this.weekId = weekId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setAmount(float amount)
    {
        this.amount = amount;
    }

    public Nutrients getNutrients()
    {
        return nutrients;
    }

    public void setNutrients(Nutrients nutrients)
    {
        this.nutrients = nutrients;
    }

    public void setFeedingTime(String feedingTime)
    {
        this.feedingTime = feedingTime;
    }

    public String getDayOfWeek()
    {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek)
    {
        this.dayOfWeek = dayOfWeek;
    }

    public int getDayId()
    {
        return dayId;
    }

    public void setDayId(int dayId)
    {
        this.dayId = dayId;
    }

    public int getWeekId()
    {
        return weekId;
    }

    public void setWeekId(int weekId)
    {
        this.weekId = weekId;
    }

    @Override
    public String toString()
    {
        if(type.equalsIgnoreCase("mush"))
        {
            return "" + part.replace("_", " ") + ", " + amount + " grams";
        }
        else
            return "" + type.replace("_", " ") + " " + part.replace("_", " ") + " " + amount + " grams";
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getPart()
    {
        return part;
    }

    public void setPart(String part)
    {
        this.part = part;
    }

    public float getAmount()
    {
        return amount;
    }

    public void setAmount(int amount)
    {
        this.amount = amount;
    }

    public String getFeedingTime()
    {
        return feedingTime;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }
}
