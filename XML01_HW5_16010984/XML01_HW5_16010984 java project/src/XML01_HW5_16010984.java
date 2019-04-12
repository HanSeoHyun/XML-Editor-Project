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


// 181210 ���� 1�� 43�� 
// 1,3,4,5,6,7,9 �Ϻ��ϰ� �� ��
// ��ȿ�� �˻簡 �ʿ���

public class XML01_HW5_16010984 extends JFrame {

	private JPanel contentPane;
	private final JPanel inputpanel = new JPanel();
	private JPanel outputpanel;
	private JLabel lblNewLabel;
	private JTextField myTextField;
	private JTextArea myTextArea;
	private JLabel stateLabel;
	
	private static boolean validationCheck = false; //validation �˻�
	private static String xsdFile;
	static int menuNumber=0; 
	
	static String loadFileName; // Load 1 �޴����� �Է¹��� ���� �̸�
	static Document nowDoc; //���� �۾� ���� ���� doc
	static String makeName; // Make 2 �޴����� �Է¹��� �� �̸�
	static int valueType=0; // � Ÿ������ ���� (1:root, 2:element, 3:attribute, 4:text, 5:comment)
	static int rootExist =0 ; //��Ʈ�� ������:1, ������:0
	static String toFindValue; // Find 3 �޴����� �� ���� ã���� ����
	static String toSaveFile; // Save 4 �޴����� �� �̸����� ���� ����
	static String printFile; // print 5 �޴����� ����� ���� �̸�
	static String toInsert; // insert 6 �޴����� �Է¹��� ��
	static String toInsertValue; // insert 6 �޴����� ���� ��
	static String toInsertRef; // insert 6�޴����� ���� ��ġ
	static String toUpdate;//update 7 �޴����� �Է¹��� ��
	static String toUpdateValue; //update 7 �޴����� �ٲٰ� ���� ��
	static String toUpdateNewName; //update 7 ���� ���� �ٲ� ��
	static String toUpdateNewValue; //update 7 ���� �ٲٰ� ���� ��
	static String toDelete; //delete 8 ���� ����� ���� ��
	
	private Document makeDoc; //���� �۾� ���� ����
	static String defaultState = "���� �۾� ���� ������ �����ϴ�."; //�۾� ���� ������ ���� �� �⺻ �޽���
	static String nowFile = defaultState; //���� ������ ����
	static String inputFile; //�Է¹��� �ؽ�Ʈ �̸�.
	static String inputLoad;
	static boolean textState = false; //textState�� true�� ���� �Է��� ����Ѵ�.
	static String newFileworking ="�� root�� �ϴ� ���ο� ������ �۾� ���Դϴ�.";
	
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
		
		lblNewLabel = new JLabel("XML Editor by 16010984 �Ѽ���");
		lblNewLabel.setFont(new Font("����", Font.BOLD, 24));
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
		
