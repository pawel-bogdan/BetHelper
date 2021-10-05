package io.github.pawel_bogdan.model;

public class ClubDTO {

    public String shortName;
    public String fullName;
    public String squadValue;

    public ClubDTO(Club source) {
        this.shortName = source.getShortName();
        this.fullName = source.getFullName();
        this.squadValue = source.getSquadValue();
    }
}
