package org.black_ixx.bossshop.managers.serverpinging;

import org.black_ixx.bossshop.managers.ClassManager;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


public class ServerConnector4 implements ServerConnector {

    //A very old connector


    @Override
    public boolean update(ServerInfo info) {
        return ping(info);
    }


    public boolean ping(ServerInfo info) {
        try {
            final Socket socket = new Socket();
            socket.connect(info.getAddress(), info.getTimeout());

            final DataInputStream in = new DataInputStream(socket.getInputStream());
            final DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            out.write(0xFE);
            out.write(0x01);
            out.write(0xFA);
            out.writeShort(11);
            out.writeChars("MC|PingHost");
            out.writeShort(7 + 2 * info.getHost().length());
            out.writeByte(73); // Protocol version
            out.writeShort(info.getHost().length());
            out.writeChars(info.getHost());
            out.writeInt(info.getPort());

            out.flush();

            if (in.read() != 255) {
                socket.close();
                throw new IOException("Bad message: An incorrect packet was received.");
            }

            final short bit = in.readShort();

            final StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bit; ++i) {
                sb.append(in.readChar());
            }

            in.close();
            out.close();
            socket.close();

            final String[] bits = sb.toString().split("\0");


            info.setMotd(bits[3]);
            info.setPlayers(Integer.valueOf(bits[4]));
            info.setMaxPlayers(Integer.valueOf(bits[5]));
            info.setOnline(true);
            return true;
        } catch (Exception e) {
            info.setOnline(false);
            if (ClassManager.manager.getSettings().isDebugEnabled()) {
                e.printStackTrace();
            }
            return false;
        }
    }


}
