![Java CI with Maven](https://github.com/alanasjuzokas/kompleksinis_demo_be/workflows/Java%20CI%20with%20Maven/badge.svg?branch=master)

# API

### Stop poll

```
POST https://vote-block-demo.herokuapp.com/v1/polls/stop

Payload: 

{
    "pollId": "123"
}

Response:
{
 "choiceId": 12,
 "choiceId": 0,
  ...
}

200, 404
```
### Start poll

```
POST https://vote-block-demo.herokuapp.com/v1/polls/start

Payload: 

{
    "pollId": "123"
}

Response:

200, 404
```

### Approve request

```
POST https://vote-block-demo.herokuapp.com/v1/requests/approve

Payload: 

{
    "requestId": "123"
}

Response (Poll):
{
        "id": "1dd8a1fa-0c5f-486d-8e4d-59b38830c72b",
        "status": "NOT_STARTED",
        "name": "Test requets",
        "choices": [
            "pirmas choice",
            "antras choice"
        ],
        "requesterId": "3cf21fbe-9614-4b71-bc71-2f26d3f3d5f7"
    }

200, 404
```

### Reject request

```
POST https://vote-block-demo.herokuapp.com/v1/requests/reject

Payload: 

{
    "requestId": "123"
}

Response (Request):
{
    "id": "123",
    "status": "REJECTED",
    "name": "Test requets2",
    "choices": [
        "pirmas choice",
        "antras choice"
    ],
    "requesterId": "3cf21fbe-9614-4b71-bc71-2f26d3f3d5f7"
}

200, 404
```

### Get all requests

```
GET https://vote-block-demo.herokuapp.com/v1/requests

Response:
[
    {
        "id": "e8bbeca8-1c8e-4137-ad89-0fcded559611",
        "status": "APPROVED",
        "name": "Test requets",
        "choices": [
            "pirmas choice",
            "antras choice"
        ],
        "requesterId": "3cf21fbe-9614-4b71-bc71-2f26d3f3d5f7"
    },
    {
        "id": "22743918-9fe4-4aca-aca3-564a710eca6f",
        "status": "REJECTED",
        "name": "Test requets2",
        "choices": [
            "pirmas choice",
            "antras choice"
        ],
        "requesterId": "3cf21fbe-9614-4b71-bc71-2f26d3f3d5f7"
    }
]

200
```

### Get requests by status [APPROVED, PENDING, REJECTED]

```
GET https://vote-block-demo.herokuapp.com/v1/request/{status}

Response:
[
    {
        "id": "22743918-9fe4-4aca-aca3-564a710eca6f",
        "status": "REJECTED",
        "name": "Test requets2",
        "choices": [
            "pirmas choice",
            "antras choice"
        ],
        "requesterId": "3cf21fbe-9614-4b71-bc71-2f26d3f3d5f7"
    }
] 200
```

### Get vote results

```
GET https://vote-block-demo.herokuapp.com/v1/polls/{pollId}/raw

Response:
{
 "choiceId": 12,
 "choiceId": 0,
  ...
}
```

### Get vote results as blockchain
```
GET https://vote-block-demo.herokuapp.com/v1/polls/{pollId}/results

Response:

[
    {
        "_id": "5eb7e36369bc5d3998805cd9",
        "index": "1",
        "timestamp": "2020-05-10T11:20:02.948Z",
        "choiceId": "123",
        "pollId": "123",
        "precedingHash": "c50548340988a77b7f1276fdfb5559a5b6b4ea9d0ef065a587001c3f95142ddb",
        "hash": "70146669955204a7cb88e981b7896e0059146bcc96e35d73633abe8816fd2113"
    }
]

```

### Get all polls

```
GET https://vote-block-demo.herokuapp.com/v1/polls

Response:
[
    {
            "id": "1dd8a1fa-0c5f-486d-8e4d-59b38830c72b",
            "status": "NOT_STARTED",
            "name": "Test requets",
            "choices": [
                "pirmas choice",
                "antras choice"
            ],
            "requesterId": "3cf21fbe-9614-4b71-bc71-2f26d3f3d5f7"
        }
]

200
```

### Get poll by id

```
GET https://vote-block-demo.herokuapp.com/v1/polls/{pollId}

Response:
{
    "id": "1dd8a1fa-0c5f-486d-8e4d-59b38830c72b",
    "status": "NOT_STARTED",
    "name": "Test requets",
    "choices": [
        "pirmas choice",
        "antras choice"
    ],
    "requesterId": "3cf21fbe-9614-4b71-bc71-2f26d3f3d5f7"
}

200, 404
```

### Get polls by status [NOT_STARTED, STARTED, STOPPED]

```
GET https://vote-block-demo.herokuapp.com/v1/polls/status/{status}

Response:
[
    {
        "id": "1dd8a1fa-0c5f-486d-8e4d-59b38830c72b",
        "status": "NOT_STARTED",
        "name": "Test requets",
        "choices": [
            "pirmas choice",
            "antras choice"
        ],
        "requesterId": "3cf21fbe-9614-4b71-bc71-2f26d3f3d5f7"
    }
]
200
```

### Approve request

```
POST https://vote-block-demo.herokuapp.com/v1/requests/approve

Payload: 

{
    "requestId": "123"
}

Response:
{
 "status": "NOT_STARTED",
    "name": "Test requets",
    "choices": [
        "pirmas choice",
        "antras choice"
    ],
    "requesterId": "123"
}

200, 404
```

### Create poll request (kol auth nera reikia id perduot)

```
POST https://vote-block-demo.herokuapp.com/v1/requests/create

Payload: 

    {
    	"name": "Test requets2",
    	"choices": [
    		"pirmas choice", "antras choice"
    	],
    }


Response:
{
    "id": "22743918-9fe4-4aca-aca3-564a710eca6f",
    "status": "PENDING",
    "name": "Test requets2",
    "choices": [
        "pirmas choice",
        "antras choice"
    ],
    "requesterId": "3cf21fbe-9614-4b71-bc71-2f26d3f3d5f7"
}

200
```

### Create requester user

```
POST https://vote-block-demo.herokuapp.com/v1/users/requester

Payload: 

{
	"name": "tempo",
	"surname": "user",
	"phone": "111113",
	"address": "testo gatve",
	"personCode": "1233",
	"email": "aiko@lo.lt"
}

Response:
{
    "id": "3cf21fbe-9614-4b71-bc71-2f26d3f3d5f7",
    "type": "REQUESTER",
    "name": "tempo",
    "surname": "user",
    "phone": "111113",
    "address": "testo gatve",
    "personCode": "1233",
    "email": "aiko@lo.lt"
}

200
```

### Get user by personcode

```
GET https://vote-block-demo.herokuapp.com/v1/users/{personCode}

Response:
{
    "id": "3cf21fbe-9614-4b71-bc71-2f26d3f3d5f7",
    "type": "REQUESTER",
    "name": "tempo",
    "surname": "user",
    "phone": "111113",
    "address": "testo gatve",
    "personCode": "1233",
    "email": "aiko@lo.lt"
}

or

{
    "id": "b0d78693-b8e6-4894-b81d-db17586f96f1",
    "type": "ADMINISTRATOR",
    "name": null,
    "surname": null,
    "phone": null,
    "address": null,
    "personCode": "11111111111",
    "email": "admin@admin.lt"
}

200, 404
```

### Get requester requests

```
GET https://vote-block-demo.herokuapp.com/v1/users/{requesterId}/requests

Response:
[
    {
        "id": "e8bbeca8-1c8e-4137-ad89-0fcded559611",
        "status": "APPROVED",
        "name": "Test requets",
        "choices": [
            "pirmas choice",
            "antras choice"
        ],
        "requesterId": "3cf21fbe-9614-4b71-bc71-2f26d3f3d5f7"
    },
    {
        "id": "22743918-9fe4-4aca-aca3-564a710eca6f",
        "status": "REJECTED",
        "name": "Test requets2",
        "choices": [
            "pirmas choice",
            "antras choice"
        ],
        "requesterId": "3cf21fbe-9614-4b71-bc71-2f26d3f3d5f7"
    }
]

200
```

### Get requester polls

```
GET https://vote-block-demo.herokuapp.com/v1/users/{requesterId}/polls

Response:
[
    {
        "id": "1dd8a1fa-0c5f-486d-8e4d-59b38830c72b",
        "status": "NOT_STARTED",
        "name": "Test requets",
        "choices": [
            "pirmas choice",
            "antras choice"
        ],
        "requesterId": "3cf21fbe-9614-4b71-bc71-2f26d3f3d5f7"
    }
]

200, 302
```

### Vote

```
POST https://vote-block-demo.herokuapp.com/v1/polls/vote

Payload: {
    "choiceId": "123",
    "pollId": "123",
}

Response: 200 (if all ok, or user already voted), 422 (if validation failed),
400 (if voting on not started pol))
```

### Validate poll voter blockchain

```
POST https://vote-block-demo.herokuapp.com/v1/chain/votes/{pollId}

Response: 200 (if all ok), 422 (if validation failed),

```


### Validate poll user blockchain

```
POST https://vote-block-demo.herokuapp.com/v1/chain/user/{pollId}

Response: 200 (if all ok), 422 (if validation failed),

```

### Init smart id login

```
POST https://vote-block-demo.herokuapp.com/v1/login

Payload: 

{
    "idCode": "3970900000"
}

Response:
{
    "sessionId": "123",
    "verificationCode": "1242"
}

200
```

### Init smart id poll

```
GET https://vote-block-demo.herokuapp.com/v1/login/{sessionId}
STATUS can be [RUNNING, COMPLETE]

Response:
{
    "state": "RUNNING",
    "authToken": "",
    "subject": {
            "givenName": "DEMO",
            "serialNumber": "10101010005",
            "surname": "SMART-ID"
       }
}

200
```
