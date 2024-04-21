package task37_Post;

import java.util.*;

class Comment{
    String author;
    String id;
    String content;
    List<Comment> comments;
    int likes;

    public Comment(String author, String id, String content) {
        this.author = author;
        this.id = id;
        this.content = content;
        comments=new ArrayList<>();
        likes=0;
    }

    public void likeComment() {
        likes++;
    }

    public Comment findComment(String targetId) {
        if(id.equals(targetId))
            return this;
        for (Comment comment : comments) {
            Comment found=comment.findComment(targetId);
            if(found!=null){
                return found;
            }
        }
        return null;
    }

    public void addComment(Comment comment) {
        comments.add(comment);
    }
    public int totalLikes(){
        return likes+comments.stream().mapToInt(i->i.likes).sum();
    }


    public String toString(int level) {
        Comparator<Comment> comparator=Comparator.comparing(Comment::totalLikes).reversed();
        StringBuilder sb=new StringBuilder();
        String tab="    "+"    ".repeat(level);
        sb.append(tab).append("Comment: ").append(content).append("\n");
        sb.append(tab).append("Written by: ").append(author).append("\n");
        sb.append(tab).append("Likes: ").append(likes).append("\n");
        comments.stream().sorted(comparator)
                .forEach(x->sb.append(x.toString(level+1)));
        return sb.toString();
    }
}
class Post{
    String username;
    String postContent;
    List<Comment> comments;
    public Post(String username, String postContent) {
        this.username = username;
        this.postContent = postContent;
        comments=new ArrayList<>();
    }

    public void addComment(String author, String id, String content, String replyToId) {
        Comment comment=new Comment(author, id, content);
        if(replyToId==null){
            comments.add(comment);
            return;
        }
        Comment target=findComment(replyToId);
        if(target!=null){
            target.addComment(comment);
        }
    }

    public void likeComment(String commentId) {
        Comment comment=findComment(commentId);
        if(comment!=null){
            comment.likeComment();
        }
    }
    private Comment findComment(String commentId){
        for (Comment comment : comments) {
            Comment found=comment.findComment(commentId);
            if(found!=null){
                return found;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        Comparator<Comment> comparator=Comparator.comparing(Comment::totalLikes).reversed();
        StringBuilder sb=new StringBuilder();
        sb.append("Post: ").append(postContent).append("\n");
        sb.append("Written by: ").append(username).append("\n");
        sb.append("Comments:\n");
        comments.stream().sorted(comparator)
                .forEach(x->sb.append(x.toString(1)));
        return sb.toString();
    }
}
public class PostTester {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String postAuthor = sc.nextLine();
        String postContent = sc.nextLine();

        Post p = new Post(postAuthor, postContent);

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split(";");
            String testCase = parts[0];

            if (testCase.equals("addComment")) {
                String author = parts[1];
                String id = parts[2];
                String content = parts[3];
                String replyToId = null;
                if (parts.length == 5) {
                    replyToId = parts[4];
                }
                p.addComment(author, id, content, replyToId);
            } else if (testCase.equals("likes")) { //likes;1;2;3;4;1;1;1;1;1 example
                for (int i = 1; i < parts.length; i++) {
                    p.likeComment(parts[i]);
                }
            } else {
                System.out.println(p);
            }

        }
    }
}

