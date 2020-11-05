package LabClass;

import java.util.ArrayList;

public class Student implements Comparable<Student> {
    public String name;
    public final boolean gender;
    private final long id;
    private double averageGrade = 0;
    private final ArrayList<GradeBySubject> grades = new ArrayList<>();

    public Student(String name, boolean gender, long id) {
        this.name = name;
        this.gender = gender;
        this.id = id;
    }

    public boolean addGrade(String subject, int grade) {
        if(grade < 2 || grade > 5 || subject == null || subject.length() == 0)
            return false;
        grades.add(new GradeBySubject(subject, grade));
        averageGrade =
                ((grades.size() - 1) * averageGrade + grade) / grades.size();
        return true;
    }

    public long getID() {
        return id;
    }

    public double getAverageGrade() {
        return averageGrade;
    }

    @Override
    public int compareTo(Student st) {
        int i = name.compareTo(st.name);
        if(i == 0)
            return Long.compare(getID(), st.getID());
        return i;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id = " + id +
                ", name = '" + name + '\'' +
                ", gender = " + (gender ? 'm' : 'f') +
                ", averageGrade = " + averageGrade +
                '}';
    }


    private class GradeBySubject {
        public String subject;
        public int grade;

        public GradeBySubject(String subject) {
            this.subject = subject;
            this.grade = 0;
        }

        public GradeBySubject(String subject, int grade) {
            this.subject = subject;
            this.grade = grade;
        }

        @Override
        public String toString() {
            return "GradeBySubject{" +
                    "subject = '" + subject + '\'' +
                    ", grade = " + grade +
                    '}';
        }
    }
}
