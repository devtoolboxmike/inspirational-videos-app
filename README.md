# inspirational-inspirationItems-app
inspirational-inspirationItems-app to showcase dynamodb in my tutorial

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