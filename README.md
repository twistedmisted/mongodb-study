# Practice Spring Data MongoDB

This simple project used for study some aspect of Spring Data MongoDB. It contains:
- 4 collections (users, chats, usersChats, messages);
- parent-children and built-in objects are represented as embedded model;
- one-many and reference are represented as normalized model;
- CRUD operation;
- simple key-value query with 2 conditions:
  ```
  @Query("{ 'name' : ?0, 'createdAt' :  { $lt :  ?1 } }")
  Optional<UserDocument> findByNameAndCreatedAt(String name, Date createdAt);
  ```
- example of using aggregation with 4 stages:
  ```
  @Aggregation(pipeline = {
          "{ $lookup : { from : 'users', localField : 'sender', foreignField : '_id', as : 'sender_info' } }",
          "{ $unwind : { path : '$sender_info' } }",
          "{ $match : { 'sender_info.name' : ?0 } }",
          "{ $group : { _id : '$sender_info.name', count : { $sum : 1 } } }"
  })
  int getMessageCountByUserName(String name); 
  ```