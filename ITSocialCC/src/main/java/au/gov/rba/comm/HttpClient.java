package au.gov.rba.comm;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpClient {

	public String sendRequest(String url, String method, String requestParam,
			String requestValue) throws IOException {

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setUseCaches(false);
		con.setRequestMethod(method);
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		con.setRequestProperty("Content-Type", "text/plain; charset=utf-8");
		String urlParameters = "";
		if (requestParam.length() > 0) {
			urlParameters = requestParam + "=";
		}
		if (requestValue.length() > 0) {
			urlParameters += requestValue;
		}
		con.setDoOutput(true);
		if (urlParameters.length() > 0) {
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();
		}
		int responseCode = con.getResponseCode();
		//System.out
				//.println("\nSending '" + method + "' request to URL : " + url);
		//System.out.println("Post parameters : " + urlParameters);
		//System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(new InputStreamReader(
				con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		// print result
		// System.out.println(response.toString());
		return response.toString();
	}
}
