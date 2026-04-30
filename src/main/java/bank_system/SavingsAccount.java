package bank_system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Tài khoản tiết kiệm (Savings Account) có các hạn mức riêng:
 * - Mỗi lần rút không quá 1000
 * - Số dư sau rút không được dưới 5000
 */
public class SavingsAccount extends Account {
    private static final Logger logger = LoggerFactory.getLogger(SavingsAccount.class);
    private static final double MAX_WITHDRAW = 1000.0;
    private static final double MIN_BALANCE = 5000.0;

    public SavingsAccount(long accountNumber, double balance) {
        super(accountNumber, balance);
    }

    @Override
    public void deposit(double amount) {
        double initialBalance = getBalance();
        logger.info("Bắt đầu nạp tiền vào SAVINGS {}: số tiền={}", getAccountNumber(), amount);
        try {
            doDepositing(amount);
            double finalBalance = getBalance();
            Transaction transaction = new Transaction(
                    Transaction.TYPE_DEPOSIT_SAVINGS,
                    amount,
                    initialBalance,
                    finalBalance);
            addTransaction(transaction);
            logger.info("Nạp tiền thành công vào SAVINGS {}. Số dư mới: {}",
                    getAccountNumber(), finalBalance);
        } catch (InvalidFundingAmountException e) {
            logger.error("Nạp tiền thất bại (số tiền không hợp lệ) vào tài khoản {}: {}",
                    getAccountNumber(), amount, e);
        }
    }

    @Override
    public void withdraw(double amount) {
        double initialBalance = getBalance();
        logger.info("Bắt đầu rút tiền từ SAVINGS {}: số tiền={}", getAccountNumber(), amount);
        try {
            // Kiểm tra hạn mức rút của tài khoản tiết kiệm
            if (amount > MAX_WITHDRAW) {
                logger.warn("Yêu cầu rút {} vượt quá hạn mức {} cho tài khoản {}",
                        amount, MAX_WITHDRAW, getAccountNumber());
                throw new InvalidFundingAmountException(amount);
            }
            if (getBalance() - amount < MIN_BALANCE) {
                logger.warn("Sau khi rút {}, số dư tài khoản {} sẽ thấp hơn số dư tối thiểu {}",
                        amount, getAccountNumber(), MIN_BALANCE);
                throw new InsufficientFundsException(amount);
            }
            doWithdrawing(amount);
            double finalBalance = getBalance();
            Transaction transaction = new Transaction(
                    Transaction.TYPE_WITHDRAW_SAVINGS,
                    amount,
                    initialBalance,
                    finalBalance);
            addTransaction(transaction);
            logger.info("Rút tiền thành công từ SAVINGS {}. Số dư mới: {}",
                    getAccountNumber(), finalBalance);
        } catch (InvalidFundingAmountException | InsufficientFundsException e) {
            logger.error("Rút tiền thất bại từ SAVINGS {}: {}",
                    getAccountNumber(), e.getMessage(), e);
        }
    }
}