package app.entities;

import app.enums.ItemCategory;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.math.BigDecimal;
import java.time.LocalDate;

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
    @Setter
    private String name;
    @Setter
    private BigDecimal purchasePrice;
    @Setter
    private ItemCategory category;
    @Setter
    private LocalDate acquisitionDate;
    @Setter
    private String description;

    @ManyToOne
    @JoinColumn(name = "item_id")
    @Setter
    private Student student;
}
