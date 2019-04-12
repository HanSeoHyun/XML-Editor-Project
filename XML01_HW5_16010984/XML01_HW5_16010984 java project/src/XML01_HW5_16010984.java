import java.awt.*;
import java.util.*;
import javax.print.Doc;
import javax.swing.*;
import org.apache.xerces.dom.DocumentImpl;
import org.apache.xerces.dom.ParentNode;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import java.awt.event.*;
import java.awt.Window.*;
import java.io.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.stream.*;
import javax.xml.transform.dom.*;
import javax.xml.validation.*;
import org.xml.sax.*;
import org.w3c.dom.*;


// 181210 오전 1시 43분 
// 1,3,4,5,6,7,9 완벽하게 잘 됨
// 유효성 검사가 필요함

public class XML01_HW5_16010984 extends JFrame {

	private JPanel contentPane;
	private final JPanel inputpanel = new JPanel();
	private JPanel outputpanel;
	private JLabel lblNewLabel;
	private JTextField myTextField;
	private JTextArea myTextArea;
	private JLabel stateLabel;
	
	private static boolean validationCheck = false; //validation 검사
	private static String xsdFile;
	static int menuNumber=0; 
	
	static String loadFileName; // Load 1 메뉴에서 입력받은 파일 이름
	static Document nowDoc; //현재 작업 중인 파일 doc
	static String makeName; // Make 2 메뉴에서 입력받은 값 이름
	static int valueType=0; // 어떤 타입으로 할지 (1:root, 2:element, 3:attribute, 4:text, 5:comment)
	static int rootExist =0 ; //루트가 있으면:1, 없으면:0
	static String toFindValue; // Find 3 메뉴에서 이 값을 찾으러 간다
	static String toSaveFile; // Save 4 메뉴에서 이 이름으로 파일 저장
	static String printFile; // print 5 메뉴에서 출력할 파일 이름
	static String toInsert; // insert 6 메뉴에서 입력받은 값
	static String toInsertValue; // insert 6 메뉴에서 넣을 값
	static String toInsertRef; // insert 6메뉴에서 넣을 위치
	static String toUpdate;//update 7 메뉴에서 입력받은 값
	static String toUpdateValue; //update 7 메뉴에서 바꾸고 싶은 값
	static String toUpdateNewName; //update 7 에서 새로 바꿀 값
	static String toUpdateNewValue; //update 7 에서 바꾸고 싶은 값
	static String toDelete; //delete 8 에서 지우고 싶은 값
	
	private Document makeDoc; //새로 작업 중인 파일
	static String defaultState = "현재 작업 중인 파일이 없습니다."; //작업 중인 파일이 없을 때 기본 메시지
	static String nowFile = defaultState; //현재 오픈한 파일
	static String inputFile; //입력받은 텍스트 이름.
	static String inputLoad;
	static boolean textState = false; //textState가 true일 때만 입력을 허용한다.
	static String newFileworking ="을 root로 하는 새로운 파일을 작업 중입니다.";
	
	/**
	 * Launch the application.
	 */

	
	public XML01_HW5_16010984() {
		setBackground(SystemColor.menu);
		setTitle("XML Final Assignment");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 1600, 500);
		
		
		contentPane = new JPanel();
		contentPane.setForeground(new Color(0, 0, 0));
		contentPane.setBackground(SystemColor.menu);
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel titlePanel = new JPanel();
		titlePanel.setBackground(SystemColor.menu);
		titlePanel.setBounds(0, 0, 1600, 50);
		contentPane.add(titlePanel);
		titlePanel.setLayout(null);
		
		lblNewLabel = new JLabel("XML Editor by 16010984 한서현");
		lblNewLabel.setFont(new Font("굴림", Font.BOLD, 24));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBackground(new Color(75, 0, 130));
		lblNewLabel.setBounds(0, 0, 1588, 50);
		titlePanel.add(lblNewLabel);
		inputpanel.setBackground(SystemColor.menu);
		inputpanel.setBounds(0, 50, 800, 411);
		contentPane.add(inputpanel);
		inputpanel.setLayout(null);
		
		JPanel systemMasseagePanel = new JPanel();
		systemMasseagePanel.setBounds(0, 0, 800, 50);
		inputpanel.add(systemMasseagePanel);
		
		stateLabel = new JLabel("작업 중인 파일 이름: "+nowFile);
		stateLabel.setLabelFor(contentPane);
		systemMasseagePanel.add(stateLabel);
		stateLabel.setFont(new Font("굴림", Font.BOLD, 20));
		stateLabel.setHorizontalAlignment(SwingConstants.CENTER);
		stateLabel.setForeground(new Color(255, 140, 0));
		stateLabel.setBackground(new Color(255, 140, 0));
		
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setBounds(100, 50, 600, 300);
		inputpanel.add(buttonsPanel);
		
		
		
		JPanel textPanel = new JPanel();
		textPanel.setForeground(new Color(119, 136, 153));
		textPanel.setBounds(0, 350, 800, 61);
		inputpanel.add(textPanel);
		
		myTextField = new JTextField();
	
		myTextField.setToolTipText("input area");
		textPanel.add(myTextField);
		myTextField.setColumns(60);
		
		
	
		
		outputpanel = new JPanel();
		outputpanel.setBackground(SystemColor.menu);
		outputpanel.setBounds(800, 50, 800, 411);
		contentPane.add(outputpanel);
		outputpanel.setLayout(null);
		
		myTextArea = new JTextArea();
		myTextArea.setEditable(false);
		myTextArea.setBounds(0, 0, 761, 393);
		outputpanel.add(myTextArea);
		myTextArea.setText(">> 이곳은 텍스트가 출력되는 곳입니다. 처음에는 메뉴 <1>Load를 통해 작업할 파일을 입력해주세요.\n");
		myTextArea.setText(">> 메뉴 <1>Load에서 xsd 파일을 통한 유효성 검사를 할 경우, 체크박스를 선택한 후  파일 이름을 입력해주세요.\n");
		myTextArea.setToolTipText("output area");
		
		JScrollPane scrollPane = new JScrollPane(myTextArea);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setBounds(0, 0, 780, 405);
		outputpanel.add(scrollPane);
		
