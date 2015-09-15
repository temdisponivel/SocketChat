package GUI;

import javax.swing.JFrame;

import java.awt.BorderLayout;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import ChatService.*;
import ChatService.Contract.ChatListener;
import ChatService.Contract.ChatService;

public class GUI extends javax.swing.JFrame implements ChatListener {

    private ChatService _chat = null;
    private JButton jButton1;
    private JButton jButton2;
    private JScrollPane jScrollPane1;
    private JTextArea jTextArea1;
    private JTextField jTextField1;
    private BorderLayout layout;
    private boolean client = false;
    private boolean showConnected = false;
    
    public GUI(boolean cliente) {    
    	this.client = cliente;
    	jTextField1 = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
	    
        jTextArea1.setColumns(20);
        jTextArea1.setEditable(false);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jButton1.setText("Enviar");
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton1MouseClicked(evt);
            }
        });

        jButton2.setText("Desconectar");
        jButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton2MouseClicked(evt);
            }
        });
        
			
	layout = new BorderLayout( 5, 5 ); 
        getContentPane().setLayout(layout);
		add( jScrollPane1, BorderLayout.CENTER ); 
		add( jTextField1, BorderLayout.SOUTH ); 
		add( jButton1, BorderLayout.EAST ); 
		add( jButton2, BorderLayout.NORTH ); 		
    }

    public void init(){
    	
    	if (this.client && _chat == null)
    	{
    		_chat = new ClientService(this, "127.0.0.1", 1550);
    	}
    	else if (_chat == null)
    	{
    		_chat = new ServerService(this);
    	}
    	
    	if (_chat != null)
    	{
    		_chat.Init();
    	}
	}
   
    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {
    	
    	String message = jTextField1.getText();
		if (_chat != null && _chat.IsOpen())
    	{
			if (showConnected)
			{
				this.addStrTextArea("YOU IS NOW CONNECTED AGAIN!");
			}
			
    		try
			{
				_chat.Send(message);
				this.addStrTextArea("SEND: \n" + message);
				jTextField1.setText("");
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
    	}
    	else
    	{
    		this.init();
    	}
    }

    private void jButton2MouseClicked(java.awt.event.MouseEvent evt) {
	   try
	   {
		   this.addStrTextArea("YOU'VE CLOSE THIS CHAT. TRYING TO CONNECT AGAING!");
		   _chat.FinishChat();
		   this.init();
	   }
	   catch (IOException e)
	   {
		   e.printStackTrace();
	   }
    }

    public void addStrTextArea(String str) {
        jTextArea1.append(str.trim() + "\n");
    }
    
    public static void main(String[] args) {
    	boolean cliente = args[0].equals("cliente");
        GUI talkGUI = new GUI(cliente);
	    talkGUI.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
	    talkGUI.setSize( 300, 200 ); // set frame size
        talkGUI.setVisible( true );
        talkGUI.init();
    }

	@Override
	public void Receive(String message)
	{
		this.addStrTextArea("RECEIVE: \n" + message);		
	}

	@Override
	public void Finish()
	{
		this.addStrTextArea("CHAT CLOSED BY OTHER COMPUTER. TRYING TO CONNECT AGAIN...");
		this.init();
	}   
}
