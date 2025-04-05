package app.dtos;

import app.entities.Item;
import app.entities.Student;
import app.enums.ItemCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Date;

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
    private Date acquisitionDate;
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
