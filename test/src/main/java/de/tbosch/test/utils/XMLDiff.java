package de.tbosch.test.utils;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.builder.Input;
import org.xmlunit.diff.DefaultNodeMatcher;
import org.xmlunit.diff.Diff;
import org.xmlunit.diff.Difference;
import org.xmlunit.diff.ElementSelectors;

public class XMLDiff {

	public static void main(String[] args) throws Exception {
		XMLDiff diff = new XMLDiff();
		diff.printDiff(new File("/tmp/xml1.xml"), new File("/tmp/xml2.xml"));
	}

	public void printDiff(File xml1, File xml2) throws IOException {
		Diff diff = DiffBuilder//
				.compare(Input.fromString(FileUtils.readFileToString(xml1)))//
				.withTest(Input.fromString(FileUtils.readFileToString(xml2)))//
				.ignoreComments()//
				.ignoreWhitespace()//
				.withNodeMatcher(new DefaultNodeMatcher(ElementSelectors.byNameAndAllAttributes))//
				.checkForSimilar()//
				.ignoreWhitespace()//
				.build();
		for (Difference difference : diff.getDifferences()) {
			System.out.println(difference.toString());
		}
	}

}
