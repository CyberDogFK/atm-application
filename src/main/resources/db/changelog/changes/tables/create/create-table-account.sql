--liquibase formatted sql
--changeset <antonpvliuk>:<create-table-account>
CREATE TABLE `account` (
                         `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                         `user_id` BIGINT,
                         `currency_id` BIGINT,
                         FOREIGN KEY (`user_id`) REFERENCES users(id) ON DELETE CASCADE,
                         FOREIGN KEY (`currency_id`) REFERENCES users(id) ON DELETE SET NULL
);
