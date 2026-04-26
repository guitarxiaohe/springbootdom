DROP TABLE IF EXISTS QRTZ_FIRED_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_PAUSED_TRIGGER_GRPS;
DROP TABLE IF EXISTS QRTZ_SCHEDULER_STATE;
DROP TABLE IF EXISTS QRTZ_LOCKS;
DROP TABLE IF EXISTS QRTZ_SIMPLE_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_SIMPROP_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_CRON_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_BLOB_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_JOB_DETAILS;
DROP TABLE IF EXISTS QRTZ_CALENDARS;

CREATE TABLE QRTZ_JOB_DETAILS (
    sched_name VARCHAR(120) NOT NULL COMMENT '调度名称',
    job_name VARCHAR(200) NOT NULL COMMENT '任务名称',
    job_group VARCHAR(200) NOT NULL COMMENT '任务组名',
    description VARCHAR(250) NULL COMMENT '相关介绍',
    job_class_name VARCHAR(250) NOT NULL COMMENT '执行任务类名称',
    is_durable VARCHAR(1) NOT NULL COMMENT '是否持久化',
    is_nonconcurrent VARCHAR(1) NOT NULL COMMENT '是否并发',
    is_update_data VARCHAR(1) NOT NULL COMMENT '是否更新数据',
    requests_recovery VARCHAR(1) NOT NULL COMMENT '是否接受恢复执行',
    job_data BLOB NULL COMMENT '存放持久化job对象',
    PRIMARY KEY (sched_name, job_name, job_group)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='任务详细信息表';

CREATE TABLE QRTZ_TRIGGERS (
    sched_name VARCHAR(120) NOT NULL COMMENT '调度名称',
    trigger_name VARCHAR(200) NOT NULL COMMENT '触发器名称',
    trigger_group VARCHAR(200) NOT NULL COMMENT '触发器组名',
    job_name VARCHAR(200) NOT NULL COMMENT '任务名称',
    job_group VARCHAR(200) NOT NULL COMMENT '任务组名',
    description VARCHAR(250) NULL COMMENT '相关介绍',
    next_fire_time BIGINT(13) NULL COMMENT '下次触发时间',
    prev_fire_time BIGINT(13) NULL COMMENT '上次触发时间',
    priority INTEGER NULL COMMENT '优先级',
    trigger_state VARCHAR(16) NOT NULL COMMENT '触发器状态',
    trigger_type VARCHAR(8) NOT NULL COMMENT '触发器类型',
    start_time BIGINT(13) NOT NULL COMMENT '开始时间',
    end_time BIGINT(13) NULL COMMENT '结束时间',
    calendar_name VARCHAR(200) NULL COMMENT '日程表名称',
    misfire_instr SMALLINT(2) NULL COMMENT '补偿执行策略',
    job_data BLOB NULL COMMENT '存放持久化job对象',
    PRIMARY KEY (sched_name, trigger_name, trigger_group),
    FOREIGN KEY (sched_name, job_name, job_group)
        REFERENCES QRTZ_JOB_DETAILS(sched_name, job_name, job_group)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='触发器信息表';

CREATE TABLE QRTZ_SIMPLE_TRIGGERS (
    sched_name VARCHAR(120) NOT NULL COMMENT '调度名称',
    trigger_name VARCHAR(200) NOT NULL COMMENT '触发器名称',
    trigger_group VARCHAR(200) NOT NULL COMMENT '触发器组名',
    repeat_count BIGINT(7) NOT NULL COMMENT '重复次数',
    repeat_interval BIGINT(12) NOT NULL COMMENT '重复间隔时间',
    times_triggered BIGINT(10) NOT NULL COMMENT '已触发次数',
    PRIMARY KEY (sched_name, trigger_name, trigger_group),
    FOREIGN KEY (sched_name, trigger_name, trigger_group)
        REFERENCES QRTZ_TRIGGERS(sched_name, trigger_name, trigger_group)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='简单触发器信息表';

CREATE TABLE QRTZ_CRON_TRIGGERS (
    sched_name VARCHAR(120) NOT NULL COMMENT '调度名称',
    trigger_name VARCHAR(200) NOT NULL COMMENT '触发器名称',
    trigger_group VARCHAR(200) NOT NULL COMMENT '触发器组名',
    cron_expression VARCHAR(200) NOT NULL COMMENT 'cron表达式',
    time_zone_id VARCHAR(80) COMMENT '时区',
    PRIMARY KEY (sched_name, trigger_name, trigger_group),
    FOREIGN KEY (sched_name, trigger_name, trigger_group)
        REFERENCES QRTZ_TRIGGERS(sched_name, trigger_name, trigger_group)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Cron触发器信息表';

CREATE TABLE QRTZ_SIMPROP_TRIGGERS (
    sched_name VARCHAR(120) NOT NULL COMMENT '调度名称',
    trigger_name VARCHAR(200) NOT NULL COMMENT '触发器名称',
    trigger_group VARCHAR(200) NOT NULL COMMENT '触发器组名',
    str_prop_1 VARCHAR(512) NULL COMMENT '字符串属性1',
    str_prop_2 VARCHAR(512) NULL COMMENT '字符串属性2',
    str_prop_3 VARCHAR(512) NULL COMMENT '字符串属性3',
    int_prop_1 INT NULL COMMENT '整型属性1',
    int_prop_2 INT NULL COMMENT '整型属性2',
    long_prop_1 BIGINT NULL COMMENT '长整型属性1',
    long_prop_2 BIGINT NULL COMMENT '长整型属性2',
    dec_prop_1 NUMERIC(13,4) NULL COMMENT '小数属性1',
    dec_prop_2 NUMERIC(13,4) NULL COMMENT '小数属性2',
    bool_prop_1 VARCHAR(1) NULL COMMENT '布尔属性1',
    bool_prop_2 VARCHAR(1) NULL COMMENT '布尔属性2',
    PRIMARY KEY (sched_name, trigger_name, trigger_group),
    FOREIGN KEY (sched_name, trigger_name, trigger_group)
        REFERENCES QRTZ_TRIGGERS(sched_name, trigger_name, trigger_group)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='简单属性触发器信息表';

CREATE TABLE QRTZ_BLOB_TRIGGERS (
    sched_name VARCHAR(120) NOT NULL COMMENT '调度名称',
    trigger_name VARCHAR(200) NOT NULL COMMENT '触发器名称',
    trigger_group VARCHAR(200) NOT NULL COMMENT '触发器组名',
    blob_data BLOB NULL COMMENT '存放持久化trigger对象',
    PRIMARY KEY (sched_name, trigger_name, trigger_group),
    FOREIGN KEY (sched_name, trigger_name, trigger_group)
        REFERENCES QRTZ_TRIGGERS(sched_name, trigger_name, trigger_group)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Blob触发器信息表';

CREATE TABLE QRTZ_CALENDARS (
    sched_name VARCHAR(120) NOT NULL COMMENT '调度名称',
    calendar_name VARCHAR(200) NOT NULL COMMENT '日程表名称',
    calendar BLOB NOT NULL COMMENT '存放持久化calendar对象',
    PRIMARY KEY (sched_name, calendar_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='日程表信息表';

CREATE TABLE QRTZ_PAUSED_TRIGGER_GRPS (
    sched_name VARCHAR(120) NOT NULL COMMENT '调度名称',
    trigger_group VARCHAR(200) NOT NULL COMMENT '触发器组名',
    PRIMARY KEY (sched_name, trigger_group)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='暂停触发器组表';

CREATE TABLE QRTZ_FIRED_TRIGGERS (
    sched_name VARCHAR(120) NOT NULL COMMENT '调度名称',
    entry_id VARCHAR(95) NOT NULL COMMENT '条目ID',
    trigger_name VARCHAR(200) NOT NULL COMMENT '触发器名称',
    trigger_group VARCHAR(200) NOT NULL COMMENT '触发器组名',
    instance_name VARCHAR(200) NOT NULL COMMENT '实例名称',
    fired_time BIGINT(13) NOT NULL COMMENT '触发时间',
    sched_time BIGINT(13) NOT NULL COMMENT '调度时间',
    priority INTEGER NOT NULL COMMENT '优先级',
    state VARCHAR(16) NOT NULL COMMENT '状态',
    job_name VARCHAR(200) NULL COMMENT '任务名称',
    job_group VARCHAR(200) NULL COMMENT '任务组名',
    is_nonconcurrent VARCHAR(1) NULL COMMENT '是否并发',
    requests_recovery VARCHAR(1) NULL COMMENT '是否接受恢复执行',
    PRIMARY KEY (sched_name, entry_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='已触发触发器表';

CREATE TABLE QRTZ_SCHEDULER_STATE (
    sched_name VARCHAR(120) NOT NULL COMMENT '调度名称',
    instance_name VARCHAR(200) NOT NULL COMMENT '实例名称',
    last_checkin_time BIGINT(13) NOT NULL COMMENT '上次检查时间',
    checkin_interval BIGINT(13) NOT NULL COMMENT '检查间隔时间',
    PRIMARY KEY (sched_name, instance_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='调度器状态表';

CREATE TABLE QRTZ_LOCKS (
    sched_name VARCHAR(120) NOT NULL COMMENT '调度名称',
    lock_name VARCHAR(40) NOT NULL COMMENT '悲观锁名称',
    PRIMARY KEY (sched_name, lock_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='存储悲观锁';
