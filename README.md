# API

### Create new poll

```
POST https://fierce-waters-74037.herokuapp.com/v1/create

Payload: 

{
    "question": "Wanna vote?
}

Response:

e532ab6f-0357-45ec-8ef5-a8854588a7ad

```
### Vote

```
POST https://fierce-waters-74037.herokuapp.com/v1/vote

Payload: 

{
    "pollId": "e532ab6f-0357-45ec-8ef5-a8854588a7ad",
    "votedYes": true
}

Response:
200, 400
```

### Get poll results

```
GET https://fierce-waters-74037.herokuapp.com/v1/{pollId}/results

Response:
{
    "yes": 1,
    "no": 0
}
```

### Get all polls

```
GET https://fierce-waters-74037.herokuapp.com/v1/polls

Response:
[
    {
        "id": "1111111111111",
        "question": "simple quest",
        "yes": 1,
        "no": 0,
        "running": true
    },
    {
       "id": "22222222",
       "question": "another simple quest",
       "yes": 1,
       "no": 2,
       "running": false
    },
]
```

### Get poll 

```
GET https://fierce-waters-74037.herokuapp.com/v1/{pollId}

Response:
{
        "id": "1111111111111",
        "question": "simple quest",
        "yes": 1,
        "no": 0,
        "running": true
}
```

### Get active polls

```
GET https://fierce-waters-74037.herokuapp.com/v1/active

Response:
[
    {
        "id": "1111111111111",
        "question": "simple quest",
        "yes": 1,
        "no": 0,
        "running": true
    },
    {
       "id": "22222222",
       "question": "another simple quest",
       "yes": 1,
       "no": 2,
       "running": true
    },
]
```

### Stop poll

```
POST https://fierce-waters-74037.herokuapp.com/v1/stop

Payload: 

{
    "pollId": "e532ab6f-0357-45ec-8ef5-a8854588a7ad",
}

Response:
200
```
