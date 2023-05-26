insert into account (id) values (1);
insert into account (id) values (2);

insert into activity (id, timestamp, owner_account_id, source_account_id, target_account_id, money)
values (1, '2018-08-08 08:00:00.0', 1, 1, 2, 500);

insert into activity (id, timestamp, owner_account_id, source_account_id, target_account_id, money)
values (2, '2018-08-08 08:00:00.0', 2, 1, 2, 500);

insert into activity (id, timestamp, owner_account_id, source_account_id, target_account_id, money)
values (3, '2018-08-09 10:00:00.0', 1, 2, 1, 1000);

insert into activity (id, timestamp, owner_account_id, source_account_id, target_account_id, money)
values (4, '2018-08-09 10:00:00.0', 2, 2, 1, 1000);

insert into activity (id, timestamp, owner_account_id, source_account_id, target_account_id, money)
values (5, '2019-08-09 09:00:00.0', 1, 1, 2, 1000);

insert into activity (id, timestamp, owner_account_id, source_account_id, target_account_id, money)
values (6, '2019-08-09 09:00:00.0', 2, 1, 2, 1000);

insert into activity (id, timestamp, owner_account_id, source_account_id, target_account_id, money)
values (7, '2019-08-09 10:00:00.0', 1, 2, 1, 1000);

insert into activity (id, timestamp, owner_account_id, source_account_id, target_account_id, money)
values (8, '2019-08-09 10:00:00.0', 2, 2, 1, 1000);