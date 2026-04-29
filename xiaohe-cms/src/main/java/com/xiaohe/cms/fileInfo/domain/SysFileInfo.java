package com.xiaohe.cms.fileInfo.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.xiaohe.common.annotation.Excel;
import com.xiaohe.common.core.domain.BaseEntity;

/**
 * 文件管理对象 sys_file_info
 * 
 * @author xiaohe
 * @date 2021-12-29
 */
public class SysFileInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 文件主键id */
    private Long fileId;

    /** 文件名称 */
    @Excel(name = "文件名称")
    private String fileOriginName;

    /** 文件类型，例如txt */
    @Excel(name = "文件类型，例如txt")
    private String fileSuffix;

    /** 文件大小 */
    @Excel(name = "文件大小")
    private String fileSizeInfo;

    /** 存储文件名称 */
    @Excel(name = "存储文件名称")
    private String fileObjectName;

    /** 存储路径 */
    @Excel(name = "存储路径")
    private String fileUrl;

    /** 是否删除：Y-被删除，N-未删除 */
    private String delFlag;

    public void setFileId(Long fileId) 
    {
        this.fileId = fileId;
    }

    public Long getFileId() 
    {
        return fileId;
    }
    public void setFileOriginName(String fileOriginName) 
    {
        this.fileOriginName = fileOriginName;
    }

    public String getFileOriginName() 
    {
        return fileOriginName;
    }
    public void setFileSuffix(String fileSuffix) 
    {
        this.fileSuffix = fileSuffix;
    }

    public String getFileSuffix() 
    {
        return fileSuffix;
    }
    public void setFileSizeInfo(String fileSizeInfo) 
    {
        this.fileSizeInfo = fileSizeInfo;
    }

    public String getFileSizeInfo() 
    {
        return fileSizeInfo;
    }
    public void setFileObjectName(String fileObjectName) 
    {
        this.fileObjectName = fileObjectName;
    }

    public String getFileObjectName() 
    {
        return fileObjectName;
    }
    public void setfileUrl(String fileUrl) 
    {
        this.fileUrl = fileUrl;
    }

    public String getfileUrl() 
    {
        return fileUrl;
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
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("fileId", getFileId())
            .append("fileOriginName", getFileOriginName())
            .append("fileSuffix", getFileSuffix())
            .append("fileSizeInfo", getFileSizeInfo())
            .append("fileObjectName", getFileObjectName())
            .append("fileUrl", getfileUrl())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
