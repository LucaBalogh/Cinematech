package ro.uoradea.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EntityTest {

    Entity entity;

    @BeforeEach
    void setUp() {
        entity = new Entity();
    }

    @Test
    void getSetId() {
        entity.setId(1);
        assertEquals(entity.getId(),1);
        entity.setId(-1);
        assertEquals(entity.getId(),-1);
    }
}