import java.io.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.browser.*;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;

public class BigBrowser {
	private Display _display = Display.getCurrent();
	private Browser _browser;
	private Text _addressBar;
	private CTabFolder _cTabFolder;
	static private String _historyPath = Core.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "history.html";
	
	/**
	 * @param args
	 */
	public BigBrowser(Shell shell) {
		initBigBrowser(shell);
	}
	
	public void initBigBrowser(Shell shell) {
		GridLayout layoutShell = new GridLayout(1, false);
		shell.setLayout(layoutShell);
		shell.setText("B!gBr0s3r !!!");
		shell.setImage(new Image(_display, "img/browser.png"));
		

		// Creation de la toolBar
		Composite compositeToolBar = new Composite(shell, SWT.NONE);
		compositeToolBar.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		ToolBar toolBar = initToolBar(compositeToolBar);
		
		// Creation des Tabs
		_cTabFolder = new CTabFolder(shell, SWT.NONE);
		_cTabFolder.setSimple(false);
		_cTabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		_cTabFolder.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				SimpleBrowser sB = (SimpleBrowser) _cTabFolder.getSelection().getData();
				_addressBar = sB.getAddressBar();
				_browser = sB.getBrowser();
			}
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});
		
		newTabBrowser();
//		// Creation du browser
//		final CTabItem item = new CTabItem(cTabFolder, SWT.CLOSE);
//		Composite compositeSimpleBrowser = new Composite(cTabFolder, SWT.NONE);
//		compositeSimpleBrowser.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
//		final SimpleBrowser simpleBrowser = new SimpleBrowser(compositeSimpleBrowser);
//		_browser = simpleBrowser.getBrowser();
//		_addressBar = simpleBrowser.getAddressBar();
//		item.setControl(compositeSimpleBrowser);
//		item.setText(_addressBar.getText());
//		item.setData(simpleBrowser);
//		cTabFolder.setSelection(0);
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
	
	static public String getHistoryPath() {
		return _historyPath;
	}
	
	public void setHistoryPath(String historyPath) {
		_historyPath = historyPath;
	}
	
	/**
	 * Init Toolbar
	 * @param Composite parent
	 * @return Toolbar
	 */
	protected ToolBar initToolBar(Composite parent) {
		
		parent.setLayout(new GridLayout(1, false));
		//ToolBar declaration
		ToolBar toolBar = new ToolBar(parent, SWT.NONE);
		toolBar.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
		
		ToolItem itemBack = addButtonToolBar(toolBar, "Back", "img/back.png");
		ToolItem itemForward = addButtonToolBar(toolBar, "Forward", "img/forward.png");
		ToolItem itemStop = addButtonToolBar(toolBar, "Stop", "img/stop.png");
		ToolItem itemRefresh = addButtonToolBar(toolBar, "Refresh", "img/refresh.png");
		ToolItem itemHistory = addButtonToolBar(toolBar, "History", "img/history.png");
		ToolItem itemAnalyzer = addButtonToolBar(toolBar, "Analyzer", "img/analyzer.png");
		ToolItem itemGo = addButtonToolBar(toolBar, "Go", "img/go.png");
		ToolItem itemNewTab = addButtonToolBar(toolBar, "New Tab", "img/newTab.png");
		
		//Definition des Actions
		itemBack.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) 
			{
				_browser.back();
			}
		});
		itemForward.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) 
			{
				_browser.forward();
			}
		});
		itemStop.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) 
			{
				_browser.stop();
			}
		});
		itemRefresh.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) 
			{
				_browser.refresh();
			}
		});
		itemHistory.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) 
			{
				_browser.setUrl("file:///" + _historyPath);
			}
		});
		itemAnalyzer.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) 
			{
				//A new analyzer triggers the event listener and refreshes the current webpage
				_browser.refresh();
				//_bA = new BrowserAnalyzer(_browserLocation);
				new BrowserAnalyzer();
			}
		});
		itemGo.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) 
			{
				_browser.setUrl(_addressBar.getText());
			}
		});
		itemNewTab.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) 
			{
				newTabBrowser();
			}
		});
		
		return toolBar;
	}
	
	protected ToolItem addButtonToolBar(ToolBar parent, String name, String imagePath) {
		
		Image image = new Image(_display, new Image(_display, imagePath).getImageData().scaledTo(28, 28));
		ToolItem toolItem = new ToolItem(parent, SWT.PUSH);
		toolItem.setImage(image);
		toolItem.setToolTipText(name);
		
		return toolItem;
	}
	
	/**
	 * New Tab Browser
	 */
	protected void newTabBrowser() {
		final CTabItem tabItem = new CTabItem(_cTabFolder, SWT.CLOSE);
		Composite compositeSimpleBrowser = new Composite(_cTabFolder, SWT.NONE);
		final SimpleBrowser simpleBrowser = initSimpleBrowser(compositeSimpleBrowser);
		tabItem.setControl(compositeSimpleBrowser);
		tabItem.setText(_addressBar.getText());
		tabItem.setData(simpleBrowser);
		_cTabFolder.setSelection(tabItem);
		
		// Address Bar
		_browser.addLocationListener(new LocationListener() {
			public void changed(LocationEvent event) 
			{
				if (event.top) tabItem.setText(event.location);
			}
			public void changing(LocationEvent event) 
			{
			}
		});
	}
	
	/**
	 * Init SimpleBrowser
	 * @param parent
	 * @return
	 */
	protected SimpleBrowser initSimpleBrowser(Composite parent) {
		// Creation du browser
		parent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		SimpleBrowser simpleBrowser = new SimpleBrowser(parent);
		_browser = simpleBrowser.getBrowser();
		_addressBar = simpleBrowser.getAddressBar();
		return simpleBrowser;
	}
}
