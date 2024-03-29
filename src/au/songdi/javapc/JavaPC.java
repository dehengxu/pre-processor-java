package au.songdi.javapc;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import au.songdi.javapc.tag.TagProcessor;
import au.songdi.javapc.tag.TagSelector;

/**
 * This is the main class for javaPC. It can be used with command line and GUI
 * And it is still called for a javapc ant task
 * 
 * @author Di SONG
 * @version 0.1
 */

public class JavaPC {

	/**
	 * Sole entry point to class & application
	 * 
	 * @param args
	 *            Array of string arguments
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		// -g to run GUI
		if ((args.length == 1) && (args[0].equals("-g"))) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					au.songdi.javapc.gui.MainFrame window = new au.songdi.javapc.gui.MainFrame();
					window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					window.setVisible(true);
				}
			});
			return;
		}

		// to run with CMD
		if (args.length % 2 != 0) {
			System.out.println(getHelpString());
			return;
		}
		String srcdir = null;
		String destdir = null;
		String initfile = "global.def";
		for (int i = 0; i < args.length;) {
			if ("-s".equals(args[i])) {
				srcdir = args[++i];
			} else if ("-d".equals(args[i])) {
				destdir = args[++i];
			} else if ("-e".equals(args[i])) {
				boolean export = Boolean.parseBoolean(args[++i]);
				ContextManager context = ContextManager.getContext();
				context.setExport(export);
			} else if ("-i".equals(args[i])) {
				initfile = args[++i];
			} else if ("-m".equals(args[i])) {
				ContextManager context = ContextManager.getContext();
				context.setCommentMark(args[++i]);
			} else if ("-h".equals(args[i])) {
				System.out.println(getHelpString());
			} else {
				i++;
			}
		}

		if (srcdir == null) {
			System.err.println("[ERROR] No srcdir set.");
			System.out.println(getHelpString());
			return;
		}
		if (destdir == null) {
			System.err.println("[ERROR] No destdir set.");
			System.out.println(getHelpString());
			return;
		}
		try {
			File src = new File(srcdir).getCanonicalFile();

			if (!src.exists()) {
				System.err.println("[Warning] [srcdir] = " + srcdir
						+ " does not exist");
				return;
			}
			File dest = new File(destdir).getCanonicalFile();

			if (!dest.exists()) {
				System.err.println("[Warning] [destdir] = " + destdir
						+ " does not exist.");
				return;
			}

			File init = new File(initfile);
			if (init.exists()) {
				System.out
						.println("loading initfile:" + init.getAbsolutePath());
				JavaPC.preprocess(init, dest);
			} else {
				System.out.println("[Warning] Fail to load initfile:"
						+ init.getAbsolutePath());
			}
			JavaPC.preprocess(src, dest);
			System.out.println("Pre compile is completed.");
		} catch (Exception e) {
			System.out.println(e.toString());
		}

	}

	/**
	 * Define a default IReport. If no setting other IReport, this will be
	 * invoked.
	 */

	private static IReport REPORT = new IReport() {
		public void report(String msg) {
			// TODO Auto-generated method stub
			System.out.println(msg);

		}
	};

	/**
	 * Set one IReport. This IReport will show some information during
	 * processing.
	 */

	public static void setReport(IReport report) {
		REPORT = report;
	}

	/**
	 * The preprocess method of the JavaPC. This method is called as a static
	 * method when a single file or a direction will be pre-compile.
	 * 
	 * @param src
	 *            the sourcefile means a single file or a direction
	 * @param dest
	 *            the destfile is where the result of pre-compile will be put
	 * @throws Exception
	 *             if an error occurred. The error include SyntaxException and
	 *             IOException
	 */

	public static void preprocess(String src, String dest) throws Exception {

		File srcfile = new File(src).getCanonicalFile();

		if (!srcfile.exists()) {
			REPORT.report("[srcdir] = " + src + " does not exist.");
			return;
		}
		File destfile = new File(dest).getCanonicalFile();

		if (!destfile.exists()) {
			REPORT.report("[destdir] = " + dest + " does not exist.");
			return;
		}
		preprocess(srcfile, destfile);
	}

	/**
	 * A OVERLOAD version of the preprocess method of the JavaPC. This method is
	 * called as a static method when a single file or a direction will be
	 * pre-compile.
	 * 
	 * @param src
	 *            the sourcefile means a single file or a direction
	 * @param dest
	 *            the destfile is where the result of pre-compile will be put
	 * @throws Exception
	 *             if an error occurred. The error include SyntaxException and
	 *             IOException
	 */

	public static void preprocess(File src, File dest) throws Exception {
		if ((src == null) || (dest == null))
			return;
		ContextManager context = ContextManager.getContext();
		context.setDestPath(dest.getAbsolutePath());
		beginProcess(src);

	}

	/**
	 * The beginProcess method of the JavaPC. This method is the real implement
	 * of the pre-compile function, it is called as a static method.
	 * 
	 * @param src
	 *            the src is a File Object which means a single file or a
	 *            direction
	 * @throws Exception
	 *             if an error occurred. The error include SyntaxException and
	 *             IOException
	 */

	private static void beginProcess(File src) throws Exception {

		ContextManager context = ContextManager.getContext();
		if (src.isDirectory()) {
			File dest = new File(context.getDestPath() + File.separator
					+ src.getName());
			dest.mkdir();
			context.setDestPath(dest.getAbsolutePath());
			File[] files = src.listFiles();
			for (int i = 0; i < files.length; i++) {
				beginProcess(files[i]);
			}
		} else {

			REPORT.report("Processing: " + src.getCanonicalPath());

			SyntaxChecker checker = new SyntaxChecker(src);
			if (!checker.check())
				return;

			if (context.exist(src.getAbsolutePath())) {
				return;
			} else {
				context.addIncludeFile(src.getAbsolutePath());
			}

			context.setNameSpaceOfCurrentFile(src.getPath());
			SourceFileReader reader = new SourceFileReader(src);
			File dest = new File(context.getDestPath() + File.separator
					+ src.getName());

			DestFileWriter writer = new DestFileWriter(dest);

			reader.openReader();
			writer.openWriter();

			Iterator it = reader.iterator();
			while (it.hasNext()) {
				String line = (String) it.next();

				TagProcessor p = null;
				if ((p = TagSelector.getTagProcessor(line)) != null) {
					// do with a status process class
					p.process(it, writer);
				} else {
					// write to destfile
					writer.writeln(line);
				}
			}
			reader.closeReader();
			writer.closeWriter();
		}
	}

	private static String getHelpString() {
		StringBuffer sb = new StringBuffer(128);
		sb.append("-s Source file or dir\r\n");
		sb.append("-d Destination file or dir\r\n");
		sb.append("-e whether export code with some params\r\n");
		sb.append("-i a init file, default param file name is global.def\r\n");
		sb.append("-m set a comment mark, default mark is //");
		return sb.toString();
	}

}
