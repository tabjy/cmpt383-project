/**
 * @param {number[]} nums
 * @param {number} target
 * @return {number[]}
 */
module.exports = function twoSum (nums, target) {
    // TODO: work your solution here
    return [];
}

// For demonstration purposes, I've prepared a solution for you:
// module.exports = function(nums, target) {
//     const map = new Map();
//     for (let i = 0; i < nums.length; i++) {
//         let complement = target - nums[i];
//         if (map.has(complement)) {
//             return [map.get(complement), i]
//         }
//         map.set(nums[i], i);
//     }
// }
