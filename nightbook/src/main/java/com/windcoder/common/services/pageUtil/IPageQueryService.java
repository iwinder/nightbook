package com.windcoder.common.services.pageUtil;









import com.windcoder.common.entity.PageEntity;

import java.util.List;

/**
 * Created by wind on 2016/8/24.
 */
public interface IPageQueryService {
    public <T> List<T> queryForListPage(String sqlKey, Object params, PageEntity page);
}
