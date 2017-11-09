#/bin/bash

#run drop.sql batch file to drop existing tables
mysql CS144 < drop.sql

#run create.sql batch file to create database and tables
mysql CS144 < create.sql

#Compile and run parser to generate appropriate load files
ant
ant run-all

#Duplicate removal
sort bids.dat | uniq > uniqueBids.dat
rm bids.dat
sort users.dat | uniq > uniqueUsers.dat
rm users.dat
sort categories.dat | uniq > uniqueCategories.dat
rm categories.dat
sort items.dat | uniq > uniqueItems.dat
rm items.dat

#run the load.sql batch file to load the data
mysql CS144 < load.sql

#remove all temporary files
rm *.dat
rm -rf bin
