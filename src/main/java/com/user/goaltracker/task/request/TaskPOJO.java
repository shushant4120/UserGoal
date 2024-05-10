package com.user.goaltracker.task.request;

import java.util.List;

import com.user.goaltracker.util.Frequency;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class TaskPOJO {
    private String id;
    private String description;
    private int quantity;
    private String frequency;
    private List<Integer> daysOfWeek; // For weekly tasks
    private boolean remindersEnabled;
    private List<String> reminders; // Time suggestions for reminders
}
