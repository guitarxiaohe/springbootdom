package com.xiaohe.web.domain.Entity;

import com.xiaohe.common.annotation.Excel;
import com.xiaohe.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 笔记分类对象 sys_note_category
 * 
 * @author xiaohe
 * @date 2025-01-07
 */
@ApiModel("笔记分类")
public class SysNoteCategory extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 分类ID */
    @ApiModelProperty("分类ID")
    private Long categoryId;

    /** 分类名称 */
    @ApiModelProperty(value = "分类名称", required = true)
    @Excel(name = "分类名称")
    private String categoryName;

    /** 分类编码 */
    @ApiModelProperty(value = "分类编码", required = true, example = "react")
    @Excel(name = "分类编码")
    private String categoryCode;

    /** 分类图标 */
    @ApiModelProperty(value = "分类图标", example = "⚛️")
    @Excel(name = "分类图标")
    private String categoryIcon;

    /** 分类描述 */
    @ApiModelProperty("分类描述")
    @Excel(name = "分类描述")
    private String categoryDesc;

    /** 排序 */
    @ApiModelProperty("显示顺序")
    @Excel(name = "排序")
    private Integer orderNum;

    /** 状态（0正常 1停用） */
    @ApiModelProperty("状态（0正常 1停用）")
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    @ApiModelProperty(hidden = true)
    private String delFlag;

    public void setCategoryId(Long categoryId) 
    {
        this.categoryId = categoryId;
    }

    public Long getCategoryId() 
    {
        return categoryId;
    }

    public void setCategoryName(String categoryName) 
    {
        this.categoryName = categoryName;
    }

    public String getCategoryName() 
    {
        return categoryName;
    }

    public void setCategoryCode(String categoryCode) 
    {
        this.categoryCode = categoryCode;
    }

    public String getCategoryCode() 
    {
        return categoryCode;
    }

    public void setCategoryIcon(String categoryIcon) 
    {
        this.categoryIcon = categoryIcon;
    }

    public String getCategoryIcon() 
    {
        return categoryIcon;
    }

    public void setCategoryDesc(String categoryDesc) 
    {
        this.categoryDesc = categoryDesc;
    }

    public String getCategoryDesc() 
    {
        return categoryDesc;
    }

    public void setOrderNum(Integer orderNum) 
    {
        this.orderNum = orderNum;
    }

    public Integer getOrderNum() 
    {
        return orderNum;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    public void setDelFlag(String delFlag) 
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag() 
    {
        return delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("categoryId", getCategoryId())
            .append("categoryName", getCategoryName())
            .append("categoryCode", getCategoryCode())
            .append("categoryIcon", getCategoryIcon())
            .append("categoryDesc", getCategoryDesc())
            .append("orderNum", getOrderNum())
            .append("status", getStatus())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}

