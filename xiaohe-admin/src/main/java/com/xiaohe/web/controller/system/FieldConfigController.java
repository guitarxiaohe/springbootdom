package com.xiaohe.web.controller.system;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.xiaohe.common.annotation.Log;
import com.xiaohe.common.config.XiaoHeConfig;
import com.xiaohe.common.core.controller.BaseController;
import com.xiaohe.common.core.domain.AjaxResult;
import com.xiaohe.common.core.page.PageDomain;
import com.xiaohe.common.core.page.TableDataInfo;
import com.xiaohe.common.core.page.TableSupport;
import com.xiaohe.common.enums.BusinessType;
import com.xiaohe.common.utils.StringUtils;
import com.xiaohe.common.utils.sql.SqlUtil;
import com.xiaohe.cms.fileInfo.domain.SysFileInfo;
import com.xiaohe.cms.fileInfo.service.ISysFileInfoService;
import com.xiaohe.system.domain.FieldConfig;
import com.xiaohe.system.domain.FieldConfigSortRequest;
import com.xiaohe.system.service.IDynamicEntityDataService;
import com.xiaohe.system.service.IFieldConfigService;
import com.xiaohe.system.service.IOperatorUserFillService;

/**
 * 字段配置Controller
 *
 * @author xiaohe
 */
@RestController
@RequestMapping("/system/fieldConfig")
public class FieldConfigController extends BaseController
{
    @Autowired
    private IFieldConfigService fieldConfigService;

    @Autowired
    private IDynamicEntityDataService dynamicEntityDataService;

    @Autowired
    private IOperatorUserFillService operatorUserFillService;

    @Autowired
    private ISysFileInfoService sysFileInfoService;

    /**
     * 查询字段配置列表
     */
    @PreAuthorize("@ss.hasPermi('system:fieldConfig:list')")
    @GetMapping("/list")
    public TableDataInfo list(FieldConfig fieldConfig)
    {
        startPage();
        List<FieldConfig> list = fieldConfigService.selectFieldConfigList(fieldConfig);
        operatorUserFillService.fillAuditUsers(list);
        return getDataTable(list);
    }

    /**
     * By entityKey: without pageNum/pageSize returns field_config rows;
     * with pagination resolves table via entity_config and returns business rows (query params and dataParams keys must be field_config.field_key).
     */
    @PreAuthorize("@ss.hasPermi('system:fieldConfig:list')")
    @GetMapping("/listByEntityKey/{entityKey}")
    public Object listByEntityKey(@PathVariable String entityKey)
    {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        List<FieldConfig> visibleFieldConfigs = selectVisibleFieldConfigs(entityKey);
        if (StringUtils.isNotNull(pageDomain.getPageNum()) && StringUtils.isNotNull(pageDomain.getPageSize()))
        {
            String orderBy = "";
            if (StringUtils.isNotEmpty(pageDomain.getOrderByColumn()))
            {
                String col = StringUtils.toUnderScoreCase(pageDomain.getOrderByColumn());
                if (dynamicEntityDataService.isAllowedOrderColumn(entityKey, col))
                {
                    orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
                }
            }
            List<Map<String, Object>> rows = dynamicEntityDataService.selectEntityRowList(entityKey,
                    pageDomain.getPageNum(), pageDomain.getPageSize(), orderBy, pageDomain.getReasonable());
            retainVisibleRowColumns(rows, visibleFieldConfigs);
            enrichDynamicRows(rows, visibleFieldConfigs);
            return getDataTable(rows);
        }
        operatorUserFillService.fillAuditUsers(visibleFieldConfigs);
        return AjaxResult.success(visibleFieldConfigs);
    }

    /**
     * Delete business rows by primary key id (comma-separated); table from entity_config.
     */
    @PreAuthorize("@ss.hasPermi('system:fieldConfig:remove')")
    @Log(title = "Dynamic entity rows", businessType = BusinessType.DELETE)
    @DeleteMapping("/delete/{entityKey}/{ids}")
    public AjaxResult deleteEntityRows(@PathVariable String entityKey, @PathVariable String ids)
    {
        if (StringUtils.isEmpty(ids))
        {
            return AjaxResult.error("Missing primary key ids");
        }
        String[] parts = ids.split(",");
        Long[] longIds = new Long[parts.length];
        try
        {
            for (int i = 0; i < parts.length; i++)
            {
                longIds[i] = Long.parseLong(parts[i].trim());
            }
        }
        catch (NumberFormatException ex)
        {
            return AjaxResult.error("Invalid id format");
        }
        return toAjax(dynamicEntityDataService.deleteEntityRowsByIds(entityKey, longIds));
    }

