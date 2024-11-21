# Battle-Royale-3D-v2
### Demo Video: https://youtu.be/K_y6Qn0E9So

### Add the required files by following steps
1. Open the `src` folder and locate `JFX3DModelImporters_EA_2014-02-09`.  
2. Extract the `jimObjModelImporterJFX.jar` file.  
3. Find the folder where JavaFX is installed on your computer (e.g., `javafx-sdk-17.0.2`).  
4. Place the extracted folder into the `javafx-sdk-17.0.2/src` directory.  
5. Copy the `jimObjModelImporterJFX.jar` file (the unextracted `.jar` file) into the `javafx-sdk-17.0.2/lib` directory.


### Command
`javac --module-path $PATH_TO_FX --add-modules=javafx.controls,javafx.fxml,jimObjModelImporterJFX,javafx.media *.java
java --module-path $PATH_TO_FX --add-modules=javafx.controls,javafx.fxml,jimObjModelImporterJFX,javafx.media Camera3D`