		myTextField.addActionListener (new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(textState==true) {
				myTextField = (JTextField)e.getSource();
					switch(menuNumber){
					case 1:
						myTextArea.append("현재 입력받은 파일 이름 "+myTextField.getText()+"으로 다양한 기능들을 수행할 수 있습니다.\n");
						inputLoad=myTextField.getText();
						if(validationCheck==true) {
							//유효성 검사를 할 때는 xsd 파일도 입력받아야함
//							inputFile=inputLoad.split("\\s")[0];
//							xsdFile=inputLoad.split("\\s")[1];
							inputFile=inputLoad.split("\\^")[0];
							xsdFile=inputLoad.split("\\^")[1];
						}else if(validationCheck==false) {
							inputFile=inputLoad;
						}
						NodeListTests();
						break;
					case 2:
						makeName=myTextField.getText();
						myTextArea.append("현재 입력받은 값 "+myTextField.getText()+"으로 새로운 XML 파일을 만듭니다.\n");
						makeNode();
						break;
					case 3:
						toFindValue=myTextField.getText();
						myTextArea.append(nowFile+" 파일에 있는 "+toFindValue+" 를 찾습니다.\n");
						DOMNodeFind(toFindValue);
						break;
					case 4:
						toSaveFile=myTextField.getText();
						myTextArea.append("현재 입력받은 파일 이름:"+myTextField.getText()+"으로 현재 작업 중인 파일을 저장합니다.\n");
						DOMSave();
						break;
					case 5:
						//print
						break;
					case 6:
						toInsert = myTextField.getText();
						toInsertRef = toInsert.split("\\^")[0];
						toInsertValue = toInsert.split("\\^")[1];
						//myTextArea.append(toInsert+"\n");
						myTextArea.append("넣고 싶은 위치는:"+toInsertRef+"앞이고, 넣고 싶은 값은:"+toInsertValue+"입니다.\n");
						DOMNodeInsert();
						break;
					case 7:
						toUpdate = myTextField.getText();
						toUpdateValue=toUpdate.split("\\^")[0];
						toUpdateNewName=toUpdate.split("\\^")[1];
						//myTextArea.append(toUpdate+"\n");
						myTextArea.append(toUpdateValue+"의 값을 "+toUpdateNewName+"으로 바꿉니다.\n");
						domNodeUpdate();
						break;
					case 8:
						toDelete = myTextField.getText();
						myTextArea.append("현재 작업 중인 파일에서 "+toDelete+"를 삭제합니다.\n");
						domNodeDelete();
						break;
					case 9:
						//exit
						break;
					}
				
				}
			
				myTextField.setText("");
				textState=false; //입력 막아두기
			}
		});
	
		buttonsPanel.setLayout(new GridLayout(0, 1, 0, 0));
		
		JPanel panel_1 = new JPanel();
		buttonsPanel.add(panel_1);
		
		JButton btnLoad = new JButton("1 Load");
		btnLoad.setHorizontalTextPosition(SwingConstants.CENTER);

		panel_1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JCheckBox chckbxValidation = new JCheckBox("validation");
		panel_1.add(chckbxValidation);
		
		
		btnLoad.setPreferredSize(new Dimension(150, 23));
		panel_1.add(btnLoad);
		btnLoad.setFont(new Font("굴림", Font.PLAIN, 16));
		btnLoad.setAlignmentX(Component.RIGHT_ALIGNMENT);
		
		//아래는 리스너들입니다
		
		btnLoad.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				myTextArea.append(">> 메뉴 <1>Load 이(가) 선택되었습니다. 입력한 파일의 이름으로 프로그램의 기능들을 사용할 수 있습니다.\n");
				myTextArea.append(">> 예시: xmlFilename^xsdFilename 특수문자 '^'를 사용해주세요.\n");
				myTextArea.append(">> xsd 파일을 통한 유효성 검사를 할 경우에는 xsd파일도 함께 입력해주세요.\n");
				//System.out.println("1 Load");
				menuNumber=1;
				if(textState==false) {
					
					
					if(nowFile==defaultState) {
						myTextArea.append(">> 메모리에 올려진 파일이 없습니다. 파일 이름을 먼저 입력해주세요.\n");
						
					}
					if(nowFile!=defaultState) {
						//현재 작업 중인 파일이 있을 때
						myTextArea.append(">> 현재 작업 중인 파일이 있습니다. 이 파일을 저장한 후에 새로운 파일을 만들 수 있습니다. Save 메뉴를 눌러 작업 중인 파일을 먼저 저장해주세요.\n");
						return;
					}
					if(chckbxValidation.isSelected()) {
						System.out.println("validation 체크 합시다");
						validationCheck=true;
						chckbxValidation.setSelected(false);
						
					}
					
					textState=true; //입력받기
					//System.out.println(inputString);
					//MyTextField.setEditable(true);
				}
				
						
			}
		});
		
		JPanel panel_2 = new JPanel();
		buttonsPanel.add(panel_2);

		panel_2.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
				
				JRadioButton rd2root = new JRadioButton("root");
				panel_2.add(rd2root);
				
				JRadioButton rd2ele = new JRadioButton("element");
				panel_2.add(rd2ele);
				
				JRadioButton rd2attr = new JRadioButton("attribute");
				panel_2.add(rd2attr);
				
				JRadioButton rd2comt = new JRadioButton("comment");
				panel_2.add(rd2comt);
