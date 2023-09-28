package com.windcoder.nightbook.mina.controller;

import com.windcoder.nightbook.common.dto.BookDto;
import com.windcoder.nightbook.common.dto.UserBookCountDto;
import com.windcoder.nightbook.common.dto.UserBookDto;
import com.windcoder.nightbook.common.entity.Issues;
import com.windcoder.nightbook.common.entity.User;
import com.windcoder.nightbook.common.entity.UserBook;
import com.windcoder.nightbook.common.exception.BusinessException;
import com.windcoder.nightbook.common.service.CoreConsumerService;
import com.windcoder.nightbook.common.utils.ModelMapperUtils;
import com.windcoder.nightbook.common.utils.ReturnResult;
import com.windcoder.nightbook.common.utils.douBan.DouBanBookSearch;
import com.windcoder.nightbook.mina.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/mina/")
public class MinaUserBookController {
    @Autowired
    private CoreConsumerService coreConsumerService;

    /**
     * 扫码添加
     * @param request
     * @param isbn
     * @return
     */
    @RequestMapping("book/getBookByISBN/{isbn}")
    public UserBookDto findUserBookByISBN(HttpServletRequest request,@PathVariable("isbn")  String isbn){
        UserBook ub = new UserBook();
        ub.setIsbn(isbn);
        getUserId(request,ub);
        ub = coreConsumerService.findBookByISBN(ub);
        return   ModelMapperUtils.map(ub,UserBookDto.class);
    }

    /**
     * 关键词查书
     * @param request
     * @param bs
     * @return
     */
    @RequestMapping("book/getBookByKeys")
    public ReturnResult getBookByKeys(HttpServletRequest request, DouBanBookSearch bs) {
        bs.setUuid(Long.valueOf(request.getHeader(Constants.WX_HEADER_ID)));
        checkIdIsNotNull(bs.getUuid());
        return coreConsumerService.findBookInfoFromDouBanByKeys(bs);
    }

    /**
     * 更新阅读状态
     * @param request
     * @param ub
     */
    @RequestMapping("updateReadStatus")
    public void updateReadStatusById(HttpServletRequest request,UserBook ub){
        getUserId(request,ub);
        coreConsumerService.updateReadStatusById(ub);
    }

    /**
     * 通过bid和uuid添加用户拥有的图书
     * @param request
     * @param ub
     */
    @RequestMapping("addReadStatus")
    public void addUserBookByUidAndBid(HttpServletRequest request,UserBook ub){
        getUserId(request,ub);
        coreConsumerService.addUserBookByUidAndBid(ub);
    }

    /**
     * 根据Bid查找Book信息
     * @param request
     * @param bookId
     * @return
     */
    @RequestMapping("book/{bookId}")
    public BookDto findBookInfoByBid(HttpServletRequest request,@PathVariable("bookId") Long bookId){
        UserBook ub = new UserBook();
        ub.setBookId(bookId);
        getUserId(request,ub);
        return coreConsumerService.findBookInfoByBid(ub);
    }

    /**
     * 分页获取用户书籍
     * @param request
     * @param ub
     * @param page
     * @return
     */
    @RequestMapping("findUserBook")
    public Page<UserBook> findInfoWithPage(HttpServletRequest request, UserBook ub, @PageableDefault Pageable page){
        getUserId(request,ub);
        return coreConsumerService.findInfoWithPage(ub, page);
    }

    /**
     * 统计用户阅读
     */
    @RequestMapping("findUserBookNumCount")
    public List<UserBookCountDto> findUserBookNumCount(HttpServletRequest request){
        Long id =  Long.valueOf(request.getHeader(Constants.WX_HEADER_ID));
        checkIdIsNotNull(id);
        return coreConsumerService.findUserBookNumCount(id);
    }

    /**
     * 添加建议
     * @param request
     * @param bs
     */
    @RequestMapping("addIssues")
    public void addIssues(HttpServletRequest request, Issues bs) {
        Long id =  Long.valueOf(request.getHeader(Constants.WX_HEADER_ID));
        checkIdIsNotNull(id);
        User user = coreConsumerService.findOneUserByUId(id);
        if (user == null){
            throw new BusinessException("用户不存在");
        }
        bs.setUser(user);
        coreConsumerService.addIssues(bs);
    }



    private void getUserId(HttpServletRequest request, UserBook ub){
        ub.setUserId(Long.valueOf(request.getHeader(Constants.WX_HEADER_ID)));
        checkIdIsNotNull(ub.getUserId());
    }

    private void checkIdIsNotNull(Long id){
        if (id == null){
            throw new BusinessException("用户不存在");
        }
    }












}
