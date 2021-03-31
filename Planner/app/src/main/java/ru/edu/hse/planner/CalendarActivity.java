package ru.edu.hse.planner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;

public class CalendarActivity extends AppCompatActivity implements Observer {
    private WeekCalendar calendar;
    TextView dateText;
    TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        Button exitBtn = findViewById(R.id.calendarExitBtn);
        Button groupButton = findViewById(R.id.calendarCreateGrBtn);
        Button todoButton = findViewById(R.id.calendarCreateTodoBtn);
        Button backBtn = findViewById(R.id.back_btn);
        Button nextBtn = findViewById(R.id.forward_btn);
        dateText = findViewById(R.id.calendar_date_text);
        tableLayout = findViewById(R.id.tableLayout);

        Repository repository = new Repository();
        repository.attach(this);

        calendar = new WeekCalendar(CalendarActivity.this, this);

        Update();

        exitBtn.setOnClickListener(v -> finish());
        groupButton.setOnClickListener(v -> transfer(this, CreateGroupActivity.class));
        todoButton.setOnClickListener(v -> transfer(this, CreateTodoActivity.class));
        backBtn.setOnClickListener(v -> {
            calendar.goBack();
            Update();
        });
        nextBtn.setOnClickListener(v -> {
            calendar.goNext();
            Update();
        });
    }

    @Override
    public void Update() {
        tableLayout.removeAllViews();
        tableLayout.addView(calendar.Update());
        dateText.setText(calendar.getStringBorders());
    }

    private void transfer(Context context1, Class context2) {
        Intent intent = new Intent(context1, context2);
        startActivity(intent);
    }
}