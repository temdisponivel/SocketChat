package ChatService;

import java.io.IOException;
import java.net.Socket;

import ChatService.Contract.ChatListener;

public class ClientService extends Chat
{
	protected String _host = "";
	protected int _port = -1;
	
	public ClientService(ChatListener listener, String serverIP, int port)
	{
		super(listener);
		_port = port;
		_host = serverIP;
	}

	@Override
	protected void Connect()
	{
		try
		{
			_socket = new Socket(_host, _port);	
			this.GetStreams();
			_closed = false;
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public boolean IsOpen()
	{
		if (_socket == null)
		{
			return false;
		}
		
		return _socket.isConnected();
	}

}
