package task21_PhoneBook;
import java.util.*;
import java.util.stream.Collectors;

class DuplicateNumberException extends Exception{
    public DuplicateNumberException(String message) {
        super(message);
    }
}
class Contact implements Comparable<Contact>{
    String name;
    String number;

    public Contact(String name, String number) {
        this.name = name;
        this.number = number;
    }

    @Override
    public String toString() {
        return name+" "+number;
    }

    public String getNumber() {
        return number;
    }
    @Override
    public int compareTo(Contact o) {
        int res=this.name.compareTo(o.name);
        if(res==0){
            return this.number.compareTo(o.number);
        }
        else
            return res;
    }
}
class PhoneBook{
    Map<String,Set<Contact>> contactsByName;

    public PhoneBook() {
        contactsByName=new HashMap<>();
    }

    void addContact(String name, String number) throws DuplicateNumberException {
        if(contactsByName.containsValue(number)){
            throw new DuplicateNumberException("Duplicate number: "+number);
        }
        contactsByName.putIfAbsent(name,new TreeSet<>(Contact::compareTo));
        contactsByName.get(name).add(new Contact(name,number));
    }
    void contactsByName(String name) {
        List<Contact> matchingContacts = contactsByName.getOrDefault(name, Collections.emptySet())
                .stream()
                .sorted(Contact::compareTo)
                .collect(Collectors.toList());

        if (matchingContacts.isEmpty()) {
            System.out.println("NOT FOUND");
        } else {
            matchingContacts.forEach(System.out::println);
        }
    }

    void contactsByNumber(String number) {
        List<Contact> matchingContacts = contactsByName.values()
                .stream()
                .flatMap(Collection::stream)
                .filter(contact -> contact.getNumber().contains(number))
                .sorted(Contact::compareTo)
                .collect(Collectors.toList());

        if (matchingContacts.isEmpty()) {
            System.out.println("NOT FOUND");
        } else {
            matchingContacts.forEach(System.out::println);
        }

    }

}
public class PhoneBookTest {

    public static void main(String[] args) {
        PhoneBook phoneBook = new PhoneBook();
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(":");
            try {
                phoneBook.addContact(parts[0], parts[1]);
            } catch (DuplicateNumberException e) {
                System.out.println(e.getMessage());
            }
        }
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            System.out.println(line);
            String[] parts = line.split(":");
            if (parts[0].equals("NUM")) {
                phoneBook.contactsByNumber(parts[1]);
            } else {
                phoneBook.contactsByName(parts[1]);
            }
        }
    }

}


