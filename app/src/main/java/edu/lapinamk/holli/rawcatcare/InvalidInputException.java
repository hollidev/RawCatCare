package edu.lapinamk.holli.rawcatcare;

import android.content.Context;
import android.widget.Toast;

public class InvalidInputException extends Exception
{
    public static final int INVALID_MEAL_AMOUNT = 0;
    public static final int INVALID_WEIGHT = 1;
    public static final int INVALID_DATE_OF_BIRTH = 2;
    public static final int NAME_EMPTY = 3;

    private String message;

    public InvalidInputException(int cause)
    {
        switch(cause)
        {
            case INVALID_DATE_OF_BIRTH:
                message = "Please enter a valid date of birth";
                break;
            case INVALID_MEAL_AMOUNT:
                message = "Please enter a valid amount";
                break;
            case INVALID_WEIGHT:
                message = "Please enter a valid weight";
                break;
            case NAME_EMPTY:
                message = "Please enter a name";
                break;
        }
    }

    public void showToast(Context context)
    {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public String getMessage()
    {
        return message;
    }
}
