@echo off
echo Compiling JavaFX application...

set JAVAFX_PATH="C:\javafx\javafx-sdk-25\lib"
:: The classpath needs to include the parent directory to find the simulation code
set CLASSPATH=.;../ 

:: Removed the empty src/app/view folder from the command
javac --module-path %JAVAFX_PATH% --add-modules javafx.controls,javafx.fxml -cp %CLASSPATH% -d . src/app/model/*.java src/app/controller/*.java src/app/Main.java

echo Compilation finished.
