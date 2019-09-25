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
import java.lang.Exception
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

                vbox {
                    button("Se Connecter").action { controller.login() }
                    button("Se Déconnecter").action { controller.logout() }
                }
            }

            useMaxWidth = true
            addClass(GlobalStyles.bottomBorder)
        }


        listview(controller.history) {
            cellFormat { value ->
                graphic = vbox {
                    label("Message de : ${value.getUsername()} le ${value.time}").addClass(GlobalStyles.bottomBorder)
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

    private val receivedMessageQueue = ArrayBlockingQueue<Pair<Socket, Message>>(10)
    private lateinit var clientSocket: KotlinChatSocket

    val history: ObservableList<Message> = FXCollections.observableArrayList()
    val userName = SimpleStringProperty("")
    val messageValues = SimpleStringProperty("")

    val errorMessage = SimpleStringProperty("")
    val displayErrorMessage = SimpleBooleanProperty(false)

    var isLogged = false

    init {
        subscribe<UpdateHistoryRequest> { history.add(it.message) }

        fixedRateTimer("timer", false, 0L, 100) {
            val pair = receivedMessageQueue.poll() ?: return@fixedRateTimer
            val message = pair.second

            if (message.identifier == MessageIdentifier.SEND)
            {
                fire(UpdateHistoryRequest(message))
            }
        }
    }

    fun login() {
        if (userName.value.isNullOrEmpty() || userName.value == "server") {
            displayError("Nom d'utilisateur non valide...")
            return
        } else if (isLogged) { return }

        clientSocket = KotlinChatSocket(Socket("127.0.0.1", 4242), receivedMessageQueue)
        isLogged = true

        val message = MessageFactory.createLoginMessage(userName.value)
        clientSocket.sendMessage(message)
    }

    fun logout() {
        if (!isLogged)
            return

        isLogged = false

        val message = MessageFactory.createLogoutMessage(userName.value)
        clientSocket.sendMessage(message)
    }

    fun sendMessage() {
        if (userName.value.isNullOrEmpty() || messageValues.value.isNullOrEmpty())
            return

        val message = MessageFactory.createSendMessage(userName.value, messageValues.value)
        clientSocket.sendMessage(message)
        history.add(message)
    }

    /* UI */

    private fun displayError(message: String)
    {
        errorMessage.set(message);
        displayErrorMessage.set(true)
    }
}