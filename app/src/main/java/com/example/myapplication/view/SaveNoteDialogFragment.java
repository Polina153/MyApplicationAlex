package com.example.myapplication.view;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.myapplication.R;

public class SaveNoteDialogFragment extends DialogFragment {
    public static final String TAG = "MY_DIALOG_FRAGMENT";
    private OnDialogFragmentClickListener clickListener;
    @Override
    @NonNull
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new AlertDialog.Builder(requireContext())
                .setTitle(R.string.question_to_user)
                .setPositiveButton(R.string.positive_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        clickListener.onButtonClick(ButtonName.YES_BUTTON);
                    }
                })
                .setNegativeButton(R.string.negative_button, (dialogInterface, i) -> {
                    clickListener.onButtonClick(ButtonName.NO_BUTTON);
                })
                .create();
    }
    void setButtonClickListener(OnDialogFragmentClickListener clickListener) {
        this.clickListener = clickListener;
    }
    interface OnDialogFragmentClickListener {
        void onButtonClick(ButtonName name);
    }
    enum ButtonName {
        YES_BUTTON, NO_BUTTON
    }
}
