package lab3_task2_PhoneBook;

// package PhoneContact;

import java.io.*;
import java.util.*;
import java.util.stream.IntStream;

class InvalidNameException extends Exception {
    public String name;

    public InvalidNameException(String name) {
        super();
        this.name=name;
    }
}

class InvalidNumberException extends Exception {
    public InvalidNumberException() {
        super();
    }
}

class MaximumSizeExceddedException extends Exception {
    public MaximumSizeExceddedException() {
        super();
    }
}

class InvalidFormatException extends Exception {
    public InvalidFormatException() {
        super();
    }
}

class Contact implements Comparable<Contact>{
    private String name;
    private String[] phonenumber;

    public Contact(String name, String... phonenumber) throws InvalidNameException, InvalidNumberException, MaximumSizeExceddedException {
//        if (name.length() < 4 || name.length() > 10) {
//            throw new InvalidNameException(name);
//        }
        if (!checkName(name))
            throw new InvalidNameException(name);
        this.name = name;
        if (!Arrays.stream(phonenumber).allMatch(this::checkPhone)) {
            throw new InvalidNumberException();
        }
        if (phonenumber.length > 5) {
            throw new MaximumSizeExceddedException();
        }
        this.phonenumber = new String[phonenumber.length];
        for (int i = 0; i < phonenumber.length; i++) {
            this.phonenumber[i] = phonenumber[i];
        }
//        IntStream.range(0, phonenumber.length).forEach(i->this.phonenumber[i]=phonenumber[i]);
    }

    private boolean checkName(String name) {
        return name.matches("[a-zA-Z0-9]{5,10}");
    }

    private boolean checkPhone(String phone) {
        return phone.matches("07[0125678][0-9]{6}");
    }

    public String getName() {
        return name;
    }

    public String[] getNumbers() {
        return Arrays.stream(phonenumber)
                .sorted()
                .toArray(String[]::new);
    }

    void addNumber(String number) throws InvalidNumberException {
        if (!checkPhone(number)) {
            throw new InvalidNumberException();
        }
        phonenumber = Arrays.copyOf(phonenumber, phonenumber.length + 1);
        phonenumber[phonenumber.length - 1] = number;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append("\n");
        sb.append(phonenumber.length).append("\n");
        Arrays.stream(phonenumber)
                .sorted()
                .forEach(i -> sb.append(i)
                        .append("\n"));
        return sb.toString();
    }

    public static Contact valueOf(String s) throws InvalidFormatException {
        try {
            return new Contact(s);
        } catch (Exception e) {
            throw new InvalidFormatException();
        }
    }

    @Override
    public int compareTo(Contact o) {
        return name.compareTo(o.name);
    }
    public boolean hasNumber(String s) {
        return Arrays.stream(phonenumber)
                .anyMatch(i -> i.startsWith(s));
    }
}
class PhoneBook{
    Contact []contacts;

    public PhoneBook() {
        this.contacts=new Contact[0];
    }
    void addContact(Contact contact) throws MaximumSizeExceddedException, InvalidNameException {
        if(contacts.length>=250){
            throw new MaximumSizeExceddedException();
        }
//        Contact []n=new Contact[contacts.length+1];
//        System.arraycopy(contacts, 0, n, 0, contacts.length);
//        n[contacts.length-1]=contact;
//        contacts=n;
        if(contactExsists(contact)){
            throw new InvalidNameException(contact.getName());
        }
        contacts = Arrays.copyOf(contacts, contacts.length + 1);
        contacts[contacts.length - 1] = contact;
    }
    private boolean contactExsists(Contact c){
        return Arrays.stream(contacts)
                .anyMatch(i->i.getName().equals(c.getName()));
    }
    public Contact getContactForName(String name){
        return Arrays.stream(contacts)
                .filter(i->i.getName().equals(name))
                .findFirst()
                .orElse(null);
    }
    public int numberOfContacts(){
        return contacts.length;
    }

