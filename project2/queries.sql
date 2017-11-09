SELECT COUNT(*) FROM User;
SELECT COUNT(*) FROM Item WHERE Location = "New York" COLLATE utf8_bin;
SELECT COUNT(*) FROM (SELECT ItemID FROM Category GROUP BY ItemID HAVING COUNT(ItemID)=4) AS T;

SELECT ItemID FROM Item
WHERE Ends>'2001-12-20 12:00:00' 
AND Number_of_Bids>0 
AND Currently = (SELECT MAX(Currently) FROM Item WHERE Ends>'2001-12-20 12:00:00' AND Number_of_Bids>0);

SELECT COUNT(DISTINCT SellerID) FROM User, Item WHERE Item.SellerID=User.UserID AND User.Rating > 1000;
SELECT COUNT(DISTINCT UserID) FROM Item, Bid WHERE Item.SellerID = Bid.UserID;
SELECT COUNT(DISTINCT Category) FROM Item, Category WHERE Item.ItemID=Category.ItemID AND Item.Currently > 100 && Item.Number_of_Bids > 0;
