package ru.edu.hse.planner;

import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.time.LocalDate;
import java.time.LocalTime;

public class Todo {
    private String name;
    private Status status;
    private String stringTime;
    private int dayOfWeek;
    private int color;
    private String groupName;
    private LocalDate date;
    private LocalTime time;

    public Todo() {}

    public Todo(String name, String stringTime, int dayOfWeek, String groupName) {
        this.name = name;
        this.status = Status.CREATED;
        this.stringTime = stringTime;
        this.dayOfWeek = dayOfWeek;
        this.groupName = groupName;
        this.color = Repository.getColor(groupName);
    }

    public Status changeState() {
        if (status == Status.CREATED) {
            status = Status.FINISHED;
            color = Color.parseColor("#454545");
            return status;
        } else if (status == Status.FINISHED) {
            status = Status.MOVED;
            return status;
        }
        return Status.CREATED;
    }

    public String getName() {
        return name;
    }

    public Status getStatus() {
        return status;
    }

    public String getStringTime() {
        return stringTime;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public int getColor() {
        return color;
    }

    public String getGroupName() { return groupName; }

    public boolean isSuitable(String start, String end, int dayOfWeek) {
        return start.compareTo(stringTime) <= 0
                && end.compareTo(stringTime) >= 0
                && this.dayOfWeek == dayOfWeek;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof Todo) {
            return this.getStringTime().equals(((Todo) obj).getStringTime())
                    && this.getGroupName().equals(((Todo) obj).getGroupName());
        }
        return false;
    }

    @NonNull
    public String toString() {
        return name + "\n" + status + "\n" + stringTime;
    }
}
