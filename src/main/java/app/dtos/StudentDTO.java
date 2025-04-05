package app.dtos;

import app.entities.Student;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

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

    public StudentDTO(Student student)
    {
        this.id = student.getId();
        this.name = student.getName();
        this.enrollmentDate = student.getEnrollmentDate();
        this.phone = student.getPhone();
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
