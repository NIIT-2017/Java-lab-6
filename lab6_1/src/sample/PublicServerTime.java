package sample;

import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Date;

public final class PublicServerTime {

    public static Date getNTPDate() {

        String[] hosts = new String[]{
                 "ntp04.oal.ul.pt",
                "ntp.xs4all.nl","ntp02.oal.ul.pt"};

        NTPUDPClient client = new NTPUDPClient();
        client.setDefaultTimeout(10000);

        for (String host : hosts) {

            try {
                InetAddress hostAddr = InetAddress.getByName(host);
                System.out.println("> " + hostAddr.getHostName() + "/" + hostAddr.getHostAddress());
                TimeInfo info = client.getTime(hostAddr);
                Date date = new Date(info.getMessage().getTransmitTimeStamp().getTime());
                return date;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        client.close();
        return null;

    }

}
