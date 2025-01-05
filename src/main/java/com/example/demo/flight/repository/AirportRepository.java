package com.example.demo.flight.repository;

import com.example.demo.flight.model.entity.AirportEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

/**
 * Repository interface for managing {@link AirportEntity} entities in the Couchbase database.
 * This interface extends {@link MongoRepository}, providing CRUD operations for {@link AirportEntity}.
 * It also defines custom query methods for working with tasks by name.
 */
public interface AirportRepository extends MongoRepository<AirportEntity,String> {

    /**
     * Checks if a task with the specified name already exists in the database.
     *
     * @param name the name of the task to check for existence.
     * @return {@code true} if a task with the specified name exists, {@code false} otherwise.
     */
    boolean existsByName(String name);

    /**
     * Finds a task entity by its name.
     *
     * @param name the name of the task to find.
     * @return an {@link Optional} containing the {@link AirportEntity} if found, or an empty {@link Optional} if not.
     */
    Optional<AirportEntity> findAirportByName(String name);

}
