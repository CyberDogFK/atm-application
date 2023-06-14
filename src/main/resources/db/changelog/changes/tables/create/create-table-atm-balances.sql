--liquibase formatted sql
--changeset <antonpvliuk>:<create-table-atm-balance>
CREATE TABLE `atm_balances` (
    `atm_id` bigint,
    `balance_id` bigint,
    foreign key (`atm_id`) references atm(id),
    foreign key (`balance_id`) references atmbalance(id)
);
