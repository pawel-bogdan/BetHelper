package io.github.pawel_bogdan.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "clubs")
public class Club {

    @Id
    @Column(name = "short_name")
    private String shortName;
    @Column(name = "full_name")
    private String fullName;
    @Column(name = "squad_value")
    private String squadValue;
    @Embedded
    private ModificationHistory modificationHistory = new ModificationHistory();

    public Club(String shortName) {
        this.shortName = shortName;
        validateShortName();
    }

    /**
     * Hibernate needs it.
     */
    public Club() {
    }

    public String getShortName() {
        return shortName;
    }

    public String getFullName() {
        return fullName;
    }

    private void validateShortName() {
        if(shortName.endsWith("."))
            shortName = shortName.substring(0, shortName.length() - 1);
    }

    public String getSquadValue() {
        return squadValue;
    }

    public void setSquadValue(String squadValue) {
        this.squadValue = squadValue;
    }

    public LocalDateTime getUpdatedOnDate() {
        return this.modificationHistory.getUpdatedOn();
    }

    @Override
    public String toString() {
        return "{short_name: " + shortName + "\nfull_name: " + fullName + "\nsquad_value: " + squadValue + "}";
    }


}
