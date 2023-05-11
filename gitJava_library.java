package final_team;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

class resTable extends JTable{
	public resTable(DefaultTableModel model) {
		super(model);
		
		//���� �߾�����
		DefaultTableCellRenderer renderer = (DefaultTableCellRenderer)this.getDefaultRenderer(Object.class);
		renderer.setHorizontalAlignment( SwingConstants.CENTER );
		
		//�� ������ ���� �Ұ�
		this.getTableHeader().setReorderingAllowed(false);
		this.getTableHeader().setResizingAllowed(false);
		
		//�� ������ ����
		this.setRowHeight(30);
		this.getColumnModel().getColumn(0).setPreferredWidth(60);
		this.getColumnModel().getColumn(1).setPreferredWidth(140);
		this.getColumnModel().getColumn(2).setPreferredWidth(140);
		this.getColumnModel().getColumn(3).setPreferredWidth(290);
		this.getColumnModel().getColumn(4).setPreferredWidth(140);
		this.getColumnModel().getColumn(5).setPreferredWidth(210);
		this.getColumnModel().getColumn(6).setPreferredWidth(140);
	}
}

public class gitJava_library extends JFrame{
	static Connection conn;
	
	//main �����̳� ����
	Container 	main_con; 		//�����̳�
	JFrame 		main_frame;		//���� ������ 
	CardLayout 	main_lay_card; 	//ī�巹�̾ƿ�
	
	//login ȭ�� ����
	JTextField 		login_tf_id; //id�Է� �ؽ�Ʈ�ʵ�
	JPasswordField 	login_pf_pw; //password�Է� �ؽ�Ʈ�ʵ�
	
	ImageIcon logoImage = new ImageIcon("./res/logo.png");
	ImageIcon imageIcon = new ImageIcon("./res/icon.png");
	ImageIcon adminLogo = new ImageIcon("./res/admin_logo.png");
	
	String[] dayList = new String[5]; //�޺��ڽ��ȿ� ���� ��¥ ����Ʈ
	String[] stroomNum = {"���͵��1","���͵��2","���͵��3"};
	String listname = " ";
	
	JLabel sizeLabel;      //JLabel �� �������
	
	LocalDateTime now = LocalDateTime.now();
	int nowDate = (now.getYear()*10000 + now.getMonthValue() * 100 + now.getDayOfMonth());      //���� ��¥�� �������
	int nowTime = now.getHour();         //���� �ð��� ��� ����
	Vector<Vector<String>> list = new Vector<Vector<String>>();      //���̺��� ��������� ���� list
	
	public gitJava_library() {
		//main �����̳� ����
		main_con = this.getContentPane();
		main_frame = this;
		//������ main �����̳� ����
		main_lay_card = new CardLayout();
		this.setLayout(main_lay_card);
		
		BackGround backGroundThread = new BackGround();
		backGroundThread.start();
		
		main_con.add(new Login(), "Login");
				
		//���̾ƿ� ����
		main_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //gui����� main�� ����
		main_frame.setTitle("���͵�� ���� by.2021081104/����ȯ");
		main_frame.setIconImage(imageIcon.getImage()); //���α׷� ������ �̹��� ����
		main_frame.setResizable(false); //ȭ��ũ�� ����Ұ�
		main_frame.setSize(700,450); //ȭ�� ������ 800 x 500 ���� ����
		main_frame.setLocationRelativeTo(null); //�߾ӿ� ����
		main_frame.setVisible(true);
	}
	
