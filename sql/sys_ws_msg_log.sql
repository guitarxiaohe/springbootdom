-- WebSocket消息日志表
create table sys_ws_msg_log (
    msg_id          bigint(20)      not null auto_increment    comment '消息ID',
    msg_type        varchar(32)     not null                   comment '消息类型 notice/system/alert',
    title           varchar(128)    default ''                  comment '标题',
    content         varchar(512)    default ''                  comment '正文',
    path            varchar(255)    default ''                  comment '跳转路径',
    params          varchar(512)    default ''                  comment 'JSON格式筛选参数',
    target_user_id  bigint(20)      default null                comment '目标用户ID，null=广播',
    send_status     char(1)         default '0'                 comment '0=待发送 1=已发送 2=发送失败',
    send_time       datetime        default null                comment '实际发送时间',
    create_by       varchar(64)     default '',
    create_time     datetime,
    update_by       varchar(64)     default '',
    update_time     datetime,
    remark          varchar(255)    default null,
    primary key (msg_id),
    key idx_target_user (target_user_id),
    key idx_create_time (create_time)
) engine=innodb auto_increment=1 comment='WebSocket消息日志表';
