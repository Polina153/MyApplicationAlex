package com.example.myapplication.view;

import static android.content.Context.MODE_PRIVATE;
import static android.widget.Toast.LENGTH_SHORT;

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

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myapplication.MainActivity;
import com.example.myapplication.model.ISharedPreferences;
import com.example.myapplication.model.Note;
import com.example.myapplication.model.SharedPreferencesImpl;
import com.example.myapplication.presenter.Navigator;
import com.example.myapplication.presenter.NotesAdapter;
import com.example.myapplication.R;
import com.example.myapplication.presenter.ToolbarCreator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


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
                            navigator.addFragment(DetailsFragment.newInstance(note));
                        }
                    }, sharedPref, this);
        }
        recyclerView.setAdapter(notesAdapter);
    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.cards_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = requireActivity().getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_delete) {
            //notesAdapter.deleteElement(getPositionOfClickedElement());
            notesAdapter.deleteElement(notesAdapter.getPosition());
            sharedPref.deleteNote(notesAdapter.getPosition());
            Toast.makeText(getActivity(), R.string.item_deleted, LENGTH_SHORT).show();
            return true;
        }
        return super.onContextItemSelected(item);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_add) {
            Note newNote = new Note(getString(R.string.title),
                    getString(R.string.note),
                    getDateOfCreation(),
                    false);
            sharedPref.saveNewNote(newNote);
            notesAdapter.addNewElement(newNote, 0);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void onPause() {
        ArrayList<Note> list = notesAdapter.getList();
        //Неоптимальный код: нужно только сохранять checkbox, а не все переменные класса Note
        sharedPref.saveNotes(list);
        super.onPause();
    }

    @NonNull
    private String getDateOfCreation() {
        return new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(new Date());
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

