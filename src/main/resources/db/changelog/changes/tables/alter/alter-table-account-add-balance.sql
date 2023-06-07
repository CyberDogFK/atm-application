-- ALTER TABLE PUBLIC.EMPLOYEE ADD NEW_COL VARCHAR(25)

--liquibase formatted sql
--changeset <antonpvliuk>:<alter-table-account-add-balance>
ALTER TABLE PUBLIC.account ADD BALANCE NUMERIC