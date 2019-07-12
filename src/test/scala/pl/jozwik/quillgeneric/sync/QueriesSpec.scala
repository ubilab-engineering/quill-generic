package pl.jozwik.quillgeneric.sync

import java.time.LocalDate

import io.getquill.{ H2Dialect, H2JdbcContext, SnakeCase }
import org.scalatest.TryValues._
import pl.jozwik.quillgeneric.AbstractSpec
import pl.jozwik.quillgeneric.model.{ Person, PersonId }
import pl.jozwik.quillgeneric.quillmacro.sync.Queries

import scala.util.Try

class QueriesSpec extends AbstractSpec {

  private lazy val ctx = new H2JdbcContext(SnakeCase, "h2") with Queries
  private lazy val repository = new PersonRepository[H2Dialect, SnakeCase.type] {
    val context: H2JdbcContext[SnakeCase.type] with Queries = ctx
    implicit val tableName: String = "Person"
  }

  private lazy val repositoryAutoIncrement = new PersonRepository[H2Dialect, SnakeCase.type] {
    val context: H2JdbcContext[SnakeCase.type] with Queries = ctx
    implicit val tableName: String = "Person2"
  }

  override def afterAll(): Unit = {
    ctx.close()
    super.afterAll()
  }

  "QueriesSync " should {
    "Call all operations on Person" in {
      val person = Person(PersonId(1), "firstName", "lastName", LocalDate.now)
      val notExisting = Person(PersonId(2), "firstName", "lastName", LocalDate.now)
      repository.all shouldBe Try(Seq())
      repository.allBySchema shouldBe Try(Seq())
      repository.create(person) shouldBe 'success
      repository.read(notExisting.id).success.value shouldBe empty
      repository.read(person.id).success.value shouldBe Option(person)
      repository.update(person) shouldBe 'success
      repository.all shouldBe Try(Seq(person))
      repository.delete(person.id) shouldBe 'success
      repository.all shouldBe Try(Seq())
    }

    "Call all operations on Person2 with auto generated id" in {
      val person = Person(PersonId(2), "firstName", "lastName", LocalDate.now)
      repositoryAutoIncrement.all shouldBe Try(Seq())
      repositoryAutoIncrement.allBySchema shouldBe Try(Seq())
      val personId = repositoryAutoIncrement.create(person, true)
      val personIdProvided = personId.success.value
      val createdPatron = repositoryAutoIncrement.read(personIdProvided).success.value.getOrElse(fail())
      repositoryAutoIncrement.update(createdPatron) shouldBe 'success
      repositoryAutoIncrement.all shouldBe Try(Seq(createdPatron))

      repositoryAutoIncrement.delete(createdPatron.id) shouldBe 'success
      repositoryAutoIncrement.read(createdPatron.id).success.value shouldBe empty
      repositoryAutoIncrement.all shouldBe Try(Seq())
    }
  }
}
