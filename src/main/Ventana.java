/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Date;
import java.util.Enumeration;
import javax.swing.ImageIcon;
 
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
 
public class Ventana extends JFrame implements ActionListener {
 
    private JPanel contentPane;     //variable de tipo JPanel, para añadir elementos al panel
    private JTextField textField;       //variable tipo Jtextfield(cuadro en blanco)
    Enumeration puertos_libres =null;       //variable de tipo Enumeration
    CommPortIdentifier port=null;           //variable liberira rxtx (Identificar el puerto de conexion arduino)
    SerialPort puerto_ser = null;           //variable libreria rxtx()(Conexion con puerto serial)
    OutputStream out = null;                //variable de tipo outputStream
    InputStream in = null;                  //variable de tipo inputstream
    int temperatura=10;                     //Variable de tipo int
    Thread timer;                           //variable de tipo thread
    JLabel lblNewLabel;                     //Objetos tipo label para la interfaz grafica
    JLabel lblNewLabel1;
    JLabel bajo;
    JLabel medio;
    JLabel alto;
    JLabel escala;
    JLabel logo;
    JButton btnNewButton,btnNewButton_1,Guardar;  //Objetos Jbutton 
    JFileChooser fileChooser;                     //Objetos JfoleChooser
    JTextArea areaDeTexto;                        //Objeto tipo Jtextarea

    public Ventana() {           //  Contructor para añadir los elementos y las caracteristicas del frame
        setResizable(false);    //Metodo para bloquear el cambio de tamaño del frame
        Image icon = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/imagen/Imagen1.png")); // Metodo para asigar imagen de icono a el frame
        setIconImage(icon); //Muestra imagen en el frame
        setVisible(true);  //Siempre visible
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//Metodo para sacar por pantalla el frame
        setBounds(100, 100, 636, 365); // Dimensiones del frame
        this.setTitle("SENSOR TEMPERATURA"); // metodo para asignar el titulo del frame
        
        contentPane = new JPanel();  //Objeto tipo Jpanel para añadir elementos al panel
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane); //Asignar al panel los elementos del contentpane
        contentPane.setLayout(null);
        contentPane.setBackground(Color.LIGHT_GRAY);  //Metodo para elegir color de fondo al panel
        fileChooser=new JFileChooser();   //creacion de un objeto tipo Jfilechooser
        areaDeTexto = new JTextArea();
	//para que el texto se ajuste al area
	areaDeTexto.setLineWrap(true);
        
        this.setVisible(true);
        timer = new Thread(new ImplementoRunnable());   //creacion de un hilo 
        timer.start();                                  //metodo para iniciar el hilo
        timer.interrupt();                              //Metodo para cortar la ejecucion del hilo
        btnNewButton = new JButton("Conectar Sensor");      //Boton conectar tipo Jbutton
        btnNewButton.addActionListener(new ActionListener() {   //se crea un actionlistener
            public void actionPerformed(ActionEvent e) {        //creacion del metodo action performed para el evento del boton conectar
                  puertos_libres = CommPortIdentifier.getPortIdentifiers(); //Metodo para reconocer el puerto donde se conecta el arduino
                    int aux=0;
                    while (puertos_libres.hasMoreElements())    //crean un ciclo que devuelve true si la enumeracion con tiene mas elementos
                    {
                     port = (CommPortIdentifier) puertos_libres.nextElement(); //Se asigna a la variable port el puerto al cua lesta conectado el arduino
                     int type = port.getPortType();                          // este metodo asigna a type el valor obtenido del puerto
                     if (port.getName().equals(textField.getText()))        //condicional si el nombre del puerto es el mismo que asigamos al jtextfield (puerta com)
                     {
                            try {
                                    puerto_ser = (SerialPort) port.open("puerto serial", 2000);
                                    int baudRate = 9600; // 9600bps
                                    //configuracion de arduino
                                    puerto_ser.setSerialPortParams(
                                            baudRate,
                                            SerialPort.DATABITS_8,
                                            SerialPort.STOPBITS_1,
                                            SerialPort.PARITY_NONE);
                                    puerto_ser.setDTR(true);
                 
                                    out = puerto_ser.getOutputStream();//Envia los bytes
                                    in = puerto_ser.getInputStream(); // Recibe los bytes
                                    btnNewButton_1.setEnabled(true);//Metodo para activar un boton cuando el otro no lo esta
                                    btnNewButton.setEnabled(false);
                                    timer.resume();//esta variable iniciará nuestro metodo ImplementoRunnable
                            } catch (  IOException e1) {  //Captura los errores producidos en la la conexion con arduino
                                
                            } catch (PortInUseException e1) {
                                e1.printStackTrace();
                            } catch (UnsupportedCommOperationException e1) {
                                e1.printStackTrace();
                            }
 
                         break;
                     }
                    }
            }
        });
        
        
        btnNewButton.setBounds(45, 140, 140, 23);  //Dimensiones del Jbutton conectar
        contentPane.add(btnNewButton);              //metodo para añadir boton al panel
        