//				
				ButtonGroup make2 = new ButtonGroup();
				make2.add(rd2root);
				make2.add(rd2ele);
				make2.add(rd2attr);
				make2.add(rd2comt);
				
				JButton btnMake = new JButton("2 Make");
				btnMake.setMargin(new Insets(0, 14, 0, 14));
				btnMake.setPreferredSize(new Dimension(150, 23));
	
				panel_2.add(btnMake);
				btnMake.setFont(new Font("굴림", Font.PLAIN, 16));
				btnMake.setAlignmentX(50.0f);
				
				btnMake.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						//System.out.println("2 Make");
						myTextArea.append(">> 메뉴 <2>Make 이(가) 선택되었습니다. 새로운 XML 파일을 만듭니다.\n");
						menuNumber=2;
						
						
						if(textState==false){
							if(nowFile!=defaultState) {
								//현재 작업 중인 파일이 있을 때
								if(rootExist==1) {
									//새로운 파일에서 루트를 만들었을 때
									if(rd2root.isSelected()) {
										myTextArea.append(">> 현재 작업 중인 파일이 있습니다. 이 파일을 저장한 후에 새로운 파일을 만들 수 있습니다. Save 메뉴를 눌러 작업 중인 파일을 먼저 저장해주세요.\n");
										return;
									}
									myTextArea.append(">> 새로운 XML 파일의  데이터 타입이 선택되었습니다. 만들 데이터의 이름을 입력해주세요.\n");
									textState=true; //입력받기
								}else {
									myTextArea.append(">> 현재 작업 중인 파일이 있습니다. 이 파일을 저장한 후에 새로운 파일을 만들 수 있습니다. Save 메뉴를 눌러 작업 중인 파일을 먼저 저장해주세요.\n");
									return;
								}
							}
							else {
							myTextArea.append(">> 새로 만들 XML 파일의  root를 입력해주세요.\n");
							textState=true; //입력받기
							}
						}
					
						if(rd2root.isSelected()) {
							valueType=1;
							//rd2root.setSelected(false);
							
						}else if(rd2ele.isSelected()) {
							valueType=2;
						}else if(rd2attr.isSelected()) {
							valueType=3;
						}else if(rd2comt.isSelected()) {
							valueType=5;
						}else {
							myTextArea.append("만들고 싶은 종류를 선택해주세요.\n");
						}
					
						make2.clearSelection(); //선택한 것들 초기화!
					}
				});
		
		
		
	
		
		JPanel panel_3 = new JPanel();
		buttonsPanel.add(panel_3);
		
		JRadioButton rd3ele = new JRadioButton("element");
		panel_3.add(rd3ele);
		
		JRadioButton rd3attr = new JRadioButton("attribute");
		panel_3.add(rd3attr);
		
		JRadioButton rd3text = new JRadioButton("text");
		panel_3.add(rd3text);
		
		
		JRadioButton rd3comt = new JRadioButton("comment");
		panel_3.add(rd3comt);
		
		
		ButtonGroup find3 = new ButtonGroup();
		find3.add(rd3ele);
		find3.add(rd3attr);
		find3.add(rd3comt);
		find3.add(rd3text);
		
		JButton btnFind = new JButton("3 Find");
		btnFind.setPreferredSize(new Dimension(150, 23));

		panel_3.add(btnFind);
		btnFind.setFont(new Font("굴림", Font.PLAIN, 16));
		btnFind.setAlignmentX(50.0f);
		
		btnFind.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				//System.out.println("3 Find");
				myTextArea.append(">> 메뉴 <3>Find 이(가) 선택되었습니다. 현재  작업 중인 파일에서 입력하는 값의 위치를 찾을 수 있습니다.\n");
				menuNumber=3;
	
				//이제 element, attribute, comment, text 중 골라야하고
				if(nowFile==defaultState) {
					myTextArea.append("메모리에 올려진 파일이 없습니다. Load 메뉴를 통해 파일 이름을 먼저 입력해주세요.\n");
					return;
				}
				
				
				if(textState==false) {
								
					
				//어떤 타입으로 만들지(1:root, 2:element, 3:attribute, 4:text, 5:comment)
					if(rd3ele.isSelected()) {
						System.out.print("rd3ele");
						valueType=2;
						myTextArea.append(">> 찾고 싶은 element 값을 입력하고 엔터를 눌러주세요.\n");
						
					}else if(rd3attr.isSelected()) {
						valueType=3;
						myTextArea.append(">> 찾고 싶은 attribute 값을 입력하고 엔터를 눌러주세요.\n");
					}else if(rd3comt.isSelected()) {
						valueType=5;
						myTextArea.append(">> 찾고 싶은 comment 값을 입력하고 엔터를 눌러주세요.\n");
					}else if(rd3text.isSelected()) {
						valueType=4;
						myTextArea.append(">> 찾고 싶은 text 값을 입력하고 엔터를 눌러주세요.\n");
					}else {
						myTextArea.append("만들고 싶은 종류를 선택해주세요.\n");
					}
					textState=true; //입력받기
				}
				
			
				find3.clearSelection(); //선택한 것들 초기화!
				
			}
		});
		
		JPanel panel_4 = new JPanel();
		buttonsPanel.add(panel_4);
		
		JButton btnSave = new JButton("4 Save");
		btnSave.setPreferredSize(new Dimension(150, 23));
		panel_4.add(btnSave);
		btnSave.setFont(new Font("굴림", Font.PLAIN, 16));
		btnSave.setAlignmentX(50.0f);
		
		
		btnSave.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				//System.out.println("4 Save");
				myTextArea.append(">> 메뉴 <4> Save 이(가) 선택되었습니다. 현재 작업 중인 파일을 다른 이름으로 저장합니다.\n");
				menuNumber=4;
				

				if(textState==false) {
					
					if(nowFile==defaultState) {
						//현재 메모리에 올려진 파일이 없을 때
						myTextArea.append(">> 메모리에 올려진 파일이 없습니다. Load 메뉴를 통해 파일 이름을 먼저 입력해주세요.\n");
						return;
					}
					textState=true;

					
				}

			}
		});
		
		JPanel panel_5 = new JPanel();
		buttonsPanel.add(panel_5);
		
		JButton btnPrint = new JButton("5 Print");
		btnPrint.setPreferredSize(new Dimension(150, 23));
		panel_5.add(btnPrint);
		btnPrint.setFont(new Font("굴림", Font.PLAIN, 16));
		btnPrint.setAlignmentX(50.0f);
		
		btnPrint.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
			
				//System.out.println("5 Print");
				myTextArea.append(">> 메뉴 <5> Print 이(가) 선택되었습니다. 현재 작업 중인 파일을 출력합니다.\n");
				System.out.println(nowFile+"~~~~~");
				menuNumber=5;
				if(nowFile==defaultState) {
					//현재 작업 중인 파일이 없을 때
					myTextArea.append("메모리에 올려진 파일이 없습니다. Load 메뉴를 통해 파일 이름을 먼저 입력해주세요.\n");
					return;
				}

				printFile=nowFile;
				Document doc=nowDoc;
				myTextArea.append("현재 작업 중인 파일의 출력 결과입니다.\n");
				traverse(doc.getDocumentElement()," ");
				myTextArea.append("\n---finish---\n");
			}
		});
		
		JPanel panel_6 = new JPanel();
		buttonsPanel.add(panel_6);
		
		JRadioButton rd6ele = new JRadioButton("element");
		panel_6.add(rd6ele);
		
		JRadioButton rd6attr = new JRadioButton("attribute");
		panel_6.add(rd6attr);
		
		JRadioButton rd6text = new JRadioButton("text");
		panel_6.add(rd6text);
		
		
		JRadioButton rd6comt = new JRadioButton("comment");
		panel_6.add(rd6comt);
		
		

		ButtonGroup insert6 = new ButtonGroup();
		insert6.add(rd6ele);
		insert6.add(rd6attr);
		insert6.add(rd6comt);
		insert6.add(rd6text);
		
		JButton btnInsert = new JButton("6 Insert");
		btnInsert.setPreferredSize(new Dimension(150, 23));
		panel_6.add(btnInsert);
		btnInsert.setFont(new Font("굴림", Font.PLAIN, 16));
		btnInsert.setAlignmentX(50.0f);
		
		btnInsert.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				myTextArea.append(">> 메뉴 <6>Insert 이(가) 선택되었습니다.\n");
				myTextArea.append(">> 예시: selectToInsertElementName^toInsertValue 특수문자'^'를 사용해주세요.\n");
				menuNumber=6;
				
				//특정 element를 넣기
				
				if(textState==false) {
					if(nowFile==defaultState) {
						//현재 메모리에 올려진 파일이 없을 때
						myTextArea.append(">> 메모리에 올려진 파일이 없습니다. Load 메뉴를 통해 파일 이름을 먼저 입력해주세요.\n");
						return;
					}

					//어떤 타입으로 만들지(1:root, 2:element, 3:attribute, 4:text, 5:comment)
					if(rd6ele.isSelected()) {
						System.out.print("rd6ele");
						valueType=2;
						myTextArea.append(">> 어떤 element 앞에 넣을지,넣고 싶은 element 값 입력하고 엔터를 눌러주세요.\n");
						
					}else if(rd6attr.isSelected()) {
						valueType=3;
						myTextArea.append(">> 어떤 element의 attribute를 만들지, 넣고 싶은 attribute 값을 입력하고 엔터를 눌러주세요.\n");
					}else if(rd6comt.isSelected()) {
						valueType=5;
						myTextArea.append(">> 어떤 element 앞에 넣을지, 넣고 싶은 comment 값을 입력하고 엔터를 눌러주세요.\n");
					}else if(rd6text.isSelected()) {
						valueType=4;
						myTextArea.append(">> 어떤 element의 text를 추가할지, 넣고 싶은 text 값을 입력하고 엔터를 눌러주세요.\n");
					}else {
						myTextArea.append("만들고 싶은 종류를 선택해주세요.\n");
					}
					textState=true; //입력받기
					
				}
				insert6.clearSelection();
			}
		});
		
		JPanel panel_7 = new JPanel();
		buttonsPanel.add(panel_7);
		
		JButton btnUpdate = new JButton("7 Update");
		btnUpdate.setPreferredSize(new Dimension(150, 23));
	
		JRadioButton rd7ele = new JRadioButton("element");
		panel_7.add(rd7ele);
		
		JRadioButton rd7attr = new JRadioButton("attribute");
		panel_7.add(rd7attr);
		
		JRadioButton rd7text = new JRadioButton("text");
		panel_7.add(rd7text);
		
		
		JRadioButton rd7comt = new JRadioButton("comment");
		panel_7.add(rd7comt);
		panel_7.add(btnUpdate);
		btnUpdate.setFont(new Font("굴림", Font.PLAIN, 16));
		
		
		ButtonGroup update7 = new ButtonGroup();
		update7.add(rd7ele);
		update7.add(rd7attr);
		update7.add(rd7comt);
		update7.add(rd7text);
		
		btnUpdate.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				myTextArea.append(">> 메뉴 <7>Update 이(가) 선택되었습니다.\n");
				myTextArea.append(">> 예시: selectToUpdateData^UpdateValue 특수문자'^'를 사용해주세요.\n");
				//특정 element의 이름을 바꾸기
				menuNumber=7;
				if(textState==false) {
					if(nowFile==defaultState) {
						//현재 메모리에 올려진 파일이 없을 때
						myTextArea.append(">> 메모리에 올려진 파일이 없습니다. Load 메뉴를 통해 파일 이름을 먼저 입력해주세요.\n");
						return;
					}
					//어떤 타입으로 만들지(1:root, 2:element, 3:attribute, 4:text, 5:comment)
					if(rd7ele.isSelected()) {
						System.out.print("rd7ele");
						valueType=2;
						myTextArea.append(">> 어떤 element를 바꿀지,바꾸고 싶은 element 값 입력하고 엔터를 눌러주세요.\n");
						
					}else if(rd7attr.isSelected()) {
						valueType=3;
						myTextArea.append(">> 어떤 attribute의 이름을 바꿀지, 바꾸고 싶은 attribute 값을 입력하고 엔터를 눌러주세요.\n");
					}else if(rd7comt.isSelected()) {
						valueType=5;
						myTextArea.append(">> 어떤 comment의 내용을 바꿀지, 바꾸고 싶은 comment 값을 입력하고 엔터를 눌러주세요.\n");
					}else if(rd7text.isSelected()) {
						valueType=4;
						myTextArea.append(">> 어떤 element의 text를 바꿀지, 바꾸고 싶은 text 값을 입력하고 엔터를 눌러주세요.\n");
					}else {
						myTextArea.append("업데이트 하고 싶은 종류를 선택해주세요.\n");
					}
					
					//myTextArea.append(">> 어떤 값을 바꿀지,바꾸고 싶은 값 입력\n");
					textState=true; //입력받기
					
				}
				update7.clearSelection();
			}
		});
		
		JPanel panel_8 = new JPanel();
		buttonsPanel.add(panel_8);
		
		JRadioButton rd8ele = new JRadioButton("element");
		panel_8.add(rd8ele);
		
		JRadioButton rd8attr = new JRadioButton("attribute");
		panel_8.add(rd8attr);
		
		JRadioButton rd8text = new JRadioButton("text");
		panel_8.add(rd8text);
		
		
		JRadioButton rd8comt = new JRadioButton("comment");
		panel_8.add(rd8comt);
		
		JButton btnDelete = new JButton("8 Delete");
		btnDelete.setPreferredSize(new Dimension(150, 23));
		panel_8.add(btnDelete);
		btnDelete.setFont(new Font("굴림", Font.PLAIN, 16));
		btnDelete.setAlignmentX(50.0f);
		
		
		ButtonGroup delete8 = new ButtonGroup();
		delete8.add(rd8ele);
		delete8.add(rd8attr);
		delete8.add(rd8comt);
		delete8.add(rd8text);
		
		btnDelete.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				myTextArea.append(">> 메뉴 <8>Delete 이(가) 선택되었습니다.\n");
				//특정 element를 없애기
				menuNumber=8;
				if(textState==false) {
					if(nowFile==defaultState) {
						//현재 메모리에 올려진 파일이 없을 때
						myTextArea.append(">> 메모리에 올려진 파일이 없습니다. Load 메뉴를 통해 파일 이름을 먼저 입력해주세요.\n");
						return;
					}
					//어떤 타입으로 만들지(1:root, 2:element, 3:attribute, 4:text, 5:comment)
					if(rd8ele.isSelected()) {
						System.out.println("rd8ele");
						valueType=2;
						myTextArea.append(">> 어떤 element를 없앨지 입력하고 엔터를 눌러주세요.\n");
						
					}else if(rd8attr.isSelected()) {
						valueType=3;
						myTextArea.append(">> 어떤 attribute를 없앨지 입력하고 엔터를 눌러주세요.\n");
					}else if(rd8comt.isSelected()) {
						valueType=5;
						myTextArea.append(">> 어떤 comment를 없앨지 입력하고 엔터를 눌러주세요.\n");
					}else if(rd8text.isSelected()) {
						valueType=4;
						myTextArea.append(">> 어떤 text를 없앨지 입력하고 엔터를 눌러주세요.\n");
					}else {
						myTextArea.append("없애고 싶은 종류를 선택해주세요.\n");
					}
					//myTextArea.append(">> 어떤 값을 없앨지 입력 \n");
					textState=true; //입력받기
					
				}
				delete8.clearSelection();
			}
		});
		
		JPanel panel_9 = new JPanel();
		buttonsPanel.add(panel_9);
		
		JButton btnExit = new JButton("9 Exit");
		btnExit.setPreferredSize(new Dimension(150, 23));
		panel_9.add(btnExit);
		btnExit.setFont(new Font("굴림", Font.PLAIN, 16));
		btnExit.setAlignmentX(50.0f);
		
		
		
		btnExit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				//메뉴 <9>exit
				if(nowFile!=defaultState) {
					//작업중인 파일이 있는 경우
					myTextArea.append("Save 메뉴를 통해 작업 중인 파일을 저장한 후에 종료해주세요.\n");
					return;
					
				}
				else {
					System.exit(0);
				}
			}
		});
		
		
	}
	public void NodeListTests() {
		// TODO Auto-generated method stub
	
		try{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			SchemaFactory schemaFactory = null;
			if (validationCheck) {
				factory.setValidating(false);
				factory.setNamespaceAware(true);
				schemaFactory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
				factory.setSchema(schemaFactory.newSchema(new Source[] { new StreamSource(xsdFile)}));
				validationCheck=false;
			}
			Document doc = builder.parse(inputFile);
			nowDoc = doc; //현재 작업 중인 document
			Node node = doc.getDocumentElement();
			NodeList children = node.getChildNodes();
			
			textState=false; //이제 입력막아두기
			nowFile=inputFile; //Load가 완료되면 현재 작업 중인 파일 이름 설정
			stateLabel.setText("현재 작업 중인 파일: "+nowFile);
			
		}catch(FactoryConfigurationError e){
		//unable to get a document builder factory
			e.printStackTrace(System.err);
			myTextArea.append("시스템 에러1\n");
		}catch(ParserConfigurationException e){
			//parser was unable to be configured
			e.printStackTrace(System.err);
			myTextArea.append("시스템 에러2\n");
		}catch(SAXException e){
			//parsing error
			e.printStackTrace(System.err);
			myTextArea.append("시스템 에러3\n");
		}catch(IOException e){
			//I/O error
			e.printStackTrace(System.err);
			myTextArea.append("시스템 에러 4. I/O error.\n");
		}
	}
	
	public void makeNode() {
		try {
			makeDoc = new DocumentImpl();
			Document doc = makeDoc;
			Node root = null;
			switch(valueType) {
			case 1:
				//root
				root = doc.createElement(makeName);
				root.appendChild(doc.createTextNode("\n"));
				//파일을 만들었으면
				nowFile = makeName + newFileworking;
				stateLabel.setText("현재 작업 중인 파일: "+nowFile);
				nowDoc = makeDoc; //현재 작업 중인 document도 변경
				nowDoc.appendChild(root);
				makeDoc =null; //초기화
				rootExist=1; //root 존재
				break;
			case 2:
				//element
				if(rootExist==0) {
					myTextArea.append("root를 먼저 만들어주세요\n");
					break;
				}
				//Element item = doc.createElement(makeName);
				//item.appendChild(doc.createTextNode(makeName+"'s text"));
				//root.appendChild(item);
				myTextArea.append("Make element는 Insert 메뉴를 이용해주세요.\n");
				
				break;
			case 3:
				//attr
				if(rootExist==0) {
					myTextArea.append("root를 먼저 만들어주세요\n");
					break;
				}
				myTextArea.append("Make attribute는 Insert 메뉴를 이용해주세요.\n");
				break;
			case 4:
				//text
				if(rootExist==0) {
					myTextArea.append("root를 먼저 만들어주세요\n");
					break;
				}
				myTextArea.append("Make text는 Insert 메뉴를 이용해주세요.\n");
				break;
			case 5:
				//comment
				if(rootExist==0) {
					myTextArea.append("root를 먼저 만들어주세요\n");
					break;
				}
				myTextArea.append("Make Comment는 Insert 메뉴를 이용해주세요.\n");
				break;
			}
			
			valueType=0;
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
	}
	
	
	public void DOMSave() {
		try {
			Document doc = nowDoc; //현재 작업 중인 파일
			if(makeDoc != null) {
				//새로운 파일이 있다면, 새로운 파일로 대체하기
				doc = makeDoc;
			}

		
			
		OutputFormat format = new OutputFormat(doc);
		StringWriter stringOut = new StringWriter();
		XMLSerializer serial = new XMLSerializer(stringOut, format);
		serial.asDOMSerializer();
		serial.serialize(doc.getDocumentElement());
		
		
		//FileWriter fw = new FileWriter(toSaveFile);
		
		//한글이 깨지지 않게 UTF-8 설정
		FileOutputStream output=new FileOutputStream(toSaveFile,false);
        OutputStreamWriter writer=new OutputStreamWriter(output,"UTF-8");
		BufferedWriter bw = new BufferedWriter(writer);
		bw.write(stringOut.toString());
		bw.close();
		} catch (Exception ex) {
		ex.printStackTrace();
		}
		
		//Save가 완료되었으면 초기화
		textState=false; //이제 입력막아두기
		nowFile = defaultState; //Save가 완료되면 현재 작업 중인 파일 텍스트 초기화
		nowDoc = null;
		makeDoc = null;
		stateLabel.setText("현재 작업 중인 파일: "+nowFile);
		
	}

	public void DOMNodeFind(String findNode){

			Document doc = nowDoc;
			System.out.println(valueType+"number~~");
			switch(valueType) {
			case 2:
				System.out.println("find ele");
				nodeFind(doc.getDocumentElement(),findNode);
				break;
			case 3:
				System.out.println("find attr");
				nodeFindAttr(doc.getDocumentElement(),findNode);
				break;
			case 4:
				System.out.println("find text");
				nodeFindText(doc.getDocumentElement(),findNode);
				break;
			case 5:
				System.out.println("find comt");
				nodeFindComt(doc.getDocumentElement(),findNode);
				break;
			}

			if(findflag==0) {
				myTextArea.append("해당 값을 찾을 수 없습니다.\n");
			}
			//노드를 잘 찾은 후에 초기화
			findflag=0; //값이 없는 경우
			textState=false; //입력 받지 않기
			valueType=0;
	}
	
	public int findflag=0; //해당하는 값을 찾으면:1, 해당 하는 값이 없으면: 0
	
	public void nodeFind (Node node, String eleName){
	
		if(node == null) return;
		
		if(node.getNodeName().equals(eleName)) {
			//루트
			myTextArea.append("["+getDepth(node)+","+getSiblingIndex(node)+"] "+node.getNodeName()+"\n");
			findflag=1;
			return;
		}
		NodeList children = node.getChildNodes();
		
		for(int i=0;i<children.getLength();i++){
			Node child = children.item(i);
			
			if(child.getNodeName().equals(eleName)){
				myTextArea.append("["+getDepth(child)+","+getSiblingIndex(child)+"] "+child.getNodeName()+" "+child.getTextContent()+"\n");
				findflag=1;
			}
			
			nodeFind(child,eleName);
		}
	} //print(Node)
	
	public void nodeFindAttr (Node node, String attrName){
		
		if(node == null) return;
		
		NodeList children = node.getChildNodes();
		
		for(int i=0;i<children.getLength();i++){
			Node child = children.item(i);
			//System.out.println("["+getDepth(child)+","+getSiblingIndex(child)+"] "+child.getNodeName()+"\n");
			if(child.hasAttributes()) {
				NamedNodeMap attr = child.getAttributes();//node에서 child로 수정
				//System.out.println("["+getDepth(child)+","+getSiblingIndex(child)+"] "+child.getNodeName()+"\n");
				//System.out.print("has attributes");
				for(int j = 0;j<attr.getLength();j++) {
//					System.out.println(" "+indent+"[Attribute]"+attr.item(i).getNodeName()+"="+attr.item(i).getNodeValue());
					//System.out.print(j);
					//System.out.println(attr.item(j).getNodeName());
					if(attr.item(j).getNodeName().equals(attrName)) {
						myTextArea.append("["+getDepth(attr.item(j))+","+getSiblingIndex(attr.item(j))+"] "+attr.item(j).getNodeName()+" " + attr.item(j).getTextContent()+"\n");
						findflag=1;
					}
					
				}
			}
			nodeFindAttr(child,attrName);
		}
	} //print(Node)
	
	public void nodeFindComt (Node node, String comtName){
			
			if(node == null) return;
			
			NodeList children = node.getChildNodes();
			
			for(int i=0;i<children.getLength();i++){
				Node child = children.item(i);
				//System.out.println("["+getDepth(child)+","+getSiblingIndex(child)+"] "+child.getNodeName()+"\n");
				if(child.getNodeType()==Node.COMMENT_NODE) {
					if(child.getTextContent().equals(comtName)) {
					myTextArea.append("["+getDepth(child)+","+getSiblingIndex(child)+"] "+child.getNodeName()+" "+child.getTextContent()+"\n");
					findflag=1;			
					}
				}
				
				nodeFindComt(child,comtName);
			}
		} //print(Node)
	
	public void nodeFindText (Node node, String textName){
		
		if(node == null) return;
		
		NodeList children = node.getChildNodes();
		
		for(int i=0;i<children.getLength();i++){
			Node child = children.item(i);
			//System.out.println("["+getDepth(child)+","+getSiblingIndex(child)+"] "+child.getNodeName()+"\n");
			if(child.getNodeType()==Node.TEXT_NODE) {
				if(child.getTextContent().equals(textName)) {
				myTextArea.append("["+getDepth(child)+","+getSiblingIndex(child)+"] "+child.getNodeName()+" "+child.getTextContent()+"\n");
				findflag=1;			
				}
			}
			
			nodeFindText(child,textName);
		}
	} //print(Node)

	//Calculate the depth of the current element
	public int getDepth(Node node){
			int index=0;
			while((node = node.getParentNode())!= null)
				index++;
			return index;
		}
		
		
	//Calculate the element index for each depth(starting from 1)
	protected int getSiblingIndex(Node node){
		int index=1;
		
		while((node=node.getPreviousSibling())!=null)
			if(node.getNodeType()!= Node.TEXT_NODE &&node.getNodeType()!=Node.COMMENT_NODE)
				index++;
		
		return index;
	}
	
	public void traverse(Node node,String indent){
		if(node == null)
			return;
		int type = node.getNodeType();
		switch(type) {
		case Node.DOCUMENT_NODE:
//			System.out.println(indent+"[Document]"+node.getNodeName());
			myTextArea.append(indent+"[Document]"+node.getNodeName());
			break;
		case Node.ELEMENT_NODE:
//			System.out.println(indent+"[Element]"+node.getNodeName());
			myTextArea.append(indent+"[Element]"+node.getNodeName());
			if(node.hasAttributes()) {
				NamedNodeMap attr = node.getAttributes();
				for(int i = 0;i<attr.getLength();i++) {
//					System.out.println(" "+indent+"[Attribute]"+attr.item(i).getNodeName()+"="+attr.item(i).getNodeValue());
					myTextArea.append(" "+indent+"[Attribute]"+attr.item(i).getNodeName()+"="+attr.item(i).getNodeValue());
				}
			}
			break;
		case Node.CDATA_SECTION_NODE:
//			System.out.print(indent+"[CDATA_SECTION]");
//			System.out.print(node.getNodeName());
//			System.out.println(" "+node.getNodeValue());
			myTextArea.append(indent+"[CDATA_SECTION]"+node.getNodeName()+" "+node.getNodeValue());
			break;
		case Node.COMMENT_NODE:
//			System.out.print(indent+"[COMMENT]");
//			System.out.print(node.getNodeName());
//			System.out.println(" "+node.getNodeValue());
			myTextArea.append(indent+"[COMMENT]"+node.getNodeName()+" "+node.getNodeValue());
			break;
		case Node.TEXT_NODE:
//			System.out.print(indent+"[TEXT]");
//			System.out.print(node.getNodeName());
//			System.out.println(" "+node.getNodeValue());
			myTextArea.append(indent+"[TEXT]"+node.getNodeName()+" "+node.getNodeValue());
			break;
			}
		NodeList children = node.getChildNodes();
		if(children != null) {
			int len = children.getLength();
			for(int i=0;i<len;i++)
				traverse(children.item(i),indent+" ");
		}
		
	}
	

	
	public void DOMNodeInsert(){
		
		// 참고 링크
		// https://stackoverflow.com/questions/3247577/how-can-i-insert-element-into-xml-after-before-certain-element-in-java

			Document doc = nowDoc;
			
			System.out.println(valueType+"number~~");
			switch(valueType) {
			case 2:
				System.out.println("insert ele");
				nodeFindforInsert(doc.getDocumentElement(),toInsertRef,doc);
				break;
			case 3:
				System.out.println("insert attr");
				nodeFindforInsertAttr(doc.getDocumentElement(),toInsertRef,doc);
				break;
			case 4:
				System.out.println("insert text");
				nodeFindforInsertText(doc.getDocumentElement(),toInsertRef,doc);
				break;
			case 5:
				System.out.println("find comt");
				nodeFindforInsertComt(doc.getDocumentElement(),toInsertRef,doc);
				break;
			}
			
//			NodeList nodes = doc.getElementsByTagName(toInsertRef);
//			Element p = doc.createElement(toInsertValue);
//			p.appendChild(doc.createTextNode(toInsertValue+"'s value"));
//			
//			nodes.item(0).getParentNode().insertBefore(p, nodes.item(0));
			//이렇게 하면 너무 잘되지만 하나만 된다.
			
			//넣고 싶은 위치 찾기
			//nodeFindforInsert(doc.getDocumentElement(),toInsertRef,doc);
			if(findflag==0) {
				myTextArea.append("해당 값을 찾을 수 없습니다.\n");
		}
//			
			//노드를 잘 찾은 후에 초기화
			findflag=0; //값이 없는 경우
			textState=false; //입력 받지 않기
			nowDoc = doc;
			valueType=0;
			//makeDoc = doc; 

		
	}
	
	public void nodeFindforInsert (Node node, String eleName,Document doc){
		if(node == null) return;
		if(node.getNodeName().equals(eleName)) {
			//루트
			//myTextArea.append("["+getDepth(node)+","+getSiblingIndex(node)+"] "+node.getNodeName()+"\n");
			findflag=1;
			Element p = doc.createElement(toInsertValue);
			p.appendChild(doc.createTextNode(" "));
			//node.getParentNode().insertBefore(p, node);
			node.appendChild(p);
			myTextArea.append(eleName+"은 루트이고, 루트에는 자식만 만들 수 있습니다. "+toInsertValue+"를 자식으로 만듭니다.\n");
			myTextArea.append("["+getDepth(p)+","+getSiblingIndex(p)+"] "+p.getNodeName()+" "+p.getTextContent()+"\n");
			return;
		}
		NodeList children = node.getChildNodes();

		for(int i=0;i<children.getLength();i++){
			Node child = children.item(i);

			if(child.getNodeName().equals(eleName)){

				findflag=1;

				Element p = doc.createElement(toInsertValue);
				p.appendChild(doc.createTextNode(" "));
				child.getParentNode().insertBefore(p, child);
				myTextArea.append("["+getDepth(p)+","+getSiblingIndex(p)+"] "+p.getNodeName()+" "+p.getTextContent()+"\n");
				return;
				//이유는 잘 모르겠지만 return 하니까 아주 잘 된다...
			}
			
			nodeFindforInsert(child,eleName,doc);
		}
	} //print(Node)
	

	public void nodeFindforInsertAttr (Node node, String eleName,Document doc){
		if(node == null) return;
		if(node.getNodeName().equals(eleName)) {
			//루트
			findflag=1;
			myTextArea.append("XML의 루트에는 attribute를 만들 수 없습니다.\n");
			return;
		}
		NodeList children = node.getChildNodes();

		for(int i=0;i<children.getLength();i++){
			Node child = children.item(i);

			if(child.getNodeName().equals(eleName)){

				findflag=1;
				((Element) child).setAttribute(toInsertValue,toInsertValue+"'s value");
				myTextArea.append("["+getDepth(child)+","+getSiblingIndex(child)+"] "+child.getNodeName()+" "
				+child.getTextContent()+" "+toInsertValue+" "+toInsertValue+"'s value"+"\n");
				return;
			}
			
			nodeFindforInsertAttr(child,eleName,doc);
		}
	} //print(Node)
	
	
	public void nodeFindforInsertText (Node node, String eleName,Document doc){
		if(node == null) return;
		if(node.getNodeName().equals(eleName)) {
			//루트
			findflag=1;
			node.appendChild(doc.createTextNode(toInsertValue));
			//node.setTextContent(node.getTextContent()+" "+toInsertValue+" is insert");
			myTextArea.append("["+getDepth(node)+","+getSiblingIndex(node)+"] "+node.getNodeName()+"\n");
			return;
		}
		NodeList children = node.getChildNodes();

		for(int i=0;i<children.getLength();i++){
			Node child = children.item(i);

			if(child.getNodeName().equals(eleName)){
				
				findflag=1;
				child.setTextContent(child.getTextContent()+" "+toInsertValue);
				myTextArea.append("["+getDepth(child)+","+getSiblingIndex(child)+"] "+child.getNodeName()+"\n");
				return;
			}
			
			nodeFindforInsertText(child,eleName,doc);
		}
	} //print(Node)
	
	public void nodeFindforInsertComt (Node node, String eleName,Document doc){
		if(node == null) return;
		
		if(node.getNodeName().equals(eleName)) {
			//루트
			findflag=1;
			Comment p = doc.createComment(toInsertValue);
			node.getParentNode().insertBefore(p, node);
			myTextArea.append("["+getDepth(node)+","+getSiblingIndex(node)+"] "+node.getNodeName()+" "+p.getTextContent()+"\n");
			return;
		}
		NodeList children = node.getChildNodes();

		for(int i=0;i<children.getLength();i++){
			Node child = children.item(i);

			if(child.getNodeName().equals(eleName)){
				findflag=1;
				Comment p = doc.createComment(toInsertValue);
				child.getParentNode().insertBefore(p, child);
				myTextArea.append("["+getDepth(child)+","+getSiblingIndex(child)+"] "+child.getNodeName()+" "+p.getTextContent()+"\n");
				return;
			}
			
			nodeFindforInsertComt(child,eleName,doc);
		}
	} //print(Node)
	
	
	public void domNodeUpdate(){

		//https://www.journaldev.com/901/modify-xml-file-in-java-dom-parser
		
		Document doc = nowDoc;
		System.out.println(valueType+"number~~");
		switch(valueType) {
		case 2:
			System.out.println("update ele");
			nodeFindforUpdateEle(doc.getDocumentElement(),toUpdateValue,doc);
			break;
		case 3:
			System.out.println("update attr");
			nodeFindforUpdateAttr(doc.getDocumentElement(),toUpdateValue,doc);
			break;
		case 4:
			System.out.println("update text");
			nodeFindforUpdateText(doc.getDocumentElement(),toUpdateValue,doc);
			break;
		case 5:
			System.out.println("update comt");
			nodeFindforUpdateComt(doc.getDocumentElement(),toUpdateValue);
			break;
		
		}
		

		if(findflag==0) {
			myTextArea.append("해당 값을 찾을 수 없습니다.\n");
		}
		//노드를 잘 찾은 후에 초기화
		findflag=0; //값이 없는 경우
		textState=false; //입력 받지 않기
		nowDoc = doc;
		valueType=0;
}


	public void nodeFindforUpdateEle (Node node, String eleName,Document doc){
	
	if(node == null) return;
	
	if(node.getNodeName().equals(eleName)) {
		//루트
		findflag=1;
		doc.renameNode(node, null, toUpdateNewName);	
		return;
	}
	
	NodeList children = node.getChildNodes();

	for(int i=0;i<children.getLength();i++){
		Node child = children.item(i);
		
		if(child.getNodeName().equals(eleName)){
			
			findflag=1;
//			System.out.println(child.getNodeName()); //엘리먼트 이름 잘 나오고
//			System.out.println(child.getNodeValue()); //null
//			System.out.println(child.getTextContent()); //엘리먼트의 text가 잘 나오고
			doc.renameNode(child, null, toUpdateNewName);	
			myTextArea.append("["+getDepth(child)+","+getSiblingIndex(child)+"] "+child.getNodeName()+" "+child.getTextContent()+"\n");

			}
		
		nodeFindforUpdateEle(child,eleName,doc);
		}
	} //print(Node)

	
	public void nodeFindforUpdateAttr (Node node, String attrName,Document doc){
		if(node == null) return;
		
		NodeList children = node.getChildNodes();

		for(int i=0;i<children.getLength();i++){
			Node child = children.item(i);
			
			if(child.hasAttributes()) {
					NamedNodeMap attr = child.getAttributes();//node에서 child로 수정
			
					for(int j = 0;j<attr.getLength();j++) {
//						System.out.println(" "+indent+"[Attribute]"+attr.item(i).getNodeName()+"="+attr.item(i).getNodeValue());
						//System.out.print(j);
						//System.out.println(attr.item(j).getNodeName());
						if(attr.item(j).getNodeName().equals(attrName)) {
							findflag=1;
							doc.renameNode(attr.item(j),null,toUpdateNewName);
							myTextArea.append("["+getDepth(attr.item(j))+","+getSiblingIndex(attr.item(j))+"] "+attr.item(j).getNodeName()+" " + attr.item(j).getTextContent()+"\n");
						}

					}
			}
			nodeFindforUpdateAttr(child,attrName,doc);
		}
	} //print(Node)
	
	public void nodeFindforUpdateText (Node node, String eleName,Document doc){
		
		if(node == null) return;
		
		NodeList children = node.getChildNodes();
		for(int i=0;i<children.getLength();i++){
			Node child = children.item(i);
			
			//if(child.getNodeType()==Node.TEXT_NODE) {
				if(child.getNodeName().equals(eleName)){
					
					findflag=1;
					//System.out.println(child.getNodeName()); //엘리먼트 이름 잘 나오고
					//System.out.println(child.getNodeValue()); //null
					//System.out.println(child.getTextContent()); //엘리먼트의 text가 잘 나오고
					//doc.renameNode(child, null, toUpdateNewName);		
					child.setTextContent(toUpdateNewName); //엘리먼트 안에 있는 값(텍스트)을 해당 값으로 바꿈
					myTextArea.append("["+getDepth(child)+","+getSiblingIndex(child)+"] "+child.getNodeName()+" "+child.getTextContent()+"\n");
					}
				
				nodeFindforUpdateText(child,eleName,doc);
				
		}
	} //print(Node)	
		
	
	public void nodeFindforUpdateComt (Node node, String comtName){
		
		if(node == null) return;
		
		NodeList children = node.getChildNodes();
		
		for(int i=0;i<children.getLength();i++){
			Node child = children.item(i);
			//System.out.println("["+getDepth(child)+","+getSiblingIndex(child)+"] "+child.getNodeName()+"\n");
			if(child.getNodeType()==Node.COMMENT_NODE) {
				if(child.getTextContent().equals(comtName)) {
					//해당 코멘트를 찾았다!
					child.setTextContent(toUpdateNewName);
					myTextArea.append("["+getDepth(child)+","+getSiblingIndex(child)+"] "+child.getNodeName()+" "+child.getTextContent()+"\n");
					findflag=1;			
				}
			}
			
			nodeFindforUpdateComt(child,comtName);
		}
	} //print(Node)
	
	public void domNodeDelete(){

		//https://www.journaldev.com/901/modify-xml-file-in-java-dom-parser
		
		Document doc = nowDoc;
		System.out.println(valueType+"number~~");
		switch(valueType) {
		case 2:
			System.out.println("delete ele");
			nodeFindforDeleteEle(doc.getDocumentElement(),toDelete);
			break;
		case 3:
			System.out.println("delete attr");
			nodeFindforDeleteAttr(doc.getDocumentElement(),toDelete);
			break;
		case 4:
			System.out.println("delete text");
			nodeFindforDeleteText(doc.getDocumentElement(),toDelete);
			break;
		case 5:
			System.out.println("delete comt");
			nodeFindforDeleteComt(doc.getDocumentElement(),toDelete);
			break;
		
		}

		if(findflag==0) {
			myTextArea.append("해당 값을 찾을 수 없습니다.\n");
		}
		//노드를 잘 찾은 후에 초기화
		findflag=0; //값이 없는 경우
		textState=false; //입력 받지 않기
		nowDoc = doc;
		valueType=0;
	
}

	public void nodeFindforDeleteEle (Node node, String eleName){
	
	if(node == null) return;
	
	if(node.getNodeName().equals(eleName)) {
		//루트
		myTextArea.append("XML 파일의 root는 없앨 수 없습니다.\n");
		findflag=1;
		return;
	}
	
	NodeList children = node.getChildNodes();

	for(int i=0;i<children.getLength();i++){
		Node child = children.item(i);
		
		if(child.getNodeName().equals(eleName)){
			myTextArea.append("["+getDepth(child)+","+getSiblingIndex(child)+"] "+child.getNodeName()+" "+child.getTextContent()+"\n");
			findflag=1;
//			System.out.println(child.getNodeName()); //엘리먼트 이름 잘 나오고
//			System.out.println(child.getNodeValue()); //null
//			System.out.println(child.getTextContent()); //엘리먼트의 text가 잘 나오고
			node.removeChild(child);
			return;
			}
		
		nodeFindforDeleteEle(child,eleName);
		}
	} //print(Node)
	
	
	public void nodeFindforDeleteAttr (Node node, String attrName){
		
		if(node == null) return;
		
		NodeList children = node.getChildNodes();

		for(int i=0;i<children.getLength();i++){
			Node child = children.item(i);
			
			if(child.hasAttributes()) {
				NamedNodeMap attr = child.getAttributes();//node에서 child로 수정
				//System.out.println("["+getDepth(child)+","+getSiblingIndex(child)+"] "+child.getNodeName()+"\n");
				//System.out.print("has attributes");
				for(int j = 0;j<attr.getLength();j++) {
//					System.out.println(" "+indent+"[Attribute]"+attr.item(i).getNodeName()+"="+attr.item(i).getNodeValue());
					//System.out.print(j);
					//System.out.println(attr.item(j).getNodeName());
					if(attr.item(j).getNodeName().equals(attrName)) {
						findflag=1;
						attr.removeNamedItem(attr.item(j).getNodeName());
						//myTextArea.append("["+getDepth(attr.item(j))+","+getSiblingIndex(attr.item(j))+"] "+attr.item(j).getNodeName()+" " + attr.item(j).getTextContent()+"\n");
						//위에걸 출력하면 error가 뜬다. 당연하지 값이 없으니까.
					}
					
				}
			}
			nodeFindforDeleteAttr(child,attrName);
			}
		} //print(Node)
	
	
	public void nodeFindforDeleteText (Node node, String textName){
		
		if(node == null) return;
		
		NodeList children = node.getChildNodes();
		
		for(int i=0;i<children.getLength();i++){
			Node child = children.item(i);
			//System.out.println("["+getDepth(child)+","+getSiblingIndex(child)+"] "+child.getNodeName()+"\n");
			if(child.getNodeType()==Node.TEXT_NODE) {
				if(child.getTextContent().equals(textName)) {
				//myTextArea.append("["+getDepth(child)+","+getSiblingIndex(child)+"] "+child.getNodeName()+" "+child.getTextContent()+"\n");
				findflag=1;	
				child.getParentNode().removeChild(child);
				}
			}
			
			nodeFindforDeleteText(child,textName);
		}
	} //print(Node)

	public void nodeFindforDeleteComt (Node node, String comtName){
		
		if(node == null) return;
		
		NodeList children = node.getChildNodes();
		
		for(int i=0;i<children.getLength();i++){
			Node child = children.item(i);
			//System.out.println("["+getDepth(child)+","+getSiblingIndex(child)+"] "+child.getNodeName()+"\n");
			if(child.getNodeType()==Node.COMMENT_NODE) {
				if(child.getTextContent().equals(comtName)) {
				//myTextArea.append("["+getDepth(child)+","+getSiblingIndex(child)+"] "+child.getNodeName()+" "+child.getTextContent()+"\n");
				findflag=1;	
				node.removeChild(child);
				}
			}
			
			nodeFindforDeleteComt(child,comtName);
		}
	} //print(Node)
	
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					XML01_HW5_16010984 frame = new XML01_HW5_16010984();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		
	}
}

