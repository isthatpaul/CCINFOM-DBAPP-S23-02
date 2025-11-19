package Services;

import Model.Bill;
import Model.BillCRUD;
import Model.Customer;
import Model.CustomerCRUD;
import java.util.List;

public class CustomerService {

    private final CustomerCRUD customerCRUD = new CustomerCRUD();
    private final BillCRUD billCRUD = new BillCRUD(); // Add dependency to check for bills

    // Add new customer with business rule validation
    public boolean addCustomer(Customer customer) {
        // Rule: AccountNumber must be unique
        List<Customer> all = customerCRUD.getAllRecords();
        for (Customer c : all) {
            if (c.accountNumber().equals(customer.accountNumber())) {
                System.err.println("Error: Account Number '" + customer.accountNumber() + "' already exists.");
                return false;
            }
        }

        // Rule: Required contact info
        if (customer.firstName().isEmpty() || customer.lastName().isEmpty()
                || customer.street().isEmpty() || customer.contactNumber().isEmpty()) {
            System.err.println("Error: Incomplete customer information. All fields are required.");
            return false;
        }

        return customerCRUD.addRecord(customer);
    }

    public boolean updateCustomer(Customer customer) {
        // You can add validation rules before update
        return customerCRUD.updateRecord(customer);
    }

    /**
     * Deletes a customer only if they have no outstanding bills.
     * @param customerID The ID of the customer to delete.
     * @return true if deletion was successful, false otherwise.
     */
    public boolean deleteCustomer(int customerID) {
        // FIX: Implement the business rule: Cannot delete if customer has unpaid bills.
        List<Bill> customerBills = billCRUD.getBillsByCustomerId(customerID);
        
        for (Bill bill : customerBills) {
            String status = bill.status().toUpperCase();
            // Check for any status that indicates an outstanding balance.
            if (status.equals("UNPAID") || status.equals("PARTIALLY_PAID") || status.equals("OVERDUE")) {
                System.err.println("Error: Cannot delete customer with ID " + customerID + ". They have an outstanding bill (Bill ID: " + bill.billID() + ").");
                // We show a more user-friendly message in the panel itself.
                return false; // Prevent deletion
            }
        }

        // If no outstanding bills are found, proceed with deletion.
        return customerCRUD.deleteRecord(customerID);
    }

    public List<Customer> getAllCustomers() {
        return customerCRUD.getAllRecords();
    }

    public Customer getCustomerById(int customerID) {
        return customerCRUD.getRecordById(customerID);
    }
}