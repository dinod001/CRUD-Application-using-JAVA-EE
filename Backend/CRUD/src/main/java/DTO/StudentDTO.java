package DTO;

import java.util.Objects;

public class StudentDTO {
    int id;
    String firstName;
    String lastName;
    int age;

    public StudentDTO(int id, String frstName, String lastName, int age) {
        this.id = id;
        this.firstName = frstName;
        this.lastName = lastName;
        this.age = age;
    }

    public StudentDTO() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFrstName() {
        return firstName;
    }

    public void setFrstName(String frstName) {
        this.firstName = frstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        StudentDTO that = (StudentDTO) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
