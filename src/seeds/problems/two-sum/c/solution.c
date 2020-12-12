int* two_sum(int* nums, int nums_size, int target, int* return_size)
{
    *return_size = 0;
    return 0;
}

// For demonstration purposes, I've prepared a solution for you:
//#include <stdlib.h>
//
//#define SIZE 50000
//
//int hash(int key) {
//    int r = key % SIZE;
//    return r < 0 ? r + SIZE : r;
//}
//
//void insert(int *keys, int *values, int key, int value) {
//    int index = hash(key);
//    while (values[index]) {
//        index = (index + 1) % SIZE;
//    }
//    keys[index] = key;
//    values[index] = value;
//}
//
//int search(int *keys, int *values, int key) {
//    int index = hash(key);
//    while (values[index]) {
//        if (keys[index] == key) {
//            return values[index];
//        }
//        index = (index + 1) % SIZE;
//    }
//    return 0;
//}
//
//int* two_sum(int* nums, int nums_size, int target, int* return_size) {
//    *return_size = 2;
//    int keys[SIZE];
//    int values[SIZE] = {0};
//    for (int i = 0; i < nums_size; i++) {
//        int complements = target - nums[i];
//        int value = search(keys, values, complements);
//        if (value) {
//            int *indices = (int *) malloc(sizeof(int) * 2);
//            indices[0] = value - 1;
//            indices[1] = i;
//            return indices;
//        }
//        insert(keys, values, nums[i], i + 1);
//    }
//    return 0;
//}