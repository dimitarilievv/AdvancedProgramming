package lab2_task1_Contact;

// package zad1;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;
enum Operator{
    VIP,
    ONE,
    TMOBILE
}
abstract class Contact{
    String date;

    public Contact(String date) {
        this.date = date;
    }
    public boolean isNewerThan(Contact c){
        //19201212 19500131
        return this.getDateAsInt() > c.getDateAsInt();
    }
    abstract String getType();
    public int getDateAsInt(){
        String []parts=date.split("-");
        //1920-12-12
        //vo 19200000+1200+12
        return Integer.parseInt(parts[0])*10000+
                Integer.parseInt(parts[1])*100+
                Integer.parseInt(parts[2]);
    }

    @Override
    public String toString() {
        return "Contact{" +
                "date='" + date + '\'' +
                ", type='" + getType() + '\'' +
                '}';
    }
}
class EmailContact extends Contact{
    private String email;

    public EmailContact(String date, String email) {
        super(date);
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    @Override
    String getType() {
        return "Email";
    }
    @Override
    public String toString() {
        return "\""+email+"\"";
    }
}
class PhoneContact extends Contact{
    private String phone;

    public PhoneContact(String date, String phone) {
        super(date);
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }
    Operator getOperator(){
        if(phone.startsWith("070")||phone.startsWith("071")||phone.startsWith("072")){
            return Operator.TMOBILE;
        }else if(phone.startsWith("075")||phone.startsWith("076")){
            return Operator.ONE;
        }else if(phone.startsWith("077")||phone.startsWith("078")){
            return Operator.VIP;
        }
        return null;
    }

    @Override
    String getType() {
        return "Phone";
    }

    @Override
    public String toString() {
        return "\""+phone+"\"";
    }
}
class Student{
    private Contact[] contacts;
    private String firstName;
    private String lastName;
    private String city;
    private int age;
    private long index;
    Contact []emailContacts;
    Contact []phoneContacts;
    int numberOfEmailContacts;
    int numberOfPhoneContacts;

    public Student(String firstName, String lastName, String city, int age, long index) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        this.age = age;
        this.index = index;
        contacts=new Contact[0];
        emailContacts=new Contact[0];
        phoneContacts=new Contact[0];
        numberOfPhoneContacts=0;
        numberOfEmailContacts=0;
    }
    void addEmailContact(String date, String email) {
        Contact[] newArr = new Contact[contacts.length + 1];
        System.arraycopy(contacts, 0, newArr, 0, contacts.length);
        newArr[contacts.length] = new EmailContact(date, email);
        contacts = newArr;

        Contact[] newEmailArr = new Contact[emailContacts.length + 1];
        System.arraycopy(emailContacts, 0, newEmailArr, 0, emailContacts.length);
        newEmailArr[numberOfEmailContacts] = new EmailContact(date, email);
        emailContacts = newEmailArr;
        numberOfEmailContacts++;
    }

    void addPhoneContact(String date, String phone) {
        Contact[] newArr = new Contact[contacts.length + 1];
        System.arraycopy(contacts, 0, newArr, 0, contacts.length);
        newArr[contacts.length] = new PhoneContact(date, phone);
        contacts = newArr;

        Contact[] newPhoneArr = new Contact[phoneContacts.length + 1];
        System.arraycopy(phoneContacts, 0, newPhoneArr, 0, phoneContacts.length);
        newPhoneArr[numberOfPhoneContacts] = new PhoneContact(date, phone);
        phoneContacts = newPhoneArr;
        numberOfPhoneContacts++;
    }
    public Contact[] getEmailContacts(){
        return emailContacts;
    }
    public Contact[] getPhoneContacts(){
        return phoneContacts;
    }
    public int getNumber(){
        return getNumberOfEmailContacts()+getNumberOfPhoneContacts();
    }
    public String getCity() {
        return city;
    }
    public String getFullName(){
        return firstName+" "+lastName;
    }

    public long getIndex() {
        return index;
    }
    Contact getLatestContact(){
        int latest=0;
        for (int i = 0; i < contacts.length; i++) {
            if(contacts[i].isNewerThan(contacts[latest])){
                latest=i;
            }
        }
        return contacts[latest];
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getAge() {
        return age;
    }

    public int getNumberOfEmailContacts() {
        return numberOfEmailContacts;
    }

    public int getNumberOfPhoneContacts() {
        return numberOfPhoneContacts;
    }

    @Override
    public String toString() {
        return "{\"ime\":\"" + getFirstName() + "\", \"prezime\":\"" + getLastName() +
                "\", \"vozrast\":" + getAge() + ", " + "\"grad\":\"" + getCity() + "\", " +
                "\"indeks\":" + getIndex() + ", " + "\"telefonskiKontakti\":" + Arrays.toString(getPhoneContacts()) + ", "
                + "\"emailKontakti\":" + Arrays.toString(getEmailContacts())+"}";
    }
}
class Faculty{
    private String name;
    private Student[] students;

