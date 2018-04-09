-- ----------------------------
-- Table structure for `articles`
-- ----------------------------
drop table if exists `articles`;
create table `articles` (
  `id`           bigint(20)          not null auto_increment,
  `create_time`  datetime            not null,
  `update_time`  datetime            not null,
  `type`         tinyint(3) unsigned not null,
  `image_path`   varchar(256)        not null,
  `user_id`      bigint(20)          not null,
  `title`        varchar(32)         not null,
  `synopsis`     varchar(256)        not null,
  `author`       varchar(32)                  default null,
  `content`      text                not null,
  `order_id`     int(11)             not null,
  `publish_time` date                         default null,
  `published`    tinyint(1) unsigned not null,
  `deleted`      tinyint(1) unsigned not null,
  primary key (`id`)
)
  engine = InnoDB
  default charset = utf8;

-- ----------------------------
-- Table structure for `client`
-- ----------------------------
drop table if exists `client`;
create table `client` (
  `id`              bigint(20)          not null auto_increment,
  `create_time`     datetime            not null,
  `update_time`     datetime            not null,
  `corp_name`       varchar(128)        not null,
  `short_name`      varchar(128)        not null,
  `license`         varchar(32)         not null,
  `industry_id`     bigint(20)          not null,
  `primary_user_id` bigint(20)          not null,
  `username`        varchar(32)         not null,
  `manager_id`      bigint(20)          not null,
  `account_qty`     int(11)             not null,
  `enabled`         tinyint(3) unsigned not null,
  `deleted`         tinyint(3) unsigned not null,
  primary key (`id`),
  unique key `uk_corp_license` (`license`) using btree,
  unique key `uk_corp_name` (`corp_name`) using btree
)
  engine = InnoDB
  default charset = utf8;

-- ----------------------------
-- Table structure for `client_contact`
-- ----------------------------
drop table if exists `client_contact`;
create table `client_contact` (
  `id`          bigint(20)          not null auto_increment,
  `create_time` datetime            not null,
  `update_time` datetime            not null,
  `client_id`   bigint(20)          not null,
  `name`        varchar(32)         not null,
  `position`    varchar(32)         not null,
  `phone`       varchar(16)         not null,
  `email`       varchar(128)                 default null,
  `general`     tinyint(3) unsigned not null,
  primary key (`id`)
)
  engine = InnoDB
  default charset = utf8;

-- ----------------------------
-- Table structure for `client_message`
-- ----------------------------
drop table if exists `client_message`;
create table `client_message` (
  `id`          bigint(20)          not null auto_increment,
  `create_time` datetime            not null,
  `update_time` datetime            not null,
  `client_id`   bigint(20)          not null,
  `type`        tinyint(3) unsigned not null,
  `content`     text,
  primary key (`id`)
)
  engine = InnoDB
  default charset = utf8;

-- ----------------------------
-- Table structure for `client_operate_log`
-- ----------------------------
drop table if exists `client_operate_log`;
create table `client_operate_log` (
  `id`             bigint(20)          not null auto_increment,
  `create_time`    datetime            not null,
  `update_time`    datetime            not null,
  `client_id`      bigint(20)          not null,
  `client_user_id` bigint(20)          not null,
  `manager_id`     bigint(20)          not null,
  `type`           tinyint(3) unsigned not null,
  `reason`         varchar(512)        not null,
  primary key (`id`)
)
  engine = InnoDB
  default charset = utf8;

-- ----------------------------
-- Table structure for `client_product`
-- ----------------------------
drop table if exists `client_product`;
create table `client_product` (
  `id`                 bigint(20)          not null auto_increment,
  `create_time`        datetime            not null,
  `update_time`        datetime            not null,
  `client_id`          bigint(20)          not null,
  `product_id`         bigint(20)          not null,
  `app_id`             varchar(32)                  default null,
  `bill_plan`          tinyint(3) unsigned          default null,
  `balance`            decimal(16, 2)               default null,
  `latest_recharge_id` bigint(20)                   default null,
  `opened`             tinyint(3) unsigned not null,
  primary key (`id`),
  unique key `uk_client_product` (`client_id`, `product_id`) using btree,
  unique key `uk_corp_prod_key` (`app_id`) using btree
)
  engine = InnoDB
  default charset = utf8;

-- ----------------------------
-- Table structure for `client_remind`
-- ----------------------------
drop table if exists `client_remind`;
create table `client_remind` (
  `id`          bigint(20)          not null auto_increment,
  `create_time` datetime            not null,
  `update_time` datetime            not null,
  `remind_date` date                not null,
  `type`        tinyint(1)          not null,
  `client_id`   bigint(20)          not null,
  `link_name`   varchar(32)                  default null,
  `link_phone`  varchar(16)                  default null,
  `product_id`  bigint(20)          not null,
  `count`       int(11)             not null,
  `day`         int(11)                      default null,
  `remark`      varchar(256)                 default null,
  `dispose`     tinyint(1) unsigned not null,
  primary key (`id`)
)
  engine = InnoDB
  default charset = utf8;

