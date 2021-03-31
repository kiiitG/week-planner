package ru.edu.hse.planner;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class WeekCalendar {
    private LocalDate start;
    private LocalDate end;
    private LocalDate currentDate;
    private int rows;
    private static final int COLUMNS = 8;
    private static final String[] zeroRow = {" ", "ПН", "ВТ", "СР", "ЧТ", "ПТ", "СБ", "ВС"};
    private List<TextView> hat;
    private final Context context;
    private List<TextView> names;
    private List<List<Button>> buttons;
    Calendar dateAndTime=Calendar.getInstance();
    private Observer observer;

    public WeekCalendar(Context context, Observer observer) {
        this.context = context;
        this.observer = observer;

        setCurrentDate(LocalDate.now());

        Update();
    }

    public String getStringBorders() {
        return start + " - " + end;
    }

    private void setCurrentDate(LocalDate currentDate) {
        this.currentDate = currentDate;
        this.start = currentDate.minusDays(currentDate.getDayOfWeek().getValue() - 1);
        this.end = currentDate.plusDays(7 - currentDate.getDayOfWeek().getValue());
    }

    public void goBack() {
        setCurrentDate(currentDate.minusDays(7));
    }

    public void goNext() {
        setCurrentDate(currentDate.plusDays(7));
    }

    public TableLayout Update() {
        names = new ArrayList<>();
        buttons = new ArrayList<>();
        rows = Repository.getCount() + 1;
        hat = new ArrayList<>();
        TableLayout res = new TableLayout(context);

        fillHat();

        for (int i = 0; i < rows; i++) {
            TableRow tableRow = new TableRow(context);
            for (int j = 0; j < COLUMNS; j++) {
                if (i == 0) {
                    tableRow.addView(hat.get(j));
                }
                else {
                    buttons.add(new ArrayList<>());
                    if (j == 0) {
                        TextView textView = createTextView(Repository.getNameByIndex(i - 1), i - 1);
                        names.add(textView);
                        tableRow.addView(textView);
                    }
                    else {
                        Todo todo = Repository.getTodo(i - 1, j - 1, start, end);
                        Button button = createButton(todo);
                        buttons.get(i - 1).add(button);
                        tableRow.addView(button);
                    }
                }
            }
            res.addView(tableRow);
        }
        return res;
    }

    private TextView createTextView(String text, int index) {
        TextView res = new TextView(context);
        TableRow.LayoutParams params = new TableRow.LayoutParams
                (0, TableRow.LayoutParams.MATCH_PARENT, 1);
        params.setMargins(1,1,1,1);
        res.setLayoutParams(params);
        res.setOnLongClickListener(v -> {
            Repository.remove(text, index);
            observer.Update();
            return true;
        });
        res.setTextColor(context.getResources().getColor(R.color.diary_sheet));
        res.setTextSize(12);
        res.setText(text);
        res.setBackgroundColor(context.getResources().getColor(R.color.teal_700));
        res.setTypeface(Typeface.MONOSPACE);
        return res;
    }

    private void fillHat() {
        for (int k = 0; k < COLUMNS; ++k) {
            TextView res = new TextView(context);
            TableRow.LayoutParams params = new TableRow.LayoutParams
                    (0, TableRow.LayoutParams.MATCH_PARENT, 1);
            params.setMargins(1,1,1,1);
            res.setLayoutParams(params);
            res.setText(zeroRow[k]);
            res.setTextColor(context.getResources().getColor(R.color.diary_sheet));
            res.setTextSize(22);
            res.setBackgroundColor(context.getResources().getColor(R.color.teal_700));
            res.setTypeface(Typeface.MONOSPACE);
            hat.add(res);
        }
    }

    private void showDialog(Todo todo) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setMessage(todo.toString());
        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }

    private Button createButton(Todo todo) {
        Button res = new Button(context);
        TableRow.LayoutParams params = new TableRow.LayoutParams
                (0, TableRow.LayoutParams.MATCH_PARENT, 1);
        params.setMargins(1,1,1,1);
        res.setLayoutParams(params);
        if (todo != null) {
            res.setBackgroundColor(todo.getColor());
            res.setOnClickListener(v -> showDialog(todo));
            res.setOnLongClickListener(v -> changeState(todo, v));
        }
        else {
            res.setVisibility(View.INVISIBLE);
        }
        return res;
    }

    private boolean changeState(Todo todo, View v) {
        Status status = todo.changeState();
        v.setBackgroundColor(todo.getColor());
        if (status == Status.MOVED) {
            showInputDialog(todo);
        }
        return true;
    }

    private void createNewMovedTodo(Todo todo) {
        LocalDateTime dt = LocalDateTime.of(dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH) + 1,
                dateAndTime.get(Calendar.DAY_OF_MONTH),
                dateAndTime.get(Calendar.HOUR_OF_DAY),
                dateAndTime.get(Calendar.MINUTE));
        Todo todo1 = new Todo(todo.getName(),
                dt.toString(), dt.getDayOfWeek().getValue(),
                todo.getGroupName());
        Repository.add(todo1, todo.getGroupName());
        observer.Update();
    }

    private void showInputDialog(Todo todo) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                t, dateAndTime.get(Calendar.HOUR_OF_DAY),
                dateAndTime.get(Calendar.MINUTE), true);
        timePickerDialog.setOnDismissListener(dialog -> createNewMovedTodo(todo));
        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                d, dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setOnDismissListener(dialog -> timePickerDialog.show());
        datePickerDialog.show();
    }

    DatePickerDialog.OnDateSetListener d = (view, year, monthOfYear, dayOfMonth) -> {
        dateAndTime.set(year, monthOfYear, dayOfMonth);
    };

    TimePickerDialog.OnTimeSetListener t = (view, hourOfDay, minute) -> {
        dateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
        dateAndTime.set(Calendar.MINUTE, minute);

        LocalDateTime dt = LocalDateTime.of(dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH) + 1,
                dateAndTime.get(Calendar.DAY_OF_MONTH),
                dateAndTime.get(Calendar.HOUR_OF_DAY),
                dateAndTime.get(Calendar.MINUTE));
    };
}
