package com.windcoder.nightbook.services;

import com.windcoder.common.entity.PageEntity;
import com.windcoder.common.utills.ReturnResult;
import com.windcoder.nightbook.entity.BookSearch;
import com.windcoder.nightbook.entity.UserBook;
import com.windcoder.nightbook.entity.UserBookInfo;

import java.util.List;

/**
 * Description:
 * User: WindCoder
 * Date: 2017-10-17
 * Time: 21:59 下午
 */
public interface IUserBookService {

    public List<UserBookInfo> findUserBookInfoListByUidOfPage(PageEntity page, UserBook ub);
    public ReturnResult findUserIsHasBook(UserBook ub);
    public ReturnResult updateReadStatusById(UserBook ub);
    public ReturnResult addReadStatusByUidAndBid(UserBook ub);
    public ReturnResult findBookInfoByBidInf(UserBook ub);

    public ReturnResult findByUserBookNumInterface(String uuid);

    public ReturnResult getBooksByTagAndPage(BookSearch bs);
}
