insert into
  `function`
  (id, create_time, update_time, name, seq_no, level, parent_id, enabled)
values
  (12403, now(), now(), '设置测试token', 1, 4, 12400, 1);

insert into
  `sistem`
  (create_time, update_time, name, value)
values
  (now(), now(), 'TEST_TOKEN', '');