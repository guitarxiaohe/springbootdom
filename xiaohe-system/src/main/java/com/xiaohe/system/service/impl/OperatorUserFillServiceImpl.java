package com.xiaohe.system.service.impl;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xiaohe.common.core.domain.BaseEntity;
import com.xiaohe.common.core.domain.vo.AuditUserAttachable;
import com.xiaohe.common.core.domain.vo.OperatorUserVo;
import com.xiaohe.common.utils.StringUtils;
import com.xiaohe.system.service.IOperatorUserFillService;
import com.xiaohe.system.service.ISysUserService;

/**
 * 创建人/修改人用户信息填充
 *
 * @author xiaohe
 */
@Service
public class OperatorUserFillServiceImpl implements IOperatorUserFillService
{
    @Autowired
    private ISysUserService userService;

    @Override
    public void fillAuditUsers(List<? extends AuditUserAttachable> list)
    {
        if (list == null || list.isEmpty())
        {
            return;
        }
        Set<Long> ids = new HashSet<>();
        for (AuditUserAttachable row : list)
        {
            if (row.getCreatedBy() != null)
            {
                ids.add(row.getCreatedBy());
            }
            if (row.getUpdatedBy() != null)
            {
                ids.add(row.getUpdatedBy());
            }
        }
        Map<Long, OperatorUserVo> map = userService.selectOperatorUserMapByIds(ids);
        for (AuditUserAttachable row : list)
        {
            row.setCreateUser(row.getCreatedBy() == null ? null : map.get(row.getCreatedBy()));
            row.setUpdateUser(row.getUpdatedBy() == null ? null : map.get(row.getUpdatedBy()));
        }
    }

    @Override
    public void fillAuditUser(AuditUserAttachable one)
    {
        if (one == null)
        {
            return;
        }
        fillAuditUsers(Collections.singletonList(one));
    }

    @Override
    public void fillBaseEntityAuditUsers(List<? extends BaseEntity> list)
    {
        if (list == null || list.isEmpty())
        {
            return;
        }
        Set<String> userNames = new HashSet<>();
        for (BaseEntity row : list)
        {
            if (StringUtils.isNotEmpty(row.getCreateBy()))
            {
                userNames.add(row.getCreateBy().trim());
            }
            if (StringUtils.isNotEmpty(row.getUpdateBy()))
            {
                userNames.add(row.getUpdateBy().trim());
            }
        }
        Map<String, OperatorUserVo> map = userService.selectOperatorUserMapByUserNames(userNames);
        for (BaseEntity row : list)
        {
            row.setCreateUser(StringUtils.isNotEmpty(row.getCreateBy()) ? map.get(row.getCreateBy().trim()) : null);
            row.setUpdateUser(StringUtils.isNotEmpty(row.getUpdateBy()) ? map.get(row.getUpdateBy().trim()) : null);
        }
    }

    @Override
    public void fillBaseEntityAuditUser(BaseEntity one)
    {
        if (one == null)
        {
            return;
        }
        fillBaseEntityAuditUsers(Collections.singletonList(one));
    }

    @Override
    public <T> void fillAuditUsers(List<T> list, Function<T, Long> getCreatedBy,
            BiConsumer<T, OperatorUserVo> setCreateUser, Function<T, Long> getUpdatedBy,
            BiConsumer<T, OperatorUserVo> setUpdateUser)
    {
        if (list == null || list.isEmpty())
        {
            return;
        }
        Set<Long> ids = new HashSet<>();
        for (T row : list)
        {
            Long c = getCreatedBy.apply(row);
            if (c != null)
            {
                ids.add(c);
            }
            Long u = getUpdatedBy.apply(row);
            if (u != null)
            {
                ids.add(u);
            }
        }
        Map<Long, OperatorUserVo> map = userService.selectOperatorUserMapByIds(ids);
        for (T row : list)
        {
            Long c = getCreatedBy.apply(row);
            setCreateUser.accept(row, c == null ? null : map.get(c));
            Long u = getUpdatedBy.apply(row);
            setUpdateUser.accept(row, u == null ? null : map.get(u));
        }
    }

    @Override
    public void fillAuditUserMaps(List<Map<String, Object>> rows, String createByColumn, String updateByColumn)
    {
        if (rows == null || rows.isEmpty())
        {
            return;
        }
        Set<Long> ids = new HashSet<>();
        Set<String> userNames = new HashSet<>();
        for (Map<String, Object> row : rows)
        {
            if (createByColumn != null)
            {
                collectIdentity(row.get(createByColumn), ids, userNames);
            }
            if (updateByColumn != null)
            {
                collectIdentity(row.get(updateByColumn), ids, userNames);
            }
        }
        Map<Long, OperatorUserVo> map = userService.selectOperatorUserMapByIds(ids);
        Map<String, OperatorUserVo> nameMap = userService.selectOperatorUserMapByUserNames(userNames);
        for (Map<String, Object> row : rows)
        {
            if (createByColumn != null)
            {
                row.put("createUser", resolveUser(row.get(createByColumn), map, nameMap));
            }
            if (updateByColumn != null)
            {
                row.put("updateUser", resolveUser(row.get(updateByColumn), map, nameMap));
            }
        }
    }

    private static void collectIdentity(Object value, Set<Long> ids, Set<String> userNames)
    {
        Long id = toLong(value);
        if (id != null)
        {
            ids.add(id);
            return;
        }
        if (value instanceof String && StringUtils.isNotEmpty((String) value))
        {
            userNames.add(((String) value).trim());
        }
    }

    private static OperatorUserVo resolveUser(Object value, Map<Long, OperatorUserVo> idMap,
            Map<String, OperatorUserVo> nameMap)
    {
        Long id = toLong(value);
        if (id != null)
        {
            return idMap.get(id);
        }
        if (value instanceof String && StringUtils.isNotEmpty((String) value))
        {
            return nameMap.get(((String) value).trim());
        }
        return null;
    }

    private static Long toLong(Object v)
    {
        if (v == null)
        {
            return null;
        }
        if (v instanceof Long)
        {
            return (Long) v;
        }
        if (v instanceof Integer)
        {
            return ((Integer) v).longValue();
        }
        if (v instanceof BigDecimal)
        {
            return ((BigDecimal) v).longValue();
        }
        if (v instanceof Number)
        {
            return ((Number) v).longValue();
        }
        if (v instanceof String && !((String) v).isEmpty())
        {
            try
            {
                return Long.parseLong((String) v);
            }
            catch (NumberFormatException ignored)
            {
                return null;
            }
        }
        return null;
    }
}
