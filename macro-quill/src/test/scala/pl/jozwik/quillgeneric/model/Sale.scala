package pl.jozwik.quillgeneric.model

import java.time.Instant

import io.getquill.Embedded
import pl.jozwik.quillgeneric.quillmacro.{ CompositeKey2, WithId }

final case class SaleId(fk1: ProductId, fk2: PersonId) extends Embedded with CompositeKey2[ProductId, PersonId]

final case class Sale(id: SaleId, saleDate: Instant) extends WithId[SaleId]
