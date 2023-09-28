package com.windcoder.nightbook.services;

import com.windcoder.common.utills.ReturnResult;
import com.windcoder.nightbook.entity.BookInfo;
import com.windcoder.nightbook.entity.BookSearch;

import java.util.List;

/**
 * Description:
 * User: WindCoder
 * Date: 2017-10-17
 * Time: 20:29 下午
 */
public interface IBookInfoService {
    public BookInfo findBookInfoBaseByIsbnORNot(String isbn);
    public BookInfo findBookInfoByBid(Integer bid);
    public String findIsbnByBid(int bid);
    public List<BookInfo> findBookInfoFromDouBanByKyes(BookSearch bookSearch);

    public List<BookInfo> findBookInfoFromDouBanByKyesTest(BookSearch bookSearch);
    public Integer findBidByIsbn(String isbn);
}
