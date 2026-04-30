package bank_system;

/**
 * Ngoại lệ chung cho hệ thống ngân hàng.
 * Các ngoại lệ cụ thể sẽ kế thừa lớp này.
 */
public class BankException extends Exception {
    public BankException(String message) {
        super(message);  // Gọi constructor của Exception
    }
}