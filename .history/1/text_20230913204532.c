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
// Ident
int a;
int A;
int _;

// 函数存在多个
int funDef_for_int() {
    return 0;
}
int funcDef_for_int_with_params() {
    return 0;
}
void funcDef1() {
    int n = 0;
}
void funcDef2() {
    int n = 0;
}
// 函数定义
void funcDef3() {}
void funcDef4(int int_12) {}
// 函数类型
void funcDef5() {}
int funcDef6() {
    int n = 0;
    return n;
}
// 函数形参表
void funcDef7() {}
void funcDef8(int int_13) {}
void funcDef9(int int_14, int int_15) {}
/*怕后面实参表说不够多来一个*/
void funcDef9_2(int int_14, int int_15, int int_15_2) {}
// 函数形参
/*C级不用管*/
// 语句块
/*花括号重复0次*/
void funcDef10() {}
/*花括号重复1次*/
void funcDef11() {
    {}
}
/*花括号重复多次*/
void funcDef12() {
    {}
    {}
}
//语句块项
void funcDef13() {
    /*Decl*/
    int int_16;
    /*Stmt*/
    {}
}
// 语句
/*Stmt有Exp*/
void funcDef14() {
    /*借用Decl生产左值*/
    int int_17;
    /*Stmt*/
    /*Lval=Exp*/
    int_17 = 17;
    /*Exp*/
    0*0;
    /*Block*/
    {}
    /*if*/
    /*无else*/
    if(0) {}
    /*有else*/
    if(0) {} else {}
    /*for*/
    /*各种缺省*/
    /*先放一个满状态示例*/
    for (int_17 = 0; 0; int_17 = 0) {break;}
    for (int_17 = 0; 0;) {}
    for (int_17 = 0;; int_17 = 0) {continue;}
    for (int_17 = 0;;) {}
    for (; 0; int_17 = 0) {}
    for (; 0;) {}
    for (;; int_17 = 0) {}
    for (;;) {}
    /*break和continue*/
    /*语义约束中似乎未提及该部分应当在循环和条件表达式中存在，保险起见写入上述for内*/
    /*return*/
    /*下面会定义返回int的函数来覆盖有Exp的情况*/
    return;
    /*LVal=getint*/
    int_17 = getint();
    /*printf*/
    /*printf涉及计数和顺序有点麻烦，统一放在main中管理*/
    /*ForStmt*/
    /*位置不好判断，for中已有*/
    /*Exp*/
    /*funcDef15的return中已存在*/
    /*Cont*/
    /*for中已存在*/
    /*LVal*/
    int_17 = 0;
    /*PrimaryExp*/
    int_17 = (0);
    int_17 = int_17;
    int_17 = 0;
    /*Number*/
    int_17 = 0;
    /*UnaryExp*/
    int_17 = 0;
    int_17 = funDef_for_int();
    int_17 = funcDef_for_int_with_params(0);
    //多维数组问题
    int_17 = +0;
    /*UnaryOp*/
    int_17 = +0;
    int_17 = -0;
    if(!0) {}
    /*FuncRParams*/
    funcDef8(0);
    funcDef9(0, 0);
    funcDef9_2(0, 0, 0);
    /*B级数组*/
    /*MulExp*/
    0;
    0*0;
    1/1;
    1%2;
    /*AddExp*/
    0;
    0+0;
    0-0;
    /*RelExp*/
    if (0){}
    if (0 < 0){}
    if (0 > 0){}
    if (0 <= 0){}
    if (0 >= 0){}
    /*EqExp*/
    if (0){}
    if (0 == 0){}
    if (0 != 0){}
    /*LAndExp*/
    if (0){}
    if (0 && 0){}
    /*LOrExp*/
    if (0){}
    if (0 || 0){}
    /*ConstExp*/
    const int int_18 = 0;
}
int funcDef15() {
    return 0;
}

// B级
//常数定义
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


/*
使用main后的语句块覆盖这里
*/


int main() {
    printf("21371518\n");
    printf("21371518\n");
    printf("21371518\n");
    printf("21371518\n");
    printf("21371518\n");
    printf("21371518\n");
    printf("21371518\n");
    int num1 = 21371518;
    printf("%d\n", num1);
    printf("%d\n", 21371518);
    printf("%d", getint());
    // 突发奇想的一些补充
    ;
    int d = 0;
    +-+d;
    int int_1 = 0;
    int_1 = 0;
    int_1 = 1;
    int_1 = 2;
    int_1 = 3;
    int_1 = 4;
    int_1 = 5;
    int_1 = 6;
    int_1 = 7+const_int_1;
    int_1 = 8+int_1;
    int_1 = 9+1;
    int A;
    int _A;
    int abcdefghijk;
    int lmnopqrstvuwxyz;
    int ABCDEFGHIJK;
    int LMNOPQRSTUVWXYZ;
    if (int_1 || 0 && int_1 + 1){}

    return 0;
}