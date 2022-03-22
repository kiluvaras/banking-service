# Questions and answers block

### Explanation of important choices in your solution
* For the tech stack I chose:
  * Mapstruct as a mapper because it is one of the best performing mappers out there
  * Flyway as a migration tool so that it would be easy to do migrations
  * Lombok to decrease the amount of boilerplate code
* Controller layer exposes only DTOs, not entities

### Estimate on how many transactions can your account application can handle per second on your development machine
* To be honest I'm not sure about how to calculate this

### Describe what do you have to consider to be able to scale applications horizontally
* Microservice architecture
* Run multiple instances of said microservices in the cloud and have a load balancer even out the workload

### Things I would improve / do differently if I had more time
* Split up different messages into different queues in RabbitMQ
* Come up with a solution to better running integration tests with Mybatis