package com.xiaohe.web.domain.Entity;

import com.xiaohe.common.annotation.Excel;
import com.xiaohe.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * 笔记对象 sys_note
 * 
 * @author xiaohe
 * @date 2025-01-07
 */
@ApiModel("笔记")
public class SysNote extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 笔记ID */
    @ApiModelProperty("笔记ID")
    private Long noteId;

    /** 父笔记ID（用于目录结构，0表示根节点） */
    @ApiModelProperty(value = "父笔记ID", example = "0")
    @Excel(name = "父笔记ID")
    private Long parentId;

    /** 分类ID */
    @ApiModelProperty(value = "分类ID", required = true)
    @Excel(name = "分类ID")
    private Long categoryId;

    /** 笔记标题 */
    @ApiModelProperty(value = "笔记标题", required = true)
    @Excel(name = "笔记标题")
    private String noteTitle;

    /** 笔记内容 */
    @ApiModelProperty("笔记内容（支持Markdown）")
    @Excel(name = "笔记内容")
    private String noteContent;

    /** 笔记类型（1目录 2笔记） */
    @ApiModelProperty(value = "笔记类型", example = "2", allowableValues = "1,2", notes = "1=目录 2=笔记")
    @Excel(name = "笔记类型", readConverterExp = "1=目录,2=笔记")
    private String noteType;

    /** 笔记标签 */
    @ApiModelProperty(value = "笔记标签", example = "React,Hooks,useState")
    @Excel(name = "笔记标签")
    private String noteTags;

    /** 排序 */
    @ApiModelProperty("显示顺序")
    @Excel(name = "排序")
    private Integer orderNum;

    /** 状态（0正常 1停用） */
    @ApiModelProperty("状态（0正常 1停用）")
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 是否公开（0私有 1公开） */
    @ApiModelProperty("是否公开（0私有 1公开）")
    @Excel(name = "是否公开", readConverterExp = "0=私有,1=公开")
    private String isPublic;

    /** 阅读次数 */
    @ApiModelProperty("阅读次数")
    @Excel(name = "阅读次数")
    private Long readCount;

    /** 删除标志（0代表存在 2代表删除） */
    @ApiModelProperty(hidden = true)
    private String delFlag;

    /** 分类名称 */
    @ApiModelProperty("分类名称")
    private String categoryName;

    /** 父笔记标题 */
    @ApiModelProperty("父笔记标题")
    private String parentTitle;

    /** 子节点 */
    @ApiModelProperty("子节点列表")
    private List<SysNote> children = new ArrayList<>();

    public void setNoteId(Long noteId) 
    {
        this.noteId = noteId;
    }

    public Long getNoteId() 
    {
        return noteId;
    }

    public void setParentId(Long parentId) 
    {
        this.parentId = parentId;
    }

    public Long getParentId() 
    {
        return parentId;
    }

    public void setCategoryId(Long categoryId) 
    {
        this.categoryId = categoryId;
    }

    public Long getCategoryId() 
    {
        return categoryId;
    }

    public void setNoteTitle(String noteTitle) 
    {
        this.noteTitle = noteTitle;
    }

    public String getNoteTitle() 
    {
        return noteTitle;
    }

    public void setNoteContent(String noteContent) 
    {
        this.noteContent = noteContent;
    }

    public String getNoteContent() 
    {
        return noteContent;
    }

    public void setNoteType(String noteType) 
    {
        this.noteType = noteType;
    }

    public String getNoteType() 
    {
        return noteType;
    }

    public void setNoteTags(String noteTags) 
    {
        this.noteTags = noteTags;
    }

    public String getNoteTags() 
    {
        return noteTags;
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

    public void setIsPublic(String isPublic) 
    {
        this.isPublic = isPublic;
    }

    public String getIsPublic() 
    {
        return isPublic;
    }

    public void setReadCount(Long readCount) 
    {
        this.readCount = readCount;
    }

    public Long getReadCount() 
    {
        return readCount;
    }

    public void setDelFlag(String delFlag) 
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag() 
    {
        return delFlag;
    }

    public String getCategoryName() 
    {
        return categoryName;
    }

    public void setCategoryName(String categoryName) 
    {
        this.categoryName = categoryName;
    }

    public String getParentTitle() 
    {
        return parentTitle;
    }

    public void setParentTitle(String parentTitle) 
    {
        this.parentTitle = parentTitle;
    }

    public List<SysNote> getChildren() 
    {
        return children;
    }

    public void setChildren(List<SysNote> children) 
    {
        this.children = children;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("noteId", getNoteId())
            .append("parentId", getParentId())
            .append("categoryId", getCategoryId())
            .append("noteTitle", getNoteTitle())
            .append("noteContent", getNoteContent())
            .append("noteType", getNoteType())
            .append("noteTags", getNoteTags())
            .append("orderNum", getOrderNum())
            .append("status", getStatus())
            .append("isPublic", getIsPublic())
            .append("readCount", getReadCount())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}

