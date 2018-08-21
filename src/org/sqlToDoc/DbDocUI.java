package org.sqlToDoc;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.sqlToDoc.util.FileUtil;
import org.sqlToDoc.util.FreeMarkerUtils;

/** 
 * Description: 生成db doc文档的界面
 * 
 * @author jiujiya
 * @version 1.0 
 */
public class DbDocUI {
	
	/** 主窗体 */
	private Shell shell;
	
	// 文档模板
	Combo styleList;
	// 文档名称
	Text projectText;
	// 文档路径
	Text pathText;
	// 数据库IP
	Text dbIpText;
	// 端口号
	Text dbPortText;
	// 用户名
	Text dbUserText;
	// 密码
	Text dbPsText;
	// 密码
	Text dbNameText;
	// 数据bean
	DbDocBean bean;
	// 生成按钮
	Button createButton;
	// 打开按钮
	Button openButton;

	public static void main(String[] args) {
		DbDocBean bean = new DbDocBean();
		String basePath = System.getProperty("user.dir");
		bean.ftlBasePath = basePath + "\\templet\\";
		bean.pathText = basePath + "\\doc\\";
		List<String> files= FileUtil.getFileList(bean.ftlBasePath);
		bean.styleList = new String[files.size()];
		for (int i = 0; i < bean.styleList.length; i++) {
			bean.styleList[i] = FileUtil.getFileName(files.get(i));
		}
		new DbDocUI(bean).show();
	}

	public DbDocUI(DbDocBean bean) {
		this.bean = bean;
		
		shell = new Shell(192);
		shell.setText("数据库文档生成工具");
		shell.setLayout(null);

		addView();

		// 设置主窗体大小
		shell.setSize(430, 400);
		centerWindow(shell);
	}

	/**
	 * 添加界面
	 * @throws Exception 
	 */
	private void addView(){
		int y = 10;
		Label titleLabel = new Label(shell, 0);
		titleLabel.setText("温馨提示：选中项目，可自动生成文档");
		titleLabel.setFont(new Font(titleLabel.getDisplay(), "宋体", 8, SWT.UNDERLINE_LINK));
		titleLabel.setBounds(20, y+3, 300, 20);

		y += 30;
		Label styleLabel = new Label(shell, 0);
		styleLabel.setText("文档模板：");
		styleLabel.setBounds(20, y+3, 70, 28);
		styleList = new Combo(shell, SWT.READ_ONLY);
		styleList.setBounds(100, y, 220, 28);
		styleList.setItems(bean.styleList);
		styleList.select(0);
		Label styleCustom = new Label(shell, 0);
		styleCustom.setText("自定义模板？");
		styleCustom.setFont(new Font(titleLabel.getDisplay(), "宋体", 8, SWT.NORMAL));
		styleCustom.setBounds(328, y+7, 90, 28);
		
		styleCustom.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				try {
					Desktop.getDesktop().open(new File(bean.ftlBasePath));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});

		y += 40;
		Label projectLabel = new Label(shell, 0);
		projectLabel.setText("文档名称：");
		projectLabel.setBounds(20, y+3, 70, 28);
		projectText = new Text(shell, 2048);
		projectText.setBounds(100, y, 300, 28);
		projectText.setText(bean.projectText);

		y += 40;
		Label pathLabel = new Label(shell, 0);
		pathLabel.setText("文档路径：");
		pathLabel.setBounds(20, y+3, 70, 28);
		pathText = new Text(shell, 2048);
		pathText.setBounds(100, y, 300, 28);
		pathText.setText(bean.pathText);

		y += 40;
		Label dbIpLabel = new Label(shell, 0);
		dbIpLabel.setText("数据库IP：");
		dbIpLabel.setBounds(20, y+3, 70, 28);
		dbIpText = new Text(shell, 2048);
		dbIpText.setBounds(100, y, 140, 28);
		dbIpText.setText(bean.dbIpText);
		
		Label dbPortLabel = new Label(shell, 0);
		dbPortLabel.setText("端口号：");
		dbPortLabel.setBounds(260, y+3, 70, 28);
		dbPortText = new Text(shell, 2048);
		dbPortText.setBounds(330, y, 70, 28);
		dbPortText.setText(bean.dbPortText);

