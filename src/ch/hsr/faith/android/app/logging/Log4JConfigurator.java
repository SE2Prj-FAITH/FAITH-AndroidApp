package ch.hsr.faith.android.app.logging;

import org.apache.log4j.Level;

import de.mindpipe.android.logging.log4j.LogConfigurator;

public class Log4JConfigurator {

	public static void configure() {
		final LogConfigurator logConfigurator = new LogConfigurator();
		logConfigurator.setUseLogCatAppender(true);
		logConfigurator.setUseFileAppender(false);
		logConfigurator.setRootLevel(Level.DEBUG);
		logConfigurator.configure();
	}

}
