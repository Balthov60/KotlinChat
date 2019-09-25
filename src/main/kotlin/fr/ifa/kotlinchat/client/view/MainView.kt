package fr.ifa.kotlinchat.client.view

import fr.balthazarfrolin.kotlin.tools.views.GlobalStyles
import fr.ifa.kotlinchat.client.app.Styles
import fr.ifa.kotlinchat.common.message.MessageFactory
import fr.ifa.kotlinchat.common.message.MessageIdentifier
import fr.ifa.kotlinchat.common.socket.KotlinChatSocket
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.geometry.HPos
import javafx.geometry.VPos
import tornadofx.*
import java.net.Socket
import java.util.*

class MainView : View("Kotlin Chat") {

    private val controller = MainViewController()

    init {
        importStylesheet<GlobalStyles>()
    }

    override val root = vbox {
        label(title) {
            useMaxWidth = true
            addClass(Styles.heading)
        }

        vbox {
            addClass(GlobalStyles.defaultPadding)

            hbox {
                useMaxSize = true
                style { rowVAlignment = VPos.CENTER }

                label("Nom d'utilisateur : ")
                textfield(controller.userName)
                button("Se Connecter").action { controller.login() }
            }

            useMaxWidth = true
            addClass(GlobalStyles.bottomBorder)
        }


        listview(controller.history) {
            cellFormat { value ->
                println("test : $value")
                graphic = vbox {
                    label("Message de : test").addClass(GlobalStyles.bottomBorder)
                    label(value)
                }
            }
        }


        form {
            fieldset("Nouveau Message") {
                useMaxWidth = true
                // style { backgroundColor += Color.RED }

                textarea(controller.messageValues) {
                    maxHeight = 100.0
                }

                button("Envoyer") {
                    style { hAlignment = HPos.RIGHT }
                    action { controller.sendMessage() }
                }
            }
        }
    }
}

class MainViewController : Controller()
{
    private val clientSocket = KotlinChatSocket(Socket("127.0.0.1", 4242), ArrayDeque())

    val history: ObservableList<String> = FXCollections.observableArrayList("test", "top")
    val userName = SimpleStringProperty("")
    val messageValues = SimpleStringProperty("")

    val errorMessage = SimpleStringProperty("")
    val displayErrorMessage = SimpleBooleanProperty(false)

    fun login(): Boolean
    {
        history.add("teste");
        if (userName.value.isNullOrEmpty() || userName.value == "server")
        {
            displayError("Nom d'utilisateur non valide...")
            return false
        }

        val message = MessageFactory.createSimpleMessage(MessageIdentifier.LOGIN, userName.value)
        clientSocket.sendMessage(message)
        return true
    }

    /* UI */

    private fun displayError(message: String)
    {
        errorMessage.set(message);
        displayErrorMessage.set(true)
    }

    fun sendMessage() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}