package lab7_task1_ChatRoom;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.TreeSet;

class NoSuchRoomException extends Exception {
    NoSuchRoomException(String name) {
        super(String.format("Room %s doesn't exist!", name));
    }
}

class NoSuchUserException extends Exception {
    public NoSuchUserException(String name) {
        super(String.format("User %s doesn't exist!", name));
    }
}

class ChatRoom {
    private final String name;
    private final Set<String> users;

    public ChatRoom(String name) {
        this.name = name;
        this.users = new TreeSet<>();
    }

    void addUser(String username) {
        users.add(username);
    }

    void removeUser(String username) {
        users.remove(username);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append("\n");
        if (!users.isEmpty()) {
            for (String user : users) {
                sb.append(user).append("\n");
            }
        } else {
            sb.append("EMPTY").append("\n");
        }
        return sb.toString();
    }

    boolean hasUser(String username) {
        return users.contains(username);
    }

    int numUsers() {
        return users.size();
    }

    public String getName() {
        return name;
    }
}

class ChatSystem {
    private final Map<String, ChatRoom> map;
    private final Set<String> set;

    public ChatSystem() {
        this.map = new TreeMap<>();
        this.set = new TreeSet<>();
    }

    public void addRoom(String name) {
        map.put(name, new ChatRoom(name));
    }

    public void removeRoom(String name) {
        map.remove(name);
    }

    public ChatRoom getRoom(String name) throws NoSuchRoomException {
        if (!map.containsKey(name)) {
            throw new NoSuchRoomException(name);
        }

        return map.get(name);
    }

    public void register(String name) {
        ChatRoom cr = map.values().stream()
                .min(Comparator.comparing(ChatRoom::numUsers)
                        .thenComparing(ChatRoom::getName))
                .orElse(null);

        set.add(name);

        if (cr != null) {
            cr.addUser(name);
        }
    }

    public void registerAndJoin(String user, String room) {
        if (map.containsKey(room)) {
            map.get(room).addUser(user);
        }

        set.add(user);
    }

    public void joinRoom(String user, String room) throws NoSuchRoomException, NoSuchUserException {
        if (!map.containsKey(room)) {
            throw new NoSuchRoomException(room);
        }

        if (!set.contains(user)) {
            throw new NoSuchUserException(user);
        }

        map.get(room).addUser(user);
    }

    public void leaveRoom(String user, String room) throws NoSuchRoomException, NoSuchUserException {
        if (!map.containsKey(room)) {
            throw new NoSuchRoomException(room);
        }

        if (!set.contains(user)) {
            throw new NoSuchUserException(user);
        }

        map.get(room).removeUser(user);
    }

    public void followFriend(String user, String friend) throws NoSuchUserException {
        if (!set.contains(user)) {
            throw new NoSuchUserException(friend);
        }

        for (ChatRoom cr : map.values()) {
            if (cr.hasUser(friend)) {
                cr.addUser(user);
            }
        }
    }
}

public class ChatSystemTest {
    public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchRoomException {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if (k == 0) {
            ChatRoom cr = new ChatRoom(jin.next());
            int n = jin.nextInt();
            for (int i = 0; i < n; ++i) {
                k = jin.nextInt();
                if (k == 0) cr.addUser(jin.next());
                if (k == 1) cr.removeUser(jin.next());
                if (k == 2) System.out.println(cr.hasUser(jin.next()));
            }
            System.out.println("");
            System.out.println(cr.toString());
            n = jin.nextInt();
            if (n == 0) return;
            ChatRoom cr2 = new ChatRoom(jin.next());
            for (int i = 0; i < n; ++i) {
                k = jin.nextInt();
                if (k == 0) cr2.addUser(jin.next());
                if (k == 1) cr2.removeUser(jin.next());
                if (k == 2) cr2.hasUser(jin.next());
            }
            System.out.println(cr2.toString());
        }
        if (k == 1) {
            ChatSystem cs = new ChatSystem();
            Method mts[] = cs.getClass().getMethods();
            while (true) {
                String cmd = jin.next();
                if (cmd.equals("stop")) break;
                if (cmd.equals("print")) {
                    System.out.println(cs.getRoom(jin.next()) + "\n");
                    continue;
                }
                for (Method m : mts) {
                    if (m.getName().equals(cmd)) {
                        String params[] = new String[m.getParameterTypes().length];
                        for (int i = 0; i < params.length; ++i) params[i] = jin.next();
                        m.invoke(cs, (Object[]) params);
                    }
                }
            }
        }
    }
}