package bank_system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;

/**
 * Giao dịch: lưu loại giao dịch, số tiền, số dư trước/sau.
 */
public class Transaction {
    private static final Logger logger = LoggerFactory.getLogger(Transaction.class);
    private static final Locale LOCALE_US = Locale.US;

    // Các hằng số loại giao dịch (dùng thay cho magic numbers)
    public static final int TYPE_DEPOSIT_CHECKING = 1;
    public static final int TYPE_WITHDRAW_CHECKING = 2;
    public static final int TYPE_DEPOSIT_SAVINGS = 3;
    public static final int TYPE_WITHDRAW_SAVINGS = 4;

    private int type;
    private double amount;
    private double initialBalance;
    private double finalBalance;

    public Transaction(int type, double amount, double initialBalance, double finalBalance) {
        this.type = type;
        this.amount = amount;
        this.initialBalance = initialBalance;
        this.finalBalance = finalBalance;
        logger.debug("Tạo giao dịch: type={}, amount={}, initial={}, final={}",
                type, amount, initialBalance, finalBalance);
    }

    // Getters & Setters (bỏ qua để ngắn gọn, bạn tự thêm nếu cần)

    public static String getTypeString(int typeCode) {
        switch (typeCode) {
            case TYPE_DEPOSIT_CHECKING: return "Nạp tiền vãng lai";
            case TYPE_WITHDRAW_CHECKING: return "Rút tiền vãng lai";
            case TYPE_DEPOSIT_SAVINGS: return "Nạp tiền tiết kiệm";
            case TYPE_WITHDRAW_SAVINGS: return "Rút tiền tiết kiệm";
            default: return "Không rõ";
        }
    }

    public String getTransactionSummary() {
        logger.debug("Tạo tóm tắt giao dịch cho type={}", type);
        String format = "%.2f";
        return String.format("- Kiểu giao dịch: %s. Số dư ban đầu: $%s. Số tiền: $%s. Số dư cuối: $%s.",
                getTypeString(type),
                String.format(LOCALE_US, format, initialBalance),
                String.format(LOCALE_US, format, amount),
                String.format(LOCALE_US, format, finalBalance));
    }
}