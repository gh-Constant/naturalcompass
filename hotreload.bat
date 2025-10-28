@echo off
echo Building and deploying NaturalCompass plugin...
mvn clean package -Pdev
if %errorlevel% neq 0 (
    echo Build failed!
    pause
    exit /b 1
)
echo Build successful. Reloading plugin on server...
echo plugman reload naturalcompass > "C:\Users\const\IdeaProjects\servers\paper1.21.10\reload_command.txt"
echo Reload command sent. Plugin should be reloaded.