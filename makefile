include ./.env
export 

.PHONY: all db db_migrate compile start

all: config db db_migrate compile start

config:
	@echo "***************************Configuring needed variables***************************"
	echo "flyway.user=${DB_USER}" >> ./flyway.conf
	echo "flyway.password=${DB_PSWRD}" >> ./flyway.conf
	echo "flyway.schemas=${DB_SCHEMA}" >> ./flyway.conf
	echo "flyway.url=jdbc:postgresql://localhost:5432/${DB_NAME}" >> ./flyway.conf
	echo "flyway.locations=filesystem:db/migrations" >> ./flyway.conf
	echo "spring.datasource.url=jdbc:postgresql://localhost:5432/${DB_NAME}" >> ./application.properties
	echo "spring.datasource.username=${DB_USER}" >> ./application.properties
	echo "spring.datasource.password=${DB_PSWRD}" >> ./application.properties
 
compile:
	@echo "***************************Compiling***************************"
	mvn clean install

db:
	@echo "***************************Seting up postgresql container ***************************"
	docker run --name ${CONTAINER_NAME} -e POSTGRES_USER=${DB_USER} -e POSTGRES_PASSWORD=${DB_PSWRD} -e POSTGRES_DB=${DB_NAME} -p ${DB_PORT}:5432 -d ${CONTAINER_NAME}

db_migrate:
	@echo "***************************Migrating db using flyway***************************"
	mvn flyway:migrate -Dflyway.configFiles=./flyway.conf

db_clean:
	@echo "***************************Stoping & removing db container***************************"
	docker stop ${CONTAINER_NAME}
	docker rm ${CONTAINER_NAME}

clean:
	@echo "***************************Cleaning***************************"
	mvn clean
	mvn flyway:clean -Dflyway.configFiles=./flyway.conf

start:
	java -jar target/metrics-0.0.1-SNAPSHOT.jar