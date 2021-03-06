package pl.jozwik.quillgeneric.sync.jdbc.repository

import io.getquill.NamingStrategy
import io.getquill.context.sql.idiom.SqlIdiom
import pl.jozwik.quillgeneric.model.{ Person, PersonId }
import pl.jozwik.quillgeneric.quillmacro.sync.JdbcRepository.JdbcContextDateQuotes
import pl.jozwik.quillgeneric.quillmacro.sync.JdbcRepositoryWithGeneratedId

import scala.util.Try

final class PersonCustomRepository[D <: SqlIdiom, N <: NamingStrategy](protected val context: JdbcContextDateQuotes[D, N], tableName: String)
  extends JdbcRepositoryWithGeneratedId[PersonId, Person, D, N] {

  private val aliases = {
    import context._
    Seq {
      alias[Person](_.birthDate, "dob")
    }
  }

  protected def dynamicSchema: context.DynamicEntityQuery[Person] = dSchema

  private implicit val dSchema: context.DynamicEntityQuery[Person] = context.dynamicQuerySchema[Person](tableName, aliases: _*)

  override def all: Try[Seq[Person]] = Try {
    context.all[Person]
  }

  override def create(entity: Person, generateId: Boolean = true): Try[PersonId] = Try {
    if (generateId) {
      context.createAndGenerateId[PersonId, Person](entity)
    } else {
      context.create[PersonId, Person](entity)
    }
  }

  override def createAndRead(entity: Person, generateId: Boolean = true): Try[Person] = Try {
    context.transaction {
      if (generateId) {
        context.createWithGenerateIdAndRead[PersonId, Person](entity)
      } else {
        context.createAndRead[PersonId, Person](entity)
      }
    }
  }

  override def read(id: PersonId): Try[Option[Person]] = Try {
    context.read[PersonId, Person](id)
  }

  override def readUnsafe(id: PersonId): Try[Person] = Try {
    context.readUnsafe[PersonId, Person](id)
  }

  override def createOrUpdate(entity: Person, generateId: Boolean = true): Try[PersonId] = Try {
    context.transaction {
      if (generateId) {
        context.createAndGenerateIdOrUpdate[PersonId, Person](entity)
      } else {
        context.createOrUpdate[PersonId, Person](entity)
      }
    }
  }

  override def createOrUpdateAndRead(entity: Person, generateId: Boolean = true): Try[Person] = Try {
    context.transaction {
      if (generateId) {
        context.createWithGenerateIdOrUpdateAndRead[PersonId, Person](entity)
      } else {
        context.createOrUpdateAndRead[PersonId, Person](entity)
      }
    }
  }

  override def update(entity: Person): Try[Long] = Try {
    context.update[PersonId, Person](entity)
  }

  override def updateAndRead(entity: Person): Try[Person] = Try {
    context.transaction {
      context.updateAndRead[PersonId, Person](entity)
    }
  }

  override def delete(id: PersonId): Try[Long] =
    Try {
      context.delete[PersonId, Person](id)
    }

  override def deleteAll =
    Try {
      context.deleteAll
    }

}
