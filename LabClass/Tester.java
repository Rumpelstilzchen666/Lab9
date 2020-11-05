package LabClass;

import java.util.Random;

public class Tester {
    private static final String[] maleNames =
            {"Николай", "Алексей", "Ярослав", "Дмитрий", "Олег"};
    private static final String[] femaleNames =
            {"Милена", "Диана", "Оксана", "Карина", "Ольга"};
    private static final String[] subjects =
            {"ООП", "Физ-ра", "Мат. анализ", "Java", "СиАОД"};

    public static Student[] getStudentArr(int len) {
        Random rand = new Random();
        Student[] students = new Student[len];
        String name;
        boolean gender;
        for(int i = 0; i < len; i++) {
            gender = rand.nextBoolean();
            name = (gender ? maleNames : femaleNames)[Math.abs(rand.nextInt() %
                    (gender ? maleNames : femaleNames).length)];
            students[i] = new Student(name, gender, Math.abs(rand.nextInt()));
            for(String subject : subjects)
                students[i].addGrade(subject, Math.abs(rand.nextInt()) % 4 + 2);
        }
        return students;
    }
}
