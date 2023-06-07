--liquibase formatted sql
--changeset <antonpvliuk>:<insert-role-table>
insert into `role` (`name`) values ('ADMIN');
insert into `role` (`name`) values ('USER');
