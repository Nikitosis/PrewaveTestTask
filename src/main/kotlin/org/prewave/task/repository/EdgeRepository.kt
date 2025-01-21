package org.prewave.task.repository

import org.jooq.DSLContext
import org.prewave.task.entity.dto.EdgeDTO
import org.prewave.task.entity.generated.tables.records.EdgeRecord
import org.prewave.task.entity.generated.tables.references.EDGE
import org.prewave.task.exception.EntityAlreadyExistsException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.dao.DuplicateKeyException
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class EdgeRepository(
    private val dslContext: DSLContext
) {

    fun isNodeExist(nodeId: Int): Boolean {
        return dslContext.fetchExists(
            dslContext.selectOne()
                .from(EDGE)
                .where(EDGE.FROM_ID.eq(nodeId).or(EDGE.TO_ID.eq(nodeId)))
        )
    }

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
        try {
            val result = dslContext.insertInto(EDGE)
                .values(fromId, toId)
                .execute()
        } catch (e: DuplicateKeyException) {
            //if we have a race condition, we could encounter this error when we try to insert existing edge
            //so just in case, lets handle the error
            log.warn("Tried to insert a duplicate edge", e)
            throw EntityAlreadyExistsException("Edge with fromId=${fromId}; toId=${toId} already exists")
        }
    }

    fun removeEdge(fromId: Int, toId: Int) {
        val result = dslContext.delete(EDGE)
            .where(EDGE.FROM_ID.eq(fromId).and(EDGE.TO_ID.eq(toId)))
            .execute()
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(this::class.java)
    }
}
