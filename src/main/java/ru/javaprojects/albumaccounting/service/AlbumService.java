package ru.javaprojects.albumaccounting.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.javaprojects.albumaccounting.model.Album;
import ru.javaprojects.albumaccounting.model.Employee;
import ru.javaprojects.albumaccounting.repository.AlbumRepository;
import ru.javaprojects.albumaccounting.util.exception.NotFoundException;

@Service
public class AlbumService {
    private final AlbumRepository repository;
    private final EmployeeService employeeService;

    public AlbumService(AlbumRepository repository, EmployeeService employeeService) {
        this.repository = repository;
        this.employeeService = employeeService;
    }

    @Transactional
    public Album create(Album album, int holderId) {
        Assert.notNull(album, "album must not be null");
        Employee holder = employeeService.get(holderId);
        album.setHolder(holder);
        return repository.save(album);
    }

    public Album get(int id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Not found album with id=" + id));
    }

    public Page<Album> getAlbums(Pageable pageable) {
        Assert.notNull(pageable, "pageable must not be null");
        return repository.findAllBy(pageable);
    }

    public void delete(int id) {
        Album album = get(id);
        repository.delete(album);
    }

    @Transactional
    public void update(Album album, int holderId) {
        Assert.notNull(album, "album must not be null");
        get(album.id());
        Employee holder = employeeService.get(holderId);
        album.setHolder(holder);
        repository.save(album);
    }
}