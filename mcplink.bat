@echo off
cd /D "E:\MCP\src\"
mklink /D "common\eb" "E:\Eclipse\EasyBuilding\eb_common\eb"
mklink /D "minecraft\eb" "E:\Eclipse\EasyBuilding\eb_client\eb"
cd "E:\MCP\eclipse\Minecraft\bin"
mklink /D eb\gui "E:\Eclipse\EasyBuilding\resources\eb\gui"