help:           ## Show this help.
	@fgrep -h "##" $(MAKEFILE_LIST) | fgrep -v fgrep | sed -e 's/\\$$//' | sed -e 's/##//'

package: ## Compile and package the Spring Boot application
	mvn clean package

clean: ## Clean the build
	mvn clean

run: ## Run Spring Boot applicaiton locally
	java --add-opens=java.base/java.nio=ALL-UNNAMED -jar target/demoapi-0.0.1-SNAPSHOT.jar
