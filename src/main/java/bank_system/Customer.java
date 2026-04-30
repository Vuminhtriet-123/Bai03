package bank_system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Đại diện cho một khách hàng với họ tên, số CMND và danh sách tài khoản.
 */
public class Customer {
    private static final Logger logger = LoggerFactory.getLogger(Customer.class);

    private long idNumber;
    private String fullName;
    private List<Account> accountList;

    public Customer() {
        this(0L, "");
    }

    public Customer(long idNumber, String fullName) {
        this.idNumber = idNumber;
        this.fullName = fullName;
        this.accountList = new ArrayList<>();
        logger.debug("Tạo khách hàng mới: ID={}, tên={}", idNumber, fullName);
    }

    // Getters & Setters
    public long getIdNumber() { return idNumber; }
    public void setIdNumber(long idNumber) { this.idNumber = idNumber; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public List<Account> getAccountList() { return accountList; }

    public void setAccountList(List<Account> accountList) {
        if (accountList == null) {
            this.accountList = new ArrayList<>();
        } else {
            this.accountList = accountList;
        }
    }

    public void addAccount(Account account) {
        if (account == null) {
            logger.warn("Thử thêm tài khoản null vào khách hàng ID={}", idNumber);
            return;
        }
        if (!accountList.contains(account)) {
            accountList.add(account);
            logger.info("Thêm tài khoản {} cho khách hàng ID={}",
                    account.getAccountNumber(), idNumber);
        }
    }

    public void removeAccount(Account account) {
        if (account == null) return;
        if (accountList.remove(account)) {
            logger.info("Xóa tài khoản {} khỏi khách hàng ID={}",
                    account.getAccountNumber(), idNumber);
        }
    }

    public String getCustomerInfo() {
        return "Số CMND: " + idNumber + ". Họ tên: " + fullName + ".";
    }
}