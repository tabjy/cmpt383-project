import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        int[] nums = Arrays.stream(args[1].split(",")).map(String::trim).map(Integer::parseInt).mapToInt(Integer::intValue).toArray();

        boolean answer = (new Solution()).splitArraySameAverage(nums);
        System.out.println(String.valueOf(answer).toLowerCase());
    }
}
