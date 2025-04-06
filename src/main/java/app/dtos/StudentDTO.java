package app.dtos;

import app.entities.Student;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@JsonIgnoreProperties
public class StudentDTO
{
    private Long id;
    private String name;
    private LocalDate enrollmentDate;
    private int phone;
    @ToString.Exclude
    @JsonIgnore
    private Set<ItemDTO> itemList;

    public StudentDTO(Student student, boolean includeDetails)
    {
        this.id = student.getId();
        this.name = student.getName();
        this.enrollmentDate = student.getEnrollmentDate();
        this.phone = student.getPhone();
        this.itemList = new HashSet<>();

        if(includeDetails && student.getItemList() != null)
        {
            this.itemList = student.getItemList()
                    .stream()
                    .map(item -> new ItemDTO(item, false))
                    .collect(Collectors.toSet());
        }
    }

    public Student toEntity()
    {
        Student student = Student.builder()
                .id(this.id)
                .name(this.name)
                .enrollmentDate(this.enrollmentDate)
                .phone(this.phone)
                .itemList(new HashSet<>())
                .build();

        return student;
    }

    public void addItem(ItemDTO itemDTO)
    {
        this.itemList.add(itemDTO);
    }


}
