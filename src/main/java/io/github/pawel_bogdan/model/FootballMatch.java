package io.github.pawel_bogdan.model;

import java.util.List;

public class FootballMatch extends Event {
    //private Integer id;
    private Club hostClub;
    private Club guestClub;
    private Double hostClubSVCRating;
    private Double guestClubSVCRating;

    private List<Float> basicCourses;

    public FootballMatch(String name, String date, List<Float> basicCourses, String url) {
        super(name, date, url);
        this.basicCourses = basicCourses;
        updateHostAndGuestTeamShortNames();
    }

    public Club getHostClub() {
        return hostClub;
    }

    public Club getGuestClub() {
        return guestClub;
    }

    public void setHostClub(Club hostClub) {
        this.hostClub = hostClub;
    }

    public void setGuestClub(Club guestClub) {
        this.guestClub = guestClub;
    }

    public Float getHostTeamVictoryCourse() {
        return basicCourses.get(0);
    }

    public Float getDrawCourse() {
        return basicCourses.get(1);
    }

    public Float getGuestTeamVictoryCourse() {
        return basicCourses.get(2);
    }

    public Float getHostTeamVictoryOrDrawCourse() {
        return basicCourses.get(3);
    }

    public Float getGuestTeamVictoryOrDrawCourse() {
        return basicCourses.get(4);
    }

    public void updateHostAndGuestTeamShortNames() {
        var shortHostTeamName = this.name.split(" - ")[0];
        var shortGuestTeamName = this.name.split(" - ")[1];
        this.hostClub = new Club(shortHostTeamName);
        this.guestClub = new Club(shortGuestTeamName);
    }

    public Double getHostClubSVCRating() {
        return hostClubSVCRating;
    }

    public Double getGuestClubSVCRating() {
        return guestClubSVCRating;
    }

    List<Float> getBasicCourses() {
        return basicCourses;
    }

    public void setHostClubSVCRating(Double hostClubSVCRating) {
        this.hostClubSVCRating = hostClubSVCRating;
    }

    public void setGuestClubSVCRating(Double guestClubSVCRating) {
        this.guestClubSVCRating = guestClubSVCRating;
    }

    @Override
    public String toString() {
        return "[" + date + "]" + this.name + " [1] " + getHostTeamVictoryCourse() + " [0] " + getDrawCourse() +
                " [2] " + getGuestTeamVictoryCourse() + " [10] " + getHostTeamVictoryOrDrawCourse() + " [02] "
                + getGuestTeamVictoryOrDrawCourse() + "\t\t\t" + hostClub.getSquadValue() + "\t" +
                guestClub.getSquadValue() + "\t\t" + hostClubSVCRating + "\t" + guestClubSVCRating;
    }
}
