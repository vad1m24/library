package by.ruva.lib.utils;

import by.ruva.lib.api.dto.BookDto;
import by.ruva.lib.api.dto.UserDto;
import by.ruva.lib.api.exceptions.EntityNotFoundException;
import by.ruva.lib.api.service.IBookService;
import by.ruva.lib.api.service.IUserService;

import org.apache.commons.net.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class ImgFileUploader {

    @Autowired
    IUserService userService;
    
    @Autowired
    IBookService bookService;

    private static final String IMAGE_EXTENSION = ".png";

    private static final String FOLDER_PATH = "classpath:static/img/";

    public void createOrUpdateUserAvatar(UserDto dto, MultipartFile file)
            throws IOException, EntityNotFoundException {
        if (file != null && !file.isEmpty()) {
            setImgInDB(file, dto);
        }
    }

    public void createOrUpdateBookCover(BookDto dto, MultipartFile file)
            throws IOException, EntityNotFoundException {
        if (file != null && !file.isEmpty()) {
            String bookIsbn = bookService.getBookById(dto.getId()).getIsbn();
            createOrUpdate(file, bookIsbn);
        }
    }

    private void setImgInDB(MultipartFile file, UserDto dto)
            throws IOException, EntityNotFoundException {
        String base64Encoded = new String(Base64.encodeBase64(file.getBytes()), StandardCharsets.UTF_8);
        userService.setOrUpdateUserAvatar(dto.getId(), base64Encoded);
    }

    private void createOrUpdate(MultipartFile file, String futureImgName)
            throws IOException {
        String filePath = new StringBuilder(FOLDER_PATH).append(futureImgName).append(IMAGE_EXTENSION).toString();
        File bookImage;
        try {
            bookImage = ResourceUtils.getFile(filePath);
        } catch (FileNotFoundException e) {
            URL fileUrl = ResourceUtils.getURL(FOLDER_PATH);
            bookImage = new File(
                    new StringBuilder(fileUrl.getPath()).append(futureImgName).append(IMAGE_EXTENSION).toString());
        }
        Path path = Paths.get(bookImage.getPath());
        byte[] bytes = file.getBytes();
        Files.write(path, bytes);
    }
}