package com.hybrid.culinarystudio.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "insumos")
data class InsumoEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val unidadCompra: String,
    val costoCompra: Double,
    val cantidadCompra: Double,
    val costoUnitarioBase: Double,
    val factorMermaLimpieza: Double = 1.0,
    val factorMermaCoccion: Double = 1.0,
    val stockActualGramos: Double,
    val stockMinimoGramos: Double
) {
    val costoRealPorGramoServido: Double
        get() {
            val rendimientoTotal = factorMermaLimpieza * factorMermaCoccion
            return if (rendimientoTotal > 0) costoUnitarioBase / rendimientoTotal else costoUnitarioBase
        }
}

@Dao
interface InsumoDao {
    @Query("SELECT * FROM insumos ORDER BY nombre ASC")
    fun getAllInsumos(): Flow<List<InsumoEntity>>

    @Upsert
    suspend fun upsertInsumo(insumo: InsumoEntity)
}

@Database(entities = [InsumoEntity::class], version = 1, exportSchema = false)
abstract class CulinaryDatabase : RoomDatabase() {
    abstract fun insumoDao(): InsumoDao

    companion object {
        @Volatile
        private var INSTANCE: CulinaryDatabase? = null

        fun getDatabase(context: android.content.Context): CulinaryDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CulinaryDatabase::class.java,
                    "culinary_studio_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
