package com.windcoder.nightbook.controller;

import com.windcoder.common.entity.PageEntity;
import com.windcoder.common.utills.Constants;
import com.windcoder.common.utills.ReturnResult;
import com.windcoder.nightbook.entity.*;
import com.windcoder.nightbook.services.IBookInfoService;
import com.windcoder.nightbook.services.IISsuesService;
import com.windcoder.nightbook.services.IUserBookService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Description:
 * User: WindCoder
 * Date: 2017-10-17
 * Time: 20:40 下午
 */
@Controller
@RequestMapping("/jsapi/")
public class JSAPIController {
    @Autowired
    private IBookInfoService bookInfoService;
    @Autowired
    private IUserBookService userBookService;

    @Autowired
    private IISsuesService issuesService;

    /**
     * 扫码添加
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("getBookByISBN")
    public String getBookByISBN(HttpServletRequest request,String isbn) {
        ReturnResult r = new ReturnResult();
        WXHeader header = new WXHeader();

        header.setId(request.getHeader(Constants.WX_HEADER_ID));
        JSONObject j = new JSONObject();
        //获取isbn码与id，查询用户是否添加过
        UserBook ub = new UserBook();
        ub.setIsbn(isbn);
        ub.setUuid(header.getId());
        r = userBookService.findUserIsHasBook(ub);

       return r.toJsonString();
    }

    /**
     * 关键词查书
     * @param request
     * @param bs
     * @return
     */
    @ResponseBody
    @RequestMapping("getBookByKeys")
    public String getBookByKeys(HttpServletRequest request, BookSearch bs) {
        ReturnResult r = new ReturnResult();
//        WXHeader header = new WXHeader();
//
//        header.setId(request.getHeader(Constants.WX_HEADER_ID));
//        JSONObject j = new JSONObject();
//        //获取isbn码与id，查询用户是否添加过
//        UserBook ub = new UserBook();
//        ub.setIsbn(isbn);
//        ub.setUuid(header.getId());
        bs.setUuid(request.getHeader(Constants.WX_HEADER_ID));
        r = userBookService.getBooksByTagAndPage(bs);
       return r.toJsonString();
    }

    @ResponseBody
    @RequestMapping("addIssues")
    public String addIssues(HttpServletRequest request, Issues bs) {
        ReturnResult r = new ReturnResult();
        bs.setUid(request.getHeader(Constants.WX_HEADER_ID));
        r = issuesService.addIssues(bs);
       return r.toJsonString();
    }

    /**
     * 更新阅读类型
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("updateReadStatus")
    public String updateReadStatus(HttpServletRequest request,UserBook ub){
        ReturnResult r = new ReturnResult();

        r= userBookService.updateReadStatusById(ub);

        return r.toJsonString();
    }

    @ResponseBody
    @RequestMapping("findByUserBookNums")
    public String findByUserBookNumInterface(HttpServletRequest request,UserBook ub){
        ReturnResult r = new ReturnResult();
        String uuid = request.getHeader(Constants.WX_HEADER_ID);
        r= userBookService.findByUserBookNumInterface(uuid);

        return r.toJsonString();
    }

    /**
     * 通过bid和uuid添加用户拥有的图书
     * @param request
     * @param ub
     * @return
     */
    @ResponseBody
    @RequestMapping("addReadStatus")
    public String addReadStatus(HttpServletRequest request,UserBook ub){
        ReturnResult r = new ReturnResult();
        ub.setUuid(request.getHeader(Constants.WX_HEADER_ID));
        r= userBookService.addReadStatusByUidAndBid(ub);
        return r.toJsonString();
    }

    /**
     * 根据Bid查找Book信息
     * @param request
     * @param ub
     * @return
     */
    @ResponseBody
    @RequestMapping("getBookInfoByBid")
    public String findBookInfoByBidInf(HttpServletRequest request,UserBook ub){
        ReturnResult r = new ReturnResult();
        ub.setUuid(request.getHeader(Constants.WX_HEADER_ID));
        r =userBookService.findBookInfoByBidInf(ub);
        return r.toJsonString();
    }

    /**
     * 分页获取报名用户-按条件查询
     * @param response
     * @param request
     * @param page
     * @param ub
     * @return
     */
    @ResponseBody
    @RequestMapping("findUserBook")
    public String findUserBookOfPage(HttpServletResponse response, HttpServletRequest request, PageEntity page, UserBook ub){
        ub.setUuid(request.getHeader(Constants.WX_HEADER_ID));
        List<UserBookInfo> playUsers = userBookService.findUserBookInfoListByUidOfPage(page,ub);
        JSONArray rows = new JSONArray(playUsers);
        JSONObject pageObj = new JSONObject(page);
        JSONObject j = new JSONObject();
        j.put("pageInfo",pageObj);
        j.put("rows",rows);
        ReturnResult r = new ReturnResult();
        r.setCode(1);
        r.setMsg("success");
        r.setResult(j);
        return r.toJsonString();
    }
}
