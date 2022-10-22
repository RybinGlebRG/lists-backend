package ru.rerumu.lists.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.rerumu.lists.mappers.TitleMapper;
import ru.rerumu.lists.model.Title;
import ru.rerumu.lists.repository.TitlesRepository;
import ru.rerumu.lists.views.TitleCreateView;

import java.util.List;

@Component
public class TitlesRepositoryImpl implements TitlesRepository {

    @Autowired
    private TitleMapper titleMapper;

    @Override
    public List<Title> getAll(Long watchListId) {
        return titleMapper.getAllTitles(watchListId);
    }

    @Override
    public Title getOne(Long watchListId, Long titleId) {
        return titleMapper.getOne(watchListId,titleId);
    }

    @Override
    public Title update(Title title) {
        titleMapper.update(
                title.getName(),
                title.getTitleId(),
                title.getWatchListId(),
                title.getCreateDateUTC(),
                title.getStatusId(),
                title.getVideoType().getTypeId()
        );
        Title updatedTitle = titleMapper.getOne(title.getWatchListId(),title.getTitleId());
        return updatedTitle;
    }

    public Long getNextId(){
        return titleMapper.getNextId();
    }

    @Override
    public void delete(Long titleId) {
        titleMapper.delete(titleId);
    }

    @Override
    public Title addOne(TitleCreateView newTitle) {
        titleMapper.addOne(
                newTitle.getWatchListId(),
                newTitle.getTitleId(),
                newTitle.getName(),
                newTitle.getCreateDateUTC(),
                newTitle.getStatusId(),
                newTitle.getVideoType().getTypeId()
        );
        Title createdTitle = titleMapper.getOne(newTitle.getWatchListId(), newTitle.getTitleId());
        return createdTitle;
    }
}
