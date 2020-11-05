package LabClass;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class LabClassUI extends JFrame {
    public LabClassUI(Student[] allStudents) {
        setTitle("Список студентов");
        setLayout(new BorderLayout());

        final JMenuBar menuBar = new JMenuBar();
        final JMenu menuSortBy = new JMenu("Сортировать по…");
        final String[] sortNames =
                new String[]{"Имени", "ID", "Среднему баллу"};
        final String[] sortTypes = new String[]{"Name", "ID", "Grade"};
        final String[] sortType = new String[]{"Name"};
        final JMenuItem[] menuSortTypes = new JMenuItem[sortNames.length];
        for(int i = 0; i < sortNames.length; i++) {
            final int j = i;
            menuSortTypes[i] = new JMenuItem(sortNames[j]);
            menuSortTypes[i].addActionListener(e -> sortType[0] = sortTypes[j]);
            menuSortBy.add(menuSortTypes[i]);
        }
        menuBar.add(menuSortBy);

        final JMenu menuSearchBy = new JMenu("Поиск по…");
        final String[] searchNames =
                new String[]{"Имени", "ID", "Среднему баллу", "Отменить поиск"};
        final String[] searchTypes =
                new String[]{"Name", "ID", "Grade", "Undo"};
        final String[] searchType = new String[]{null};
        final JMenuItem[] menuSearchTypes = new JMenuItem[searchNames.length];
        for(int i = 0; i < searchNames.length; i++) {
            final int j = i;
            menuSearchTypes[i] = new JMenuItem(searchNames[j]);
            menuSearchTypes[i].addActionListener(
                    e -> searchType[0] = searchTypes[j]);
            menuSearchBy.add(menuSearchTypes[i]);
        }
        menuBar.add(menuSearchBy);
        add(menuBar, BorderLayout.NORTH);

        final JPanel[] mainPnl = new JPanel[]{new JPanel()};
        Label[][] studentLabels = printMainPanel(mainPnl, allStudents);
        add(mainPnl[0], BorderLayout.CENTER);
        add(new JScrollPane(mainPnl[0],
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));


        Student[] students = allStudents;
        String prevSortField = "null", searchKey;
        boolean needPrintMainPanel = false;
        while(true) {
            try {
                Thread.sleep(100);
            } catch(Exception ignored) {
            }
            if(searchType[0] != null) {
                if(searchType[0].equals("Undo")) {
                    // В отдельном if, чтобы не ушло в else
                    if(!((Integer) students.length).equals(
                            allStudents.length)) {
                        students = allStudents;
                        needPrintMainPanel = true;
                        prevSortField = "null";
                    }
                }
                else {
                    searchKey = JOptionPane.showInputDialog(this,
                            "Введите " + switch (searchType[0]) {
                                case "Name" -> "имя";
                                case "ID" -> "ID";
                                case "Grade" -> "средний балл";
                                default -> throw new IllegalStateException(
                                        "Unexpected value: " + searchType[0]);
                            } + " для поиска",
                            "Поиск по " + switch (searchType[0]) {
                                case "Name" -> "имени";
                                case "ID" -> "ID";
                                case "Grade" -> "среднему баллу";
                                default -> throw new IllegalStateException(
                                        "Unexpected value: " + searchType[0]);
                            }, JOptionPane.QUESTION_MESSAGE);
                    if(searchKey != null)
                        try {
                            students = searchStudents(students, searchType[0],
                                    searchKey);
                            needPrintMainPanel = true;
                        } catch(Exception e) {
                            String exceptionName = e.getClass().getSimpleName();
                            JOptionPane.showMessageDialog(this,
                                    switch (exceptionName) {
                                        case "EmptyStringException" ->
                                                "Задан пустой поисковой запрос!";
                                        case "NumberFormatException" ->
                                                "Неверный формат входных данных!";
                                        case "StudentNotFoundException" ->
                                                "Не найдено студентов, соответствующиих поисковому запросу!";
                                        default -> throw new IllegalStateException(
                                                "Unexpected value: " +
                                                        exceptionName);
                                    },
                                    "Ошибка", JOptionPane.ERROR_MESSAGE);
                        }
                }
                searchType[0] = null;
                if(needPrintMainPanel) {
                    studentLabels = printMainPanel(mainPnl, students);
                    needPrintMainPanel = false;
                }
            }
            if(!prevSortField.equals(sortType[0])) {
                sortStudents(students, sortType[0]);
                prevSortField = sortType[0];
                printStudents(studentLabels, students);
            }
        }
    }

    public static void main(String[] args) {
        String.valueOf(null);
        new LabClassUI(Tester.getStudentArr(20));
    }

    private static Student[] searchStudents(final Student[] students,
            final String searchType,
            final String key) throws EmptyStringException,
            NumberFormatException, StudentNotFoundException {
        final ArrayList<Student> relevantStudents = new ArrayList<>();
        for(Student student : students)
            if(compare(searchType, key, student) == 0)
                relevantStudents.add(student);
        if(relevantStudents.size() == 0)
            throw new StudentNotFoundException(
                    new Throwable("Не найдено студентов, содержащих " +
                            key + " в поле " + searchType));
        final Student[] newStudents = new Student[relevantStudents.size()];
        return relevantStudents.toArray(newStudents);
    }

    private static void sortStudents(Student[] students, String sortType) {
        try {
            quickSortStudents(students, 0, students.length - 1, sortType);
        } catch(EmptyStringException e) {
            e.printStackTrace();
        }
    }

    private static void quickSortStudents(final Student[] students,
            final int first, final int last,
            final String sortType) throws EmptyStringException {
        int i = first, j = last;
        Student keyStudent = students[(first + last) / 2];
        String key = switch (sortType) {
            case "Name" -> keyStudent.name;
            case "ID" -> String.valueOf(keyStudent.getID());
            case "Grade" -> String.valueOf(keyStudent.getAverageGrade());
            default -> throw new IllegalStateException(
                    "Unexpected value: " + sortType);
        };

        while(i < j) {
            while(compare(sortType, key, students[i]) > 0)
                i++;
            while(compare(sortType, key, students[j]) < 0)
                j--;

            if(i <= j) {
                if(i < j) {
                    Student temp = students[i];
                    students[i] = students[j];
                    students[j] = temp;
                }
                i++;
                j--;
            }
        }
        if(i < last)
            quickSortStudents(students, i, last, sortType);
        if(first < j)
            quickSortStudents(students, first, j, sortType);
    }

    private static int compare(final String compareType,
            final String valueToCompare,
            final Student student) throws EmptyStringException, NumberFormatException {
        if(valueToCompare == null || valueToCompare.length() == 0)
            throw new EmptyStringException(
                    new Throwable("Задан пустой поисковой запрос"));
        Long valueToCompareLong = null;
        if(compareType.equals("ID"))
            valueToCompareLong = Long.parseLong(valueToCompare);
        Double valueToCompareDouble = null;
        if(compareType.equals("Grade"))
            valueToCompareDouble = Double.parseDouble(valueToCompare);
        return switch (compareType) {
            case "Name" -> valueToCompare.compareTo(student.name);
            case "ID" -> valueToCompareLong.compareTo(student.getID());
            case "Grade" -> valueToCompareDouble.compareTo(
                    student.getAverageGrade()) * -1;
            default -> throw new IllegalStateException(
                    "Unexpected value: " + compareType);
        };
    }

    private Label[][] printMainPanel(final JPanel[] mainPnl,
            final Student[] students) {
        mainPnl[0].removeAll();
        mainPnl[0].setLayout(new GridLayout(students.length + 1, 4));
        final String[] headers =
                new String[]{"Имя", "Пол", "ID", "Средний балл"};
        final JPanel[] headersPnls = new JPanel[headers.length];
        for(int i = 0; i < headers.length; i++) {
            headersPnls[i] = new JPanel();
            headersPnls[i].add(new JLabel(headers[i]));
            mainPnl[0].add(headersPnls[i]);
        }

        final JPanel[][] studentPnls =
                new JPanel[students.length][headers.length];
        final Label[][] studentLabels =
                new Label[students.length][headers.length];
        for(int i = 0; i < students.length; i++) {
            for(int j = 0; j < headers.length; j++) {
                studentPnls[i][j] = new JPanel();
                studentLabels[i][j] = new Label();
                studentPnls[i][j].add(studentLabels[i][j]);
                mainPnl[0].add(studentPnls[i][j]);
            }
        }
        setSize(400, students.length * 33 + 98);
        printStudents(studentLabels, students);
        return studentLabels;
    }

    private void printStudents(final Label[][] studentLabels,
            final Student[] students) {
        for(int i = 0; i < students.length; i++) {
            studentLabels[i][0].setText(students[i].name);
            studentLabels[i][1].setText(students[i].gender ? "М" : "Ж");
            studentLabels[i][2].setText(String.valueOf(students[i].getID()));
            studentLabels[i][3].setText(
                    String.valueOf(students[i].getAverageGrade()));
        }
        setVisible(true); //Чтобы выровнять текст в столбцах
    }

    private static class EmptyStringException extends Exception {
        public EmptyStringException(Throwable err) {
            super(err);
        }
    }

    private static class StudentNotFoundException extends Exception {
        public StudentNotFoundException(Throwable err) {
            super(err);
        }
    }
}
