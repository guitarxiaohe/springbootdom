package com.xiaohe.web.controller.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import com.xiaohe.common.core.domain.AjaxResult;
import com.xiaohe.common.core.domain.BaseEntity;
import com.xiaohe.common.core.page.TableDataInfo;
import com.xiaohe.system.service.IOperatorUserFillService;

/**
 * 统一为返回结果补充创建人/修改人用户对象。
 *
 * @author xiaohe
 */
@RestControllerAdvice(basePackages = "com.xiaohe.web.controller")
public class AuditUserResponseAdvice implements ResponseBodyAdvice<Object>
{
    @Autowired
    private IOperatorUserFillService operatorUserFillService;

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType)
    {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
            Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
            ServerHttpResponse response)
    {
        enrichBody(body);
        return body;
    }

    private void enrichBody(Object body)
    {
        if (body == null)
        {
            return;
        }
        if (body instanceof AjaxResult)
        {
            enrichBody(((AjaxResult) body).get(AjaxResult.DATA_TAG));
            return;
        }
        if (body instanceof TableDataInfo)
        {
            enrichBody(((TableDataInfo) body).getRows());
            return;
        }
        if (body instanceof BaseEntity)
        {
            operatorUserFillService.fillBaseEntityAuditUser((BaseEntity) body);
            return;
        }
        if (body instanceof Map)
        {
            enrichMap((Map<String, Object>) body);
            return;
        }
        if (body instanceof List<?>)
        {
            enrichList((List<?>) body);
        }
    }

    private void enrichList(List<?> list)
    {
        if (list == null || list.isEmpty())
        {
            return;
        }
        List<BaseEntity> entities = new ArrayList<>();
        List<Map<String, Object>> rows = new ArrayList<>();
        for (Object item : list)
        {
            if (item instanceof BaseEntity)
            {
                entities.add((BaseEntity) item);
            }
            else if (item instanceof Map)
            {
                rows.add((Map<String, Object>) item);
            }
        }
        if (!entities.isEmpty())
        {
            operatorUserFillService.fillBaseEntityAuditUsers(entities);
        }
        if (!rows.isEmpty())
        {
            String createByColumn = findColumn(rows, "createBy", "create_by", "createdBy", "created_by");
            String updateByColumn = findColumn(rows, "updateBy", "update_by", "updatedBy", "updated_by");
            if (createByColumn != null || updateByColumn != null)
            {
                operatorUserFillService.fillAuditUserMaps(rows, createByColumn, updateByColumn);
            }
        }
    }

    private String findColumn(List<Map<String, Object>> rows, String... candidates)
    {
        for (Map<String, Object> row : rows)
        {
            for (String candidate : candidates)
            {
                if (row.containsKey(candidate))
                {
                    return candidate;
                }
            }
        }
        return null;
    }

    private void enrichMap(Map<String, Object> row)
    {
        if (row == null || row.isEmpty())
        {
            return;
        }
        String createByColumn = findColumn(java.util.Collections.singletonList(row), "createBy", "create_by",
                "createdBy", "created_by");
        String updateByColumn = findColumn(java.util.Collections.singletonList(row), "updateBy", "update_by",
                "updatedBy", "updated_by");
        if (createByColumn != null || updateByColumn != null)
        {
            operatorUserFillService.fillAuditUserMaps(java.util.Collections.singletonList(row), createByColumn,
                    updateByColumn);
        }
    }
}
