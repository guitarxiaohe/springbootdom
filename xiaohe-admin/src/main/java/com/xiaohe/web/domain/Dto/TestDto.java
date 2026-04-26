package com.xiaohe.web.domain.Dto;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xiaohe.common.core.domain.BaseEntity;
import lombok.Data;

import java.util.Date;

@Data
@TableName("sys_test")
public class TestDto  extends BaseEntity {


    @TableId
    private Long testId;

    @TableField("test_name")
    private String testName;

    @TableField("create_time")
    private Date createTime;

    @TableField("create_by")
    private String createBy;

    @TableField("update_time")
    private Date updateTime;

    // 注意：这里修正了字段名，数据库中字段名是 updata_by，但建议统一为 update_by
    @TableField("updata_by")
    private String updateBy;
}
