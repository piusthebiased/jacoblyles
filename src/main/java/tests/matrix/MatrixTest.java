package tests.matrix;

import matrix.bot.Client;
import matrix.bot.Message;
import org.json.JSONObject;

import java.io.IOException;

//EsTS 4TvK Yrk5 nVjr YeFx Fxrm YbbU rk6S Cmrc ZpQ1 kZJ6 b9bS
//EsTk yhD9 cvR2 kfDc bb8D 1k1N 9RWR Fk28 CxE2 yBzY gbM8 GsfG

public class MatrixTest {
    public static void main(String[] args) throws IOException {
        Client c = new Client("http://matrix-client.matrix.org/");
        c.login("thatonetestingaccount", "thisisaverybadpasswordlmao", loginData -> {
            if (loginData.isSuccess()) {
                JSONObject json = new JSONObject("{\"body\":\"message from java\",\"msgtype\":\"m.text\"}");

                c.registerRoomEventListener(roomEvent -> {
                    if(roomEvent.size() != 0){
                        System.out.println(roomEvent.get(0).getContent());
                    }
                });

                c.sendMessage("!GtIfdsfQtQIgbQSxwJ:archlinux.org", json, res -> {
                    System.out.println("message sent");
                });
            } else {
                System.err.println("error logging in");
            }
        });
    }
}
