package ru.mail.polis.collections.set.hash;

import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

import ru.mail.polis.collections.set.hash.todo.Student;
import ru.mail.polis.collections.set.hash.todo.Student.Gender;

/*
 * Created by Nechaev Mikhail
 * Since 27/11/2018.
 */
public class StudentGenerator {

    private static final AtomicInteger currentId = new AtomicInteger();
    private static final LocalDate MIN_YEAR_OF_BIRTHDAY = LocalDate.of(1990, 1, 1);
    private static final int MAX_YEARS_AFTER_BIRTHDAY = 10;
    private static final int MIN_GROUP_ID = 1000;

    private static final String[] maleFirstNames = {
            "Адонис", "Баграт", "Вальтер", "Гелеон", "Дамир", "Жерар", "Измаил", "Камиль", "Лазарь",
            "Марк", "Назар", "Оганес", "Пабло", "Радим", "Севастьян", "Тенгиз", "Фарид", "Христоф",
            "Чарлз", "Шамиль", "Эвальд", "Ювеналий", "Януарий"
    };
    private static final String[] femaleFirstNames = {
            "Августа", "Бажена", "Валентина", "Габриэлла", "Далида", "Ева", "Жаклин", "Забава", "Ильзира",
            "Камила", "Лада", "Мавиле", "Надежда", "Одетта", "Павлина", "Рада", "Сабина", "Таисия", "Ульяна",
            "Фёкла", "Хадия", "Цагана", "Челси", "Шакира", "Эвелина", "Юлианна", "Ядвига"
    };

    private static final String[] lastNames = {
            "АБАИМОВ", "БАБАДЖАНОВ", "ВАВИЛИН", "ГАВЕНДЯЕВ", "ДАЙНЕКО", "ЕВГЕЕВ", "ЖАБЕНКОВ", "ЗАБАВА",
            "ИБРАГИМОВ", "КАБАКОВ", "ЛАБЗИН", "МАВРИН", "НАБАТОВ", "ОБАБКОВ", "ПАВЕЛЕВ", "РАБИН",
            "САБАНЕЕВ", "ТАБАКОВ", "УБАЙДУЛЛАЕВ", "ФАБИШ", "ХАБАЛОВ", "ЦАГАРАЕВ", "ЧААДАЕВ",
            "ШАБАЛДИН", "ЩАВЕЛЕВ", "ЭВАРНИЦКИЙ", "ЮБЕРОВ", "ЯВЛАШКИН"
    };

    public static Student generate() {
        Random random = ThreadLocalRandom.current();
        Gender gender = random.nextBoolean() ? Gender.MALE : Gender.FEMALE;
        String[] firstNames = gender == Gender.MALE ? maleFirstNames : femaleFirstNames;
        String firstName = firstNames[random.nextInt(firstNames.length)];
        String lastName = lastNames[random.nextInt(lastNames.length)];
        LocalDate birthday = MIN_YEAR_OF_BIRTHDAY
                .plusYears(random.nextInt(MAX_YEARS_AFTER_BIRTHDAY + 1))
                .plusMonths(ChronoField.MONTH_OF_YEAR.range().getMinimum() + random.nextInt((int) ChronoField.MONTH_OF_YEAR.range().getMaximum()))
                .plusDays(ChronoField.DAY_OF_MONTH.range().getMinimum() + random.nextInt((int) ChronoField.DAY_OF_MONTH.range().getSmallestMaximum()));
        int groupId = MIN_GROUP_ID + random.nextInt(100);
        return new Student(currentId.getAndIncrement(), firstName, lastName, gender, birthday, groupId);
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            System.out.println(StudentGenerator.generate());
        }
    }
}
