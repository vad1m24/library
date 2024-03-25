package by.ruva.lib.utils;

import by.ruva.lib.api.dto.DepartmentDto;
import by.ruva.lib.api.dto.UserDto;
import by.ruva.lib.api.service.IUserService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.User;
import org.springframework.stereotype.Component;

@Component
public class FacebookConnectionSignUp implements ConnectionSignUp {

    @Autowired
    private IUserService userService;

    @Override
    public String execute(Connection<?> connection) {

        Facebook facebook = (Facebook) connection.getApi();
        String[] arr = { "id", "email", "first_name", "last_name" };
        User userProfile = facebook.fetchObject("me", User.class, arr);
        if (Boolean.TRUE.equals(userService.checkIfUserWithThisEmailAlreadyExists(userProfile.getEmail()))) {
            return userProfile.getEmail();
        } else {
            UserDto userDto = new UserDto();
            DepartmentDto depDto = new DepartmentDto();
            depDto.setName("default");
            userDto.setUsername(userProfile.getFirstName() + StringUtils.SPACE + userProfile.getLastName());
            userDto.setPassword(userProfile.getId());
            userDto.setEmail(userProfile.getEmail());
            userService.createUserFromSocialNetworks(userDto, depDto);
            return userProfile.getEmail();
        }
    }
}