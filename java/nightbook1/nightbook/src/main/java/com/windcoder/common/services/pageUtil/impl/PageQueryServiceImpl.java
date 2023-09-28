package com.windcoder.common.services.pageUtil.impl;





import com.windcoder.common.entity.PageEntity;
import com.windcoder.common.entity.PageOL;
import com.windcoder.common.services.pageUtil.IPageQueryService;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wind on 2016/8/24.
 */
@Service("pageQueryService")
public class PageQueryServiceImpl implements IPageQueryService {
    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;
    @Override
    public <T> List<T> queryForListPage(String sqlKey, Object params, PageEntity page) {
        /**
         * 分页时需要2个sql。在正常sql后面加pageCount为计算count的sql
         * 如：customre.getcustomreByTime必须命名为customre.getcustomreByTimeCount
         */

        // 查询总行数
        Map<String, Object> map = new HashMap<String, Object>();
        if(params!=null){
            map.put("e", params);
        }
        PageOL pageOL = new PageOL();
        pageOL.setOffsetPara((page.getCurrentPage() - 1) * page.getPageSize());
        pageOL.setLimitPara(page.getPageSize());
        map.put("page", pageOL);

        Integer objectscount = sqlSessionTemplate.selectOne(sqlKey + "Count", map);

        if (objectscount == null || objectscount == 0) {
            page.setTotalResultSize(0);
            int totalPageSize = 0;
            page.setTotalPageSize(totalPageSize);
            return null;
        } else {
            page.setTotalResultSize(objectscount);
            int totalPageSize = (page.getTotalResultSize() - 1) / page.getPageSize() + 1;
            page.setTotalPageSize(totalPageSize);
            return sqlSessionTemplate.selectList(sqlKey, map);
        }

    }
}
