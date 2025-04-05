package app.dtos;

import app.entities.Student;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentDTO
{
    private Long id;
    private String name;
    private Date enrollmentDate;
    private int phone;
    private List<ItemDTO> itemList;

    public StudentDTO(Student student, boolean includeDetails)
    {
        this.id = student.getId();
        this.name = student.getName();
        this.enrollmentDate = student.getEnrollmentDate();
        this.phone = student.getPhone();

        if(includeDetails && student.getItemList() != null)
        {
            this.itemList = student.getItemList()
                    .stream()
                    .map(item -> new ItemDTO(item, false))
                    .collect(Collectors.toList());
        }
    }

    public Student toEntity()
    {
        Student student = Student.builder()
                .id(this.id)
                .name(this.name)
                .enrollmentDate(this.enrollmentDate)
                .phone(this.phone)
                .build();

        return student;
    }


}
