import sys

from solution import Solution

nums = map(int, sys.argv[2].split(','))

answer = Solution().splitArraySameAverage(nums)
print(str(answer).lower())
