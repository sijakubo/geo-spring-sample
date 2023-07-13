package com.newcubator.geosample.field.domain;

import com.newcubator.geosample.farmer.domain.Farmer;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.geolatte.geom.G2D;
import org.geolatte.geom.Polygon;
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

    @ManyToOne
    private Farmer farmer;

}
