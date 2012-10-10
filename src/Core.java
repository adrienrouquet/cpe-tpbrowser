import org.eclipse.swt.widgets.*;

public class Core {

	private static Display _display;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Shell shell = initShell();
		
		new BigBrowser(shell);
		//new SimpleBrowser(shell);
		runAndClose(shell);
	}
	
	protected static Shell initShell() {
		if (_display == null)
		{
			_display = new Display();
		}
		else
		{
			_display = Display.getCurrent();
		}
		return new Shell(_display);
	}
	
	protected static void runAndClose(Shell shell) {
		shell.open();
		while (!shell.isDisposed()) {
			if (!_display.readAndDispatch())
				_display.sleep();
		}
	}
}
