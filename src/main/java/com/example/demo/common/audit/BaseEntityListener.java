package com.example.demo.common.audit;

import com.example.demo.auth.model.enums.TokenClaims;
import com.example.demo.common.model.entity.BaseEntity;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.data.mongodb.core.mapping.event.BeforeSaveEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Listener for MongoDB lifecycle events to manage auditing fields in {@link BaseEntity}.
 */
@Component
public class BaseEntityListener extends AbstractMongoEventListener<BaseEntity> {

    /**
     * Sets auditing fields before converting the entity to a MongoDB document.
     *
     * @param event the event triggered before entity conversion.
     */
    @Override
    public void onBeforeConvert(BeforeConvertEvent<BaseEntity> event) {
        BaseEntity entity = event.getSource();
        if (entity.getCreatedAt() == null) {
            entity.setCreatedAt(LocalDateTime.now());
            entity.setCreatedBy(getCurrentUser());
        }
    }

    /**
     * Sets auditing fields before saving the entity to the MongoDB collection.
     *
     * @param event the event triggered before entity is saved.
     */
    @Override
    public void onBeforeSave(BeforeSaveEvent<BaseEntity> event) {
        BaseEntity entity = event.getSource();
        entity.setUpdatedAt(LocalDateTime.now());
        entity.setUpdatedBy(getCurrentUser());
    }


    /**
     * Retrieves the email of the currently authenticated user from the security context.
     * If no authenticated user is found, it returns "anonymousUser".
     *
     * @return the email of the current user or "anonymousUser" if not authenticated
     */
    private String getCurrentUser() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(Authentication::getPrincipal)
                .filter(user -> !"anonymousUser".equals(user))
                .map(Jwt.class::cast)
                .map(jwt -> jwt.getClaim(TokenClaims.USER_EMAIL.getValue()).toString())
                .orElse("anonymousUser");
    }

}
