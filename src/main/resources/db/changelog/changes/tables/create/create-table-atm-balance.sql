--liquibase formatted sql
--changeset <antonpvliuk>:<create-table-atm-balance>
CREATE TABLE `atmbalance` (
                            `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                            `atm_id` BIGINT,
                            `currency_id` BIGINT,
                            `balance` NUMERIC(32, 2),
                            FOREIGN KEY (`atm_id`) REFERENCES atm(id),
                            FOREIGN KEY (`currency_id`) REFERENCES currency(id)
);
