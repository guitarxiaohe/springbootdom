package com.xiaohe.web.controller.system;

import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.xiaohe.common.annotation.Log;
import com.xiaohe.common.core.controller.BaseController;
import com.xiaohe.common.core.domain.AjaxResult;
import com.xiaohe.common.enums.BusinessType;
import com.xiaohe.system.service.impl.DynamicExcelService;

/**
 * 动态实体 Excel 导入导出 Controller。
 *
 * @author xiaohe
 */
@RestController
@RequestMapping("/system/dynamicExcel")
public class DynamicExcelController extends BaseController
{
    @Autowired
    private DynamicExcelService dynamicExcelService;

    /**
     * 导出实体数据为 Excel。
     */
    @PreAuthorize("@ss.hasEntityPermi(#entityKey, 'export')")
    @Log(title = "动态实体导出", businessType = BusinessType.EXPORT)
    @GetMapping("/export/{entityKey}")
    public void export(@PathVariable String entityKey, HttpServletResponse response) throws Exception
    {
        dynamicExcelService.exportEntityToExcel(entityKey, response);
    }

    /**
     * 下载导入模板（仅表头 + 示例行）。
     */
    @PreAuthorize("@ss.hasEntityPermi(#entityKey, 'import')")
    @Log(title = "动态实体模板", businessType = BusinessType.EXPORT)
    @GetMapping("/template/{entityKey}")
    public void downloadTemplate(@PathVariable String entityKey, HttpServletResponse response) throws Exception
    {
        dynamicExcelService.exportTemplate(entityKey, response);
    }

    /**
     * 导入 Excel 数据到动态实体。
     *
     * @param entityKey 实体 key
     * @param file      Excel 文件
     * @param mode      导入模式：create（默认）、update、upsert
     * @param children  子表卸除 JSON 参数，格式：[{"tableName":"xx","childKey":"parent_id","parentValue":123}]
     */
    @PreAuthorize("@ss.hasEntityPermi(#entityKey, 'import')")
    @Log(title = "动态实体导入", businessType = BusinessType.IMPORT)
    @PostMapping("/import/{entityKey}")
    public AjaxResult importData(@PathVariable String entityKey,
            @RequestParam("file") MultipartFile file,
            @RequestParam(defaultValue = "create") String mode,
            @RequestParam(required = false) String children) throws Exception
    {
        if (file.isEmpty())
        {
            return AjaxResult.error("上传文件不能为空");
        }
        Map<String, Object> result = dynamicExcelService.importFromExcel(entityKey, file, mode, children);
        return AjaxResult.success(result);
    }
}
