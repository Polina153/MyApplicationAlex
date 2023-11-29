package com.example.myapplication.view;

import static android.content.Context.MODE_PRIVATE;

import static com.example.myapplication.view.DetailsFragment.NOTE_KEY;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.MainActivity;
import com.example.myapplication.model.ISharedPreferences;
import com.example.myapplication.model.Note;
import com.example.myapplication.model.SharedPreferencesImpl;
import com.example.myapplication.presenter.Navigator;
import com.example.myapplication.presenter.NotesAdapter;
import com.example.myapplication.R;
import com.example.myapplication.presenter.ToolbarCreator;

import java.util.ArrayList;


public class MainFragment extends Fragment {
    public static final String REQUEST_KEY = "requestKey";
    private Navigator navigator;
    private ToolbarCreator toolbarCreator;
    private NotesAdapter notesAdapter;
    private ISharedPreferences sharedPref;
    private int positionOfClickedElement;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPref = new SharedPreferencesImpl(requireActivity().getPreferences(MODE_PRIVATE));
        getParentFragmentManager().setFragmentResultListener(REQUEST_KEY, this, (requestKey, bundle) -> {
            Note note = bundle.getParcelable(NOTE_KEY);
            sharedPref.saveNote(note, positionOfClickedElement);
            notesAdapter.changeElement(note, positionOfClickedElement);
        });
    }

    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        navigator = ((MainActivity) context).getNavigator();
        toolbarCreator = ((MainActivity) context).getToolbarCreator();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AppCompatActivity activity = (AppCompatActivity) requireActivity();
        toolbarCreator.setActionBar(view.findViewById(R.id.my_toolbar), activity);
        setHasOptionsMenu(true);
        createRecyclerView(view);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void createRecyclerView(@NonNull View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_lines);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL);
        itemDecoration.setDrawable(getResources().getDrawable(R.drawable.separator, null));
        recyclerView.addItemDecoration(itemDecoration);
        if (notesAdapter == null) {
            notesAdapter = new NotesAdapter(sharedPref.getNotes(),
                    new NotesAdapter.OnMyItemClickListener() {
                        @Override
                        public void onListItemClick(Note note, int position) {
                            positionOfClickedElement = position;
                            //navigator.addFragment(DetailsFragment.newInstance(note));
                            navigator.addFragment(DetailsFragment.newInstance(note));
                        }
                    }, sharedPref, this);
        }
        recyclerView.setAdapter(notesAdapter);
    }
    public void onPause() {
        ArrayList<Note> list = notesAdapter.getList();
        //Неоптимальный код: нужно только сохранять checkbox, а не все переменные класса Note
        sharedPref.saveNotes(list);
        super.onPause();
    }

    @Override
    public void onResume() {
        sharedPref.getNotes();
        super.onResume();
    }

    public static Fragment newInstance() {
        return new MainFragment();
    }
}