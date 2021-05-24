# Read Me First
The following was discovered as part of building this project:

* The original package name 'com.filippomortari.twitter-clone-backend' is invalid and this project uses 'com.filippomortari.twitterclonebackend' instead.

# Getting Started

### Reference Documentation
For further reference, please consider the following sections:

#### create first changelog
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
#### deploy the stack through cloudformation
```bash
aws cloudformation create-stack \
--stack-name twitter-clone-backend \
--template-body file://infra/twitter-store-backend-cloudformation.yml \
--capabilities CAPABILITY_IAM \
--parameters \
      ParameterKey=ImageUrl,ParameterValue=${AWS_ACCOUNT_ID}.dkr.ecr.eu-west-2.amazonaws.com/twitter-clone-backend:0.0.1
```


[comment]: <> (* [R2DBC Homepage]&#40;https://r2dbc.io&#41;)

