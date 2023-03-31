import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;

import java.util.Scanner;
import java.util.Set;

public class Client {
    public static void main(String[] args) {


        Socket socket = null;
        InputStreamReader inputStreamReader = null;
        OutputStreamWriter outputStreamWriter = null;
        BufferedReader bufferedReader = null;
        BufferedWriter bufferedWriter = null;

        Scanner scan = new Scanner(System.in);


        try {

            socket = new Socket("localhost", 8080);

            inputStreamReader = new InputStreamReader(socket.getInputStream());
            outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());

            bufferedReader = new BufferedReader(inputStreamReader);
            bufferedWriter = new BufferedWriter(outputStreamWriter);

            while (true) {

                String message = userInput();


                bufferedWriter.write(message);
                bufferedWriter.newLine();
                bufferedWriter.flush();

                String resp = bufferedReader.readLine();
                JSONObject persons = openRespons(resp);

                String val = scan.nextLine();
                int number = Integer.parseInt(val);
                Set<String> keys = persons.keySet();

                if (number != 1 && number <= 4){
                    String p = "p" + number;
                    JSONObject person = (JSONObject) persons.get(p);
                    System.out.println(person.get("name") + ", " + person.get("age") + ", " + person.get("color"));

                } else if (number == 1){
                    for (String x : keys) {
                        JSONObject person = (JSONObject) persons.get(x);


                        System.out.println(person.get("name"));
                    }
                }

                if (message.equalsIgnoreCase("Exit"))
                    break;
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {

            try {
                if (socket != null) {
                    socket.close();
                }
                if (inputStreamReader != null) {
                    inputStreamReader.close();
                }
                if (outputStreamWriter != null) {
                    outputStreamWriter.close();
                }
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                if (bufferedWriter != null) {
                    bufferedWriter.close();
                }
            } catch (Exception e) {
                System.out.println(e);
                System.out.println("Client disabled");
            }
        }
    }

    static String userInput(){

        System.out.println("Who would you like to know more about?");

        System.out.println("Type 1 to see your choices!");
        System.out.println("2 <-- Person 1");
        System.out.println("3 <-- Person 2");
        System.out.println("4 <-- Person 3");




        JSONObject jsonReturn = new JSONObject();
        jsonReturn.put("httpURL", "allPersons");
        jsonReturn.put("httpMethod", "get");

        //Returnera JSON objekt
        return jsonReturn.toJSONString();

    }

    static JSONObject openRespons(String resp) throws ParseException {


        JSONParser parser = new JSONParser();

        JSONObject serverRespons = (JSONObject) parser.parse(resp);


        if ("200".equals(serverRespons.get("httpStatusCode").toString())){


            JSONObject data = (JSONObject) parser.parse((String) serverRespons.get("data"));

            return data;
        }
        return null;
    }
}
