package com.xiaohe.web.domain.Export;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("sys_worker_user")
public class SysWorkerUserEx {


    /**
     * 名称
     */
    @ColumnWidth(value = 10)
    @ExcelProperty(value = "姓名", index = 0)
    private String name;


    ;


    /**
     * 手机号
     */
    @ColumnWidth(value = 20)
    @ExcelProperty(value = "手机号", index = 1)
    private String phone;

    /**
     * 身份证号码
     */
    @ColumnWidth(value = 30)
    @ExcelProperty(value = "身份证号码", index = 2)
    private String cardNumber;

    /**
     * 工种
     */
    @ColumnWidth(value = 10)
    @ExcelProperty(value = "工种", index = 3)
    private String craft;

    /**
     * 银行卡号
     */
    @ColumnWidth(value = 30)
    @ExcelProperty(value = "银行卡号", index = 4)
    private String bankNumber;

    /**
     * 户籍所在地
     */
    @ColumnWidth(value = 20)
    @ExcelProperty(value = "户籍所在地", index = 5)
    private String domicile;

    /**
     * 开户账号所在地
     */
    @ColumnWidth(value = 20)
    @ExcelProperty(value = "开户账号所在地", index = 6)
    private String accountLocation;

    /**
     * 总工时
     */
    @ColumnWidth(value = 10)
    @ExcelProperty(value = "总工时", index = 7)
    private BigDecimal manHour;


    /**
     * 总生活费
     */
    @ColumnWidth(value = 10)
    @ExcelProperty(value = "生活费", index = 8)
    private BigDecimal livingExpenses;

    /**
     * 总借支
     */
    @ColumnWidth(value = 10)
    @ExcelProperty(value = "总借支", index = 9)
    private BigDecimal lendMoney;

    /**
     * 总工资
     */
    @ColumnWidth(value = 10)
    @ExcelProperty(value = "总工资", index = 10)
    private BigDecimal wage;

    /**
     * 应发工资
     */
    @ExcelProperty(value = "应发工资", index = 11)
    private BigDecimal wagePayable;


}
