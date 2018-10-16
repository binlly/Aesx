package com.yy.aes

import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.geometry.Insets
import javafx.scene.control.*
import javafx.scene.input.TransferMode
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.CornerRadii
import javafx.scene.paint.Paint
import java.io.File
import java.net.URL
import java.util.*

/**
 * Created by binlly on 2018/10/16-16:48
 * @author binlly
 */

class Controller: Initializable {

    @FXML internal lateinit var text_input: TextField
    @FXML internal lateinit var text_origin: TextArea
    @FXML internal lateinit var text_result: TextArea
    @FXML internal lateinit var check_encdec: CheckBox
    @FXML internal lateinit var progress: ProgressIndicator
    @FXML internal lateinit var text_key: TextField
    @FXML internal lateinit var choice_mode: ChoiceBox<String>
    @FXML internal lateinit var choice_padding: ChoiceBox<String>

    private val bgActive by lazy {
        Background(BackgroundFill(Paint.valueOf("#eeeeee"), CornerRadii.EMPTY, Insets.EMPTY))
    }
    private val bgOrigin by lazy {
        Background(BackgroundFill(Paint.valueOf("#00ffffff"), CornerRadii.EMPTY, Insets.EMPTY))
    }
    private val alert by lazy {
        Alert(Alert.AlertType.WARNING)
    }
    private var originFile: File? = null

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        text_origin.setOnDragEntered { e ->
            text_input.text = "${e.dragboard.files}"
            text_origin.background = bgActive
        }

        text_origin.setOnDragOver { e ->
            if (e.gestureSource != text_origin && e.dragboard.hasFiles()) {
                if (e.dragboard.files.lastOrNull()?.isFile == true) {
                    e.acceptTransferModes(*TransferMode.ANY)
                }
            }
            e.consume()
        }

        text_origin.setOnDragDropped { e ->
            originFile = e.dragboard.files.lastOrNull()
            text_origin.background = bgOrigin
            originFile?.let {
                text_input.text = it.absolutePath
                if (it.isFile) {
                    text_origin.text = it.readLines().toString()
                }
            }
        }

        text_key.setOnKeyTyped { e ->
            val text = text_key.text ?: run {
                e.consume()
                return@setOnKeyTyped
            }
            if (text.length >= 16) e.consume()
        }
    }

    @FXML
    private fun clickDo() {
        val key = text_key.text
        if (!checkAesKey(key)) {
            alert.headerText = "出错了"
            alert.contentText = "Aes key 长度非法"
            alert.show()
            return
        }

        val content = originFile?.let {
            it.readLines().fold("") { acc, line -> "$line\n$acc" }
        } ?: text_input.text?.let {
            it
        } ?: return
        text_origin.text = content

        val result = if (check_encdec.isSelected) {
            AES.encryptString(content, key)
        } else {
            AES.decryptString(content, key)
        }
        text_result.text = result
    }

    private fun checkAesKey(key: String?): Boolean {
        if (key.isNullOrEmpty()) return false
        return key!!.length == 16
    }
}