        btnNewButton_1 = new JButton("Desconectar");      //Creacion del boton desconectar
            btnNewButton_1.setEnabled(false);               //Metodo para mantender el boton desactivado
            btnNewButton_1.addActionListener(new ActionListener() {
            
                public void actionPerformed(ActionEvent arg0) {   //asignacion a cumplir una vez se realice el evento
                    timer.interrupt();      //Metodo para cortar el hilo de la conexion
                puerto_ser.close();             //Metodo para cerrar la entradade datos del puerto serial
                btnNewButton_1.setEnabled(false);      //Metodo para mantener solo un boton activado
                btnNewButton.setEnabled(true);
            }
        });
        
        btnNewButton_1.setBounds(210, 140, 128, 23);        //Dimensiones del boton desconectar
        contentPane.add(btnNewButton_1);                    //Metodo para añadir el boton desconectar
                
        Guardar = new JButton("Guardar");                   //creacion de buton guardar tipo Jbutton
            Guardar.setBounds(130, 340, 128, 23);           //Dimensiones del boton guardar
            contentPane.add(Guardar);                       //Metodo para añadi el boton guardar
            Guardar.addActionListener(this);                //Metodo para implementar un evento al boton guardar
               
                
            Guardar.addActionListener(new ActionListener(){     //tarea a realizar una vez se active el evento del boton guardar
                    
                public void actionPerformed(ActionEvent ae){
                          
                     if (ae.getSource()==Guardar)  //Condicional( evento igual a jbutton guardar)  
                        {
                            guardarArchivo();   //Si se cumple llamamos el metodo guardar archivo.
                        }
                }   
            }                
        );
        
        textField = new JTextField();                       //Creacion de objeto tipo Jtextield para la asignar la compuerta
            textField.setBounds(130, 100, 128, 20);
            contentPane.add(textField);
            textField.setColumns(10);                       //Metodo para numero de columnas visibles en el Jtextfield
 
        JLabel lblPuertoCom = new JLabel("Puerto COM");         //Creacion de objeto Jlabel
            lblPuertoCom.setBounds(155, 80, 90, 14);
            contentPane.add(lblPuertoCom);
            lblPuertoCom .setFont(new java.awt.Font("Arial", 1, 13));  //Metodo para editar las caracteriticas de la letra dentro del label
        ImageIcon titulo = new ImageIcon("C:\\Users\\josel\\OneDrive\\Documentos\\Imagen2.png"); //Asignacion de ruta de imagen a objeto ImageIcon   
        lblNewLabel = new JLabel(titulo);                               //Este label imprme por pantala la imagen asignada al objeto titulo
            lblNewLabel.setBounds(10, 10, 280, 60);
            lblNewLabel .setFont(new java.awt.Font("Arial", 0, 18));
            lblNewLabel .setForeground(Color.black);            //Metodo para seleccionar el color de la letra
            contentPane.add(lblNewLabel);
        
        lblNewLabel1 = new JLabel("Temperatura     Termometro        Escala");
            lblNewLabel1.setBounds(40, 180, 400, 24);
            lblNewLabel1 .setFont(new java.awt.Font("Arial", 1, 14));
            lblNewLabel1 .setForeground(Color.black);
            contentPane.add(lblNewLabel1);
        
        escala = new JLabel();
            escala.setForeground(Color.black);
            escala.setBounds(40, 250, 100, 50);
            contentPane.add(escala);
            escala.setFont(new java.awt.Font("Arial", 0, 40));
            escala .setForeground(Color.BLACK);
        
        bajo = new JLabel("MENOR A 20");
            bajo.setForeground(Color.blue);
            bajo.setBounds(260, 220, 120, 24);
            contentPane.add(bajo);
        
