package task8_ArchieveStore;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;
class NonExistingItemException extends Exception{
    public NonExistingItemException(String message) {
        super(message);
    }
}
class InvalidArchiveOpenException extends Exception{
    public InvalidArchiveOpenException(String message) {
        super(message);
    }
}
abstract class Archive{
    protected int ID;
    protected LocalDate dateArchived;
    public Archive() {}
    public int getID() {
        return ID;
    }
    public LocalDate getDateArchived() {
        return dateArchived;
    }
    public void archive(LocalDate date){
        this.dateArchived=date;
    }
    abstract LocalDate open(LocalDate date) throws  InvalidArchiveOpenException;
}
class LockedArchive extends Archive{
    private LocalDate dateToOpen;

    public LockedArchive(int ID,LocalDate dateToOpen) {
        this.ID=ID;
        this.dateToOpen = dateToOpen;
    }

    @Override
    public LocalDate open(LocalDate date) throws InvalidArchiveOpenException {
        if(date.isBefore(dateToOpen)){
            throw new InvalidArchiveOpenException(String.format("Item %d cannot be opened before %s",ID,dateToOpen));
        }
        return date;
    }
}
class SpecialArchive extends Archive{
    private int maxOpen;
    private int counter;

    public SpecialArchive(int ID,int maxOpen) {
        this.ID=ID;
        this.maxOpen = maxOpen;
        counter=0;
    }

    @Override
    public LocalDate open(LocalDate date) throws InvalidArchiveOpenException {
        if(counter>=maxOpen){
            throw new InvalidArchiveOpenException(String.format("Item %d cannot be opened more than %d times",ID,maxOpen));
        }
        counter++;
        return date;
    }
}
class ArchiveStore{
    private ArrayList<Archive> items;
    private StringBuilder log;
    public ArchiveStore() {
        items=new ArrayList<Archive>();
        log=new StringBuilder();
    }
    public String getLog(){
        return log.toString();
    }
    public void archiveItem(Archive item, LocalDate date){
        item.archive(date);
        items.add(item);
        log.append(String.format("Item %d archived at %s\n",item.getID(),date));

    }

    public void openItem(int id, LocalDate date) throws NonExistingItemException {
        for(Archive item:items){
            if(item.getID()==id){
                try{
                    item.open(date);
                    log.append(String.format("Item %d opened at %s\n",item.getID(),date));
                }catch(InvalidArchiveOpenException e){
                    log.append(e.getMessage());
                    log.append("\n");
                }
                return;
            }
        }
        throw new NonExistingItemException(String.format("Item with id %d doesn't exist",id));
    }
}
public class ArchiveStoreTest {
    public static void main(String[] args) {
        ArchiveStore store = new ArchiveStore();
        LocalDate date = LocalDate.of(2013, 10, 7);
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        int n = scanner.nextInt();
        scanner.nextLine();
        scanner.nextLine();
        int i;
        for (i = 0; i < n; ++i) {
            int id = scanner.nextInt();
            long days = scanner.nextLong();

            LocalDate dateToOpen = date.atStartOfDay().plusSeconds(days * 24 * 60 * 60).toLocalDate();
            LockedArchive lockedArchive = new LockedArchive(id, dateToOpen);
            store.archiveItem(lockedArchive, date);
        }
        scanner.nextLine();
        scanner.nextLine();
        n = scanner.nextInt();
        scanner.nextLine();
        scanner.nextLine();
        for (i = 0; i < n; ++i) {
            int id = scanner.nextInt();
            int maxOpen = scanner.nextInt();
            SpecialArchive specialArchive = new SpecialArchive(id, maxOpen);
            store.archiveItem(specialArchive, date);
        }
        scanner.nextLine();
        scanner.nextLine();
        while(scanner.hasNext()) {
            int open = scanner.nextInt();
            try {
                store.openItem(open, date);
            } catch(NonExistingItemException e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println(store.getLog());
    }
}