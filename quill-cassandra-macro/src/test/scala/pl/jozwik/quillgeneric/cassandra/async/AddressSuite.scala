package pl.jozwik.quillgeneric.cassandra.async

import pl.jozwik.quillgeneric.cassandra.async.repository.AddressAsyncRepository
import pl.jozwik.quillgeneric.cassandra.model.AddressId

trait AddressSuite extends AbstractAsyncSpec {

  private lazy val repository = new AddressAsyncRepository(ctx)

  "AddressSuite simple transformation" should {

      "run async crud operations" in {
        val id      = AddressId.random
        val address = createAddress(id)
        repository.create(address).futureValue
        repository.create(address).futureValue
        val all = repository.all.futureValue
        logger.debug(s"$all")
        repository.read(id).futureValue shouldBe Option(address)
        repository.all.futureValue shouldBe Seq(address)
        repository.deleteAll.futureValue
        repository.all.futureValue shouldBe Seq.empty
      }

    }

}
