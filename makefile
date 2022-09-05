include ./.env
export 

.PHONY: all db db_migrate compile start

all: config db db_migrate compile start

common_config:
	@echo "***************************Configuring needed variables***************************"
	rm  ./flyway.conf && touch  ./flyway.conf
	rm ./src/main/resources/application.properties && cp ./src/main/resources/application.properties.template ./src/main/resources/application.properties
	echo "flyway.user=${DB_USER}" >> ./flyway.conf
	echo "flyway.password=${DB_PSWRD}" >> ./flyway.conf
	echo "flyway.schemas=${DB_SCHEMA}" >> ./flyway.conf
	echo "spring.datasource.username=${DB_USER}" >> ./src/main/resources/application.properties
	echo "spring.datasource.password=${DB_PSWRD}" >> ./src/main/resources/application.properties
	
	

config: common_config
	echo "flyway.url=jdbc:postgresql://localhost:${DB_PORT}/${DB_NAME}" >> ./flyway.conf
	echo "spring.datasource.url=jdbc:postgresql://localhost:${DB_PORT}/${DB_NAME}" >> ./src/main/resources/application.properties
	
container_config: common_config
	echo "flyway.url=jdbc:postgresql://${DB_CONTAINER_NAME}:${DB_PORT}/${DB_NAME}" >> ./flyway.conf	
	echo "spring.datasource.url=jdbc:postgresql://${DB_CONTAINER_NAME}:${DB_PORT}/${DB_NAME}" >> ./src/main/resources/application.properties
 
compile:
	@echo "***************************Compiling***************************"
	mvn clean install

docker_compose:
	@echo "***************************Starting***************************"
	docker-compose up

kubernetes_db:
	kubectl apply -f ./kubernetes/db.yaml

kubernetes_app:
	@echo "***************************Starting***************************"
	kubectl apply -f ./kubernetes/deployment.yaml

db:
	@echo "***************************Seting up postgresql container ***************************"
	docker run --name ${DB_CONTAINER_NAME} -e POSTGRES_USER=${DB_USER} -e POSTGRES_PASSWORD=${DB_PSWRD} -e POSTGRES_DB=${DB_NAME} -p ${DB_PORT}:5432 -d postgres

db_migrate:
	@echo "***************************Migrating db using flyway***************************"
	mvn flyway:migrate -Dflyway.configFiles=./flyway.conf

db_clean:
	@echo "***************************Stoping & removing db container***************************"
	docker stop ${DB_CONTAINER_NAME}
	docker rm ${DB_CONTAINER_NAME}

kubernetes_clean:
	@echo "***************************Cleaning***************************"	
	kubectl delete -f ./kubernetes/db.yaml
	kubectl delete -f ./kubernetes/deployment.yaml

clean:
	@echo "***************************Cleaning***************************"
	mvn clean
	mvn flyway:clean -Dflyway.configFiles=./flyway.conf
	mvn flyway:migrate -Dflyway.configFiles=./flyway.conf

clean_all: clean db_clean

test:
	mvn test

start:
	@echo "***************************Starting***************************"
	java -jar target/metrics-0.0.1-SNAPSHOT.jar

container_start: container_config docker_compose

kubernetes_start: kubernetes_db kubernetes_app