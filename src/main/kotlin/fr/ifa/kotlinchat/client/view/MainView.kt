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
import javafx.geometry.Insets
import javafx.scene.text.TextAlignment
import tornadofx.*
import java.net.Socket
import java.util.concurrent.ArrayBlockingQueue
import kotlin.concurrent.fixedRateTimer

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

        borderpane {
            vboxConstraints {
                margin = Insets(10.0, 10.0, 10.0, 10.0)
                isFillWidth = true
            }
            left = label("Nom d'utilisateur : ") {
                useMaxSize = true
                textAlignment = TextAlignment.CENTER
            }

            center = textfield(controller.userName) {
                borderpaneConstraints { margin = Insets(10.0, 10.0, 10.0, 10.0) }
                editableWhen(controller.isLogged.not())
            }

            right = vbox {
                button("Se Connecter") {
                    vboxConstraints { margin = Insets(0.0, 0.0, 5.0, 0.0) }
                    action { controller.login() }
                }
                button("Se Déconnecter") {
                    vboxConstraints { margin = Insets(5.0, 0.0, 0.0, 0.0) }
                    action { controller.logout() }
                }
            }
            }

        listview(controller.history) {
            cellFormat { value ->
                graphic = vbox {
                    label("Message de : ${value.getUsername()} le ${value.getUserMessageTime()}").addClass(GlobalStyles.bottomBorder)
                    label(value.getUserMessageContent())
                }
            }
        }

        label(controller.errorMessage)
                .visibleWhen(controller.displayErrorMessage)
                .addClass(GlobalStyles.errorMessage)

        label("Nouveau Message") {
        }

        textarea(controller.messageValues) {
            vboxConstraints { margin = Insets(0.0, 0.0, 10.0, 0.0) }
            maxHeight = 100.0
        }

        button("Envoyer") {
            useMaxWidth = true
            action { controller.sendMessage() }
        }
    }
}

class MainViewController : Controller()
{
    class UpdateHistoryRequest(val message: Message) : FXEvent()

    private val receivedMessageQueue = ArrayBlockingQueue<Pair<Socket, Message>>(1000)
    private lateinit var clientSocket: KotlinChatSocket

    val history: ObservableList<Message> = FXCollections.observableArrayList()
    val userName = SimpleStringProperty("")
    val messageValues = SimpleStringProperty("")

    val errorMessage = SimpleStringProperty("")
    val displayErrorMessage = SimpleBooleanProperty(false)

    var isLogged = SimpleBooleanProperty(false)

    init {
        subscribe<UpdateHistoryRequest> {
            if (it.message.getUsername() != userName.value)
                history.add(0, it.message)
        }

        fixedRateTimer("timer", false, 0L, 100) {
            val pair = receivedMessageQueue.poll() ?: return@fixedRateTimer
            val message = pair.second

            when(message.identifier)
            {
                MessageIdentifier.SEND -> fire(UpdateHistoryRequest(message))
                MessageIdentifier.HISTORY -> {
                    for (historyMessage in message.getHistoryMessagesList())
                    fire(UpdateHistoryRequest(historyMessage))
                }
                else -> return@fixedRateTimer
            }
        }
    }

    fun login() {
        if (userName.value.isNullOrEmpty() || userName.value == "server") {
            displayError("Nom d'utilisateur non valide...")
            return
        } else if (isLogged.value) { return }

        history.clear()
        clientSocket = KotlinChatSocket(Socket("127.0.0.1", 4242), receivedMessageQueue)
        isLogged.set(true)

        val message = MessageFactory.createLoginMessage(userName.value)
        clientSocket.sendMessage(message)
    }

    fun logout() {
        if (!isLogged.value)
            return

        isLogged.set(false)

        val message = MessageFactory.createLogoutMessage(userName.value)
        clientSocket.sendMessage(message)
        clientSocket.close()
    }

    fun sendMessage() {
        if (userName.value.isNullOrEmpty() || messageValues.value.isNullOrEmpty())
            return

        val message = MessageFactory.createSendMessage(userName.value, messageValues.value)
        clientSocket.sendMulticastMessage(message)
        history.add(0, message)
    }

    /* UI */

    private fun displayError(message: String)
    {
        errorMessage.set(message);
        displayErrorMessage.set(true)
    }
}