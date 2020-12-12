#include <stdlib.h>
#include <string.h>
#include <stdio.h>

extern int* two_sum(int* nums, int nums_size, int target, int* return_size);

int cmpfunc (const void * a, const void * b) {
   return ( *(int*)a - *(int*)b );
}

int main(int argc, char** argvs) {
    int nums_size = atoi(argvs[1]);
    int* nums = malloc(sizeof(int) * nums_size);

    char *ptr = strtok(argvs[2], ", ");
    int i = 0;
    while(ptr != NULL) {
        nums[i++] = atoi(ptr);
    	ptr = strtok(NULL, ", ");
    }

    int target = atoi(argvs[3]);

    int return_size = 0;
    int* answer = two_sum(nums, nums_size, target, &return_size);

    qsort(answer, return_size, sizeof(int), cmpfunc);
    for (i = 0; i < return_size; i++) {
        printf("%d", answer[i]);

        if (i != return_size - 1) {
            printf(", ");
        }
    }

    return 0;
}