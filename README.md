# Hibernate
All Hibernate programs from Basic CRUD to Advanced E-R/E-V Relationship.

Overview

This project demonstrates the use of Hibernate ORM in a Java application. It showcases the separation of concerns through distinct packages:

DAO: Contains Data Access Objects for CRUD operations.

POJOs: Houses Plain Old Java Objects representing database entities.

Tester: Includes test classes to validate the application's functionality.

Utils: Provides utility classes to support various operations.


src/

└── main/

    └── java/
    
        ├── dao/  # Data access operations 
        
        ├── pojo/      # Entity classes
        
        ├── tester/    # Test classes
        
        └── utils/     # Utility classes (Hibernate session management)

Technologies Used

Java -> Enterprise Edition 

Hibernate ORM ->  For object-relational mapping and database operations.

MySQL ->  Relational database.

Maven -> Build and dependency management (
