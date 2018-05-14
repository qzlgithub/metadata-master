insert into
  `function`
  (id, create_time, update_time, name, seq_no, level, parent_id, enabled)
values
  (31200, now(), now(), '客户安抚', 1, 3, 31000, 1),
  (31201, now(), now(), '查看', 1, 4, 31200, 1),
  (31202, now(), now(), '标记安抚', 1, 4, 31200, 1),
  (31300, now(), now(), '服务提醒', 1, 3, 31000, 1),
  (31301, now(), now(), '查看', 1, 4, 31300, 1),
  (31302, now(), now(), '标记通知', 1, 4, 31300, 1);