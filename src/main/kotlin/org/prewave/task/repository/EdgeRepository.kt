package org.prewave.task.repository

import org.jooq.DSLContext
import org.prewave.task.entity.dto.EdgeDTO
import org.prewave.task.entity.generated.tables.records.EdgeRecord
import org.prewave.task.entity.generated.tables.references.EDGE
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class EdgeRepository(
    private val dslContext: DSLContext
) {

    fun findAllByFromIds(fromIds: List<Int>): List<EdgeDTO> {
        val edges = dslContext.select()
            .from(EDGE)
            .where(EDGE.FROM_ID.`in`(fromIds))
            .fetchArray()

        return edges.map { record ->
            EdgeDTO().apply {
                this.fromId = record!!.get(EDGE.FROM_ID)
                this.toId = record!!.get(EDGE.TO_ID)
            }
        }
    }

    fun findByFromIdAndToId(fromId: Int, toId: Int): EdgeDTO? {
        val edge = dslContext.select()
            .from(EDGE)
            .where(EDGE.FROM_ID.eq(fromId).and(EDGE.TO_ID.eq(toId)))
            .fetchOne()

        return edge?.let {
            EdgeDTO().apply {
                this.fromId = edge!!.get(EDGE.FROM_ID)
                this.toId = edge!!.get(EDGE.TO_ID)
            }
        }
    }

    fun addEdge(fromId: Int, toId: Int) {
        val result = dslContext.insertInto(EDGE)
            .values(fromId, toId)
            .execute()
    }

    fun removeEdge(fromId: Int, toId: Int) {
        val result = dslContext.delete(EDGE)
            .where(EDGE.FROM_ID.eq(fromId).and(EDGE.TO_ID.eq(toId)))
            .execute()
    }
}
