# Challengames

## Description
This API has been designed in order to give the possibility to a group of friends to dare each other on succeeding challenges on their favourite games, and keep track of results of the latter challenges. The well-known piece of paper used usually is often lost and isn't really adequate to keep long term scores of challenges between friends. Challenges is composed of a simple description as well as the game of interest. A challenge's description could be for example: "Gather a certain number of coins x in the level l of Mario 64 under a certain amount of time t". After defining a challenge, it is sent to a contestant that can either accept or refuse it. After the challenge being performed by the contestant, the challenger is then able to define if the challenge was a success or a failure.

## Current status
IN DEVELOPMENT

## Technologies
- Kotlin 1.3.x
- Spring-Boot
- Spring Data
- Spring RestTemplate
- Neo4j
- Cypher
- Jackson
- Drools

## External APIS
- igdb (v3)

## Setup
### Example of application.yml
```
server:
  port: 8081

spring:
  data:
    neo4j:
      username: my-neo4j-user
      password: my-neo4j-password
  jackson:
    deserialization:
      fail-on-unknown-properties: false

igdb:
  uri: https://api-v3.igdb.com/games/
  api-key: my-api-key
```
