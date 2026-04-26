package com.xiaohe.web.domain.Entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.xiaohe.common.annotation.Excel;
import com.xiaohe.common.core.domain.BaseEntity;
import lombok.Data;

@Data
@TableName("project_type")
public class ProjectType extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** id */
    private Long projectTypeId;

    /** 项目名称 */
    @Excel(name = "项目名称")
    private String projectTypeName;

    /** 项目类型（1：vue，2react，3：uniapp，4：three） */
    @Excel(name = "项目类型", readConverterExp = "1=：vue，2react，3：uniapp，4：three")
    private String projectType;
//
//    /** 项目开始时间 */
//    @JsonFormat(pattern = "yyyy-MM-dd")
//    @Excel(name = "项目开始时间", width = 30, dateFormat = "yyyy-MM-dd")
//    private Date projectStartTime;
//
//    /** 项目结束时间 */
//    @JsonFormat(pattern = "yyyy-MM-dd")
//    @Excel(name = "项目结束时间", width = 30, dateFormat = "yyyy-MM-dd")
//    private Date projectEndTime;
}
