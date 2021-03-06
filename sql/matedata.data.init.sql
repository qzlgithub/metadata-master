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
  (now(), now(), 1, 0, 'FIN-HMD', '黑名单', '0', '挖掘出数据库中逾期天数达到过90天的客户信息', 1),
  (now(), now(), 1, 0, 'FIN-CDK', '常欠客', '0', '详细记录客户借贷历史中逾期次数时间和逾期天数', 1),
  (now(), now(), 1, 0, 'FIN-DTK', '多头客', '0', '高效精准的识别多家平台注册的客户并附详细报告', 1);

-- dict_industry data
insert into
  `dict_industry`
  (`create_time`, `update_time`, `code`, `name`, `seq_no`, `parent_id`, `enabled`)
values (now(), now(), 'CY0', '餐饮/食品', 1, 0, 1),
  (now(), now(), 'OF0', '线下零售', 1, 0, 1),
  (now(), now(), 'GW0', '网上购物', 1, 0, 1),
  (now(), now(), 'CY1', '餐饮', 1, 1, 1),
  (now(), now(), 'CS0', '超市', 1, 2, 1),
  (now(), now(), 'BLD0', '便利店', 1, 2, 1),
  (now(), now(), 'XS0', '线上商超', 1, 3, 1),
  (now(), now(), 'SP0', '食品', 1, 1, 1),
  (now(), now(), 'HL0', '生活/家居用品', 1, 0, 1),
  (now(), now(), 'ZX0', '生活/咨询服务', 1, 0, 1),
  (now(), now(), 'YL0', '娱乐/健身服务', 1, 0, 1),
  (now(), now(), 'JT0', '交通运输/票务旅游', 1, 0, 1),
  (now(), now(), 'WJY0', '网络媒体/计算机服务/游戏', 1, 0, 1),
  (now(), now(), 'WF0', '网上服务平台', 1, 0, 1),
  (now(), now(), 'JE0', '教育/医疗', 1, 0, 1),
  (now(), now(), 'SJ0', '生活缴费', 1, 0, 1),
  (now(), now(), 'JY0', '金融', 1, 0, 1),
  (now(), now(), 'ZDFM0', '自动贩卖机', 1, 2, 1),
  (now(), now(), 'BSG0', '百货/商圈/购物中心', 1, 2, 1),
  (now(), now(), 'QT0', '其他零售', 1, 2, 1),
  (now(), now(), 'PL0', '批发和零售业', 1, 2, 1),
  (now(), now(), 'JJ0', '家居建材/家居家纺', 1, 10, 1),
  (now(), now(), 'XB0', '数码家电/办公设备', 1, 10, 1),
  (now(), now(), 'MH0', '美妆/护肤', 1, 10, 1),
  (now(), now(), 'JPG0', '交通工具/配件/改装', 1, 10, 1),
  (now(), now(), 'ME0', '母婴用品/儿童玩具', 1, 10, 1),
  (now(), now(), 'FXS0', '服饰/箱包/饰品', 1, 10, 1),
  (now(), now(), 'HYJA0', '户外/运动/健身器材/安防', 1, 10, 1),
  (now(), now(), 'ZY0', '钟表/眼镜', 1, 10, 1),
  (now(), now(), 'HZY0', '黄金珠宝/钻石/玉石', 1, 10, 1),
  (now(), now(), 'ZFJ0', '咨询/法律咨询/金融咨询等', 1, 11, 1),
  (now(), now(), 'JS0', '家装/设计', 1, 11, 1),
  (now(), now(), 'JW0', '家政/维修服务', 1, 11, 1),
  (now(), now(), 'GHH0', '广告/会展/活动策划', 1, 11, 1),
  (now(), now(), 'RZL0', '人才中介机构/招聘/猎头', 1, 11, 1),
  (now(), now(), 'ZHJ0', '职业社交/婚介/交友', 1, 11, 1),
  (now(), now(), 'MY0', '苗木种植/园林绿化', 1, 11, 1),
  (now(), now(), 'MJ0', '美容/健身类会所', 1, 12, 1),
  (now(), now(), 'JX0', '俱乐部/休闲会所', 1, 12, 1),
  (now(), now(), 'YKW0', '游艺厅/KTV/网吧', 1, 12, 1),
  (now(), now(), 'ZC0', '租车', 1, 13, 1),
  (now(), now(), 'WK0', '物流/快递', 1, 13, 1),
  (now(), now(), 'LXS0', '旅行社', 1, 13, 1),
  (now(), now(), 'JP0', '机票/机票代理', 1, 13, 1),
  (now(), now(), 'YP0', '娱乐票务', 1, 13, 1),
  (now(), now(), 'JP1', '交通票务', 1, 13, 1),
  (now(), now(), 'LJD0', '旅馆/酒店/度假区', 1, 13, 1),
  (now(), now(), 'QT1', '其他', 1, 13, 1),
  (now(), now(), 'MZL0', '门户/咨询/论坛', 1, 14, 1),
  (now(), now(), 'YX0', '游戏', 1, 14, 1),
  (now(), now(), 'WZ0', '网络直播平台', 1, 14, 1),
  (now(), now(), 'RJJ0', '软件/建站/技术开发', 1, 14, 1),
  (now(), now(), 'WT0', '网络推广/网络广告', 1, 14, 1),
  (now(), now(), 'ZF0', '综合生活服务平台', 1, 15, 1),
  (now(), now(), 'LY0', '旅游服务平台', 1, 15, 1),
  (now(), now(), 'JPKX0', '教育/培训/考试缴费/学费', 1, 16, 1),
  (now(), now(), 'JEF0', '健身器械/医疗器械/非处方药品', 1, 16, 1),
  (now(), now(), 'WG0', '物业管理费', 1, 17, 1),
  (now(), now(), 'SJ1', '水电煤缴费/交通罚款等生活缴费', 1, 17, 1),
  (now(), now(), 'TJ0', '停车缴费', 1, 17, 1),
  (now(), now(), 'SY0', '事业单位', 1, 17, 1),
  (now(), now(), 'SH0', '其他生活缴费', 1, 17, 1),
  (now(), now(), 'GR0', '股票软件类', 1, 18, 1),
  (now(), now(), 'BX0', '保险业务', 1, 18, 1),
  (now(), now(), 'XY0', '信用还款', 1, 18, 1),
  (now(), now(), 'CS1', '催收', 1, 18, 1),
  (now(), now(), 'DDH0', '典当行', 1, 18, 1),
  (now(), now(), 'ZQ0', '政企', 1, 18, 1),
  (now(), now(), 'GH0', '挂号平台', 1, 16, 1);