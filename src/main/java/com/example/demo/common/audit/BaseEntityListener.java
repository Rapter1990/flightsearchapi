package com.example.demo.common.audit;

import com.example.demo.common.model.entity.BaseEntity;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

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
            entity.prePersist();
        }
        entity.preUpdate();
    }

}
