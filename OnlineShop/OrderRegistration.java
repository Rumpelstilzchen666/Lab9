package OnlineShop;

import javax.swing.*;
import java.awt.*;

public class OrderRegistration {
    //Корректные ИНН: 123456789047 777777777708 000000000000
    private static final int nFields = 2;
    private static final String[] fieldTitles = {"ФИО", "ИНН"};
    private final String[] fields = new String[nFields];

    public static void main(String[] args) {
        new OrderRegistration();
    }

    public OrderRegistration() {
        JFrame frame = new JFrame("Order Registration");
        frame.setSize(400, 39 + (nFields + 2) * (26 + 5));
        frame.setLayout(new GridLayout(nFields + 2, 1));
        JPanel[] fieldPnl = new JPanel[nFields];
        JLabel[] fieldLbl = new JLabel[nFields];
        JTextField[] fieldJTF = new JTextField[nFields];

        for(int i = 0; i < nFields; i++) {
            fieldPnl[i] = new JPanel(new GridLayout(1, 2));
            fieldLbl[i] = new JLabel("Введите " + fieldTitles[i]);
            JPanel fieldLabelPnl = new JPanel();
            fieldLabelPnl.add(fieldLbl[i]);
            fieldPnl[i].add(fieldLabelPnl);
            fieldJTF[i] = new JTextField(15);
            JPanel fieldTextFieldPnl = new JPanel();
            fieldTextFieldPnl.add(fieldJTF[i]);
            fieldPnl[i].add(fieldTextFieldPnl);
            frame.add(fieldPnl[i]);
        }

        JPanel entryPnl = new JPanel();
        JButton entryButton = new JButton("Ввод");
        entryPnl.add(entryButton);
        frame.add(entryPnl);

        JPanel errorPnl = new JPanel();
        JLabel resultLbl = new JLabel("");
        errorPnl.add(resultLbl);
        frame.add(errorPnl);

        entryButton.addActionListener(e -> {
            for(int i = 0; i < nFields; i++) {
                fields[i] = fieldJTF[i].getText().trim();
            }
            try {
                if(checkFields()) {
                    resultLbl.setText("Данные успешно введены!");
                }
            } catch(Exception err) {
                resultLbl.setText(new String(err.getMessage().toCharArray(), 21,
                        err.getMessage().length() - 21));
            }
        });

        frame.setVisible(true);
    }

    private boolean checkFields() throws EmptyFieldException, WrongINNException {
        for(int i = 0; i < nFields; i++) {
            if(fields[i].equals(""))
                throw new EmptyFieldException(
                        new Throwable("Введите " + fieldTitles[i]));
        }

        checkINN(fields[1]);
        return true;
    }

    private int[] checkINN(String INNStr) throws WrongINNException {
        if(INNStr.length() != 12)
            throw new WrongINNLengthException(
                    new Throwable("ИНН должен содержать 12 цифр"));

        int[] INN = new int[12];
        for(int i = 0; i < 12; i++) {
            INN[i] = INNStr.charAt(i) - 48;
            if(INN[i] < 0 || INN[i] > 9)
                throw new WrongCharInINNException(
                        new Throwable("ИНН содержит недопустимые символы"));
        }

        int n10 = (7 * INN[0] + 2 * INN[1] + 4 * INN[2] + 10 * INN[3] +
                3 * INN[4] + 5 * INN[5] + 9 * INN[6] + 4 * INN[7] +
                6 * INN[8] + 8 * INN[9]) % 11 % 10;
        int n11 = (3 * INN[0] + 7 * INN[1] + 2 * INN[2] + 4 * INN[3] +
                10 * INN[4] + 3 * INN[5] + 5 * INN[6] + 9 * INN[7] +
                4 * INN[8] + 6 * INN[9] + 8 * n10) % 11 % 10;
        if(n10 != INN[10] || n11 != INN[11])
            throw new WrongINNChecksumException(
                    new Throwable("Контрольная сумма ИНН не совпадает"));
        return INN;
    }

    private static class EmptyFieldException extends Exception {
        public EmptyFieldException(Throwable err) {
            super(err);
        }
    }

    private static class WrongINNException extends Exception {
        public WrongINNException(Throwable err) {
            super(err);
        }
    }

    private static class WrongINNLengthException extends WrongINNException {
        public WrongINNLengthException(Throwable err) {
            super(err);
        }
    }

    private static class WrongCharInINNException extends WrongINNException {
        public WrongCharInINNException(Throwable err) {
            super(err);
        }
    }

    private static class WrongINNChecksumException extends WrongINNException {
        public WrongINNChecksumException(Throwable err) {
            super(err);
        }
    }
}
