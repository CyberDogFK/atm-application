--liquibase formatted sql
--changeset <antonpvliuk>:<create-table-users>
CREATE TABLE `users` (
                       `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                       `name` VARCHAR(255) NOT NULL,
                       `password` VARCHAR(255) NOT NULL,
                       `role_id` BIGINT,
                       FOREIGN KEY (`role_id`) REFERENCES role(id)
);
