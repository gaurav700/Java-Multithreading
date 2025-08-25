import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

record  Person(String firstName, String lastName, int age){
    private final static String[] firsts = {"able", "bob", "charlie", "donna", "eve", "fred"};
    private final static String[] lasts = {"norton", "ohara", "petersen", "quincy", "richardson", "smith"};

    private final static Random random = new Random();
    public Person(){
        this(firsts[random.nextInt(lasts.length)], lasts[random.nextInt(lasts.length)], random.nextInt(18, 100));
    }

    @Override
    public String toString() {
        return "%s %s (%d)".formatted(lastName, firstName, age);
    }
}
public class Main {
    public static void main(String[] args) {
        var persons = Stream.generate(Person::new)
                .limit(10)
                .sorted(Comparator.comparing(Person::lastName))
                .toArray();
        for (var person : persons) {
            System.out.println(person);
        }

        System.out.println("---------------------------------------------");

        Arrays.stream(persons)
                .limit(10)
                .parallel()
                .forEachOrdered(System.out::println);

        System.out.println("---------------------------------------------");

        int sum = IntStream.range(1, 101)
                .parallel()
                .reduce(0, Integer::sum);
        System.out.println("The sum of the number is: " + sum);

        System.out.println("---------------------------------------------");

        String str = """
                Humpty Dumpty sat on a wall.
                Humpty Dumpty had a great fall.
                All the king's horses and all the king's men
                couldn't put humpty together again.
                """;
        var words = new Scanner(str).tokens().toList();
        words.forEach(System.out::println);

        System.out.println("---------------------------------------------");

        var backTogetherStr = words.stream()
                .reduce(new StringJoiner(" "), StringJoiner::add, StringJoiner::merge);
        System.out.println(backTogetherStr);

        System.out.println("---------------------------------------------");

        String backTogetherStr2 = words.parallelStream().collect(Collectors.joining(" "));
        System.out.println(backTogetherStr2);

        System.out.println("---------------------------------------------");



    }
}