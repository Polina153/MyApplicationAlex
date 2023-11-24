package com.example.myapplication;

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

import java.util.ArrayList;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {
    private ArrayList<Note> dataSet;
    private final Fragment fragment;
    private int position = 0;
    public ArrayList<Note> getDataSet() {
        return dataSet;
    }
    public NotesAdapter(@Nullable ArrayList<Note> dataSet,
                        Fragment fragment) {
        this.dataSet = dataSet;
        this.fragment = fragment;
    }

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

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_item_view, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.bind(dataSet.get(position));
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
        }
    }

}