        medio= new JLabel ("ENTRE 20 y 39");
            medio.setForeground(Color.YELLOW);
            medio.setBounds(260, 235, 120, 24);
            contentPane.add(medio);
        
        alto = new JLabel ("MAYOR a 40");
            alto.setForeground(Color.red);
            alto.setBounds(260, 250, 120, 24);
            contentPane.add(alto);
        
        
        ImageIcon imagen = new ImageIcon("C:\\Users\\josel\\OneDrive\\Documentos\\ICONO2.01.png"); //Asignacion de ruta imagen a objeto tipo imageIcon
        logo = new JLabel(imagen);                  //Asignacion de imagen a objeto tipo Jlabel
            logo.setBounds(270, 10, 102, 120);
            contentPane.add(logo);
            logo.setBackground(Color.yellow);
        
        /*areaDeTexto= new JTextArea();
        areaDeTexto.setBounds(40, 250, 100, 50);
        areaDeTexto.setFont(new java.awt.Font("Arial", 0, 40));
        
        areaDeTexto .setForeground(Color.BLACK);
        areaDeTexto .setBackground(Color.white.brighter());
        contentPane.add(areaDeTexto);*/
    }
        private void guardarArchivo() {         //Metodo a realizar cuando se active el evento del boton guardar
            java.util.Date fecha = new Date();   //Metodo para obtener la fecha
	 		try
	 		{
				String nombre="";   //Creacion de variable tipo String
				JFileChooser file=new JFileChooser(); //Creacion de objeto tipo Jfilechooser(FileChooser es un seleccionador de ficheros con interfaz preestablecida)
				file.showSaveDialog(this);  //Metodo para guardar un nuevo fichero
				File guarda =file.getSelectedFile();//Este metodo es para seleccionar un fichero
		
				if(guarda !=null)
				{
		 			nombre=file.getSelectedFile().getName();//se asigna al atrubuto nombre el nombre del fichero seleccionado
		 			//guardamos el archivo y le damos el formato directamente,
		 			FileWriter  save=new FileWriter(guarda+".txt"); //Crea una variable para escribir en el archivo
		 			save.write("La temperatura recibida por el sensor es: "+escala.getText()+" Fecha: "+fecha);//Guarda la temperatura en el archivo de texto con extencion txt
		 			save.close();//cierra el archivo
		 			JOptionPane.showMessageDialog(null,"El archivo se a guardado Exitosamente","Información",JOptionPane.INFORMATION_MESSAGE);// Salida por pantalla de notificacion
			    }
	 		 }
	 	   catch(Throwable e)
		   {
			 JOptionPane.showMessageDialog(null,"Su archivo no se ha guardado","Advertencia",JOptionPane.WARNING_MESSAGE); //Salida por pantalla en caso de que haya un error(exception)
		   }
	
    }
    
    
    public void paint(Graphics g) {   //Metodo para dibujar la barra de temperatura
        
        
        
        
        super.paint(g);
           
            if(temperatura<20){
        g.setColor(Color.blue);         //Impresion del color segun la condicion
            }
            else if(temperatura>=20 && temperatura<40){
            g.setColor(Color.YELLOW);
            }
            else
            g.setColor(Color.red);
            
        g.fillRect(160, 330-temperatura, 80, temperatura);  //Con este metodo se asignan las dimensiones de la barra de temperatura
        try {
            Thread.sleep(200); //Este metodo le dice al thread que pare por los milisegundos especificados
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        
    }
    private class ImplementoRunnable implements Runnable{   //Este es el metodo a ejecutar una vez inicia el hilo
        int aux;    //Creacion de atributo tipo int
        public void run() {       
            while(true){    //ciclo que permiteel acceso de el hilo
                try {
                out.write('T'); //Se envia el dato al puerto desde netbeans mediante el objeto out
                Thread.sleep(100); //Detencion del thread por 100 milisegudos
                aux = in.read(); //Se asigna al atributo aux la lecutra recibida del arduino
                if (aux!=2){
                temperatura = aux; //Se asigna al atributo temperatura el dato recibido por el sensor
                escala.setText(""+temperatura+" ºC"); //lo imprimimos para mostrar el valor de la temperatura
            System.out.println(aux);
            }
                repaint();  //Este metodo dibuja invocando el metodo paint
 
            } catch (Exception e1) {
            
            }
                
            }
        }
    }
}