		stateLabel = new JLabel("�۾� ���� ���� �̸�: "+nowFile);
		stateLabel.setLabelFor(contentPane);
		systemMasseagePanel.add(stateLabel);
		stateLabel.setFont(new Font("����", Font.BOLD, 20));
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
		myTextArea.setText(">> �̰��� �ؽ�Ʈ�� ��µǴ� ���Դϴ�. ó������ �޴� <1>Load�� ���� �۾��� ������ �Է����ּ���.\n");
		myTextArea.setText(">> �޴� <1>Load���� xsd ������ ���� ��ȿ�� �˻縦 �� ���, üũ�ڽ��� ������ ��  ���� �̸��� �Է����ּ���.\n");
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
						myTextArea.append("���� �Է¹��� ���� �̸� "+myTextField.getText()+"���� �پ��� ��ɵ��� ������ �� �ֽ��ϴ�.\n");
						inputLoad=myTextField.getText();
						if(validationCheck==true) {
							//��ȿ�� �˻縦 �� ���� xsd ���ϵ� �Է¹޾ƾ���
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
						myTextArea.append("���� �Է¹��� �� "+myTextField.getText()+"���� ���ο� XML ������ ����ϴ�.\n");
						makeNode();
						break;
					case 3:
						toFindValue=myTextField.getText();
						myTextArea.append(nowFile+" ���Ͽ� �ִ� "+toFindValue+" �� ã���ϴ�.\n");
						DOMNodeFind(toFindValue);
						break;
					case 4:
						toSaveFile=myTextField.getText();
						myTextArea.append("���� �Է¹��� ���� �̸�:"+myTextField.getText()+"���� ���� �۾� ���� ������ �����մϴ�.\n");
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
						myTextArea.append("�ְ� ���� ��ġ��:"+toInsertRef+"���̰�, �ְ� ���� ����:"+toInsertValue+"�Դϴ�.\n");
						DOMNodeInsert();
						break;
					case 7:
						toUpdate = myTextField.getText();
						toUpdateValue=toUpdate.split("\\^")[0];
						toUpdateNewName=toUpdate.split("\\^")[1];
						//myTextArea.append(toUpdate+"\n");
						myTextArea.append(toUpdateValue+"�� ���� "+toUpdateNewName+"���� �ٲߴϴ�.\n");
						domNodeUpdate();
						break;
					case 8:
						toDelete = myTextField.getText();
						myTextArea.append("���� �۾� ���� ���Ͽ��� "+toDelete+"�� �����մϴ�.\n");
						domNodeDelete();
						break;
					case 9:
						//exit
						break;
					}
				
				}
			
				myTextField.setText("");
				textState=false; //�Է� ���Ƶα�
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
		btnLoad.setFont(new Font("����", Font.PLAIN, 16));
		btnLoad.setAlignmentX(Component.RIGHT_ALIGNMENT);
		
		//�Ʒ��� �����ʵ��Դϴ�
		
		btnLoad.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				myTextArea.append(">> �޴� <1>Load ��(��) ���õǾ����ϴ�. �Է��� ������ �̸����� ���α׷��� ��ɵ��� ����� �� �ֽ��ϴ�.\n");
				myTextArea.append(">> ����: xmlFilename^xsdFilename Ư������ '^'�� ������ּ���.\n");
				myTextArea.append(">> xsd ������ ���� ��ȿ�� �˻縦 �� ��쿡�� xsd���ϵ� �Բ� �Է����ּ���.\n");
				//System.out.println("1 Load");
				menuNumber=1;
				if(textState==false) {
					
					
					if(nowFile==defaultState) {
						myTextArea.append(">> �޸𸮿� �÷��� ������ �����ϴ�. ���� �̸��� ���� �Է����ּ���.\n");
						
					}
					if(nowFile!=defaultState) {
						//���� �۾� ���� ������ ���� ��
						myTextArea.append(">> ���� �۾� ���� ������ �ֽ��ϴ�. �� ������ ������ �Ŀ� ���ο� ������ ���� �� �ֽ��ϴ�. Save �޴��� ���� �۾� ���� ������ ���� �������ּ���.\n");
						return;
					}
					if(chckbxValidation.isSelected()) {
						System.out.println("validation üũ �սô�");
						validationCheck=true;
						chckbxValidation.setSelected(false);
						
					}
					
					textState=true; //�Է¹ޱ�
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
				btnMake.setFont(new Font("����", Font.PLAIN, 16));
				btnMake.setAlignmentX(50.0f);
				
				btnMake.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						//System.out.println("2 Make");
						myTextArea.append(">> �޴� <2>Make ��(��) ���õǾ����ϴ�. ���ο� XML ������ ����ϴ�.\n");
						menuNumber=2;
						
						
						if(textState==false){
							if(nowFile!=defaultState) {
								//���� �۾� ���� ������ ���� ��
								if(rootExist==1) {
									//���ο� ���Ͽ��� ��Ʈ�� ������� ��
									if(rd2root.isSelected()) {
										myTextArea.append(">> ���� �۾� ���� ������ �ֽ��ϴ�. �� ������ ������ �Ŀ� ���ο� ������ ���� �� �ֽ��ϴ�. Save �޴��� ���� �۾� ���� ������ ���� �������ּ���.\n");
										return;
									}
									myTextArea.append(">> ���ο� XML ������  ������ Ÿ���� ���õǾ����ϴ�. ���� �������� �̸��� �Է����ּ���.\n");
									textState=true; //�Է¹ޱ�
								}else {
									myTextArea.append(">> ���� �۾� ���� ������ �ֽ��ϴ�. �� ������ ������ �Ŀ� ���ο� ������ ���� �� �ֽ��ϴ�. Save �޴��� ���� �۾� ���� ������ ���� �������ּ���.\n");
									return;
								}
							}
							else {
							myTextArea.append(">> ���� ���� XML ������  root�� �Է����ּ���.\n");
							textState=true; //�Է¹ޱ�
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
							myTextArea.append("����� ���� ������ �������ּ���.\n");
						}
					
						make2.clearSelection(); //������ �͵� �ʱ�ȭ!
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
		btnFind.setFont(new Font("����", Font.PLAIN, 16));
		btnFind.setAlignmentX(50.0f);
		
		btnFind.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				//System.out.println("3 Find");
				myTextArea.append(">> �޴� <3>Find ��(��) ���õǾ����ϴ�. ����  �۾� ���� ���Ͽ��� �Է��ϴ� ���� ��ġ�� ã�� �� �ֽ��ϴ�.\n");
				menuNumber=3;
	
				//���� element, attribute, comment, text �� �����ϰ�
				if(nowFile==defaultState) {
					myTextArea.append("�޸𸮿� �÷��� ������ �����ϴ�. Load �޴��� ���� ���� �̸��� ���� �Է����ּ���.\n");
					return;
				}
				
				
				if(textState==false) {
								
					
				//� Ÿ������ ������(1:root, 2:element, 3:attribute, 4:text, 5:comment)
					if(rd3ele.isSelected()) {
						System.out.print("rd3ele");
						valueType=2;
						myTextArea.append(">> ã�� ���� element ���� �Է��ϰ� ���͸� �����ּ���.\n");
						
					}else if(rd3attr.isSelected()) {
						valueType=3;
						myTextArea.append(">> ã�� ���� attribute ���� �Է��ϰ� ���͸� �����ּ���.\n");
					}else if(rd3comt.isSelected()) {
						valueType=5;
						myTextArea.append(">> ã�� ���� comment ���� �Է��ϰ� ���͸� �����ּ���.\n");
					}else if(rd3text.isSelected()) {
						valueType=4;
						myTextArea.append(">> ã�� ���� text ���� �Է��ϰ� ���͸� �����ּ���.\n");
					}else {
						myTextArea.append("����� ���� ������ �������ּ���.\n");
					}
					textState=true; //�Է¹ޱ�
				}
				
			
				find3.clearSelection(); //������ �͵� �ʱ�ȭ!
				
			}
		});
		
		JPanel panel_4 = new JPanel();
		buttonsPanel.add(panel_4);
		
		JButton btnSave = new JButton("4 Save");
		btnSave.setPreferredSize(new Dimension(150, 23));
		panel_4.add(btnSave);
		btnSave.setFont(new Font("����", Font.PLAIN, 16));
		btnSave.setAlignmentX(50.0f);
		
		
		btnSave.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				//System.out.println("4 Save");
				myTextArea.append(">> �޴� <4> Save ��(��) ���õǾ����ϴ�. ���� �۾� ���� ������ �ٸ� �̸����� �����մϴ�.\n");
				menuNumber=4;
				

				if(textState==false) {
					
					if(nowFile==defaultState) {
						//���� �޸𸮿� �÷��� ������ ���� ��
						myTextArea.append(">> �޸𸮿� �÷��� ������ �����ϴ�. Load �޴��� ���� ���� �̸��� ���� �Է����ּ���.\n");
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
		btnPrint.setFont(new Font("����", Font.PLAIN, 16));
		btnPrint.setAlignmentX(50.0f);
		
		btnPrint.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
			
				//System.out.println("5 Print");
				myTextArea.append(">> �޴� <5> Print ��(��) ���õǾ����ϴ�. ���� �۾� ���� ������ ����մϴ�.\n");
				System.out.println(nowFile+"~~~~~");
				menuNumber=5;
				if(nowFile==defaultState) {
					//���� �۾� ���� ������ ���� ��
					myTextArea.append("�޸𸮿� �÷��� ������ �����ϴ�. Load �޴��� ���� ���� �̸��� ���� �Է����ּ���.\n");
					return;
				}

				printFile=nowFile;
				Document doc=nowDoc;
				myTextArea.append("���� �۾� ���� ������ ��� ����Դϴ�.\n");
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
		btnInsert.setFont(new Font("����", Font.PLAIN, 16));
		btnInsert.setAlignmentX(50.0f);
		
		btnInsert.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				myTextArea.append(">> �޴� <6>Insert ��(��) ���õǾ����ϴ�.\n");
				myTextArea.append(">> ����: selectToInsertElementName^toInsertValue Ư������'^'�� ������ּ���.\n");
				menuNumber=6;
				
				//Ư�� element�� �ֱ�
				
				if(textState==false) {
					if(nowFile==defaultState) {
						//���� �޸𸮿� �÷��� ������ ���� ��
						myTextArea.append(">> �޸𸮿� �÷��� ������ �����ϴ�. Load �޴��� ���� ���� �̸��� ���� �Է����ּ���.\n");
						return;
					}

					//� Ÿ������ ������(1:root, 2:element, 3:attribute, 4:text, 5:comment)
					if(rd6ele.isSelected()) {
						System.out.print("rd6ele");
						valueType=2;
						myTextArea.append(">> � element �տ� ������,�ְ� ���� element �� �Է��ϰ� ���͸� �����ּ���.\n");
						
					}else if(rd6attr.isSelected()) {
						valueType=3;
						myTextArea.append(">> � element�� attribute�� ������, �ְ� ���� attribute ���� �Է��ϰ� ���͸� �����ּ���.\n");
					}else if(rd6comt.isSelected()) {
						valueType=5;
						myTextArea.append(">> � element �տ� ������, �ְ� ���� comment ���� �Է��ϰ� ���͸� �����ּ���.\n");
					}else if(rd6text.isSelected()) {
						valueType=4;
						myTextArea.append(">> � element�� text�� �߰�����, �ְ� ���� text ���� �Է��ϰ� ���͸� �����ּ���.\n");
					}else {
						myTextArea.append("����� ���� ������ �������ּ���.\n");
					}
					textState=true; //�Է¹ޱ�
					
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
		btnUpdate.setFont(new Font("����", Font.PLAIN, 16));
		
		
		ButtonGroup update7 = new ButtonGroup();
		update7.add(rd7ele);
		update7.add(rd7attr);
		update7.add(rd7comt);
		update7.add(rd7text);
		
		btnUpdate.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				myTextArea.append(">> �޴� <7>Update ��(��) ���õǾ����ϴ�.\n");
				myTextArea.append(">> ����: selectToUpdateData^UpdateValue Ư������'^'�� ������ּ���.\n");
				//Ư�� element�� �̸��� �ٲٱ�
				menuNumber=7;
				if(textState==false) {
					if(nowFile==defaultState) {
						//���� �޸𸮿� �÷��� ������ ���� ��
						myTextArea.append(">> �޸𸮿� �÷��� ������ �����ϴ�. Load �޴��� ���� ���� �̸��� ���� �Է����ּ���.\n");
						return;
					}
					//� Ÿ������ ������(1:root, 2:element, 3:attribute, 4:text, 5:comment)
					if(rd7ele.isSelected()) {
						System.out.print("rd7ele");
						valueType=2;
						myTextArea.append(">> � element�� �ٲ���,�ٲٰ� ���� element �� �Է��ϰ� ���͸� �����ּ���.\n");
						
					}else if(rd7attr.isSelected()) {
						valueType=3;
						myTextArea.append(">> � attribute�� �̸��� �ٲ���, �ٲٰ� ���� attribute ���� �Է��ϰ� ���͸� �����ּ���.\n");
					}else if(rd7comt.isSelected()) {
						valueType=5;
						myTextArea.append(">> � comment�� ������ �ٲ���, �ٲٰ� ���� comment ���� �Է��ϰ� ���͸� �����ּ���.\n");
					}else if(rd7text.isSelected()) {
						valueType=4;
						myTextArea.append(">> � element�� text�� �ٲ���, �ٲٰ� ���� text ���� �Է��ϰ� ���͸� �����ּ���.\n");
					}else {
						myTextArea.append("������Ʈ �ϰ� ���� ������ �������ּ���.\n");
					}
					
					//myTextArea.append(">> � ���� �ٲ���,�ٲٰ� ���� �� �Է�\n");
					textState=true; //�Է¹ޱ�
					
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
		btnDelete.setFont(new Font("����", Font.PLAIN, 16));
		btnDelete.setAlignmentX(50.0f);
		
		
		ButtonGroup delete8 = new ButtonGroup();
		delete8.add(rd8ele);
		delete8.add(rd8attr);
		delete8.add(rd8comt);
		delete8.add(rd8text);
		
		btnDelete.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				myTextArea.append(">> �޴� <8>Delete ��(��) ���õǾ����ϴ�.\n");
				//Ư�� element�� ���ֱ�
				menuNumber=8;
				if(textState==false) {
					if(nowFile==defaultState) {
						//���� �޸𸮿� �÷��� ������ ���� ��
						myTextArea.append(">> �޸𸮿� �÷��� ������ �����ϴ�. Load �޴��� ���� ���� �̸��� ���� �Է����ּ���.\n");
						return;
					}
					//� Ÿ������ ������(1:root, 2:element, 3:attribute, 4:text, 5:comment)
					if(rd8ele.isSelected()) {
						System.out.println("rd8ele");
						valueType=2;
						myTextArea.append(">> � element�� ������ �Է��ϰ� ���͸� �����ּ���.\n");
						
					}else if(rd8attr.isSelected()) {
						valueType=3;
						myTextArea.append(">> � attribute�� ������ �Է��ϰ� ���͸� �����ּ���.\n");
					}else if(rd8comt.isSelected()) {
						valueType=5;
						myTextArea.append(">> � comment�� ������ �Է��ϰ� ���͸� �����ּ���.\n");
					}else if(rd8text.isSelected()) {
						valueType=4;
						myTextArea.append(">> � text�� ������ �Է��ϰ� ���͸� �����ּ���.\n");
					}else {
						myTextArea.append("���ְ� ���� ������ �������ּ���.\n");
					}
					//myTextArea.append(">> � ���� ������ �Է� \n");
					textState=true; //�Է¹ޱ�
					
				}
				delete8.clearSelection();
			}
		});
		
		JPanel panel_9 = new JPanel();
		buttonsPanel.add(panel_9);
		
		JButton btnExit = new JButton("9 Exit");
		btnExit.setPreferredSize(new Dimension(150, 23));
		panel_9.add(btnExit);
		btnExit.setFont(new Font("����", Font.PLAIN, 16));
		btnExit.setAlignmentX(50.0f);
		
		
		
		btnExit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				//�޴� <9>exit
				if(nowFile!=defaultState) {
					//�۾����� ������ �ִ� ���
					myTextArea.append("Save �޴��� ���� �۾� ���� ������ ������ �Ŀ� �������ּ���.\n");
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
			nowDoc = doc; //���� �۾� ���� document
			Node node = doc.getDocumentElement();
			NodeList children = node.getChildNodes();
			
			textState=false; //���� �Է¸��Ƶα�
			nowFile=inputFile; //Load�� �Ϸ�Ǹ� ���� �۾� ���� ���� �̸� ����
			stateLabel.setText("���� �۾� ���� ����: "+nowFile);
			
		}catch(FactoryConfigurationError e){
		//unable to get a document builder factory
			e.printStackTrace(System.err);
			myTextArea.append("�ý��� ����1\n");
		}catch(ParserConfigurationException e){
			//parser was unable to be configured
			e.printStackTrace(System.err);
			myTextArea.append("�ý��� ����2\n");
		}catch(SAXException e){
			//parsing error
			e.printStackTrace(System.err);
			myTextArea.append("�ý��� ����3\n");
		}catch(IOException e){
			//I/O error
			e.printStackTrace(System.err);
			myTextArea.append("�ý��� ���� 4. I/O error.\n");
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
				//������ ���������
				nowFile = makeName + newFileworking;
				stateLabel.setText("���� �۾� ���� ����: "+nowFile);
				nowDoc = makeDoc; //���� �۾� ���� document�� ����
				nowDoc.appendChild(root);
				makeDoc =null; //�ʱ�ȭ
				rootExist=1; //root ����
				break;
			case 2:
				//element
				if(rootExist==0) {
					myTextArea.append("root�� ���� ������ּ���\n");
					break;
				}
				//Element item = doc.createElement(makeName);
				//item.appendChild(doc.createTextNode(makeName+"'s text"));
				//root.appendChild(item);
				myTextArea.append("Make element�� Insert �޴��� �̿����ּ���.\n");
				
				break;
			case 3:
				//attr
				if(rootExist==0) {
					myTextArea.append("root�� ���� ������ּ���\n");
					break;
				}
				myTextArea.append("Make attribute�� Insert �޴��� �̿����ּ���.\n");
				break;
			case 4:
				//text
				if(rootExist==0) {
					myTextArea.append("root�� ���� ������ּ���\n");
					break;
				}
				myTextArea.append("Make text�� Insert �޴��� �̿����ּ���.\n");
				break;
			case 5:
				//comment
				if(rootExist==0) {
					myTextArea.append("root�� ���� ������ּ���\n");
					break;
				}
				myTextArea.append("Make Comment�� Insert �޴��� �̿����ּ���.\n");
				break;
			}
			
			valueType=0;
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
	}
	
	
	public void DOMSave() {
		try {
			Document doc = nowDoc; //���� �۾� ���� ����
			if(makeDoc != null) {
				//���ο� ������ �ִٸ�, ���ο� ���Ϸ� ��ü�ϱ�
				doc = makeDoc;
			}

		
			
		OutputFormat format = new OutputFormat(doc);
		StringWriter stringOut = new StringWriter();
		XMLSerializer serial = new XMLSerializer(stringOut, format);
		serial.asDOMSerializer();
		serial.serialize(doc.getDocumentElement());
		
		
		//FileWriter fw = new FileWriter(toSaveFile);
		
		//�ѱ��� ������ �ʰ� UTF-8 ����
		FileOutputStream output=new FileOutputStream(toSaveFile,false);
        OutputStreamWriter writer=new OutputStreamWriter(output,"UTF-8");
		BufferedWriter bw = new BufferedWriter(writer);
		bw.write(stringOut.toString());
		bw.close();
		} catch (Exception ex) {
		ex.printStackTrace();
		}
		
		//Save�� �Ϸ�Ǿ����� �ʱ�ȭ
		textState=false; //���� �Է¸��Ƶα�
		nowFile = defaultState; //Save�� �Ϸ�Ǹ� ���� �۾� ���� ���� �ؽ�Ʈ �ʱ�ȭ
		nowDoc = null;
		makeDoc = null;
		stateLabel.setText("���� �۾� ���� ����: "+nowFile);
		
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
				myTextArea.append("�ش� ���� ã�� �� �����ϴ�.\n");
			}
			//��带 �� ã�� �Ŀ� �ʱ�ȭ
			findflag=0; //���� ���� ���
			textState=false; //�Է� ���� �ʱ�
			valueType=0;
	}
	
	public int findflag=0; //�ش��ϴ� ���� ã����:1, �ش� �ϴ� ���� ������: 0
	
	public void nodeFind (Node node, String eleName){
	
		if(node == null) return;
		
		if(node.getNodeName().equals(eleName)) {
			//��Ʈ
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
				NamedNodeMap attr = child.getAttributes();//node���� child�� ����
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
		
		// ���� ��ũ
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
			//�̷��� �ϸ� �ʹ� �ߵ����� �ϳ��� �ȴ�.
			
			//�ְ� ���� ��ġ ã��
			//nodeFindforInsert(doc.getDocumentElement(),toInsertRef,doc);
			if(findflag==0) {
				myTextArea.append("�ش� ���� ã�� �� �����ϴ�.\n");
		}
//			
			//��带 �� ã�� �Ŀ� �ʱ�ȭ
			findflag=0; //���� ���� ���
			textState=false; //�Է� ���� �ʱ�
			nowDoc = doc;
			valueType=0;
			//makeDoc = doc; 

		
	}
	
	public void nodeFindforInsert (Node node, String eleName,Document doc){
		if(node == null) return;
		if(node.getNodeName().equals(eleName)) {
			//��Ʈ
			//myTextArea.append("["+getDepth(node)+","+getSiblingIndex(node)+"] "+node.getNodeName()+"\n");
			findflag=1;
			Element p = doc.createElement(toInsertValue);
			p.appendChild(doc.createTextNode(" "));
			//node.getParentNode().insertBefore(p, node);
			node.appendChild(p);
			myTextArea.append(eleName+"�� ��Ʈ�̰�, ��Ʈ���� �ڽĸ� ���� �� �ֽ��ϴ�. "+toInsertValue+"�� �ڽ����� ����ϴ�.\n");
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
				//������ �� �𸣰����� return �ϴϱ� ���� �� �ȴ�...
			}
			
			nodeFindforInsert(child,eleName,doc);
		}
	} //print(Node)
	

	public void nodeFindforInsertAttr (Node node, String eleName,Document doc){
		if(node == null) return;
		if(node.getNodeName().equals(eleName)) {
			//��Ʈ
			findflag=1;
			myTextArea.append("XML�� ��Ʈ���� attribute�� ���� �� �����ϴ�.\n");
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
			//��Ʈ
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
			//��Ʈ
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
			myTextArea.append("�ش� ���� ã�� �� �����ϴ�.\n");
		}
		//��带 �� ã�� �Ŀ� �ʱ�ȭ
		findflag=0; //���� ���� ���
		textState=false; //�Է� ���� �ʱ�
		nowDoc = doc;
		valueType=0;
}


	public void nodeFindforUpdateEle (Node node, String eleName,Document doc){
	
	if(node == null) return;
	
	if(node.getNodeName().equals(eleName)) {
		//��Ʈ
		findflag=1;
		doc.renameNode(node, null, toUpdateNewName);	
		return;
	}
	
	NodeList children = node.getChildNodes();

	for(int i=0;i<children.getLength();i++){
		Node child = children.item(i);
		
		if(child.getNodeName().equals(eleName)){
			
			findflag=1;
//			System.out.println(child.getNodeName()); //������Ʈ �̸� �� ������
//			System.out.println(child.getNodeValue()); //null
//			System.out.println(child.getTextContent()); //������Ʈ�� text�� �� ������
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
					NamedNodeMap attr = child.getAttributes();//node���� child�� ����
			
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
					//System.out.println(child.getNodeName()); //������Ʈ �̸� �� ������
					//System.out.println(child.getNodeValue()); //null
					//System.out.println(child.getTextContent()); //������Ʈ�� text�� �� ������
					//doc.renameNode(child, null, toUpdateNewName);		
					child.setTextContent(toUpdateNewName); //������Ʈ �ȿ� �ִ� ��(�ؽ�Ʈ)�� �ش� ������ �ٲ�
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
					//�ش� �ڸ�Ʈ�� ã�Ҵ�!
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
			myTextArea.append("�ش� ���� ã�� �� �����ϴ�.\n");
		}
		//��带 �� ã�� �Ŀ� �ʱ�ȭ
		findflag=0; //���� ���� ���
		textState=false; //�Է� ���� �ʱ�
		nowDoc = doc;
		valueType=0;
	
}

	public void nodeFindforDeleteEle (Node node, String eleName){
	
	if(node == null) return;
	
	if(node.getNodeName().equals(eleName)) {
		//��Ʈ
		myTextArea.append("XML ������ root�� ���� �� �����ϴ�.\n");
		findflag=1;
		return;
	}
	
	NodeList children = node.getChildNodes();

	for(int i=0;i<children.getLength();i++){
		Node child = children.item(i);
		
		if(child.getNodeName().equals(eleName)){
			myTextArea.append("["+getDepth(child)+","+getSiblingIndex(child)+"] "+child.getNodeName()+" "+child.getTextContent()+"\n");
			findflag=1;
//			System.out.println(child.getNodeName()); //������Ʈ �̸� �� ������
//			System.out.println(child.getNodeValue()); //null
//			System.out.println(child.getTextContent()); //������Ʈ�� text�� �� ������
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
				NamedNodeMap attr = child.getAttributes();//node���� child�� ����
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
						//������ ����ϸ� error�� ���. �翬���� ���� �����ϱ�.
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

