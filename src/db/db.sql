create database mini_dish_db;

\c mini_didh_db

create user 'mini_dish_db_manager' with password '123456';
grant connection on database mini_dish_db to mini_dish_db_manager;
grant create , select , insert, update, delete on mini_dish_db.* to 'mini_dish_db_manager';
flush privileges;

