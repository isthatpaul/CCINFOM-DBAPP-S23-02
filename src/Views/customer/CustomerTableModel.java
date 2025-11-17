package Views.customer;

import Model.Customer;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Custom table model for Customer data
 */
public class CustomerTableModel extends AbstractTableModel {

    private final String[] columnNames = {
            "ID", "Account #", "First Name", "Last Name",
            "City", "Contact", "Status"
    };

    private List<Customer> customers;

    public CustomerTableModel() {
        this.customers = new ArrayList<>();
    }

    public CustomerTableModel(List<Customer> customers) {
        this.customers = customers;
    }

    @Override
    public int getRowCount() {
        return customers.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Customer customer = customers.get(rowIndex);

        switch (columnIndex) {
            case 0: return customer.customerID();
            case 1: return customer.accountNumber();
            case 2: return customer.firstName();
            case 3: return customer.lastName();
            case 4: return customer.city();
            case 5: return customer.contactNumber();
            case 6: return customer.billingStatus();
            default: return null;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
        fireTableDataChanged();
    }

    public Customer getCustomerAt(int rowIndex) {
        return customers.get(rowIndex);
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
        fireTableRowsInserted(customers.size() - 1, customers.size() - 1);
    }

    public void updateCustomer(int rowIndex, Customer customer) {
        customers.set(rowIndex, customer);
        fireTableRowsUpdated(rowIndex, rowIndex);
    }

    public void removeCustomer(int rowIndex) {
        customers.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }

    public void clear() {
        customers.clear();
        fireTableDataChanged();
    }
}