package com.yy.aes

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage

/**
 * Created by binlly on 2018/10/16-16:48
 * @author binlly
 */

class Main: Application() {

    @Throws(Exception::class)
    override fun start(primaryStage: Stage) {
        val root = FXMLLoader.load<Parent>(javaClass.getResource("main.fxml"))
        primaryStage.title = "AES加解密测试工具"
        primaryStage.scene = Scene(root, 670.0, 520.0)
        primaryStage.show()

        val loader = FXMLLoader(javaClass.getResource("main.fxml"))
        val controller = loader.getController<Controller>()
    }
}

fun main(args: Array<String>) {
    Application.launch(*args)
}
