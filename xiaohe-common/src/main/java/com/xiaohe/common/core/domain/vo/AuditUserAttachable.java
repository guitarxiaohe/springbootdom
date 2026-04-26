package com.xiaohe.common.core.domain.vo;

/**
 * 具备 Long 类型创建人/修改人主键，并可在返回结果中挂载 {@link OperatorUserVo} 的实体约定。
 *
 * @author xiaohe
 */
public interface AuditUserAttachable
{
    Long getCreatedBy();

    Long getUpdatedBy();

    OperatorUserVo getCreateUser();

    void setCreateUser(OperatorUserVo user);

    OperatorUserVo getUpdateUser();

    void setUpdateUser(OperatorUserVo user);
}
