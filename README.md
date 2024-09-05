# Script Execute Service
## Microservice to run IkData Scripts
#### Application.properties
The `ikdata.projects.folder` property must contain the folder of the IkData  script projects.
#### Http Method
POST
#### Endpoint
http://localhost:8080/api/scripts/execute
#### Params
- projectId (Number of the project)
- method (Script method to run e.g. VALIDATE, HOMOLOGATE, etc.)
- JSON of the BatchJsonDTO to send
#### Response
```json
{
    "success": false,
    "data": {},
    "message": "Response message"
}
```
- success: [boolean], the execution was successful or not
- data: [BatchJsonDTO], the JSON result of execute the script
- message: [String]: message of the result
## Technology
- Spring Boot 3.3.3
- Java 21