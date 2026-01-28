create type unit_type as enum ('PCS','KG','L');
create table dish_ingredient (
    id serial,
    id_dish int,
    id_ingredient int,
    quantity_required numeric,
    unit unit_type
);

ALTER TABLE Dish
    ADD COLUMN IF NOT EXISTS selling_price NUMERIC;

Alter table ingredient drop column id_dish;

Alter table ingredient drop column required_quantity;