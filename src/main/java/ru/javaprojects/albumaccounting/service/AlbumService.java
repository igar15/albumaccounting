package ru.javaprojects.albumaccounting.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.javaprojects.albumaccounting.model.Album;
import ru.javaprojects.albumaccounting.model.Employee;
import ru.javaprojects.albumaccounting.repository.AlbumRepository;
import ru.javaprojects.albumaccounting.to.AlbumTo;
import ru.javaprojects.albumaccounting.util.exception.NotFoundException;

import static ru.javaprojects.albumaccounting.util.AlbumUtil.createFromTo;
import static ru.javaprojects.albumaccounting.util.AlbumUtil.updateFromTo;

@Service
public class AlbumService {
    private final AlbumRepository repository;
    private final EmployeeService employeeService;

    public AlbumService(AlbumRepository repository, EmployeeService employeeService) {
        this.repository = repository;
        this.employeeService = employeeService;
    }

    @Transactional
    public Album create(AlbumTo albumTo) {
        Assert.notNull(albumTo, "albumTo must not be null");
        Employee holder = employeeService.get(albumTo.getHolderId());
        Album album = createFromTo(albumTo);
        album.setHolder(holder);
        return repository.save(album);
    }

    public Album get(int id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Not found album with id=" + id));
    }

    public Page<Album> getAlbums(Pageable pageable) {
        Assert.notNull(pageable, "pageable must not be null");
        return repository.findAllByOrderByDecimalNumber(pageable);
    }

    public void delete(int id) {
        Album album = get(id);
        repository.delete(album);
    }

    @Transactional
    public void update(AlbumTo albumTo) {
        Assert.notNull(albumTo, "albumTo must not be null");
        Album album = get(albumTo.getId());
        Employee holder = employeeService.get(albumTo.getHolderId());
        updateFromTo(album, albumTo);
        album.setHolder(holder);
    }
}