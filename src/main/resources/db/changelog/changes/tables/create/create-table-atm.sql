--liquibase formatted sql
--changeset <antonpvliuk>:<create-table-atm>
CREATE TABLE `atm` (
                     `id` BIGINT PRIMARY KEY,
                     `address` VARCHAR(255) NOT NULL
);
