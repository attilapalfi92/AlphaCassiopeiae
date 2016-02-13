import com.attilapalfi.commons.BUFFER_SIZE
import com.attilapalfi.commons.IntelligentTcpMessageBuffer
import com.attilapalfi.commons.TcpSignalProcessor
import com.attilapalfi.commons.messages.SENSOR_DATA
import com.attilapalfi.commons.messages.TcpClientMessage
import org.mockito.Mockito
import java.net.ServerSocket
import java.net.Socket

/**
 * Created by palfi on 2016-02-13.
 */
class TcpMessageBufferTest {

    val tcpMessageBuffer = IntelligentTcpMessageBuffer(Mockito.mock(TcpSignalProcessor::class.java))
    val serverPort = 6789

    fun startServerThread() {
        Thread({
            val serverSocket = ServerSocket(serverPort)
            serverSocket.bind(null)
            val connection: Socket = serverSocket.accept()
            connection.keepAlive = true


            while (true) {
                val array = ByteArray(BUFFER_SIZE)
                var readBytes: Int = 0
                connection.inputStream.use {
                    while (readBytes != -1) {
                        readBytes = it.read(array)
                        if (readBytes != -1) {
                            tcpMessageBuffer.tryToProcess(array, readBytes)
                        }
                    }
                }

            }
        }).start()
    }


    fun startClientThread() {
        Thread({
            val clientSocket: Socket = Socket("localhost", serverPort)
            clientSocket.outputStream.use {
                var clientMessage: TcpClientMessage = TcpClientMessage(SENSOR_DATA, "alma")

            }
        }).start()
    }

}