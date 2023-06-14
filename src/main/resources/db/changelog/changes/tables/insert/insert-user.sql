--liquibase formatted sql
--changeset <antonpvliuk>:<insert-role-table>
insert into `users` (`name`, `password`, `role_id`) values ('bob',
                                                            '$2a$10$GHX9hbHw.pdhHjoGjPPiuOqx.kZsXlqBSlztDkwuaWF5o99app3EK',
                                                            '1');