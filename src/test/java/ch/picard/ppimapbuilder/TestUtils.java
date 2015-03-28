package ch.picard.ppimapbuilder;

import ch.picard.ppimapbuilder.util.io.FileUtil;

import java.io.File;

public class TestUtils {

	private static final boolean deleteAtShutDown = false;
	private static final File baseTestOutputFolder = new File("test-output");

	public static File createTestOutputFolder(String preffix) {
		final File testFolderOutput = getTestOutputFolder(preffix + "-output-" + System.currentTimeMillis());
		testFolderOutput.mkdir();

		if(deleteAtShutDown) {
			Runtime.getRuntime().addShutdownHook(new Thread(){
				@Override
				public void run() {
					FileUtil.recursiveDelete(testFolderOutput);
				}
			});
		}

		return testFolderOutput;
	}

	public static File getBaseTestOutputFolder() {
		return baseTestOutputFolder;
	}

	public static File getTestOutputFolder(String folderName) {
		return new File(baseTestOutputFolder, folderName);
	}
}
