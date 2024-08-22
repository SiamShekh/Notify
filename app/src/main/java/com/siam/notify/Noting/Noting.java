package com.siam.notify.Noting;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.siam.notify.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Noting extends AppCompatActivity {
    private LinearLayout dynamicContainer;
    private Button save_btn;
    private ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();

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

        dynamicContainer = findViewById(R.id.dynamic_container);
        save_btn = findViewById(R.id.save_btn);
        Button buttonText = findViewById(R.id.button_text);
        Button buttonCode = findViewById(R.id.button_code);
        Button buttonTodo = findViewById(R.id.button_todo);

        buttonText.setOnClickListener(v -> addEditText("Text"));
        buttonCode.setOnClickListener(v -> addEditText("Code"));
        buttonTodo.setOnClickListener(v -> addEditText("Todo"));

        save_btn.setOnClickListener(v -> {
            captureAllEditTextValues();
            JSONArray array = new JSONArray();

            for (int i =0; i < arrayList.size() ; i++ ) {
                JSONObject object = new JSONObject();
                try {
                    object.put("type", arrayList.get(i).get("type"));
                    object.put("no" , arrayList.get(i).get("no"));
                    object.put("note" , arrayList.get(i).get("note"));
                    array.put(object);
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            Toast.makeText(this, ""+array.toString(), Toast.LENGTH_SHORT).show();
        });
    }

    @SuppressLint("ResourceAsColor")
    private void addEditText(String type) {
        HashMap<String, String> hashMap = new HashMap<>();

        if (type.equals("Todo")) {
            LinearLayout newLinearLayout = new LinearLayout(this);
            newLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    50
            );
            float density = getResources().getDisplayMetrics().density;
            int heightInPx = (int) (50 * density);
            layoutParams.height = heightInPx;
            newLinearLayout.setLayoutParams(layoutParams);

            View newView = new View(this);
            LinearLayout.LayoutParams viewParams = new LinearLayout.LayoutParams(
                    (int) (10 * density),
                    (int) (10 * density)
            );
            viewParams.gravity = android.view.Gravity.CENTER_VERTICAL;
            newView.setLayoutParams(viewParams);
            newView.setBackgroundColor(getResources().getColor(R.color.black));

            EditText newEditText = new EditText(this);
            LinearLayout.LayoutParams editTextParams = new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    1.0f
            );
            editTextParams.setMargins((int) (10 * density), 0, 0, 0);
            newEditText.setLayoutParams(editTextParams);
            newEditText.setGravity(android.view.Gravity.CENTER_VERTICAL);
            newEditText.setBackgroundColor(Color.parseColor("#00000000"));
            newEditText.setHint("Todo");
            newEditText.setTextSize(16);
            newEditText.setPadding(10, 10, 10, 10);

            newLinearLayout.addView(newView);
            newLinearLayout.addView(newEditText);
            dynamicContainer.addView(newLinearLayout);

            // Store references and type in HashMap
            hashMap.put("type", "Todo");
            hashMap.put("no", String.valueOf(arrayList.size()));
            hashMap.put("note", "");  // Initially empty, will be captured on save
            arrayList.add(hashMap);

            newEditText.setTag(hashMap);  // Attach the HashMap to the EditText as a tag for later retrieval
        } else {
            EditText newEditText = new EditText(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 5, 0, 0);
            newEditText.setLayoutParams(params);

            switch (type) {
                case "Text":
                    newEditText.setHint("Text");
                    newEditText.setTextColor(Color.BLACK);
                    newEditText.setTextSize(16);
                    newEditText.setPadding(10, 10, 10, 10);
                    break;
                case "Code":
                    newEditText.setHint("Code");
                    newEditText.setTextSize(16);
                    newEditText.setBackgroundColor(Color.BLACK);
                    newEditText.setTextColor(Color.WHITE);
                    newEditText.setHintTextColor(Color.WHITE);
                    newEditText.setPadding(10, 10, 10, 10);
                    break;
            }
            dynamicContainer.addView(newEditText);

            // Store references and type in HashMap
            hashMap.put("type", type);
            hashMap.put("no", String.valueOf(arrayList.size()));
            hashMap.put("note", "");  // Initially empty, will be captured on save
            arrayList.add(hashMap);

            newEditText.setTag(hashMap);  // Attach the HashMap to the EditText as a tag for later retrieval
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
