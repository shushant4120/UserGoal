package com.user.goaltracker.users.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserPOJO {
    private String userID;
    private String userName;
    private String email;
    private String password;

}
