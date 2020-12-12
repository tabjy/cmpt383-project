import sys

from solution import Solution

nums = map(int, sys.argv[2].split(','))
target = int(sys.argv[3])

answer = Solution().twoSum(nums, target)
answer.sort()
print(", ".join(map(str, answer)))