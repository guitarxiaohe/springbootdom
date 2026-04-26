package com.xiaohe.cms.message.mapper;

import com.xiaohe.cms.message.domain.CmsMessageLike;

import java.util.List;

/**
 * @program: xiaohe
 * @Author: WangNing
 * @Description: 〈数据层〉
 * @Date: 2022/1/19 8:42
 */
public interface CmsMessageLikeMapper {
    /**
     * 查询列表
     */
    public List<CmsMessageLike> selectCmsMessageLikeList(CmsMessageLike cmsMessageLike);
    /**
     * 新增
     */
    public int addCmsMessageLike(CmsMessageLike cmsMessageLike);
    /**
     * 删除关联
     */
    public int deleteCmsMessageLike(CmsMessageLike cmsMessageLike);
}
