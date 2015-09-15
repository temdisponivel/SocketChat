package ChatService.Contract;

import java.io.IOException;

public interface ChatService
{
	public void Init();
	public void Send(String message) throws IOException;
	public void FinishChat() throws IOException;
	public boolean IsOpen();
}
