package ru.rerumu.lists.model.book.readingrecords.status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.rerumu.lists.dao.book.readingrecord.status.StatusRepository;

@Component
public class StatusFactory {

    private final StatusRepository statusRepository;

    @Autowired
    public StatusFactory(StatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }

    public BookStatusRecord findById(Long statusId) {
        return statusRepository.findById(statusId);
    }
}
