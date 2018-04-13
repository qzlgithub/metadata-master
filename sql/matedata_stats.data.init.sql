-- job data
insert into
  `job`
  (code, name, last_suc_time)
values
  ('STATS_ALL', '统计各维度', null),
  ('STATS_RECHARGE', '统计充值', null),
  ('CLIENT_REMIND', '客户服务提醒', null),
  ('CLEAN_TRAFFIC', '清理定时缓存数据', null);
