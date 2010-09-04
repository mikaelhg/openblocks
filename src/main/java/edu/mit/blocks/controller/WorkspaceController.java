package edu.mit.blocks.controller;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import edu.mit.blocks.workspace.SearchBar;
import edu.mit.blocks.workspace.SearchableContainer;
import edu.mit.blocks.workspace.Workspace;
import edu.mit.blocks.codeblocks.BlockConnectorShape;
import edu.mit.blocks.codeblocks.BlockGenus;
import edu.mit.blocks.codeblocks.BlockLinkChecker;
import edu.mit.blocks.codeblocks.CommandRule;
import edu.mit.blocks.codeblocks.SocketRule;

/**
 * @author Ricarose Roque
 */
public class WorkspaceController {

    private Element langDefRoot;
    private boolean isWorkspacePanelInitialized = false;
    protected JPanel workspacePanel;
    protected Workspace workspace;
    protected SearchBar searchBar;
    //flag to indicate if a new lang definition file has been set
    private boolean langDefDirty = true;
    //flag to indicate if a workspace has been loaded/initialized 
    private boolean workspaceLoaded = false;

    /**
     * Constructs a WorkspaceController instance that manages the
     * interaction with the codeblocks.Workspace
     *
     */
    public WorkspaceController() {
        workspace = Workspace.getInstance();
    }

