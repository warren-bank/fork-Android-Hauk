@echo off

set res_dir=%~dp0..\..\..\android-studio-project\Hauk\src\main\res

set src_dir="%res_dir%\drawable"
set dst_dir="%res_dir%\drawable-v24"

if exist %dst_dir% exit /B 0

move  %src_dir% %dst_dir%
mkdir %src_dir%
