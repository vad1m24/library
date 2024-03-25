package by.ruva.lib.service;

import by.ruva.lib.api.dao.IAGenericDao;
import by.ruva.lib.api.dao.IUserDao;
import by.ruva.lib.api.dto.DepartmentDto;
import by.ruva.lib.api.dto.RoleDto;
import by.ruva.lib.api.dto.UserDto;
import by.ruva.lib.api.exceptions.EntityNotFoundException;
import by.ruva.lib.api.exceptions.UserIsAlreadyExistsException;
import by.ruva.lib.api.service.IDepartmentService;
import by.ruva.lib.api.service.IRoleService;
import by.ruva.lib.api.service.IUserService;
import by.ruva.lib.entities.Department;
import by.ruva.lib.entities.Role;
import by.ruva.lib.entities.User;
import by.ruva.lib.utils.mailsender.EmailSender;
import by.ruva.lib.utils.mappers.AMapper;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import javax.mail.MessagingException;
import javax.transaction.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class UserService implements IUserService {

    @Autowired
    private IUserDao userDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AMapper<User, UserDto> userMapper;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private AMapper<Role, RoleDto> roleMapper;

    @Autowired
    private IDepartmentService departmentService;

    @Autowired
    private AMapper<Department, DepartmentDto> departmentMapper;

    @Autowired
    private EmailSender emailSender;

    public IAGenericDao<User> getUserDao() {
        return userDao;
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userMapper.toListDto(getUserDao().getAll());
    }

    @Override
    public UserDto createUser(UserDto userDto, DepartmentDto departmentDto)
            throws UserIsAlreadyExistsException, EntityNotFoundException {
        if (Boolean.TRUE.equals(checkIfUserWithThisEmailAlreadyExists(userDto.getEmail()))) {
            throw new UserIsAlreadyExistsException();
        } else {
            User user = new User().setEmail(userDto.getEmail()).setUsername(userDto.getUsername())
                    .setDepartment(getDepartmentByName(departmentDto))
                    .setPassword(passwordEncoder.encode(userDto.getPassword()))
                    .setRoles(Collections.singletonList(roleMapper.toEntity(roleService.getRoleById(2L))));
            return userMapper.toDto(getUserDao().create(user));
        }
    }

    @Override
    public UserDto createUserFromSocialNetworks(UserDto userDto, DepartmentDto departmentDto) {
        User user = new User().setEmail(userDto.getEmail()).setUsername(userDto.getUsername())
                .setDepartment(getDepartmentByName(departmentDto))
                .setPassword(passwordEncoder.encode(userDto.getPassword()))
                .setRoles(Collections.singletonList(roleMapper.toEntity(roleService.getRoleById(2L))));
        return userMapper.toDto(getUserDao().create(user));
    }

    @Override
    public UserDto getUserById(Long id) throws EntityNotFoundException {
        return Optional.ofNullable(userMapper.toDto(getUserDao().get(id)))
                .orElseThrow(() -> new EntityNotFoundException("User"));
    }

    @Override
    public UserDto getUserByEmail(String email) throws EntityNotFoundException {
        return Optional.ofNullable(userMapper.toDto(userDao.getByEmail(email)))
                .orElseThrow(() -> new EntityNotFoundException("User"));
    }

    @Override
    public UserDto getUserByName(String name) throws EntityNotFoundException {
        return Optional.ofNullable(userMapper.toDto(userDao.getByName(name)))
                .orElseThrow(() -> new EntityNotFoundException("User"));
    }

    @Override
    public void deleteUserById(Long id) throws EntityNotFoundException {
        getUserDao().delete(getUserDao().get(id));
    }

    @Override
    public UserDto updateUser(Long id, UserDto userDto, DepartmentDto departmentDto) throws EntityNotFoundException {
        User existingUser = Optional.ofNullable(getUserDao().get(id))
                .orElseThrow(() -> new EntityNotFoundException("User"));
        Optional.ofNullable(userDto.getEmail()).ifPresent(existingUser::setEmail);
        Optional.ofNullable(userDto.getUsername()).ifPresent(existingUser::setUsername);
        if (!StringUtils.isBlank(userDto.getPassword())) {
            existingUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }
        if (!StringUtils.isBlank(departmentDto.getName())) {
            existingUser.setDepartment(getDepartmentByName(departmentDto));
        }
        getUserDao().update(existingUser);
        return userMapper.toDto(existingUser);
    }

    public UserDto setOrUpdateUserAvatar(Long id, String encoded) throws EntityNotFoundException {
        User existingUser = Optional.ofNullable(getUserDao().get(id))
                .orElseThrow(() -> new EntityNotFoundException("User"));
        existingUser.setImg(encoded);
        getUserDao().update(existingUser);
        return userMapper.toDto(existingUser);
    }

    @Override
    public Boolean checkIfUserWithThisEmailAlreadyExists(String email) {
        return (userDao.getByEmail(email) != null);
    }

    public void sendEmailToAdmin(String email, String text) {
        try {
            emailSender.sendEmailToAdmin(email, text);
        } catch (MessagingException e) {
            log.info("Mail not sent!");
        }
    }

    @Override
    public UserDto setRoles(Long id, RoleDto roleDto) throws EntityNotFoundException {
        return userMapper.toDto(userDao.get(id).setRoles(
                Collections.singletonList(roleMapper.toEntity(roleService.getRoleByName(roleDto.getName())))));
    }

    @Override
    public void sendEmailWithNewPassword(String email) throws EntityNotFoundException {
        String newPassword = generateCommonLangPassword();
        UserDto userDto = getUserByEmail(email);
        userDto.setPassword(newPassword);
        DepartmentDto depDto = new DepartmentDto();
        updateUser(userDto.getId(), userDto, depDto);
        try {
            emailSender.sendEmailToUserWithNewPassword(newPassword, userDto);
        } catch (MessagingException e) {
            log.info("Mail not sent!");
        }
    }

    private Department getDepartmentByName(DepartmentDto departmentDto) {
        return departmentMapper.toEntity(departmentService.getDepartmentByName(departmentDto.getName()));
    }

    private String generateCommonLangPassword() {
        String upperCaseLetters = RandomStringUtils.random(2, 65, 90, true, true);
        String lowerCaseLetters = RandomStringUtils.random(2, 97, 122, true, true);
        String numbers = RandomStringUtils.randomNumeric(2);
        String totalChars = RandomStringUtils.randomAlphanumeric(2);
        String combinedChars = upperCaseLetters.concat(lowerCaseLetters).concat(numbers).concat(totalChars);
        List<Character> pwdChars = combinedChars.chars().mapToObj(c -> (char) c).collect(Collectors.toList());
        Collections.shuffle(pwdChars);
        return pwdChars.stream().collect(StringBuilder::new, StringBuilder::append, StringBuilder::append).toString();
    }
}