    public Contact[] getContacts() {
        return Arrays.stream(contacts)
                .sorted(Comparator.comparing(Contact::getName))
                .toArray(Contact[]::new);
    }
    boolean removeContact(String name){
        for (Contact c : contacts) {
            if (c.getName().equals(name)) {
                contacts=IntStream.range(0,contacts.length)
                        .filter(i->!contacts[i].getName().equals(name))
                        .mapToObj(i->contacts[i])
                        .toArray(Contact[]::new);
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();
        Arrays.stream(contacts)
                .sorted()
                .forEach(i->sb.append(i).append("\n"));
        return sb.toString();
    }
    static boolean saveAsTextFile(PhoneBook phonebook, String path){
        try {
            ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream(path));
            oos.writeObject(phonebook);
            oos.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }
    static PhoneBook loadFromTextFile(String path) throws InvalidFormatException {
        PhoneBook p=null;
        ObjectInputStream ois= null;
        try {
            ois = new ObjectInputStream(new FileInputStream(path));
            p=(PhoneBook)ois.readObject();
            ois.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new InvalidFormatException();
        }
        return p;
    }
    Contact[] getContactsForNumber(String number_prefix){
        return Arrays.stream(contacts)
                .filter(i -> i.hasNumber(number_prefix))
                .toArray(Contact[]::new);
    }
}
public class PhonebookTester {

    public static void main(String[] args) throws Exception {
        Scanner jin = new Scanner(System.in);
        String line = jin.nextLine();
        switch( line ) {
            case "test_contact":
                testContact(jin);
                break;
            case "test_phonebook_exceptions":
                testPhonebookExceptions(jin);
                break;
            case "test_usage":
                testUsage(jin);
                break;
        }
    }

    private static void testFile(Scanner jin) throws Exception {
        PhoneBook phonebook = new PhoneBook();
        while ( jin.hasNextLine() )
            phonebook.addContact(new Contact(jin.nextLine(),jin.nextLine().split("\\s++")));
        String text_file = "phonebook.txt";
        PhoneBook.saveAsTextFile(phonebook,text_file);
        PhoneBook pb = PhoneBook.loadFromTextFile(text_file);
        if ( ! pb.equals(phonebook) ) System.out.println("Your file saving and loading doesn't seem to work right");
        else System.out.println("Your file saving and loading works great. Good job!");
    }

    private static void testUsage(Scanner jin) throws Exception {
        PhoneBook phonebook = new PhoneBook();
        while ( jin.hasNextLine() ) {
            String command = jin.nextLine();
            switch ( command ) {
                case "add":
                    phonebook.addContact(new Contact(jin.nextLine(),jin.nextLine().split("\\s++")));
                    break;
                case "remove":
                    phonebook.removeContact(jin.nextLine());
                    break;
                case "print":
                    System.out.println(phonebook.numberOfContacts());
                    System.out.println(Arrays.toString(phonebook.getContacts()));
                    System.out.println(phonebook.toString());
                    break;
                case "get_name":
                    System.out.println(phonebook.getContactForName(jin.nextLine()));
                    break;
                case "get_number":
                    System.out.println(Arrays.toString(phonebook.getContactsForNumber(jin.nextLine())));
                    break;
            }
        }
    }

    private static void testPhonebookExceptions(Scanner jin) {
        PhoneBook phonebook = new PhoneBook();
        boolean exception_thrown = false;
        try {
            while ( jin.hasNextLine() ) {
                phonebook.addContact(new Contact(jin.nextLine()));
            }
        }
        catch ( InvalidNameException e ) {
            System.out.println(e.name);
            exception_thrown = true;
        }
        catch ( Exception e ) {}
        if ( ! exception_thrown ) System.out.println("Your addContact method doesn't throw InvalidNameException");
        /*
		exception_thrown = false;
		try {
		phonebook.addContact(new Contact(jin.nextLine()));
		} catch ( MaximumSizeExceddedException e ) {
			exception_thrown = true;
		}
		catch ( Exception e ) {}
		if ( ! exception_thrown ) System.out.println("Your addContact method doesn't throw MaximumSizeExcededException");
        */
    }

    private static void testContact(Scanner jin) throws Exception {
        boolean exception_thrown = true;
        String names_to_test[] = { "And\nrej","asd","AAAAAAAAAAAAAAAAAAAAAA","Ð�Ð½Ð´Ñ€ÐµÑ˜A123213","Andrej#","Andrej<3"};
        for ( String name : names_to_test ) {
            try {
                new Contact(name);
                exception_thrown = false;
            } catch (InvalidNameException e) {
                exception_thrown = true;
            }
            if ( ! exception_thrown ) System.out.println("Your Contact constructor doesn't throw an InvalidNameException");
        }
        String numbers_to_test[] = { "+071718028","number","078asdasdasd","070asdqwe","070a56798","07045678a","123456789","074456798","073456798","079456798" };
        for ( String number : numbers_to_test ) {
            try {
                new Contact("Andrej",number);
                exception_thrown = false;
            } catch (InvalidNumberException e) {
                exception_thrown = true;
            }
            if ( ! exception_thrown ) System.out.println("Your Contact constructor doesn't throw an InvalidNumberException");
        }
        String nums[] = new String[10];
        for ( int i = 0 ; i < nums.length ; ++i ) nums[i] = getRandomLegitNumber();
        try {
            new Contact("Andrej",nums);
            exception_thrown = false;
        } catch (MaximumSizeExceddedException e) {
            exception_thrown = true;
        }
        if ( ! exception_thrown ) System.out.println("Your Contact constructor doesn't throw a MaximumSizeExceddedException");
        Random rnd = new Random(5);
        Contact contact = new Contact("Andrej",getRandomLegitNumber(rnd),getRandomLegitNumber(rnd),getRandomLegitNumber(rnd));
        System.out.println(contact.getName());
        System.out.println(Arrays.toString(contact.getNumbers()));
        System.out.println(contact.toString());
        contact.addNumber(getRandomLegitNumber(rnd));
        System.out.println(Arrays.toString(contact.getNumbers()));
        System.out.println(contact.toString());
        contact.addNumber(getRandomLegitNumber(rnd));
        System.out.println(Arrays.toString(contact.getNumbers()));
        System.out.println(contact.toString());
    }

    static String[] legit_prefixes = {"070","071","072","075","076","077","078"};
    static Random rnd = new Random();

    private static String getRandomLegitNumber() {
        return getRandomLegitNumber(rnd);
    }

    private static String getRandomLegitNumber(Random rnd) {
        StringBuilder sb = new StringBuilder(legit_prefixes[rnd.nextInt(legit_prefixes.length)]);
        for ( int i = 3 ; i < 9 ; ++i )
            sb.append(rnd.nextInt(10));
        return sb.toString();
    }


}