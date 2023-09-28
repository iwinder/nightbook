package com.windcoder.nightbook.common.service;

import com.windcoder.nightbook.common.dto.BookDto;
import com.windcoder.nightbook.common.dto.UserBookCountDto;
import com.windcoder.nightbook.common.entity.Book;
import com.windcoder.nightbook.common.entity.Issues;
import com.windcoder.nightbook.common.entity.User;
import com.windcoder.nightbook.common.entity.UserBook;
import com.windcoder.nightbook.common.exception.BusinessException;
import com.windcoder.nightbook.common.utils.ModelMapperUtils;
import com.windcoder.nightbook.common.utils.ReturnCodeUtil;
import com.windcoder.nightbook.common.utils.ReturnResult;
import com.windcoder.nightbook.common.utils.douBan.DouBanBookSearch;
import org.json.JSONArray;
import org.json.JSONObject;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Service
public class CoreConsumerService {
    @Autowired
    BookService bookService;
    @Autowired
    UserService userService;
    @Autowired
    UserBookService userBookService;
    @Autowired
    IssuesService issuesService;



    /**
     * 通过扫码isbn查询图书信息以及保存用户阅读信息。
     * @param ub
     * @return
     */
    public UserBook findBookByISBN(UserBook ub){
        UserBook bookInfo =  userBookService.findOneByUserIdAndIsBn(ub);
        if (bookInfo != null){
            return bookInfo;
        }
        Book bookInfoNew = bookService.findBookInfoBaseByIsbn(ub.getIsbn());
        if (bookInfoNew != null){
            ub.setIsRead(1);
            bookInfoToUserBook(bookInfoNew,ub);
            userBookService.save(ub);
        }else{
            throw new BusinessException("未找到书籍，或许被吃掉了");
        }
        return bookInfo;
    }


    /**
     * 通过关键字查询图书信息--分页(目前仅豆瓣)
     * @param bookSearch
     * @return
     */
    public ReturnResult findBookInfoFromDouBanByKeys(DouBanBookSearch bookSearch){
        ReturnResult r = new ReturnResult();
        List<Book> books = bookService.findBookInfoFromDouBanByKeys(bookSearch);
        if (books == null){
            r.setCode(ReturnCodeUtil.MA_DATA_NULL);
            String error = null;
            if (bookSearch.getStart().equals(0)){
                 error = "未找到相关书籍";
            }else{
                error = "暂无更多书籍";
            }
            r.setMsg(error);
            return r;
        }
        Type type =  new TypeToken<List<BookDto>>(){}.getType();
        UserBook ub = new UserBook();
        List<BookDto> bookDtoList = ModelMapperUtils.map(books,type);
        for (BookDto book:bookDtoList){
            ub.setUserId(bookSearch.getUuid());
            ub.setBookId(book.getId());
            book.setHasStatus(checkUserBookHasStatus(ub));
        }
        r.setCode(ReturnCodeUtil.MA_OK);
        r.setMsg("success");
        JSONArray jsonArray = new JSONArray(bookDtoList);
        JSONObject pageObj = new JSONObject(bookSearch);
        JSONObject result = new JSONObject();
        result.put("pageInfo",pageObj);
        result.put("books",jsonArray);
        r.setResult(result);
        return r;
    }


    public UserBook updateReadStatusById(UserBook ub){
        UserBook oldUb =userBookService.findByUserIdAndBookId(ub);
        oldUb.setIsRead(ub.getIsRead());
        return userBookService.save(oldUb);
    }

    public UserBook addUserBookByUidAndBid(UserBook ub){
        Book book = bookService.findOne(ub.getBookId());
        bookInfoToUserBook(book,ub);
        return userBookService.save(ub);
    }

    public BookDto findBookInfoByBid(UserBook ub){
        Book book = bookService.findOne(ub.getBookId());
        if (book==null){
            throw new BusinessException("未找到书籍，或许被吃掉了");
        }
        BookDto bookDto = ModelMapperUtils.map(book,BookDto.class);

        bookDto.setHasStatus(checkUserBookHasStatus(ub));
        return bookDto;
    }

    public Page<UserBook> findInfoWithPage(UserBook ub, Pageable page){
        return userBookService.findInfoWithPage(ub,page);
    }

    public List<UserBookCountDto> findUserBookNumCount(Long userId){
        return userBookService.findUserBookNumCount(userId);
    }

    public User findOneUserByUId(Long id){
        return userService.findOne(id);
    }


    public Issues addIssues(Issues i){
        return issuesService.save(i);
    }

    private void bookInfoToUserBook(Book book,UserBook ub){
        ub.setBookId(book.getId());
        ub.setIsbn(book.getIsbn());
        ub.setTitle(book.getTitle());
        ub.setAuthor(book.getAuthor());
        ub.setHeadImage(book.getHeadImage());
    }

    private Integer checkUserBookHasStatus(UserBook ub){
        Integer count = userBookService.countByUserIdAndBookId(ub);
        return count.equals(0)?0:1;
    }



}
