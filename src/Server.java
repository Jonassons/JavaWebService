import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {

        ServerSocket serverSocket;
        Socket socket;
        InputStreamReader inputStreamReader;
        OutputStreamWriter outputStreamWriter;
        BufferedReader bufferedReader;
        BufferedWriter bufferedWriter;
        try {

            serverSocket = new ServerSocket(8080);
        }
        catch (Exception e)  {
            System.out.println(e);
            return;
        }

        while (true){
            try {
                socket = serverSocket.accept();

                inputStreamReader = new InputStreamReader(socket.getInputStream());
                outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());

                bufferedReader = new BufferedReader(inputStreamReader);
                bufferedWriter = new BufferedWriter(outputStreamWriter);

                while (true){
                    String message = bufferedReader.readLine();

                    String returnData = openUpData(message);

                    System.out.println("message received");

                    bufferedWriter.write(returnData);
                    bufferedWriter.newLine();
                    bufferedWriter.flush();

                    if (message.equalsIgnoreCase("exit")){
                        break;
                    }
                }
                socket.close();
                inputStreamReader.close();
                outputStreamWriter.close();
                bufferedReader.close();
                bufferedWriter.close();
            }
            catch (Exception e){
                System.out.println(e);
            }
        }
    }

    static String openUpData (String message) throws ParseException, IOException {

        JSONParser parser = new JSONParser();
        JSONObject jsonOb = (JSONObject) parser.parse(message);


        String url = jsonOb.get("httpURL").toString();
        String method = jsonOb.get("httpMethod").toString();


        String[] urls = url.split("/");


        switch (urls[0]) {
            case "allPersons": {
                if (method.equals("get")) {

                    JSONObject jsonReturn = new JSONObject();

                    jsonReturn.put("data", parser.parse(new FileReader("src/information.json")).toString());


                    jsonReturn.put("httpStatusCode", 200);


                    return jsonReturn.toJSONString();
                }
                break;
            }
        }
        return "message received";
    }
}