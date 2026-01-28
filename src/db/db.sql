create database mini_dish_db;

\c mini_didh_db

create user "mini_dish_db_manager" with password '123456';
grant connect on database mini_dish_db to mini_dish_db_manager;
grant USAGE ON SCHEMA public TO mini_dish_db_manager;
GRANT SELECT ON dish TO mini_dish_db_manager;
GRANT SELECT ON ingredient TO mini_dish_db_manager;
GRANT SELECT ON ALL TABLES IN SCHEMA public TO mini_dish_db_manager;
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA public TO mini_dish_db_manager;


