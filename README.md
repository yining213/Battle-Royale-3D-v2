# Battle-Royale-3D-v2
### Demo Video: https://youtu.be/K_y6Qn0E9So

### Add the required files by following steps
1. Open the `src` folder and locate `JFX3DModelImporters_EA_2014-02-09`.  
2. Extract the `jimObjModelImporterJFX.jar` file.  
3. Find the folder where JavaFX is installed on your computer (e.g., `javafx-sdk-23.0.1`).  
    -  Download JavaFX [here](https://gluonhq.com/products/javafx/)
5. Place the extracted folder into the `javafx-sdk-17.0.2/src` directory.  
6. Copy the `jimObjModelImporterJFX.jar` file (the unextracted `.jar` file) into the `javafx-sdk-17.0.2/lib` directory.


### Command
first
```
javac --module-path $PATH_TO_FX --add-modules=javafx.controls,javafx.fxml,jimObjModelImporterJFX,javafx.media *.java
```

-  Reminder: add environmnet variable `PATH_TO_FX` (e.g. `export PATH_TO_FX=path/to/javafx-sdk-23.0.1/lib`)

then,  
```
java --module-path $PATH_TO_FX --add-modules=javafx.controls,javafx.fxml,jimObjModelImporterJFX,javafx.media Camera3D
```
