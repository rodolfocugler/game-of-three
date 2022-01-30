# Game Of Three

The project is a java rest-api application resulted from 
the Game of Three assignment shared by JustEatTakeAway company.

### Important

Important things to take into account are following:

#### Database

The H2 in memory database is in the project to help in the 
integration tests. However, in order to allow other devs 
to run the application, H2 database is also present in the 
software code.

#### Api

Basically, the project has 2 main classes: player and game (
which has a list of Moves). 
All APIs can be found in [swagger](http://localhost:8080/swagger-ui.html).

#### Authentication

The authentication method present is JWT. The player can sign
up given the post api (POST /api/players) with a proper
unique username and a password, and hence log in with /login.

The Bearer token is in the header response ('Authorization') 
and can be added to swagger.

Who tries to request a method without the token, will get 401.
Who tries to update a different user, will get 403.

The authentication secret is in 
[application.properities](src/main/resources/application.properties)
application.secret and the project gets it from environment 
variables.

#### Websocket

Application also has websocket present. Therefore, the project
notifies the other player if it is his/her turn.
