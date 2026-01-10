package ru.rerumu.lists.views;

import ru.rerumu.lists.crosscut.exception.EmptyMandatoryParameterException;
import ru.rerumu.lists.domain.movietype.MovieType;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TitleCreateView {
    private Long titleId;
    private String name;
    private Date createDateUTC;
    private Long watchListId;
    private Long statusId;
    private MovieType videoType;

    public String getName(){
        return this.name;
    }

    public Long getTitleId(){
        return this.titleId;
    }

    public Date getCreateDateUTC() {
        return this.createDateUTC;
    }

    public Long getWatchListId(){
        return this.watchListId;
    }

    public int getdd(){
        return Integer.parseInt(new SimpleDateFormat("dd").format(this.createDateUTC));
    }

    public int getMonth(){
        return Integer.parseInt(new SimpleDateFormat("MM").format(this.createDateUTC));
    }

    public int getyyyy(){
        return Integer.parseInt(new SimpleDateFormat("yyyy").format(this.createDateUTC));
    }

    public int getHH(){
        return Integer.parseInt(new SimpleDateFormat("HH").format(this.createDateUTC));
    }

    public int getmm(){
        return Integer.parseInt(new SimpleDateFormat("mm").format(this.createDateUTC));
    }

    public int getss() {
        return Integer.parseInt(new SimpleDateFormat("ss").format(this.createDateUTC));
    }

    public void setName(String name) throws EmptyMandatoryParameterException {
        if (name == null || name.isEmpty()){
            throw new EmptyMandatoryParameterException("Name is null or empty");
        }
        this.name = name;
    }

    public void setCreateDateUTC(Date createDateUTC) throws EmptyMandatoryParameterException{
        if (createDateUTC == null){
            throw new EmptyMandatoryParameterException("createDateUTC is null");
        }
        this.createDateUTC = createDateUTC;
    }

    public Long getStatusId() {
        return statusId;
    }

    public void setStatusId(Long statusId) {
        this.statusId = statusId;
    }

    public MovieType getVideoType() {
        return videoType;
    }

    public void setVideoType(MovieType videoType) {
        this.videoType = videoType;
    }

    public void setTitleId(Long titleId) {
        this.titleId = titleId;
    }

    public void setWatchListId(Long watchListId) {
        this.watchListId = watchListId;
    }

    public TitleCreateView(Long titleId, Long watchListId, String name, Date createDateUTC, Long statusId){
        this.titleId = titleId;
        this.watchListId = watchListId;
        this.name = name;
        this.createDateUTC = createDateUTC;
        this.statusId = statusId;
    }

    public void validate() throws EmptyMandatoryParameterException {
        if (titleId == null || watchListId == null || name == null || createDateUTC == null || statusId == null){
            throw  new EmptyMandatoryParameterException();
        }
    }
}
