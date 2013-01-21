@echo off
set MCP_LOC="C:\Users\James\Documents\MCP"
set EB_LOC=%CD%
mklink /D "%MCP_LOC%\src\minecraft\eb" "%EB_LOC%\common\eb"
mklink /D "%MCP_LOC%\eclipse\Minecraft\bin\eb\gui" "%EB_LOC%\resources\eb\gui"