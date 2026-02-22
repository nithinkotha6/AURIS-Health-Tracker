package com.auris.data.di

import com.auris.data.health.HealthConnectRepositoryImpl
import com.auris.data.repository.HabitRepositoryImpl
import com.auris.data.repository.RoomFoodRepositoryImpl
import com.auris.data.repository.RoomVitaminRepositoryImpl
import com.auris.domain.repository.FoodRepository
import com.auris.domain.repository.HealthConnectRepository
import com.auris.domain.repository.HabitRepository
import com.auris.domain.repository.VitaminRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * RepositoryModule — Hilt bindings for repository interfaces.
 *
 * Phase 6: binds to Room-backed implementations.
 *
 * To revert to in-memory fakes (e.g. for testing or Phase 5 debugging),
 * swap RoomFoodRepositoryImpl → FakeFoodRepositoryImpl
 * and   RoomVitaminRepositoryImpl → FakeVitaminRepositoryImpl.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindFoodRepository(
        impl: RoomFoodRepositoryImpl
    ): FoodRepository

    @Binds
    @Singleton
    abstract fun bindVitaminRepository(
        impl: RoomVitaminRepositoryImpl
    ): VitaminRepository

    @Binds
    @Singleton
    abstract fun bindHealthConnectRepository(
        impl: HealthConnectRepositoryImpl
    ): HealthConnectRepository

    @Binds
    @Singleton
    abstract fun bindHabitRepository(
        impl: HabitRepositoryImpl
    ): HabitRepository
}
