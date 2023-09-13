@echo off
setlocal enabledelayedexpansion

rem 列出要更改的文件名
set "files=textfile1.c textfile2.c textfile3.c textfile4.c textfile5.c textfile6.c"

rem 遍历文件名列表并将扩展名从.c更改为.txt
for %%f in (%files%) do (
    set "file=%%~f"
    set "filename=%%~nf"
    set "newfile=!filename!.txt"
    ren "!file!" "!newfile!"
    echo 文件 "!file!" 已成功重命名为 "!newfile!"
)

exit /b 0