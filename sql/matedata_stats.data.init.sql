-- job data
insert into
  `job`
  (code, name, last_suc_time)
values
  ('STATS_ALL', '统计各维度', null),
  ('STATS_RECHARGE', '统计充值', null),
  ('CLIENT_REMIND', '客户服务提醒', null),
  ('STATS_REQUEST', '统计请求量', null);

-- warning_setting data
insert into
  `warning_setting`
  (code, create_time, update_time, content, type, send, play, file_name, file_path, general_limit, severity_limit, warning_limit, enabled)
values
  ('PRODUCT_ANOMALY', now(), now(), '请求异常', '1', '0', '0', null, null, '30', '60', '90', '1'),
  ('CLIENT_FAILURE', now(), now(), '请求失败', '2', '0', '0', null, null, '30', '60', '90', '1');
