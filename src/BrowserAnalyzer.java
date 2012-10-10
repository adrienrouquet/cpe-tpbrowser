import org.eclipse.swt.SWT;
import org.mozilla.interfaces.*;
import org.mozilla.xpcom.Mozilla;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------
//Henri Lahoud
//Adrien Rouquet
//Loic Ortola
//5ETI SID
//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------

public class BrowserAnalyzer {
    
    private Shell analyzer;
    
    public BrowserAnalyzer(){
        analyzer = new Shell();
        init();
    }
    
    private void init() {
        
    	analyzer.setText("HTTP Analyzer");
    
        analyzer.setLayout(new FillLayout());

        final List myList = new List(analyzer,SWT.V_SCROLL|SWT.H_SCROLL);
        
        nsIObserver observer = new nsIObserver() {
            public nsISupports queryInterface(String aIID) {
                if (aIID.equals(nsIObserver.NS_IOBSERVER_IID) || aIID.equals(nsIObserver.NS_ISUPPORTS_IID)) {
                    return this;
                }
                return null;
            }
            
            void printRequestHeader(nsIHttpChannel channel, String header) {
                try {
                    myList.add(header + '=' + channel.getRequestHeader(header) + "\n");
                } catch (Exception e) {
                    // the header did not exist, just continue
                }
            }
            
            void printResponseHeader(nsIHttpChannel channel, String header) {
                try {
                    
                    myList.add('\t' + header + '=' + channel.getResponseHeader(header) + "\n");
                } catch (Exception e) {
                    // the header did not exist, just continue
                }
            }
            
            public void observe(nsISupports subject, String topic, String data) {
                nsIHttpChannel channel = (nsIHttpChannel)subject.queryInterface(nsIHttpChannel.NS_IHTTPCHANNEL_IID);
                nsIRequest request = (nsIRequest)subject.queryInterface(nsIRequest.NS_IREQUEST_IID);
                if (topic.equals("http-on-modify-request")) {
                    
                    
                    //window.setText("---------------------\nSome Request Header Values for " + request.getName() + ':');            
                    
                
                    
                    
                    myList.add("---------------------\nSome Request Header Values for " + request.getName() + ':' + "\n");
                    printRequestHeader(channel, "accept");
                    printRequestHeader(channel, "accept-language");
                    printRequestHeader(channel, "host");
                    printRequestHeader(channel, "user-agent");
                } else {
                    /* http-on-examine-response */
                    myList.add("---------------------\n\tSome Response Header Values for " + request.getName() + ':' + "\n");
                    printResponseHeader(channel, "content-length");
                    printResponseHeader(channel, "content-type");
                    printResponseHeader(channel, "expires");
                    printResponseHeader(channel, "server");
                }
            }            
    };
        nsIObserverService observerService = (nsIObserverService)Mozilla.getInstance().getServiceManager().getServiceByContractID("@mozilla.org/observer-service;1", nsIObserverService.NS_IOBSERVERSERVICE_IID);
        observerService.addObserver(observer, "http-on-modify-request", false);
        observerService.addObserver(observer, "http-on-examine-response", false);

        analyzer.open();
    }
    
    public void stop()
    {
        
    }
}