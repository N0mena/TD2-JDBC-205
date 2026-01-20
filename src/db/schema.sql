create type dish_category as enum ('START' , 'MAIN' , 'DESSERT');

create table Dish(
                     id serial primary key ,
                     name varchar(255) not null,
                     dish_type dish_category

);


create type ingredient_category as enum ('VEGETABLE', 'ANIMAL', 'MARINE', 'DAIRY', 'OTHER');

create table Ingredient (
    id serial primary key ,
    name varchar(255) not null,
    price numeric(10,2),
    category ingredient_category,
    id_dish integer references Dish(id)
);


ALTER TABLE ingredient
    ADD COLUMN IF NOT EXISTS required_quantity NUMERIC;

