package db

// a DAO storing data in a H2 in-memory database

class CarAdH2DAO() extends CarAdDAO with CarAdSqlDAO with H2MemoryDb
