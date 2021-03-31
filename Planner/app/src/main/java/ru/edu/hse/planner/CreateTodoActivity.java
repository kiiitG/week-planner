package ru.edu.hse.planner;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TimePicker;
import android.widget.Toast;

import java.time.LocalDateTime;

public class CreateTodoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_todo);

        EditText nameEditText = findViewById(R.id.create_name_edit);
        DatePicker datePicker = findViewById(R.id.create_date_edit);
        TimePicker timePicker = findViewById(R.id.timePicker);
        EditText groupNameEdit = findViewById(R.id.createGroupNameEdit);
        Button createBtn = findViewById(R.id.button);
        Button exitBtn = findViewById(R.id.todoExitBtn);

        exitBtn.setOnClickListener(v -> finish());
        groupNameEdit.setText(Repository.getFirstGroupName());
        groupNameEdit.setOnClickListener(v -> {
            PopupMenu menu = Repository.getPopupMenu(this, groupNameEdit);
            menu.setOnMenuItemClickListener(item -> {
                groupNameEdit.setText(item.getTitle());
                return false;
            });
            menu.show();
        });

        createBtn.setOnClickListener(v -> {
            String groupName = groupNameEdit.getText().toString();
            if (groupName.equals("нет групп")) {
                Toast.makeText(CreateTodoActivity.this, "нет доступных групп", Toast.LENGTH_SHORT).show();
            } else {
                LocalDateTime dateTime = LocalDateTime.of(datePicker.getYear(),
                        datePicker.getMonth() + 1,
                        datePicker.getDayOfMonth(),
                        timePicker.getHour(),
                        timePicker.getMinute());
                Todo todo = new Todo(nameEditText.getText().toString(),
                        dateTime.toString(), dateTime.getDayOfWeek().getValue(),
                        groupName);
                if (Repository.contains(todo)) {
                    Toast.makeText(CreateTodoActivity.this, "cобытие уже существует", Toast.LENGTH_SHORT).show();
                }
                else {
                    Repository.add(todo, todo.getGroupName());
                    Toast.makeText(CreateTodoActivity.this, "создано", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}