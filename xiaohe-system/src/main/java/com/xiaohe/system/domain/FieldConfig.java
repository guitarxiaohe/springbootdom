package com.xiaohe.system.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.xiaohe.common.core.domain.BaseEntity;
import com.xiaohe.common.core.domain.vo.AuditUserAttachable;
import com.xiaohe.common.core.domain.vo.OperatorUserVo;
import lombok.Data;

/**
 * 字段配置表 field_config
 *
 * @author xiaohe
 */
@Data
public class FieldConfig extends BaseEntity implements AuditUserAttachable
{
    private static final long serialVersionUID = 1L;

    private Long id;

    private String entityKey;

    private String fixed;

    private String fieldKey;

    private String fieldName;

    private String fieldType;

    private String fieldRole;

    private String dictCode;

    private String selectEntityKey;

    private Integer sort;

    private Integer isFuzzySearch;

    private Integer isVisible;

    private Long createdBy;

    private Long createdTime;

    private Long updatedBy;

    private Long updatedTime;

    /** 创建人信息（查询返回时填充，不落库） */
    @JsonInclude(JsonInclude.Include.ALWAYS)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private OperatorUserVo createUser;

    /** 修改人信息（查询返回时填充，不落库） */
    @JsonInclude(JsonInclude.Include.ALWAYS)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private OperatorUserVo updateUser;

}
