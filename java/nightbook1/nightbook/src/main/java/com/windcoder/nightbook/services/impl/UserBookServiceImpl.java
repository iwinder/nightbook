package com.windcoder.nightbook.services.impl;

import com.windcoder.common.entity.PageEntity;
import com.windcoder.common.services.pageUtil.IPageQueryService;
import com.windcoder.common.utills.DateUtillZ;
import com.windcoder.common.utills.ReturnResult;
import com.windcoder.nightbook.entity.*;
import com.windcoder.nightbook.services.IBookInfoService;
import com.windcoder.nightbook.services.IUserBookService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * User: WindCoder
 * Date: 2017-10-17
 * Time: 21:59 下午
 */
@Service("userInfoService")
public class UserBookServiceImpl implements IUserBookService {
    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    @Autowired
    private IBookInfoService bookInfoService;
    @Autowired
    private IPageQueryService pageQueryService;

    private static Logger logger = LogManager.getLogger(UserBookServiceImpl.class);
    /**
     * 添加拥有信息
     * @param ub
     * @return
     */
    public ReturnResult addUserBook(UserBook ub){
        ReturnResult r = new ReturnResult();
        ub.setDeleteStatus(1);
        ub.setCreatetime(DateUtillZ.getTimestampOfNow());
        ub.setUpdatetime(ub.getCreatetime());
        int bol = 0;
        try {
            bol = sqlSessionTemplate.insert("UserBook.addUserBook", ub);
            if (bol>0){
                r.setResult(ub.getId());
                r.setMsg("addUserBook success");
            }else{
                bol = -2;
                r.setMsg("addUserBook error");
            }
        }catch (Exception e){
            bol = -1;
            logger.error(e);
            e.printStackTrace();
            r.setMsg("addUserBook error");
        }
        r.setCode(bol);
        return r;
    }

    public List<UserBookInfo> findUserBookInfoListByUidOfPage(PageEntity page,UserBook ub){
        List<UserBookInfo> ubList = pageQueryService.queryForListPage("UserBook.findUserBookInfoListByUidOfPage",ub,page);
        if (ubList == null){
            ubList = new ArrayList<UserBookInfo>();
        }
        return ubList;
    }

    public ReturnResult updateDeleteStatusById(UserBook ub){
        ReturnResult r = new ReturnResult();
        int bol = 0;
        try{
            bol = sqlSessionTemplate.update("UserBook.updateDeleteStatusById",ub);
            if(bol>0){
                bol = 2;
                r.setMsg(String.valueOf(ub.getId()));
            }else{
                bol = -3;
                r.setMsg("updateDeleteStatusById error");
            }
        }catch (Exception e){
            bol = -2;
            logger.error(e);
            r.setMsg("updateDeleteStatusById error");
        }
        r.setCode(bol);
        return r;
    }

    public ReturnResult updateReadStatusById(UserBook ub){
        ReturnResult r = new ReturnResult();
        int bol = 0;
        ub.setUpdatetime(DateUtillZ.getTimestampOfNow());
        try{
            bol = sqlSessionTemplate.update("UserBook.updateReadStatusById",ub);
            if(bol>0){
                bol = 2;
                r.setMsg(String.valueOf(ub.getId()));
            }else{
                bol = -3;
                r.setMsg("updateReadStatusById error");
            }
        }catch (Exception e){
            bol = -2;
            logger.error(e);
            r.setMsg("updateReadStatusById error");
        }
        r.setCode(bol);
        return r;
    }

    public UserBookInfo findUserBookInfoByUidAndIsBn(UserBook ub){
        return sqlSessionTemplate.selectOne("UserBook.findUserBookInfoByUidAndIsBn",ub);
    }

    public Integer findUserBookHasOrNot(UserBook ub){
        return sqlSessionTemplate.selectOne("UserBook.findUserBookHasOrNot",ub);
    }

    public List<UserBookCount> findByUserBookNum(String uuid){
        return sqlSessionTemplate.selectList("UserBook.findByUserBookNum",uuid);
    }

    public ReturnResult findByUserBookNumInterface(String uuid){
        ReturnResult r = new ReturnResult();
        List<UserBookCount> ubs = findByUserBookNum(uuid);
        int ubsLen = ubs.size();
        int countNum = 0;
        for (int i =0;i<ubsLen;i++){
            countNum += ubs.get(i).getCountNum();
        }
        UserBookCount ub = new UserBookCount();
        ub.setCountNum(countNum);
        ub.setReadStatus(4);
        ubs.add(ub);
        JSONArray j = new JSONArray(ubs);
        r.setCode(1);
        r.setMsg("success");
        r.setResult(j);
        return r;
    }




