package ChatService;

import java.io.IOException;
import java.net.ServerSocket;

import ChatService.Contract.ChatListener;

public class ServerService extends Chat
{
	protected ServerSocket _serverSocket = null;
	
	public ServerService(ChatListener listener)
	{
		super(listener);
	}

	@Override
	protected void Connect()
	{
		try
		{
			if (_serverSocket == null || !_serverSocket.isBound() || _serverSocket.isClosed())
			{
				_serverSocket = new ServerSocket(1550);
			}
			
			_socket = _serverSocket.accept();
			this.GetStreams();
			_closed = false;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public void FinishChat() throws IOException 
	{
		super.FinishChat();
	}
	
	public boolean IsOpen()
	{
		if (_serverSocket == null)
		{
			return false;
		}
		
		return _serverSocket.isBound();
	}
}
