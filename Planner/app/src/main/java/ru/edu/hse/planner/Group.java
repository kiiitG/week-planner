package ru.edu.hse.planner;

import androidx.annotation.Nullable;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Group {
    private String name;
    private int color;
    private List<Todo> todos;

    public Group() { }

    public Group(String name, int color) {
        this.name = name;
        this.todos = new ArrayList<>();
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public int getColor() {
        return color;
    }

    // Не удалять!
    public List<Todo> getTodos() {
        return todos;
    }

    public Todo getTodo(int index, LocalDate start, LocalDate end) {
        LocalDateTime startDateTime = LocalDateTime.of(start, LocalTime.MIDNIGHT);
        LocalDateTime endDateTime = LocalDateTime.of(end, LocalTime.MIDNIGHT.minusMinutes(1));
        if (todos == null) {
            todos = new ArrayList<>();
        }
            for (Todo todo : todos) {
                if (todo.isSuitable(startDateTime.toString(),
                        endDateTime.toString(), index + 1)) {
                    return todo;
                }
            }
        return null;
    }

    public boolean contains(Todo todo) {
        if (todos == null) {
            todos = new ArrayList<>();
        }
        return todos.contains(todo);
    }

    public void addTodo(Todo todo) {
        if (todos == null) {
            todos = new ArrayList<>();
        }
        todos.add(todo);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof Group) {
            return this.getName().equals(((Group) obj).getName());
        }
        return false;
    }
}
