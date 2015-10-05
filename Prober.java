import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;



public class Prober {
	
	private static PrintWriter writer;
	
	public static void main(String[] args) throws IOException {
		if(args.length == 2) {
			//Infinite loop until user terminates
			long lastCall = 0;
			String urlname = "http://" + args[0];
			URL url = new URL(urlname);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setFollowRedirects(true);
			HttpURLConnection.setFollowRedirects(true);
			
			try {
				if(connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
					urlname = connection.getHeaderField("Location");
					connection = (HttpURLConnection) new URL(urlname).openConnection();
				}
			}
			catch(Exception e) {
				System.out.println("Could not connect to host! : " + urlname);
				System.exit(0);
			}
			
			File file = new File(args[1]);
			writer = new PrintWriter(file);
			writer.println("URL=" + urlname);
			System.out.println("URL=" + urlname);
			while(true) {
				if(System.currentTimeMillis() - lastCall > 30000) {
					lastCall = System.currentTimeMillis();
					long unixTime = System.currentTimeMillis() / 1000;
					String responseVal = Integer.toString(connection.getResponseCode());
					if(responseVal.equals("200")) {
						System.out.println(Long.toString(unixTime) + "," + responseVal);
						writer.println(Long.toString(unixTime) + "," + responseVal);
						writer.flush();
					}
					else {
						System.out.println(Long.toString(unixTime) + "," + responseVal);
						writer.println(Long.toString(unixTime) + "," + responseVal);
						writer.flush();
					}
				}
			}
		}
		else {
			System.out.println("Arguments not provided! <hostname> <samples_file>");
		}
		
	}

}
