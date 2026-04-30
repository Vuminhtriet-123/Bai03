package bank_system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Tài khoản vãng lai (Checking Account) – không có hạn mức đặc biệt.
 */
public class CheckingAccount extends Account {
    private static final Logger logger = LoggerFactory.getLogger(CheckingAccount.class);

    public CheckingAccount(long accountNumber, double balance) {
        super(accountNumber, balance);
    }

    @Override
    public void deposit(double amount) {
        double initialBalance = getBalance();
        logger.info("Bắt đầu nạp tiền vào CHECKING {}: số tiền={}", getAccountNumber(), amount);
        try {
            doDepositing(amount);
            double finalBalance = getBalance();
            Transaction transaction = new Transaction(
                    Transaction.TYPE_DEPOSIT_CHECKING,
                    amount,
                    initialBalance,
                    finalBalance);
            addTransaction(transaction);
            logger.info("Nạp tiền thành công vào tài khoản {}. Số dư mới: {}",
                    getAccountNumber(), finalBalance);
        } catch (InvalidFundingAmountException e) {
            logger.error("Nạp tiền thất bại do số tiền không hợp lệ: {}", amount, e);
        }
    }

    @Override
    public void withdraw(double amount) {
        double initialBalance = getBalance();
        logger.info("Bắt đầu rút tiền từ CHECKING {}: số tiền={}", getAccountNumber(), amount);
        try {
            doWithdrawing(amount);
            double finalBalance = getBalance();
            Transaction transaction = new Transaction(
                    Transaction.TYPE_WITHDRAW_CHECKING,
                    amount,
                    initialBalance,
                    finalBalance);
            addTransaction(transaction);
            logger.info("Rút tiền thành công từ tài khoản {}. Số dư mới: {}",
                    getAccountNumber(), finalBalance);
        } catch (InvalidFundingAmountException | InsufficientFundsException e) {
            logger.error("Rút tiền thất bại từ tài khoản {}: {}",
                    getAccountNumber(), e.getMessage(), e);
        }
    }
}