		y += 40;
		Label dbUserLabel = new Label(shell, 0);
		dbUserLabel.setText("用户名：");
		dbUserLabel.setBounds(20 + 12, y+3, 60, 28);
		dbUserText = new Text(shell, 2048);
		dbUserText.setBounds(100, y, 115, 28);
		dbUserText.setText(bean.dbUserText);
		
		Label dbPsLabel = new Label(shell, 0);
		dbPsLabel.setText("密码：");
		dbPsLabel.setBounds(235, y+3, 50, 28);
		dbPsText = new Text(shell, 2048);
		dbPsText.setBounds(285, y, 115, 28);
		dbPsText.setText(bean.dbPsText);
		
		y += 40;
		Label dbNameLabel = new Label(shell, 0);
		dbNameLabel.setText("数据库名：");
		dbNameLabel.setBounds(20, y+3, 70, 28);
		dbNameText = new Text(shell, 2048);
		dbNameText.setBounds(100, y, 300, 28);
		dbNameText.setText(bean.dbNameText);

		y += 60;
		createButton = new Button(shell, 0);
		createButton.setText("确认生成");
		createButton.setBounds(70, y, 120, 28);
		
		openButton = new Button(shell, 0);
		openButton.setText("打开文档路径");
		openButton.setBounds(220, y, 120, 28);
		openButton.setVisible(false);
		
		createButton.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseDown(MouseEvent arg0) {
				createDoc();
			}
		});
		
		openButton.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseDown(MouseEvent arg0) {
				try {
					Desktop.getDesktop().open(new File(pathText.getText()));
				} catch (IOException e) {
					error(shell, e);
					e.printStackTrace();
				}
			}
		});
		
		// 如果数据库名称不为空，自动生成
		if(!bean.dbNameText.equals("")) {
			createDoc();
		}
	}
	
	/**
	 * 生成文档
	 * @throws IOException 
	 */
	public void createDoc() {
		createButton.setEnabled(false);
		createButton.setText("文档生成中");
		Display.getDefault().asyncExec(new Runnable() {
		    public void run() {
		    	try {
					// 初始化数据库链接
					DBHelper.driverName = bean.driverClassName;
					DBHelper.dbname = dbNameText.getText();
					DBHelper.user = dbUserText.getText();
					DBHelper.password = dbPsText.getText();
					DBHelper.url = "jdbc:mysql://" + dbIpText.getText() + ":" + dbPortText.getText() + "/" 
									+ dbNameText.getText() + "?characterEncoding=utf8";
					// 初始化模板引擎  
			    	FreeMarkerUtils.initFreeMarker(bean.ftlBasePath);
			    	/** 模板引擎所需要的数据Map */
			    	Map<String,Object> templateData = new HashMap<String, Object>();
			    	templateData.put("projectName", projectText.getText());
			    	templateData.put("tables", DBHelper.getTableAndColumns());
			    	// 生成word文件
			    	if("".equals(pathText.getText().trim())) {
			    		throw new RuntimeException("文档路径不能为空");
			    	}
			    	String fileName = pathText.getText() + projectText.getText() + ".doc";
			    	FileUtil.addDirs(fileName);
			    	FreeMarkerUtils.crateFile(templateData, styleList.getText(), fileName, true);
					openButton.setVisible(true);
					createButton.setText("重新生成");
				} catch (Exception e) {
					e.printStackTrace();
					createButton.setText("确认生成");
					error(shell, e);
				} finally {
					createButton.setEnabled(true);
				}
		    }
		});
		
	}

	public void show() {
		shell.open();
		Display display = shell.getDisplay();
		while (!(shell.isDisposed()))
			if (!(display.readAndDispatch()))
				display.sleep();
	}
	
	public void close() {
		shell.dispose();
	}
	
	/**
	 * @param shell
	 * @param e
	 */
	public static void error(Shell shell, Exception e) {
		error(shell, e.getMessage());
	}
	
	/**
	 * @param shell
	 * @param message
	 */
	public static void error(Shell shell, String message) {
		MessageBox dialog = new MessageBox(shell, SWT.OK | SWT.ICON_ERROR);
        dialog.setText("温馨提示");
        dialog.setMessage(message);
        dialog.open();
	}
	
	/**
	 * 窗体居中
	 * @param shell
	 */
	public static void centerWindow(Shell shell) {
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension scmSize = toolkit.getScreenSize();
		shell.setLocation(scmSize.width / 2 - shell.getSize().x / 2, scmSize.height / 2 - shell.getSize().y / 2);
	}
}