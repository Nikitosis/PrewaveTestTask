# Run an application:
1. Go to /infrastructure folder and in console execute ```docker-compose up```. It will start a postgresql locally
2. In Intellij idea open the project and run PrewaveTestTaskApplication. It will start a server on port 8090.

# Using endpoints
## Create edge
```
POST http://localhost:8090/edges
{
    "fromId":1,
    "toId": 2
}
```
## Remove edge
```
DELETE http://localhost:8090/edges?fromId=1&toId=2
```

## Get tree
```
GET http://localhost:8090/tree?parentNodeId=1
```
