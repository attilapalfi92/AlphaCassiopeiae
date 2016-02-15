package com.attilapalfi.network

import com.attilapalfi.commons.BUFFER_SIZE
import com.attilapalfi.commons.UdpMessageBroadcaster
import com.attilapalfi.commons.messages.*
import com.attilapalfi.commons.utlis.ClientMessageConverter
import com.attilapalfi.commons.utlis.ServerMessageConverter
import com.attilapalfi.game.World
import org.junit.Assert
import org.junit.Test
import org.mockito.Mockito
import java.io.OutputStream
import java.net.InetAddress
import java.net.Socket

/**
 * Created by palfi on 2016-02-14.
 */
class TcpConnectionHandlerTest {

    val world: World = World()
    val udpMessageBroadcaster: UdpMessageBroadcaster = Mockito.mock(UdpMessageBroadcaster::class.java)
    val ackSender: AckSender = Mockito.mock(AckSender::class.java)
    val connectionManager: TcpConnectionManager = Mockito.mock(TcpConnectionManager::class.java)

    @Test
    fun canStopProperly() {
        val tcpConnection = TcpConnection(world, udpMessageBroadcaster, ackSender, connectionManager)
        tcpConnection.start()
        Thread.sleep(200)
        tcpConnection.disconnect()
        Thread.sleep(100)
        Assert.assertTrue(tcpConnection.stopped)
        Assert.assertFalse(tcpConnection.isConnected())
    }

    @Test
    fun canReadServerProperties() {
        val tcpConnection = TcpConnection(world, udpMessageBroadcaster, ackSender, connectionManager)
        tcpConnection.start()
        Thread.sleep(200)
        Assert.assertNotNull(tcpConnection.serverPort)
    }

    @Test
    fun canReadClientProperties() {
        val tcpConnection = TcpConnection(world, udpMessageBroadcaster, ackSender, connectionManager)
        tcpConnection.start()
        Thread.sleep(100)
        startClientThread(InetAddress.getByName("localhost"), tcpConnection.serverPort)
        Thread.sleep(200)
        Assert.assertNotNull(tcpConnection.clientIp)
        Assert.assertNotNull(tcpConnection.clientPort)
        tcpConnection.disconnect()
    }

    @Test
    fun canSendAck() {
        val tcpConnection = TcpConnection(world, udpMessageBroadcaster, ackSender, connectionManager)
        tcpConnection.start()
        Thread.sleep(100)
        val expected = ServerMessageConverter.tcpMessageToByteArray(TcpServerMessage(REG_ACK)) + MESSAGE_END
        val results = ByteArray(expected.size)
        val readingClientThread = startReadingClient(InetAddress.getByName("localhost"), tcpConnection.serverPort, results)
        Thread.sleep(100)
        tcpConnection.sendRegAck()
        readingClientThread.join()
        Assert.assertArrayEquals(expected, results)
    }

    private fun startClientThread(serverAddress: InetAddress, serverPort: Int): Thread {
        return Thread({
            val clientSocket: Socket = Socket(serverAddress, serverPort)
            clientSocket.outputStream.use {
                writeTcpClientMessagesToStream(it)
                Thread.sleep(1000)
            }
        }).apply { start() }
    }

    private fun writeTcpClientMessagesToStream(out: OutputStream) {
        var clientMessage: TcpClientMessage = TcpClientMessage(SENSOR_DATA, "alma")
        writeToStream(clientMessage, out)

        clientMessage = TcpClientMessage(REGISTRATION, "alma")
        writeToStream(clientMessage, out)

        clientMessage = TcpClientMessage(START, "alma")
        writeToStream(clientMessage, out)

        clientMessage = TcpClientMessage(PAUSE, "alma")
        writeToStream(clientMessage, out)

        clientMessage = TcpClientMessage(RESUME, "alma")
        writeToStream(clientMessage, out)
    }

    private fun writeToStream(clientMessage: TcpClientMessage, it: OutputStream) {
        it.write(ClientMessageConverter.tcpClientMessageToByteArray(clientMessage) + MESSAGE_END)
    }

    private fun startReadingClient(serverAddress: InetAddress, serverPort: Int, results: ByteArray): Thread {
        return Thread({
            val clientSocket: Socket = Socket(serverAddress, serverPort)
            clientSocket.inputStream.use {
                var readBytes: Int = 0
                while (readBytes != -1) {
                    readBytes = it.read(results)
                    if (readBytes != -1) {
                        println(readBytes)
                    }
                }
            }
        }).apply { start() }
    }
}