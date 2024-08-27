package com.siam.notify.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.siam.notify.Noting.Noting;
import com.siam.notify.R;
import com.siam.notify.Room.Note.Database;
import com.siam.notify.Room.Note.NoteEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class Note extends Fragment {
    RecyclerView NoteList;
    LottieAnimationView no_data;
    List<NoteEntity> NoteArrayList;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note, container, false);
        NoteList = view.findViewById(R.id.recyclerItems);
        no_data = view.findViewById(R.id.no_data);

        NoteArrayList = new Database().Database(requireActivity()).getAllNote();
        NoteListAdapter listAdapter = new NoteListAdapter();
        NoteList.setAdapter(listAdapter);

        EditText searchEdit = view.findViewById(R.id.editNoteSearch);
        searchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                NoteArrayList = new Database().Database(requireActivity()).getSearchList(s.toString()+" ");
                NoteListAdapter listAdapter = new NoteListAdapter();
                NoteList.setAdapter(listAdapter);
                Toast.makeText(requireActivity(), ""+s.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private class NoteListAdapter extends RecyclerView.Adapter {

        public NoteListAdapter() {
            if (NoteArrayList.isEmpty()) {
                no_data.setVisibility(View.VISIBLE);
                NoteList.setVisibility(View.GONE);
            } else {
                no_data.setVisibility(View.GONE);
                NoteList.setVisibility(View.VISIBLE);
            }
        }

        public class NoteItem extends RecyclerView.ViewHolder {
            TextView description_notePreview, title_notePreview, todoText, codeText;
            LinearLayout Todo_Layout, codeLayout, mainNote;
            ImageView delete_note;
            public NoteItem(@NonNull View itemView) {
                super(itemView);
                description_notePreview = itemView.findViewById(R.id.description_notePreview);
                title_notePreview = itemView.findViewById(R.id.title_notePreview);
                Todo_Layout = itemView.findViewById(R.id.Todo_Layout);
                codeLayout = itemView.findViewById(R.id.codeLayout);
                todoText = itemView.findViewById(R.id.todoText);
                codeText = itemView.findViewById(R.id.codeText);
                mainNote = itemView.findViewById(R.id.mainNote);
                delete_note = itemView.findViewById(R.id.delete_note);

            }
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View NoteItems = LayoutInflater.from(requireActivity()).inflate(R.layout.item_note_preview,parent, false);
            return new NoteItem(NoteItems);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            NoteItem NoteHolder = (NoteItem) holder;

            NoteEntity note = NoteArrayList.get(position);
            NoteHolder.title_notePreview.setText(note.getTitle()+" ");

            NoteHolder.delete_note.setOnClickListener(v -> {
                new Database().Database(requireActivity()).deleteSingleNote(note.getId());
                Toast.makeText(requireActivity(), "Note Deleted", Toast.LENGTH_SHORT).show();
                reloadNotes();
            });

            NoteHolder.mainNote.setOnClickListener(v -> {
                Intent i = new Intent(requireActivity(), Noting.class);
                Noting.NoteId = note.getId();
                startActivity(i);
            });

            try {
                JSONArray noteArray = new JSONArray(note.getNote());
                Log.d("AMITOMILOG", "onBindViewHolder: "+ noteArray.toString());
                JSONObject noteObj = noteArray.getJSONObject(0);
                String noteText = noteObj.getString("note");
                String type = noteObj.getString("type");

                switch (type) {
                    case "Todo":
                        NoteHolder.Todo_Layout.setVisibility(View.VISIBLE);
                        NoteHolder.description_notePreview.setVisibility(View.GONE);
                        NoteHolder.codeLayout.setVisibility(View.GONE);
                        NoteHolder.todoText.setText(noteText);

                        break;
                    case "Text":
                        NoteHolder.Todo_Layout.setVisibility(View.GONE);
                        NoteHolder.description_notePreview.setVisibility(View.VISIBLE);
                        NoteHolder.codeLayout.setVisibility(View.GONE);
                        NoteHolder.description_notePreview.setText(noteText);
                        break;
                    case "Code":
                        NoteHolder.Todo_Layout.setVisibility(View.GONE);
                        NoteHolder.description_notePreview.setVisibility(View.GONE);
                        NoteHolder.codeLayout.setVisibility(View.VISIBLE);
                        NoteHolder.codeText.setText(noteText);
                        break;
                }

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public int getItemCount() {
            return NoteArrayList.size();
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        reloadNotes();
    }

    @Override
    public void onPause() {
        super.onPause();
        reloadNotes();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        reloadNotes();
    }

    private void reloadNotes() {
        NoteArrayList = new Database().Database(requireActivity()).getAllNote();
        NoteListAdapter listAdapter = new NoteListAdapter();
        NoteList.setAdapter(listAdapter);
    }
}