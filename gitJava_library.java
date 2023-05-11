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
		
		//내용 중앙정렬
		DefaultTableCellRenderer renderer = (DefaultTableCellRenderer)this.getDefaultRenderer(Object.class);
		renderer.setHorizontalAlignment( SwingConstants.CENTER );
		
		//셀 사이즈 조절 불가
		this.getTableHeader().setReorderingAllowed(false);
		this.getTableHeader().setResizingAllowed(false);
		
		//셀 사이즈 조절
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
	
	//main 컨테이너 관련
	Container 	main_con; 		//컨테이너
	JFrame 		main_frame;		//현재 프레임 
	CardLayout 	main_lay_card; 	//카드레이아웃
	
	//login 화면 관련
	JTextField 		login_tf_id; //id입력 텍스트필드
	JPasswordField 	login_pf_pw; //password입력 텍스트필드
	
	ImageIcon logoImage = new ImageIcon("./res/logo.png");
	ImageIcon imageIcon = new ImageIcon("./res/icon.png");
	ImageIcon adminLogo = new ImageIcon("./res/admin_logo.png");
	
	String[] dayList = new String[5]; //콤보박스안에 넣을 날짜 리스트
	String[] stroomNum = {"스터디룸1","스터디룸2","스터디룸3"};
	String listname = " ";
	
	JLabel sizeLabel;      //JLabel 총 몇건인지
	
	LocalDateTime now = LocalDateTime.now();
	int nowDate = (now.getYear()*10000 + now.getMonthValue() * 100 + now.getDayOfMonth());      //현재 날짜를 얻기위함
	int nowTime = now.getHour();         //현재 시간을 얻기 위함
	Vector<Vector<String>> list = new Vector<Vector<String>>();      //테이블을 만들기위한 벡터 list
	
	public gitJava_library() {
		//main 컨테이너 설정
		main_con = this.getContentPane();
		main_frame = this;
		//나머지 main 컨테이너 설정
		main_lay_card = new CardLayout();
		this.setLayout(main_lay_card);
		
		BackGround backGroundThread = new BackGround();
		backGroundThread.start();
		
		main_con.add(new Login(), "Login");
				
		//레이아웃 설정
		main_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //gui종료시 main도 종료
		main_frame.setTitle("스터디룸 예약 by.2021081104/허주환");
		main_frame.setIconImage(imageIcon.getImage()); //프로그램 아이콘 이미지 설정
		main_frame.setResizable(false); //화면크기 변경불가
		main_frame.setSize(700,450); //화면 사이즈 800 x 500 으로 설정
		main_frame.setLocationRelativeTo(null); //중앙에 생성
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
			JButton signup_btn = new JButton("회원가입");
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
	                        JOptionPane.showMessageDialog(null, "로그인 실패","로그인 실패",JOptionPane.DEFAULT_OPTION);
	                     }
						if(id.equals(loginId) && pw.equals(loginPw)) {
							main_con.add(new UserMainPage(), "UserMainPage");
							main_lay_card.show(main_con, "UserMainPage");
	                     }else {
	                    	 login_tf_id.setText("");
	                    	 login_pf_pw.setText("");
	                         JOptionPane.showMessageDialog(null, "아이디와 비밀번호가 다릅니다.","로그인 실패",JOptionPane.DEFAULT_OPTION);
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
	
	class Sign_up extends JPanel{//회원가입
	      
	      JTextField stu_id;
	      JTextField stu_name;
	      JPasswordField pass;
	      
	       Sign_up(){
	         setLayout(new BorderLayout(30,40));
	         
	         JLabel lb1=new JLabel(logoImage);
	         add(lb1,BorderLayout.NORTH);
	         
	         JPanel info=new JPanel();//회원가입 패널 설정
	         info.setLayout(new GridLayout(4,4,0,20));
	         
	         info.add(new JLabel(""));
	         
	         JLabel name=new JLabel("이름");
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
	         
	         JLabel pw_check=new JLabel("비밀번호 확인");
	         pw_check.setHorizontalAlignment(JLabel.CENTER);
	         info.add(pw_check);
	         JPasswordField pw_check_field=new JPasswordField(15);
	         info.add(pw_check_field);
	         
	         info.add(new JLabel(""));
	         
	         JPanel button=new JPanel();
	         JButton confirm=new JButton("확인");
	         JButton cancel=new JButton("취소");
	         button.add(confirm);
	         button.add(cancel);
	         
	         confirm.addActionListener(new ActionListener() {
	            //확인 버튼 액션
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
	                     
	                     String query = "insert into stu values("+id+",'"+name+"','"+password+"')";//학생DB에 저장
	                     
	                     stmt.executeUpdate(query);
	                  } catch (SQLException e1) {
	                     // TODO: handle exception
	                     e1.printStackTrace();
	                  }
	                  JOptionPane.showMessageDialog(null, "회원가입이 완료되었습니다.","회원가입 완료",JOptionPane.DEFAULT_OPTION);
	                  main_lay_card.show(main_con, "Login");//로그인 화면으로 돌아가기
	               }
	               else {
	                  pw_check_field.setText("");//비밀번호 확인하는 부분 지우기
	                  JOptionPane.showMessageDialog(null, "비밀번호를 다시 확인하세요.","회원가입 실패",JOptionPane.DEFAULT_OPTION);
	               }
	            }
	         });//confirm.addActionListener(new ActionListener())
	         cancel.addActionListener(new ActionListener() {
	            //취소 버튼 액션
	            @Override
	            public void actionPerformed(ActionEvent e) {
	               // TODO Auto-generated method stub
	               stu_name.setText("");
	               stu_id.setText("");
	               pass.setText("");
	               pw_check_field.setText("");//회원가입 부분 초기화
	               JOptionPane.showMessageDialog(null, "회원가입이 취소되었습니다.","회원가입 취소.",JOptionPane.DEFAULT_OPTION);
	               main_lay_card.show(main_con, "Login");//로그인 화면으로 돌아가기
	            }
	         });
	         add(info,BorderLayout.CENTER);
	         add(button,BorderLayout.SOUTH);
	         setVisible(true);
	      }
	   }

	class Bookstudy extends JPanel{      //스터디룸 패널 클래스 생성
	       Vector<Vector<String>> lists = new Vector<Vector<String>>();
	        Vector<Vector<String>> booked1 = new Vector<Vector<String>>();
	        Vector<Vector<String>> booked2 = new Vector<Vector<String>>();
	        Vector<Vector<String>> booked3 = new Vector<Vector<String>>();
	        JPanel stPanel = new JPanel(new BorderLayout());//중간
	        JPanel btnPanel = new JPanel(new FlowLayout());   //버튼 넣을 패널
	        JPanel labelPanel = new JPanel(new BorderLayout());   //라벨 넣을패널
	        JPanel label2Panel = new JPanel(new FlowLayout(FlowLayout.RIGHT,5,0));
	        JComboBox dayCombo;      //예약희망날짜
	        JComboBox timeCombo;      //예약희망시간
	        Vector<String> stnl = new Vector<String>();
	        Vector<String> nownow;   //stnTable에 넣을 값
	        JTable stTable;      // 값을 넣기위한 테이블 생성
	        JTable stnTable;      //예약 상태를 넣기위한 테이블 생성
	        JButton renew_btn = new JButton("갱신");
	        JButton book_btn = new JButton("예약하기");
	        TableModel stnmodel;
	        String bookok = "예약가능";
	        String bookno = "예약불가";
	        boolean[] bookedbool1;
	      boolean[] bookedbool2;
	      boolean[] bookedbool3;
	      String[] timeList = {"09:00 ~ 10:00","10:00 ~ 11:00","11:00 ~ 12:00","12:00 ~ 13:00","13:00 ~ 14:00","14:00 ~ 15:00","15:00 ~ 16:00",
	               "16:00 ~ 17:00","17:00 ~ 18:00","18:00 ~ 19:00","19:00 ~ 20:00","20:00 ~ 21:00","21:00 ~ 22:00"};   //콤보박스안에 넣을 시간 리스트
	      String[] stHeader = {"이름","층", "수용인원"};
	       String[][] stContents = new String[3][4];
	      
	        Bookstudy() {
	        	if(login_tf_id.getText().equals("admin")) {
	        		book_btn.setVisible(false);
	        	}
	           
	        	for(int i = 0; i<=4; i++) {
	                String dateform;
	                dateform = now.format(DateTimeFormatter.ofPattern("yyyy-MM-")).toString()+(now.getDayOfMonth()+i);
	                dayList[i] = dateform;
	                //현재 날짜로 부터 5일분 예약
	             }
	            this.setLayout(new BorderLayout());
	            labelPanel.setBackground(Color.white);
	            labelPanel.setPreferredSize(new Dimension(700,30));
	            labelPanel.add(new JLabel(" 총 3건"),BorderLayout.WEST);
	            dayCombo = new JComboBox(dayList);      //예약희망날짜 생성
	            dayCombo.setBackground(Color.WHITE);
	            timeCombo = new JComboBox(timeList);   //예약희망시간 생성
	            timeCombo.setBackground(Color.WHITE);
	            label2Panel.setBackground(Color.white);
	            label2Panel.add(new JLabel("예약희망 날짜"));
	            label2Panel.add(dayCombo);
	            label2Panel.add(new JLabel("예약희망 시간"));
	            label2Panel.add(timeCombo);
	            renew_btn.setBackground(Color.WHITE);
	            renew_btn.addActionListener(new ActionListener() {   //JTable 갱신 버튼
	            public void actionPerformed(ActionEvent e) {
	               
	               int roomNum;
	               bookedbool1 = new boolean[1];
	                   bookedbool2 = new boolean[1];
	                   bookedbool3 = new boolean[1];
	               try {
	                        int ad_book_day;
	                        String ad_book_day_DB="";
	                      String ad_book_day_iDB = dayCombo.getSelectedItem().toString();   //선택한 예약 시간 값 가져오기
	                      String[] adb = ad_book_day_iDB.split("-");
	                      for(int k=0; k<adb.length ; k++) {
	                         ad_book_day_DB += adb[k];
	                        }
	                      ad_book_day = Integer.parseInt(ad_book_day_DB);
	                      int ad_book_time = timeCombo.getSelectedIndex()+9;   //선택한 예약 날짜 값 가져오기
	                      
	                      setConn();      //데이터베이스 연결
	                      Statement stmt1 = conn.createStatement();
	                      ResultSet srs1 = stmt1.executeQuery("select * from res where roomNum=1 and resDay ='"+ad_book_day+"'and resTime ='"+ad_book_time+"'");
	                      Statement stmt2 = conn.createStatement();
	                      ResultSet srs2 = stmt2.executeQuery("select * from res where roomNum=2 and resDay ='"+ad_book_day+"'and resTime ='"+ad_book_time+"'");
	                      Statement stmt3 = conn.createStatement();
	                      ResultSet srs3 = stmt3.executeQuery("select * from res where roomNum=3 and resDay ='"+ad_book_day+"'and resTime ='"+ad_book_time+"'");
	                      //스터디룸에 따라 쿼리문 다르게 작성하기 위해 3개씩 작성
	                      Vector<String> book1 = new Vector<String>();
	                      Vector<String> book2 = new Vector<String>();
	                      Vector<String> book3 = new Vector<String>();
	                      booked1.clear();
	                      booked2.clear();
	                      booked3.clear();
	                      while(srs1.next()) {
	                         bookedbool1[Integer.parseInt(String.valueOf(srs1.getString("roomNum")))-1] = true;   //roomNum을 갖고와서 값이 있다면 true 반환
	                      }
	                      if(bookedbool1[0]) {      //true라면 사용중이기 때문에 예약불가
	                         book1.add(bookno);
	                         booked1.add(book1);
	                      }else {
	                         book1.add(bookok);      //false라면 예약이 없기때문에 예약 가능
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
	               ((DefaultTableModel) stnmodel).setNumRows(0);      //JTable 비우기
	               ((DefaultTableModel) stnmodel).addRow(booked1);      //행에 값 순서대로 채우기
	               ((DefaultTableModel) stnmodel).addRow(booked2);
	               ((DefaultTableModel) stnmodel).addRow(booked3);
	               JOptionPane.showMessageDialog(null, "갱신되었습니다.");
	            }
	         });
	            label2Panel.add(renew_btn);
	            labelPanel.add(label2Panel);
	            stPanel.add(labelPanel,BorderLayout.NORTH);      //라벨 패널을 중간 패널 상단에 배치
	            
	            for (int i = 0; i < stContents.length; i++) {      //JTable에서 사용할 stContents에 값을 넣어줌
	                stContents[i][0]="스터디룸"+(i+1);
	                stContents[i][1]="1층";
	                stContents[i][2]="4 ~ 8";
	            }
	            TableModel stmodel = new DefaultTableModel(stContents,stHeader) {
	                public boolean isCellEditable(int i, int c) {
	                     return false;
	                 }
	            };
	            stTable = new JTable(stmodel);      //테이블 생성
	            //stTable 내용 중앙 정렬
	            DefaultTableCellRenderer renderer = (DefaultTableCellRenderer)stTable.getDefaultRenderer(Object.class);
	            renderer.setHorizontalAlignment( SwingConstants.CENTER );

	            //stTable 셀 사이즈 조절
	            stTable.setRowHeight(60);
	            stTable.getColumnModel().getColumn(0).setPreferredWidth(70);
	            stTable.getColumnModel().getColumn(1).setPreferredWidth(50);
	            stTable.getColumnModel().getColumn(2).setPreferredWidth(80);
	            stTable.getTableHeader().setReorderingAllowed(false);
	            stTable.getTableHeader().setResizingAllowed(false);
	            JScrollPane stJs = new JScrollPane(stTable);
	            stPanel.add(stJs, BorderLayout.WEST);
	            int i =0;
	            int albookDay = nowDate;      //비교를 위한 현재 날짜
	            int albookTime = nowTime;      //비교를 위한 현재 시간
	            boolean[] booked = new boolean[stroomNum.length];
	            try {
	               setConn();
	                Statement stmt = conn.createStatement();
	                ResultSet srs = stmt.executeQuery("select roomNum from res where resDay='"+albookDay+"' and resTime='"+albookTime+"'");      //처음 화면의 사용가능 여부
	                while(srs.next()) {
	                    booked[Integer.parseInt(String.valueOf(srs.getString("roomNum")))-1] = true;
	                }
	            }catch (Exception e){
	                  e.printStackTrace();
	            }
	            while(!(i==3)){
	                //백터값으로 배열 nownow를 만듬
	                nownow = new Vector<String>();
	                if(booked[i]) 
	                   nownow.add(bookno);
	                else 
	                   nownow.add(bookok);
	                lists.add(nownow);
	                i++;
	            }

	            stnl.add("예약현황");
	            
	            
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
	            //테이블 생성 및 설정
	            stPanel.add(stnJs, BorderLayout.CENTER);
	            book_btn.setBackground(Color.white);
	            
	            book_btn.addActionListener(new ActionListener() {      //예약버튼을 누르면 예약페이지로 이동
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
	       int accNum;   //동반이용자 학번
	        JPanel northP = new JPanel();      //상단 패널
	        JComboBox hope_studyr = new JComboBox(stroomNum);
	        JComboBox hope_day;
	        JComboBox hope_time = new JComboBox(htc1);
	        
	        JPanel centerP = new JPanel(new BorderLayout());      //중단패널
	        JPanel center_northP = new JPanel(new FlowLayout(FlowLayout.LEFT));   //중단패널의 상단패널
	        JPanel center_centerP = new JPanel(new BorderLayout(5,5));   //중단패널의 중단패널
	        JPanel center_center_leftP = new JPanel(new GridLayout(5,1));      //중단패널의 중단패널의 왼쪽패널
	        JPanel center_center_centerP = new JPanel(new GridLayout(5,1));   //중단 패널의 중단패널의 중단패널
	        JPanel center_center_rightP = new JPanel(new GridLayout(5,1));
	        JPanel center_south = new JPanel();   //중단패널의 하단패널
	        JPanel southP = new JPanel();      //하단패널
	        JButton add_btn = new JButton("추가");
	        JButton submit_btn = new JButton("예약");
	        JButton cancel_btn = new JButton("취소");
	        JTextField withname_tf = new JTextField();
	        JTextField withnum_tf = new JTextField();
	        JTextField withlist_tf = new JTextField(" ");
	        String withNum_compare;      //stu와 비교를위한 학번
	        String withName_compare;   //stu와 비교를 위한 이름   
	        boolean[] selec_bool1;         //예약가능여부를 나타내는 boolean 배열
	      boolean[] selec_bool2;
	      boolean[] selec_bool3;
	        StudyBook_info(){
	            hope_day = new JComboBox(dayList);
	           hope_studyr.setBackground(Color.WHITE);
	           hope_day.setBackground(Color.WHITE);
	           hope_time.setBackground(Color.WHITE);
	            setLayout(new BorderLayout());
	            
	            //예약페이지 *******************************************************
	            northP.add(new JLabel(logoImage),BorderLayout.NORTH);      //상단패널에는 로고를 넣음
	            JLabel studyr_label = new JLabel("희망 스터디룸");
	            center_northP.add(studyr_label);
	            center_northP.add(hope_studyr);   
	            //중단의 상단패널
	            centerP.add(center_northP , BorderLayout.NORTH);
	            submit_btn.setBackground(Color.white);
	            cancel_btn.setBackground(Color.white);
	            add_btn.setBackground(Color.white);
	            JLabel hdLabel = new JLabel("예약희망일자 ↓");
	            JLabel htLabel = new JLabel("예약희망시간 ↓");
	            JLabel withnameLabel = new JLabel("동반이용자 이름 ↓");
	            JLabel withnumLabel = new JLabel("동반이용자 학번 ↓");
	            JLabel withList = new JLabel("동반이용자 목록 >");
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
	           
	            submit_btn.addActionListener(new  ActionListener() {      //예약 버튼을 누를 시 작동
	                public void actionPerformed(ActionEvent e) {
	                    if(withlist_tf.getText().equals(" ")) {
	                        hope_day.setSelectedIndex(0);
	                        hope_time.setSelectedIndex(0);
	                        hope_studyr.setSelectedIndex(0);
	                        JOptionPane.showMessageDialog(null,"신청 인원이없습니다.");
	                        String listPeo = withlist_tf.getText();
	                        String [] listPeonum = listPeo.split(",");
	                        System.out.println(listPeonum.length);
	                        listname =" ";

	                    }else {
	                        try {
	                            setConn();
	                            String room1 = "스터디룸1";      //스터디룸을 구분하기 위한 변수
	                            String room2 = "스터디룸2";
	                            String room3 = "스터디룸3";
	                            Statement stmt = conn.createStatement();
	                          int selec_day;   //선택한 예약 날짜 값 가져오기
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
	                            String resState = "예약됨";   //예약됨, 사용중, 사용종료, 취소
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
	                                  selec_bool1[Integer.parseInt(String.valueOf(srsS1.getString("roomNum")))-1] = true;   //roomNum을 갖고와서 값이 있다면 true 반환
	                            }
	                               if(selec_bool1[0]) {      //true라면 사용중이기 때문에 예약불가
	                                 JOptionPane.showMessageDialog(null, "이미 예약중인 스터디룸입니다.");
	                              }else if(nowDate < selec_day){                  //false라면 예약이 없기때문에 예약 가능
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
	                                        //repStuNum 값은 로그인 값에서 빼내오기*************************************************************************************************
//	                                        System.out.println("insert into res values('"+resNum+"','"+roomNum+"','"+resDay+"','"+resTime+"','"+repStuName+"','"+repStuNum+"','"+resState+"','"+accStuCnt);
	                                        stmt.execute("insert into res values('"+resNum+"','"+roomNum+"','"+resDay+"','"+resTime+"','"+repStuName+"','"+repStuNum+"','"+resState+"','"+accStuCnt+"')");
	                                        sizeLabel.setText("총 "+list.size()+"건");
	                                        JOptionPane.showMessageDialog(null,"신청되셨습니다.");
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
	                                        //repStuNum 값은 로그인 값에서 빼내오기*************************************************************************************************
	                                        
//	                                        System.out.println("insert into res values('"+resNum+"','"+roomNum+"','"+resDay+"','"+resTime+"','"+repStuName+"','"+repStuNum+"','"+resState+"','"+accStuCnt);
	                                        stmt.execute("insert into res values('"+resNum+"','"+roomNum+"','"+resDay+"','"+resTime+"','"+repStuName+"','"+repStuNum+"','"+resState+"','"+accStuCnt+"')");
	                                        sizeLabel.setText("총 "+list.size()+"건");
	                                        JOptionPane.showMessageDialog(null,"신청되셨습니다.");
	                                 }else {
	                                    JOptionPane.showMessageDialog(null, "신청 시간이 지났습니다.");
	                                 }
	                              }else{
	                                 JOptionPane.showMessageDialog(null, "신청 시간이 지났습니다.");
	                              }
	                          }
	                            
	                            if((hope_studyr.getSelectedItem().toString() == room2) && (selec_day  == selec_day) && ((hope_time.getSelectedIndex()+9) == selec_time)){
	                               while(srsS2.next()) {
	                                  selec_bool2[Integer.parseInt(String.valueOf(srsS2.getString("roomNum")))-2] = true;   //roomNum을 갖고와서 값이 있다면 true 반환
	                            }
	                               if(selec_bool2[0]) {      //true라면 사용중이기 때문에 예약불가
	                                 JOptionPane.showMessageDialog(null, "이미 예약중인 스터디룸입니다.");
	                              }else if(nowDate < selec_day){                  //false라면 예약이 없기때문에 예약 가능
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
	                                    //repStuNum 값은 로그인 값에서 빼내오기*************************************************************************************************
	                                    
//	                                    System.out.println("insert into res values('"+resNum+"','"+roomNum+"','"+resDay+"','"+resTime+"','"+repStuName+"','"+repStuNum+"','"+resState+"','"+accStuCnt);
                                        stmt.execute("insert into res values('"+resNum+"','"+roomNum+"','"+resDay+"','"+resTime+"','"+repStuName+"','"+repStuNum+"','"+resState+"','"+accStuCnt+"')");
	                                    sizeLabel.setText("총 "+list.size()+"건");
	                                    JOptionPane.showMessageDialog(null,"신청되셨습니다.");
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
	                                    //repStuNum 값은 로그인 값에서 빼내오기*************************************************************************************************
	                                    
//	                                    System.out.println("insert into res values('"+resNum+"','"+roomNum+"','"+resDay+"','"+resTime+"','"+repStuName+"','"+repStuNum+"','"+resState+"','"+accStuCnt);
                                        stmt.execute("insert into res values('"+resNum+"','"+roomNum+"','"+resDay+"','"+resTime+"','"+repStuName+"','"+repStuNum+"','"+resState+"','"+accStuCnt+"')");
	                                    sizeLabel.setText("총 "+list.size()+"건");
	                                    JOptionPane.showMessageDialog(null,"신청되셨습니다.");
	                             }else {
	                                JOptionPane.showMessageDialog(null, "신청 시간이 지났습니다.");
	                             }
	                          }else{
	                             JOptionPane.showMessageDialog(null, "신청 시간이 지났습니다.");
	                          }
	                          }
	                            if((hope_studyr.getSelectedItem().toString() == room3) && (selec_day  == selec_day) && ((hope_time.getSelectedIndex()+9) == selec_time)){
	                               while(srsS3.next()) {
	                                  selec_bool3[Integer.parseInt(String.valueOf(srsS1.getString("roomNum")))-3] = true;   //roomNum을 갖고와서 값이 있다면 true 반환
	                            }
	                               if(selec_bool3[0]) {      //true라면 사용중이기 때문에 예약불가
	                                 JOptionPane.showMessageDialog(null, "이미 예약중인 스터디룸입니다.");
	                              }else if(nowDate < selec_day){                  //false라면 예약이 없기때문에 예약 가능
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
	                                    //repStuNum 값은 로그인 값에서 빼내오기*************************************************************************************************
	                                    
//	                                    System.out.println("insert into res values('"+resNum+"','"+roomNum+"','"+resDay+"','"+resTime+"','"+repStuName+"','"+repStuNum+"','"+resState+"','"+accStuCnt);
                                        stmt.execute("insert into res values('"+resNum+"','"+roomNum+"','"+resDay+"','"+resTime+"','"+repStuName+"','"+repStuNum+"','"+resState+"','"+accStuCnt+"')");
	                                    sizeLabel.setText("총 "+list.size()+"건");
	                                    JOptionPane.showMessageDialog(null,"신청되셨습니다.");
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
	                                    //repStuNum 값은 로그인 값에서 빼내오기*************************************************************************************************
	                                    
//	                                    System.out.println("insert into res values('"+resNum+"','"+roomNum+"','"+resDay+"','"+resTime+"','"+repStuName+"','"+repStuNum+"','"+resState+"','"+accStuCnt);
                                        stmt.execute("insert into res values('"+resNum+"','"+repStuNum+"','"+roomNum+"','"+resDay+"','"+resTime+"','"+resState+"','"+accStuNum1+"')");
	                                    sizeLabel.setText("총 "+list.size()+"건");
	                                    JOptionPane.showMessageDialog(null,"신청되셨습니다.");
	                             }else {
	                                JOptionPane.showMessageDialog(null, "신청 시간이 지났습니다.");
	                             }
	                          }else{
	                             JOptionPane.showMessageDialog(null, "신청 시간이 지났습니다.");
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
	            cancel_btn.addActionListener(new ActionListener() {      //취소 버튼을 누르면 모든걸 초기화하고 메인페이지로 돌아감
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
	            
	             
	            add_btn.addActionListener(new ActionListener() {      //학생 추가 버튼
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
	                   //stu 데이터에 있는 학생이 아니면 추가 불가
	               if(withnum_tf.getText().equals(withNum_compare) && withname_tf.getText().equals(withName_compare)) {
	                  listname += (withNum_compare + withName_compare + ", ");
	                  accNum = Integer.parseInt(withNum_compare);
	                       withlist_tf.setText(listname);
	                       JOptionPane.showMessageDialog(null, "추가되었습니다.");
	                       withnum_tf.setText("");
	                       withname_tf.setText("");
	                           //목록에 추가하고 예약 희망날자는 신청 누를때 데이터베이스에 저장
	                   }else {
	                      JOptionPane.showMessageDialog(null, "학번,이름 잘못입력");
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
	
	class Tab extends JPanel{//관리자
		JTabbedPane t = new JTabbedPane();  //JTabbedPane생성
		
		Tab(){
			this.setLayout(new BorderLayout());
			add(new JLabel(adminLogo),BorderLayout.NORTH);//상단 이미지 넣기
			
			AdminMainPage amp = new AdminMainPage();
			Thread thread = new Thread(amp);
			thread.start();
			
			User user = new User();
			Thread thread2 = new Thread(user);
			thread2.start();
			
			t.add("스터디룸", new Bookstudy());
			t.add("예약내역",amp);
			t.add("예약현황",user);
			
			this.add(t);
		}
	}

	class AdminMainPage extends JPanel implements Runnable{
		String[] header = {"순번","공간","동반 이용자수","사용시간","이름","학번","상태"};
		DefaultTableModel model = new DefaultTableModel(header,0){
	        public boolean isCellEditable(int i, int c){
	        	return false;
	        }
	    };
	    
	    JLabel cntJLabel = new JLabel("총 0건");//총 몇건
	    String[] searchCondition = {"학번","이름","일시"}; 
	    JPanel northJPanel = new JPanel(new BorderLayout());//상단에 위치할 패널
		JPanel northLeftJPanel = new JPanel(new FlowLayout(FlowLayout.LEFT,10,10));//상단 왼쪽
	    
		JPanel searchJPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT,10,10));//상단 우측 검색조건 콤보박스, 텍스트 필드, 조회버튼
		JComboBox<String> searchBox = new JComboBox<String>(searchCondition);//검색 조건 콤보박스
		JTextField searchField = new JTextField();//검색내용
		
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
			
			searchJPanel.add(new JLabel("검색조건 "));
			searchJPanel.add(searchBox);
			searchJPanel.add(searchField);
			searchField.setColumns(10);
			searchJPanel.setBackground(Color.white);
			
			table = new resTable(model);
			tableJScrollPane = new JScrollPane(table);
			this.add(tableJScrollPane,BorderLayout.CENTER);
			
			tableAdd();

		}//생성자
		
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
					if(resState.equals("예약됨") || resState.equals("사용중"))
						continue;
					
					int resDay = srs.getInt("resDay");
					int resTime = srs.getInt("resTime");
		
					model.addRow(new Object[] {
							cnt,
							"스터디룸"+srs.getInt("roomNum"),
							srs.getInt("accStuCnt")+"명",
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
							"스터디룸"+srsCancle.getInt("roomNum"),
							srsCancle.getInt("accStuCnt")+"명",
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
			
//			학번 이름 일시
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

			cntJLabel.setText("총 "+model.getRowCount()+"건 서버시간 : "+now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
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
	}//클래스
	
	class ResCancle extends JDialog {
		JButton noBtn = new JButton("취소");
		JButton okBtn = new JButton("확인");
		JPanel labelJPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,10,10));
		JPanel btnJPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,10,10));
		JLabel resInfoJLabel;
		
		public ResCancle(String info, String cancleQuery) {
			super(main_frame,"예약취소");
			
			//다이얼로그 설정
			setLayout(new BorderLayout());
			setIconImage(imageIcon.getImage()); //프로그램 아이콘 이미지 설정
			setResizable(false); //화면크기 변경불가
			setSize(350,125); //화면 사이즈 800 x 500 으로 설정
			setLocationRelativeTo(null); //중앙에 생성
			
			//컴포넌트 추가
			resInfoJLabel = new JLabel(info);
			add(labelJPanel,BorderLayout.CENTER);
			labelJPanel.add(resInfoJLabel);
			labelJPanel.add(new JLabel("취소하시겠습니까?"));
			add(btnJPanel,BorderLayout.SOUTH);
			btnJPanel.add(okBtn);
			btnJPanel.add(noBtn);
			
			noBtn.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					setVisible(false);//확인창 누르면 다이알로그 사라지도록
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
					
					setVisible(false);//확인창 누르면 다이알로그 사라지도록
				}
			});
			
		}
	}
	
	class User extends JPanel implements Runnable{
		String[] header = {"순번","공간","동반 이용자수","예약시간","이름","학번","상태"};
		DefaultTableModel model = new DefaultTableModel(header,0){
	        public boolean isCellEditable(int i, int c){
	        	return false;
	        }
	    };
	    
	    JLabel cntJLabel = new JLabel("총 0건");//총 몇건
		JPanel northLeftJPanel = new JPanel(new FlowLayout(FlowLayout.LEFT,10,10));//상단 왼쪽
	    
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
//						System.out.println("클릭 씹힘문제 발생");
					}
					
				}
			});

		}//생성자
		
		void tableAdd() {
			try {
				Statement stmt = conn.createStatement();
				ResultSet srs = stmt.executeQuery("select * from res");
				
				int cnt=1;
				while(srs.next()) {
					String resState = srs.getString("resState");
					if(resState.equals("사용완료") || resState.equals("예약취소"))
						continue;
					int resDay = srs.getInt("resDay");
					int resTime = srs.getInt("resTime");
		
					model.addRow(new Object[] {
							cnt,
							"스터디룸"+srs.getInt("roomNum"),
							srs.getInt("accStuCnt")+"명",
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
			
			cntJLabel.setText("총 "+model.getRowCount()+"건");
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
	}//클래스
	
	class UserMainPage extends JPanel{
		JTabbedPane t = new JTabbedPane();  //JTabbedPane생성
		
		UserMainPage(){
			this.setLayout(new BorderLayout());
			add(new JLabel(logoImage),BorderLayout.NORTH);//상단 이미지 넣기
			
			UserAdminMainPage uamp = new UserAdminMainPage();
			Thread thread = new Thread(uamp);
			thread.start();
			
			UserUser uuser = new UserUser();
			Thread thread2 = new Thread(uuser);
			thread2.start();
			
			t.add("스터디룸", new Bookstudy());
			t.add("예약내역",uamp);
			t.add("예약현황",uuser);
			
			this.add(t);
		}
	}
	
	class UserAdminMainPage extends JPanel implements Runnable{
		String[] header = {"순번","공간","동반 이용자수","사용시간","이름","학번","상태"};
		DefaultTableModel model = new DefaultTableModel(header,0){
	        public boolean isCellEditable(int i, int c){
	        	return false;
	        }
	    };
	    
	    JLabel cntJLabel = new JLabel("총 0건");//총 몇건
	    JPanel northJPanel = new JPanel(new BorderLayout());//상단에 위치할 패널
		JPanel northLeftJPanel = new JPanel(new FlowLayout(FlowLayout.LEFT,10,10));//상단 왼쪽
		
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

		}//생성자
		
		void tableAdd() {
			try {
				Statement stmt = conn.createStatement();
				ResultSet srs = stmt.executeQuery("select * from res where repStuNum ="+login_tf_id.getText());
				
				int cnt=1;
				while(srs.next()) {
					String resState = srs.getString("resState");
					if(resState.equals("예약됨") || resState.equals("사용중"))
						continue;
					int resDay = srs.getInt("resDay");
					int resTime = srs.getInt("resTime");
		
					model.addRow(new Object[] {
							cnt,
							"스터디룸"+srs.getInt("roomNum"),
							srs.getInt("accStuCnt")+"명",
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
	                     "스터디룸"+srsCancle.getInt("roomNum"),
	                     srsCancle.getInt("accStuCnt")+"명",
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

			cntJLabel.setText("총 "+model.getRowCount()+"건");
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
	}//클래스
	
	class UserUser extends JPanel implements Runnable{
		String[] header = {"순번","공간","동반 이용자수","예약시간","이름","학번","상태"};
		DefaultTableModel model = new DefaultTableModel(header,0){
	        public boolean isCellEditable(int i, int c){
	        	return false;
	        }
	    };
	    
	    JLabel cntJLabel = new JLabel("총 0건");//총 몇건
		JPanel northLeftJPanel = new JPanel(new FlowLayout(FlowLayout.LEFT,10,10));//상단 왼쪽
		
		JButton cancleButton = new JButton("취소");
	    
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
//						System.out.println("클릭 씹힘문제 발생");
					}
				}
			});
			
		}//생성자
		
		void tableAdd() {
			try {
				Statement stmt = conn.createStatement();
				ResultSet srs = stmt.executeQuery("select * from res where repStuNum ="+login_tf_id.getText());
				
				int cnt=1;
				while(srs.next()) {
					String resState = srs.getString("resState");
					if(resState.equals("사용완료") || resState.equals("예약취소"))
						continue;
					int resDay = srs.getInt("resDay");
					int resTime = srs.getInt("resTime");
		
					model.addRow(new Object[] {
							cnt,
							"스터디룸"+srs.getInt("roomNum"),
							srs.getInt("accStuCnt")+"명",
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
			
			cntJLabel.setText("총 "+model.getRowCount()+"건");
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
	}//클래스
	
	class BackGround extends Thread{
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			
			while(true) {
				now = LocalDateTime.now();//서버시간

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
							if(!resState.equals("예약됨")) {
								stmtUpdate.executeUpdate("update res set resState='예약됨' where resNum ="+ resNum);
								System.out.println("[Log:db]DB 업데이트 완료");
							}
						} else if(compare.plusHours(1).isAfter(now)) {
							if(!resState.equals("사용중")) {
								stmtUpdate.executeUpdate("update res set resState='사용중' where resNum ="+ resNum);
								System.out.println("[Log:db]DB 업데이트 완료");
							}
						} else {
							if(!resState.equals("사용완료")) {
								stmtUpdate.executeUpdate("update res set resState='사용완료' where resNum ="+ resNum);
								System.out.println("[Log:db]DB 업데이트 완료");
							}
						}
					}//while
					
					Statement stmtCancle = conn.createStatement();
					ResultSet srsCancle = stmtCancle.executeQuery("select * from cancle");
					
					while(srsCancle.next()) {
						Statement stmtUpdate = conn.createStatement();
						
						if(!srsCancle.getString("resState").equals("예약취소")) {
							stmtUpdate.executeUpdate("update cancle set resState='예약취소' where resNum ="+ srsCancle.getInt("resNum"));
							System.out.println("[Log:db]DB 업데이트 완료");
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
	
	public static void setConn(){      //데이터 베이스 서버연결 메소드 생성
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
		
		//DB연결
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/j_studyroom", "root","1234");
			System.out.println("[Log:db]DB 연결 완료");
			
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
