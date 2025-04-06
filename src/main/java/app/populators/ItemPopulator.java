package app.populators;

import app.entities.Item;
import app.entities.Student;
import app.enums.ItemCategory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ItemPopulator
{
    public static List<Item> populate()
    {
        List<Student> students = StudentPopulator.populate();
        List<Item> items = new ArrayList<>();

        Item i1 = Item.builder()
                .name("Item 1")
                .purchasePrice(BigDecimal.valueOf(25.00))
                .category(ItemCategory.VIDEO)
                .acquisitionDate(LocalDate.now())
                .description("This is item 1")
                .student(students.get(0))
                .build();
        items.add(i1);
        students.get(0).addItem(i1);

        Item i2 = Item.builder()
                .name("Item 2")
                .purchasePrice(BigDecimal.valueOf(33.50))
                .category(ItemCategory.TOOL)
                .acquisitionDate(LocalDate.now().minusYears(5))
                .description("This is item 2")
                .student(students.get(0))
                .build();
        items.add(i2);
        students.get(0).addItem(i2);

        Item i3 = Item.builder()
                .name("Item 3")
                .purchasePrice(BigDecimal.valueOf(500.00))
                .category(ItemCategory.VR)
                .acquisitionDate(LocalDate.now().minusDays(4))
                .description("This is item 3")
                .student(students.get(1))
                .build();
        items.add(i3);
        students.get(1).addItem(i3);

        Item i4 = Item.builder()
                .name("Item 4")
                .purchasePrice(BigDecimal.valueOf(1.00))
                .category(ItemCategory.PRINT)
                .acquisitionDate(LocalDate.now().minusMonths(7))
                .description("This is item 4")
                .student(students.get(1))
                .build();
        items.add(i4);
        students.get(1).addItem(i4);

        Item i5 = Item.builder()
                .name("Item 5")
                .purchasePrice(BigDecimal.valueOf(75.00))
                .category(ItemCategory.SOUND)
                .acquisitionDate(LocalDate.now().minusMonths(7).minusDays(2))
                .description("This is item 5")
                .build();
        items.add(i5);

        return items;
    }
}
