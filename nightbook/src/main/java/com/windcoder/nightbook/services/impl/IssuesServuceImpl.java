package com.windcoder.nightbook.services.impl;

import com.windcoder.common.utills.DateUtillZ;
import com.windcoder.common.utills.ReturnResult;
import com.windcoder.nightbook.entity.Issues;
import com.windcoder.nightbook.services.IISsuesService;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Description:
 * User: WindCoder
 * Date: 2017-10-26
 * Time: 0:22 上午
 */
@Service("issuesServuce")
public class IssuesServuceImpl implements IISsuesService{
    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    public ReturnResult addIssues(Issues i){
        ReturnResult r = new ReturnResult();
        i.setCreatetime(DateUtillZ.getTimestampOfNow());
        int bol = 0;
        try {
            bol = sqlSessionTemplate.insert("Issues.addIssues", i);
            if (bol>0){
                r.setResult(i.getId());
                r.setMsg("Issues success");
            }else{
                bol = -2;
                r.setMsg("Issues error");
            }
        }catch (Exception e){
            bol = -1;
//            logger.error(e);
            e.printStackTrace();
            r.setMsg("Issues error");
        }
        r.setCode(bol);
        return r;
    }
}
