# rds.tf
resource "aws_db_instance" "postgres" {
  allocated_storage    = 20
  storage_type         = "gp2"
  engine               = "postgres"
  engine_version       = "13.3"
  instance_class       = "db.t3.micro"
  name                 = "mydatabase"
  username             = "yourusername"
  password             = "yourpassword"
  parameter_group_name = "default.postgres13"
  skip_final_snapshot  = true

  vpc_security_group_ids = [aws_security_group.rds_sg.id]
  db_subnet_group_name   = aws_db_subnet_group.main_subnet_group.name

  publicly_accessible = false
}

resource "aws_db_subnet_group" "main_subnet_group" {
  name       = "main_subnet_group"
  subnet_ids = [aws_subnet.public_subnet.id]
}

resource "aws_security_group" "rds_sg" {
  vpc_id = aws_vpc.main_vpc.id

  ingress {
    from_port   = 5432
    to_port     = 5432
    protocol    = "tcp"
    cidr_blocks = ["10.0.0.0/16"] # Permitir apenas na VPC
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}