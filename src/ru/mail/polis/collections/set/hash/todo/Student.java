package ru.mail.polis.collections.set.hash.todo;

import java.time.LocalDate;
import java.util.Objects;

import ru.mail.polis.collections.set.hash.CheckedOpenHashTableEntity;

public class Student extends CheckedOpenHashTableEntity {

    private final long id;
    private final String firstName;
    private final String lastName;
    private final Gender gender;
    private final LocalDate birthday;
    private final int groupId;

    public Student(long id, String firstName, String lastName, Gender gender, LocalDate birthday, int groupId) {
        this.id = id;
        this.firstName = Objects.requireNonNull(firstName, "firstName");
        this.lastName = Objects.requireNonNull(lastName, "lastName");
        this.gender = Objects.requireNonNull(gender, "gender");
        this.birthday = Objects.requireNonNull(birthday, "birthday");
        this.groupId = groupId;
    }

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Gender getGender() {
        return gender;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public int getGroupId() {
        return groupId;
    }

    @Override
    public int hashCode(int tableSize, int probId) throws IllegalArgumentException {
        //todo: use base hashCode with additional logic
        return 0;
    }

    @Override
    public int hashCode() {
        //don't forget hashCode - equals contract
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        if (id != student.id) return false;
        if (groupId != student.groupId) return false;
        if (!firstName.equals(student.firstName)) return false;
        if (!lastName.equals(student.lastName)) return false;
        if (gender != student.gender) return false;
        return birthday.equals(student.birthday);
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", gender=" + gender +
                ", birthday=" + birthday +
                ", groupId=" + groupId +
                '}';
    }

    public enum Gender {
        MALE, FEMALE
    }
}
