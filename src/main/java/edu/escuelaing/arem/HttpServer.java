package edu.escuelaing.arem;

import java.net.*;
import java.io.*;

public class HttpServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(getPort());
        } catch (IOException e) {
            System.err.println("Could not listen on port: 35000.");
            System.exit(1);
        }
        boolean running = true;
        while (running) {
            Socket clientSocket = null;
            try {
                System.out.println("Listo para recibir ...");
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }

            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            clientSocket.getInputStream()));
            String inputLine, outputLine;
            String file = "";
            boolean firstLine = true;
            while ((inputLine = in.readLine()) != null) {
                if (firstLine) {
                    file = inputLine.split(" ")[1];
                    System.out.println("File: " + file);
                    firstLine = false;
                }
                System.out.println("Received: " + inputLine);
                if (!in.ready()) {
                    break;
                }
            }
            outputLine = "";
            if (file.contains("/clima")) {
                outputLine = "HTTP/1.1 200 OK\r\n"
                        + "Content-Type: text/html\r\n"
                        + "\r\n"
                        + "<!DOCTYPE html>\n"
                        + "<html>\n"
                        + "<head>\n"
                        + "<script>"
                        + "function clima(pais){\n" +
                        "\n" +
                        "    fetch('http://localhost:4567/consulta?lugar='+pais)\n" +
                        "          .then(response => response.json())\n" +
                        "          .then(json => console.log(json))\n" +
                        "\n" +
                        "\n" +
                        "}"
                        +"</script>"
                        + "<meta charset=\"UTF-8\">\n"
                        + "<title>My weather app</title>\n"
                        + "</head>\n"
                        + "</head>\n"
                        + "<body>\n"
                        + "Consultar clima de una ciudad: \n"
                        + "<br>"
                        + "<input type=\"text\" id=\"fname\" name=\"fname\">"
                        + "<br>"
                        + "<button name=\"button\" onclick\"= clima(London)\" >Consultar</button>"
                        + "</body>\n"
                        + "</html>\n";

            } else if (file.contains("/consulta?lugar=")) {
                String resultado;

                Connection connection = new Connection();
                resultado = connection.getClima(file.split("=")[1]);

                outputLine = "HTTP/1.1 200 OK\r\n"
                        + "Content-Type: text/html\r\n"
                        + "\r\n"
                        + "<!DOCTYPE html>\n"
                        + "<html>\n"
                        + "<head>\n"
                        + "<meta charset=\"UTF-8\">\n"
                        + "<title>My weather app</title>\n"
                        + "</head>\n"
                        + "<body>\n"
                        + resultado
                        + "Consultar \n"
                        + "</body>\n"
                        + "</html>\n";
                // http://localhost:4567/consulta?lugar=London


            } else {
                outputLine = "HTTP/1.1 200 OK\r\n"
                        + "Content-Type: text/html\r\n"
                        + "\r\n"
                        + "<!DOCTYPE html>\n"
                        + "<html>\n"
                        + "<head>\n" + "<script> "
                        + "<meta charset=\"UTF-8\">\n"
                        + "<title>My weather app</title>\n"
                        + "</head>\n"
                        + "<body>\n"
                        + "My website\n"
                        + "</body>\n"
                        + "</html>\n";
            }
            out.println(outputLine);
            out.close();
            in.close();
            clientSocket.close();
        }
        serverSocket.close();
    }

    static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 4567; //returns default port if heroku-port isn't set }
    }

}