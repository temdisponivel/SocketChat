package ChatService.Contract;

public interface ChatListener
{
	public void Receive(String message);
	public void Finish();
}
