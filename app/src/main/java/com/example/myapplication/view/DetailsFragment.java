package com.example.myapplication.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.model.Note;
import com.example.myapplication.presenter.Navigator;
import com.example.myapplication.presenter.ToolbarCreator;

public class DetailsFragment extends Fragment {

    public final static String NOTE_KEY = "NOTE_KEY";
    private static final String NOTE_IS_CLICKED_KEY = "NOTE_IS_CLICKED_KEY";
    private ToolbarCreator toolbarCreator;
    private EditText textOfTheNoteEditText;
    private EditText titleEditText;
    private TextView dateTextView;
    private CheckBox isImportantCheckBox;
    private Navigator navigator;
    private boolean isKeyboardActive = false;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        toolbarCreator = ((MainActivity) context).getToolbarCreator();
        navigator = ((MainActivity) context).getNavigator();
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                isKeyboardActive = false;
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(
                this, // LifecycleOwner
                callback);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AppCompatActivity activity = (AppCompatActivity) requireActivity();
        toolbarCreator.setActionBar(
                view.findViewById(R.id.second_toolbar),
                activity);
        toolbarCreator.setButtonBack(activity.getSupportActionBar());
        textOfTheNoteEditText = view.findViewById(R.id.body_of_note_edit_text);
        dateTextView = view.findViewById(R.id.date_of_the_note);
        titleEditText = view.findViewById(R.id.title);
        //TextView dateTextView = view.findViewById(R.id.date_of_the_note);
        isImportantCheckBox = view.findViewById(R.id.importance_second_fragment);

        Bundle args = getArguments();
        if (args != null) {
            Note note = args.getParcelable(NOTE_IS_CLICKED_KEY);
            titleEditText.setText(note.getTitle());
            textOfTheNoteEditText.setText(note.getBody());
            dateTextView.setText(note.getDate());
            isImportantCheckBox.setChecked(note.isImportant());
        }
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            setKeyboardStatus();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    private void hideKeyBoard() {
        AppCompatActivity activity = (AppCompatActivity) requireActivity();
        View view = activity.getCurrentFocus();
        InputMethodManager imm = (InputMethodManager) activity.
                getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    void setKeyboardStatus() {
        if (textOfTheNoteEditText.hasFocus() || titleEditText.hasFocus()) {
            isKeyboardActive = true;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    //TODO putParcelable
    public static DetailsFragment newInstance(Note note) {
        DetailsFragment detailsFragment = new DetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(NOTE_IS_CLICKED_KEY, note);
        detailsFragment.setArguments(bundle);
        return detailsFragment;
    }
}
