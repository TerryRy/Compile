#include<stdio.h>
#include<stdlib.h>
#include<string.h>
int getint() {
    int n = 0;
    scanf("%d", &n);
    return n;
}





// Decl存在多个
// 多个Decl
const int const_int_1 = 1;
const int const_int_2 = 2;
// Decl包含VarDecl
int int_1;
// 多种ConstDecl
const int const_int_3 = 3;
const int const_int_4 = 4, const_int_5 = 5;
const int const_int_6 = 6, const_int_7 = 7, const_int_8 = 8;
// 基本类型
int BType = 0;
// 多种常数定义、C级不包含数组
const int const_int_9 = 9;
// 常量初值
const int const_int_10 = 10;
// 变量声明
int int_2;
int int_3, int_4;
int int_5, int_6, int_7;
// 变量定义
int int_9;
int int_10 = 10;
// 变量初值
int int_11 = 11;


// 函数存在多个
void funcDef1() {
    int n = 0;
}
void funcDef2() {
    int n = 0;
}
// 常数定义
const int constList[3] = {0, 0, 0};
/*
使用main后的语句块覆盖这里
*/

int main() {
    printf("21371518\n");
    int d = 0;
    printf("");
    printf("a");
    printf("\n");
    printf("aa\n");
    printf(" !()*+,-./\n");
    printf("0123456789\n");
    printf(":;<=>?@\n");
    printf("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz\n");
    printf("[]^_`{|}~");
    return 0;
}