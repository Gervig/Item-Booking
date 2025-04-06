package app.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@ToString
@Builder
@Getter
@Entity
@Table(name = "student", uniqueConstraints = {
        @UniqueConstraint(name = "unique_name_phone", columnNames = {"name", "phone"})
})
public class Student
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private LocalDate enrollmentDate;
    private int phone;

    @ToString.Exclude //avoids infinite to.String recursion
    @OneToMany(mappedBy = "student", cascade = CascadeType.PERSIST) // a student should still persist even if an item is deleted
    private Set<Item> itemList = new HashSet<>();

    public void addItem(Item item)
    {
        this.itemList.add(item);
    }
}
