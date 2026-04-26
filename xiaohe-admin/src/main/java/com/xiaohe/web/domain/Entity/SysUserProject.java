package com.xiaohe.web.domain.Entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * @author : GuitarXiaohe
 * @version V1.0
 * @Project: lthr-serve
 * @Package com.xiaohe.web.domain.Entity
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date Date : 2024年02月13日 01:30
 */

@Data
@TableName("sys_user_project")
public class SysUserProject {

    /**
     * id
     */
    @JsonFormat(shape =JsonFormat.Shape.STRING )
    @TableId("id")
    private Long id;

    /**
     * 人员id
     */
    private Long userId;


    /**
     * 项目id
     */
    private Long projectId;


}