    public Faculty(String name, Student[] students) {
        this.name = name;
        this.students = students;
    }
    public int countStudentsFromCity(String cityName){
        return (int) Arrays.stream(students).filter(student->student.getCity().equals(cityName)).count();
    }
    public double getAverageNumberOfContacts(){
        return (double) Arrays.stream(students)
                .mapToInt(i -> i.getEmailContacts().length + i.getPhoneContacts().length)
                .sum() /students.length;
    }
    Student getStudentWithMostContacts(){
        return Arrays.stream(students).max(Comparator.comparing(Student::getNumber).thenComparing(Student::getIndex)).get();
    }

    @Override
    public String toString() {
        return "{\"fakultet\":\"" + name + "\", " +
                "\"studenti\":" + Arrays.toString(students) + "}";
    }

    public Student getStudent(long index) {
        for (int i = 0; i < students.length; i++) {
            if(students[i].getIndex()==index){
                return students[i];
            }
        }
        return null;
    }
}
public class ContactsTester {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int tests = scanner.nextInt();
        Faculty faculty = null;

        int rvalue = 0;
        long rindex = -1;

        DecimalFormat df = new DecimalFormat("0.00");

        for (int t = 0; t < tests; t++) {

            rvalue++;
            String operation = scanner.next();

            switch (operation) {
                case "CREATE_FACULTY": {
                    String name = scanner.nextLine().trim();
                    int N = scanner.nextInt();

                    Student[] students = new Student[N];

                    for (int i = 0; i < N; i++) {
                        rvalue++;

                        String firstName = scanner.next();
                        String lastName = scanner.next();
                        String city = scanner.next();
                        int age = scanner.nextInt();
                        long index = scanner.nextLong();

                        if ((rindex == -1) || (rvalue % 13 == 0))
                            rindex = index;

                        Student student = new Student(firstName, lastName, city,
                                age, index);
                        students[i] = student;
                    }

                    faculty = new Faculty(name, students);
                    break;
                }

                case "ADD_EMAIL_CONTACT": {
                    long index = scanner.nextInt();
                    String date = scanner.next();
                    String email = scanner.next();

                    rvalue++;

                    if ((rindex == -1) || (rvalue % 3 == 0))
                        rindex = index;

                    faculty.getStudent(index).addEmailContact(date, email);
                    break;
                }

                case "ADD_PHONE_CONTACT": {
                    long index = scanner.nextInt();
                    String date = scanner.next();
                    String phone = scanner.next();

                    rvalue++;

                    if ((rindex == -1) || (rvalue % 3 == 0))
                        rindex = index;

                    faculty.getStudent(index).addPhoneContact(date, phone);
                    break;
                }

                case "CHECK_SIMPLE": {
                    System.out.println("Average number of contacts: "
                            + df.format(faculty.getAverageNumberOfContacts()));

                    rvalue++;

                    String city = faculty.getStudent(rindex).getCity();
                    System.out.println("Number of students from " + city + ": "
                            + faculty.countStudentsFromCity(city));

                    break;
                }

                case "CHECK_DATES": {

                    rvalue++;

                    System.out.print("Latest contact: ");
                    Contact latestContact = faculty.getStudent(rindex)
                            .getLatestContact();
                    if (latestContact.getType().equals("Email"))
                        System.out.println(((EmailContact) latestContact)
                                .getEmail());
                    if (latestContact.getType().equals("Phone"))
                        System.out.println(((PhoneContact) latestContact)
                                .getPhone()
                                + " ("
                                + ((PhoneContact) latestContact).getOperator()
                                .toString() + ")");

                    if (faculty.getStudent(rindex).getEmailContacts().length > 0
                            && faculty.getStudent(rindex).getPhoneContacts().length > 0) {
                        System.out.print("Number of email and phone contacts: ");
                        System.out
                                .println(faculty.getStudent(rindex)
                                        .getEmailContacts().length
                                        + " "
                                        + faculty.getStudent(rindex)
                                        .getPhoneContacts().length);

                        System.out.print("Comparing dates: ");
                        int posEmail = rvalue
                                % faculty.getStudent(rindex).getEmailContacts().length;
                        int posPhone = rvalue
                                % faculty.getStudent(rindex).getPhoneContacts().length;

                        System.out.println(faculty.getStudent(rindex)
                                .getEmailContacts()[posEmail].isNewerThan(faculty
                                .getStudent(rindex).getPhoneContacts()[posPhone]));
                    }

                    break;
                }

                case "PRINT_FACULTY_METHODS": {
                    System.out.println("Faculty: " + faculty.toString());
                    System.out.println("Student with most contacts: "
                            + faculty.getStudentWithMostContacts().toString());
                    break;
                }

            }

        }

        scanner.close();
    }
}
