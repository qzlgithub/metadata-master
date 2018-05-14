-- ----------------------------
-- Table structure for `warning_manage`
-- ----------------------------
drop table if exists `warning_manage`;
create table `warning_manage` (
  `id` bigint(20) not null auto_increment,
  `create_time` datetime not null,
  `update_time` datetime not null,
  `warning_code` varchar(16) not null,
  `product_id` bigint(20) default null,
  `client_id` bigint(20) default null,
  `third_code` varchar(32) default null,
  `user_id` bigint(20) default null,
  `dispose` tinyint(1) unsigned not null,
  `dispose_time` datetime default null,
  `remark` varchar(256) default null,
  primary key (`id`)
) engine = InnoDB
  default charset = utf8;

-- ----------------------------
-- Table structure for `warning_manage_detail`
-- ----------------------------
drop table if exists `warning_manage_detail`;
create table `warning_manage_detail` (
  `id` bigint(20) not null auto_increment,
  `create_time` datetime not null,
  `update_time` datetime not null,
  `time` datetime not null,
  `manage_id` bigint(20) not null,
  `product_id` bigint(20) not null,
  `client_id` bigint(20) not null,
  `client_user_id` bigint(20) not null,
  `request_ip` varchar(64) not null,
  `error_type` tinyint(3) unsigned default null,
  `third_code` varchar(32) default null,
  primary key (`id`)
) engine = InnoDB
  default charset = utf8;

-- ----------------------------
-- Table structure for `warning_out`
-- ----------------------------
drop table if exists `warning_out`;
create table `warning_out` (
  `id` bigint(20) not null auto_increment,
  `create_time` datetime not null,
  `update_time` datetime not null,
  `warning_code` varchar(16) not null,
  `product_id` bigint(20) default null,
  `client_id` bigint(20) default null,
  `third_code` varchar(32) default null,
  `level` tinyint(3) not null,
  `count` bigint(20) not null,
  `last_time` datetime not null,
  primary key (`id`)
) engine = InnoDB
  default charset = utf8;

-- ----------------------------
-- Table structure for `warning_pacify`
-- ----------------------------
drop table if exists `warning_pacify`;
create table `warning_pacify` (
  `id` bigint(20) not null auto_increment,
  `create_time` datetime not null,
  `update_time` datetime not null,
  `manage_id` bigint(20) not null,
  `client_id` bigint(20) not null,
  `user_id` bigint(20) default null,
  `dispose` tinyint(1) unsigned not null,
  `dispose_time` datetime default null,
  `remark` varchar(256) default null,
  primary key (`id`)
) engine = InnoDB
  default charset = utf8;

-- ----------------------------
-- Table structure for `warning_pacify_product`
-- ----------------------------
drop table if exists `warning_pacify_product`;
create table `warning_pacify_product` (
  `id` bigint(20) not null auto_increment,
  `create_time` datetime not null,
  `update_time` datetime not null,
  `pacify_id` bigint(20) not null,
  `product_id` bigint(20) not null,
  primary key (`id`)
) engine = InnoDB
  default charset = utf8;
