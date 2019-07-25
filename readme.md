# Project: AutoScout24 Scala Task

## Introduction

This project is a task in the recruiting process of AutoScout24. It is an opportunity to prove Scala programming skills by developing a REST service. The task is given in the document "Tech task.md".

The REST service is built in the environment:

- Play framework 2.73
- Scala 2.13.0
- H2 in memory DB
- IDE: VS Code 1.36.1 with Scala Metal (and surprisingly little problems)

**Some personal impressions from the development process can be found in "TaskDiary.pdf"**

## API

The REST service provides CRUD functionality for Car Adverts containing the following fields:

- **id** (_required_): **int** or **guid**, choose whatever is more convenient for you;
- **title** (_required_): **string**, e.g. _"Audi A4 Avant"_;
- **fuel** (_required_): gasoline or diesel, use some type which could be extended in the future by adding additional fuel types;
- **price** (_required_): **integer**;
- **new** (_required_): **boolean**, indicates if car is new or used;
- **mileage** (_only for used cars_): **integer**;
- **firstRegistration** (_only for used cars_): **date** without time.

The service can be accessed on the following paths of the deployment server (default: **http://localhost:9000**):

    Path            Verb    Body    Function
    -----------------------------------------------------------------
    /v1/carads      GET     -       Returns all car adverts
    /v1/carads      POST    yes     Adds a new car advert
    /v1/carads      PUT     yes     Updates a car advert
    /v1/carads/n    GET     -       Returns the car advert with id n
    /v1/carads/n    DELETE  -       Deletes the car advert with id n

Notes:

- n stands for a number; for instance 1, 2, 3, ...
- all data is transferred in JSON-Formt from and to the service
- the POST and PUT methods expect a valid JSON car ad record in body of the request. The id is extracted from that record. POST needs a new id - PUT expects an existing id.

Example of a valid JSON car ad:

    {
        "id": 1,
        "title": "Audi 80",
        "fuel": "gasoline",
        "price": 2500,
        "newCar": false,
        "mileage": 254000,
        "firstRegistration": "1995-02-20"
    }

## Sorting

When querying all car ads, it is possible to sort the result. Adding the parameter **sortby=_field_** sorts ascending by ***field***. Descending sort can be accomplished by preceding the fieldname with a '-' character.

Examles:

- localhost:9000/v1/carads?sortby=mileage ===> returns a list sorted by mileage, ascending
- localhost:9000/v1/carads?sortby=-id ===> returns a list sorted by id, descending

## Validation

The validation rules from the field description are checked. If invalid, an error description is returned as BAD_REQUEST.

Valid fuels as stored in a database table called _fuels_. Adding/removing fuels is possible without recompilation. Yet after changes, the application should be restarted.

## CORS

The application should allow CORS requests. This is achieved by the line

- play.filters.disabled+=play.filters.csrf.CSRFFilter

in _application.conf_ - the only entry in this file. (I hope this works!)

## Exchanging Databases

Currently the application uses a H2 in memory DB - i.e. all entered data is lost at the of the application! Yet, switching to another DB should be easy:

- for another SQL-database, it should be sufficient to replace the class _db.H2MemoryDb_ with an class adequate for the new DB. The class members _db_, _init_ and _dateToSql_ should be replaced.
- for Non-SQL-databases its only necessary to implement the trait _db.CarAdDAO_ for the new DB. Of course, also the dependency injection annotation, **@ImplementedBy(classOf[CarAdH2DAO]** in *db.CarAdDAO*, must be adjusted.

## Possible Extensions and Improvements

- sorting by multiple fields
- filtering the list of all car ads
- automatically generate id of new car ad
- ...

## Author

Hans-Martin Fetzer  
Unterlimpurger Str. 97  
74523 Schwäbisch Hall  
Telefon +49 791 93 99 15  
Mobil +49 170 751 8051

25.07.2019