    /**
     * Sets the file path for the language definition file, if the 
     * language definition file is located in
     */
    public void setLangDefFilePath(final String filePath) {
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        final DocumentBuilder builder;
        final Document doc;
        try {
            builder = factory.newDocumentBuilder();
            doc = builder.parse(new File(filePath));
            langDefRoot = doc.getDocumentElement();
            langDefDirty = true;
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads all the block genuses, properties, and link rules of 
     * a language specified in the pre-defined language def file.
     * @param root Loads the language specified in the Element root
     */
    public void loadBlockLanguage(final Element root) {
        /* MUST load shapes before genuses in order to initialize
         connectors within each block correctly */
        BlockConnectorShape.loadBlockConnectorShapes(root);

        //load genuses
        BlockGenus.loadBlockGenera(root);

        //load rules
        BlockLinkChecker.addRule(new CommandRule());
        BlockLinkChecker.addRule(new SocketRule());

        //set the dirty flag for the language definition file 
        //to false now that the lang file has been loaded
        langDefDirty = false;
    }

    /**
     * Resets the current language within the active
     * Workspace.
     *
     */
    public void resetLanguage() {
        BlockConnectorShape.resetConnectorShapeMappings();
        BlockGenus.resetAllGenuses();
        BlockLinkChecker.reset();
    }

    ////////////////////////
    // SAVING AND LOADING //
    ////////////////////////
    /**
     * Returns the save string for the entire workspace.  This includes the block workspace, any 
     * custom factories, canvas view state and position, pages
     * @return the save string for the entire workspace.
     */
    public String getSaveString() {
        final StringBuilder saveString = new StringBuilder();
        saveString.append("<?xml version=\"1.0\" encoding=\"ISO-8859\"?>");
        saveString.append("\r\n");
        saveString.append("<CODEBLOCKS>");
        saveString.append(workspace.getSaveString());
        saveString.append("</CODEBLOCKS>");
        return saveString.toString();
    }

    /**
     * Loads a fresh workspace based on the default specifications in the language 
     * definition file.  The block canvas will have no live blocks.   
     */
    public void loadFreshWorkspace() {
        if (workspaceLoaded) {
            resetWorkspace();
        }
        if (langDefDirty) {
            loadBlockLanguage(langDefRoot);
        }
        workspace.loadWorkspaceFrom(null, langDefRoot);
        workspaceLoaded = true;
    }

    /**
     * Loads the programming project from the specified file path.  
     * This method assumes that a Language Definition File has already 
     * been specified for this programming project.
     * @param path String file path of the programming project to load
     */
    public void loadProjectFromPath(final String path) {
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        final DocumentBuilder builder;
        final Document doc;
        try {
            builder = factory.newDocumentBuilder();
            doc = builder.parse(new File(path));
            final Element projectRoot = doc.getDocumentElement();
            //load the canvas (or pages and page blocks if any) blocks from the save file
            //also load drawers, or any custom drawers from file.  if no custom drawers
            //are present in root, then the default set of drawers is loaded from 
            //langDefRoot
            workspace.loadWorkspaceFrom(projectRoot, langDefRoot);
            workspaceLoaded = true;
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads the programming project specified in the projectContents String, 
     * which is associated with the language definition file contained in the 
     * specified langDefContents.  All the blocks contained in projectContents
     * must have an associted block genus defined in langDefContents.
     * 
     * If the langDefContents have any workspace settings such as pages or 
     * drawers and projectContents has workspace settings as well, the 
     * workspace settings within the projectContents will override the 
     * workspace settings in langDefContents.  
     * 
     * NOTE: The language definition contained in langDefContents does 
     * not replace the default language definition file set by: setLangDefFilePath() or 
     * setLangDefFile().
     * 
     * @param projectContents
     * @param langDefContents String XML that defines the language of
     * projectContents
     */
    public void loadProject(String projectContents, String langDefContents) {
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        final DocumentBuilder builder;
        final Document projectDoc;
        final Document langDoc;
        try {
            builder = factory.newDocumentBuilder();
            projectDoc = builder.parse(new InputSource(new StringReader(projectContents)));
            final Element projectRoot = projectDoc.getDocumentElement();
            langDoc = builder.parse(new InputSource(new StringReader(projectContents)));
            final Element langRoot = langDoc.getDocumentElement();
            if (workspaceLoaded) {
                resetWorkspace();
            }
            if (langDefContents == null) {
                loadBlockLanguage(langDefRoot);
            } else {
                loadBlockLanguage(langRoot);
            }
            workspace.loadWorkspaceFrom(projectRoot, langRoot);
            workspaceLoaded = true;
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Resets the entire workspace.  This includes all blocks, pages, drawers, and trashed blocks.  
     * Also resets the undo/redo stack.  The language (i.e. genuses and shapes) is not reset.
     */
    public void resetWorkspace() {
        //clear all pages and their drawers
        //clear all drawers and their content
        //clear all block and renderable block instances
        workspace.reset();
    }

    /**
     * This method creates and lays out the entire workspace panel with its 
     * different components.  Workspace and language data not loaded in 
     * this function.
     * Should be call only once at application startup.
     */
    private void initWorkspacePanel() {
        workspacePanel = new JPanel();
        workspacePanel.setLayout(new BorderLayout());
        workspacePanel.add(workspace, BorderLayout.CENTER);
        isWorkspacePanelInitialized = true;
    }

    /**
     * Returns the JComponent of the entire workspace. 
     * @return the JComponent of the entire workspace. 
     */
    public JComponent getWorkspacePanel() {
        if (!isWorkspacePanelInitialized) {
            initWorkspacePanel();
        }
        return workspacePanel;
    }

    /**
     * Returns a SearchBar instance capable of searching for blocks 
     * within the BlockCanvas and block drawers
     */
    public JComponent getSearchBar() {
        final SearchBar sb = new SearchBar(
                "Search blocks", "Search for blocks in the drawers and workspace", workspace);
        for (SearchableContainer con : getAllSearchableContainers()) {
            sb.addSearchableContainer(con);
        }
        return sb.getComponent();
    }

    /**
     * Returns an unmodifiable Iterable of SearchableContainers
     * @return an unmodifiable Iterable of SearchableContainers
     */
    public Iterable<SearchableContainer> getAllSearchableContainers() {
        return workspace.getAllSearchableContainers();
    }

    /**
     * Create the GUI and show it.  For thread safety, this method should be
     * invoked from the event-dispatching thread.
     */
    private void createAndShowGUI() {
        final JFrame frame = new JFrame("WorkspaceDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(100, 100, 500, 500);
        final SearchBar sb = new SearchBar("Search blocks",
                "Search for blocks in the drawers and workspace", workspace);
        for (final SearchableContainer con : this.getAllSearchableContainers()) {
            sb.addSearchableContainer(con);
        }
        final JPanel topPane = new JPanel();
        sb.getComponent().setPreferredSize(new Dimension(130, 23));
        topPane.add(sb.getComponent());
        frame.add(topPane, BorderLayout.PAGE_START);
        frame.add(this.getWorkspacePanel(), BorderLayout.CENTER);
        frame.setVisible(true);
    }

    public static void main(final String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                final WorkspaceController wc = new WorkspaceController();
                wc.setLangDefFilePath(args[0]);
                wc.loadFreshWorkspace();
                wc.createAndShowGUI();
            }
        });
    }

}
3