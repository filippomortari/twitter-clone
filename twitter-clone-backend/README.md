# twitter-clone-backend

### Reference Documentation
This application is a Spring Boot app containerised via `paketo buildpacks`. This is favoured over Dockerfiles as it adopts a layered approach that embraces modern container standards like the OCI image format and enable cross-repository blob mounting and image layer “rebasing”. They aim to bring advanced caching, multi-language support, minimal app images and reproducibility to our images without forcing us to care of all this ourselves. (See this [blog post](https://blog.codecentric.de/en/2020/11/buildpacks-spring-boot/))
#### Database migrations
The app uses Liquibase to handle DB migrations. Liquibase is exploited in 2 ways: 
1. to produce new migration files by diffing the existing schema and the JPA Entities stored in `package com.filippomortari.twitterclonebackend.domain.entity;`
2. to apply the aforementioned migrations when the Spring Boot app is bootstrapped. (This is discouraged in a production setting - should be run as an independent ECS task, see for example this [blog post](https://spencern319.medium.com/containerized-liquibase-migrations-9f5e0af222ea))
```bash
docker run --name postgresql-container -p 5432:5432 -e POSTGRES_PASSWORD=postgres -d postgres

./gradlew diffChangeLog
 ```
feel free to check results via psql
```bash
psql -h localhost -p 5432 -U postgres
```

#### build jar and container
`./gradlew clean build bootBuildImage`

#### push it to ECR
```bash
aws ecr create-repository \
    --repository-name twitter-clone-backend \
    --image-scanning-configuration scanOnPush=true \
    --region eu-west-2
```

```bash
aws ecr get-login-password --region eu-west-2 | docker login --username AWS --password-stdin ${AWS_ACCOUNT_ID}.dkr.ecr.eu-west-2.amazonaws.com

docker images

docker tag com.filippomortari/twitter-clone-backend:0.0.1-SNAPSHOT ${AWS_ACCOUNT_ID}.dkr.ecr.eu-west-2.amazonaws.com/twitter-clone-backend:0.0.1

docker push ${AWS_ACCOUNT_ID}.dkr.ecr.eu-west-2.amazonaws.com/twitter-clone-backend:0.0.1
```
#### deployment via Cloudformation
All the infra needed to support the backend is defined as code via Cloudformation templates. Below an example run: 
```bash
aws cloudformation create-stack \
--stack-name twitter-clone-backend \
--template-body file://infra/twitter-store-backend-cloudformation.yml \
--capabilities CAPABILITY_IAM \
--parameters \
      ParameterKey=ImageUrl,ParameterValue=${AWS_ACCOUNT_ID}.dkr.ecr.eu-west-2.amazonaws.com/twitter-clone-backend:0.0.1
```

subsequent updates:
```bash
aws cloudformation update-stack \
--stack-name twitter-clone-backend \
--template-body file://infra/twitter-store-backend-cloudformation.yml \
--capabilities CAPABILITY_IAM \
--parameters \
      ParameterKey=ImageUrl,ParameterValue=${AWS_ACCOUNT_ID}.dkr.ecr.eu-west-2.amazonaws.com/twitter-clone-backend:0.0.1
```


[comment]: <> (* [R2DBC Homepage]&#40;https://r2dbc.io&#41;)

