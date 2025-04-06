package app.populators;

import app.entities.Student;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class StudentPopulator
{
    public static List<Student> populate()
    {
        List<Student> students = new ArrayList<>();

        Student s1 = Student.builder()
                .name("Student 1")
                .enrollmentDate(LocalDate.now())
                .phone(12345678)
                .itemList(new HashSet<>())
                .build();
        students.add(s1);

        Student s2 = Student.builder()
                .name("Student 2")
                .enrollmentDate(LocalDate.now().minusDays(5))
                .phone(87654321)
                .itemList(new HashSet<>())
                .build();
        students.add(s2);

        Student s3 = Student.builder()
                .name("Student 3")
                .enrollmentDate(LocalDate.now().minusMonths(3))
                .phone(84756321)
                .itemList(new HashSet<>())
                .build();
        students.add(s3);

        return students;
    }
}
