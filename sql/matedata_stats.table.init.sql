-- ----------------------------
-- Table structure for `failed_request_log`
-- ----------------------------
drop table if exists `failed_request_log`;
create table `failed_request_log`
(
  `id`           bigint           not null auto_increment,
  `request_time` datetime         not null,
  `client_id`    bigint           not null,
  `product_id`   bigint           not null,
  `status`       tinyint unsigned not null,
  primary key (`id`)
)
  engine = InnoDB
  default charset = utf8;

-- ----------------------------
-- Table structure for `stats_summary`
-- ----------------------------
drop table if exists `stats_summary`;
create table `stats_summary` (
  `id`                 bigint(20)                       not null auto_increment,
  `create_time`        datetime                         not null,
  `update_time`        datetime                         not null,
  `client_increment`   int(11) default 0                not null,
  `request`            bigint(20) default 0             not null,
  `request_not_hit`    bigint default 0                 not null,
  `request_failed`     bigint(20) default 0             not null,
  `request_3rd_failed` bigint(20) default 0             not null,
  `recharge`           decimal(16, 2) default '0.00'    not null,
  `profit`             decimal(16, 2) default '0.00'    not null,
  `stats_date`         date                             not null,
  `stats_year`         int(11)                          not null,
  `stats_month`        int(11)                          not null,
  `stats_week`         int(11)                          not null,
  `stats_day`          int(11)                          not null,
  `stats_hour`         tinyint(3) unsigned              not null,
  primary key (`id`),
  unique key `uk_date_hour` (`stats_date`, `stats_hour`) using btree
)
  engine = InnoDB
  default charset = utf8;

-- ----------------------------
-- Table structure for `stats_recharge`
-- ----------------------------
drop table if exists `stats_recharge`;
create table `stats_recharge` (
  `id`            bigint(20)                      not null auto_increment,
  `create_time`   datetime                        not null,
  `update_time`   datetime                        not null,
  `recharge_type` tinyint(3) unsigned             not null,
  `stats_year`    int(10) unsigned                not null,
  `stats_month`   tinyint(3) unsigned             not null,
  `stats_week`    tinyint(3) unsigned             not null,
  `stats_day`     tinyint(3) unsigned             not null,
  `stats_hour`    tinyint(3) unsigned             not null,
  `stats_date`    date                            not null,
  `amount`        decimal(16, 2) default '0.00'   not null,
  primary key (`id`),
  unique key `uk_recharge_type_hour` (`recharge_type`, `stats_date`, `stats_hour`) using btree
)
  engine = InnoDB
  default charset = utf8;

-- ----------------------------
-- Table structure for `job`
-- ----------------------------
drop table if exists `job`;
create table `job`
(
  `code`          varchar(16) not null,
  `name`          varchar(32) not null,
  `last_suc_time` datetime default null,
  primary key (`code`)
)
  engine = InnoDB
  default charset = utf8;

-- ----------------------------
-- Table structure for `job_log`
-- ----------------------------
drop table if exists `job_log`;
create table `job_log`
(
  `id`          bigint(20)          not null auto_increment,
  `create_time` datetime            not null,
  `job_code`    varchar(16)         not null,
  `success`     tinyint(1) unsigned not null,
  `remark`      varchar(256)                 default null,
  primary key (`id`)
)
  engine = InnoDB
  default charset = utf8;

-- ----------------------------
-- Table structure for `warning_setting`
-- ----------------------------
drop table if exists `warning_setting`;
create table `warning_setting` (
  `id`             bigint(20)          not null auto_increment,
  `code`           varchar(16)         not null,
  `create_time`    datetime            not null,
  `update_time`    datetime            not null,
  `content`        varchar(64)                  default null,
  `type`           tinyint(3)          not null,
  `send`           tinyint(1) unsigned not null,
  `play`           tinyint(1) unsigned not null,
  `file_name`      varchar(128)                 default null,
  `file_path`      varchar(128)                 default null,
  `general_limit`  int(11)                      default null,
  `severity_limit` int(11)                      default null,
  `warning_limit`  int(11)                      default null,
  `enabled`        tinyint(1) unsigned not null,
  primary key (`id`)
)
  engine = InnoDB
  default charset = utf8;

-- ----------------------------
-- Table structure for `stats_request`
-- ----------------------------
drop table if exists `stats_request`;
create table `stats_request` (
  `id`              bigint(20)                       not null auto_increment,
  `create_time`     datetime                         not null,
  `update_time`     datetime                         not null,
  `product_id`      bigint(20)                       not null,
  `client_id`       bigint(20)                       not null,
  `stats_year`      int(10) unsigned                 not null,
  `stats_month`     tinyint(3) unsigned              not null,
  `stats_week`      tinyint(3) unsigned              not null,
  `stats_day`       tinyint(3) unsigned              not null,
  `stats_hour`      tinyint(3) unsigned              not null,
  `stats_date`      date                             not null,
  `request`         bigint(20) default 0             not null,
  `request_not_hit` bigint(20) default 0             not null,
  `request_failed`  bigint(20) default 0             not null,
  primary key (`id`)
)
  engine = InnoDB
  default charset = utf8;