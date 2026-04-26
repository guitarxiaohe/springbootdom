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
 * @Package com.xiaohe.web.domain
 * @Description: 工人信息表
 * @date Date : 2024年02月08日 19:25
 */

@Data
@ContentRowHeight(20)
@HeadRowHeight(18)
public class SysWorkerUserViewEx {
    /**
     * 姓名
     */
    @ApiModelProperty("姓名")
    @ColumnWidth(value = 30)
    @ExcelProperty(value = "姓名",index = 0)
    private String name;

    /**
     * 生活费
     */
    @ApiModelProperty("生活费")
    @ColumnWidth(value = 30)
    @ExcelProperty(value = "生活费",index = 1)
    private BigDecimal livingExpenses;

    /**
     * 借支
     */
    @ApiModelProperty("总借支")
    @ColumnWidth(value = 30)
    @ExcelProperty(value = "总借支",index = 2)
    private BigDecimal lendMoney;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    @ColumnWidth(value = 60)
    @ExcelProperty(value = "备注",index = 3)
    private String remark;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @ColumnWidth(value = 60)
    @ExcelProperty(value = "时间",index = 4)
    private String createTime;


//    /**
//     * 创建人
//     */
//    @ApiModelProperty("创建人")
//    @ExcelProperty(value = "创建人",index = 4)
//    private String createBy;

//    /**
//     * 修改时间
//     */
//    @ApiModelProperty("修改时间")
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    @Excel(name = "修改时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
//    private Date updateTime;
//
//    /**
//     * 修改人
//     */
//    @ApiModelProperty("创建人")
//    @Excel(name = "创建人")
//    private String updateBy;


}
