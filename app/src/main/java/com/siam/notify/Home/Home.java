package com.siam.notify.Home;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.siam.notify.Fragment.Help;
import com.siam.notify.Fragment.Me;
import com.siam.notify.Fragment.Note;
import com.siam.notify.Noting.Noting;
import com.siam.notify.R;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        frag(new Note());
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_native);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.note) {
                    frag(new Note());
                    return true;
                } else if (menuItem.getItemId() == R.id.help) {
                    frag(new Help());
                    return true;
                } else if (menuItem.getItemId() == R.id.me) {
                    frag(new Me());
                    return true;
                }
                return false;
            }
        });

    }
    public void onNewNote(View view) {
        Intent i = new Intent(this, Noting.class);
        startActivity(i);
    }


    private void frag(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.native_frame, fragment)
                .commit();
    }
}