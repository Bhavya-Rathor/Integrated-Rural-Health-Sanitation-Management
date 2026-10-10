package com.irhsn.medicine.entity;

import com.irhsn.medicine.enums.DosageForm;   // my own list: TABLET, SYRUP, etc.
import jakarta.persistence.*;                 // the database labels (@Entity, @Id, @Column...)
import jakarta.validation.constraints.Min;    // label: "number can't go below X"
import jakarta.validation.constraints.NotBlank; // label: "text can't be empty"
import jakarta.validation.constraints.NotNull;  // label: "must have a value"

// Hey Spring, this class is not just normal Java,
// please make a database table out of it.
@Entity
// And call that table "medicine".
@Table(name = "medicine")
public class Medicine {

    // ===== THE COLUMNS OF MY TABLE =====
    // Every "private ..." line below becomes one column in the table.

    // Every medicine gets a unique number: 1, 2, 3...
    // @Id = this is the main identity of the row (primary key).
    // @GeneratedValue = I don't type this number myself,
    // the database gives the next number automatically.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The medicine's name, like "Paracetamol 500 mg".
    // @NotBlank = if someone submits the form with an empty name, reject it.
    // nullable = false = the database also refuses an empty name.
    // unique = true = no two medicines can have the same name.
    // length = 100 = max 100 characters.
    @NotBlank
    @Column(nullable = false, unique = true, length = 100)
    private String name;

    // The basic chemical name, like "Paracetamol".
    // Optional, so no @NotBlank.
    @Column(length = 100)
    private String genericName;

    // What shape the medicine comes in: TABLET, SYRUP, INJECTION...
    // It can only be one of the values in my DosageForm list.
    // EnumType.STRING = save the actual word "TABLET" in the database,
    // not a number like 0 (words are easier to read and safer).
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private DosageForm form;

    // How strong it is, like "500 mg" or "5 ml".
    @Column(length = 30)
    private String strength;

    // How we count it: "tablet", "bottle", "vial", "pack".
    @Column(length = 20)
    private String unit;

    // The "warning line" for stock.
    // Example: reorderLevel = 100 means
    // "if a health centre has fewer than 100 left, order more automatically".
    // @NotNull = must be filled in. @Min(0) = can't be negative.
    @NotNull
    @Min(0)
    private Integer reorderLevel;

    // Is this a must-have medicine (like ORS or BP tablets)?
    // true = yes. If it runs low, its reorder request is marked HIGH priority.
    private boolean essential;

    // ===== EMPTY CONSTRUCTOR =====
    // Spring needs this "blank page" to create a Medicine object
    // when it reads a row from the database, and then fills in the fields.
    public Medicine() {
    }

    // ===== GETTERS AND SETTERS =====
    // The fields above are "private" (locked), so other classes can't touch them directly.
    // getX() = read the value. setX() = change the value.
    // Spring uses these when saving to the database, filling a form, or sending JSON.
    // (IntelliJ generates all of these for you: Alt+Insert -> Getter and Setter)

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

    public String getGenericName() {
        return genericName;
    }

    public void setGenericName(String genericName) {
        this.genericName = genericName;
    }

    public DosageForm getForm() {
        return form;
    }

    public void setForm(DosageForm form) {
        this.form = form;
    }

    public String getStrength() {
        return strength;
    }

    public void setStrength(String strength) {
        this.strength = strength;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Integer getReorderLevel() {
        return reorderLevel;
    }

    public void setReorderLevel(Integer reorderLevel) {
        this.reorderLevel = reorderLevel;
    }

    // For true/false fields, Java names the getter "isX" instead of "getX".
    public boolean isEssential() {
        return essential;
    }

    public void setEssential(boolean essential) {
        this.essential = essential;
    }
}