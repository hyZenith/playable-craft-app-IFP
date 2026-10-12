package com.example.myapplication.data.repo

import com.example.myapplication.data.model.DeliveryContentType
import com.example.myapplication.data.model.DeliveryItem
import com.example.myapplication.data.model.DeliveryStatus
import kotlinx.coroutines.delay

/**
 * Repository interface managing deliveries of simulations, lesson plans, and MCQs to student batches.
 */
interface DeliveryRepository {
    /**
     * Lists deliveries, optionally filtered by target batch.
     */
    suspend fun list(batch: String? = null): Result<List<DeliveryItem>>

    /**
     * Creates and schedules a new batch delivery.
     */
    suspend fun create(
        title: String,
        contentType: DeliveryContentType,
        targetBatch: String,
        scheduledDate: String
    ): Result<DeliveryItem>

    /**
     * Deletes a scheduled delivery by its identifier.
     */
    suspend fun delete(id: String): Result<Unit>
}

/**
 * In-memory repository implementation simulating batch delivery management.
 */
class FakeDeliveryRepository : DeliveryRepository {

    private val deliveries = mutableListOf<DeliveryItem>()

    override suspend fun list(batch: String?): Result<List<DeliveryItem>> {
        delay(250)
        val filtered = if (batch.isNullOrBlank()) {
            deliveries.toList()
        } else {
            deliveries.filter { it.targetBatch.equals(batch, ignoreCase = true) }
        }
        return Result.success(filtered)
    }

    override suspend fun create(
        title: String,
        contentType: DeliveryContentType,
        targetBatch: String,
        scheduledDate: String
    ): Result<DeliveryItem> {
        delay(150)
        val newItem = DeliveryItem(
            id = "del_${System.currentTimeMillis()}",
            title = title.ifBlank { "Untitled Delivery" },
            contentType = contentType,
            targetBatch = targetBatch.ifBlank { "Batch 2026-2027" },
            scheduledDateFormatted = scheduledDate.ifBlank { "Oct 15, 2026" },
            status = DeliveryStatus.SCHEDULED
        )
        deliveries.add(0, newItem)
        return Result.success(newItem)
    }

    override suspend fun delete(id: String): Result<Unit> {
        delay(100)
        deliveries.removeAll { it.id == id }
        return Result.success(Unit)
    }
}
