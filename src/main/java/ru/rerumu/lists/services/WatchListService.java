package ru.rerumu.lists.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.rerumu.lists.crosscut.exception.EmptyMandatoryParameterException;
import ru.rerumu.lists.dao.title.TitlesRepository;
import ru.rerumu.lists.domain.movie.Movie;
import ru.rerumu.lists.views.TitleCreateView;

import java.util.List;

@Component
public class WatchListService {

    @Autowired
    private TitlesRepository titlesRepository;

    // TODO: Test
    @Transactional(rollbackFor = Exception.class)
    public Movie updateTitle(Long watchListId, Long titleId, Movie newMovie) throws EmptyMandatoryParameterException {
        Movie currentMovie = titlesRepository.getOne(watchListId, titleId);

        currentMovie.setName(newMovie.getName());
        currentMovie.setCreateDateUTC(newMovie.getCreateDateUTC());
        currentMovie.setStatusId(newMovie.getStatusId());
        currentMovie.setVideoType(newMovie.getVideoType());

        Movie updatedMovie = titlesRepository.update(currentMovie);
        return updatedMovie;

    }

    @Transactional(rollbackFor = Exception.class)
    public Movie addTitle(Long watchListId, TitleCreateView newTitleView) throws EmptyMandatoryParameterException {
        newTitleView.setWatchListId(watchListId);
        Long titleId = titlesRepository.getNextId();
        newTitleView.setTitleId(titleId);
        newTitleView.validate();
        Movie createdMovie = titlesRepository.addOne(newTitleView);
        return createdMovie;
    }

    public Movie getOne(Long watchListId, Long titleId){
        return this.titlesRepository.getOne(watchListId,titleId);
    }

    public List<Movie> getAll(Long watchListId){
        return titlesRepository.getAll(watchListId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteOne(Long titleId){
        titlesRepository.delete(titleId);
    }
}
