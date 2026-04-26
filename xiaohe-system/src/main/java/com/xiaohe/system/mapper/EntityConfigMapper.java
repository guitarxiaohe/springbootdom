package com.xiaohe.system.mapper;

import com.xiaohe.system.domain.EntityConfig;

/**
 * entity_config mapper.
 *
 * @author xiaohe
 */
public interface EntityConfigMapper
{
    EntityConfig selectEntityConfigByEntityKey(String entityKey);
}
