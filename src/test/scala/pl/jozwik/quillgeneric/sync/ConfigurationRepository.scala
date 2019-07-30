package pl.jozwik.quillgeneric.sync

import io.getquill.NamingStrategy
import io.getquill.context.sql.idiom.SqlIdiom
import pl.jozwik.quillgeneric.model.{ Configuration, ConfigurationId }
import pl.jozwik.quillgeneric.quillmacro.sync.JdbcRepository
import pl.jozwik.quillgeneric.quillmacro.sync.JdbcRepository.JdbcContextDateQuotes

import scala.util.Try

class ConfigurationRepository[D <: SqlIdiom, N <: NamingStrategy](
    protected val context: JdbcContextDateQuotes[D, N],
    protected val tableName: String = "Configuration")
  extends JdbcRepository[ConfigurationId, Configuration, D, N] {

  protected def dynamicSchema: context.DynamicEntityQuery[Configuration] = dSchema

  private val aliases = {
    import context._
    Seq(
      alias[Configuration](_.id, "key"),
      alias[Configuration](_.value, "value")
    )
  }
  private implicit val dSchema: context.DynamicEntityQuery[Configuration] =
    context.dynamicQuerySchema[Configuration](tableName, aliases: _*)

  override def create(entity: Configuration): Try[ConfigurationId] =
    context.create[ConfigurationId, Configuration](entity)(dSchema)

  override def createOrUpdate(entity: Configuration): Try[ConfigurationId] =
    context.createOrUpdate[ConfigurationId, Configuration](entity)

  override def all: Try[Seq[Configuration]] =
    context.all[Configuration]

  override def read(id: ConfigurationId): Try[Option[Configuration]] =
    context.read[ConfigurationId, Configuration](id)

  override def update(entity: Configuration): Try[Long] =
    context.update[Configuration](entity)(dSchema)

  override def delete(id: ConfigurationId): Try[Boolean] =
    context.delete[ConfigurationId, Configuration](id)

}
