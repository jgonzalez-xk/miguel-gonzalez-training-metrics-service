# miguel-gonzalez-training-metrics-service

## Instructions

## Start

On first run application you should run `make`.

Then you should run `make start`.

## Config

Config variables are found on the `.env` file.

`make config` loads db and flyway config variables into `application.properties` & `flyway.conf`.

## Clean

`make clean` cleans the project and the database using `mvn clean & flyway:clean`.

## DB & DB Cleanup

DB is running on docker.

`make db_clean` is used to remove the container, but also you can use just `make clean` to use `flyway:clean` without destroying the container.

`make db_migrate` is used to run the migrations.

## Testing

`make test` runs the unit tests.

You can also test the REST endpoints using swagger in [here](http://localhost:8080/swagger-ui/index.html)
