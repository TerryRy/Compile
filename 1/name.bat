@echo off.txt
setlocal enabledelayedexpansion

rem 列出要更改的文件名
set "files=testfile1.c testfile2.c testfile3.c testfile4.c testfile5.c testfile6.c"

rem 遍历文件名列表并将扩展名从.c更改为.txt
for %%f in (%files%) do (
    set "file=%%~f"
    set "filename=%%~nf"
    set "newfile=!filename!.txt"
    ren "!file!" "!newfile!"
    echo 文件 "!file!" 已成功重命名为 "!newfile!"
)

exit /b 0