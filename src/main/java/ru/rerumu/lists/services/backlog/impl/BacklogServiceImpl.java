package ru.rerumu.lists.services.backlog.impl;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.rerumu.lists.controller.backlog.view.in.BacklogItemCreateView;
import ru.rerumu.lists.controller.backlog.view.in.BacklogItemEventCreateView;
import ru.rerumu.lists.controller.backlog.view.in.BacklogItemUpdateView;
import ru.rerumu.lists.crosscut.exception.ClientException;
import ru.rerumu.lists.crosscut.utils.DateFactory;
import ru.rerumu.lists.dao.book.BookRepository;
import ru.rerumu.lists.dao.readingrecord.ReadingRecordsRepository;
import ru.rerumu.lists.dao.user.UsersRepository;
import ru.rerumu.lists.domain.backlog.BacklogItem;
import ru.rerumu.lists.domain.backlog.BacklogItemEventType;
import ru.rerumu.lists.domain.backlog.BacklogItemFactory;
import ru.rerumu.lists.domain.book.Book;
import ru.rerumu.lists.domain.book.BookFactory;
import ru.rerumu.lists.domain.readingrecord.ReadingRecord;
import ru.rerumu.lists.domain.readingrecordstatus.ReadingRecordStatuses;
import ru.rerumu.lists.domain.series.item.SeriesItemType;
import ru.rerumu.lists.domain.user.User;
import ru.rerumu.lists.services.backlog.BacklogService;

import java.util.List;

@Service("backlogServiceImpl")
@Slf4j
public class BacklogServiceImpl implements BacklogService {

    private final BacklogItemFactory backlogItemFactory;
    private final BookFactory bookFactory;
    private final DateFactory dateFactory;
    private final ReadingRecordsRepository readingRecordsRepository;
    private final BookRepository bookRepository;
    private final UsersRepository usersRepository;

    @Autowired
    public BacklogServiceImpl(
            BacklogItemFactory backlogItemFactory,
            BookFactory bookFactory,
            DateFactory dateFactory,
            ReadingRecordsRepository readingRecordsRepository,
            BookRepository bookRepository,
            UsersRepository usersRepository
    ) {
        this.backlogItemFactory = backlogItemFactory;
        this.bookFactory = bookFactory;
        this.dateFactory = dateFactory;
        this.readingRecordsRepository = readingRecordsRepository;
        this.bookRepository = bookRepository;
        this.usersRepository = usersRepository;
    }

    @Override
    public BacklogItem addItemToBacklog(@NonNull Long userId, @NonNull BacklogItemCreateView backlogItemCreateView) {
        // Get user
        User user = usersRepository.findById(userId);

        // Get type and check correctness
        SeriesItemType seriesItemType = SeriesItemType.findById(backlogItemCreateView.getType());
        if (seriesItemType == null) {
            throw new ClientException();
        }

        // Create backlog item
        BacklogItem backlogItem = backlogItemFactory.create(
                backlogItemCreateView.getTitle(),
                seriesItemType,
                backlogItemCreateView.getNote(),
                user,
                backlogItemCreateView.getCreationDate()
        );

        // Save created backlog item
        backlogItem.save();

        return backlogItem;
    }

    @Override
    public List<BacklogItem> getBacklog(@NonNull Long userId) {
        // Get user
        User user = usersRepository.findById(userId);

        // Load and return backlog items
        return backlogItemFactory.loadByUser(user);
    }

    @Override
    public BacklogItem updateBacklogItem(@NonNull Long userId, @NonNull Long backlogItemId, @NonNull BacklogItemUpdateView backlogItemUpdateView) {
        // Get user
        User user = usersRepository.findById(userId);

        // Get backlog item
        BacklogItem backlogItem = backlogItemFactory.loadById(user, backlogItemId);

        // Update title
        backlogItem.updateTitle(backlogItemUpdateView.getTitle());

        // Get type and check correctness
        SeriesItemType seriesItemType = SeriesItemType.findById(backlogItemUpdateView.getType());
        if (seriesItemType == null) {
            throw new ClientException();
        }

        // Update type
        backlogItem.updateType(seriesItemType);

        // Update note
        backlogItem.updateNote(backlogItemUpdateView.getNote());

        // Update creation date
        backlogItem.updateCreationDate(backlogItemUpdateView.getCreationDate());

        // Save updated backlog item
        backlogItem.save();

        return backlogItem;
    }

    @Override
    public void deleteBacklogItem(@NonNull Long userId, @NonNull Long backlogItemId) {
        // Get user
        User user = usersRepository.findById(userId);

        // Get backlog item
        BacklogItem backlogItem = backlogItemFactory.loadById(user, backlogItemId);

        // Delete backlog item
        backlogItem.delete();
    }

    @Override
    public void processEvent(@NonNull Long userId, @NonNull Long backlogItemId, @NonNull BacklogItemEventCreateView backlogItemEventCreateView) {
        // Get user
        User user = usersRepository.findById(userId);

        // Get backlog item
        BacklogItem backlogItem = backlogItemFactory.loadById(user, backlogItemId);

        // Get type and check correctness
        BacklogItemEventType backlogItemEventType = BacklogItemEventType.findById(backlogItemEventCreateView.getEventTypeId());
        if (backlogItemEventType == null) {
            throw new ClientException();
        }

        // If moving to lists
        if (backlogItemEventType.equals(BacklogItemEventType.MOVE_TO_LIST)) {

            // If item is book
            if (backlogItem.getType().equals(SeriesItemType.BOOK)) {
                Book book = bookRepository.create(
                        backlogItem.getTitle(),
                        null,
                        backlogItem.getNote(),
                        ReadingRecordStatuses.IN_PROGRESS,
                        dateFactory.getLocalDateTime(),
                        null,
                        null,
                        user
                );

                // Find status
                ReadingRecordStatuses bookStatusRecord = ReadingRecordStatuses.IN_PROGRESS;

                // Create reading record
                ReadingRecord readingRecord = readingRecordsRepository.create(
                        book.getId(),
                        bookStatusRecord,
                        dateFactory.getLocalDateTime(),
                        null,
                        null
                );

                // Add reading record to book
                book.addReadingRecord(readingRecord);

                book.save();

                backlogItem.delete();
            }
        }
    }
}
