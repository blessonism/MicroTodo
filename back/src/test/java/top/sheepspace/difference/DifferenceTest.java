package top.sheepspace.difference;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Description:
 *
 * @Project: MicroTodo
 * @Package: top.sheepspace
 * @Author: Yang Xiong Email:2829727259@qq.com
 * @Version: jdk1.8.0_131
 * @Time 2024-04-25 - 17:42
 */
@SpringBootTest
public class DifferenceTest {
    @Test
    public void testDiff() {
        int[] nums = {8, 2, 6, 3, 1};
        Difference diff = new Difference(nums);

        // 假设我们要将区间[1, 3]的元素都加上2
        diff.increment(1, 3, 2);

        // 获取修改后的数组
        int[] result = diff.getResult();
        for (int num : result) {
            System.out.print(num + " ");
        }
    }
}

class Difference {
    private int[] diff; // 差分数组

    public Difference(int[] nums) {
        assert nums.length > 0;
        diff = new int[nums.length];
        // 构造差分数组
        diff[0] = nums[0];
        for (int i = 1; i < nums.length; i++) {
            diff[i] = nums[i] - nums[i - 1];
        }
    }

    // 给闭区间[i, j]增加val（可以是负数）
    public void increment(int i, int j, int val) {
        diff[i] += val;
        if (j + 1 < diff.length) {
            diff[j + 1] -= val;
        }
    }

    // 返回结果数组
    public int[] getResult() {
        int[] result = new int[diff.length];
        result[0] = diff[0];
        for (int i = 1; i < diff.length; i++) {
            result[i] = result[i - 1] + diff[i];
        }
        return result;
    }
}