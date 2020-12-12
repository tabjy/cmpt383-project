import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        int[] nums = Arrays.stream(args[1].split(",")).map(String::trim).map(Integer::parseInt).mapToInt(Integer::intValue).toArray();
        int target = Integer.parseInt(args[2]);

        int[] answer = (new Solution()).twoSum(nums, target);

        String out = Arrays.toString(answer);
        System.out.println(out.substring(1, out.length() - 1));
    }
}
