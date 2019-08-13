package pl.jozwik.quillgeneric.sync.jdbc.repository

import io.getquill.NamingStrategy
import io.getquill.context.sql.idiom.SqlIdiom
import pl.jozwik.quillgeneric.model.{ Sale, SaleId }
import pl.jozwik.quillgeneric.quillmacro.sync.JdbcRepository.JdbcContextDateQuotes
import pl.jozwik.quillgeneric.quillmacro.sync.JdbcRepositoryCompositeKey

import scala.util.Try

final class SaleRepository[D <: SqlIdiom, N <: NamingStrategy](
    protected val context: JdbcContextDateQuotes[D, N],
    tableName: String
) extends JdbcRepositoryCompositeKey[SaleId, Sale, D, N] {

  private val aliases = {
    import context._
    Seq(
      alias[Sale](_.id.fk1, "productId"),
      alias[Sale](_.id.fk2, "personId")
    )
  }

  private implicit val dSchema: context.DynamicEntityQuery[Sale] = context.dynamicQuerySchema[Sale](tableName, aliases: _*)

  protected def dynamicSchema: context.DynamicEntityQuery[Sale] = dSchema

  override def create(entity: Sale): Try[SaleId] =
    Try {
      context.create[SaleId, Sale](entity)
    }

  override def createAndRead(entity: Sale): Try[Sale] =
    Try {
      context.transaction {
        context.createAndRead[SaleId, Sale](entity)
      }
    }

  override def createOrUpdate(entity: Sale): Try[SaleId] =
    Try {
      context.transaction {
        context.createOrUpdate[SaleId, Sale](entity)
      }
    }

  override def createOrUpdateAndRead(entity: Sale): Try[Sale] =
    Try {
      context.transaction {
        context.createOrUpdateAndRead[SaleId, Sale](entity)
      }
    }

  override def all: Try[Seq[Sale]] =
    Try {
      context.all[Sale]
    }

  override def read(id: SaleId): Try[Option[Sale]] =
    Try {
      context.read[SaleId, Sale](id)
    }

  override def update(entity: Sale): Try[Long] = Try {
    context.update[SaleId, Sale](entity)
  }

  override def updateAndRead(entity: Sale): Try[Sale] = Try {
    context.transaction {
      context.updateAndRead[SaleId, Sale](entity)
    }
  }

  override def delete(id: SaleId): Try[Long] =
    Try {
      context.delete[SaleId, Sale](id)
    }
}
