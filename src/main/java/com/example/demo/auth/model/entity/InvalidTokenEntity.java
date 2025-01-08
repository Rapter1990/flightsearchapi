package com.example.demo.auth.model.entity;

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
@Document(collection = "invalid-token-collection")
public class InvalidTokenEntity extends BaseEntity {

    @Id
    @Indexed(unique = true)
    private String id = UUID.randomUUID().toString();

    @Field(name = "TOKEN_ID")
    private String tokenId;

}
