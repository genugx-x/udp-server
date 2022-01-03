package com.genug.udpserver.application;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.StandardProtocolFamily;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.charset.Charset;

@Slf4j
@Component
public class UdpSocketServer implements CommandLineRunner {

    private static final Integer PORT = 8090;

    @Override
    public void run(String... args) {
        log.info("UDP SERVER START!");
        DatagramChannel datagramChannel = null;
        try {
            datagramChannel = DatagramChannel.open(StandardProtocolFamily.INET); // StandardProtocolFamily.INET : IPv4
            datagramChannel.bind(new InetSocketAddress(PORT));
            log.info("[수신 시작]");
            try {
                while(true) {
                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024); // 1KB
                    SocketAddress sockerAddress = datagramChannel.receive(byteBuffer); // 데이터 받기
                    byteBuffer.flip();

                    Charset charset = Charset.forName("UTF-8");
                    String data = charset.decode(byteBuffer).toString();
                    log.info("[{}] : {}", sockerAddress.toString(), data);
                }
            } catch (Exception e) {
                log.warn("[수신 종료]");
                e.printStackTrace();
            }

            datagramChannel.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                datagramChannel.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        log.info("UDP SERVER SHUTDOWN!");
    }
}
