package task24_MoviesList;
import java.util.*;
import java.util.stream.Collectors;

class Movie{
    String title;
    List<Integer> ratings;
    public Movie(String title, List<Integer> ratings) {
        this.title = title;
        this.ratings = ratings;
    }
    int totalRatings(){
        return ratings.stream().mapToInt(i->i).sum();
    }
    double averageRatingPerMovie(){
        return (double) totalRatings() /ratings.size();
    }
    double ratingCoefficient(int maxRatings){
        return averageRatingPerMovie()*ratings.size()/maxRatings;
    }
    @Override
    public String toString() {
        return String.format("%s (%.2f) of %d ratings",title,averageRatingPerMovie(),ratings.size());
    }

    public String getTitle() {
        return title;
    }
}
class MoviesList{
    List<Movie> movies;

    public MoviesList() {
        movies=new ArrayList<>();
    }
    int maxRatings(){
        return movies.stream().mapToInt(i->i.ratings.size()).max().orElse(0);
    }
    public void addMovie(String title, int[] ratings) {
        List<Integer> r=new ArrayList<>();
        for (int rating : ratings) {
            r.add(rating);
        }
        movies.add(new Movie(title,r));
    }

    public List<Movie> top10ByAvgRating() {
        return movies.stream()
                .sorted(Comparator.comparing(Movie::averageRatingPerMovie)
                        .reversed().thenComparing(Movie::getTitle))
                .limit(10)
                .collect(Collectors.toList());
    }

    public List<Movie> top10ByRatingCoeff() {
        Comparator<Movie> comparator=(o1,o2)->{
            int rating=Double.compare(o2.ratingCoefficient(maxRatings()),o1.ratingCoefficient(maxRatings()));
            if(rating==0){
                return o1.getTitle().compareTo(o2.getTitle());
            }
            return rating;
        };
        return movies.stream()
                .sorted(comparator)
                .limit(10)
                .collect(Collectors.toList());
    }
}
public class MoviesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MoviesList moviesList = new MoviesList();
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            int x = scanner.nextInt();
            int[] ratings = new int[x];
            for (int j = 0; j < x; ++j) {
                ratings[j] = scanner.nextInt();
            }
            scanner.nextLine();
            moviesList.addMovie(title, ratings);
        }
        scanner.close();
        List<Movie> movies = moviesList.top10ByAvgRating();
        System.out.println("=== TOP 10 BY AVERAGE RATING ===");
        for (Movie movie : movies) {
            System.out.println(movie);
        }
        movies = moviesList.top10ByRatingCoeff();
        System.out.println("=== TOP 10 BY RATING COEFFICIENT ===");
        for (Movie movie : movies) {
            System.out.println(movie);
        }
    }
}

