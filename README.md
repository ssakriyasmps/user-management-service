# User Management Service
## Create user endpoint

POST /users HTTP/1.1

Host: localhost:9002

Content-Type: application/json

Request Payload:

`{
"name": "Joe blog",
"age": 18,
"email": "domain@ff",
"phone": "+44 88888888",
"socialMedia": [
"facebook",
"insta"
]
}`

## Get user endpoint

GET /users/{userId} HTTP/1.1

Host: localhost:9002

Content-Type: application/json



    












