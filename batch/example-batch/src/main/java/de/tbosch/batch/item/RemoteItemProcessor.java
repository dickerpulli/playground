package de.tbosch.batch.item;

import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import org.apache.commons.io.IOUtils;
import org.springframework.batch.item.ItemProcessor;

import de.tbosch.batch.model.Person;

public class RemoteItemProcessor implements ItemProcessor<Person, String> {

	@Override
	public String process(Person item) throws Exception {
		Socket socket = new Socket();
		socket.connect(new InetSocketAddress("localhost", 8888), 10);
		OutputStream outputStream = socket.getOutputStream();
		InputStream inputStream = socket.getInputStream();
		ObjectOutputStream oos = new ObjectOutputStream(outputStream);
		oos.writeObject(item);
		String name = IOUtils.readLines(inputStream).get(0);
		socket.close();
		return name;
	}

}
