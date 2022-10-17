package com.newcubator.geosample.domain.field;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.geolatte.geom.G2D;
import org.geolatte.geom.Polygon;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

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

}
