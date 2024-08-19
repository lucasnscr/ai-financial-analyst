# mongodb.tf
resource "aws_instance" "mongodb_server" {
  ami           = "ami-0c55b159cbfafe1f0" # Substituir pelo AMI desejado
  instance_type = "t2.micro"

  vpc_security_group_ids = [aws_security_group.allow_mongodb.id]
  subnet_id              = aws_subnet.public_subnet.id

  key_name = "your-key-pair-name"

  user_data = <<-EOF
              #!/bin/bash
              sudo apt-get update -y
              sudo apt-get install -y mongodb
              sudo systemctl start mongodb
              sudo systemctl enable mongodb
            EOF

  tags = {
    Name = "MongoDBServer"
  }
}

resource "aws_security_group" "allow_mongodb" {
  vpc_id = aws_vpc.main_vpc.id

  ingress {
    from_port   = 27017
    to_port     = 27017
    protocol    = "tcp"
    cidr_blocks = ["10.0.0.0/16"] # Permitir apenas na VPC
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "allow_mongodb"
  }
}