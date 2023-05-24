package com.newcubator.geosample.domain.field;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.code_intelligence.jazzer.api.FuzzedDataProvider;
import com.code_intelligence.jazzer.junit.FuzzTest;
import java.util.UUID;
import org.geolatte.geom.G2D;
import org.geolatte.geom.Polygon;
import org.geolatte.geom.PositionSequenceBuilders;
import org.geolatte.geom.crs.CoordinateReferenceSystems;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FieldCreateTest {

    @FuzzTest
    void shouldCreateAField(FuzzedDataProvider data) {
        FieldRepository fieldRepository = mock(FieldRepository.class);
        FieldCreate cut = new FieldCreate(fieldRepository);

        Polygon geometry = new Polygon(
            PositionSequenceBuilders.variableSized(G2D.class)
                .add(5.8208837124389, 51.0596004663904)
                .add(5.83490292265498, 51.0571257015788)
                .add(5.87078646658134, 51.0451607414904)
                .add(5.79146302423308, 51.0612386272784)
                .add(5.8208837124389, 51.0596004663904)
                .toPositionSequence(),
            CoordinateReferenceSystems.WGS84
        );

        String fieldName = data.consumeAsciiString(255);
        when(fieldRepository.isValid(fieldName, geometry)).thenReturn(true);
        when(fieldRepository.save(Mockito.any())).thenAnswer(invocation -> invocation.getArguments()[0]);

        Field field = cut.handle(UUID.randomUUID(), fieldName, geometry);
        Assertions.assertNotNull(field);
    }
}
