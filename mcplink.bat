@echo off
set MCP_LOC="E:\MCP"
set EB_LOC=%CD%
cd /D "%MCP_LOC%\src\"
mklink /D "common\eb" "%EB_LOC%\eb_common\eb"
mklink /D "minecraft\eb" "%EB_LOC%\eb_client\eb"
cd "%MCP_LOC%\eclipse\Minecraft\bin"
mklink /D eb\gui "%EB_LOC%\resources\eb\gui"