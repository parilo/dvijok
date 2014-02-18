package org.dvijok.launcher;

import java.io.File;

import com.google.gwt.core.ext.ServletContainer;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.dev.shell.jetty.JettyLauncher;

public class JettyDvijokLauncher extends JettyLauncher {

	@Override
	public ServletContainer start(TreeLogger logger, int port, File appRootDir)	throws Exception {
		
		ServletContainer sc = super.start(logger, port, appRootDir);
		org.eclipse.jetty.util.resource.FileResource.setCheckAliases(false);
		
		return sc;
	}
	
}
