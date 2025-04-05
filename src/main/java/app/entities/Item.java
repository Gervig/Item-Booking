package app.entities;

import app.enums.ItemCategory;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.math.BigDecimal;
import java.sql.Date;

@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@ToString
@Builder
@Getter
@Entity
public class Item
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private BigDecimal purchasePrice;
    private ItemCategory category;
    private Date acquisitionDate;
    private String description;

    @ManyToOne(cascade = CascadeType.PERSIST) // an item should still persist even if a student is deleted
    @JoinColumn(name = "item_id")
    @Setter
    private Student student;
}