    /**
     * 查询并添加用户书籍
     * @param ub
     * @return
     */
    public ReturnResult addReadStatusByUidAndBid(UserBook ub){
        ReturnResult r= new ReturnResult();
        String b = bookInfoService.findIsbnByBid(ub.getBid());
        if (b==null){
            r.setCode(-1);
            r.setMsg("暂无本书信息");
            return r;
        }
        //添加
        ub.setIsbn(b);
        r = addUserBook(ub);
        return r;
    }
    /**
     * 通过关键字查询图书信息
     * @param bs
     * @return
     */
    public ReturnResult getBooksByTagAndPage(BookSearch bs){
        ReturnResult r = new ReturnResult();
//        List<BookInfo> bookInfos = bookInfoService.findBookInfoFromDouBanByKyesTest(bs);
        List<BookInfo> bookInfos = bookInfoService.findBookInfoFromDouBanByKyes(bs);
        BookInfo bookInfo = null;
        List<BookInfo> bookInfoForList = new ArrayList<BookInfo>();
        UserBook ub = new UserBook();
        int flg = 0;
        r.setCode(-1);
        r.setMsg("暂未查到相关书籍");
        if (bookInfos!=null){
            int bookLen = bookInfos.size();
            for (int i=0;i<bookLen;i++){
                bookInfo = new BookInfo();
                if (bookInfo.getBid()==null){
                    //查询bid
                    bookInfos.get(i).setBid(bookInfoService.findBidByIsbn(bookInfos.get(i).getIsbn()));
                }
                bookInfo.setBid(bookInfos.get(i).getBid());
                bookInfo.setTitle(bookInfos.get(i).getTitle());
                bookInfo.setAuthor(bookInfos.get(i).getAuthor());
                bookInfo.setHeadImage(bookInfos.get(i).getHeadImage());
                //查询ub中是否存在
                ub.setUuid(bs.getUuid());
                ub.setBid(bookInfos.get(i).getBid());
                flg = findUserBookHasOrNot(ub);
                bookInfo.setHasStatus(flg);
                bookInfoForList.add(bookInfo);
            }
            JSONArray jsonArray = new JSONArray(bookInfoForList);
            JSONObject pageObj = new JSONObject(bs);
            pageObj.remove("uuid");
            JSONObject result = new JSONObject();
            result.put("pageInfo",pageObj);
            result.put("books",jsonArray);
            r.setCode(1);
            r.setMsg("success");
            r.setResult(result);
        }
        return r;
    }


    /**
     * 通过bid查询书籍详细信息
     * @param ub
     * @return
     */
    public ReturnResult findBookInfoByBidInf(UserBook ub){
        ReturnResult r= new ReturnResult();
        BookInfo b = bookInfoService.findBookInfoByBid(ub.getBid());
        if (b==null){
            r.setCode(-1);
            r.setMsg("暂无本书信息");
            return r;
        }
        Integer flag = findUserBookHasOrNot(ub);
        b.setHasStatus(flag);
        JSONObject j = new JSONObject(b);
        r.setCode(1);
        r.setMsg("success");
        r.setResult(j);
        return r;
    }



    /**
     * 通过扫码查询图书信息以及用户拥有信息。
     * @param ub
     * @return
     */
    public ReturnResult findUserIsHasBook(UserBook ub){
        ReturnResult r = new ReturnResult();
        UserBookInfo bookInfo = findUserBookInfoByUidAndIsBn(ub);


        if (bookInfo ==null){
            r.setCode(1);
            r.setMsg("add success");
            //  //未添加过，查询是否存在该书籍，先数据库后豆瓣
            BookInfo  bookInfoAll = bookInfoService.findBookInfoBaseByIsbnORNot(ub.getIsbn());
            if (bookInfoAll != null){
                ub.setBid(bookInfoAll.getBid());
                ub.setDeleteStatus(1);
                ub.setReadStatus(1);
                addUserBook(ub);
                bookInfo = new UserBookInfo();
                bookInfo.setBid(bookInfoAll.getBid());
                bookInfo.setTitle(bookInfoAll.getTitle());
                bookInfo.setAuthor(bookInfoAll.getAuthor());
                bookInfo.setHeadImage(bookInfoAll.getHeadImage());

                bookInfo.setId(ub.getId());
                bookInfo.setReadStatus(1);
                JSONObject jsonObject = new JSONObject(bookInfo);
                r.setResult(jsonObject);
            }else{
                //书籍不存在
                r.setCode(-2);
                r.setMsg("书籍不存在");
            }
        }else{
            // //添加过，返回书籍基本信息
              r.setMsg("find success");
              r.setCode(2);
//            if (bookInfo.getDeleteStatus()<0){
                ub.setId(bookInfo.getId());
                ub.setDeleteStatus(1);
                updateDeleteStatusById(ub);
//            }
            JSONObject jsonObject = new JSONObject(bookInfo);
            jsonObject.remove("deleteStatus");
            r.setResult(jsonObject);
        }
        return r;
    }
}
