package bank_system;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CheckingAccountTest {

    private CheckingAccount checkingAccount;
    private final long ACCOUNT_NUMBER = 123456789L;
    private final double INITIAL_BALANCE = 5000.0;

    @BeforeEach
    void setUp() {
        // Khởi tạo tài khoản vãng lai với 5,000 ban đầu
        checkingAccount = new CheckingAccount(ACCOUNT_NUMBER, INITIAL_BALANCE);
    }

    @Test
    @DisplayName("Nạp tiền vào tài khoản vãng lai thành công")
    void testDepositSuccess() {
        double depositAmount = 2000.0;
        checkingAccount.deposit(depositAmount);

        assertEquals(7000.0, checkingAccount.getBalance(), "Số dư phải tăng lên sau khi nạp");
        assertEquals(1, checkingAccount.getTransactionList().size(), "Phải ghi nhận 1 giao dịch");
    }

    @Test
    @DisplayName("Rút tiền từ tài khoản vãng lai thành công")
    void testWithdrawSuccess() {
        double withdrawAmount = 10000.0;
        checkingAccount.withdraw(withdrawAmount);

        assertEquals(4000.0, checkingAccount.getBalance(), "Số dư phải giảm sau khi rút");
        assertEquals(1, checkingAccount.getTransactionList().size(), "Phải ghi nhận 1 giao dịch");
    }

    @Test
    @DisplayName("Giao dịch thất bại không được thay đổi số dư")
    void testTransactionFailureDoesNotChangeBalance() {
        // Giả sử rút quá số tiền đang có (Nếu lớp cha Account kiểm tra điều này)
        double invalidAmount = 10000.0;
        checkingAccount.withdraw(invalidAmount);

        // Vì hiện tại CheckingAccount đang 'nuốt' lỗi (catch nhưng không throw),
        // ta kiểm tra xem số dư có bị thay đổi sai trái hay không.
        assertEquals(INITIAL_BALANCE, checkingAccount.getBalance(),
                "Số dư không được thay đổi khi giao dịch lỗi");
    }
}