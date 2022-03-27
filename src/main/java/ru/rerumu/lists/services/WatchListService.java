package ru.rerumu.lists.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.rerumu.lists.exception.EmptyMandatoryParameterException;
import ru.rerumu.lists.model.Title;
import ru.rerumu.lists.model.TitlesList;
import ru.rerumu.lists.repository.TitlesRepository;
import ru.rerumu.lists.views.TitleCreateView;

import java.util.List;

@Component
public class WatchListService {

    @Autowired
    private TitlesRepository titlesRepository;

    // TODO: Test
    @Transactional(rollbackFor = Exception.class)
    public Title updateTitle(Long watchListId, Long titleId, Title newTitle) throws EmptyMandatoryParameterException {
        Title currentTitle = titlesRepository.getOne(watchListId, titleId);

        currentTitle.setName(newTitle.getName());
        currentTitle.setCreateDateUTC(newTitle.getCreateDateUTC());
        currentTitle.setStatusId(newTitle.getStatusId());
        currentTitle.setVideoType(newTitle.getVideoType());

        Title updatedTitle = titlesRepository.update(currentTitle);
        return updatedTitle;

    }

    @Transactional(rollbackFor = Exception.class)
    public Title addTitle(Long watchListId, TitleCreateView newTitleView) throws EmptyMandatoryParameterException {
        newTitleView.setWatchListId(watchListId);
        Long titleId = titlesRepository.getNextId();
        newTitleView.setTitleId(titleId);
        newTitleView.validate();
        Title createdTitle = titlesRepository.addOne(newTitleView);
        return createdTitle;
    }

    public Title getOne(Long watchListId, Long titleId){
        return this.titlesRepository.getOne(watchListId,titleId);
    }

    public TitlesList getAll(Long watchListId){
        List<Title> titles = titlesRepository.getAll(watchListId);
        TitlesList titlesList = new TitlesList(titles);
        titlesList.sort();
        return titlesList;
    }

//    // TODO: Test
//    @Transactional(rollbackFor = Exception.class)
//    public Title createTitle(Long watchListId, Title newTitle)  {
//        currentTitle.setName(newTitle.getName());
//        currentTitle.setCreateDateUTC(newTitle.getCreateDateUTC());
//
//        Title updatedTitle = this.repository.update(currentTitle);
//        return updatedTitle;
//
//    }
}
