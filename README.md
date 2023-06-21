# HR-PROJECT

docker build --build-arg JAR_FILE=config-server-git/build/libs/config-server-git-v.0.0.1.jar --platform=linux/amd64 -t secil123456/config-git-server:v.1.0 .

docker build --build-arg JAR_FILE=auth-service/build/libs/auth-service-v.0.0.1.jar --platform=linux/amd64 -t secil123456/auth-service:v.4.0 .
docker build --build-arg JAR_FILE=user-profile-service/build/libs/user-profile-service-v.0.0.1.jar --platform=linux/amd64 -t secil123456/user-profile-service:v.2.0 .
docker build --build-arg JAR_FILE=company-service/build/libs/company-service-v.0.0.1.jar --platform=linux/amd64 -t secil123456/company-service:v.2.0 .


docker build --build-arg JAR_FILE=mail-service/build/libs/mail-service-v.0.0.1.jar --platform=linux/amd64 -t secil123456/mail-service:v.1.0 .
docker build --build-arg JAR_FILE=api-gateway-service/build/libs/api-gateway-service-v.0.0.1.jar --platform=linux/amd64 -t secil123456/api-gateway-service:v.1.2 .



db.createUser({user: "Java7User",pwd: "root",roles: ["readWrite", "dbAdmin"]})