package com.example.demo.common.model.entity;

import com.example.demo.auth.model.enums.TokenClaims;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * A base entity class that provides common fields for tracking the creation
 * and update metadata of an entity. This class is intended to be extended by other
 * entity classes to include audit information such as creation and update timestamps
 * and user details.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BaseEntity {


    @Field(name = "createdAt")
    private LocalDateTime createdAt;

    @Field(name = "createdBy")
    private String createdBy;

    @Field(name = "updatedAt")
    private LocalDateTime updatedAt;

    @Field(name = "updatedBy")
    private String updatedBy;

    /**
     * Sets the createdBy and createdAt fields before persisting the entity.
     * If no authenticated user is found, sets createdBy to "anonymousUser".
     */
    public void prePersist() {
        this.createdBy = Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(Authentication::getPrincipal)
                .filter(user -> !"anonymousUser".equals(user))
                .map(Jwt.class::cast)
                .map(jwt -> jwt.getClaim(TokenClaims.USER_EMAIL.getValue()).toString())
                .orElse("anonymousUser");
        this.createdAt = LocalDateTime.now();
    }

    /**
     * Sets the updatedBy and updatedAt fields before updating the entity.
     * If no authenticated user is found, sets updatedBy to "anonymousUser".
     */
    public void preUpdate() {
        this.updatedBy = Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(Authentication::getPrincipal)
                .filter(user -> !"anonymousUser".equals(user))
                .map(Jwt.class::cast)
                .map(jwt -> jwt.getClaim(TokenClaims.USER_EMAIL.getValue()).toString())
                .orElse("anonymousUser");
        this.updatedAt = LocalDateTime.now();
    }

}
