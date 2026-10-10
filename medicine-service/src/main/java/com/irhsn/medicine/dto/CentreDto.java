package com.irhsn.medicine.dto;

// A simple box for the centre data that comes back
// from Bhavya's Health Centre service as JSON.
// Field names MUST match the JSON keys, so Spring can fill them in.
public class CentreDto {

    private Long id;        // centre ID number
    private String name;    // e.g. "PHC Kalpakkam"
    private String village; // e.g. "Kalpakkam"

    // Empty constructor: Spring needs it to create the object
    // before filling in the fields from JSON.
    public CentreDto() {
    }

    // Handy constructor so I can build a placeholder myself
    // when Bhavya's service is down.
    public CentreDto(Long id, String name, String village) {
        this.id = id;
        this.name = name;
        this.village = village;
    }

    // Getters and setters (Alt+Insert -> Getter and Setter)

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }
}