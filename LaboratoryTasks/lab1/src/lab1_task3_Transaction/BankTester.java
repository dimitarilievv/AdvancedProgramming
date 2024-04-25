package lab1_task3_Transaction;

import java.util.*;
import java.util.stream.Collectors;
class Account{
    private String name;
    private long ID;
    private String balance;

    public Account(String name, String balance) {
        this.name = name;
        this.balance = balance;
        Random r=new Random();
        this.ID=r.nextLong();
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return ID;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();
        sb.append("Name: ").append(name).append("\n");
        sb.append("Balance: ").append(balance);
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return ID == account.ID && Objects.equals(name, account.name) && Objects.equals(balance, account.balance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name,ID,balance);
    }
}
abstract class Transaction{
    private long fromId;
    private long toId;
    private String description;
    private String amount;

    public Transaction(long fromId, long toId, String description, String amount) {
        this.fromId = fromId;
        this.toId = toId;
        this.description = description;
        this.amount = amount;
    }

    public long getFromId() {
        return fromId;
    }

    public long getToId() {
        return toId;
    }

    public String getDescription() {
        return description;
    }

    public String getAmount() {
        return amount;
    }
    public abstract double getProvision();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return fromId == that.fromId && toId == that.toId && Objects.equals(description, that.description) && Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fromId, toId, description, amount);
    }
}
class FlatAmountProvisionTransaction extends Transaction{
    private final String flatProvision;
    public FlatAmountProvisionTransaction(long fromId, long toId, String amount, String flatProvision) {
        super(fromId, toId, "FlatAmount", amount);
        this.flatProvision= flatProvision;
    }
    public double parseStringToDouble(String s){
        return Double.parseDouble(s.substring(0,s.length()-1));
    }
    @Override
    public double getProvision() {
        return parseStringToDouble(flatProvision);
    }

    public String getFlatProvision() {
        return flatProvision;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FlatAmountProvisionTransaction that = (FlatAmountProvisionTransaction) o;
        return Objects.equals(flatProvision, that.flatProvision);
    }

    @Override
    public int hashCode() {
        return Objects.hash(flatProvision);
    }
}
class FlatPercentProvisionTransaction extends Transaction{
    private final int centsPerDolar;
    public FlatPercentProvisionTransaction(long fromId, long toId, String amount,  int centsPerDolar) {
        super(fromId, toId, "FlatPercent", amount);
        this.centsPerDolar=centsPerDolar;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FlatPercentProvisionTransaction that = (FlatPercentProvisionTransaction) o;
        return centsPerDolar == that.centsPerDolar;
    }

    @Override
    public int hashCode() {
        return Objects.hash(centsPerDolar);
    }
    public double parseStringToDouble(String s){
        return Double.parseDouble(s.substring(0,s.length()-1));
    }
    @Override
    public double getProvision() {
        return (int)parseStringToDouble(getAmount())*(centsPerDolar/100.0);

    }
    public int getCentsPerDolar() {
        return centsPerDolar;
    }
}
class Bank{
    private final String name;
    private final Account []accounts;
    private double totalTransfers;
    private double totalProvision;

    public Account[] getAccounts() {
        return accounts;
    }

    public Bank(String name, Account[] accounts) {
        this.name = name;
        this.accounts = new Account[accounts.length];
        System.arraycopy(accounts,0,this.accounts,0,accounts.length);
        this.totalProvision=0;
        this.totalTransfers=0;
    }
    public double parseStringToDouble(String s){
        return Double.parseDouble(s.substring(0,s.length()-1));
    }
    boolean makeTransaction(Transaction t){
        //dali i dvete smetki from i to se vo bankata vo akkauntite
        int indexFrom=findId(t.getFromId());
        int indexTo=findId(t.getToId());
        if(indexFrom==-1 || indexTo==-1){
            return false;
        }
        //dali fromId ja ima potrebnata suma
        double balanceFrom= parseStringToDouble(accounts[indexFrom].getBalance());
        double balanceTo=parseStringToDouble(accounts[indexTo].getBalance());
        double transactionAmount=parseStringToDouble(t.getAmount());
        if(balanceFrom<transactionAmount){
            return false;
        }
        double provision=t.getProvision();
        totalTransfers+=transactionAmount;
        totalProvision+=provision;
        if(indexFrom==indexTo){
            accounts[indexFrom].setBalance(String.format("%.2f$",balanceFrom-provision));
        }else{
            accounts[indexFrom].setBalance(String.format("%.2f$",balanceFrom-provision-transactionAmount));
            accounts[indexTo].setBalance(String.format("%.2f$",balanceTo+transactionAmount));
        }
        return true;
    }

