# Item Booking API
This is an Item Booking REST API. It was made using Java, JPA, Lombok, Javalin and Hibernate.

# Purpose
Backend project for an educational institution to manage and track items like camera, microphones
etc, in the medialab and makerlab.

# Item Booking Endpoint Table

| Endpoints                               | Method | Description                 |
|:----------------------------------------|:-------|:----------------------------|
| api/items                               | GET    | Get all items               |
| api/items/{id}                          | GET    | Get an item by ID           |
| api/items                               | POST   | Add a new item              |
| api/items/{id}                          | PUT    | Update an item              |
| api/items/{id}                          | DELETE | Delete an item              |
| api/items/{itemId}/students/{studentId} | PUT    | Assign an item to a student |
| api/items/populate                      | POST   | Populate the database       |

# Theoretical questions
* **3.3.3**: Why is PUT used for assigning an item to a student instead of POST?
  - PUT is an update, whereas POST is the same as creating something new. Since we just want to assign an item to a student and not create a completely new student, we use PUT.

# Security Endpoint Table

| Endpoints                         | Method | Secured      | Description                     |
|:----------------------------------|:-------|:------------:|:--------------------------------|
| api/auth/register                 | POST   | ❌          | Create a new user               |
| api/auth/login                    | POST   | ❌          | Auth a user, return JWT token   |
| api/secured/demo                  | GET    | ✅          | Tests a users token after login |

❌ = Not secured

✅ = User secured

🔒 = Admin secured

