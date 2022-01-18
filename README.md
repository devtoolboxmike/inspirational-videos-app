# inspirational-inspirationItems-app
Spring boot app to showcase dynamodb in my tutorial video: [link]()

Rank motivational videos in vote order.

## Steps for running locally
```
//Clone onto your machine:
git clone https://github.com/devtoolboxmike/inspirational-videos-app.git
cd inspirational-videos-app

//startup dynamodb locally on docker (port 8000)
docker-compose up

//start spring boot web app
./mvnw spring-boot:run

//visit UI
http://localhost:8080/
```

**Import postman collection**
Use `devtoolbox-inspirational-video.postman_collection.json` in this project and import it into postman.

Once the collection is in postman run the entire collection, and set the iterations something around 20, or more. 

Now go back to the UI and you should see videos listed with their vote count.

If you get stuck, just view the [tutorial video]()

## Helpful commands:
```
aws dynamodb list-tables --endpoint-url http://localhost:8000
for ^ you should get: 
{
    "TableNames": [
        "inspirational-item"
    ]
}

aws dynamodb scan --table-name inspirational-item --endpoint-url http://localhost:8000
for ^ you should something like:
{
    "Items": [
        {
            "createdAt": {
                "S": "2022-01-09T02:30:52.974Z"
            },
            "id": {
                "S": "73fe7ef4-ca16-4f8c-8c1b-44ce734ee7da"
            },
            "voteCount": {
                "N": "0"
            },
            "submitterUsername": {
                "S": "devtoolbox"
            },
            "videoUrl": {
                "S": "https://www.youtube.com/watch?v=Kxvp3eOYphY"
            },
            "submitterEmail": {
                "S": "devtoolbox@coolhost.com"
            }
        }
    ],
    "Count": 1,
    "ScannedCount": 1,
    "ConsumedCapacity": null
}

aws dynamodb get-item --table-name inspirational-item --key '{"id": {"S": "73fe7ef4-ca16-4f8c-8c1b-44ce734ee7da"}}'


aws dynamodb delete-table --table-name inspiration-item --endpoint-url http://localhost:8000

```