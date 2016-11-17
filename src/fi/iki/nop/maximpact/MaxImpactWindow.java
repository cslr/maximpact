package fi.iki.nop.maximpact;


import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.FileDialog;
import java.io.*;
import java.util.stream.Collectors;

public class MaxImpactWindow {

	protected Shell shlMaximpact;
	private Text text;
	private Text text_1;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			MaxImpactWindow window = new MaxImpactWindow();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shlMaximpact.open();
		shlMaximpact.layout();
		while (!shlMaximpact.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shlMaximpact = new Shell();
		shlMaximpact.setSize(450, 300);
		shlMaximpact.setText("MaxImpact");
		shlMaximpact.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		TabFolder tabFolder = new TabFolder(shlMaximpact, SWT.NONE);
		
		TabItem tbtmMain = new TabItem(tabFolder, SWT.NONE);
		tbtmMain.setText("Main");
		
		Composite composite = new Composite(tabFolder, SWT.NONE);
		tbtmMain.setControl(composite);
		composite.setLayout(new GridLayout(3, false));
		
		Label lblDevice = new Label(composite, SWT.NONE);
		lblDevice.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		lblDevice.setText("Device");
		
		Combo combo = new Combo(composite, SWT.READ_ONLY);
		combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		combo.setItems(
				"Random", 
				"Interaxon Muse", 
				"Neurosky [experimental]", 
				"Emotiv Insight [experimental]", 
				"WildDivine Lightstone [experimental]");
		combo.select(0);
		combo.setToolTipText("Connection: osc.udp://localhost:4545/ (muse-io.exe), " + 
							 " COM5 (neurosky)\n");
		new Label(composite, SWT.NONE);
		
		Label lblPicturesFolder = new Label(composite, SWT.NONE);
		lblPicturesFolder.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		lblPicturesFolder.setText("Pictures folder");
		
		text = new Text(composite, SWT.BORDER | SWT.READ_ONLY);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		text.setToolTipText("Pictures folder (jpg and png files).");
		
		Button btnSelect = new Button(composite, SWT.NONE);
		btnSelect.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				DirectoryDialog dialog = new DirectoryDialog(shlMaximpact);
			    dialog.setFilterPath("c:\\");
			    
			    String dir = dialog.open();
			    
			    if(dir != null)
			    	if(dir.length() > 0)
			    		text.setText(dir);
			}
		});
		btnSelect.setText("Select..");
		
		Label lblDisplayTime = new Label(composite, SWT.NONE);
		lblDisplayTime.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		lblDisplayTime.setText("Display Time");
		
		Combo combo_1 = new Combo(composite, SWT.READ_ONLY);
		combo_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		combo_1.setItems("100", "200", "300", "400", "500", "600", "700", "800", "900", "1000");
		combo_1.select(1); // starts from zero
		combo_1.setToolTipText("Picture display time in milliseconds.");
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		Button btnStart = new Button(composite, SWT.NONE);
		btnStart.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btnStart.setEnabled(false);
				
				// gets data from widgets
				int devIndex = combo.getSelectionIndex();
				String folder = text.getText();
				int delayIndex = combo_1.getSelectionIndex();
				
				String dev = "random";
				if(devIndex == 0) dev = "random";
				else if(devIndex == 1) dev = "muse";
				else if(devIndex == 2) dev = "neurosky";
				else if(devIndex == 3) dev = "insight";
				else if(devIndex == 4) dev = "lightstone";
				
				int delay = 200;
				if(delayIndex >= 0)
					delay = (delayIndex+1)*100;
				
				String cmd = "E:\\maximpact\\maximpact.exe " + dev + " " + folder + " " + Integer.toString(delay);
				
				String[] cmdarray = new String[4];
				cmdarray[0] = "maximpact.exe";
				cmdarray[1] = dev;
				cmdarray[2] = folder;
				cmdarray[3] = Integer.toString(delay);
				
				String str = this.getClass().getProtectionDomain().getCodeSource().getLocation().toString();
				File dir = new File(str.substring(6)).getParentFile();
				//System.out.println(dir.toString());
				cmdarray[0] = dir.toString() + "\\maximpact.exe";
				
				try{
					Process child = Runtime.getRuntime().exec(cmdarray);
					
					java.io.InputStream in = child.getInputStream();
					int rval = 0;
					
					try{ rval = child.waitFor(); }
					catch(InterruptedException ie){ }
					
					String result = new BufferedReader(new InputStreamReader(in))
							  .lines().collect(Collectors.joining("\n"));
					
					text_1.setText(result);
					// System.out.println(result);
					
				}
				catch(IOException ioe){ }
				
				btnStart.setEnabled(true);
				
				// System.out.println(cmd);
			}
		});
		btnStart.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, false, true, 1, 1));
		btnStart.setText("Start");
		
		TabItem tbtmMessages = new TabItem(tabFolder, SWT.NONE);
		tbtmMessages.setText("Messages");
		
		Composite composite_1 = new Composite(tabFolder, SWT.NONE);
		tbtmMessages.setControl(composite_1);
		composite_1.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		text_1 = new Text(composite_1, SWT.BORDER | SWT.READ_ONLY | SWT.V_SCROLL | SWT.MULTI);
		text_1.setText("MaxImpact 0.91a (C) Copyright Tomas Ukkonen <tomas.ukkonen@iki.fi>.\nEach purchase allows use of this software on a SINGLE computer.");

	}
}
