package org.black_ixx.bossshop.managers.serverpinging;

import org.black_ixx.bossshop.managers.ClassManager;

import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;


public class ServerConnector3 implements ServerConnector {

    //A very old connector


    @Override
    public boolean update(ServerInfo info) {
        return fetchData(info);
    }


    public boolean fetchData(ServerInfo info) {

        Socket socket = new Socket();

        try {

            socket.setSoTimeout(info.getTimeout());
            socket.connect(info.getAddress(), info.getTimeout());
            OutputStream os = socket.getOutputStream();
            DataOutputStream dos = new DataOutputStream(os);
            InputStream is = socket.getInputStream();

            InputStreamReader isr = new InputStreamReader(is, Charset.forName("UTF-16BE"));
            dos.write(new byte[]{(byte) 0xFE, (byte) 0x01});
            int packetId = is.read();

            if (packetId == -1) {
                info.setNoConnection();
                return false;
            }

            if (packetId != 0xFF) {
                info.setNoConnection();
                return false;
            }

            int length = isr.read();

            if (length == -1) {
                info.setNoConnection();
                return false;
            }
            if (length == 1) {
                info.setNoConnection();
                return false;
            }

            char[] chars = new char[length];
            socket.close();

            if (isr.read(chars, 0, length) != length) {
                info.setNoConnection();
                return false;
            }

            String string = new String(chars);
            String[] data = string.split("\0");

            info.setMotd(data[3]);
            info.setPlayers(Integer.parseInt(data[4]));
            info.setMaxPlayers(Integer.parseInt(data[5]));
            info.setOnline(true);
            return true;

        } catch (Exception e) {
            info.setNoConnection();
            if (ClassManager.manager.getSettings().isDebugEnabled()) {
                e.printStackTrace();
            }

            try {//new
                socket.close();
            } catch (IOException e1) {
            }//new end

            return false;
        }
    }


}
