insert into
  `product`
  (`create_time`, `update_time`, `type`, `custom`, `code`, `name`, `cost_amt`, `remark`, `enabled`)
values
  (now(), now(), 1, 0, 'FIN-TGK', '通过客', '0', '详细的报表反馈已通过平台注册并放款的客户信息', 1),
  (now(), now(), 1, 0, 'FIN-YLK', '优良客', '0', '统计出数据库内信贷中无不良记录的优质客户报表', 1),
  (now(), now(), 1, 0, 'FIN-JDK', '拒贷客', '0', '识别出被平台拒绝过贷款的历史记录及其详细信息', 1);