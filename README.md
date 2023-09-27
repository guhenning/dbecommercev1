# dbecommercev1
This is a project of a E-Commerce Data Base using Spring Boot and JWT.
Diagram of the Project
![E-Commerce DB](https://github.com/guhenning/dbecommercev1/assets/39813412/7679c825-afa7-400f-8aed-387ffb8ed648)
Swagger URL: http://localhost:8080/swagger-ui/index.html#/
![image](https://github.com/guhenning/dbecommercev1/assets/39813412/4b8da618-7051-4f42-92e2-1fdc63b51d12)


In this project I'm using:
Spring Boot
JPA Repository to Map the SQL queries, also using Pagination and Sorting.
Swagger for the Documentation.
External API's to get the address data from the Postal Code. 

Admin can Get all Items at Once by User is limited to 100 items per Page.
Only Admin can Add Remove or Delete Brands Categories and Items.
To Add or Remove Item to Cart and also to made the Payment is used the JWT to check if the user matches the cart owner.