    private int findId(long Id){
        int index=-1;
        for (int i = 0; i < accounts.length; i++) {
            if(accounts[i].getId()==Id){
                index=i;
                break;
            }
        }
        return index;
    }

    public String totalProvision() {
        return String.format("%.2f$", totalProvision);
    }

    public String totalTransfers() {
        return String.format("%.2f$", totalTransfers);
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Name: ").append(name).append("\n\n");
        for (Account account : accounts) {
            sb.append(account.toString()).append("\n");
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bank bank = (Bank) o;
        return Double.compare(totalTransfers, bank.totalTransfers) == 0 && Double.compare(totalProvision, bank.totalProvision) == 0 && Objects.equals(name, bank.name) && Arrays.equals(accounts, bank.accounts);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(name, totalTransfers, totalProvision);
        result = 31 * result + Arrays.hashCode(accounts);
        return result;
    }
}
public class BankTester {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        String test_type = jin.nextLine();
        switch (test_type) {
            case "typical_usage":
                testTypicalUsage(jin);
                break;
            case "equals":
                testEquals();
                break;
        }
        jin.close();
    }

    private static void testEquals() {
        Account a1 = new Account("Andrej", "20.00$");
        Account a2 = new Account("Andrej", "20.00$");
        Account a3 = new Account("Andrej", "30.00$");
        Account a4 = new Account("Gajduk", "20.00$");
        List<Account> all = Arrays.asList(a1, a2, a3, a4);
        if (!(a1.equals(a1)&&!a1.equals(a2)&&!a2.equals(a1) && !a3.equals(a1)
                && !a4.equals(a1)
                && !a1.equals(null))) {
            System.out.println("Your account equals method does not work properly.");
            return;
        }
        Set<Long> ids = all.stream().map(Account::getId).collect(Collectors.toSet());
        if (ids.size() != all.size()) {
            System.out.println("Different accounts have the same IDS. This is not allowed");
            return;
        }
        FlatAmountProvisionTransaction fa1 = new FlatAmountProvisionTransaction(10, 20, "20.00$", "10.00$");
        FlatAmountProvisionTransaction fa2 = new FlatAmountProvisionTransaction(20, 20, "20.00$", "10.00$");
        FlatAmountProvisionTransaction fa3 = new FlatAmountProvisionTransaction(20, 10, "20.00$", "10.00$");
        FlatAmountProvisionTransaction fa4 = new FlatAmountProvisionTransaction(10, 20, "50.00$", "50.00$");
        FlatAmountProvisionTransaction fa5 = new FlatAmountProvisionTransaction(30, 40, "20.00$", "10.00$");
        FlatPercentProvisionTransaction fp1 = new FlatPercentProvisionTransaction(10, 20, "20.00$", 10);
        FlatPercentProvisionTransaction fp2 = new FlatPercentProvisionTransaction(10, 20, "20.00$", 10);
        FlatPercentProvisionTransaction fp3 = new FlatPercentProvisionTransaction(10, 10, "20.00$", 10);
        FlatPercentProvisionTransaction fp4 = new FlatPercentProvisionTransaction(10, 20, "50.00$", 10);
        FlatPercentProvisionTransaction fp5 = new FlatPercentProvisionTransaction(10, 20, "20.00$", 30);
        FlatPercentProvisionTransaction fp6 = new FlatPercentProvisionTransaction(30, 40, "20.00$", 10);
        if (fa1.equals(fa1) &&
                !fa2.equals(null) &&
                fa2.equals(fa1) &&
                fa1.equals(fa2) &&
                fa1.equals(fa3) &&
                !fa1.equals(fa4) &&
                !fa1.equals(fa5) &&
                !fa1.equals(fp1) &&
                fp1.equals(fp1) &&
                !fp2.equals(null) &&
                fp2.equals(fp1) &&
                fp1.equals(fp2) &&
                fp1.equals(fp3) &&
                !fp1.equals(fp4) &&
                !fp1.equals(fp5) &&
                !fp1.equals(fp6)) {
            System.out.println("Your transactions equals methods do not work properly.");
            return;
        }
        Account accounts[] = new Account[]{a1, a2, a3, a4};
        Account accounts1[] = new Account[]{a2, a1, a3, a4};
        Account accounts2[] = new Account[]{a1, a2, a3};
        Account accounts3[] = new Account[]{a1, a2, a3, a4};

        Bank b1 = new Bank("Test", accounts);
        Bank b2 = new Bank("Test", accounts1);
        Bank b3 = new Bank("Test", accounts2);
        Bank b4 = new Bank("Sample", accounts);
        Bank b5 = new Bank("Test", accounts3);

        if (!(b1.equals(b1) &&
                !b1.equals(null) &&
                !b1.equals(b2) &&
                !b2.equals(b1) &&
                !b1.equals(b3) &&
                !b3.equals(b1) &&
                !b1.equals(b4) &&
                b1.equals(b5))) {
            System.out.println("Your bank equals method do not work properly.");
            return;
        }
        //accounts[2] = a1;
        if (!b1.equals(b5)) {
            System.out.println("Your bank equals method do not work properly.");
            return;
        }
        long from_id = a2.getId();
        long to_id = a3.getId();
        Transaction t = new FlatAmountProvisionTransaction(from_id, to_id, "3.00$", "3.00$");
        b1.makeTransaction(t);
        if (b1.equals(b5)) {
            System.out.println("Your bank equals method do not work properly.");
            return;
        }
        b5.makeTransaction(t);
        if (!b1.equals(b5)) {
            System.out.println("Your bank equals method do not work properly.");
            return;
        }
        System.out.println("All your equals methods work properly.");
    }

