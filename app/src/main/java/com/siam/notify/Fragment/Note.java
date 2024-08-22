package com.siam.notify.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

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

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note, container, false);
        NoteList = view.findViewById(R.id.recyclerItems);
        no_data = view.findViewById(R.id.no_data);

        NoteListAdapter listAdapter = new NoteListAdapter();
        NoteList.setAdapter(listAdapter);
        //TODO: new Database().Database(this).getAllNote().isEmpty() (if its true then the database is empty and if is false then the database is have data)

        return view;
    }

    private class NoteListAdapter extends RecyclerView.Adapter {
        List<NoteEntity> NoteArrayList = new Database().Database(requireActivity()).getAllNote();

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
            LinearLayout Todo_Layout, codeLayout;

            public NoteItem(@NonNull View itemView) {
                super(itemView);
                description_notePreview = itemView.findViewById(R.id.description_notePreview);
                title_notePreview = itemView.findViewById(R.id.title_notePreview);
                Todo_Layout = itemView.findViewById(R.id.Todo_Layout);
                codeLayout = itemView.findViewById(R.id.codeLayout);
                todoText = itemView.findViewById(R.id.todoText);
                codeText = itemView.findViewById(R.id.codeText);

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
            try {
                JSONArray noteArray = new JSONArray(note.getNote());
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
}