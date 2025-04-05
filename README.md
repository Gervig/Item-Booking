# Item Booking API
This is an Item Booking REST API. It was made using Java, JPA, Lombok, Javalin and Hibernate.

# Purpose
Backend project for an educational institution to manage and track items like camera, microphones
etc, in the medialab and makerlab.

# Endpoint Table

| Endpoints                         | Method | Secured      | Description                     |
|:----------------------------------|:-------|:------------:|:--------------------------------|
| api/auth/register                 | POST   | ❌          | Create a new user               |
| api/auth/login                    | POST   | ❌          | Auth a user, return JWT token   |
| api/secured/demo                  | GET    | ✅          | Tests a users token after login |

❌ = Not secured

✅ = User secured

🔒 = Admin secured