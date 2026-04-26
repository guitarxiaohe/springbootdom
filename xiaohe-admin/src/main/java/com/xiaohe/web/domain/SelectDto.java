package com.xiaohe.web.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * @author : GuitarXiaohe
 * @version V1.0
 * @Project: lthr-serve
 * @Package com.xiaohe.web.domain
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date Date : 2024年02月12日 10:20
 */

@Data
public class SelectDto {
    /**
     * 汉字
     */
    private String label;

    /**
     * id
     */
    @JsonFormat(shape =JsonFormat.Shape.STRING )
    private Long value;

}