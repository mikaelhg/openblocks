package edu.mit.blocks.controller;

import static org.junit.Assert.*;

import org.junit.Test;

public class WorkspaceControllerTest {
	@Test
	public void testLoadProjectFromPath() {
        final WorkspaceController wc = new WorkspaceController();
        wc.setLangDefFilePath("support/ardublock_def.xml");
        wc.loadFreshWorkspace();
        wc.loadProjectFromPath("src/test/resources/ardublock001.xml");
	}
}
