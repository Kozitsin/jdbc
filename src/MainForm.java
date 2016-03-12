import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * Created by Александр on 28.02.2016.
 */
public class MainForm extends JFrame {

    private Connectivity connectivity = new Connectivity();

    private JPanel dialogPanel = new JPanel();
    private JPanel mainPanel = new JPanel();

    private JTextField dbNameTextField = new JTextField();

    private boolean isNewDB = true;

    public MainForm() {
        this.setPreferredSize(new Dimension(400, 400));
        createDialogPanel();
        this.getContentPane().add(dialogPanel);
        this.pack();
        this.setVisible(true);
    }


    private void createDialogPanel() {
        ActionListener actionListener = new FlagOnDBChanged();

        JRadioButton createDBRadioButton = new JRadioButton(StringConstants.CREATE_DB);
        createDBRadioButton.setActionCommand(StringConstants.CREATE_DB);
        createDBRadioButton.addActionListener(actionListener);
        createDBRadioButton.setSelected(true);

        final JRadioButton connectDBRadioButton = new JRadioButton(StringConstants.CONNECT_DB);
        connectDBRadioButton.setActionCommand(StringConstants.CONNECT_DB);
        connectDBRadioButton.addActionListener(actionListener);

        ButtonGroup group = new ButtonGroup();
        group.add(createDBRadioButton);
        group.add(connectDBRadioButton);

        JButton applyButton = new JButton(StringConstants.APPLY_BUTTON);
        applyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                connectivity.setDbName(dbNameTextField.getText());
                if (isNewDB) {
                    connectivity.createNewDB();
                    if (!connectivity.isOperationStatus()) {
                        JOptionPane.showMessageDialog(null, "Couldn't create new database!", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "New database created!", "Info", JOptionPane.INFORMATION_MESSAGE);
                        createMainPage();
                    }
                } else {
                    createMainPage();
                }
            }
        });

        this.setTitle(StringConstants.TITLE_OF_DIALOG_FRAME);

        dialogPanel.add(createDBRadioButton);
        dialogPanel.add(connectDBRadioButton);
        dialogPanel.add(dbNameTextField);
        dialogPanel.add(applyButton);
        dialogPanel.setVisible(true);
        dialogPanel.setLayout(new GridLayout(4, 1));
    }

    private void createMainPage() {
        JButton deleteDBButton = new JButton(StringConstants.DELETE_DB_BUTTON);
        deleteDBButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                connectivity.deleteDB();
                if (connectivity.isOperationStatus()) {
                    JOptionPane.showMessageDialog(null, "Database deleted", "Info", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Error occurred!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JButton deleteTableButton = new JButton(StringConstants.DELETE_TABLE_BUTTON);
        deleteTableButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                connectivity.deleteTable();
                if (connectivity.isOperationStatus()) {
                    JOptionPane.showMessageDialog(null, "Table deleted", "Info", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Error occurred!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JButton createTableButton = new JButton(StringConstants.CREATE_TABLE_BUTTON);
        createTableButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                connectivity.createTable();
                if (connectivity.isOperationStatus()) {
                    JOptionPane.showMessageDialog(null, "Table created", "Info", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Error occurred!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JButton insertTableButton = new JButton(StringConstants.INSERT_BUTTON);
        insertTableButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JTextField idTextField = new JTextField();
                JTextField nameTextField = new JTextField();
                JTextField surnameTextField = new JTextField();
                JTextField ageTextField = new JTextField();

                JLabel idLabel = new JLabel(StringConstants.ID_LABEL);
                JLabel nameLabel = new JLabel(StringConstants.NAME_LABEL);
                JLabel surnameLabel = new JLabel(StringConstants.SURNAME_LABEL);
                JLabel ageLabel = new JLabel(StringConstants.AGE_LABEL);

                JPanel panel = new JPanel(new GridLayout(0, 1));

                panel.add(idLabel);
                panel.add(idTextField);

                panel.add(nameLabel);
                panel.add(nameTextField);

                panel.add(surnameLabel);
                panel.add(surnameTextField);

                panel.add(ageLabel);
                panel.add(ageTextField);

                int result = JOptionPane.showConfirmDialog(null, panel, "Insert data",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                if (result == JOptionPane.OK_OPTION) {
                    connectivity.insertInTable(Integer.parseInt(idTextField.getText()),
                                               nameTextField.getText(),
                                               surnameTextField.getText(),
                                               Integer.parseInt(ageTextField.getText()));

                    if (connectivity.isOperationStatus()) {
                        JOptionPane.showMessageDialog(null, "Insert was successful!", "Info", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Error occurred!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        JButton searchButton = new JButton(StringConstants.SEARCH_BUTTON);
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String surname = JOptionPane.showInputDialog(null, StringConstants.SURNAME_LABEL);
                ArrayList<Object[]> rs = connectivity.search(surname);

                if (rs != null && connectivity.isOperationStatus()) {
                    if (rs.size() > 0) {
                        String[] columnNames = {StringConstants.ID_LABEL,
                                StringConstants.NAME_LABEL,
                                StringConstants.SURNAME_LABEL,
                                StringConstants.AGE_LABEL
                        };
                        JTable table = new JTable();
                        DefaultTableModel model = (DefaultTableModel) table.getModel();

                        model.setColumnIdentifiers(columnNames);

                        for (Object[] r : rs) {
                            model.addRow(r);
                        }
                        table.setModel(model);

                        JPanel panel = new JPanel();
                        panel.add(new JScrollPane(table));

                        JOptionPane.showConfirmDialog(null, panel, "Selected",
                                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                    }
                    else {
                        JOptionPane.showMessageDialog(null, "Nothing found!", "Info", JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Error occurred!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JButton updateButton = new JButton(StringConstants.UPDATE_BUTTON);
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JTextField idTextField = new JTextField();
                JTextField nameTextField = new JTextField();
                JTextField surnameTextField = new JTextField();
                JTextField ageTextField = new JTextField();

                JLabel idLabel = new JLabel(StringConstants.ID_LABEL);
                JLabel nameLabel = new JLabel(StringConstants.NAME_LABEL);
                JLabel surnameLabel = new JLabel(StringConstants.SURNAME_LABEL);
                JLabel ageLabel = new JLabel(StringConstants.AGE_LABEL);

                JPanel panel = new JPanel(new GridLayout(0, 1));

                panel.add(idLabel);
                panel.add(idTextField);

                panel.add(nameLabel);
                panel.add(nameTextField);

                panel.add(surnameLabel);
                panel.add(surnameTextField);

                panel.add(ageLabel);
                panel.add(ageTextField);

                int result = JOptionPane.showConfirmDialog(null, panel, "Insert data",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                if (result == JOptionPane.OK_OPTION) {
                    connectivity.updateTable(Integer.parseInt(idTextField.getText()),
                                             nameTextField.getText(),
                                             surnameTextField.getText(),
                                             Integer.parseInt(ageTextField.getText()));

                    if (connectivity.isOperationStatus()) {
                        JOptionPane.showMessageDialog(null, "Update was successful!", "Info", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Error occurred!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        JButton deleteButton = new JButton(StringConstants.DELETE_BUTTON);
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String surname = JOptionPane.showInputDialog(null, StringConstants.SURNAME_LABEL);
                connectivity.deleteFromTable(surname);

                if (connectivity.isOperationStatus()) {
                    JOptionPane.showMessageDialog(null, "Delete was successful!", "Info", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Error occurred!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        this.setTitle(StringConstants.TITLE_OF_MAIN_FRAME);

        mainPanel.add(deleteDBButton);
        mainPanel.add(createTableButton);
        mainPanel.add(deleteTableButton);
        mainPanel.add(insertTableButton);
        mainPanel.add(searchButton);
        mainPanel.add(updateButton);
        mainPanel.add(deleteButton);

        mainPanel.setVisible(true);
        mainPanel.setLayout(new GridLayout(4, 2));

        dialogPanel.setVisible(false);
        this.getContentPane().add(mainPanel);
        this.pack();
    }


    /* inner class */
    public class FlagOnDBChanged implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            if (actionEvent.getActionCommand().equals(StringConstants.CREATE_DB)) {
                isNewDB = true;
            }
            else if (actionEvent.getActionCommand().equals(StringConstants.CONNECT_DB)) {
                isNewDB = false;
            }
        }
    }
}
