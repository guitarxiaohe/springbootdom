package com.xiaohe.web.controller.system;

import java.util.List;
import java.util.Map;
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
import com.xiaohe.common.core.controller.BaseController;
import com.xiaohe.common.core.domain.AjaxResult;
import com.xiaohe.common.core.page.PageDomain;
import com.xiaohe.common.core.page.TableDataInfo;
import com.xiaohe.common.core.page.TableSupport;
import com.xiaohe.common.enums.BusinessType;
import com.xiaohe.common.utils.StringUtils;
import com.xiaohe.common.utils.sql.SqlUtil;
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
            return getDataTable(rows);
        }
        List<FieldConfig> list = fieldConfigService.selectFieldConfigByEntityKey(entityKey);
        operatorUserFillService.fillAuditUsers(list);
        return AjaxResult.success(list);
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
        return AjaxResult.success(dynamicEntityDataService.selectEntityRowById(entityKey, id));
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
}
