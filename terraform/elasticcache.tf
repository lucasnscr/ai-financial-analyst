# Redis (Amazon ElastiCache)
resource "aws_elasticache_subnet_group" "redis_subnet_group" {
  name       = "redis-subnet-group"
  subnet_ids = [aws_subnet.private_subnet.id]
}

resource "aws_security_group" "redis_sg" {
  vpc_id = aws_vpc.main_vpc.id

  ingress {
    from_port   = 6379
    to_port     = 6379
    protocol    = "tcp"
    cidr_blocks = ["10.0.0.0/16"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_elasticache_replication_group" "redis_cache" {
  replication_group_id          = "financial-analyst-redis"
  replication_group_description = "Redis cache for financial analyst"
  engine                        = "redis"
  engine_version                = "6.x"
  node_type                     = "cache.t2.micro"
  number_cache_clusters         = 1
  automatic_failover_enabled    = true
  subnet_group_name             = aws_elasticache_subnet_group.redis_subnet_group.name
  security_group_ids            = [aws_security_group.redis_sg.id]
}