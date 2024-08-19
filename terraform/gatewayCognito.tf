#  API Gateway with Cognito Authentication
resource "aws_api_gateway_rest_api" "financial_api" {
  name = "Financial Analyst API"
}

resource "aws_api_gateway_resource" "proxy_resource" {
  rest_api_id = aws_api_gateway_rest_api.financial_api.id
  parent_id   = aws_api_gateway_rest_api.financial_api.root_resource_id
  path_part   = "{proxy+}"
}

resource "aws_api_gateway_method" "proxy_method" {
  rest_api_id   = aws_api_gateway_rest_api.financial_api.id
  resource_id   = aws_api_gateway_resource.proxy_resource.id
  http_method   = "ANY"
  authorization = "COGNITO_USER_POOLS"
  authorizer_id = aws_api_gateway_authorizer.cognito_authorizer.id
  request_parameters = {
    "method.request.path.proxy" = true
  }
}

resource "aws_cognito_user_pool" "user_pool" {
  name = "financial-analyst-user-pool"
}

resource "aws_cognito_user_pool_client" "user_pool_client" {
  name         = "financial-analyst-client"
  user_pool_id = aws_cognito_user_pool.user_pool.id
}

resource "aws_api_gateway_authorizer" "cognito_authorizer" {
  name                = "cognito-authorizer"
  rest_api_id         = aws_api_gateway_rest_api.financial_api.id
  authorizer_uri      = "arn:aws:apigateway:${var.aws_region}:cognito-idp.authorizer"
  identity_source     = "method.request.header.Authorization"
  authorizer_result_ttl_in_seconds = 300
}

resource "aws_api_gateway_integration" "lambda_integration" {
  rest_api_id             = aws_api_gateway_rest_api.financial_api.id
  resource_id             = aws_api_gateway_resource.proxy_resource.id
  http_method             = aws_api_gateway_method.proxy_method.http_method
  integration_http_method = "POST"
  type                    = "AWS_PROXY"
  uri                     = aws_lambda_function.api_lambda.invoke_arn

  request_parameters = {
    "integration.request.path.proxy" = "method.request.path.proxy"
  }
}

resource "aws_api_gateway_deployment" "api_deployment" {
  depends_on = [aws_api_gateway_integration.lambda_integration]
  rest_api_id = aws_api_gateway_rest_api.financial_api.id
  stage_name  = "prod"
}

resource "aws_lambda_permission" "apigw_lambda" {
  statement_id  = "AllowAPIGatewayInvoke"
  action        = "lambda:InvokeFunction"
  function_name = aws_lambda_function.api_lambda.function_name
  principal     = "apigateway.amazonaws.com"
  source_arn    = "${aws_api_gateway_rest_api.financial_api.execution_arn}/*/*"
}

output "api_url" {
  value = "${aws_api_gateway_deployment.api_deployment.invoke_url}"
}