	class Login extends JPanel{
		String loginId;
        String loginPw;
		Login() {
			login_tf_id = new JTextField();
			login_tf_id.setColumns(10);
			login_pf_pw = new JPasswordField();
			login_pf_pw.setColumns(10);
			
			add(new JLabel(logoImage),BorderLayout.NORTH);
			add(new JLabel("ID: "));
			add(login_tf_id);
			add(new JLabel("PASSWORD: "));
			add(login_pf_pw);
			JButton login_btn = new JButton("LOGIN");
			add(login_btn);
			JButton signup_btn = new JButton("ȸ������");
			add(signup_btn);
			
			login_btn.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					String id=login_tf_id.getText();
					String pw=login_pf_pw.getText();
					
					if (id.equals("admin")&&pw.equals("1234")) {
						main_con.add(new Tab(), "AdminMainPage");
						main_lay_card.show(main_con, "AdminMainPage");
					} else {
						try {
	                        Statement stmt=conn.createStatement();
	                        ResultSet rset = stmt.executeQuery("select * from stu where num='"+id+"' and pw='"+pw+"'");
	                        while(rset.next()) {
	                           loginId = String.valueOf(rset.getInt("num"));
	                           loginPw = rset.getString("pw");
	                        }
	                     } catch (SQLException ex) {
	                        JOptionPane.showMessageDialog(null, "�α��� ����","�α��� ����",JOptionPane.DEFAULT_OPTION);
	                     }
						if(id.equals(loginId) && pw.equals(loginPw)) {
							main_con.add(new UserMainPage(), "UserMainPage");
							main_lay_card.show(main_con, "UserMainPage");
	                     }else {
	                    	 login_tf_id.setText("");
	                    	 login_pf_pw.setText("");
	                         JOptionPane.showMessageDialog(null, "���̵�� ��й�ȣ�� �ٸ��ϴ�.","�α��� ����",JOptionPane.DEFAULT_OPTION);
	                     }
					}
				}
			});
			
			signup_btn.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					main_con.add(new Sign_up(), "Sign_up");
					main_lay_card.show(main_con, "Sign_up");
				}
			});
		}
	}
	
	class Sign_up extends JPanel{//ȸ������
	      
	      JTextField stu_id;
	      JTextField stu_name;
	      JPasswordField pass;
	      
	       Sign_up(){
	         setLayout(new BorderLayout(30,40));
	         
	         JLabel lb1=new JLabel(logoImage);
	         add(lb1,BorderLayout.NORTH);
	         
	         JPanel info=new JPanel();//ȸ������ �г� ����
	         info.setLayout(new GridLayout(4,4,0,20));
	         
	         info.add(new JLabel(""));
	         
	         JLabel name=new JLabel("�̸�");
	         name.setHorizontalAlignment(JLabel.CENTER);
	         info.add(name);
	         stu_name=new JTextField(15);
	         info.add(stu_name);
	         
	         info.add(new JLabel(""));
	         info.add(new JLabel(""));
	         
	         JLabel id=new JLabel("ID");
	         id.setHorizontalAlignment(JLabel.CENTER);
	         info.add(id);
	         stu_id=new JTextField(15);
	         info.add(stu_id);
	         
	         info.add(new JLabel(""));
	         info.add(new JLabel(""));
	         
	         JLabel pw=new JLabel("PASSWARD");
	         pw.setHorizontalAlignment(JLabel.CENTER);
	         info.add(pw);
	         pass=new JPasswordField(15); 
	         info.add(pass);
	         
	         info.add(new JLabel(""));
	         info.add(new JLabel(""));
	         
	         JLabel pw_check=new JLabel("��й�ȣ Ȯ��");
	         pw_check.setHorizontalAlignment(JLabel.CENTER);
	         info.add(pw_check);
	         JPasswordField pw_check_field=new JPasswordField(15);
	         info.add(pw_check_field);
	         
	         info.add(new JLabel(""));
	         
	         JPanel button=new JPanel();
	         JButton confirm=new JButton("Ȯ��");
	         JButton cancel=new JButton("���");
	         button.add(confirm);
	         button.add(cancel);
	         
	         confirm.addActionListener(new ActionListener() {
	            //Ȯ�� ��ư �׼�
	            @Override
	            public void actionPerformed(ActionEvent arg0) {
	               // TODO Auto-generated method stub
	               String password=pass.getText();
	               String password_check=pw_check_field.getText();
	               if(password.equals(password_check)) {
	                  String name=stu_name.getText();
	                  int id=Integer.parseInt(stu_id.getText());
	                  try {
	                     Statement stmt = conn.createStatement();
	                     
	                     String query = "insert into stu values("+id+",'"+name+"','"+password+"')";//�л�DB�� ����
	                     
	                     stmt.executeUpdate(query);
	                  } catch (SQLException e1) {
	                     // TODO: handle exception
	                     e1.printStackTrace();
	                  }
	                  JOptionPane.showMessageDialog(null, "ȸ�������� �Ϸ�Ǿ����ϴ�.","ȸ������ �Ϸ�",JOptionPane.DEFAULT_OPTION);
	                  main_lay_card.show(main_con, "Login");//�α��� ȭ������ ���ư���
	               }
	               else {
	                  pw_check_field.setText("");//��й�ȣ Ȯ���ϴ� �κ� �����
	                  JOptionPane.showMessageDialog(null, "��й�ȣ�� �ٽ� Ȯ���ϼ���.","ȸ������ ����",JOptionPane.DEFAULT_OPTION);
	               }
	            }
	         });//confirm.addActionListener(new ActionListener())
	         cancel.addActionListener(new ActionListener() {
	            //��� ��ư �׼�
	            @Override
	            public void actionPerformed(ActionEvent e) {
	               // TODO Auto-generated method stub
	               stu_name.setText("");
	               stu_id.setText("");
	               pass.setText("");
	               pw_check_field.setText("");//ȸ������ �κ� �ʱ�ȭ
	               JOptionPane.showMessageDialog(null, "ȸ�������� ��ҵǾ����ϴ�.","ȸ������ ���.",JOptionPane.DEFAULT_OPTION);
	               main_lay_card.show(main_con, "Login");//�α��� ȭ������ ���ư���
	            }
	         });
	         add(info,BorderLayout.CENTER);
	         add(button,BorderLayout.SOUTH);
	         setVisible(true);
	      }
	   }

	class Bookstudy extends JPanel{      //���͵�� �г� Ŭ���� ����
	       Vector<Vector<String>> lists = new Vector<Vector<String>>();
	        Vector<Vector<String>> booked1 = new Vector<Vector<String>>();
	        Vector<Vector<String>> booked2 = new Vector<Vector<String>>();
	        Vector<Vector<String>> booked3 = new Vector<Vector<String>>();
	        JPanel stPanel = new JPanel(new BorderLayout());//�߰�
	        JPanel btnPanel = new JPanel(new FlowLayout());   //��ư ���� �г�
	        JPanel labelPanel = new JPanel(new BorderLayout());   //�� �����г�
	        JPanel label2Panel = new JPanel(new FlowLayout(FlowLayout.RIGHT,5,0));
	        JComboBox dayCombo;      //���������¥
	        JComboBox timeCombo;      //��������ð�
	        Vector<String> stnl = new Vector<String>();
	        Vector<String> nownow;   //stnTable�� ���� ��
	        JTable stTable;      // ���� �ֱ����� ���̺� ����
	        JTable stnTable;      //���� ���¸� �ֱ����� ���̺� ����
	        JButton renew_btn = new JButton("����");
	        JButton book_btn = new JButton("�����ϱ�");
	        TableModel stnmodel;
	        String bookok = "���డ��";
	        String bookno = "����Ұ�";
	        boolean[] bookedbool1;
	      boolean[] bookedbool2;
	      boolean[] bookedbool3;
	      String[] timeList = {"09:00 ~ 10:00","10:00 ~ 11:00","11:00 ~ 12:00","12:00 ~ 13:00","13:00 ~ 14:00","14:00 ~ 15:00","15:00 ~ 16:00",
	               "16:00 ~ 17:00","17:00 ~ 18:00","18:00 ~ 19:00","19:00 ~ 20:00","20:00 ~ 21:00","21:00 ~ 22:00"};   //�޺��ڽ��ȿ� ���� �ð� ����Ʈ
	      String[] stHeader = {"�̸�","��", "�����ο�"};
	       String[][] stContents = new String[3][4];
	      
	        Bookstudy() {
	        	if(login_tf_id.getText().equals("admin")) {
	        		book_btn.setVisible(false);
	        	}
	           
	        	for(int i = 0; i<=4; i++) {
	                String dateform;
	                dateform = now.format(DateTimeFormatter.ofPattern("yyyy-MM-")).toString()+(now.getDayOfMonth()+i);
	                dayList[i] = dateform;
	                //���� ��¥�� ���� 5�Ϻ� ����
	             }
	            this.setLayout(new BorderLayout());
	            labelPanel.setBackground(Color.white);
	            labelPanel.setPreferredSize(new Dimension(700,30));
	            labelPanel.add(new JLabel(" �� 3��"),BorderLayout.WEST);
	            dayCombo = new JComboBox(dayList);      //���������¥ ����
	            dayCombo.setBackground(Color.WHITE);
	            timeCombo = new JComboBox(timeList);   //��������ð� ����
	            timeCombo.setBackground(Color.WHITE);
	            label2Panel.setBackground(Color.white);
	            label2Panel.add(new JLabel("������� ��¥"));
	            label2Panel.add(dayCombo);
	            label2Panel.add(new JLabel("������� �ð�"));
	            label2Panel.add(timeCombo);
	            renew_btn.setBackground(Color.WHITE);
	            renew_btn.addActionListener(new ActionListener() {   //JTable ���� ��ư
	            public void actionPerformed(ActionEvent e) {
	               
	               int roomNum;
	               bookedbool1 = new boolean[1];
	                   bookedbool2 = new boolean[1];
	                   bookedbool3 = new boolean[1];
	               try {
	                        int ad_book_day;
	                        String ad_book_day_DB="";
	                      String ad_book_day_iDB = dayCombo.getSelectedItem().toString();   //������ ���� �ð� �� ��������
	                      String[] adb = ad_book_day_iDB.split("-");
	                      for(int k=0; k<adb.length ; k++) {
	                         ad_book_day_DB += adb[k];
	                        }
	                      ad_book_day = Integer.parseInt(ad_book_day_DB);
	                      int ad_book_time = timeCombo.getSelectedIndex()+9;   //������ ���� ��¥ �� ��������
	                      
	                      setConn();      //�����ͺ��̽� ����
	                      Statement stmt1 = conn.createStatement();
	                      ResultSet srs1 = stmt1.executeQuery("select * from res where roomNum=1 and resDay ='"+ad_book_day+"'and resTime ='"+ad_book_time+"'");
	                      Statement stmt2 = conn.createStatement();
	                      ResultSet srs2 = stmt2.executeQuery("select * from res where roomNum=2 and resDay ='"+ad_book_day+"'and resTime ='"+ad_book_time+"'");
	                      Statement stmt3 = conn.createStatement();
	                      ResultSet srs3 = stmt3.executeQuery("select * from res where roomNum=3 and resDay ='"+ad_book_day+"'and resTime ='"+ad_book_time+"'");
	                      //���͵�뿡 ���� ������ �ٸ��� �ۼ��ϱ� ���� 3���� �ۼ�
	                      Vector<String> book1 = new Vector<String>();
	                      Vector<String> book2 = new Vector<String>();
	                      Vector<String> book3 = new Vector<String>();
	                      booked1.clear();
	                      booked2.clear();
	                      booked3.clear();
	                      while(srs1.next()) {
	                         bookedbool1[Integer.parseInt(String.valueOf(srs1.getString("roomNum")))-1] = true;   //roomNum�� ����ͼ� ���� �ִٸ� true ��ȯ
	                      }
	                      if(bookedbool1[0]) {      //true��� ������̱� ������ ����Ұ�
	                         book1.add(bookno);
	                         booked1.add(book1);
	                      }else {
	                         book1.add(bookok);      //false��� ������ ���⶧���� ���� ����
	                         booked1.add(book1);
	                      }
	                      
	                      while(srs2.next()) {
	                         bookedbool2[Integer.parseInt(String.valueOf(srs2.getString("roomNum")))-2] = true;
	                      }
	                      if(bookedbool2[0]) {
	                         book2.add(bookno);
	                         booked2.add(book2);
	                      }else {
	                         book2.add(bookok);
	                         booked2.add(book2);
	                      }
	                      
	                      while(srs3.next()) {
	                         bookedbool3[Integer.parseInt(String.valueOf(srs3.getString("roomNum")))-3] = true;
	                      }
	                      if(bookedbool3[0]) {
	                         book3.add(bookno);
	                         booked3.add(book3);
	                      }else {
	                         book3.add(bookok);
	                         booked3.add(book3);
	                      }
	                      
	                      
	                  } catch (Exception ee) {
	                      ee.printStackTrace();
	                  }
	               ((DefaultTableModel) stnmodel).setNumRows(0);      //JTable ����
	               ((DefaultTableModel) stnmodel).addRow(booked1);      //�࿡ �� ������� ä���
	               ((DefaultTableModel) stnmodel).addRow(booked2);
	               ((DefaultTableModel) stnmodel).addRow(booked3);
	               JOptionPane.showMessageDialog(null, "���ŵǾ����ϴ�.");
	            }
	         });
	            label2Panel.add(renew_btn);
	            labelPanel.add(label2Panel);
	            stPanel.add(labelPanel,BorderLayout.NORTH);      //�� �г��� �߰� �г� ��ܿ� ��ġ
	            
	            for (int i = 0; i < stContents.length; i++) {      //JTable���� ����� stContents�� ���� �־���
	                stContents[i][0]="���͵��"+(i+1);
	                stContents[i][1]="1��";
	                stContents[i][2]="4 ~ 8";
	            }
	            TableModel stmodel = new DefaultTableModel(stContents,stHeader) {
	                public boolean isCellEditable(int i, int c) {
	                     return false;
	                 }
	            };
	            stTable = new JTable(stmodel);      //���̺� ����
	            //stTable ���� �߾� ����
	            DefaultTableCellRenderer renderer = (DefaultTableCellRenderer)stTable.getDefaultRenderer(Object.class);
	            renderer.setHorizontalAlignment( SwingConstants.CENTER );

	            //stTable �� ������ ����
	            stTable.setRowHeight(60);
	            stTable.getColumnModel().getColumn(0).setPreferredWidth(70);
	            stTable.getColumnModel().getColumn(1).setPreferredWidth(50);
	            stTable.getColumnModel().getColumn(2).setPreferredWidth(80);
	            stTable.getTableHeader().setReorderingAllowed(false);
	            stTable.getTableHeader().setResizingAllowed(false);
	            JScrollPane stJs = new JScrollPane(stTable);
	            stPanel.add(stJs, BorderLayout.WEST);
	            int i =0;
	            int albookDay = nowDate;      //�񱳸� ���� ���� ��¥
	            int albookTime = nowTime;      //�񱳸� ���� ���� �ð�
	            boolean[] booked = new boolean[stroomNum.length];
	            try {
	               setConn();
	                Statement stmt = conn.createStatement();
	                ResultSet srs = stmt.executeQuery("select roomNum from res where resDay='"+albookDay+"' and resTime='"+albookTime+"'");      //ó�� ȭ���� ��밡�� ����
	                while(srs.next()) {
	                    booked[Integer.parseInt(String.valueOf(srs.getString("roomNum")))-1] = true;
	                }
	            }catch (Exception e){
	                  e.printStackTrace();
	            }
	            while(!(i==3)){
	                //���Ͱ����� �迭 nownow�� ����
	                nownow = new Vector<String>();
	                if(booked[i]) 
	                   nownow.add(bookno);
	                else 
	                   nownow.add(bookok);
	                lists.add(nownow);
	                i++;
	            }

	            stnl.add("������Ȳ");
	            
	            
	            stnmodel = new DefaultTableModel(lists,stnl) {      
	                public boolean isCellEditable(int i, int c) {
	                    return false;
	                }
	            };
	            stnTable = new JTable(stnmodel);
	            DefaultTableCellRenderer rendererer = (DefaultTableCellRenderer)stnTable.getDefaultRenderer(Object.class);
	            rendererer.setHorizontalAlignment( SwingConstants.CENTER );
	            stnTable.setRowHeight(60);
	            stnTable.getColumnModel().getColumn(0).setPreferredWidth(30);
	            stnTable.getTableHeader().setReorderingAllowed(false);
	            stnTable.getTableHeader().setResizingAllowed(false);
	            JScrollPane stnJs = new JScrollPane(stnTable);
	            //���̺� ���� �� ����
	            stPanel.add(stnJs, BorderLayout.CENTER);
	            book_btn.setBackground(Color.white);
	            
	            book_btn.addActionListener(new ActionListener() {      //�����ư�� ������ ������������ �̵�
	                public void actionPerformed(ActionEvent arg0) {
	                	main_con.add(new StudyBook_info(),"StudyBook_info");
	                    main_lay_card.show(main_con, "StudyBook_info");
	                }
	            });
	            
	            btnPanel.add(book_btn);
	            stPanel.add(btnPanel,BorderLayout.SOUTH);
	            this.add(stPanel);
	        }
	    }
	
	class StudyBook_info extends JPanel{
	       String htc1[] = {"09:00 ~ 10:00","10:00 ~ 11:00","11:00 ~ 12:00","12:00 ~ 13:00","13:00 ~ 14:00","14:00 ~ 15:00","15:00 ~ 16:00",
	                "16:00 ~ 17:00","17:00 ~ 18:00","18:00 ~ 19:00","19:00 ~ 20:00","20:00 ~ 21:00","21:00 ~ 22:00"};
	       int accNum;   //�����̿��� �й�
	        JPanel northP = new JPanel();      //��� �г�
	        JComboBox hope_studyr = new JComboBox(stroomNum);
	        JComboBox hope_day;
	        JComboBox hope_time = new JComboBox(htc1);
	        
	        JPanel centerP = new JPanel(new BorderLayout());      //�ߴ��г�
	        JPanel center_northP = new JPanel(new FlowLayout(FlowLayout.LEFT));   //�ߴ��г��� ����г�
	        JPanel center_centerP = new JPanel(new BorderLayout(5,5));   //�ߴ��г��� �ߴ��г�
	        JPanel center_center_leftP = new JPanel(new GridLayout(5,1));      //�ߴ��г��� �ߴ��г��� �����г�
	        JPanel center_center_centerP = new JPanel(new GridLayout(5,1));   //�ߴ� �г��� �ߴ��г��� �ߴ��г�
	        JPanel center_center_rightP = new JPanel(new GridLayout(5,1));
	        JPanel center_south = new JPanel();   //�ߴ��г��� �ϴ��г�
	        JPanel southP = new JPanel();      //�ϴ��г�
	        JButton add_btn = new JButton("�߰�");
	        JButton submit_btn = new JButton("����");
	        JButton cancel_btn = new JButton("���");
	        JTextField withname_tf = new JTextField();
	        JTextField withnum_tf = new JTextField();
	        JTextField withlist_tf = new JTextField(" ");
	        String withNum_compare;      //stu�� �񱳸����� �й�
	        String withName_compare;   //stu�� �񱳸� ���� �̸�   
	        boolean[] selec_bool1;         //���డ�ɿ��θ� ��Ÿ���� boolean �迭
	      boolean[] selec_bool2;
	      boolean[] selec_bool3;
	        StudyBook_info(){
	            hope_day = new JComboBox(dayList);
	           hope_studyr.setBackground(Color.WHITE);
	           hope_day.setBackground(Color.WHITE);
	           hope_time.setBackground(Color.WHITE);
	            setLayout(new BorderLayout());
	            
	            //���������� *******************************************************
	            northP.add(new JLabel(logoImage),BorderLayout.NORTH);      //����гο��� �ΰ� ����
	            JLabel studyr_label = new JLabel("��� ���͵��");
	            center_northP.add(studyr_label);
	            center_northP.add(hope_studyr);   
	            //�ߴ��� ����г�
	            centerP.add(center_northP , BorderLayout.NORTH);
	            submit_btn.setBackground(Color.white);
	            cancel_btn.setBackground(Color.white);
	            add_btn.setBackground(Color.white);
	            JLabel hdLabel = new JLabel("����������� ��");
	            JLabel htLabel = new JLabel("��������ð� ��");
	            JLabel withnameLabel = new JLabel("�����̿��� �̸� ��");
	            JLabel withnumLabel = new JLabel("�����̿��� �й� ��");
	            JLabel withList = new JLabel("�����̿��� ��� >");
	            withlist_tf.setBackground(Color.white);
	            withlist_tf.setEditable(false);

	            JLabel nulllabel[] = new JLabel[4];
	            for(int i =0; i<nulllabel.length; i++) {
	                nulllabel[i] = new JLabel(" ");
	            }

	            center_center_leftP.add(hdLabel);
	            center_center_leftP.add(hope_day);
	            center_center_leftP.add(withnameLabel);
	            center_center_leftP.add(withname_tf);
	            center_center_leftP.add(withList);

	            center_center_centerP.add(htLabel);
	            center_center_centerP.add(hope_time);
	            center_center_centerP.add(withnumLabel);
	            center_center_centerP.add(withnum_tf);
	            center_center_centerP.add(withlist_tf);
	            
	            center_centerP.add(center_center_leftP, BorderLayout.WEST);
	            center_centerP.add(center_center_centerP, BorderLayout.CENTER);
	            
	            center_center_rightP.add(nulllabel[0]);
	            center_center_rightP.add(nulllabel[1]);
	            center_center_rightP.add(nulllabel[2]);
	            center_center_rightP.add(add_btn);
	            center_center_rightP.add(nulllabel[3]);
	            
	            center_centerP.add(center_center_rightP,BorderLayout.EAST);
	            centerP.add(center_centerP, BorderLayout.CENTER);
	            selec_bool1 = new boolean[1];
	            selec_bool2 = new boolean[1];
	            selec_bool3 = new boolean[1];
	           
	            submit_btn.addActionListener(new  ActionListener() {      //���� ��ư�� ���� �� �۵�
	                public void actionPerformed(ActionEvent e) {
	                    if(withlist_tf.getText().equals(" ")) {
	                        hope_day.setSelectedIndex(0);
	                        hope_time.setSelectedIndex(0);
	                        hope_studyr.setSelectedIndex(0);
	                        JOptionPane.showMessageDialog(null,"��û �ο��̾����ϴ�.");
	                        String listPeo = withlist_tf.getText();
	                        String [] listPeonum = listPeo.split(",");
	                        System.out.println(listPeonum.length);
	                        listname =" ";

	                    }else {
	                        try {
	                            setConn();
	                            String room1 = "���͵��1";      //���͵���� �����ϱ� ���� ����
	                            String room2 = "���͵��2";
	                            String room3 = "���͵��3";
	                            Statement stmt = conn.createStatement();
	                          int selec_day;   //������ ���� ��¥ �� ��������
	                          String selec_day_DB="";
	                          String selec_day_iDB = hope_day.getSelectedItem().toString();
	                          String[] sd1 = selec_day_iDB.split("-");
	                            for(int k=0; k<sd1.length ; k++) {
	                               selec_day_DB += sd1[k];
	                            }
	                            selec_day = Integer.parseInt(selec_day_DB);
	                            int selec_time = hope_time.getSelectedIndex()+9;
	                            
	                          Statement stmtS1 = conn.createStatement();
	                          ResultSet srsS1 = stmtS1.executeQuery("select * from res where roomNum=1 and resDay ='"+selec_day+"'and resTime ='"+selec_time+"'");
	                          Statement stmtS2 = conn.createStatement();
	                          ResultSet srsS2 = stmtS2.executeQuery("select * from res where roomNum=2 and resDay ='"+selec_day+"'and resTime ='"+selec_time+"'");
	                          Statement stmtS3 = conn.createStatement();
	                          ResultSet srsS3 = stmtS3.executeQuery("select * from res where roomNum=3 and resDay ='"+selec_day+"'and resTime ='"+selec_time+"'");
	                            int repStuNum = Integer.parseInt(login_tf_id.getText());
	                            int roomNum = 0;
	                            int resDay;
	                            int resTime;
	                            String resTimes;
	                            String resState = "�����";   //�����, �����, �������, ���
	                            Statement stmtS4 = conn.createStatement();
		                        ResultSet srsS4 = stmtS4.executeQuery("select name from stu where num="+repStuNum);
		                        srsS4.next();
	                            String repStuName = srsS4.getString("name");
	                            stmtS4.close();
	                            int accStuNum1;
	                            Statement stmtS5 = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY );
		                        ResultSet srsS5 = stmtS5.executeQuery("select resNum from res");
		                        srsS5.last();
	                            int resNum=srsS5.getRow();
	                            stmtS5.close();
	                            Statement stmtS6 = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY );
		                        ResultSet srsS6 = stmtS6.executeQuery("select resNum from cancle");
		                        srsS6.last();
	                            resNum+=srsS6.getRow()+1;
	                            stmtS6.close();
	                            String dayDB ="";
	                            String dayiDB;
	                            String splitlength[] = withlist_tf.getText().split(",");
	                            accNum = splitlength.length;
	                            int accStuCnt = accNum-1;
	                            if((hope_studyr.getSelectedItem().toString() == room1) && (selec_day  == selec_day) && ((hope_time.getSelectedIndex()+9) == selec_time)){
	                               while(srsS1.next()) {
	                                  selec_bool1[Integer.parseInt(String.valueOf(srsS1.getString("roomNum")))-1] = true;   //roomNum�� ����ͼ� ���� �ִٸ� true ��ȯ
	                            }
	                               if(selec_bool1[0]) {      //true��� ������̱� ������ ����Ұ�
	                                 JOptionPane.showMessageDialog(null, "�̹� �������� ���͵���Դϴ�.");
	                              }else if(nowDate < selec_day){                  //false��� ������ ���⶧���� ���� ����
	                                    roomNum = 1;
	                                     dayiDB = hope_day.getSelectedItem().toString();
//	                                        System.out.println(dayiDB);
	                                        
	                                        String[] sp1 = dayiDB.split("-");
	                                        for(int k=0; k<sp1.length ; k++) {
	                                           dayDB += sp1[k];
	                                        }
	                                        resDay = Integer.parseInt(dayDB);
	                                        accStuNum1 = accNum-1;
	                                        resTime = hope_time.getSelectedIndex()+9;
	                                        resTimes = hope_time.getSelectedItem().toString();
	                                        //repStuNum ���� �α��� ������ ��������*************************************************************************************************
//	                                        System.out.println("insert into res values('"+resNum+"','"+roomNum+"','"+resDay+"','"+resTime+"','"+repStuName+"','"+repStuNum+"','"+resState+"','"+accStuCnt);
	                                        stmt.execute("insert into res values('"+resNum+"','"+roomNum+"','"+resDay+"','"+resTime+"','"+repStuName+"','"+repStuNum+"','"+resState+"','"+accStuCnt+"')");
	                                        sizeLabel.setText("�� "+list.size()+"��");
	                                        JOptionPane.showMessageDialog(null,"��û�Ǽ̽��ϴ�.");
	                              }else if(nowDate == selec_day) {
	                                 if(nowTime <= selec_time) {
	                                    roomNum = 1;
	                                     dayiDB = hope_day.getSelectedItem().toString();
//	                                        System.out.println(dayiDB);
	                                        
	                                        String[] sp1 = dayiDB.split("-");
	                                        for(int k=0; k<sp1.length ; k++) {
	                                           dayDB += sp1[k];
	                                        }
	                                        resDay = Integer.parseInt(dayDB);
	                                        accStuNum1 = accNum-1;
	                                        resTime = hope_time.getSelectedIndex()+9;
	                                        resTimes = hope_time.getSelectedItem().toString();
	                                        //repStuNum ���� �α��� ������ ��������*************************************************************************************************
	                                        
//	                                        System.out.println("insert into res values('"+resNum+"','"+roomNum+"','"+resDay+"','"+resTime+"','"+repStuName+"','"+repStuNum+"','"+resState+"','"+accStuCnt);
	                                        stmt.execute("insert into res values('"+resNum+"','"+roomNum+"','"+resDay+"','"+resTime+"','"+repStuName+"','"+repStuNum+"','"+resState+"','"+accStuCnt+"')");
	                                        sizeLabel.setText("�� "+list.size()+"��");
	                                        JOptionPane.showMessageDialog(null,"��û�Ǽ̽��ϴ�.");
	                                 }else {
	                                    JOptionPane.showMessageDialog(null, "��û �ð��� �������ϴ�.");
	                                 }
	                              }else{
	                                 JOptionPane.showMessageDialog(null, "��û �ð��� �������ϴ�.");
	                              }
	                          }
	                            
	                            if((hope_studyr.getSelectedItem().toString() == room2) && (selec_day  == selec_day) && ((hope_time.getSelectedIndex()+9) == selec_time)){
	                               while(srsS2.next()) {
	                                  selec_bool2[Integer.parseInt(String.valueOf(srsS2.getString("roomNum")))-2] = true;   //roomNum�� ����ͼ� ���� �ִٸ� true ��ȯ
	                            }
	                               if(selec_bool2[0]) {      //true��� ������̱� ������ ����Ұ�
	                                 JOptionPane.showMessageDialog(null, "�̹� �������� ���͵���Դϴ�.");
	                              }else if(nowDate < selec_day){                  //false��� ������ ���⶧���� ���� ����
	                                roomNum = 2;
	                                 dayiDB = hope_day.getSelectedItem().toString();
//	                                    System.out.println(dayiDB);
	                                    
	                                    String[] sp1 = dayiDB.split("-");
	                                    for(int k=0; k<sp1.length ; k++) {
	                                       dayDB += sp1[k];
	                                    }
	                                    resDay = Integer.parseInt(dayDB);
	                                    accStuNum1 = accNum-1;
	                                    resTime = hope_time.getSelectedIndex()+9;
	                                    resTimes = hope_time.getSelectedItem().toString();
	                                    //repStuNum ���� �α��� ������ ��������*************************************************************************************************
	                                    
//	                                    System.out.println("insert into res values('"+resNum+"','"+roomNum+"','"+resDay+"','"+resTime+"','"+repStuName+"','"+repStuNum+"','"+resState+"','"+accStuCnt);
                                        stmt.execute("insert into res values('"+resNum+"','"+roomNum+"','"+resDay+"','"+resTime+"','"+repStuName+"','"+repStuNum+"','"+resState+"','"+accStuCnt+"')");
	                                    sizeLabel.setText("�� "+list.size()+"��");
	                                    JOptionPane.showMessageDialog(null,"��û�Ǽ̽��ϴ�.");
	                          }else if(nowDate == selec_day) {
	                             if(nowTime <= selec_time) {
	                                roomNum = 2;
	                                 dayiDB = hope_day.getSelectedItem().toString();
//	                                    System.out.println(dayiDB);
	                                    
	                                    String[] sp1 = dayiDB.split("-");
	                                    for(int k=0; k<sp1.length ; k++) {
	                                       dayDB += sp1[k];
	                                    }
	                                    resDay = Integer.parseInt(dayDB);
	                                    accStuNum1 = accNum-1;
	                                    resTime = hope_time.getSelectedIndex()+9;
	                                    resTimes = hope_time.getSelectedItem().toString();
	                                    //repStuNum ���� �α��� ������ ��������*************************************************************************************************
	                                    
//	                                    System.out.println("insert into res values('"+resNum+"','"+roomNum+"','"+resDay+"','"+resTime+"','"+repStuName+"','"+repStuNum+"','"+resState+"','"+accStuCnt);
                                        stmt.execute("insert into res values('"+resNum+"','"+roomNum+"','"+resDay+"','"+resTime+"','"+repStuName+"','"+repStuNum+"','"+resState+"','"+accStuCnt+"')");
	                                    sizeLabel.setText("�� "+list.size()+"��");
	                                    JOptionPane.showMessageDialog(null,"��û�Ǽ̽��ϴ�.");
	                             }else {
	                                JOptionPane.showMessageDialog(null, "��û �ð��� �������ϴ�.");
	                             }
	                          }else{
	                             JOptionPane.showMessageDialog(null, "��û �ð��� �������ϴ�.");
	                          }
	                          }
	                            if((hope_studyr.getSelectedItem().toString() == room3) && (selec_day  == selec_day) && ((hope_time.getSelectedIndex()+9) == selec_time)){
	                               while(srsS3.next()) {
	                                  selec_bool3[Integer.parseInt(String.valueOf(srsS1.getString("roomNum")))-3] = true;   //roomNum�� ����ͼ� ���� �ִٸ� true ��ȯ
	                            }
	                               if(selec_bool3[0]) {      //true��� ������̱� ������ ����Ұ�
	                                 JOptionPane.showMessageDialog(null, "�̹� �������� ���͵���Դϴ�.");
	                              }else if(nowDate < selec_day){                  //false��� ������ ���⶧���� ���� ����
	                                roomNum = 3;
	                                 dayiDB = hope_day.getSelectedItem().toString();
//	                                    System.out.println(dayiDB);
	                                    
	                                    String[] sp1 = dayiDB.split("-");
	                                    for(int k=0; k<sp1.length ; k++) {
	                                       dayDB += sp1[k];
	                                    }
	                                    resDay = Integer.parseInt(dayDB);
	                                    accStuNum1 = accNum-1;
	                                    resTime = hope_time.getSelectedIndex()+9;
	                                    resTimes = hope_time.getSelectedItem().toString();
	                                    //repStuNum ���� �α��� ������ ��������*************************************************************************************************
	                                    
//	                                    System.out.println("insert into res values('"+resNum+"','"+roomNum+"','"+resDay+"','"+resTime+"','"+repStuName+"','"+repStuNum+"','"+resState+"','"+accStuCnt);
                                        stmt.execute("insert into res values('"+resNum+"','"+roomNum+"','"+resDay+"','"+resTime+"','"+repStuName+"','"+repStuNum+"','"+resState+"','"+accStuCnt+"')");
	                                    sizeLabel.setText("�� "+list.size()+"��");
	                                    JOptionPane.showMessageDialog(null,"��û�Ǽ̽��ϴ�.");
	                          }else if(nowDate == selec_day) {
	                             if(nowTime <= selec_time) {
	                                roomNum = 3;
	                                 dayiDB = hope_day.getSelectedItem().toString();
//	                                    System.out.println(dayiDB);
	                                    
	                                    String[] sp1 = dayiDB.split("-");
	                                    for(int k=0; k<sp1.length ; k++) {
	                                       dayDB += sp1[k];
	                                    }
	                                    resDay = Integer.parseInt(dayDB);
	                                    accStuNum1 = accNum-1;
	                                    resTime = hope_time.getSelectedIndex()+9;
	                                    resTimes = hope_time.getSelectedItem().toString();
	                                    //repStuNum ���� �α��� ������ ��������*************************************************************************************************
	                                    
//	                                    System.out.println("insert into res values('"+resNum+"','"+roomNum+"','"+resDay+"','"+resTime+"','"+repStuName+"','"+repStuNum+"','"+resState+"','"+accStuCnt);
                                        stmt.execute("insert into res values('"+resNum+"','"+repStuNum+"','"+roomNum+"','"+resDay+"','"+resTime+"','"+resState+"','"+accStuNum1+"')");
	                                    sizeLabel.setText("�� "+list.size()+"��");
	                                    JOptionPane.showMessageDialog(null,"��û�Ǽ̽��ϴ�.");
	                             }else {
	                                JOptionPane.showMessageDialog(null, "��û �ð��� �������ϴ�.");
	                             }
	                          }else{
	                             JOptionPane.showMessageDialog(null, "��û �ð��� �������ϴ�.");
	                          }
	                          }

	                            stmt.close();
	                            
	                            listname = " ";
	                            withlist_tf.setText(" ");
	                            withlist_tf.setText(listname);
	                            main_lay_card.show(main_con, "UserMainPage");
	                            hope_day.setSelectedIndex(0);
	                            hope_time.setSelectedIndex(0);
	                            hope_studyr.setSelectedIndex(0);

	                        } catch (NullPointerException ee2) {
	                        	main_lay_card.show(main_con, "UserMainPage");
	                        } catch (Exception ee) {
	                            ee.printStackTrace();
	                        }
	                    }
	                }
	            });   
	            cancel_btn.addActionListener(new ActionListener() {      //��� ��ư�� ������ ���� �ʱ�ȭ�ϰ� ������������ ���ư�
	                public void actionPerformed(ActionEvent e) {
	                    hope_day.setSelectedIndex(0);
	                    hope_time.setSelectedIndex(0);
	                    hope_studyr.setSelectedIndex(0);
	                    withnum_tf.setText("");
	                    withname_tf.setText("");
	                    listname = " ";
	                    withlist_tf.setText(listname);
	                    main_lay_card.show(main_con, "UserMainPage");
	                }
	            });
	            
	             
	            add_btn.addActionListener(new ActionListener() {      //�л� �߰� ��ư
	                public void actionPerformed(ActionEvent arg0) {
	                   String getNum = withnum_tf.getText().toString();
	                   String getName = withname_tf.getText().toString();
	                   try {
	                    setConn();
	                    Statement stmtstu = conn.createStatement();
	                    ResultSet srsstu = stmtstu.executeQuery("select num,name from stu where num='"+getNum+"' and name='"+getName+"'");
	                    
	                    while(srsstu.next()) {
	                       withNum_compare = String.valueOf(srsstu.getInt("num"));
	                       withName_compare = String.valueOf(srsstu.getString("name"));
	                       
	                       }
	                 } catch (Exception e2) {
	                    e2.printStackTrace();
	                 }
	                   //stu �����Ϳ� �ִ� �л��� �ƴϸ� �߰� �Ұ�
	               if(withnum_tf.getText().equals(withNum_compare) && withname_tf.getText().equals(withName_compare)) {
	                  listname += (withNum_compare + withName_compare + ", ");
	                  accNum = Integer.parseInt(withNum_compare);
	                       withlist_tf.setText(listname);
	                       JOptionPane.showMessageDialog(null, "�߰��Ǿ����ϴ�.");
	                       withnum_tf.setText("");
	                       withname_tf.setText("");
	                           //��Ͽ� �߰��ϰ� ���� ������ڴ� ��û ������ �����ͺ��̽��� ����
	                   }else {
	                      JOptionPane.showMessageDialog(null, "�й�,�̸� �߸��Է�");
	                       withnum_tf.setText("");
	                       withname_tf.setText("");
	                   }
	            
	                   
	                       
	                       
	                }
	            });

	            southP.setLayout(new FlowLayout());
	            southP.add(submit_btn);
	            southP.add(cancel_btn);
	            add(northP, BorderLayout.NORTH);
	            add(centerP, BorderLayout.CENTER);
	            add(southP, BorderLayout.SOUTH);
	        }
	    }
	
	class Tab extends JPanel{//������
		JTabbedPane t = new JTabbedPane();  //JTabbedPane����
		
		Tab(){
			this.setLayout(new BorderLayout());
			add(new JLabel(adminLogo),BorderLayout.NORTH);//��� �̹��� �ֱ�
			
			AdminMainPage amp = new AdminMainPage();
			Thread thread = new Thread(amp);
			thread.start();
			
			User user = new User();
			Thread thread2 = new Thread(user);
			thread2.start();
			
			t.add("���͵��", new Bookstudy());
			t.add("���೻��",amp);
			t.add("������Ȳ",user);
			
			this.add(t);
		}
	}

	class AdminMainPage extends JPanel implements Runnable{
		String[] header = {"����","����","���� �̿��ڼ�","���ð�","�̸�","�й�","����"};
		DefaultTableModel model = new DefaultTableModel(header,0){
	        public boolean isCellEditable(int i, int c){
	        	return false;
	        }
	    };
	    
	    JLabel cntJLabel = new JLabel("�� 0��");//�� ���
	    String[] searchCondition = {"�й�","�̸�","�Ͻ�"}; 
	    JPanel northJPanel = new JPanel(new BorderLayout());//��ܿ� ��ġ�� �г�
		JPanel northLeftJPanel = new JPanel(new FlowLayout(FlowLayout.LEFT,10,10));//��� ����
	    
		JPanel searchJPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT,10,10));//��� ���� �˻����� �޺��ڽ�, �ؽ�Ʈ �ʵ�, ��ȸ��ư
		JComboBox<String> searchBox = new JComboBox<String>(searchCondition);//�˻� ���� �޺��ڽ�
		JTextField searchField = new JTextField();//�˻�����
		
	    JScrollPane tableJScrollPane;
	    resTable table;
	    
	    String query = "";
	    String queryCancle = "";
		
		AdminMainPage(){
			this.setLayout(new BorderLayout());
			
			this.add(northJPanel,BorderLayout.NORTH);
			
			northJPanel.add(searchJPanel,BorderLayout.EAST);
			northJPanel.add(northLeftJPanel,BorderLayout.WEST);
			northJPanel.setBackground(Color.white);
			
			northLeftJPanel.add(cntJLabel);
			northLeftJPanel.setBackground(Color.white);
			
			searchJPanel.add(new JLabel("�˻����� "));
			searchJPanel.add(searchBox);
			searchJPanel.add(searchField);
			searchField.setColumns(10);
			searchJPanel.setBackground(Color.white);
			
			table = new resTable(model);
			tableJScrollPane = new JScrollPane(table);
			this.add(tableJScrollPane,BorderLayout.CENTER);
			
			tableAdd();

		}//������
		
		void tableAdd() {
			try {
				Statement stmt = conn.createStatement();
				ResultSet srs;
				if(query.equals(""))
					srs = stmt.executeQuery("select * from res");
				else
					srs = stmt.executeQuery(query);
				
				int cnt=1;
				
				while(srs.next()) {
					String resState = srs.getString("resState");
					if(resState.equals("�����") || resState.equals("�����"))
						continue;
					
					int resDay = srs.getInt("resDay");
					int resTime = srs.getInt("resTime");
		
					model.addRow(new Object[] {
							cnt,
							"���͵��"+srs.getInt("roomNum"),
							srs.getInt("accStuCnt")+"��",
							resDay/10000+"/"+String.format("%02d",(resDay/100)%100)+"/"+String.format("%02d",resDay%100)+" "+String.format("%02d",resTime)+":00 ~ "+String.format("%02d",(resTime+1))+":00",
							srs.getString("repStuName"),
							srs.getInt("repStuNum"),
							resState
					});
					cnt++;
				}
				
				Statement stmtCancle = conn.createStatement();
				ResultSet srsCancle;
				if(queryCancle.equals(""))
					srsCancle = stmtCancle.executeQuery("select * from cancle");
				else
					srsCancle = stmtCancle.executeQuery(queryCancle);
				
				while(srsCancle.next()) {
					int resDay = srsCancle.getInt("resDay");
					int resTime = srsCancle.getInt("resTime");
					
					model.addRow(new Object[] {
							cnt,
							"���͵��"+srsCancle.getInt("roomNum"),
							srsCancle.getInt("accStuCnt")+"��",
							resDay/10000+"/"+String.format("%02d",(resDay/100)%100)+"/"+String.format("%02d",resDay%100)+" "+String.format("%02d",resTime)+":00 ~ "+String.format("%02d",(resTime+1))+":00",
							srsCancle.getString("repStuName"),
							srsCancle.getInt("repStuNum"),
							srsCancle.getString("resState")
					});
					cnt++;
				}
				
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		void tableUpdate() {
			model.setNumRows(0);
			
//			�й� �̸� �Ͻ�
			if(!searchField.getText().equals("")) {
				
				switch (searchBox.getSelectedIndex()) {
				case 0: 
					query="select * from res where repStuNum = "+searchField.getText(); 
					queryCancle="select * from cancle where repStuNum = "+searchField.getText(); break;
				case 1: 
					query="select * from res where repStuName = '"+searchField.getText()+"'"; 
					queryCancle="select * from cancle where repStuName = '"+searchField.getText()+"'"; break;
				case 2: 
					query="select * from res where resDay = "+searchField.getText(); 
					queryCancle="select * from cancle where resDay = "+searchField.getText(); break;
				default:
					break;
				}
			}else {
				query="";
				queryCancle="";
			}
			
			tableAdd();

			cntJLabel.setText("�� "+model.getRowCount()+"�� �����ð� : "+now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			while(true) {
				tableUpdate();

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
	}//Ŭ����
	
	class ResCancle extends JDialog {
		JButton noBtn = new JButton("���");
		JButton okBtn = new JButton("Ȯ��");
		JPanel labelJPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,10,10));
		JPanel btnJPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,10,10));
		JLabel resInfoJLabel;
		
		public ResCancle(String info, String cancleQuery) {
			super(main_frame,"�������");
			
			//���̾�α� ����
			setLayout(new BorderLayout());
			setIconImage(imageIcon.getImage()); //���α׷� ������ �̹��� ����
			setResizable(false); //ȭ��ũ�� ����Ұ�
			setSize(350,125); //ȭ�� ������ 800 x 500 ���� ����
			setLocationRelativeTo(null); //�߾ӿ� ����
			
			//������Ʈ �߰�
			resInfoJLabel = new JLabel(info);
			add(labelJPanel,BorderLayout.CENTER);
			labelJPanel.add(resInfoJLabel);
			labelJPanel.add(new JLabel("����Ͻðڽ��ϱ�?"));
			add(btnJPanel,BorderLayout.SOUTH);
			btnJPanel.add(okBtn);
			btnJPanel.add(noBtn);
			
			noBtn.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					setVisible(false);//Ȯ��â ������ ���̾˷α� ���������
				}
			});
			
			okBtn.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					try {
						Statement stmt = conn.createStatement();
						ResultSet srs = stmt.executeQuery(cancleQuery);
						
						while(srs.next()) {
							Statement stmtCancle = conn.createStatement();
							stmtCancle.executeUpdate("insert into cancle values("+srs.getInt("resNum")+","+srs.getInt("roomNum")+","+srs.getInt("resDay")+","+srs.getInt("resTime")+",'"+srs.getString("repStuName")+"',"+srs.getInt("repStuNum")+",'"+srs.getString("resState")+"',"+srs.getInt("accStuCnt")+")");
							
							Statement stmtDelet = conn.createStatement();
							stmtDelet.executeUpdate("delete from res where resNum="+srs.getInt("resNum"));
						}
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					setVisible(false);//Ȯ��â ������ ���̾˷α� ���������
				}
			});
			
		}
	}
	
	class User extends JPanel implements Runnable{
		String[] header = {"����","����","���� �̿��ڼ�","����ð�","�̸�","�й�","����"};
		DefaultTableModel model = new DefaultTableModel(header,0){
	        public boolean isCellEditable(int i, int c){
	        	return false;
	        }
	    };
	    
	    JLabel cntJLabel = new JLabel("�� 0��");//�� ���
		JPanel northLeftJPanel = new JPanel(new FlowLayout(FlowLayout.LEFT,10,10));//��� ����
	    
	    JScrollPane tableJScrollPane;
	    resTable table;
	    int click;
	    User(){
			this.setLayout(new BorderLayout());
			
			northLeftJPanel.add(cntJLabel);
			this.add(northLeftJPanel,BorderLayout.NORTH);
			northLeftJPanel.setBackground(Color.white);
			
			table = new resTable(model);
			tableJScrollPane = new JScrollPane(table);
			this.add(tableJScrollPane,BorderLayout.CENTER);
			
			tableAdd();

			table.addMouseListener(new MouseListener() {
				
				@Override
				public void mouseReleased(MouseEvent e) {}
				
				@Override
				public void mousePressed(MouseEvent e) {}
				
				@Override
				public void mouseExited(MouseEvent e) {}
				
				@Override
				public void mouseEntered(MouseEvent e) {}
				
				@Override
				public void mouseClicked(MouseEvent e) {
					// TODO Auto-generated method stub
					click =table.getSelectedRow();
					
					if (click!=-1) {
						String info = "";
						String cancleQuery ="";
						info=model.getValueAt(click, 1)+" "+model.getValueAt(click, 3);
						String[] split = model.getValueAt(click, 3).toString().split("/");
						String[] split2 = split[2].split(" ");
						cancleQuery="select * from res where repStuNum = "+model.getValueAt(click, 5)+" and resDay="+split[0]+split[1]+split2[0]+" and resTime="+split2[1].charAt(0)+split2[1].charAt(1);
						ResCancle dialog = new ResCancle(info,cancleQuery);
						dialog.setVisible(true);
					}else {
//						System.out.println("Ŭ�� �������� �߻�");
					}
					
				}
			});

		}//������
		
		void tableAdd() {
			try {
				Statement stmt = conn.createStatement();
				ResultSet srs = stmt.executeQuery("select * from res");
				
				int cnt=1;
				while(srs.next()) {
					String resState = srs.getString("resState");
					if(resState.equals("���Ϸ�") || resState.equals("�������"))
						continue;
					int resDay = srs.getInt("resDay");
					int resTime = srs.getInt("resTime");
		
					model.addRow(new Object[] {
							cnt,
							"���͵��"+srs.getInt("roomNum"),
							srs.getInt("accStuCnt")+"��",
							resDay/10000+"/"+String.format("%02d",(resDay/100)%100)+"/"+String.format("%02d",resDay%100)+" "+String.format("%02d",resTime)+":00 ~ "+String.format("%02d",(resTime+1))+":00",
							srs.getString("repStuName"),
							srs.getInt("repStuNum"),
							srs.getString("resState")
					});
					cnt++;
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		void tableUpdate() {
			model.setNumRows(0);
			
			tableAdd();
			
			cntJLabel.setText("�� "+model.getRowCount()+"��");
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			while(true) {
				tableUpdate();

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
	}//Ŭ����
	
	class UserMainPage extends JPanel{
		JTabbedPane t = new JTabbedPane();  //JTabbedPane����
		
		UserMainPage(){
			this.setLayout(new BorderLayout());
			add(new JLabel(logoImage),BorderLayout.NORTH);//��� �̹��� �ֱ�
			
			UserAdminMainPage uamp = new UserAdminMainPage();
			Thread thread = new Thread(uamp);
			thread.start();
			
			UserUser uuser = new UserUser();
			Thread thread2 = new Thread(uuser);
			thread2.start();
			
			t.add("���͵��", new Bookstudy());
			t.add("���೻��",uamp);
			t.add("������Ȳ",uuser);
			
			this.add(t);
		}
	}
	
	class UserAdminMainPage extends JPanel implements Runnable{
		String[] header = {"����","����","���� �̿��ڼ�","���ð�","�̸�","�й�","����"};
		DefaultTableModel model = new DefaultTableModel(header,0){
	        public boolean isCellEditable(int i, int c){
	        	return false;
	        }
	    };
	    
	    JLabel cntJLabel = new JLabel("�� 0��");//�� ���
	    JPanel northJPanel = new JPanel(new BorderLayout());//��ܿ� ��ġ�� �г�
		JPanel northLeftJPanel = new JPanel(new FlowLayout(FlowLayout.LEFT,10,10));//��� ����
		
	    JScrollPane tableJScrollPane;
	    resTable table;
		
	    UserAdminMainPage(){
			this.setLayout(new BorderLayout());
			
			this.add(northJPanel,BorderLayout.NORTH);
			
			northJPanel.add(northLeftJPanel,BorderLayout.WEST);
			northJPanel.setBackground(Color.white);
			
			northLeftJPanel.add(cntJLabel);
			northLeftJPanel.setBackground(Color.white);
			
			table = new resTable(model);
			tableJScrollPane = new JScrollPane(table);
			this.add(tableJScrollPane,BorderLayout.CENTER);
			
			tableAdd();

		}//������
		
		void tableAdd() {
			try {
				Statement stmt = conn.createStatement();
				ResultSet srs = stmt.executeQuery("select * from res where repStuNum ="+login_tf_id.getText());
				
				int cnt=1;
				while(srs.next()) {
					String resState = srs.getString("resState");
					if(resState.equals("�����") || resState.equals("�����"))
						continue;
					int resDay = srs.getInt("resDay");
					int resTime = srs.getInt("resTime");
		
					model.addRow(new Object[] {
							cnt,
							"���͵��"+srs.getInt("roomNum"),
							srs.getInt("accStuCnt")+"��",
							resDay/10000+"/"+String.format("%02d",(resDay/100)%100)+"/"+String.format("%02d",resDay%100)+" "+String.format("%02d",resTime)+":00 ~ "+String.format("%02d",(resTime+1))+":00",
							srs.getString("repStuName"),
							srs.getInt("repStuNum"),
							resState
					});
					cnt++;
				}
				
				Statement stmtCancle = conn.createStatement();
				ResultSet srsCancle = stmtCancle.executeQuery("select * from cancle where repStuNum ="+login_tf_id.getText());
				
				while(srsCancle.next()) {
	               int resDay = srsCancle.getInt("resDay");
	               int resTime = srsCancle.getInt("resTime");
	               
	               model.addRow(new Object[] {
	                     cnt,
	                     "���͵��"+srsCancle.getInt("roomNum"),
	                     srsCancle.getInt("accStuCnt")+"��",
	                     resDay/10000+"/"+String.format("%02d",(resDay/100)%100)+"/"+String.format("%02d",resDay%100)+" "+String.format("%02d",resTime)+":00 ~ "+String.format("%02d",(resTime+1))+":00",
	                     srsCancle.getString("repStuName"),
	                     srsCancle.getInt("repStuNum"),
	                     srsCancle.getString("resState")
	               });
	               cnt++;
	            }
				
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		void tableUpdate() {
			model.setNumRows(0);
			
			tableAdd();

			cntJLabel.setText("�� "+model.getRowCount()+"��");
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			while(true) {
				tableUpdate();

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
	}//Ŭ����
	
	class UserUser extends JPanel implements Runnable{
		String[] header = {"����","����","���� �̿��ڼ�","����ð�","�̸�","�й�","����"};
		DefaultTableModel model = new DefaultTableModel(header,0){
	        public boolean isCellEditable(int i, int c){
	        	return false;
	        }
	    };
	    
	    JLabel cntJLabel = new JLabel("�� 0��");//�� ���
		JPanel northLeftJPanel = new JPanel(new FlowLayout(FlowLayout.LEFT,10,10));//��� ����
		
		JButton cancleButton = new JButton("���");
	    
	    JScrollPane tableJScrollPane;
	    resTable table;
	    
	    int click;
		
	    UserUser(){
			this.setLayout(new BorderLayout());
			
			northLeftJPanel.add(cntJLabel);
			this.add(northLeftJPanel,BorderLayout.NORTH);
			northLeftJPanel.setBackground(Color.white);
			
			table = new resTable(model);
			tableJScrollPane = new JScrollPane(table);
			this.add(tableJScrollPane,BorderLayout.CENTER);
			
			tableAdd();

			table.addMouseListener(new MouseListener() {
				
				@Override
				public void mouseReleased(MouseEvent e) {}
				
				@Override
				public void mousePressed(MouseEvent e) {}
				
				@Override
				public void mouseExited(MouseEvent e) {}
				
				@Override
				public void mouseEntered(MouseEvent e) {}
				
				@Override
				public void mouseClicked(MouseEvent e) {
					// TODO Auto-generated method stub
					click =table.getSelectedRow();
					
					if (click!=-1) {
						String info = "";
						String cancleQuery="";
						info=model.getValueAt(click, 1)+" "+model.getValueAt(click, 3);
						String[] split = model.getValueAt(click, 3).toString().split("/");
						String[] split2 = split[2].split(" ");
						cancleQuery="select * from res where repStuNum = "+model.getValueAt(click, 5)+" and resDay="+split[0]+split[1]+split2[0]+" and resTime="+split2[1].charAt(0)+split2[1].charAt(1);
						ResCancle dialog = new ResCancle(info,cancleQuery);
						dialog.setVisible(true);
					}else {
//						System.out.println("Ŭ�� �������� �߻�");
					}
				}
			});
			
		}//������
		
		void tableAdd() {
			try {
				Statement stmt = conn.createStatement();
				ResultSet srs = stmt.executeQuery("select * from res where repStuNum ="+login_tf_id.getText());
				
				int cnt=1;
				while(srs.next()) {
					String resState = srs.getString("resState");
					if(resState.equals("���Ϸ�") || resState.equals("�������"))
						continue;
					int resDay = srs.getInt("resDay");
					int resTime = srs.getInt("resTime");
		
					model.addRow(new Object[] {
							cnt,
							"���͵��"+srs.getInt("roomNum"),
							srs.getInt("accStuCnt")+"��",
							resDay/10000+"/"+String.format("%02d",(resDay/100)%100)+"/"+String.format("%02d",resDay%100)+" "+String.format("%02d",resTime)+":00 ~ "+String.format("%02d",(resTime+1))+":00",
							srs.getString("repStuName"),
							srs.getInt("repStuNum"),
							srs.getString("resState")
					});
					cnt++;
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		void tableUpdate() {
			model.setNumRows(0);
			
			tableAdd();
			
			cntJLabel.setText("�� "+model.getRowCount()+"��");
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			while(true) {
				tableUpdate();

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
	}//Ŭ����
	
	class BackGround extends Thread{
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			
			while(true) {
				now = LocalDateTime.now();//�����ð�

				try {
					Statement stmt = conn.createStatement();
					ResultSet srs = stmt.executeQuery("select * from res");
						
					while(srs.next()) {
						Statement stmtUpdate = conn.createStatement();
						
						int resDay = srs.getInt("resDay");
						int resTime = srs.getInt("resTime");
						int resNum = srs.getInt("resNum");
						String resState = srs.getString("resState");
						
						int year = resDay/10000;
						int month = (resDay/100)%100;
						int day = resDay%100;
						
						LocalDateTime compare = LocalDateTime.of(year, month, day, resTime,0);
						
						if(compare.isAfter(now)) {
							if(!resState.equals("�����")) {
								stmtUpdate.executeUpdate("update res set resState='�����' where resNum ="+ resNum);
								System.out.println("[Log:db]DB ������Ʈ �Ϸ�");
							}
						} else if(compare.plusHours(1).isAfter(now)) {
							if(!resState.equals("�����")) {
								stmtUpdate.executeUpdate("update res set resState='�����' where resNum ="+ resNum);
								System.out.println("[Log:db]DB ������Ʈ �Ϸ�");
							}
						} else {
							if(!resState.equals("���Ϸ�")) {
								stmtUpdate.executeUpdate("update res set resState='���Ϸ�' where resNum ="+ resNum);
								System.out.println("[Log:db]DB ������Ʈ �Ϸ�");
							}
						}
					}//while
					
					Statement stmtCancle = conn.createStatement();
					ResultSet srsCancle = stmtCancle.executeQuery("select * from cancle");
					
					while(srsCancle.next()) {
						Statement stmtUpdate = conn.createStatement();
						
						if(!srsCancle.getString("resState").equals("�������")) {
							stmtUpdate.executeUpdate("update cancle set resState='�������' where resNum ="+ srsCancle.getInt("resNum"));
							System.out.println("[Log:db]DB ������Ʈ �Ϸ�");
						}
					}
					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}//run-while
		}
		
	}
	
	public static void setConn(){      //������ ���̽� �������� �޼ҵ� ����
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/j_studyroom", "root","1234");

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//DB����
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/j_studyroom", "root","1234");
			System.out.println("[Log:db]DB ���� �Ϸ�");
			
			new gitJava_library();

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
