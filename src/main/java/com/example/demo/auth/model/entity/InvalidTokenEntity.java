package com.example.demo.auth.model.entity;

import com.example.demo.common.model.entity.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "invalid-token-collection")
public class InvalidTokenEntity extends BaseEntity {

    @Id
    @Field(name = "ID")
    private String id;

    @Field(name = "TOKEN_ID")
    private String tokenId;

}
