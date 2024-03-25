package by.ruva.lib.utils.mappers;

import by.ruva.lib.api.dao.IDepartmentDao;
import by.ruva.lib.api.dto.UserDto;
import by.ruva.lib.entities.User;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

import javax.annotation.PostConstruct;

@Component
public class UserMapper extends AMapper<User, UserDto> {

    @Autowired
    private IDepartmentDao departmentDao;

    public UserMapper(ModelMapper mapper, IDepartmentDao departmentDao) {
        super(User.class, UserDto.class);
        this.mapper = mapper;
        this.departmentDao = departmentDao;
    }

    @PostConstruct
    public void setupMapper() {
        mapper.createTypeMap(User.class, UserDto.class).addMappings(m -> m.skip(UserDto::setDepartmentId))
                .setPostConverter(toDtoConverter());
        mapper.createTypeMap(UserDto.class, User.class).addMappings(m -> m.skip(User::setDepartment))
                .setPostConverter(toEntityConverter());
    }

    @Override
    void mapSpecificFields(User source, UserDto destination) {
        destination.setDepartmentId(getId(source));
        destination.setDepartmentName(getDepartmentName(source));
    }

    private Long getId(User source) {
        return Objects.isNull(source) || Objects.isNull(source.getId()) ? null : source.getDepartment().getId();
    }

    private String getDepartmentName(User source) {
        return Objects.isNull(source) || Objects.isNull(source.getId()) ? null : source.getDepartment().getName();
    }

    @Override
    void mapSpecificFields(UserDto source, User destination) {
        destination.setDepartment(departmentDao.getByName(source.getDepartmentName()));
    }
}