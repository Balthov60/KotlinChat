package fr.ifa.kotlinchat.client.view

import fr.balthazarfrolin.kotlin.tools.views.GlobalStyles
import fr.ifa.kotlinchat.common.socket.KotlinChatSocket
import fr.ifa.kotlinchat.client.app.Styles
import fr.ifa.kotlinchat.common.message.MessageFactory
import fr.ifa.kotlinchat.common.message.MessageIdentifier
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.*

class LoginView : View("Chat Login") {

    private val controller = MainViewController()

    override val root = vbox {
        label(title) {
            addClass(Styles.heading)
        }

        form {
            fieldset("Connexion") {
                field("username") { add(textfield(controller.userName)) }
                button("Se Connecter").action { controller.login() }
            }
        }

        label(controller.errorMessage)
                .addClass(GlobalStyles.errorMessage)
                .visibleWhen(controller.displayErrorMessage)

        vbox {
            addClass(GlobalStyles.defaultPadding)
            addClass(GlobalStyles.fullBorder)

            textarea(controller.messageValues)
        }

    }
}

class MainViewController : Controller()
{
    private val clientSocket = KotlinChatSocket("127.0.0.1", 4242)

    val userName = SimpleStringProperty("")
    val messageValues = SimpleStringProperty("")

    val errorMessage = SimpleStringProperty("")
    val displayErrorMessage = SimpleBooleanProperty(false)

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
}