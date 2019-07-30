package db

import play.api.db.Database
import play.api.db.Databases
import java.sql.Connection
import org.joda.time.LocalDate

// contains the code specific to H2
trait H2MemoryDb {

  val db: Database = Databases.inMemory()

  def init(): Unit = db.withConnection { con: Connection =>
    val createDbSql = """
    drop table if exists carads;
    create table carads (
      id        int primary key,
      title     varchar(255) not null,
      fuel      varchar(255) not null,
      price     int not null,
      mileage   int,
      firstRegistration date
    );

    drop table if exists fuels;
    create table fuels (
      fuel  varchar(255)
    );
    insert into fuels values ('gasoline');
    insert into fuels values ('diesel'); """

    con.createStatement.execute(createDbSql)
  }

  def dateToSql(d: LocalDate): String =
    s"PARSEDATETIME('$d','yyyy-MM-dd','en')"

}
