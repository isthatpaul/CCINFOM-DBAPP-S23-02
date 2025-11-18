package Services;

import Model.Customer;
import Model.CustomerCRUD;
import java.util.List;

public class CustomerService {

    private final CustomerCRUD customerCRUD = new CustomerCRUD();

    // Add new customer with business rule validation
    public boolean addCustomer(Customer customer) {
        // Rule: AccountNumber must be unique
        List<Customer> all = customerCRUD.getAllRecords();
        for (Customer c : all) {
            if (c.accountNumber().equals(customer.accountNumber())) {
                System.err.println("Account Number already exists!");
                return false;
            }
            if (c.firstName().equals(customer.firstName()) &&
                    c.lastName().equals(customer.lastName()) &&
                    c.street().equals(customer.street()) &&
                    c.contactNumber().equals(customer.contactNumber())) {
                System.err.println("Customer with same name and contact exists!");
                return false;
            }
        }

        // Rule: Required contact info
        if (customer.firstName().isEmpty() || customer.lastName().isEmpty()
                || customer.street().isEmpty() || customer.contactNumber().isEmpty()) {
            System.err.println("Incomplete customer information!");
            return false;
        }

        return customerCRUD.addRecord(customer);
    }

    public boolean updateCustomer(Customer customer) {
        // You can add validation rules before update
        return customerCRUD.updateRecord(customer);
    }

    public boolean deleteCustomer(int customerID) {
        // Rule: Cannot delete if customer has unpaid bills
        // This would need a BillService check
        return customerCRUD.deleteRecord(customerID);
    }

    public List<Customer> getAllCustomers() {
        return customerCRUD.getAllRecords();
    }

    public Customer getCustomerById(int customerID) {
        return customerCRUD.getRecordById(customerID);
    }
}
