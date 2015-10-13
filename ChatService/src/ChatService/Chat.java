package ChatService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import ChatService.Contract.ChatListener;
import ChatService.Contract.ChatService;

public abstract class Chat extends Socket implements ChatService
{
	protected ChatListener _listener = null;
	protected Socket _socket = null;
	protected OutputStream _out = null;
	protected InputStream _in = null;
	protected boolean _closed = true;
	protected boolean _waitingConfirmation = false;
	
	public boolean GetOpen()
	{
		return !_closed;
	}
	
	public Chat(ChatListener listener)
	{
		_listener  = listener;
	}

	@Override
	public void Init()
	{
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				while (true)
				{					
					if (_closed || _socket == null || !_socket.isConnected() || _socket.isClosed())
					{
						Connect();
						continue;
					}
					
					if (_listener == null || _in == null || _out == null)
					{
						continue;
					}
					
					int current;
					StringBuilder message = new StringBuilder();
					byte[] byteMessage = new byte[2048];
					
					try
					{
						current = _in.read(byteMessage);
						message.append(new String(byteMessage, 0, current));
						
						if (message.toString().equalsIgnoreCase("//close"))
						{
							Send("//close");
							Close();
						}
						else
						{
							ProcessMessage(message.toString());
						}
					}
					catch (Exception ex)
					{
						ex.printStackTrace();
					}
				}
			}
			
		}).run();
	}
	
	private void Close()
	{
		try
		{
			_out = null;
			_in = null;
			_listener.Finish();
			
			if (!_socket.isClosed())
			{
				_socket.close();
			}
			
			_closed = true;
			_socket = null;
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void Send(String message) throws IOException
	{
		try
		{
			byte[] bytes = message.getBytes();
			_out.write(bytes);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			throw ex;
		}
	}

	@Override
	public void FinishChat() throws IOException
	{
		try
		{
			if (_socket != null && !_socket.isClosed())
			{
				this.Send("//close");
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			throw ex;
		}
	}
	
	protected abstract void Connect();
	
	protected void GetStreams()
	{
		if (_socket != null)
		{
			try
			{
				_in = _socket.getInputStream();
				_out = _socket.getOutputStream();
				_closed = false;
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public void ProcessMessage(String message)
	{
		_listener.Receive(message.toString());
	}
}
