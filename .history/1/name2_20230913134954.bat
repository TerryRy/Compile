@echo off
setlocal enabledelayedexpansion

rem 列出要更改的文件名
set "files=textfile1.txt textfile2.txt textfile3.txt textfile4.txt textfile5.txt textfile6.txt"

rem 遍历文件名列表并将扩展名从.txt更改为.c
for %%f in (%files%) do (
    set "file=%%~f"
    set "filename=%%~nf"
    set "newfile=!filename!.c"
    ren "!file!" "!newfile!"
    echo 文件 "!file!" 已成功重命名为 "!newfile!"
)

exit /b 0