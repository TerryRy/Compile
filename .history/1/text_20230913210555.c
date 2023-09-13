#include<stdio.h>
#include<stdlib.h>
#include<string.h>
int getint() {
    int n = 0;
    scanf("%d", &n);
    return n;
}


int global_var = 0;
int func() {
    global_var = global_var + 1;
return 1;
}

int main() {
    printf("21371518\n");
    if (0 && func()){
        ;
    }
    printf("%d",global_var); // 输出 0
    if (1 || func()) {
        ;
    }
    printf("%d",global_var); // 输出 0
    int d = 0;
    //printf("");
    //printf("a%d%d", 0, 0);
    printf("\n");
    printf("aa\n");
    printf(" !()*+,-./\n");
    printf("0123456789\n");
    printf(":;<=>?@\n");
    printf("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz\n");
    printf("[]^_`{|}~%d", 0);
    return 0;
}