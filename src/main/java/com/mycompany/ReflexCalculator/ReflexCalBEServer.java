package com.mycompany.ReflexCalculator;
 
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;


public class ReflexCalBEServer {
 
    public static void main(String[] args) throws IOException, MalformedURLException, URISyntaxException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(36000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 35000.");
            System.exit(1);
        }
            boolean running = true;
        while (running = true) {
 
            Socket clientSocket = null;
            try {
                System.out.println("Listo para recibir ...");
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }
            PrintWriter out = new PrintWriter(
                    clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
            String inputLine, outputLine;
            boolean isFirstLine = true; 
            String firstLine ="";
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Recibí: " + inputLine);
                if (isFirstLine){
                    firstLine = inputLine;
                    isFirstLine = false;
                }
                
                if (!in.ready()) {      
                    break;
                }   
            }
            
            URI requrl = getReqURL(firstLine); 
            
            if(requrl.getPath().startsWith("/compreflex")){
                outputLine = "HTTP/1.1 200 OK\r\n"
                    + "Content-Type: application/json\r\n"
                    + "\r\n"
                    + "{\"name\":\"John\", \"age\":30, \"car\":null}";
            }else{
                outputLine= getDefaultResponse();
            }
          
    
            out.println(outputLine);
            out.close();
            in.close();
            clientSocket.close();
        }
        serverSocket.close();
    }
 
    public static String getDefaultResponse() {
        String htmlcode
                =  "HTTP/1.1 200 OK\r\n"
        + "Content-Type: text/html\r\n"
         + "\r\n"
         + "<!DOCTYPE html>\n"
         + "<html>\n"
         + "<head>\n"
         + "<meta charset=\"UTF-8\">\n"
         + "<title>Method not found</title>\n"
         + "</head>\n"
         + "<body>\n"
         + "<h1>Mi propio mensaje</h1>\n"
         + "</body>\n"
         + "</html>\n";
        return htmlcode;
    }
    
    public static URI getReqURL(String firstline) throws MalformedURLException, URISyntaxException{
        
            String rurl = firstline.split(" ")[1];      
            return new URI(rurl);
        
    }
    
    public static String computeMathCommand (String command) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException{
        Class c = Math.class;
        Class[] parameterTypes ={double.class};
        Method m = c.getDeclaredMethod("abs",parameterTypes);
        Object[] params = {-2.0};
       String resp = m.invoke(null, (Object)params).toString();
        return "";
    }

 
}