-- ----------------------------
-- Table structure for `client_remind_product`
-- ----------------------------
drop table if exists `client_remind_product`;
create table `client_remind_product` (
  `id`          bigint(20) not null auto_increment,
  `create_time` datetime   not null,
  `update_time` datetime   not null,
  `remind_id`   bigint(20) not null,
  `product_id`  bigint(20) not null,
  `recharge_id` bigint(20) not null,
  `remind`      tinyint(1) not null,
  primary key (`id`)
)
  engine = InnoDB
  default charset = utf8;

-- ----------------------------
-- Table structure for `client_user`
-- ----------------------------
drop table if exists `client_user`;
create table `client_user` (
  `id`          bigint(20)          not null auto_increment,
  `create_time` datetime            not null,
  `update_time` datetime            not null,
  `client_id`   bigint(20)          not null,
  `name`        varchar(128)        not null,
  `phone`       varchar(16)         not null,
  `email`       varchar(128)                 default null,
  `username`    varchar(32)         not null,
  `password`    varchar(32)         not null,
  `app_secret`  varchar(32)                  default null,
  `enabled`     tinyint(3) unsigned not null,
  `deleted`     tinyint(3) unsigned not null,
  primary key (`id`),
  unique key `uk_client_username` (`username`) using btree
)
  engine = InnoDB
  default charset = utf8;

-- ----------------------------
-- Table structure for `client_user_product`
-- ----------------------------
drop table if exists `client_user_product`;
create table `client_user_product` (
  `id`           bigint(20)  not null auto_increment,
  `create_time`  datetime    not null,
  `update_time`  datetime    not null,
  `user_id`      bigint(20)  not null,
  `product_id`   bigint(20)  not null,
  `req_host`     varchar(32) not null,
  `valid_time`   datetime             default null,
  `access_token` varchar(64)          default null,
  primary key (`id`),
  unique key `uk_access_token` (`access_token`) using btree
)
  engine = InnoDB
  default charset = utf8;

-- ----------------------------
-- Table structure for `dict_industry`
-- ----------------------------
drop table if exists `dict_industry`;
create table `dict_industry` (
  `id`          bigint(20)          not null auto_increment,
  `create_time` datetime            not null,
  `update_time` datetime            not null,
  `code`        varchar(16)         not null,
  `name`        varchar(32)         not null,
  `seq_no`      int(11)             not null,
  `parent_id`   bigint(20)          not null,
  `enabled`     tinyint(3) unsigned not null,
  primary key (`id`),
  unique key `uk_industry_code` (`code`) using btree
)
  engine = InnoDB
  default charset = utf8;

-- ----------------------------
-- Table structure for `dict_recharge_type`
-- ----------------------------
drop table if exists `dict_recharge_type`;
create table `dict_recharge_type` (
  `id`          int(11)             not null auto_increment,
  `create_time` datetime            not null,
  `update_time` datetime            not null,
  `name`        varchar(32)         not null,
  `remark`      varchar(512)                 default null,
  `enabled`     tinyint(3) unsigned not null,
  `deleted`     tinyint(3) unsigned not null,
  primary key (`id`),
  unique key `uk_recharge_name` (`name`) using btree
)
  engine = InnoDB
  default charset = utf8;

-- ----------------------------
-- Table structure for `function`
-- ----------------------------
drop table if exists `function`;
create table `function` (
  `id`          bigint(20)          not null,
  `create_time` datetime            not null,
  `update_time` datetime            not null,
  `name`        varchar(32)         not null,
  `seq_no`      int(11)             not null,
  `level`       int(11)             not null,
  `parent_id`   bigint(20)          not null,
  `enabled`     tinyint(3) unsigned not null,
  primary key (`id`)
)
  engine = InnoDB
  default charset = utf8;

-- ----------------------------
-- Table structure for `group`
-- ----------------------------
drop table if exists `group`;
create table `group` (
  `id`          bigint(20)          not null auto_increment,
  `create_time` datetime            not null,
  `update_time` datetime            not null,
  `name`        varchar(32)         not null,
  `enabled`     tinyint(3) unsigned not null,
  primary key (`id`),
  unique key `uk_role_name` (`name`) using btree
)
  engine = InnoDB
  default charset = utf8;

-- ----------------------------
-- Table structure for `group_function`
-- ----------------------------
drop table if exists `group_function`;
create table `group_function` (
  `id`           bigint(20) not null auto_increment,
  `create_time`  datetime   not null,
  `update_time`  datetime   not null,
  `group_id`     bigint(20) not null,
  `privilege_id` bigint(20) not null,
  primary key (`id`)
)
  engine = InnoDB
  default charset = utf8;

