package ru.rerumu.lists.dao.title.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.rerumu.lists.dao.movie.mapper.TitleMapper;
import ru.rerumu.lists.domain.movie.Movie;
import ru.rerumu.lists.dao.title.TitlesRepository;
import ru.rerumu.lists.views.TitleCreateView;

import java.util.List;

@Component
public class TitlesRepositoryImpl implements TitlesRepository {

    @Autowired
    private TitleMapper titleMapper;

    @Override
    public List<Movie> getAll(Long watchListId) {
        return titleMapper.getAllTitles(watchListId);
    }

    @Override
    public Movie getOne(Long watchListId, Long titleId) {
        return titleMapper.getOne(watchListId,titleId);
    }

    @Override
    public Movie update(Movie movie) {
        titleMapper.update(
                movie.getName(),
                movie.getId(),
                movie.getWatchListId(),
                movie.getCreateDateUTC(),
                movie.getStatusId(),
                movie.getVideoType().getTypeId()
        );
        Movie updatedMovie = titleMapper.getOne(movie.getWatchListId(), movie.getId());
        return updatedMovie;
    }

    public Long getNextId(){
        return titleMapper.getNextId();
    }

    @Override
    public void delete(Long titleId) {
        titleMapper.delete(titleId);
    }

    @Override
    public Movie addOne(TitleCreateView newTitle) {
        titleMapper.addOne(
                newTitle.getWatchListId(),
                newTitle.getTitleId(),
                newTitle.getName(),
                newTitle.getCreateDateUTC(),
                newTitle.getStatusId(),
                newTitle.getVideoType().getTypeId()
        );
        Movie createdMovie = titleMapper.getOne(newTitle.getWatchListId(), newTitle.getTitleId());
        return createdMovie;
    }
}
