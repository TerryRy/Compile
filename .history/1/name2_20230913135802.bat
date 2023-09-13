@echo off
setlocal enabledelayedexpansion

rem 列出要更改的文件名
set "files=testfile1.txt testfile2.txt testfile3.txt testfile4.txt testfile5.txt testfile6.txt"

rem 遍历文件名列表并将扩展名从.txt更改为.c
for %%f in (%files%) do (
    set "file=%%~f"
    set "filename=%%~nf"
    set "newfile=!filename!.c"
    ren "!file!" "!newfile!"
    echo 文件 "!file!" 已成功重命名为 "!newfile!"
)

exit /b 0