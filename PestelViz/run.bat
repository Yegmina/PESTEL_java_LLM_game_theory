@echo off
echo Running JavaFX application...

set JAVAFX_PATH="C:\javafx\javafx-sdk-25\lib"
:: The classpath needs to include the current directory (.) for the compiled 'app' package 
:: and the parent directory (../) for the 'simu' package.
set CLASSPATH=.;../

java --module-path %JAVAFX_PATH% --add-modules javafx.controls,javafx.fxml -cp %CLASSPATH% app.Main

echo Application closed.
