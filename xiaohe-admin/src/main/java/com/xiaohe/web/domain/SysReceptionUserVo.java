package com.xiaohe.web.domain;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.xiaohe.common.annotation.Excel;
import com.xiaohe.common.core.domain.BaseEntity;

/**
 * 接待人表对象 sys_reception_user
 *
 * @author GuitarXiaohe
 * @date 2023-09-27
 */
public class SysReceptionUserVo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long receptionId;

    /** 接待人名称 */
    @Excel(name = "接待人名称")
    private String receptionName;

    /** 接待人电话 */
    @Excel(name = "接待人电话")
    private String receptionPhone;

    public void setReceptionId(Long receptionId)
    {
        this.receptionId = receptionId;
    }

    public Long getReceptionId()
    {
        return receptionId;
    }
    public void setReceptionName(String receptionName)
    {
        this.receptionName = receptionName;
    }

    public String getReceptionName()
    {
        return receptionName;
    }
    public void setReceptionPhone(String receptionPhone)
    {
        this.receptionPhone = receptionPhone;
    }

    public String getReceptionPhone()
    {
        return receptionPhone;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("receptionId", getReceptionId())
                .append("receptionName", getReceptionName())
                .append("receptionPhone", getReceptionPhone())
                .toString();
    }
}