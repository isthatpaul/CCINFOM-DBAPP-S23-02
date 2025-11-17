package Views.customer;

import Views.components.*;
import Model.Customer;
import Model.CustomerCRUD;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Customer Management Panel
 */
public class CustomerPanel extends JPanel {

    private StyledTable customerTable;
    private DefaultTableModel tableModel;
    private CustomerCRUD customerCRUD;
    private JTextField searchField;

    public CustomerPanel() {
        customerCRUD = new CustomerCRUD();
        setLayout(new BorderLayout());
        setBackground(ColorScheme.BACKGROUND);
        initComponents();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(0, 20));
        mainPanel.setBackground(ColorScheme.BACKGROUND);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JPanel headerPanel = createHeaderPanel();
        JPanel toolbarPanel = createToolbarPanel();
        JPanel tablePanel = createTablePanel();

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout(0, 15));
        centerPanel.setBackground(ColorScheme.BACKGROUND);
        centerPanel.add(toolbarPanel, BorderLayout.NORTH);
        centerPanel.add(tablePanel, BorderLayout.CENTER);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ColorScheme.BACKGROUND);

        JLabel titleLabel = new JLabel("Customer Management");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(ColorScheme.TEXT_PRIMARY);

        panel.add(titleLabel, BorderLayout.WEST);

        return panel;
    }

    private JPanel createToolbarPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ColorScheme.BORDER, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        StyledButton addButton = new StyledButton("Add Customer", StyledButton.ButtonType.PRIMARY);
        StyledButton editButton = new StyledButton("Edit", StyledButton.ButtonType.SECONDARY);
        StyledButton deleteButton = new StyledButton("Delete", StyledButton.ButtonType.DANGER);
        StyledButton refreshButton = new StyledButton("Refresh", StyledButton.ButtonType.SECONDARY);

        searchField = new JTextField(20);
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ColorScheme.BORDER, 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        panel.add(addButton);
        panel.add(editButton);
        panel.add(deleteButton);
        panel.add(Box.createHorizontalStrut(20));
        panel.add(new JLabel("Search:"));
        panel.add(searchField);
        panel.add(refreshButton);

        refreshButton.addActionListener(e -> refreshData());

        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(ColorScheme.BORDER, 1));

        String[] columns = {"ID", "Account #", "First Name", "Last Name", "City", "Contact", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        customerTable = new StyledTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(customerTable);
        scrollPane.setBorder(null);

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    public void refreshData() {
        SwingWorker<List<Customer>, Void> worker = new SwingWorker<List<Customer>, Void>() {
            @Override
            protected List<Customer> doInBackground() {
                return customerCRUD.getAllRecords();
            }

            @Override
            protected void done() {
                try {
                    List<Customer> customers = get();
                    displayCustomers(customers);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(CustomerPanel.this,
                            "Error loading customers: " + e.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        };

        worker.execute();
    }

    private void displayCustomers(List<Customer> customers) {
        tableModel.setRowCount(0);

        for (Customer customer : customers) {
            tableModel.addRow(new Object[]{
                    customer.customerID(),
                    customer.accountNumber(),
                    customer.firstName(),
                    customer.lastName(),
                    customer.city(),
                    customer.contactNumber(),
                    customer.billingStatus()
            });
        }
    }
}