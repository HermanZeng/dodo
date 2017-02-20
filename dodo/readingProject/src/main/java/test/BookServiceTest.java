package test;

import entity.Book;
import service.AdminBookService;
import utilities.SpringIocUtil;

/**
 * Created by heming on 7/13/2016.
 */
public class BookServiceTest {
    public static void main(String args[]) {
        AdminBookService bookService = SpringIocUtil.getBean("bookService", AdminBookService.class);
        Book book = null;

        try {
            book = bookService.findBookByIsbn("11111111");
        } catch (exception.dao.NotFoundException ne) {
            System.out.println("catched");
        }
        System.out.println(book.toString());
    }
}
