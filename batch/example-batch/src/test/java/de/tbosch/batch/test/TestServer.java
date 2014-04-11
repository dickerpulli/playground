package de.tbosch.batch.test;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;

import org.apache.commons.io.IOUtils;

import de.tbosch.batch.model.Person;

public class TestServer {

	public static void main(String[] args) throws Exception {
		ServerSocket serverSocket = new ServerSocket(8888);
		while (true) {
			Socket socket = serverSocket.accept();
			InputStream inputStream = socket.getInputStream();
			OutputStream outputStream = socket.getOutputStream();
			ObjectInputStream ois = new ObjectInputStream(inputStream);
			Person person = (Person) ois.readObject();
			System.out.println("GET: " + person);
			Thread.sleep(15000);
			IOUtils.writeLines(Collections.singletonList(person.getFirstname()), null, outputStream);
			outputStream.close();
			System.out.println("PUT: " + person.getFirstname());
		}
	}
}
