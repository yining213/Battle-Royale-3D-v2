＊＊＊＊＊ 要先新增東西 ＊＊＊＊＊
點開src檔內的JFX3DModelImporters_EA_2014-02-09，解壓縮jimObjModelImporterJFX.jar，找一下你電腦執行javafx的路徑資料夾（javafx-sdk-17.0.2），把剛剛解壓縮好的資料夾放到javafx-sdk-17.0.2/src內，另外複製jimObjModelImporterJFX.jar(沒解壓縮的jar檔)到javafx-sdk-17.0.2/lib內

Cmd:

javac --module-path $PATH_TO_FX --add-modules=javafx.controls,javafx.fxml,jimObjModelImporterJFX,javafx.media *.java
java --module-path $PATH_TO_FX --add-modules=javafx.controls,javafx.fxml,jimObjModelImporterJFX,javafx.media Camera3D

