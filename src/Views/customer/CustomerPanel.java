package Views.customer;

import Views.components.*;
import Model.Customer;
import Model.CustomerCRUD;
import Services.CustomerService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class CustomerPanel extends JPanel {

    private StyledTable customerTable;
    private DefaultTableModel tableModel;
    private CustomerCRUD customerCRUD;
    private CustomerService customerService;
    private SearchBar searchBar;

    public CustomerPanel() {
        customerCRUD = new CustomerCRUD();
        customerService = new CustomerService();
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
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ColorScheme.BORDER, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        StyledButton addButton = new StyledButton("Add Customer", StyledButton.ButtonType.PRIMARY);
        StyledButton editButton = new StyledButton("Edit", StyledButton.ButtonType.SECONDARY);
        StyledButton deleteButton = new StyledButton("Delete", StyledButton.ButtonType.DANGER);
        StyledButton refreshButton = new StyledButton("Refresh", StyledButton.ButtonType.SECONDARY);

        searchBar = new SearchBar(250);
        searchBar.setPlaceholder("Search by name, account #, or ID...");
        searchBar.setSearchListener(new SearchBar.SearchListener() {
            @Override
            public void onSearch(String searchText) {
                filterData(searchText);
            }

            @Override
            public void onClear() {
                refreshData();
            }
        });

        addButton.addActionListener(e -> showFormDialog(null));
        editButton.addActionListener(e -> showEditDialog());
        deleteButton.addActionListener(e -> deleteCustomer());
        refreshButton.addActionListener(e -> refreshData());

        panel.add(addButton);
        panel.add(Box.createHorizontalStrut(10));
        panel.add(editButton);
        panel.add(Box.createHorizontalStrut(10));
        panel.add(deleteButton);
        panel.add(Box.createHorizontalGlue());
        panel.add(new JLabel("Search:"));
        panel.add(Box.createHorizontalStrut(5));
        panel.add(searchBar);
        panel.add(Box.createHorizontalStrut(10));
        panel.add(refreshButton);
        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(ColorScheme.BORDER, 1));

        String[] columns = {"ID", "Account #", "First Name", "Last Name", "City", "Contact", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };

        customerTable = new StyledTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(customerTable);
        scrollPane.setBorder(null);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    public void refreshData() {
        if(searchBar != null) searchBar.clearSearchText();
        SwingWorker<List<Customer>, Void> worker = new SwingWorker<>() {
            @Override protected List<Customer> doInBackground() { return customerCRUD.getAllRecords(); }
            @Override protected void done() {
                try {
                    displayCustomers(get());
                } catch (Exception e) { e.printStackTrace(); }
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

    private Customer getSelectedCustomer() {
        int selectedRow = customerTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a customer first.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        int customerId = (int) tableModel.getValueAt(selectedRow, 0);
        return customerCRUD.getRecordById(customerId);
    }

    private void showFormDialog(Customer customer) {
        CustomerFormDialog dialog = new CustomerFormDialog((Frame) SwingUtilities.getWindowAncestor(this), customer);
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            refreshData();
        }
    }
    
    private void showEditDialog() {
        Customer customerToEdit = getSelectedCustomer();
        if (customerToEdit != null) {
            showFormDialog(customerToEdit);
        }
    }

    private void deleteCustomer() {
        Customer customer = getSelectedCustomer();
        if (customer == null) return;

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete customer: " + customer.firstName() + " " + customer.lastName() + "?",
                "Confirm Deletion", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = customerService.deleteCustomer(customer.customerID());
            if (success) {
                JOptionPane.showMessageDialog(this, "Customer deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                refreshData();
            } else {
                JOptionPane.showMessageDialog(this, "Could not delete customer.\nThey may have outstanding bills.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void filterData(String searchText) {
        String lowerCaseText = searchText.toLowerCase();
        SwingWorker<List<Customer>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Customer> doInBackground() {
                if (lowerCaseText.isEmpty()) return customerCRUD.getAllRecords();
                return customerCRUD.getAllRecords().stream()
                        .filter(c -> c.firstName().toLowerCase().contains(lowerCaseText) ||
                                     c.lastName().toLowerCase().contains(lowerCaseText) ||
                                     c.accountNumber().toLowerCase().contains(lowerCaseText) ||
                                     String.valueOf(c.customerID()).contains(lowerCaseText))
                        .collect(Collectors.toList());
            }
            @Override
            protected void done() {
                try {
                    displayCustomers(get());
                } catch (Exception e) { e.printStackTrace(); }
            }
        };
        worker.execute();
    }
}