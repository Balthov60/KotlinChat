package fr.ifa.kotlinchat.client.view

import com.sun.org.apache.xpath.internal.operations.Bool
import fr.balthazarfrolin.kotlin.tools.views.GlobalStyles
import fr.ifa.kotlinchat.client.app.KotlinClientSocket
import fr.ifa.kotlinchat.client.app.Styles
import fr.ifa.kotlinchat.common.message.MessageFactory
import fr.ifa.kotlinchat.common.message.MessageIdentifier
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.*
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.Socket

class MainView : View("Hello TornadoFX") {

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
    private val clientSocket = KotlinClientSocket("127.0.0.1", 4242)

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