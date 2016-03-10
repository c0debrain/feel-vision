/**
 * 
 */
package com.github.gilbertotorrezan.feelvision.server.ws;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.appengine.api.utils.SystemProperty;

/**
 * @author Gilberto Torrezan Filho
 *
 * @since Oct 18, 2014 11:35:52 PM
 */
public class CredentialsProvider {
	
	private static final Logger logger = Logger.getLogger(CredentialsProvider.class.getName());
	
	public static final HttpTransport TRANSPORT = new NetHttpTransport();
	public static final JsonFactory JSON_FACTORY = new JacksonFactory();
	
	public static HttpRequestInitializer getCredentials(String ... scopes) throws Exception {
		if (isOnDevServer()){
			try {
				File file = new File("WEB-INF/feelvision.p12");
				if (!file.canRead() || !file.isFile()){
					logger.log(Level.SEVERE, "Can't read pem file: " + file.getAbsolutePath());
					throw new FileNotFoundException(file.getAbsolutePath());
				}
				GoogleCredential credential = new GoogleCredential.Builder().setTransport(TRANSPORT)
						.setJsonFactory(JSON_FACTORY)
						.setServiceAccountId("feel-vision@appspot.gserviceaccount.com")
						.setServiceAccountScopes(Arrays.asList(scopes))
						.setServiceAccountPrivateKeyFromP12File(file)
						.build();
				
				return credential;
			} catch (Exception e) {
				logger.log(Level.SEVERE, "Error creating the credentials on dev server: " + e, e);
				throw e;
			}
		}
		
		try {
			GoogleCredential credential = GoogleCredential.getApplicationDefault().createScoped(Arrays.asList(scopes));
			return credential;			
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error creating the credentials on server: " + e, e);
			throw e;
		}
	}
	
	public static boolean isOnDevServer(){
		return SystemProperty.environment.value() != SystemProperty.Environment.Value.Production;
	}
}
