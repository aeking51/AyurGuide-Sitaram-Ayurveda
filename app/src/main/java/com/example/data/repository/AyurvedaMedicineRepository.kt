package com.example.data.repository

import android.content.Context
import android.util.Log
import com.example.data.local.AyurvedaDatabase
import com.example.data.local.dao.AyurvedaMedicineDao
import com.example.data.local.entity.toDomainModel
import com.example.data.local.entity.toEntity
import com.example.data.model.AyurvedaMedicine
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Repository adhering to Clean Architecture and Room data architecture patterns.
 * Mediates between Cloud Firestore and local Room persistence.
 */
class AyurvedaMedicineRepository(
    private val medicineDao: AyurvedaMedicineDao,
    private val firestoreRepository: FirestoreRepository = FirestoreRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    companion object {
        private const val TAG = "AyurMedicineRepo"

        @Volatile
        private var INSTANCE: AyurvedaMedicineRepository? = null

        fun getInstance(context: Context): AyurvedaMedicineRepository {
            return INSTANCE ?: synchronized(this) {
                val database = AyurvedaDatabase.getDatabase(context)
                val instance = AyurvedaMedicineRepository(database.medicineDao())
                INSTANCE = instance
                instance
            }
        }
    }

    /**
     * Reactive stream of medicines from Room local cache.
     * Updates automatically whenever local or remote sync writes to Room.
     */
    val allMedicines: Flow<List<AyurvedaMedicine>> = medicineDao.getAllMedicines()
        .map { entities -> entities.map { it.toDomainModel() } }
        .flowOn(ioDispatcher)

    /**
     * Observable medicine by ID.
     */
    fun getMedicineById(id: String): Flow<AyurvedaMedicine?> = medicineDao.getMedicineById(id)
        .map { it?.toDomainModel() }
        .flowOn(ioDispatcher)

    /**
     * Search medicines in Room database.
     */
    fun searchMedicines(query: String): Flow<List<AyurvedaMedicine>> = medicineDao.searchMedicines(query)
        .map { entities -> entities.map { it.toDomainModel() } }
        .flowOn(ioDispatcher)

    /**
     * Search medicines by name in Room database.
     */
    fun searchMedicinesByName(query: String): Flow<List<AyurvedaMedicine>> = medicineDao.searchMedicinesByName(query)
        .map { entities -> entities.map { it.toDomainModel() } }
        .flowOn(ioDispatcher)

    /**
     * Search medicines by ingredient in Room database.
     */
    fun searchMedicinesByIngredient(query: String): Flow<List<AyurvedaMedicine>> = medicineDao.searchMedicinesByIngredient(query)
        .map { entities -> entities.map { it.toDomainModel() } }
        .flowOn(ioDispatcher)

    /**
     * Fetches medicines from Cloud Firestore, writes them to the local Room database,
     * and returns the resulting domain models.
     */
    suspend fun fetchAndSyncFromFirestore(): Result<List<AyurvedaMedicine>> = withContext(ioDispatcher) {
        runCatching {
            val remoteMedicines = firestoreRepository.fetchMedicinesSuspend()
            if (remoteMedicines.isNotEmpty()) {
                val entities = remoteMedicines.map { it.toEntity() }
                medicineDao.insertMedicines(entities)
                Log.d(TAG, "Successfully synced ${entities.size} medicines from Firestore to Room.")
                remoteMedicines
            } else {
                // If Firestore has no documents yet or is offline, seed Room with classical repository items
                val defaultList = AyurvedaRepository.allMedicines
                val entities = defaultList.map { it.toEntity() }
                medicineDao.insertMedicines(entities)
                defaultList
            }
        }.onFailure { e ->
            Log.e(TAG, "Error fetching from Firestore: ${e.message}", e)
        }
    }

    /**
     * Saves a medicine formulation to Cloud Firestore and updates local Room database.
     */
    suspend fun saveMedicine(medicine: AyurvedaMedicine): Result<Unit> = withContext(ioDispatcher) {
        runCatching {
            // 1. Cache immediately in Room local database
            medicineDao.insertMedicine(medicine.toEntity())

            // 2. Persist in Cloud Firestore
            val success = firestoreRepository.saveMedicineSuspend(medicine)
            if (!success) {
                Log.w(TAG, "Saved to Room locally, but Firestore sync is pending/offline.")
            }
        }.onFailure { e ->
            Log.e(TAG, "Failed to save medicine: ${e.message}", e)
        }
    }

    /**
     * Deletes a medicine formulation from both Cloud Firestore and Room.
     */
    suspend fun deleteMedicine(medicineId: String): Result<Unit> = withContext(ioDispatcher) {
        runCatching {
            // 1. Delete from local Room database
            medicineDao.deleteMedicineById(medicineId)

            // 2. Delete from Cloud Firestore
            firestoreRepository.deleteMedicineSuspend(medicineId)
            Unit
        }.onFailure { e ->
            Log.e(TAG, "Failed to delete medicine: ${e.message}", e)
        }
    }

    /**
     * Updates inventory stock units in Cloud Firestore and local Room cache.
     */
    suspend fun updateStock(medicineId: String, newStock: Int): Result<Unit> = withContext(ioDispatcher) {
        runCatching {
            val isLowStock = newStock < 15
            medicineDao.updateStock(medicineId, newStock, isLowStock)
            firestoreRepository.updateMedicineStock(medicineId, newStock, isLowStock)
            Unit
        }
    }

    /**
     * Registers a real-time Firestore snapshot listener that updates Room in background.
     */
    fun startRealtimeFirestoreSync() {
        firestoreRepository.observeMedicines(
            onSuccess = { remoteList ->
                if (remoteList.isNotEmpty()) {
                    // Update Room in background thread via dispatcher
                    kotlinx.coroutines.CoroutineScope(ioDispatcher).launch {
                        medicineDao.insertMedicines(remoteList.map { it.toEntity() })
                    }
                }
            },
            onError = { e ->
                Log.e(TAG, "Firestore snapshot listener error: ${e.message}")
            }
        )
    }
}
