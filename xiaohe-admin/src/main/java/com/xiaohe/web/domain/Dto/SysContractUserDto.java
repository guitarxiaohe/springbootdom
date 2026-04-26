package com.xiaohe.web.domain.Dto;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.xiaohe.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author : GuitarXiaohe
 * @version V1.0
 * @Project: lthr-serve
 * @Package com.xiaohe.web.domain.Entity
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date Date : 2024年02月28日 14:55
 */

@Data
@TableName("sys_contract")
public class SysContractUserDto extends BaseEntity {
    /**
     * id
     */
    @ApiModelProperty("id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableId("id")
    private Long id;

    /**
     * userId
     */
    @ApiModelProperty("用户id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableId("id")
    private Long userId;

    /**
     * 包工id
     */
    @ApiModelProperty("包工id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableId("id")
    private Long contractId;


}