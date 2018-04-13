-- sistem data
insert into
  `sistem`
  (create_time, update_time, name, value)
values
  (now(), now(), 'CLIENT_SUB_USER_QTY', '5'),
  (now(), now(), 'SERVICE_QQ', '3521720977');

-- function data
insert into
  `function`
  (id, create_time, update_time, name, seq_no, level, parent_id, enabled)
values
  (10000, now(), now(), '系统管理', 1, 1, 0, 1),
  (11000, now(), now(), '账户管理', 1, 2, 10000, 1),
  (11100, now(), now(), '后台账号管理', 1, 3, 11000, 1),
  (11101, now(), now(), '添加', 1, 4, 11100, 1),
  (11102, now(), now(), '编辑', 1, 4, 11100, 1),
  (11103, now(), now(), '禁用', 1, 4, 11100, 1),
  (11200, now(), now(), '权限分组', 1, 3, 11000, 1),
  (11201, now(), now(), '添加', 1, 4, 11200, 1),
  (11202, now(), now(), '编辑', 1, 4, 11200, 1),
  (11203, now(), now(), '禁用', 1, 4, 11200, 1),
  (12000, now(), now(), '系统设置', 1, 2, 10000, 1),
  (12100, now(), now(), '栏目管理', 1, 3, 12000, 1),
  (12103, now(), now(), '编辑', 1, 4, 12100, 1),
  (12200, now(), now(), '充值类型', 1, 3, 12000, 1),
  (12201, now(), now(), '添加类型', 1, 4, 12200, 1),
  (12202, now(), now(), '是否显示', 1, 4, 12200, 1),
  (12203, now(), now(), '编辑', 1, 4, 12200, 1),
  (12300, now(), now(), '行业分类', 1, 3, 12000, 1),
  (12301, now(), now(), '添加行业', 1, 4, 12300, 1),
  (12302, now(), now(), '是否显示', 1, 4, 12300, 1),
  (12303, now(), now(), '编辑', 1, 4, 12300, 1),
  (12305, now(), now(), '增加子行业', 1, 4, 12300, 1),
  (12400, now(), now(), '其他设置', 1, 3, 12000, 1),
  (12401, now(), now(), '设置客服', 1, 4, 12400, 1),
  (12402, now(), now(), '设置子账号数量', 1, 4, 12400, 1),
  (21000, now(), now(), '产品管理', 1, 2, 10000, 1),
  (21200, now(), now(), '产品列表', 1, 3, 21000, 1),
  (21202, now(), now(), '编辑', 1, 4, 21200, 1),
  (21203, now(), now(), '禁用', 1, 4, 21200, 1),
  (30000, now(), now(), '客户管理', 1, 1, 0, 1),
  (31000, now(), now(), '客户管理', 1, 2, 30000, 1),
  (31100, now(), now(), '客户管理', 1, 3, 31000, 1),
  (31101, now(), now(), '查看', 1, 4, 31100, 1),
  (31102, now(), now(), '添加', 1, 4, 31100, 1),
  (31103, now(), now(), '编辑', 1, 4, 31100, 1),
  (31104, now(), now(), '冻结', 1, 4, 31100, 1),
  (31105, now(), now(), '解冻', 1, 4, 31100, 1),
  (31107, now(), now(), '重置密码', 1, 4, 31100, 1),
  (31108, now(), now(), '开通服务', 1, 4, 31100, 1),
  (31109, now(), now(), '续签', 1, 4, 31100, 1),
  (31111, now(), now(), '产品历史充值记录导出', 1, 4, 31100, 1),
  (31112, now(), now(), '产品消费明细导出', 1, 4, 31100, 1),
  (50000, now(), now(), '财务管理', 1, 1, 0, 1),
  (51000, now(), now(), '核对管理', 1, 2, 50000, 1),
  (51100, now(), now(), '充值记录', 1, 3, 51000, 1),
  (51101, now(), now(), '查询', 1, 4, 51100, 1),
  (51102, now(), now(), '导出', 1, 4, 51100, 1),
  (51200, now(), now(), '账单核对', 1, 3, 51000, 1),
  (51201, now(), now(), '查询', 1, 4, 51200, 1),
  (51202, now(), now(), '导出', 1, 4, 51200, 1),
  (61000, now(), now(), '新闻动态', 1, 2, 10000, 1),
  (61100, now(), now(), '文章列表', 1, 3, 61000, 1),
  (61101, now(), now(), '添加', 1, 4, 61100, 1),
  (61102, now(), now(), '编辑', 1, 4, 61100, 1),
  (61103, now(), now(), '删除', 1, 4, 61100, 1);

