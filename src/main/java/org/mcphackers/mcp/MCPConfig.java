package org.mcphackers.mcp;

public class MCPConfig {

	public static final String CLIENT = 			 "jars/minecraft.jar";
	public static final String SERVER = 			 "jars/minecraft_server.jar";
	public static final String CLIENT_FIXED = 		 "jars/libraries/minecraft.jar";
	public static final String LWJGL = 				 "jars/libraries/lwjgl.jar";
	public static final String LWJGL_UTIL = 		 "jars/libraries/lwjgl_util.jar";
	public static final String JINPUT = 	 		 "jars/libraries/jinput.jar";
	public static final String NATIVES = 			 "jars/natives";
	public static final String CLIENT_TINY_OUT = 	 "temp/minecraft_remapped.jar";
	public static final String SERVER_TINY_OUT = 	 "temp/minecraft_server_remapped.jar";
	public static final String CLIENT_EXC_OUT = 	 "temp/minecraft_exc.jar";
	public static final String SERVER_EXC_OUT = 	 "temp/minecraft_server_exc.jar";
	public static final String CLIENT_SRC = 		 "temp/minecraft_src.zip";
	public static final String SERVER_SRC = 		 "temp/minecraft_server_src.zip";
	public static final String CLIENT_TEMP_SOURCES = "temp/src/minecraft";
	public static final String SERVER_TEMP_SOURCES = "temp/src/minecraft_server";
	public static final String CLIENT_MD5 = 		 "temp/client.md5";
	public static final String SERVER_MD5 = 		 "temp/server.md5";
	public static final String CLIENT_MD5_RO = 		 "temp/client_reobf.md5";
	public static final String SERVER_MD5_RO = 		 "temp/server_reobf.md5";
	public static final String CLIENT_REOBF_JAR = 	 "temp/client_reobf.jar";
	public static final String SERVER_REOBF_JAR = 	 "temp/server_reobf.jar";
	public static final String CLIENT_MAPPINGS_RO =  "temp/client_reobf.tiny";
	public static final String SERVER_MAPPINGS_RO =  "temp/server_reobf.tiny";
	public static final String CLIENT_SOURCES = 	 "src/minecraft";
	public static final String SERVER_SOURCES = 	 "src/minecraft_server";
	public static final String CLIENT_BIN = 		 "bin/minecraft";
	public static final String SERVER_BIN = 		 "bin/minecraft_server";
	public static final String CLIENT_REOBF = 		 "reobf/minecraft";
	public static final String SERVER_REOBF = 		 "reobf/minecraft_server";
	public static final String CLIENT_MAPPINGS = 	 "conf/client.tiny";
	public static final String SERVER_MAPPINGS = 	 "conf/server.tiny";
	public static final String EXC_CLIENT = 		 "conf/client.exc";
	public static final String EXC_SERVER = 		 "conf/server.exc";
	public static final String CLIENT_PATCHES = 	 "conf/patches_client";
	public static final String SERVER_PATCHES = 	 "conf/patches_server";
	public static final String JAVADOC_CLIENT = 	 "conf/client.javadoc";
	public static final String JAVADOC_SERVER = 	 "conf/server.javadoc";
	public static final String PROPERTIES_CLIENT = 	 "conf/client.properties";
	public static final String PROPERTIES_SERVER = 	 "conf/server.properties";
	public static final String BUILD_ZIP_CLIENT = 	 "build/minecraft.zip";
	public static final String BUILD_ZIP_SERVER = 	 "build/minecraft_server.zip";
	public static final String BUILD_JAR_CLIENT = 	 "build/minecraft.jar";
	public static final String BUILD_JAR_SERVER = 	 "build/minecraft_server.jar";
	
	public static boolean debug;
	public static boolean patch;
	public static boolean srcCleanup;
	public static String[] ignorePackages;
	public static int onlySide;
	public static String indentionString;
	public static boolean fullBuild;
	public static boolean runBuild;
	public static String setupVersion;

    static {
        resetConfig();
    }

    public static void resetConfig() {
        debug = false;
        patch = true;
        srcCleanup = false;
    	onlySide = -1;
    	ignorePackages = new String[]{"paulscode", "com/jcraft", "isom"};
    	indentionString = "\t";
    	fullBuild = false;
    	runBuild = false;
    	setupVersion = null;
    }

    public static void setParameter(String name, int value) {
        switch (name) {
        	case "side":
        		onlySide = value;
        		break;
        }
    }

    public static void setParameter(String name, String value) {
        switch (name) {
	        case "ind":
	        case "indention":
	        	indentionString = value;
	            break;
	        case "ignore":
	            ignorePackages = new String[] {value};
	            break;
	        case "setupversion":
	        	setupVersion = value;
	            break;
        }
    }

    public static void setParameter(String name, String[] value) {
        switch (name) {
	        case "ignore":
	            ignorePackages = value;
	            break;
        }
    }

    public static void setParameter(String name, boolean value) {
        switch (name) {
            case "debug":
                debug = value;
                break;
            case "patch":
                patch = value;
                break;
        	case "client":
        		onlySide = value ? 0 : onlySide;
        		break;
        	case "server":
        		onlySide = value ? 1 : onlySide;
        		break;
	        case "src":
	            srcCleanup = value;
	            break;
	        case "fullbuild":
	        	fullBuild = value;
	            break;
	        case "runbuild":
	        	runBuild = value;
	            break;
        }
    }

}
