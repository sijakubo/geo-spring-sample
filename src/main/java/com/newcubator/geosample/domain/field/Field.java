package com.newcubator.geosample.domain.field;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.geolatte.geom.G2D;
import org.geolatte.geom.Polygon;

import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class Field {

    @Id
    @ToString.Include
    private UUID id;

    @NotNull
    @ToString.Include
    private String name;

    @Column(columnDefinition = "Geometry")
    private Polygon<G2D> geometry;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Set<String> properties;

}
