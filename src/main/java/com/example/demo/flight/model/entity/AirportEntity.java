package com.example.demo.flight.model.entity;

import com.example.demo.common.model.entity.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "airport-collection")
public class AirportEntity extends BaseEntity {

    @Id
    @Field(name = "ID")
    @Indexed(unique = true)
    private String id = UUID.randomUUID().toString();

    @Field(name = "AIRPORT_NAME")
    private String name;

    @Field(name = "CITY_NAME")
    private String cityName;

}