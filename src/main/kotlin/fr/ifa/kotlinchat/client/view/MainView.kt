package fr.ifa.kotlinchat.client.view

import fr.balthazarfrolin.kotlin.tools.views.GlobalStyles
import fr.ifa.kotlinchat.client.app.Styles
import fr.ifa.kotlinchat.common.message.Message
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
import java.util.concurrent.ArrayBlockingQueue
import kotlin.concurrent.fixedRateTimer

class MainView : View("Kotlin Chat") {

    private val controller = MainViewController(this)

    init {
        importStylesheet<GlobalStyles>()
    }

    override val root = vbox {
        label(title) {
            useMaxWidth = true
            addClass(Styles.heading)
        }

        label(controller.errorMessage)
                .visibleWhen(controller.displayErrorMessage)
                .addClass(GlobalStyles.errorMessage)

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
                graphic = vbox {
                    label("Message de : ${value.getUsername()}").addClass(GlobalStyles.bottomBorder)
                    label(value.getUserMessageContent())
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

class MainViewController(mainView: MainView) : Controller()
{
    class UpdateHistoryRequest(val message: Message) : FXEvent()

    private val receivedMessageQueue = ArrayBlockingQueue<Message>(10)
    private val clientSocket = KotlinChatSocket(Socket("127.0.0.1", 4242), receivedMessageQueue)

    val history: ObservableList<Message> = FXCollections.observableArrayList()
    val userName = SimpleStringProperty("")
    val messageValues = SimpleStringProperty("")

    val errorMessage = SimpleStringProperty("")
    val displayErrorMessage = SimpleBooleanProperty(false)

    init {
        subscribe<UpdateHistoryRequest> { history.add(it.message) }

        fixedRateTimer("timer", false, 0L, 100) {
            val message = receivedMessageQueue.poll() ?: return@fixedRateTimer

            if (message.identifier == MessageIdentifier.SEND)
            {
                fire(UpdateHistoryRequest(message))
            }
        }
    }

    fun login(): Boolean
    {
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
        if (userName.value.isNullOrEmpty() || messageValues.value.isNullOrEmpty())
            return

        val message = MessageFactory.createSendMessage(MessageIdentifier.SEND, userName.value, messageValues.value)
        clientSocket.sendMessage(message.toString())
        history.add(message)
    }
}