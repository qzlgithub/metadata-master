insert into
  `product`
  (create_time, update_time, type, custom, code, name, cost_amt, remark, enabled)
values
  (now(), now(), 1, 0, 'FIN-TGK', '通过客', '0', '通过客', 1),
  (now(), now(), 1, 0, 'FIN-YLK', '优良客', '0', '优良客', 1),
  (now(), now(), 1, 0, 'FIN-JDK', '拒贷客', '0', '拒贷客', 1);