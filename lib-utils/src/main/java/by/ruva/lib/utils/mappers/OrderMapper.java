package by.ruva.lib.utils.mappers;

import by.ruva.lib.api.dao.IBookDao;
import by.ruva.lib.api.dao.IUserDao;
import by.ruva.lib.api.dto.BookDto;
import by.ruva.lib.api.dto.OrderDto;
import by.ruva.lib.api.dto.UserDto;
import by.ruva.lib.entities.Book;
import by.ruva.lib.entities.Order;
import by.ruva.lib.entities.User;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

import javax.annotation.PostConstruct;

@Component
public class OrderMapper extends AMapper<Order, OrderDto> {

    @Autowired
    private AMapper<Book, BookDto> bookMapper;

    @Autowired
    private AMapper<User, UserDto> userMapper;

    @Autowired
    private IUserDao userDao;
    @Autowired
    private IBookDao bookDao;

    public OrderMapper(ModelMapper mapper, IBookDao bookDao, IUserDao userDao, AMapper<Book, BookDto> bookMapper,
            AMapper<User, UserDto> userMapper) {
        super(Order.class, OrderDto.class);
        this.bookMapper = bookMapper;
        this.userMapper = userMapper;
        this.userDao = userDao;
        this.bookDao = bookDao;
        this.mapper = mapper;
    }

    @PostConstruct
    public void setupMapper() {
        mapper.createTypeMap(Order.class, OrderDto.class).addMappings(m -> {
            m.skip(OrderDto::setUserDto);
            m.skip(OrderDto::setBookDto);
        }).setPostConverter(toDtoConverter());
        mapper.createTypeMap(OrderDto.class, Order.class).addMappings(m -> {
            m.skip(Order::setUser);
            m.skip(Order::setBook);
        }).setPostConverter(toEntityConverter());
    }

    @Override
    void mapSpecificFields(Order source, OrderDto destination) {
        destination.setUserDto(getUserDto(source));
        destination.setBookDto(getBookDto(source));
    }

    private BookDto getBookDto(Order source) {
        return Objects.isNull(source) || Objects.isNull(source.getId()) ? null : bookMapper.toDto(source.getBook());
    }

    private UserDto getUserDto(Order source) {
        return Objects.isNull(source) || Objects.isNull(source.getId()) ? null : userMapper.toDto(source.getUser());
    }

    @Override
    void mapSpecificFields(OrderDto source, Order destination) {
        destination.setUser(userDao.get(source.getUserDto().getId()));
        destination.setBook(bookDao.get(source.getBookDto().getId()));
    }
}