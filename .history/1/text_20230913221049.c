#include<stdio.h>
#include<stdlib.h>
#include<string.h>
int getint() {
    int n = 0;
    scanf("%d", &n);
    return n;
}









// B级
//常数定义
const int const_int_1 = 0;
const int constList[3] = {0, 0, 0};
// /*不确定的*/const int constList2[3] = {0, 0};
/*应该用不到的*/const int constList3[1+2] = {0, 0, 0}; const int constList3_2[1+1][1+1] = {{0, 0}, {0, 0}};
const int constList4[2][2] = {{0, 0}, {0, 0}};
// /*不确定但应该得有的*/const int constList2_2[0] = {}; const int constList2_3[0][0] = {};

//变量定义
/*放在函数中统一处理定义和初值*/
void functionList() {
    int intList[3] = {0, 0, 0};
    int intList2[2][2] = {{0, 0}, {0, const_int_1}};
    /*应该用不到的*/int intList3[1+2] = {0, 0, 0}; int intList3_2[1+1][1+1] = {{0, 0}, {0, 0}};
    int intList4[3];
    int intList5[2][2];
}
// 函数形参
void funDef1(int f_int_list[]) {}
void funDef2(int f_int_list[][2]) {}
// 左值表达式和函数实参放在如下函数体中覆盖
void funDef3() {
    int intList[3] = {0, 0, 0};
    int intList2[2][2] = {{0, 0}, {0, const_int_1}};
    // 左值表达式和函数实参
    funcDef1(intList);
    funcDef2(intList2);
    funcDef1(intList2[0]);
}


int main() {
    printf("21371518\n");
    printf("21371518\n");
    printf("21371518\n");
    printf("21371518\n");
    printf("21371518\n");
    printf("21371518\n");
    printf("21371518\n");
    printf("21371518\n");
    printf("21371518\n");
    printf("21371518");

    return 0;
}