-- ----------------------------
-- Table structure for `user`
-- ----------------------------
drop table if exists `user`;
create table `user` (
  `id`          bigint(20)          not null auto_increment,
  `create_time` datetime            not null,
  `update_time` datetime            not null,
  `username`    varchar(32)         not null,
  `password`    varchar(32)         not null,
  `name`        varchar(32)         not null,
  `phone`       varchar(16)                  default null,
  `qq`          varchar(16)                  default null,
  `group_id`    bigint(20)          not null,
  `enabled`     tinyint(3) unsigned not null,
  `session_id`  varchar(32)                  default null,
  `role_type`   tinyint(3) unsigned not null,
  `pacify`      tinyint(1)                   default null,
  `alarm`       tinyint(1)                   default null,
  primary key (`id`),
  unique key `uk_user_username` (`username`) using btree
)
  engine = InnoDB
  default charset = utf8;

-- ----------------------------
-- Table structure for `user_function`
-- ----------------------------
drop table if exists `user_function`;
create table `user_function` (
  `id`           bigint(20) not null auto_increment,
  `create_time`  datetime   not null,
  `update_time`  datetime   not null,
  `manager_id`   bigint(20) not null,
  `privilege_id` bigint(20) not null,
  primary key (`id`),
  unique key `uk_manager_privilege` (`manager_id`, `privilege_id`) using btree
)
  engine = InnoDB
  default charset = utf8;

-- ----------------------------
-- Table structure for `product`
-- ----------------------------
drop table if exists `product`;
create table `product` (
  `id`          bigint(20)          not null auto_increment,
  `create_time` datetime            not null,
  `update_time` datetime            not null,
  `type`        tinyint(3)          not null,
  `custom`      tinyint(3) unsigned not null,
  `code`        varchar(32)         not null,
  `name`        varchar(32)         not null,
  `cost_amt`    decimal(16, 2)      not null,
  `remark`      varchar(512)                 default null,
  `enabled`     tinyint(3) unsigned not null,
  primary key (`id`),
  unique key `uk_product_code` (`code`) using btree
)
  engine = InnoDB
  default charset = utf8;

-- ----------------------------
-- Table structure for `product_txt`
-- ----------------------------
drop table if exists `product_txt`;
create table `product_txt` (
  `id`          bigint(20) not null auto_increment,
  `create_time` datetime   not null,
  `update_time` datetime   not null,
  `content`     text                default null,
  primary key (`id`)
)
  engine = InnoDB
  default charset = utf8;

-- ----------------------------
-- Table structure for `recharge`
-- ----------------------------
drop table if exists `recharge`;
create table `recharge` (
  `id`            bigint(20)          not null auto_increment,
  `create_time`   datetime            not null,
  `update_time`   datetime            not null,
  `client_id`     bigint(20)          not null,
  `product_id`    bigint(20)          not null,
  `trade_no`      varchar(32)         not null,
  `contract_no`   varchar(64)         not null,
  `bill_plan`     tinyint(3) unsigned not null,
  `recharge_type` int(11)             not null,
  `amount`        decimal(16, 2)      not null,
  `balance`       decimal(16, 2)      not null,
  `start_date`    date                         default null,
  `end_date`      date                         default null,
  `unit_amt`      decimal(16, 2)               default null,
  `remark`        varchar(512)                 default null,
  `manager_id`    bigint(20)          not null,
  primary key (`id`),
  unique key `uk_prod_trade_no` (`trade_no`) using btree
)
  engine = InnoDB
  default charset = utf8;

-- ----------------------------
-- Table structure for `sistem`
-- ----------------------------
drop table if exists `sistem`;
create table `sistem` (
  `id`          bigint(20)   not null auto_increment,
  `create_time` datetime     not null,
  `update_time` datetime     not null,
  `name`        varchar(64)  not null,
  `value`       varchar(128) not null,
  primary key (`id`),
  unique key `uk_param_name` (`name`) using btree
)
  engine = InnoDB
  default charset = utf8;

-- ----------------------------
-- Table structure for `stats`
-- ----------------------------
drop table if exists `stats`;
create table `stats` (
  `id`               bigint(20)          not null auto_increment,
  `create_time`      datetime            not null,
  `update_time`      datetime            not null,
  `client_increment` int(11)             not null,
  `client_request`   bigint(20)          not null,
  `client_recharge`  decimal(16, 2)      not null,
  `stats_date`       date                not null,
  `stats_year`       int(11)             not null,
  `stats_month`      int(11)             not null,
  `stats_week`       int(11)             not null,
  `stats_day`        int(11)             not null,
  `stats_hour`       tinyint(3) unsigned not null,
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
  `id`            bigint(20)          not null auto_increment,
  `create_time`   datetime            not null,
  `update_time`   datetime            not null,
  `recharge_type` tinyint(3) unsigned not null,
  `stats_year`    int(10) unsigned    not null,
  `stats_month`   tinyint(3) unsigned not null,
  `stats_week`    tinyint(3) unsigned not null,
  `stats_day`     tinyint(3) unsigned not null,
  `stats_hour`    tinyint(3) unsigned not null,
  `stats_date`    date                not null,
  `amount`        decimal(16, 2)      not null,
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
  `remark`      varchar(256)        null,
  primary key (`id`)
)
  engine = InnoDB
  default charset = utf8;
