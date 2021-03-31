package ru.edu.hse.planner;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;


public class CreateGroupActivity extends AppCompatActivity {
    private int curColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        EditText nameEdit = findViewById(R.id.nameGroupEdit);
        Button colorBtn = findViewById(R.id.colorGroupBtn);
        Button createBtn = findViewById(R.id.createGroupBtn);
        Button exitBtn = findViewById(R.id.grExitBtn);

        curColor = Color.LTGRAY;
        colorBtn.setBackgroundColor(curColor);

        exitBtn.setOnClickListener(v -> finish());
        colorBtn.setOnClickListener(v -> ColorPickerDialogBuilder
                .with(this)
                .setTitle("Выберите цвет для группы активностей")
                .initialColor(curColor)
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setOnColorSelectedListener(selectedColor -> {
                    curColor = selectedColor;
                    colorBtn.setBackgroundColor(selectedColor);
                })
                .setPositiveButton("ok", (dialog, selectedColor, allColors) -> {})
                .setNegativeButton("cancel", (dialog, which) -> {})
                .build()
                .show());

        createBtn.setOnClickListener(v -> {
            Group group = new Group(nameEdit.getText().toString(), curColor);
            if (Repository.contains(group)) {
                Toast.makeText(CreateGroupActivity.this, "группа уже существует", Toast.LENGTH_SHORT).show();
            }
            else {
                Repository.add(group);
                colorBtn.setBackgroundColor(Color.LTGRAY);
                Toast.makeText(CreateGroupActivity.this, "создано", Toast.LENGTH_SHORT).show();
            }
        });
    }
}