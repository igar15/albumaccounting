package ru.javaprojects.albumaccounting.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javaprojects.albumaccounting.model.Album;
import ru.javaprojects.albumaccounting.service.AlbumService;
import ru.javaprojects.albumaccounting.to.AlbumTo;

import javax.validation.Valid;
import java.net.URI;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.QUERY;
import static ru.javaprojects.albumaccounting.util.ValidationUtil.assureIdConsistent;
import static ru.javaprojects.albumaccounting.util.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = AlbumRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Album Controller")
public class AlbumRestController {
    private final Logger log = LoggerFactory.getLogger(getClass());
    static final String REST_URL = "/api/albums";

    @Autowired
    private AlbumService service;

    @Parameters({@Parameter(name = "page", in = QUERY, description = "Zero-based page index (0..N)", schema = @Schema(type = "integer", defaultValue = "0")),
                 @Parameter(name = "size", in = QUERY, description = "The size of the page to be returned", schema = @Schema(type = "integer", defaultValue = "20"))})
    @Operation(description = "Get page with albums")
    @SecurityRequirements
    @GetMapping
    public Page<Album> getAll(@Parameter(hidden = true) Pageable pageable) {
        log.info("getAll (pageNumber={}, pageSize={})", pageable.getPageNumber(), pageable.getPageSize());
        return service.getAll(pageable);
    }

    @Parameters({@Parameter(name = "page", in = QUERY, description = "Zero-based page index (0..N)", schema = @Schema(type = "integer", defaultValue = "0")),
            @Parameter(name = "size", in = QUERY, description = "The size of the page to be returned", schema = @Schema(type = "integer", defaultValue = "20"))})
    @Operation(description = "Get page with albums by decimal number")
    @SecurityRequirements
    @GetMapping("/byDecimal")
    public Page<Album> getAllByDecimalNumber(@Parameter(hidden = true) Pageable pageable, @RequestParam String decimalNumber) {
        log.info("getAllByDecimalNumber {} (pageNumber={}, pageSize={})", decimalNumber, pageable.getPageNumber(), pageable.getPageSize());
        return service.getAllByDecimalNumber(decimalNumber, pageable);
    }

    @Parameters({@Parameter(name = "page", in = QUERY, description = "Zero-based page index (0..N)", schema = @Schema(type = "integer", defaultValue = "0")),
            @Parameter(name = "size", in = QUERY, description = "The size of the page to be returned", schema = @Schema(type = "integer", defaultValue = "20"))})
    @Operation(description = "Get page with albums by holder name")
    @SecurityRequirements
    @GetMapping("/byHolder")
    public Page<Album> getAllByHolderName(@Parameter(hidden = true) Pageable pageable, @RequestParam String holderName) {
        log.info("getAllByHolderName {} (pageNumber={}, pageSize={})", holderName, pageable.getPageNumber(), pageable.getPageSize());
        return service.getAllByHolderName(holderName, pageable);
    }

    @Operation(description = "Get album")
    @SecurityRequirements
    @GetMapping("/{id}")
    public Album get(@PathVariable int id) {
        log.info("get {}", id);
        return service.get(id);
    }

    @Operation(description = "Delete album")
    @Secured({"ROLE_ARCHIVE_WORKER", "ROLE_ADMIN"})
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("delete {}", id);
        service.delete(id);
    }

    @Operation(description = "Create new album")
    @Secured({"ROLE_ARCHIVE_WORKER", "ROLE_ADMIN"})
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Album> createWithLocation(@Valid @RequestBody AlbumTo albumTo) {
        log.info("create {}", albumTo);
        checkNew(albumTo);
        Album created = service.create(albumTo);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @Operation(description = "Update album")
    @Secured({"ROLE_ARCHIVE_WORKER", "ROLE_ADMIN"})
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody AlbumTo albumTo, @PathVariable int id) {
        log.info("update {} with id={}", albumTo, id);
        assureIdConsistent(albumTo, id);
        service.update(albumTo);
    }
}