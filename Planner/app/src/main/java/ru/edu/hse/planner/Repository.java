package ru.edu.hse.planner;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.PopupMenu;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.core.RepoInfo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.content.ContentValues.TAG;

public class Repository implements Observable {
    private static final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private static final List<Group> groups = new ArrayList<>();
    private static final List<Observer> subscribers = new ArrayList<>();

    public static void load() {
        if (groups.size() == 0) {
            mDatabase.get()
                    .addOnCompleteListener(task -> {
                        if (!task.isSuccessful()) {
                            Log.d(TAG, "error");
                        } else {
                            for (DataSnapshot ds : Objects.requireNonNull(task.getResult()).getChildren()) {
                                Repository.groups.add(ds.getValue(Group.class));
                            }
                        }
                    });
        }
    }

    public static int getCount() {
        return groups.size();
    }

    public static String getFirstGroupName() {
        if (groups.size() != 0) {
            return groups.get(0).getName();
        } else {
            return "нет групп";
        }
    }

    public static PopupMenu getPopupMenu(Context context, View view) {
        PopupMenu res = new PopupMenu(context, view);

        for (Group group : groups) {
            res.getMenu().add(group.getName());
        }

        return res;
    }

    public static String getNameByIndex(int index) {
        return groups.get(index).getName();
    }

    public static void add(Group group) {
        if (!groups.contains(group)) {
            groups.add(group);
            mDatabase.child(group.getName()).setValue(group);
            Update();
        }
    }

    public static void add(Todo todo, String groupName) {
        for (Group group : groups) {
            if (group.getName().equals(groupName) && !group.contains(todo)) {
                group.addTodo(todo);
                mDatabase.child(group.getName()).setValue(group);
                Update();
                return;
            }
        }
    }

    public static void remove(String groupName, int index) {
        groups.remove(index);
        mDatabase.child(groupName).removeValue();
        Update();
    }

    public static boolean contains(Group group) {
        return groups.contains(group);
    }

    public static boolean contains(Todo todo) {
        for (Group group : groups) {
            if (group.contains(todo)) {
                return true;
            }
        }
        return false;
    }

    public static Todo getTodo(int groupIndex, int todoIndex, LocalDate start, LocalDate end) {
        return groups.get(groupIndex).getTodo(todoIndex, start, end);
    }

    public static int getColor(String groupName) {
        for (Group group : groups) {
            if (groupName.equals(group.getName())) {
                return group.getColor();
            }
        }
        return Color.LTGRAY;
    }

    @Override
    public void attach(Observer observer) {
        subscribers.add(observer);
    }

    private static void Update() {
        for (Observer subscriber : subscribers) {
            subscriber.Update();
        }
    }
}
