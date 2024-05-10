package com.user.goaltracker.goal.request;

import java.util.List;

import com.user.goaltracker.task.request.TaskPOJO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class GoalPOJO {
    private String id;
    private String userId;
    private String goalName;
    private String description;
    private int minTimeline; // in days
    private int maxTimeline; // in days
    private List<TaskPOJO> tasks;
}
