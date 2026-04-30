package bank_system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Ngân hàng quản lý danh sách khách hàng và cung cấp các chức năng đọc, sắp xếp.
 */
public class Bank {
    private static final Logger logger = LoggerFactory.getLogger(Bank.class);
    private List<Customer> customerList;

    public Bank() {
        this.customerList = new ArrayList<>();
        logger.info("Khởi tạo ngân hàng mới");
    }

    public List<Customer> getCustomerList() {
        return customerList;
    }

    public void setCustomerList(List<Customer> customerList) {
        if (customerList == null) {
            this.customerList = new ArrayList<>();
        } else {
            this.customerList = customerList;
        }
        logger.debug("Cập nhật danh sách khách hàng, kích thước={}", this.customerList.size());
    }

    /**
     * Đọc danh sách khách hàng từ InputStream theo định dạng:
     * Dòng có 9 chữ số cuối cùng là mã khách hàng, tên ở phía trước.
     * Các dòng tiếp theo chứa thông tin tài khoản: số tài khoản, loại, số dư.
     * @param inputStream luồng đầu vào (ví dụ: System.in, FileInputStream)
     */
    public void readCustomerList(InputStream inputStream) {
        logger.info("Bắt đầu đọc danh sách khách hàng từ input stream");
        if (inputStream == null) {
            logger.warn("InputStream rỗng, không thể đọc khách hàng");
            return;
        }
        // Sử dụng try-with-resources để tự động đóng reader
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            Customer current = null;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                int lastSpace = line.lastIndexOf(' ');
                if (lastSpace > 0) {
                    String token = line.substring(lastSpace + 1).trim();
                    // Nếu token là 9 chữ số → đây là dòng khách hàng
                    if (token.matches("\\d{9}")) {
                        String name = line.substring(0, lastSpace).trim();
                        current = new Customer(Long.parseLong(token), name);
                        customerList.add(current);
                        logger.info("Thêm khách hàng mới: ID={}, tên={}", token, name);
                    }
                    // Ngược lại, nếu đang có khách hàng hiện tại → dòng tài khoản
                    else if (current != null) {
                        String[] parts = line.split("\\s+");
                        if (parts.length >= 3) {
                            long accountNumber = Long.parseLong(parts[0]);
                            double balance = Double.parseDouble(parts[2]);
                            if ("CHECKING".equals(parts[1])) {
                                current.addAccount(new CheckingAccount(accountNumber, balance));
                                logger.debug("Thêm tài khoản CHECKING số {} cho KH {}",
                                        accountNumber, current.getIdNumber());
                            } else if ("SAVINGS".equals(parts[1])) {
                                current.addAccount(new SavingsAccount(accountNumber, balance));
                                logger.debug("Thêm tài khoản SAVINGS số {} cho KH {}",
                                        accountNumber, current.getIdNumber());
                            }
                        }
                    }
                }
            }
            logger.info("Đọc xong danh sách khách hàng, tổng số={}", customerList.size());
        } catch (Exception e) {
            logger.error("Lỗi khi đọc danh sách khách hàng", e);
        }
    }

    /**
     * Lấy thông tin khách hàng sắp xếp theo số CMND tăng dần.
     * @return chuỗi thông tin, mỗi khách hàng trên một dòng
     */
    public String getCustomersInfoByIdOrder() {
        List<Customer> sorted = new ArrayList<>(customerList);
        sorted.sort(Comparator.comparingLong(Customer::getIdNumber));
        logger.debug("Sắp xếp khách hàng theo ID, số lượng={}", sorted.size());

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < sorted.size(); i++) {
            builder.append(sorted.get(i).getCustomerInfo());
            if (i < sorted.size() - 1) {
                builder.append("\n");
            }
        }
        return builder.toString();
    }

    /**
     * Lấy thông tin khách hàng sắp xếp theo tên (sau đó theo ID nếu trùng tên).
     * @return chuỗi thông tin
     */
    public String getCustomersInfoByNameOrder() {
        List<Customer> sorted = new ArrayList<>(customerList);
        // Dùng lambda (Java 8) để sắp xếp
        sorted.sort((c1, c2) -> {
            int nameCompare = c1.getFullName().compareTo(c2.getFullName());
            return nameCompare != 0 ? nameCompare
                    : Long.compare(c1.getIdNumber(), c2.getIdNumber());
        });
        logger.debug("Sắp xếp khách hàng theo tên, số lượng={}", sorted.size());

        StringBuilder builder = new StringBuilder();
        for (Customer customer : sorted) {
            builder.append(customer.getCustomerInfo()).append("\n");
        }
        return builder.toString().trim();
    }
}