    /**
     * 查询单条业务数据
     */
    @PreAuthorize("@ss.hasPermi('system:fieldConfig:query')")
    @GetMapping("/data/{entityKey}/{id}")
    public AjaxResult getEntityRow(@PathVariable String entityKey, @PathVariable Long id)
    {
        Map<String, Object> row = dynamicEntityDataService.selectEntityRowById(entityKey, id);
        enrichDynamicRows(entityKey, Collections.singletonList(row));
        return AjaxResult.success(row);
    }

    /**
     * 新增业务数据
     */
    @PreAuthorize("@ss.hasPermi('system:fieldConfig:add')")
    @Log(title = "Dynamic entity row", businessType = BusinessType.INSERT)
    @PostMapping("/data/{entityKey}")
    public AjaxResult addEntityRow(@PathVariable String entityKey, @RequestBody Map<String, Object> data)
    {
        return AjaxResult.success(dynamicEntityDataService.insertEntityRow(entityKey, data, getUsername(), getUserId()));
    }

    /**
     * 修改业务数据
     */
    @PreAuthorize("@ss.hasPermi('system:fieldConfig:edit')")
    @Log(title = "Dynamic entity row", businessType = BusinessType.UPDATE)
    @PutMapping("/data/{entityKey}/{id}")
    public AjaxResult editEntityRow(@PathVariable String entityKey, @PathVariable Long id,
            @RequestBody Map<String, Object> data)
    {
        return AjaxResult.success(dynamicEntityDataService.updateEntityRow(entityKey, id, data, getUsername(), getUserId()));
    }

    /**
     * 删除单条业务数据
     */
    @PreAuthorize("@ss.hasPermi('system:fieldConfig:remove')")
    @Log(title = "Dynamic entity row", businessType = BusinessType.DELETE)
    @DeleteMapping("/data/{entityKey}/{id}")
    public AjaxResult deleteEntityRow(@PathVariable String entityKey, @PathVariable Long id)
    {
        return toAjax(dynamicEntityDataService.deleteEntityRowsByIds(entityKey, new Long[] { id }));
    }

    /**
     * 根据实体标识和字段标识查询字段配置
     */
    @PreAuthorize("@ss.hasPermi('system:fieldConfig:query')")
    @GetMapping("/getByEntityKeyAndFieldKey/{entityKey}/{fieldKey}")
    public AjaxResult getByEntityKeyAndFieldKey(@PathVariable String entityKey, @PathVariable String fieldKey)
    {
        FieldConfig fc = fieldConfigService.selectFieldConfigByEntityKeyAndFieldKey(entityKey, fieldKey);
        if (fc != null)
        {
            operatorUserFillService.fillAuditUser(fc);
        }
        return AjaxResult.success(fc);
    }

