package com.windcoder.nightbook.common.service;

import com.windcoder.nightbook.common.entity.Book;
import com.windcoder.nightbook.common.repository.BookRepository;
import com.windcoder.nightbook.common.utils.douBan.DouBanBookSearch;
import com.windcoder.nightbook.common.utils.douBan.DouBanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService extends BaseService<Book,Long,BookRepository> {


    /**
     * 通过isbn查询书籍，先查数据库后查豆瓣（并保存到数据库）
     * @param isbn
     * @return
     */
    public Book findBookInfoBaseByIsbn(String isbn){
        Book bookInfoBase = findByIsbn(isbn);
        //存在，返回相应信息
        if (bookInfoBase != null){
            return bookInfoBase;
        }
        //不存在，前往豆瓣获取图书信息，添加booke数据库
        bookInfoBase = DouBanUtils.findBookInfoFromDouBanByISBN(isbn);
        if ( bookInfoBase!= null){
            save(bookInfoBase);
        }
        return bookInfoBase;
    }

    /**
     * 通过关键字查询图书信息
     * @param bookSearch
     * @return
     */
    public List<Book>  findBookInfoFromDouBanByKeys(DouBanBookSearch bookSearch){
        List<Book> books =  DouBanUtils.findBookInfoFromDouBanByKeys(bookSearch);
        if(books!=null){
            int bookLen = books.size();
            Book tmpBook = null;
            for(int i =0;i<bookLen;i++){
                tmpBook = findByIsbn(books.get(i).getIsbn());
                if(tmpBook == null){
                    save(books.get(i));
                }else{
                    books.get(i).setId(tmpBook.getId());
                }
            }
        }
        return books;
    }

    /**
     * 通过isbn查询数据库中书籍
     * @param isbin
     * @return
     */
    private Book findByIsbn(String isbin){
        return repository.findOneByIsbn(isbin);
    }
}
