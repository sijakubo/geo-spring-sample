package com.newcubator.geosample.field.entrypoints;

import com.newcubator.geosample.field.domain.FieldCreate;
import com.newcubator.geosample.field.domain.FieldRepository;
import com.newcubator.geosample.field.domain.Field;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(FieldController.MAPPING)
@RequiredArgsConstructor
public class FieldController {

    public static final String MAPPING = "/api/fields";

    private final FieldRepository fieldRepository;
    private final FieldCreate fieldCreate;

    @GetMapping
    public ResponseEntity<Set<FieldResource>> getFields() {
        Set<FieldResource> fields = fieldRepository.findAll()
            .stream()
            .map(FieldResource::new)
            .collect(Collectors.toSet());

        return ResponseEntity.ok(fields);
    }

    @PostMapping
    public ResponseEntity<FieldResource> createField(
        @RequestBody @Valid FieldResource createResource
    ) {
        Field createdField = fieldCreate.handle(
            createResource.id(),
            createResource.name(),
            createResource.geometry(),
            createResource.properties()
        );
        return ResponseEntity.ok(new FieldResource(createdField));
    }
}
