package app.dtos;

import app.entities.Item;
import app.entities.Student;
import app.enums.ItemCategory;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemDTO
{
    private Long id;
    private String name;
    private BigDecimal purchasePrice;
    private ItemCategory category;
    private LocalDate acquisitionDate;
    private String description;
    private StudentDTO student;

   public ItemDTO(Item item, boolean includeDetails)
   {
       this.id = item.getId();
       this.name = item.getName();
       this.purchasePrice = item.getPurchasePrice();
       this.category = item.getCategory();
       this.acquisitionDate = item.getAcquisitionDate();
       this.description = item.getDescription();

       if(includeDetails && item.getStudent() != null)
       {
           this.student = new StudentDTO(item.getStudent(), false);
           this.student.getItemList().add(this);
       }
   }

   public Item toEntity()
   {
       Item item = Item.builder()
               .id(this.id)
               .name(this.name)
               .purchasePrice(this.purchasePrice)
               .category(this.category)
               .acquisitionDate(this.acquisitionDate)
               .description(this.description)
               .build();

       if(this.student != null)
       {
           Student studentEntity = this.student.toEntity();
           item.setStudent(studentEntity);
           studentEntity.addItem(item);
       }
       return item;
   }
}
