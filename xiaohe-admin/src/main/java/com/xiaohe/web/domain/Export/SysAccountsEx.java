package com.xiaohe.web.domain.Export;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author : GuitarXiaohe
 * @version V1.0
 * @Project: lthr-serve
 * @Package com.xiaohe.web.domain.Export
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date Date : 2024年02月29日 17:16
 */

@Data
@ContentRowHeight(20)
@HeadRowHeight(18)
public class SysAccountsEx {


    /**
     * 项目名称
     */
    @ExcelProperty(value = "项目名称", index = 0)
    @ColumnWidth(value = 20)
    private String projectName;


    /**
     * 名称
     */
    @ColumnWidth(value = 10)
    @ExcelProperty(value = "名称", index = 1)
    private String name;

    /**
     * 手机号
     */
    @ColumnWidth(value = 30)
    @ExcelProperty(value = "手机号", index = 2)
    private String phone;

    /**
     * 工时
     */
    @ColumnWidth(value = 10)
    @ExcelProperty(value = "工时", index = 3)

    private BigDecimal manHour;


    /**
     * 审核意见
     */
    @ApiModelProperty("审核意见")
    @ColumnWidth(value = 20)
    @ExcelProperty(value = "审核意见", index = 4)
    private String examineRemark;


    /**
     * 审核人
     */
    @ApiModelProperty("审核人")
    @ColumnWidth(value = 10)
    @ExcelProperty(value = "审核人", index = 5)
    private String examineBy;

    /**
     * 审核时间
     */
    @ApiModelProperty("审核时间")
    @ColumnWidth(value = 30)
    @ExcelProperty(value = "审核时间", index = 6)
    private String examineTime;

    /**
     * 创建时间
     */
    @ColumnWidth(value = 30)
    @ExcelProperty(value = "创建时间", index = 7)
    private String createTime;


}