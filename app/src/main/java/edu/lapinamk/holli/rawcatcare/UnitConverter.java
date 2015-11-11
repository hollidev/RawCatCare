package edu.lapinamk.holli.rawcatcare;

/**
 * A class for metric unit conversions
 */
public class UnitConverter
{
    private UnitConverter()
    {
    }

    public static double convertMicrogramsToGrams(double micrograms)
    {
        return micrograms / 1000000;
    }


}
