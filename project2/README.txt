1. Items(ItemID [key], Name, Currently, Buy_Price, First_Bid, Number_of_Bids, Started, Ends, Location, Country, SellerID, Description);
Bids(ItemID, UserID, Amount, Time);
Users(UserID, Rating);
Categories(ItemID, Category);

2. 
ItemID -> Name
ItemID -> Currently
ItemID -> Buy_Price
ItemID -> First_Bid
ItemID -> Number_of_Bids
ItemID -> Started
ItemID -> Ends
ItemID -> Location
ItemID -> Country
ItemID -> SellerID
ItemID -> Description

ItemID, UserID -> Amount
ItemID, UserID -> Time

UserID -> Rating  (not true if the Bidder rating differs from Seller)

3. Yes
4. Yes
