package com.example.myapplication.presenter;

import android.annotation.SuppressLint;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.model.ISharedPreferences;
import com.example.myapplication.model.Note;

import java.util.ArrayList;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {
    private ArrayList<Note> dataSet;
    private final OnMyItemClickListener clickListener;
    private final ISharedPreferences sharedPref;
    private final Fragment fragment;
    private int position = 0;

    public ArrayList<Note> getDataSet() {
        return dataSet;
    }

    public NotesAdapter(@Nullable ArrayList<Note> dataSet,
                        OnMyItemClickListener clickListener,
                        @NonNull ISharedPreferences sharedPref,
                        Fragment fragment) {
        this.dataSet = dataSet;
        this.clickListener = clickListener;
        this.sharedPref = sharedPref;
        this.fragment = fragment;
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_item_view, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.bind(dataSet.get(position));
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public void changeElement(@NonNull Note note, int position) {
        dataSet.set(position, note);
        notifyItemChanged(position);
    }

    public void deleteElement(int position) {
        dataSet.remove(position);
        notifyItemRemoved(position);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void addNewElement(@NonNull Note note, int positionOfNewElement) {
        dataSet.add(positionOfNewElement, note);
        notifyItemInserted(positionOfNewElement);
    }

    public int getPosition() {
        return position;
    }

    public ArrayList<Note> getList() {
        return getDataSet();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView noteTextView;
        private final TextView body;
        private final TextView date;
        private final CheckBox isImportant;

        public ViewHolder(View view) {
            super(view);
            noteTextView = view.findViewById(R.id.text_view_note_title);
            body = view.findViewById(R.id.text_view_note_body);
            date = view.findViewById(R.id.text_view_date);
            isImportant = view.findViewById(R.id.is_important_checkbox);

            registerContextMenu(itemView);
        }

        private void registerContextMenu(View itemView) {
            if (fragment != null) {
                fragment.registerForContextMenu(itemView);
            }
        }

        void bind(Note note) {
            noteTextView.setText(note.getTitle());
            body.setText(note.getBody());
            date.setText(note.getDate());
            isImportant.setChecked(note.isImportant());
            isImportant.setOnClickListener(view -> {
                Note changedNote = new Note(note.getTitle(),
                        note.getBody(),
                        note.getDate(),
                        isImportant.isChecked());
                dataSet.set(getAdapterPosition(), changedNote);
                sharedPref.saveNote(changedNote, getAdapterPosition()/*this.getLayoutPosition()*/);
            });
            itemView.setOnClickListener(view -> {
                Note newNote = new Note(note.getTitle(),
                        note.getBody(),
                        note.getDate(),
                        isImportant.isChecked());
                //TODO Save isImportant
                sharedPref.saveNote(newNote, getAdapterPosition());
                clickListener.onListItemClick(
                        newNote,
                        getAdapterPosition());
            });
            itemView.setOnLongClickListener(view -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    itemView.showContextMenu(10, 10);
                    position = getAdapterPosition();
                }
                //longClickListener.onListItemLongClick(position);
                return true;
            });
        }
    }

    public interface OnMyItemClickListener {
        void onListItemClick(Note note, int position);
    }
}
