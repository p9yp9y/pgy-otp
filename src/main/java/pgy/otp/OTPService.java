package pgy.otp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.io.IOUtils;

import com.beust.jcommander.Parameter;

import pgy.master.util.JCommanderUtil;

public class OTPService {
	@Parameter(names = { "-azonosito" })
	private String azonosito;
	
	@Parameter(names = { "-telekod" })
	private String telekod;

	public static void main(String[] args) throws MalformedURLException, IOException {
		OTPService service = new OTPService();
		JCommanderUtil.parseArgs(service, args);
		System.out.println(service.getEgyenleg());
	}

	private int getEgyenleg() throws MalformedURLException, IOException {
		URL url = new URL(
				String.format("https://www.otpbankdirekt.hu/homebank/do/bankkartyaEgyenlegLekerdezes?muvelet=login&azonosito=%s&telekod=%s", azonosito, telekod));
		HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		IOUtils.copy(con.getInputStream(), baos);
		
		String str = baos.toString();
		int beginIndex = str.indexOf("Megadott");
		int endIndex = str.indexOf("</strong></span>", beginIndex);
		
		str = str.substring(beginIndex, endIndex);		
		str = str.substring(str.indexOf("<strong>") + "<strong>".length()).replace(".", "");
		return Integer.parseInt(str);
	}
}
