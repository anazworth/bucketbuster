# Bucket Buster
### A simple Token-Bucket API rate limiter

Bucket Buster is a simple API rate limiter that uses the Token-Bucket algorithm to limit the number of requests per second by each IP address. It's written in Java using the Javalin framework and uses Redis as a backend for storing the buckets.

### How to run:

1. Clone the repository
2. Run `docker-compose up` to start all services
3. Make a GET request to `http://localhost:8080/` to test the API
4. View the Log dashboard at `http://localhost:5601`