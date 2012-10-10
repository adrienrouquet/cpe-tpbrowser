import java.io.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.mozilla.xpcom.*;

public class SimpleBrowser {
	
	private Browser _browser;
	private Text _addressBar;
	
	public SimpleBrowser(Composite parent) {
		initSimpleBrowser(parent);
	}

	public void initSimpleBrowser(Composite parent) {
		
		parent.setLayout(new GridLayout(1, false));
		// Creation de la barre d'adresse
		Composite compositeAddress = new Composite(parent, SWT.NONE);
		compositeAddress.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		compositeAddress.setLayout(new GridLayout(2, false));
		_addressBar = initAddressBar(compositeAddress);

		// Creation du browser
		Composite compositeBrowser = new Composite(parent, SWT.NONE);
		compositeBrowser.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		compositeBrowser.setLayout(new FillLayout());
		_browser = initBrowser(compositeBrowser);
		
		// Creation de la bar de status et de progres
		Composite compositeProgressAndStatus = new Composite(parent, SWT.NONE);
		compositeProgressAndStatus.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, false));
		compositeProgressAndStatus.setLayout(new GridLayout(2, false));
		final Label statusBar = initStatusBar(compositeProgressAndStatus);
		final ProgressBar progressBar = initProgressBar(compositeProgressAndStatus);
		
		initBrowserActions(statusBar, progressBar);
		_browser.setUrl("http://www.google.com");
	}
	
	public Browser getBrowser() {
		return _browser;
	}
	
	public void setBrowser(Browser browser) {
		_browser = browser;
	}
	
	public Text getAddressBar() {
		return _addressBar;
	}
	
	public void setAddressBar(Text addressBar) {
		_addressBar = addressBar;
	}
	
	/**
	 * Init Address Bar
	 * @param Composite parent
	 * @return Text
	 */
	protected Text initAddressBar(Composite parent) {
		Label labelAddress = new Label(parent, SWT.NONE);
		labelAddress.setText("Address");
		labelAddress.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false));
		Text location = new Text(parent, SWT.SINGLE);
		location.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		return location;
	}
	
	protected Browser initBrowser(Composite parent) {
		
		//Mozilla.getInstance().initialize(new File("/opt/xulrunner-sdk/bin/"));
		
		Browser browser = null;
		try {
			//The type SWT.MOZILLA has been added for the analyzer to listen
			//browser = new Browser(parent, SWT.MOZILLA);
			browser = new Browser(parent, SWT.NONE);
		} catch (SWTError e) {
			System.out.println("Could not instantiate Browser: " + e.getMessage());
		}
		return browser;
	}
	
	/**
	 * Init Progress Bar
	 * @param Composite parent
	 */
	protected ProgressBar initProgressBar(Composite parent) {
		final ProgressBar progressBar = new ProgressBar(parent, SWT.NONE);
		progressBar.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, false, false));
		
		return progressBar;
	}
	
	/**
	 * Init Status Bar
	 * @param Composite parent
	 */
	protected Label initStatusBar(Composite parent) {
		final Label status = new Label(parent, SWT.NONE);
		status.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		
		return status;
	}
	
	/**
	 * Init Actions Browser
	 * @param statusBar
	 * @param progressBar
	 */
	protected void initBrowserActions(final Label statusBar, final ProgressBar progressBar) {
		
		// Status Bar
		_browser.addStatusTextListener(new StatusTextListener() {
			public void changed(StatusTextEvent event) 
			{
				statusBar.setText(event.text);
			}
		});
		
		//Progress Bar
		_browser.addProgressListener(new ProgressListener() {
			public void changed(ProgressEvent event) 
			{
				if (event.total == 0) return;                            
				int ratio = event.current * 100 / event.total;
				progressBar.setSelection(ratio);
			}
			public void completed(ProgressEvent event) 
			{
				progressBar.setSelection(0);
				appendHistory();
			}
		});
		
		// Address Bar
		_browser.addLocationListener(new LocationListener() {
			public void changed(LocationEvent event) 
			{
				if (event.top) _addressBar.setText(event.location);
			}
			public void changing(LocationEvent event) 
			{
			}
		});
		
		_addressBar.addListener(SWT.DefaultSelection, new Listener() {
			public void handleEvent(Event e) 
			{
				_browser.setUrl(_addressBar.getText());
			}
		});
	}
	
	protected void appendHistory() {
		
		//Appending the history html file or creating it if it doesn't exist
		String filePath = BigBrowser.getHistoryPath();
		File f = new File(filePath);
		if(!f.exists())
		{
			try {
				f.createNewFile();
				FileWriter fW = null;
			    BufferedWriter writer = null;
			    try {
			        fW = new FileWriter(filePath,true);
			        writer = new BufferedWriter(fW);
			        writer.append("<html>");
			        writer.append("<head><title>Browsing History</title></head>");
			        writer.append("<h3>Browsing History</h3>");					        
			        writer.append("<body bgcolor=grey>");
			        writer.newLine();
			        writer.close();
			    } catch (Exception e) {}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else
		{
			FileWriter fW = null;
		    BufferedWriter writer = null;
		    try {
		        fW = new FileWriter(filePath,true);
		        writer = new BufferedWriter(fW);
		        writer.append("<br><a href=\"");
		        writer.append(_addressBar.getText());
		        writer.append("\"> ");
		        writer.append(_addressBar.getText());
		        writer.append(" </a>");
		        writer.newLine();
		        writer.close();
		    } catch (Exception e) {}
		}
	}
	
}
