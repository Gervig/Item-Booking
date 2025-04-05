package app.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.sql.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@ToString
@Builder
@Getter
@Entity
public class Student
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Date enrollmentDate;
    private int phone;


    @OneToMany(mappedBy = "item", cascade = CascadeType.PERSIST) // a student should still persist even if an item is deleted
    private List<Item> itemList;

    public void addItem(Item item)
    {
        this.itemList.add(item);
    }
}
