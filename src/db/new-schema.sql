create type unit_type as enum ('PCS','KG','L');
create table DishIngredient (
    id serial,
    id_dish int,
    id_ingredient int,
    quantity_required numeric,
    unit unit_type
);

ALTER TABLE Dish
    ADD COLUMN IF NOT EXISTS selling_price NUMERIC;
