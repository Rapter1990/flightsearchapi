package com.example.demo.flight.model;

import com.example.demo.common.model.BaseDomainModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Represents a task in the system with basic properties such as an identifier and a name.
 * This class extends {@link BaseDomainModel} and provides functionality to model a task
 * in the application.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
public class Airport extends BaseDomainModel {

    private String id;

    private String name;

    private String cityName;
}
