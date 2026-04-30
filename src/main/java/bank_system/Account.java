package bank_system;

// Import các class cần thiết và Logger

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Lớp trừu tượng đại diện cho một tài khoản ngân hàng.
 * Chứa các thuộc tính chung: số tài khoản, số dư, danh sách giao dịch.
 */
public abstract class Account {
    // Logger riêng cho class Account (theo chuẩn SLF4J)
    private static final Logger logger = LoggerFactory.getLogger(Account.class);

    private long accountNumber;     // Số tài khoản (thay vì _accNum)
    private double balance;         // Số dư (thay vì B viết tắt khó hiểu)
    private List<Transaction> transactions;  // Danh sách giao dịch (thay vì list)

    /**
     * Constructor khởi tạo tài khoản.
     * @param accountNumber số tài khoản
     * @param balance số dư ban đầu
     */
    public Account(long accountNumber, double balance) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.transactions = new ArrayList<>();  // Khởi tạo danh sách rỗng
        // Ghi log INFO: hành động tạo tài khoản
        logger.info("Tạo tài khoản mới: số={}, số dư={}", accountNumber, balance);
    }

    // Các getter/setter tuân thủ chuẩn Java Bean (tên rõ ràng)
    public long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    protected void setBalance(double balance) {
        this.balance = balance;
    }

    public List<Transaction> getTransactionList() {
        return transactions;
    }

    public void setTransactionList(List<Transaction> transactions) {
        if (transactions == null) {
            this.transactions = new ArrayList<>();
        } else {
            this.transactions = transactions;
        }
    }

    // Phương thức trừu tượng (bắt buộc lớp con phải cài đặt)
    public abstract void deposit(double amount);
    public abstract void withdraw(double amount);

    /**
     * Thực hiện nghiệp vụ nạp tiền (cập nhật số dư).
     * @param amount số tiền nạp
     * @throws InvalidFundingAmountException nếu amount <= 0
     */
    protected void doDepositing(double amount) throws InvalidFundingAmountException {
        if (amount <= 0) {
            logger.warn("Số tiền nạp không hợp lệ: {}", amount);  // WARN vì bất thường
            throw new InvalidFundingAmountException(amount);
        }
        double oldBalance = balance;
        balance += amount;
        // DEBUG: chỉ xuất hiện khi bật level DEBUG
        logger.debug("Nạp tiền thành công: {} -> {}, số tiền: {}", oldBalance, balance, amount);
    }

    /**
     * Thực hiện nghiệp vụ rút tiền.
     * @param amount số tiền rút
     * @throws InvalidFundingAmountException nếu amount <= 0
     * @throws InsufficientFundsException nếu số dư không đủ
     */
    protected void doWithdrawing(double amount)
            throws InvalidFundingAmountException, InsufficientFundsException {
        if (amount <= 0) {
            logger.warn("Số tiền rút không hợp lệ: {}", amount);
            throw new InvalidFundingAmountException(amount);
        }
        if (amount > balance) {
            logger.warn("Số dư không đủ để rút {} từ tài khoản {}, số dư={}",
                    amount, accountNumber, balance);
            throw new InsufficientFundsException(amount);
        }
        double oldBalance = balance;
        balance -= amount;
        logger.debug("Rút tiền thành công: {} -> {}, số tiền: {}", oldBalance, balance, amount);
    }

    /**
     * Thêm một giao dịch vào lịch sử.
     * @param transaction giao dịch cần thêm
     */
    public void addTransaction(Transaction transaction) {
        if (transaction != null) {
            transactions.add(transaction);
            logger.debug("Thêm giao dịch {} vào tài khoản {}",
                    transaction.getType(), accountNumber);
        }
    }

    /**
     * Lấy lịch sử giao dịch dạng chuỗi.
     * @return chuỗi mô tả các giao dịch (mỗi giao dịch trên một dòng)
     */
    public String getTransactionHistory() {
        // Dùng StringBuilder để tối ưu hiệu năng (thay vì cộng chuỗi)
        StringBuilder builder = new StringBuilder();
        builder.append("Lịch sử giao dịch của tài khoản ").append(accountNumber).append(":\n");
        for (int i = 0; i < transactions.size(); i++) {
            builder.append(transactions.get(i).getTransactionSummary());
            if (i < transactions.size() - 1) {
                builder.append("\n");
            }
        }
        logger.debug("Đã lấy lịch sử giao dịch cho tài khoản {}", accountNumber);
        return builder.toString();
    }

    // Ghi đè equals và hashCode dựa trên accountNumber (logic đúng)
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Account)) return false;
        Account other = (Account) obj;
        return this.accountNumber == other.accountNumber;
    }

    @Override
    public int hashCode() {
        return (int) (accountNumber ^ (accountNumber >>> 32));
    }
}