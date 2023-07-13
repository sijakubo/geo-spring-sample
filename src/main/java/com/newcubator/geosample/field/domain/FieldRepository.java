package com.newcubator.geosample.field.domain;

import java.util.UUID;
import org.geolatte.geom.Geometry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FieldRepository extends JpaRepository<Field, UUID> {

    @Query("""
            SELECT CASE WHEN COUNT(field.id) > 0 THEN false ELSE true END
            from Field field
            where field.name = :name
            OR st_intersects(field.geometry, :geometry)
        """)
    boolean isValid(String name, Geometry geometry);
}
