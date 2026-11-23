package uz.pdp.library.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
//@Table(name = "authors")

public class Author extends Auditable {

    @NotNull
    private String fullName;

    private String country;

    private int birthYear;

    @OneToMany(mappedBy = "authors")
    private transient List<Book> books;



}

