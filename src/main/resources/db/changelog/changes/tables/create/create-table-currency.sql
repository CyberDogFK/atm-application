--liquibase formatted sql
--changeset <antonpvliuk>:<create-table-currency>
CREATE TABLE `currency` (
                          `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                          `shortName` VARCHAR(255) NOT NULL,
                          `name` VARCHAR(255) NOT NULL
);
