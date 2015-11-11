package edu.lapinamk.holli.rawcatcare;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;


public class ConfirmDeleteDialogFragment extends DialogFragment
{
    private DeleteDialogListener mListener;

    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage("Are you sure you want to delete?").setPositiveButton("Yes", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                mListener.onDialogPositiveClick(ConfirmDeleteDialogFragment.this);
            }
        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        mListener.onDialogNegativeClick(ConfirmDeleteDialogFragment.this);
                    }
                });

        return builder.create();
    }


    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);

        try
        {
            mListener = (DeleteDialogListener) activity;
        } catch (ClassCastException e)
        {
            // if the class doesn't implement the method, throw exception
            throw new ClassCastException(activity.toString() + " must implement DeleteDialogListener");
        }
    }


    public interface DeleteDialogListener // the interface used for delivering yes/cancel button clicks to the activity
    {
        void onDialogPositiveClick(DialogFragment dialog);
        void onDialogNegativeClick(DialogFragment dialog);
    }
}


