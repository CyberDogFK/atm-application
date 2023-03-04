--liquibase formatted sql
--changeset <antonpvliuk>:<create-table-role>
CREATE TABLE `role` (
                      `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                      `name` VARCHAR(255) NOT NULL
);
