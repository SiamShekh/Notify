package com.siam.notify.Noting;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.siam.notify.R;
import com.siam.notify.Room.Note.Database;
import com.siam.notify.Room.Note.NoteEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Noting extends AppCompatActivity {
    private LinearLayout dynamicContainer;
    private Button save_btn;
    private ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
    public static int NoteId = 0;
    NoteEntity noteItem;
    EditText title_edt;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_noting);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (NoteId != 0) {
            noteItem = new Database().Database(this).getNoteById(NoteId);
        }

        dynamicContainer = findViewById(R.id.dynamic_container);
        save_btn = findViewById(R.id.save_btn);
        Button buttonText = findViewById(R.id.button_text);
        Button buttonCode = findViewById(R.id.button_code);
        Button buttonTodo = findViewById(R.id.button_todo);
        title_edt = findViewById(R.id.title_edt);
        title_edt.setText(noteItem.getTitle()+"");

        buttonText.setOnClickListener(v -> addEditText("Text"));
        buttonCode.setOnClickListener(v -> addEditText("Code"));
        buttonTodo.setOnClickListener(v -> addEditText("Todo"));

        save_btn.setOnClickListener(v -> {
            saveNote();
        });

        try {
            JSONArray NoteArr = new JSONArray(noteItem.getNote());
            for (int i = 0; i < NoteArr.length(); i++) {
                Log.d("This is inspact: ", String.valueOf(NoteArr.getJSONObject(i)));
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }


    private void saveNote() {
        captureAllEditTextValues();
        JSONArray array = new JSONArray();

        for (int i = 0; i < arrayList.size(); i++) {
            JSONObject object = new JSONObject();
            try {
                object.put("type", arrayList.get(i).get("type"));
                object.put("no", arrayList.get(i).get("no"));
                object.put("note", arrayList.get(i).get("note"));
                array.put(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        String titleStr = title_edt.getText().toString();
        Long tsLong = System.currentTimeMillis() / 1000;
        Date date = new Date(tsLong * 1000);
        try {
            new Database().Database(this).InsertNote(new NoteEntity(titleStr, array.toString(), date + ""));
        } finally {
            Toast.makeText(this, "Note Saved", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("ResourceAsColor")
    private void addEditText(String type) {
        HashMap<String, String> hashMap = new HashMap<>();

        if (type.equals("Todo")) {
            LinearLayout newLinearLayout = new LinearLayout(this);
            newLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
            newLinearLayout.setWeightSum(10);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    200
            );
            newLinearLayout.setLayoutParams(layoutParams);

            LinearLayout todoParents = new LinearLayout(this);
            LinearLayout.LayoutParams todoParentsParams = new LinearLayout.LayoutParams(
                    0,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    9f
            );
            todoParents.setOrientation(LinearLayout.HORIZONTAL);
            todoParents.setGravity(Gravity.CENTER_VERTICAL);
            todoParents.setLayoutParams(todoParentsParams);

            View newView = new View(this);
            LinearLayout.LayoutParams viewParams = new LinearLayout.LayoutParams(
                    20, 20
            );
            viewParams.setMargins(0, 0, 20, 0);
            viewParams.gravity = Gravity.CENTER_VERTICAL;
            newView.setLayoutParams(viewParams);
            newView.setBackgroundColor(Color.parseColor("#424200"));

            EditText newEditText = new EditText(this);
            LinearLayout.LayoutParams editTextParams = new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    1.0f
            );
            editTextParams.setMargins(20, 0, 0, 0);
            newEditText.setLayoutParams(editTextParams);
            newEditText.setGravity(Gravity.CENTER_VERTICAL);
            newEditText.setBackgroundColor(Color.parseColor("#00000000"));
            newEditText.setHint("Todo");
            newEditText.setTextSize(16);
            newEditText.setPadding(10, 10, 10, 10);

            todoParents.addView(newView);
            todoParents.addView(newEditText);

            LinearLayout closeParents = new LinearLayout(this);
            LinearLayout.LayoutParams closeParentsParams = new LinearLayout.LayoutParams(
                    0,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    1f
            );
            closeParents.setGravity(Gravity.CENTER);
            closeParents.setLayoutParams(closeParentsParams);

            ImageView closeImg = new ImageView(this);
            LinearLayout.LayoutParams closeImgParams = new LinearLayout.LayoutParams(40, 40);
            closeImg.setLayoutParams(closeImgParams);
            closeImg.setImageResource(R.drawable.icon_close);
            closeParents.addView(closeImg);

            newLinearLayout.addView(todoParents);
            newLinearLayout.addView(closeParents);

            dynamicContainer.addView(newLinearLayout);
            closeImg.setOnClickListener(v -> dynamicContainer.removeView(newLinearLayout));
            // Store references and type in HashMap
            hashMap.put("type", "Todo");
            hashMap.put("no", String.valueOf(arrayList.size()));
            hashMap.put("note", "");
            arrayList.add(hashMap);

            newEditText.setTag(hashMap);
        } else if (type.equals("Code")) {
            LinearLayout codeParents = new LinearLayout(this);
            LinearLayout.LayoutParams codeEditorParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            codeParents.setLayoutParams(codeEditorParams);
            codeParents.setOrientation(LinearLayout.VERTICAL);
            codeParents.setWeightSum(10);
            codeEditorParams.setMargins(10, 10, 10, 10);

            EditText newEditText = new EditText(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 5, 0, 0);
            newEditText.setLayoutParams(params);

            newEditText.setHint("Code");
            newEditText.setTextSize(16);
            newEditText.setBackgroundColor(Color.parseColor("#00000000"));
            codeParents.setBackgroundColor(Color.BLACK);
            codeParents.setPadding(20, 20, 20, 20);
            newEditText.setTextColor(Color.WHITE);
            newEditText.setHintTextColor(Color.WHITE);
            newEditText.setPadding(10, 10, 10, 10);

            LinearLayout ColorsHeaderCodeEditLinner = new LinearLayout(this);
            LinearLayout.LayoutParams ColorsHeaderCodeEditLinnerParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            ColorsHeaderCodeEditLinner.setLayoutParams(ColorsHeaderCodeEditLinnerParams);
            ColorsHeaderCodeEditLinner.setOrientation(LinearLayout.HORIZONTAL);
            ColorsHeaderCodeEditLinner.setWeightSum(2);

            LinearLayout codeHeaderColorsParents = new LinearLayout(this);
            LinearLayout.LayoutParams codeHeaderColorsParentsParams = new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1f
            );
            codeHeaderColorsParents.setLayoutParams(codeHeaderColorsParentsParams);
            codeHeaderColorsParents.setOrientation(LinearLayout.HORIZONTAL);

            View RedColors = new View(this);
            LinearLayout.LayoutParams RedColorsParams = new LinearLayout.LayoutParams(
                    50,
                    50
            );
            RedColors.setBackgroundResource(R.drawable.shape_red);
            RedColors.setLayoutParams(RedColorsParams);
            codeHeaderColorsParents.addView(RedColors);

            View YellowColors = new View(this);
            LinearLayout.LayoutParams YellowColorsParams = new LinearLayout.LayoutParams(
                    50,
                    50
            );
            YellowColorsParams.setMargins(20, 0, 20, 0);
            YellowColors.setBackgroundResource(R.drawable.shape_circle);
            YellowColors.setLayoutParams(YellowColorsParams);
            codeHeaderColorsParents.addView(YellowColors);

            View GreensColors = new View(this);
            LinearLayout.LayoutParams GreensColorsParams = new LinearLayout.LayoutParams(
                    50,
                    50
            );
            GreensColors.setBackgroundResource(R.drawable.shape_green);
            GreensColors.setLayoutParams(GreensColorsParams);
            codeHeaderColorsParents.addView(GreensColors);
            ColorsHeaderCodeEditLinner.addView(codeHeaderColorsParents);

            LinearLayout closeCodeBlock = new LinearLayout(this);
            LinearLayout.LayoutParams closeCodeBlockParams = new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1f
            );
            closeCodeBlock.setLayoutParams(closeCodeBlockParams);
            closeCodeBlock.setGravity(Gravity.END);

            ImageView closeImg = new ImageView(this);
            LinearLayout.LayoutParams closeImgParams = new LinearLayout.LayoutParams(
                    40,
                    40
            );
            closeImg.setLayoutParams(closeImgParams);
            closeImg.setImageResource(R.drawable.icon_close);
            closeImg.setColorFilter(Color.WHITE);
            closeCodeBlock.addView(closeImg);
            ColorsHeaderCodeEditLinner.addView(closeCodeBlock);

            codeParents.addView(ColorsHeaderCodeEditLinner);

            codeParents.addView(newEditText);
            dynamicContainer.addView(codeParents);

            closeImg.setOnClickListener(v -> {
                dynamicContainer.removeView(codeParents);
            });

            // Store references and type in HashMap
            hashMap.put("type", type);
            hashMap.put("no", String.valueOf(arrayList.size()));
            hashMap.put("note", "");  // Initially empty, will be captured on save
            arrayList.add(hashMap);

            newEditText.setTag(hashMap);  // Attach the HashMap to the EditText as a tag for later retrieval
        } else {
            LinearLayout textParents = new LinearLayout(this);
            LinearLayout.LayoutParams textParentParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            textParents.setLayoutParams(textParentParams);
            textParents.setOrientation(LinearLayout.HORIZONTAL);
            textParents.setWeightSum(10);
            textParents.setGravity(Gravity.CENTER_VERTICAL);

            // Cut edit text
            LinearLayout deleteImageParents = new LinearLayout(this);
            LinearLayout.LayoutParams deleteImageParentsParams = new LinearLayout.LayoutParams(
                    0,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    1f
            );
            deleteImageParents.setLayoutParams(deleteImageParentsParams);
            deleteImageParents.setGravity(Gravity.CENTER);

            ImageView deleteImage = new ImageView(this);
            LinearLayout.LayoutParams deleteImageParams = new LinearLayout.LayoutParams(
                    40,
                    40
            );
            deleteImage.setLayoutParams(deleteImageParams);
            deleteImage.setImageResource(R.drawable.icon_close);
            deleteImageParents.addView(deleteImage);

            EditText newEditText = new EditText(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 5, 0, 0);
            newEditText.setLayoutParams(params);
            textParents.addView(newEditText);
            textParents.addView(deleteImageParents);

            deleteImage.setOnClickListener(v -> {
                dynamicContainer.removeView(textParents);
            });

            params.weight = 9f;
            newEditText.setHint("write the text...");
            newEditText.setTextColor(Color.BLACK);
            newEditText.setTextSize(16);
            newEditText.setBackgroundColor(Color.parseColor("#00000000"));
            newEditText.setPadding(10, 10, 10, 10);

            dynamicContainer.addView(textParents);

            // Store references and type in HashMap
            hashMap.put("type", type);
            hashMap.put("no", String.valueOf(arrayList.size()));
            hashMap.put("note", "");  // Initially empty, will be captured on save
            arrayList.add(hashMap);

            newEditText.setTag(hashMap);
        }
    }


    private void captureAllEditTextValues() {
        for (int i = 0; i < dynamicContainer.getChildCount(); i++) {
            View view = dynamicContainer.getChildAt(i);
            if (view instanceof LinearLayout) {
                LinearLayout layout = (LinearLayout) view;
                for (int j = 0; j < layout.getChildCount(); j++) {
                    View childView = layout.getChildAt(j);
                    if (childView instanceof EditText) {
                        EditText editText = (EditText) childView;
                        HashMap<String, String> map = (HashMap<String, String>) editText.getTag();
                        if (map != null) {
                            map.put("note", editText.getText().toString());  // Update the value in the map
                        }
                    }
                }
            } else if (view instanceof EditText) {
                EditText editText = (EditText) view;
                HashMap<String, String> map = (HashMap<String, String>) editText.getTag();
                if (map != null) {
                    map.put("note", editText.getText().toString());  // Update the value in the map
                }
            }
        }
    }
}
