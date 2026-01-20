insert into dish values (1,'Salade fraîche','START'),
        (2,'Poulet grillé','MAIN'),
       (3,'Riz aux légumes','MAIN'),
       (4,'Gâteau au chocolat','DESSERT'),
       (5,'Salade de fruits','DESSERT');

insert into Ingredient values (1,'Laitue',800.00, 'VEGETABLE',1),
       (2,'Tomate',600.00,'VEGETABLE',1),
       (3,'Poulet',4500.00,'ANIMAL',2),
       (4,'chocolat',3000.00,'OTHER',4),
       (5,'Beurre',2500.00,'DAIRY',4);

UPDATE ingredient SET required_quantity = 1   WHERE name = 'Laitue';
UPDATE ingredient SET required_quantity = 2   WHERE name = 'Tomate';
UPDATE ingredient SET required_quantity = 0.5 WHERE name = 'Poulet';
UPDATE ingredient SET required_quantity = NULL WHERE name = 'Chocolat';
UPDATE ingredient SET required_quantity = NULL WHERE name = 'Beurre';


select  * from ingredient;

insert into DishIngredient values (1,1,1,0.20,'KG'),
                                  (2,1,2,0.15,'KG'),
                                  (3,2,3,1.00,'KG'),
                                  (4,4,4,0.30,'KG'),
                                  (5,4,5,0.20,'KG')

UPDATE Dish SET selling_price = 3500.00   WHERE name = 'Salade fraîche';
UPDATE Dish SET selling_price = 12000.00   WHERE name = 'Poulet grillé';
UPDATE Dish SET selling_price = NULL WHERE name = 'Riz aux légumes';
UPDATE Dish SET selling_price = 8000.00 WHERE name = 'Gâteau au chocolat';
UPDATE Dish SET selling_price = NULL WHERE name = 'Salade de fruits';