-- group data
insert into
  `group`
  (id, create_time, update_time, name, enabled)
values
  (1, now(), now(), '系统管理组', 1);

-- group_function data
insert into
  `group_function`
  (create_time, update_time, group_id, privilege_id)
values
  (now(), now(), 1, 10000),
  (now(), now(), 1, 11000),
  (now(), now(), 1, 11100),
  (now(), now(), 1, 11101),
  (now(), now(), 1, 11102),
  (now(), now(), 1, 11103),
  (now(), now(), 1, 11200),
  (now(), now(), 1, 11201),
  (now(), now(), 1, 11202),
  (now(), now(), 1, 11203),
  (now(), now(), 1, 12000),
  (now(), now(), 1, 12100),
  (now(), now(), 1, 12103),
  (now(), now(), 1, 12200),
  (now(), now(), 1, 12201),
  (now(), now(), 1, 12202),
  (now(), now(), 1, 12203),
  (now(), now(), 1, 12300),
  (now(), now(), 1, 12301),
  (now(), now(), 1, 12302),
  (now(), now(), 1, 12303),
  (now(), now(), 1, 12305),
  (now(), now(), 1, 12400),
  (now(), now(), 1, 12401),
  (now(), now(), 1, 12402),
  (now(), now(), 1, 21000),
  (now(), now(), 1, 21200),
  (now(), now(), 1, 21202),
  (now(), now(), 1, 21203);

-- user data, password: admin123
insert into
  `user`
  (id, create_time, update_time, username, password, name, group_id, enabled, role_type, pacify, alarm)
values
  (1, now(), now(), 'admin', '0c909a141f1f2c0a1cb602b0b2d7d050', '管理员', 1, 1, 1, 0, 0);

-- user_function data
insert into
  `user_function`
  (create_time, update_time, manager_id, privilege_id)
values
  (now(), now(), '1', '10000'),
  (now(), now(), '1', '11000'),
  (now(), now(), '1', '11100'),
  (now(), now(), '1', '11101'),
  (now(), now(), '1', '11102'),
  (now(), now(), '1', '11103'),
  (now(), now(), '1', '11200'),
  (now(), now(), '1', '11201'),
  (now(), now(), '1', '11202'),
  (now(), now(), '1', '11203'),
  (now(), now(), '1', '12000'),
  (now(), now(), '1', '12100'),
  (now(), now(), '1', '12103'),
  (now(), now(), '1', '12200'),
  (now(), now(), '1', '12201'),
  (now(), now(), '1', '12202'),
  (now(), now(), '1', '12203'),
  (now(), now(), '1', '12300'),
  (now(), now(), '1', '12301'),
  (now(), now(), '1', '12302'),
  (now(), now(), '1', '12303'),
  (now(), now(), '1', '12305'),
  (now(), now(), '1', '12400'),
  (now(), now(), '1', '12401'),
  (now(), now(), '1', '12402'),
  (now(), now(), '1', '21000'),
  (now(), now(), '1', '21200'),
  (now(), now(), '1', '21202'),
  (now(), now(), '1', '21203');

-- product data
insert into
  `product`
  (create_time, update_time, type, custom, code, name, cost_amt, remark, enabled)
values
  (now(), now(), 1, 0, 'FIN-CDK', '常欠客', '0', '常欠客', 1),
  (now(), now(), 1, 0, 'FIN-HMD', '黑名单', '0', '黑名单', 1);