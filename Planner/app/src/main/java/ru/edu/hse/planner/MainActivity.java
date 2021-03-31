package ru.edu.hse.planner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button createGrBtn = findViewById(R.id.menuCreateGroupBtn);
        Button createBtn = findViewById(R.id.create_btn);
        Button calendarBtn = findViewById(R.id.calendar_btn);
        Button exitBtn = findViewById(R.id.exit_btn);

        createGrBtn.setOnClickListener(v -> transfer(this, CreateGroupActivity.class));
        createBtn.setOnClickListener(v -> transfer(this, CreateTodoActivity.class));
        calendarBtn.setOnClickListener(v -> transfer(this, CalendarActivity.class));
        exitBtn.setOnClickListener(v -> onExitButtonClick());

        Repository.load();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.about) {
            onAboutItemClick();
        }
        return super.onOptionsItemSelected(item);
    }

    private void transfer(Context context1, Class context2) {
        Intent intent = new Intent(context1, context2);
        startActivity(intent);
    }

    private void onExitButtonClick() {
        finish();
    }

    private void onAboutItemClick() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        try {
            dialog.setMessage(getTitle().toString() + " версия " +
                    getPackageManager().getPackageInfo(getPackageName(), 0).versionName +
                    "\r\n\nPlanner" +
                    "\r\n\nАвтор - Гурова Екатерина Александровна БПИ198");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        dialog.setTitle("О программе");
        dialog.setNeutralButton("OK", (dialog1, which) -> dialog1.dismiss());
        dialog.setIcon(R.mipmap.ic_launcher_round);
        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }
}