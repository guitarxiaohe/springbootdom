package com.xiaohe.system.service;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import com.xiaohe.common.core.domain.BaseEntity;
import com.xiaohe.common.core.domain.vo.AuditUserAttachable;
import com.xiaohe.common.core.domain.vo.OperatorUserVo;

/**
 * 将创建人/修改人用户ID批量解析为 {@link OperatorUserVo} 并写回实体或 Map 行。
 *
 * @author xiaohe
 */
public interface IOperatorUserFillService
{
    /**
     * 列表：实体实现 {@link AuditUserAttachable}（Long 类型 createdBy / updatedBy）
     */
    void fillAuditUsers(List<? extends AuditUserAttachable> list);

    /**
     * 单个对象：同 {@link #fillAuditUsers(List)}
     */
    void fillAuditUser(AuditUserAttachable one);

    /**
     * 列表：BaseEntity 上的 String 类型 createBy/updateBy（存的是用户账号）
     */
    void fillBaseEntityAuditUsers(List<? extends BaseEntity> list);

    /**
     * 单个对象：同 {@link #fillBaseEntityAuditUsers(List)}
     */
    void fillBaseEntityAuditUser(BaseEntity one);

    /**
     * 列表：自定义 getter/setter，适用于未实现 {@link AuditUserAttachable} 的实体或 DTO
     */
    <T> void fillAuditUsers(List<T> list,
            Function<T, Long> getCreatedBy,
            BiConsumer<T, OperatorUserVo> setCreateUser,
            Function<T, Long> getUpdatedBy,
            BiConsumer<T, OperatorUserVo> setUpdateUser);

    /**
     * 动态 Map 行（如通用分页查询）：按列名读取创建人/修改人 ID，写入键 createUser、updateUser
     *
     * @param createByColumn 列名，如 created_by；为 null 则忽略创建人
     * @param updateByColumn 列名，如 updated_by；为 null 则忽略修改人
     */
    void fillAuditUserMaps(List<Map<String, Object>> rows, String createByColumn, String updateByColumn);
}