    /**
     * 获取字段配置详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:fieldConfig:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable Long id)
    {
        FieldConfig fc = fieldConfigService.selectFieldConfigById(id);
        if (fc != null)
        {
            operatorUserFillService.fillAuditUser(fc);
        }
        return AjaxResult.success(fc);
    }

    /**
     * 新增字段配置
     */
    @PreAuthorize("@ss.hasPermi('system:fieldConfig:add')")
    @Log(title = "字段配置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody FieldConfig fieldConfig)
    {
        fieldConfig.setCreatedBy(getUserId());
        fieldConfig.setCreatedTime(System.currentTimeMillis());
        return toAjax(fieldConfigService.insertFieldConfig(fieldConfig));
    }

    /**
     * 修改字段配置
     */
    @PreAuthorize("@ss.hasPermi('system:fieldConfig:edit')")
    @Log(title = "字段配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody FieldConfig fieldConfig)
    {
        fieldConfig.setUpdatedBy(getUserId());
        return toAjax(fieldConfigService.updateFieldConfig(fieldConfig));
    }

    /**
     * 删除字段配置
     */
    @PreAuthorize("@ss.hasPermi('system:fieldConfig:remove')")
    @Log(title = "字段配置", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(fieldConfigService.deleteFieldConfigByIds(ids));
    }

    /**
     * 批量更新排序
     */
    @PreAuthorize("@ss.hasPermi('system:fieldConfig:edit')")
    @Log(title = "字段配置", businessType = BusinessType.UPDATE)
    @PutMapping("/sort")
    public AjaxResult updateSort(@RequestBody FieldConfigSortRequest request)
    {
        return toAjax(fieldConfigService.updateSortBatch(request.getEntityKey(), request.getItems(), getUserId()));
    }

    private void enrichDynamicRows(String entityKey, List<Map<String, Object>> rows)
    {
        if (rows == null || rows.isEmpty() || StringUtils.isEmpty(entityKey))
        {
            return;
        }
        List<FieldConfig> fieldConfigs = selectVisibleFieldConfigs(entityKey);
        enrichDynamicRows(rows, fieldConfigs);
    }

    private void enrichDynamicRows(List<Map<String, Object>> rows, List<FieldConfig> fieldConfigs)
    {
        if (fieldConfigs == null || fieldConfigs.isEmpty())
        {
            return;
        }
        attachAuditUsers(rows, fieldConfigs);
        attachFileInfos(rows, fieldConfigs);
    }

    private List<FieldConfig> selectVisibleFieldConfigs(String entityKey)
    {
        FieldConfig query = new FieldConfig();
        query.setEntityKey(entityKey);
        query.setIsVisible(1);
        return fieldConfigService.selectFieldConfigList(query);
    }

    private void retainVisibleRowColumns(List<Map<String, Object>> rows, List<FieldConfig> fieldConfigs)
    {
        if (rows == null || rows.isEmpty())
        {
            return;
        }
        Set<String> visibleKeys = new LinkedHashSet<>();
        visibleKeys.add("id");
        if (fieldConfigs != null)
        {
            for (FieldConfig fieldConfig : fieldConfigs)
            {
                if (fieldConfig == null || StringUtils.isEmpty(fieldConfig.getFieldKey()))
                {
                    continue;
                }
                visibleKeys.add(fieldConfig.getFieldKey());
                visibleKeys.add(StringUtils.toCamelCase(fieldConfig.getFieldKey()));
            }
        }
        for (Map<String, Object> row : rows)
        {
            if (row == null || row.isEmpty())
            {
                continue;
            }
            row.entrySet().removeIf(entry -> !visibleKeys.contains(entry.getKey()));
        }
    }

    private void attachAuditUsers(List<Map<String, Object>> rows, List<FieldConfig> fieldConfigs)
    {
        if (rows == null || rows.isEmpty() || fieldConfigs == null || fieldConfigs.isEmpty())
        {
            return;
        }
        String createByColumn = null;
        String updateByColumn = null;
        for (FieldConfig fieldConfig : fieldConfigs)
        {
            if (fieldConfig == null || StringUtils.isEmpty(fieldConfig.getFieldKey()))
            {
                continue;
            }
            String fieldKey = resolveRowFieldKey(rows, fieldConfig.getFieldKey());
            if (StringUtils.isEmpty(fieldKey))
            {
                continue;
            }
            String fieldRole = normalizeFieldRole(fieldConfig.getFieldRole());
            if (createByColumn == null && ("createUser".equalsIgnoreCase(fieldRole)
                    || (StringUtils.isEmpty(fieldRole) && "by".equalsIgnoreCase(fieldConfig.getFieldType())
                    && isCreateAuditField(fieldKey))))
            {
                createByColumn = fieldKey;
            }
            if (updateByColumn == null && ("updateUser".equalsIgnoreCase(fieldRole)
                    || (StringUtils.isEmpty(fieldRole) && "by".equalsIgnoreCase(fieldConfig.getFieldType())
                    && isUpdateAuditField(fieldKey))))
            {
                updateByColumn = fieldKey;
            }
        }
        if (createByColumn != null || updateByColumn != null)
        {
            operatorUserFillService.fillAuditUserMaps(rows, createByColumn, updateByColumn);
        }
    }

    private void attachFileInfos(List<Map<String, Object>> rows, List<FieldConfig> fieldConfigs)
    {
        if (rows == null || rows.isEmpty() || fieldConfigs == null || fieldConfigs.isEmpty())
        {
            return;
        }
        List<String> fileFieldKeys = new ArrayList<>();
        for (FieldConfig fieldConfig : fieldConfigs)
        {
            if (fieldConfig == null || StringUtils.isEmpty(fieldConfig.getFieldKey())
                    || !isFileInfoField(fieldConfig))
            {
                continue;
            }
            String rowFieldKey = resolveRowFieldKey(rows, fieldConfig.getFieldKey());
            if (StringUtils.isNotEmpty(rowFieldKey))
            {
                fileFieldKeys.add(rowFieldKey);
            }
        }
        if (fileFieldKeys.isEmpty())
        {
            return;
        }
        Map<String, Map<String, Object>> fileInfoByCandidate = new LinkedHashMap<>();
        LinkedHashSet<String> fileUrls = new LinkedHashSet<>();
        for (Map<String, Object> row : rows)
        {
            if (row == null)
            {
                continue;
            }
            for (String fileFieldKey : fileFieldKeys)
            {
                Object fileUrlObj = row.get(fileFieldKey);
                if (fileUrlObj == null)
                {
                    continue;
                }
                fileUrls.addAll(resolveFileUrlCandidates(String.valueOf(fileUrlObj)));
            }
        }
        if (fileUrls.isEmpty())
        {
            return;
        }
        List<SysFileInfo> fileInfos = sysFileInfoService.selectSysFileInfoByFileUrls(new ArrayList<>(fileUrls));
        for (SysFileInfo fileInfo : fileInfos)
        {
            if (fileInfo == null || StringUtils.isEmpty(fileInfo.getfileUrl()))
            {
                continue;
            }
            fileInfoByCandidate.put(fileInfo.getfileUrl(), buildFileInfo(fileInfo));
        }
        if (fileInfoByCandidate.isEmpty())
        {
            return;
        }
        for (Map<String, Object> row : rows)
        {
            if (row == null)
            {
                continue;
            }
            for (String fileFieldKey : fileFieldKeys)
            {
                Object fileUrlObj = row.get(fileFieldKey);
                if (fileUrlObj == null)
                {
                    continue;
                }
                for (String candidate : resolveFileUrlCandidates(String.valueOf(fileUrlObj)))
                {
                    Map<String, Object> fileInfo = fileInfoByCandidate.get(candidate);
                    if (fileInfo != null)
                    {
                        row.put(buildFileInfoKey(fileFieldKey), fileInfo);
                        break;
                    }
                }
            }
        }
    }

    private String buildFileInfoKey(String fieldKey)
    {
        if ("fileUrl".equals(fieldKey))
        {
            return "fileInfo";
        }
        return fieldKey + "Info";
    }

    private boolean isFileInfoField(FieldConfig fieldConfig)
    {
        String fieldRole = normalizeFieldRole(fieldConfig.getFieldRole());
        if ("fileInfo".equalsIgnoreCase(fieldRole))
        {
            return true;
        }
        return StringUtils.isEmpty(fieldRole) && "file".equalsIgnoreCase(fieldConfig.getFieldType());
    }

    private String resolveRowFieldKey(List<Map<String, Object>> rows, String fieldKey)
    {
        if (rows == null || rows.isEmpty() || StringUtils.isEmpty(fieldKey))
        {
            return fieldKey;
        }
        for (Map<String, Object> row : rows)
        {
            if (row == null || row.isEmpty())
            {
                continue;
            }
            if (row.containsKey(fieldKey))
            {
                return fieldKey;
            }
            String camelFieldKey = StringUtils.toCamelCase(fieldKey);
            if (row.containsKey(camelFieldKey))
            {
                return camelFieldKey;
            }
        }
        return StringUtils.toCamelCase(fieldKey);
    }

    private String normalizeFieldRole(String fieldRole)
    {
        return fieldRole == null ? "" : fieldRole.trim();
    }

    private boolean isCreateAuditField(String fieldKey)
    {
        return "createBy".equals(fieldKey) || "createdBy".equals(fieldKey);
    }

    private boolean isUpdateAuditField(String fieldKey)
    {
        return "updateBy".equals(fieldKey) || "updatedBy".equals(fieldKey);
    }

    private List<String> resolveFileUrlCandidates(String fileUrl)
    {
        if (StringUtils.isEmpty(fileUrl))
        {
            return Collections.emptyList();
        }
        LinkedHashSet<String> candidates = new LinkedHashSet<>();
        String value = fileUrl.trim();
        candidates.add(value);
        String fileUrlPrefix = XiaoHeConfig.getFileUrl();
        if (StringUtils.isNotEmpty(fileUrlPrefix) && value.startsWith(fileUrlPrefix))
        {
            String relativePath = value.substring(fileUrlPrefix.length());
            if (StringUtils.isNotEmpty(relativePath))
            {
                candidates.add(relativePath);
            }
        }
        int profileIndex = value.indexOf("/profile/");
        if (profileIndex >= 0)
        {
            candidates.add(value.substring(profileIndex));
        }
        try
        {
            URI uri = URI.create(value);
            if (StringUtils.isNotEmpty(uri.getPath()))
            {
                candidates.add(uri.getPath());
            }
        }
        catch (Exception ignored)
        {
        }
        return new ArrayList<>(candidates);
    }

    private Map<String, Object> buildFileInfo(SysFileInfo fileInfo)
    {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("fileId", fileInfo.getFileId());
        result.put("fileObjectName", fileInfo.getFileObjectName());
        result.put("fileOriginName", fileInfo.getFileOriginName());
        result.put("fileUrl", fileInfo.getfileUrl());
        result.put("fileSizeInfo", fileInfo.getFileSizeInfo());
        result.put("fileSuffix", fileInfo.getFileSuffix());
        result.put("storageType", "local");
        return result;
    }
}
