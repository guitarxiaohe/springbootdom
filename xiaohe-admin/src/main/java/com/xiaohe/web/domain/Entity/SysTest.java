package com.xiaohe.web.domain.Entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.xiaohe.common.core.domain.BaseEntity;
import lombok.Data;

import javax.validation.constraints.NotBlank;


@Data
@TableName("sys_test")
public class SysTest  extends BaseEntity {

    @TableId(value = "test_id")
    @JsonFormat(shape =JsonFormat.Shape.STRING )
    private Long testId;

    @NotBlank(message = "名称必填")
    @TableField("test_name")
    private String testName;
//
//    @TableField("create_time")
//    private Date createTime;
//
//    @TableField("create_by")
//    private String createBy;
//
//    @TableField("update_time")
//    private Date updateTime;
//
//    // 注意：这里修正了字段名，数据库中字段名是 updata_by，但建议统一为 update_by
//    @TableField("updata_by")
//    private String updateBy;
}