    private static void testTypicalUsage(Scanner jin) {
        String bank_name = jin.nextLine();
        int num_accounts = jin.nextInt();
        jin.nextLine();
        Account accounts[] = new Account[num_accounts];
        for (int i = 0; i < num_accounts; ++i)
            accounts[i] = new Account(jin.nextLine(), jin.nextLine());
        Bank bank = new Bank(bank_name, accounts);
        while (true) {
            String line = jin.nextLine();
            switch (line) {
                case "stop":
                    return;
                case "transaction":
                    String descrption = jin.nextLine();
                    String amount = jin.nextLine();
                    String parameter = jin.nextLine();
                    int from_idx = jin.nextInt();
                    int to_idx = jin.nextInt();
                    jin.nextLine();
                    Transaction t = getTransaction(descrption, from_idx, to_idx, amount, parameter, bank);
                    System.out.println("Transaction amount: " + t.getAmount());
                    System.out.println("Transaction description: " + t.getDescription());
                    System.out.println("Transaction successful? " + bank.makeTransaction(t));
                    break;
                case "print":
                    System.out.println(bank.toString());
                    System.out.println("Total provisions: " + bank.totalProvision());
                    System.out.println("Total transfers: " + bank.totalTransfers());
                    System.out.println();
                    break;
            }
        }
    }

    private static Transaction getTransaction(String description, int from_idx, int to_idx, String amount, String o, Bank bank) {
        switch (description) {
            case "FlatAmount":
                return new FlatAmountProvisionTransaction(bank.getAccounts()[from_idx].getId(),
                        bank.getAccounts()[to_idx].getId(), amount, o);
            case "FlatPercent":
                return new FlatPercentProvisionTransaction(bank.getAccounts()[from_idx].getId(),
                        bank.getAccounts()[to_idx].getId(), amount, Integer.parseInt(o));
        }
        return null;
    }


}

