# miguel-gonzalez-training-metrics-service

## Instructions

## Commands

| Command          |                           Description                            |
| :--------------- | :--------------------------------------------------------------: |
| config           |                   Creates needed config files                    |
| container_config |      Creates needed config files to run as docker container      |
| compile          |                           Compiles app                           |
| docker_compose   |          Runs docker-compose up / Runs app on container          |
| db               |                        Runs db container                         |
| db_migrate       |                        Runs db migrations                        |
| db_clean         |                       Removes db container                       |
| kubernetes_clean |                       Deletes deployments                        |
| clean            |                            Cleans app                            |
| test             |                            Runs tests                            |
| start            |               Starts java app using compiled files               |
| container_start  |                Configs and runs app on container                 |
| kubernetes_start | Starts app on kubernetes based on the deployment on ./kubernetes |

## Start

On first run application you should run `make` or `make all`.

Then you should run `make start`.

If you want to run it on docker use `make container_start`.

## Config

Config variables are found on the `.env` file.

| Variable Name     |       Description       |
| :---------------- | :---------------------: |
| DB_USER           |    Database username    |
| DB_PSWRD          |    Database password    |
| DB_PORT           |      Database port      |
| DB_NAME           |      Database name      |
| DB_SCHEMA         |     Database Schema     |
| DB_CONTAINER_NAME | Postgres container name |

`make config` loads db and flyway config variables into `application.properties` & `flyway.conf`.
`make container_config` changes connection names according to the container's names.

## Clean

`make clean` cleans the project and the database using `mvn clean & flyway:clean`.
`make clean_all` cleans db and project.

## DB & DB Cleanup

DB is running on docker.

`make db_clean` is used to remove the container, but also you can use just `make clean` to use `flyway:clean` without destroying the container.

`make db_migrate` is used to run the migrations.

## Testing

`make test` runs the unit tests.

You can also test the REST endpoints using swagger in [here](http://localhost:8080/swagger-ui/index.html)
