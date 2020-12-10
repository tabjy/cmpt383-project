#include <stdio.h>

extern char* hello_world();

int main() {
    printf("%s\n", hello_world());
}