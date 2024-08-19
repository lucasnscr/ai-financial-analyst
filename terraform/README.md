# Project: AI Personal Financial Analyst

### AWS - Infrastructure

The project use the **AWS** like cloud provider divided into several main components, each responsible for a specific functionality:

1. AWS Lambda Function
2. Redis (Amazon ElastiCache)
3. RDS (PGVector)
4. MongoDB
5. API Gateway com Autenticação Cognito
6. VPC e Networking
7. IAM Roles e Policies

### Deploy Infrastructure

1. Initialize Terraform.
```
terraform init
```

2. Check terraform execution plan(Important step to check the infra stack available for application).
```
terraform plan
```

3. Apply changes on infrastructure.
```
terraform apply
```