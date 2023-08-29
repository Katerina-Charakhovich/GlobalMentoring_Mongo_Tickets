# GlobalMentoring_Mongo_Tickets
Task 1: Backend Database Migration
Cost: 2 points.

Move one of your pet applications/mentoring projects with DAO and simple CRUD to MongoDB. In case you don't have such projects you can use the sample project attached (ticket-booking.zip)

Install MongoDB and use the corresponding Java driver.

Define the data model for MongoDB (direct table-collection mapping is not the best idea).
Write data migration job (via SQL and MongoDriver operations).
Use an aggregation mechanism to get grouped results from the database.
(Optional, +5 bonus points) Run this job in one transaction for the SQL database and in one multi-document transaction for MongoDB.
(Optional, +5 bonus points) Use appropriate indices to improve query performance.
(Optional, +10 bonus points) Make a replica set with 1 primary and 2 secondary nodes, and execute your tests/client code over the MongoDB cluster.
