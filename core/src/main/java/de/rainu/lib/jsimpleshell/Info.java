package de.rainu.lib.jsimpleshell;

import java.io.InputStream;
import java.util.Properties;

/**
 * This class contains methods that get information about the JSimpleShell such like the version.
 * 
 * @author rainu
 */
public class Info {
	
	/**
	 * Get the used JSimpleShell version.
	 * 
	 * @return The version of JSimpleShell if known. Otherwise null!
	 */
	public static synchronized String getVersion() {
	    String version = null;

	    // try to load from maven properties first
	    try {
	        Properties p = new Properties();
	        InputStream is = Info.class.getResourceAsStream("/META-INF/maven/de.rainu.lib/jsimpleshell/pom.properties");
	        if (is != null) {
	            p.load(is);
	            version = p.getProperty("version", "");
	        }
	    } catch (Exception e) {
	        // ignore
	    }

	    // fallback to using Java API
	    if (version == null) {
	        Package aPackage = Info.class.getPackage();
	        if (aPackage != null) {
	            version = aPackage.getImplementationVersion();
	            if (version == null) {
	                version = aPackage.getSpecificationVersion();
	            }
	        }
	    }

	    return version;
	} 
}
