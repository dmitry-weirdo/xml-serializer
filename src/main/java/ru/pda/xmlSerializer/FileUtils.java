package ru.pda.xmlSerializer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.pda.xmlSerializer.jdbc.JdbcTransactionExecutor;

import java.io.File;

/**
 * <p>
 * A copy of {@link su.opencode.kefir.util.FileUtils}
 * to get rid of using log4j 1.2.
 * </p>
 *
 * // todo: use some existing library instead of self-written
 */
public final class FileUtils {
    private FileUtils() {
        // private constructor for utils class
    }

	public static boolean deleteFile(final String fileName) {
		return deleteFile(new File(fileName));
	}
	public static boolean deleteFile(final File f) {
		if ( !f.exists() )
			return true;

		if (f.delete())
		{
			logger.debug( "File \"{}\" deleted successfully", f.getAbsolutePath() );
			return true;
		}

		logger.debug( "Cannot delete file \"{}\"", f.getAbsolutePath() );
		return false;
	}

    private static final Logger logger = LogManager.getLogger(JdbcTransactionExecutor.class);
}