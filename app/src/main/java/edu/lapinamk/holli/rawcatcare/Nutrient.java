package edu.lapinamk.holli.rawcatcare;

// not in use currently

public class Nutrient
{
    private String name;
    private double value;

    public Nutrient(String name, double value)
    {
        this.name = name;
        this.value = value;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public double getValue()
    {
        return value;
    }

    public void setValue(double value)
    {
        this.value = value;
    }

    @Override
    public String toString()
    {
        return "Nutrient{" +
                "name='" + name + '\'' +
                ", value=" + value +
                '}';
    }
}
