package university;

import utils.annotations.DetailDisplay;

import java.io.Serializable;

public record UniversityInfo(
        @DetailDisplay(label = "Full Name") String fullName,
        @DetailDisplay(label = "Short Name") String shortName,
        @DetailDisplay(label = "City") String city,
        @DetailDisplay(label = "Address") String address
) implements Serializable {
    @Override
    public String toString() {
        return fullName + " (" + shortName + ") - " + city + ", " + address;
    }
}

