import java.io.File;
import java.security.MessageDigest;
import java.security.Security;
import java.io.FileInputStream;

public class ComputeSHA
{
	public static void main(String[] args) throws Exception
	{
		File file = new File(args[0]);
		MessageDigest md = MessageDigest.getInstance("SHA1");
		FileInputStream fis = new FileInputStream(file);
		byte[] buffer = new byte[(int) file.length()];

		fis.read(buffer);
		fis.close();
		md.update(buffer);
		byte[] digest = md.digest();
		
		StringBuffer sb = new StringBuffer();
		for ( int i = 0; i < digest.length; i++)
			sb.append(Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1));
		System.out.println(sb.toString());
	}
}
