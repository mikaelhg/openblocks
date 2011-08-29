package edu.mit.blocks.workspace.typeblocking;

import java.awt.Container;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import edu.mit.blocks.renderable.BlockNode;
import edu.mit.blocks.renderable.BlockUtilities;
import edu.mit.blocks.renderable.RenderableBlock;
import edu.mit.blocks.renderable.TextualFactoryBlock;
import edu.mit.blocks.workspace.BlockCanvas;
import edu.mit.blocks.workspace.PageChangeEventManager;
import edu.mit.blocks.workspace.Workspace;
import edu.mit.blocks.workspace.WorkspaceEvent;
import edu.mit.blocks.workspace.WorkspaceWidget;
import edu.mit.blocks.codeblocks.Block;
import edu.mit.blocks.codeblocks.BlockConnector;
import edu.mit.blocks.codeblocks.BlockLink;
import edu.mit.blocks.codeblocks.BlockLinkChecker;

/**
 * The TypeBlockManager primary serves to help users drop 
 * blocks manually into the bock canvas through the keyboard.  
 * To achieve this, the TypeBlockManager commands three 
 * distinct phases: Interfacing, Searching, Dropping.
 */
public class TypeBlockManager {

    /**Directional Pad values*/
    protected static enum Direction {

        UP, DOWN, LEFT, RIGHT, ESCAPE, ENTER
    };
    /**Singleton instance of TypeBlockManager*/
    private static TypeBlockManager manager;
    /**TypeBlockmanager graphical view*/
    private AutoCompletePanel autoCompletePanel = new AutoCompletePanel();
    /**Helper Controller that manages the transition between blocks D-PAD*/
    private FocusTraversalManager focusManager;
    /**Current canvas with focus*/
    private BlockCanvas blockCanvas;
    /** plus operations string constants**/
    static final String PLUS_OPERATION_LABEL = "+";
    static final String NUMBER_PLUS_OPERATION_LABEL = "+ [number]";
    static final String TEXT_PLUS_OPERATION_LABEL = "+ [text]";
    /**empty string for labels that already exist and shouldn't be altered to user's preference**/
    static final String EMPTY_LABEL_NAME = "";
    /**quote string for string blocks**/
    static final String QUOTE_LABEL = "\"";
    JFrame frame;

    /**
     * Enables singleton instance of TypeBLockManager.  If manager
     * is enabled, computation is done over user-generated input.
     */
    public static TypeBlockManager enableTypeBlockManager(BlockCanvas component) {
        if (TypeBlockManager.manager == null) {
            TypeBlockManager.manager = new TypeBlockManager(component);
        }
        return TypeBlockManager.manager;
    }

    /**
     * Disables the TypeBlockManager.  When disabled, TypeBlockManager
     * still consumes key inputs but never proceeds to Phase 2.
     * That is, the user-generated pattern is never parsed.
     *
     */
    public static void disableTypeBlockManager() {
        TypeBlockManager.manager = null;
    }

    /**
     * TypeBlockManager Constructor
     * @requires component != null
     */
    private TypeBlockManager() {
    }

    private TypeBlockManager(BlockCanvas component) {
        // turned off the automated block placements
        KeyInputMap.enableDefaultKeyMapping(false);
        this.autoCompletePanel = new AutoCompletePanel();
        this.blockCanvas = component;
        this.focusManager = Workspace.getInstance().getFocusManager();
        blockCanvas.getCanvas().addMouseListener(focusManager);
        blockCanvas.getCanvas().addKeyListener(focusManager);
        Workspace.getInstance().addWorkspaceListener(this.focusManager);
    }

    /*----------------------------------------------------------*
     * Convenience Methods										*
    -----------------------------------------------------------*/
    /**
     * @returns true if and only if block is invalid (null or ID==-1)
     */
    private static boolean invalidBlockID(Long blockID) {
        if (blockID == null) {
            return true;
        } else if (blockID.equals(Block.NULL)) {
            return true;
        } else {
            return false;
        }
    }

    private static boolean isNullBlockInstance(Long blockID) {
        if (blockID == null) {
            return true;
        } else if (blockID.equals(Block.NULL)) {
            return true;
        } else if (Block.getBlock(blockID) == null) {
            return true;
        } else if (Block.getBlock(blockID).getBlockID() == null) {
            return true;
        } else if (Block.getBlock(blockID).getBlockID().equals(Block.NULL)) {
            return true;
        } else if (RenderableBlock.getRenderableBlock(blockID) == null) {
            return true;
        } else if (RenderableBlock.getRenderableBlock(blockID).getBlockID() == null) {
            return true;
        } else if (RenderableBlock.getRenderableBlock(blockID).getBlockID().equals(Block.NULL)) {
            return true;
        } else {
            return false;
        }
    }

    ///////////////////////
    //Automation Handlers//
    ///////////////////////
    /**
     * @requires the current block with focus must exist with non-null
     * 			 ID in a non-null widget with a non-null parent
     * @modifies the current block with focus
     * @effects  removes the current block with focus and all
     * 			 its children from the GUI and destroys the link
     * 			 between the block with focus and it's parent
     * 			 block if one exists
     */
    protected static void automateBlockDeletion() {
        if (TypeBlockManager.manager == null) {
            System.err.println("AutoMateBlockDeletion invoked but typeBlockManager is disabled.");
            return;
        } else {
            if (!isNullBlockInstance(TypeBlockManager.manager.focusManager.getFocusBlockID())) {
                TypeBlockManager.manager.deleteBlockAndChildren();
                PageChangeEventManager.notifyListeners();
            }
        }
    }

    /**
     * @requires the current block with focus must exist with non-null
     * 			 ID in a non-null widget with a non-null parent
     * @modifies the current block with focus
     * @effects  removes the current block with focus and children
     * 			 from the GUI and destroys the link
     * 			 between the block with focus and it's parent
     * 			 block if one exist and children blocks
     * 			 if it has childrens.
     */
    private void deleteBlockAndChildren() {
//		====================>>>>>>>>>>>>>>>>>>>>>>>>>
//		====================focus coming in>>>>>>>>>>TODO
//		====================>>>>>>>>>>>>>>>>>>>>>>>>>

        //Do not delete null block references.  Otherwise, get Block and RenderableBlock instances.
        if (isNullBlockInstance(focusManager.getFocusBlockID())) {
            throw new RuntimeException("TypeBlockManager: deleting a null block references.");
        }
        Block block = Block.getBlock(focusManager.getFocusBlockID());
        RenderableBlock renderable = RenderableBlock.getRenderableBlock(block.getBlockID());

        //get workspace widget associated with current focus
        WorkspaceWidget widget = renderable.getParentWidget();
        //do not delete block instances in null widgets
        if (widget == null) {
            throw new RuntimeException("TypeBlockManager: do not delete blocks with no parent widget.");
            //return;
        }
        //get parent container of this graphical representation
        Container container = renderable.getParent();
        //do not delete block instances in null parents
        if (container == null) {
            throw new RuntimeException("TypeBlockManager: do not delete blocks with no parent container.");
            //return;
        }
        //get the Block's location on the canvas
        Point location = SwingUtilities.convertPoint(
                renderable, new Point(0, 0), this.blockCanvas.getCanvas());

        //for every valid and active connection, disconnect it.
        Long parentID = null;
        if (validConnection(block.getPlug())) {
            parentID = block.getPlugBlockID();
            this.disconnectBlock(block, widget);
            if (validConnection(block.getAfterConnector())) {
                disconnectBlock(Block.getBlock(block.getAfterBlockID()), widget);
            }
        } else if (validConnection(block.getBeforeConnector())) {
            parentID = block.getBeforeBlockID();
            BlockConnector parentConnectorToBlock = Block.getBlock(parentID).getConnectorTo(block.getBlockID());
            this.disconnectBlock(block, widget);
            if (validConnection(block.getAfterConnector())) {
                Long afterBlockID = block.getAfterBlockID();
                disconnectBlock(Block.getBlock(afterBlockID), widget);
                if (parentID != null) {
                    BlockLink link = BlockLinkChecker.canLink(
                            Block.getBlock(parentID),
                            Block.getBlock(afterBlockID),
                            parentConnectorToBlock,
                            Block.getBlock(afterBlockID).getBeforeConnector());
                    if (link != null) {
                        link.connect();
                        Workspace.getInstance().notifyListeners(new WorkspaceEvent(
                                RenderableBlock.getRenderableBlock(link.getPlugBlockID()).getParentWidget(),
                                link, WorkspaceEvent.BLOCKS_CONNECTED));
                        RenderableBlock.getRenderableBlock(link.getPlugBlockID()).repaintBlock();
                        RenderableBlock.getRenderableBlock(link.getPlugBlockID()).repaint();
                        RenderableBlock.getRenderableBlock(link.getPlugBlockID()).moveConnectedBlocks();
                        RenderableBlock.getRenderableBlock(link.getSocketBlockID()).repaintBlock();
                        RenderableBlock.getRenderableBlock(link.getSocketBlockID()).repaint();

                    }
                }
            }
        } else if (validConnection(block.getAfterConnector())) {
            parentID = block.getAfterBlockID();
        }

        //remove form widget and container
        this.removeChildrenBlock(renderable, widget, container);

//		<<<<<<<<<<<<<<<<<<<<<<<<<<==========================
//		<<<<<<<<<<<<<<<<<<<<<<<<<<focus changing, coming out TODO
//		<<<<<<<<<<<<<<<<<<<<<<<<<<==========================
        //If the deleted block had a parent, give the parent the focus,
        //Otherwise, give the focus to the canvas (NOT BLOCK CANVAS)
        if (invalidBlockID(parentID)) {
            this.focusManager.setFocus(location, Block.NULL);
            this.blockCanvas.getCanvas().requestFocus();
            return;
        } else {
            this.focusManager.setFocus(parentID);
            this.blockCanvas.getCanvas().requestFocus();
            return;
        }
    }

    private void removeChildrenBlock(RenderableBlock renderable, WorkspaceWidget widget, Container container) {
        widget.removeBlock(renderable);
        container.remove(renderable);
        container.validate();
        container.repaint();
        renderable.setParentWidget(null);
        //Workspace.getInstance().notifyListeners(new WorkspaceEvent(widget, renderable.getBlockID(), WorkspaceEvent.BLOCK_REMOVED));
        for (BlockConnector child : Block.getBlock(renderable.getBlockID()).getSockets()) {
            if (child == null || child.getBlockID().equals(Block.NULL)) {
                continue;
            }
            RenderableBlock childRenderable = RenderableBlock.getRenderableBlock(child.getBlockID());
            if (childRenderable == null) {
                continue;
            }
            removeBlock(childRenderable, widget, container);
        }
        // If it is a procedure block, we want to delete the entire stack
        if (Block.getBlock(renderable.getBlockID()).isProcedureDeclBlock()) {
            if (Block.getBlock(renderable.getBlockID()).getAfterBlockID() != Block.NULL) {
                removeAfterBlock(RenderableBlock.getRenderableBlock(Block.getBlock(renderable.getBlockID()).getAfterBlockID()),
                        widget, container);
                this.disconnectBlock(Block.getBlock(Block.getBlock(renderable.getBlockID()).getAfterBlockID()), widget);
            }
        }
        if (renderable.hasComment()) {
            renderable.removeComment();
        }
        Workspace.getInstance().notifyListeners(new WorkspaceEvent(widget, renderable.getBlockID(), WorkspaceEvent.BLOCK_REMOVED));
    }

    /**
     * Helper method that recursively finds and removes all the blocks connected to the bottom of
     * this block, including this block.
     *
     * @param afterBlock - RenderableBlock we start removing at
     * @param widget - WorkspaceWidget that the block is using
     * @param container - Container the block is stored in
     *
     */
    private void removeAfterBlock(RenderableBlock afterBlock, WorkspaceWidget widget, Container container) {
        if (Block.getBlock(afterBlock.getBlockID()).getAfterBlockID() != Block.NULL) {
            removeAfterBlock(RenderableBlock.getRenderableBlock(Block.getBlock(afterBlock.getBlockID()).getAfterBlockID()),
                    widget, container);
        }
        removeChildrenBlock(afterBlock, widget, container);
    }

    /**
     * Checks if a connection is a valid and ACTIVE connection.
     *
     * @param connection - BlockConnector in question
     *
     * @requires none
     * @return true if and only if connection != null && connection.hasBlock() == true
     */
    private boolean validConnection(BlockConnector connection) {
        if (connection != null) {
            Long blockID = connection.getBlockID();
            if (!isNullBlockInstance(blockID)) {
                if (connection.hasBlock()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     *
     * @param renderable
     * @param widget
     * @param container
     *
     * @requries renderable != null && renderable.blockID != null && renderable.blockID != Block.NULL
     * 			 && widget != null && container != null
     * @modifies renderable && children blocks connected to renderable
     * @effects removes renderable from container and widget and re-renders
     * 			renderable block, widget, and container appropriately.
     * 			Repeats for all of renderable's children.
     */
    private void removeBlock(RenderableBlock renderable, WorkspaceWidget widget, Container container) {
        widget.removeBlock(renderable);
        container.remove(renderable);
        container.validate();
        container.repaint();
        renderable.setParentWidget(null);
        //Workspace.getInstance().notifyListeners(new WorkspaceEvent(widget, renderable.getBlockID(), WorkspaceEvent.BLOCK_REMOVED));
        for (BlockConnector child : BlockLinkChecker.getSocketEquivalents(Block.getBlock(renderable.getBlockID()))) {
            if (child == null || child.getBlockID().equals(Block.NULL)) {
                continue;
            }
            RenderableBlock childRenderable = RenderableBlock.getRenderableBlock(child.getBlockID());
            if (childRenderable == null) {
                continue;
            }
            removeBlock(childRenderable, widget, container);
        }
        if (renderable.hasComment()) {
            renderable.removeComment();
        }
        Workspace.getInstance().notifyListeners(new WorkspaceEvent(widget, renderable.getBlockID(), WorkspaceEvent.BLOCK_REMOVED));
    }

    /**
     * @param childBlock
     * @param widget
     *
     * @requires widget != null
     * @modifies
     * @effects Does nothing if: childBlock is invalid (null)
     * 			Otherwise, remove childBlock from it's parent block
     * 			if the childBlock has a parent.  If it does not have
     * 			a parent, do nothing.
     */
    private void disconnectBlock(Block childBlock, WorkspaceWidget widget) {
        if (childBlock == null || invalidBlockID(childBlock.getBlockID())) {
            return;
        }
        BlockConnector childPlug = BlockLinkChecker.getPlugEquivalent(childBlock);
        if (childPlug == null || !childPlug.hasBlock() || isNullBlockInstance(childPlug.getBlockID())) {
            return;
        }
        Block parentBlock = Block.getBlock(childPlug.getBlockID());
        BlockConnector parentSocket = parentBlock.getConnectorTo(childBlock.getBlockID());
        if (parentSocket == null) {
            return;
        }
        //disconector if child connector exists and has a block connected to it
        BlockLink link = BlockLink.getBlockLink(childBlock, parentBlock, childPlug, parentSocket);
        if (link == null) {
            return;
        }

        link.disconnect();

        RenderableBlock parentRenderable = RenderableBlock.getRenderableBlock(parentBlock.getBlockID());
        if (parentRenderable == null) {
            throw new RuntimeException("INCONSISTANCY VIOLATION: "
                    + "parent block was valid, non-null, and existed.\n\tBut yet, when we get it's renderable"
                    + "representation, we recieve a null instance.\n\tIf the Block instance of an ID is non-null"
                    + "then its graphical RenderableBlock should be non-null as well");
        }
        parentRenderable.blockDisconnected(parentSocket);
        Workspace.getInstance().notifyListeners(new WorkspaceEvent(widget, link, WorkspaceEvent.BLOCKS_DISCONNECTED));
    }
    /**
     * @requires none
     * @modifies bufferedBlock (the block that is copied)
     * @effects change bufferedBlock such that it points
     * 			to the block with current focus
     */
    private BlockNode bufferedBlock = null;

    public static void copyBlock() {
        TypeBlockManager.automateCopyBlock();
    }

    public static void pasteBlock() {
        TypeBlockManager.automatePasteBlock();
    }

    protected static void automateCopyBlock() {
        if (TypeBlockManager.manager == null) {
            System.err.println("AutoMateCopyBlock invoked but typeBlockManager is disabled.");
            return;
        }
        TypeBlockManager.manager.bufferedBlock =
                BlockUtilities.makeNodeWithChildren(TypeBlockManager.manager.focusManager.getFocusBlockID());
    }

    protected static void automateCopyAll() {
        if (TypeBlockManager.manager == null) {
            System.err.println("AutoMatePasteBlock invoked but typeBlockManager is disabled.");
            return;
        }
        TypeBlockManager.manager.bufferedBlock =
                BlockUtilities.makeNodeWithStack(TypeBlockManager.manager.focusManager.getFocusBlockID());
    }

    /**
     * @requies whatever is requires for AutomatedBlockInsertion
     *
     */
    protected static void automatePasteBlock() {
        if (TypeBlockManager.manager == null) {
            System.err.println("AutoMatePasteBlock invoked but typeBlockManager is disabled.");
            return;
        }

        TypeBlockManager.manager.pasteStack(TypeBlockManager.manager.bufferedBlock);
    }

    private void pasteStack(BlockNode node) {
//		====================>>>>>>>>>>>>>>>>>>>>>>>>>
//		====================focus coming in>>>>>>>>>> TODO
//		====================>>>>>>>>>>>>>>>>>>>>>>>>>
        if (node == null) {
            return;
        }
        WorkspaceWidget widget = null;
        Iterable<WorkspaceWidget> widgets = null;
        Point spot = null;
        if (invalidBlockID(focusManager.getFocusBlockID())) {
            //canvas has focus
            Point location = SwingUtilities.convertPoint(
                    this.blockCanvas.getCanvas(),
                    this.focusManager.getCanvasPoint(),
                    Workspace.getInstance());
            widget = Workspace.getInstance().getWidgetAt(location);
            spot = SwingUtilities.convertPoint(
                    this.blockCanvas.getCanvas(),
                    this.focusManager.getCanvasPoint(),
                    widget.getJComponent());
        } else {
            RenderableBlock focusRenderable = RenderableBlock.getRenderableBlock(focusManager.getFocusBlockID());
            widget = focusRenderable.getParentWidget();
            spot = focusRenderable.getLocation();
        }

        if (widget == null) {
            // TODO: To be examined and fixed, occurs on macs
            JOptionPane.showMessageDialog(TypeBlockManager.manager.frame, "Please click somewhere on the canvas first.",
                    "Error", JOptionPane.PLAIN_MESSAGE);
            //throw new RuntimeException("Why are we adding a block to a null widget?");
        } else {
            // checks to see if the copied block still exists
            if (BlockUtilities.blockExists(node)) {
                //create mirror block and mirror childrens
                spot.translate(10, 10);
                RenderableBlock mirror = BlockUtilities.makeRenderable(node, widget);
                mirror.setLocation(spot);
                mirror.moveConnectedBlocks(); // make sure the childrens are placed correctly
            } else {
                //TODO: future version, allow them to paste
                JOptionPane.showMessageDialog(TypeBlockManager.manager.frame, "You cannot paste blocks that are currently NOT on the canvas."
                        + "\nThis function will be available in a future version.\n", "Error", JOptionPane.PLAIN_MESSAGE);
            }

        }
    }

    /**
     * Traverses the block tree structure to move
     * in the direction of the input argument.
     * @param dir
     */
    protected static void automateFocusTraversal(Direction dir) {
        if (TypeBlockManager.manager == null) {
            System.err.println("AutoMateFocusTraversal invoked but typeBlockManager is disabled.");
            return;
        } else {
            manager.traverseFocus(dir);
        }
    }

    private void traverseFocus(Direction dir) {
        if (isNullBlockInstance(focusManager.getFocusBlockID())) {
            if (dir == Direction.UP) {
                blockCanvas.getVerticalModel().setValue(blockCanvas.getVerticalModel().getValue() - 5);
            } else if (dir == Direction.DOWN) {
                blockCanvas.getVerticalModel().setValue(blockCanvas.getVerticalModel().getValue() + 5);
            } else if (dir == Direction.LEFT) {
                blockCanvas.getHorizontalModel().setValue(blockCanvas.getHorizontalModel().getValue() - 5);
            } else if (dir == Direction.RIGHT) {
                blockCanvas.getHorizontalModel().setValue(blockCanvas.getHorizontalModel().getValue() + 5);
            } else if (dir == Direction.ESCAPE) {
                //according to the focus manager, the canvas already
                //has focus. So, just request focus again.
                this.blockCanvas.getCanvas().requestFocus();
            } else if (dir == Direction.ENTER) {
            }
        } else {
            if (dir == Direction.UP) {
                focusManager.focusBeforeBlock();
            } else if (dir == Direction.DOWN) {
                focusManager.focusAfterBlock();
            } else if (dir == Direction.LEFT) {
                focusManager.focusPrevBlock();
            } else if (dir == Direction.RIGHT) {
                focusManager.focusNextBlock();
            } else if (dir == Direction.ESCAPE) {
                RenderableBlock block = RenderableBlock.getRenderableBlock(
                        focusManager.getFocusBlockID());
                Point location = SwingUtilities.convertPoint(block, new Point(0, 0), this.blockCanvas.getCanvas());
                this.focusManager.setFocus(location, Block.NULL);
                this.blockCanvas.getCanvas().requestFocus();
            } else if (dir == Direction.ENTER) {
                RenderableBlock.getRenderableBlock(focusManager.getFocusBlockID()).switchToLabelEditingMode(true);
            }
        }
    }

    /**
     * Displays an assisting AutoCompletePanel.
     * @param character
     */
    protected static void automateAutoComplete(char character) {
        if (TypeBlockManager.manager == null) {
            System.err.println("AutoMateAutoComplete invoked but typeBlockManager is disabled.");
            return;
        } else {
            manager.displayAutoCompletePanel(character);
        }
    }

    /**
     * @requires this.blockCanvas.getCanvas() != null
     * @param character
     */
    private void displayAutoCompletePanel(char character) {
//		====================>>>>>>>>>>>>>>>>>>>>>>>>>
//		====================focus coming in>>>>>>>>>> TODO
//		====================>>>>>>>>>>>>>>>>>>>>>>>>>
        if (invalidBlockID(focusManager.getFocusBlockID())) {
            //canvas has focus
            this.blockCanvas.getCanvas().add(autoCompletePanel, JLayeredPane.DRAG_LAYER);
            autoCompletePanel.setLocation(this.focusManager.getCanvasPoint());
            autoCompletePanel.setVisible(true);
            autoCompletePanel.requestFocus();
        } else {
            //renderableblock has focus
            this.blockCanvas.getCanvas().add(autoCompletePanel, JLayeredPane.DRAG_LAYER);
            RenderableBlock block = RenderableBlock.getRenderableBlock(focusManager.getFocusBlockID());
            Point location = SwingUtilities.convertPoint(
                    block,
                    this.focusManager.getBlockPoint(),
                    this.blockCanvas.getCanvas());
            location.translate(10, 10);
            autoCompletePanel.setLocation(location);
            autoCompletePanel.setVisible(true);
            autoCompletePanel.requestFocus();
        }
        autoCompletePanel.setText(String.valueOf(character));
    }

    /**
     * assumes number and differen genus exist and number genus has ediitabel lable
     */
    protected static void automateNegationInsertion() {
        if (TypeBlockManager.manager == null) {
            System.err.println("AutoMateNegationInsertion invoked but typeBlockManager is disabled.");
            return;
        }

//		====================>>>>>>>>>>>>>>>>>>>>>>>>>
//		====================focus coming in>>>>>>>>>> TODO
//		====================>>>>>>>>>>>>>>>>>>>>>>>>>

        //get focus block
        Long parentBlockID = TypeBlockManager.manager.focusManager.getFocusBlockID();
        if (isNullBlockInstance(parentBlockID)) {
            //focus on canvas
            TypeBlockManager.automateBlockInsertion("number", "-");

        } else {
            Block parentBlock = Block.getBlock(parentBlockID);
            if (parentBlock.isDataBlock()) {
                //focus on a data block
                TypeBlockManager.automateBlockInsertion("difference", null);
            } else {
                //focus on a non-data block
                TypeBlockManager.automateBlockInsertion("number", "-");
            }
        }
    }

    protected static void automateMultiplication(char character) {
        if (TypeBlockManager.manager == null) {
            System.err.println("AutoMateMultiplication invoked but typeBlockManager is disabled.");
            return;
        }
        if (!isNullBlockInstance(TypeBlockManager.manager.focusManager.getFocusBlockID())) {
            Block parentBlock = Block.getBlock(TypeBlockManager.manager.focusManager.getFocusBlockID());
            if (parentBlock.getGenusName().equals("number")) {
                TypeBlockManager.automateBlockInsertion("product", null);
                return;
            }
        }
        TypeBlockManager.automateAutoComplete(character);
        return;
    }

    protected static void automateAddition(char character) {
        if (TypeBlockManager.manager == null) {
            System.err.println("AutoMateMultiplication invoked but typeBlockManager is disabled.");
            return;
        }
        //get focus block
        Long parentBlockID = TypeBlockManager.manager.focusManager.getFocusBlockID();
        if (isNullBlockInstance(parentBlockID)) {
            //focus on canvas
            TypeBlockManager.automateBlockInsertion("sum", null);
        } else {
            Block parentBlock = Block.getBlock(parentBlockID);
            if (parentBlock.getGenusName().equals("string")) {
                //focus on string block
                TypeBlockManager.automateBlockInsertion("string-append", null);
            } else if (parentBlock.getGenusName().equals("string-append")) {
                //focus on string append block
                TypeBlockManager.automateBlockInsertion("string-append", null);
            } else {
                //focus on any other block
                TypeBlockManager.automateBlockInsertion("sum", null);
            }
        }
    }

    /**
     * @param genusName
     * @param label
     *
     * @requires if (label != null) then associated block.isLabelEditable() should return true
     * @modifies 	focusManager.focusblock &&
     * 				focusManager.focuspoint &&
     * 				blockCanvas
     * @effects Do nothing if "genusName" does not map to a valid block.
     * 			Otherwise, create and add a new block with matching genus
     * 			and label properties to one of the following:
     * 				1. the current block with focus at (0,0)
     * 				   relative to that block.
     * 				2. the current block with focus at next
     * 				   applicable socket location
     * 				3. the canvas at the last mouse click point.
     * 			Then update any focus and block connections.
     */
    protected static void automateBlockInsertion(String genusName, String label) {
        if (TypeBlockManager.manager == null) {
            System.err.println("AutoMateBlockInsertion invoked but typeBlockManager is disabled.");
            return;
        }
        //if genus is null, DO NOT insert a new block, DO NOT change the focus
        if (genusName == null) {
            return;
        }
        //get matching textual Block
        RenderableBlock createdRB = BlockUtilities.getBlock(genusName, null);
        if (createdRB == null) {
            return;
        } else {
            //change name of block IF AN DONLY IFF a label was passed
            //and the block's label was editable and the block
            //does not need to have a unique label
            if (label != null && Block.getBlock(createdRB.getBlockID()).isLabelEditable() && !Block.getBlock(createdRB.getBlockID()).labelMustBeUnique()) {
                Block.getBlock(createdRB.getBlockID()).setBlockLabel(label);
            }
            //add block
            manager.addBlock(createdRB);
        }
    }

    /**
     * @requires none
     * @modifies 	focusManager.focusblock &&
     * 				focusManager.focuspoint &&
     * 				blockCanvas
     * @effects Do nothing if "genusName" does not map to a valid block.
     * 			Otherwise, create and add a new block with matching genus
     * 			and label properties to one of the following:
     * 				1. the current block with focus at (0,0)
     * 				   relative to that block.
     * 				2. the current block with focus at next
     * 				   applicable socket location
     * 				3. the canvas at the last mouse click point.
     * 			Then update any focus and block connections.
     */
    protected static void automateBlockInsertion(TextualFactoryBlock block) {
        /*Passing in an empty label name means that the block should already have
        a predetermined label name that does not need to be altered to the user's preference*/
        automateBlockInsertion(block, EMPTY_LABEL_NAME);
    }

    /**
     * @requires none
     * @modifies 	focusManager.focusblock &&
     * 				focusManager.focuspoint &&
     * 				blockCanvas
     * @effects Do nothing if "genusName" does not map to a valid block.
     * 			Otherwise, create and add a new block with matching genus
     * 			and label properties to one of the following:
     * 				1. the current block with focus at (0,0)
     * 				   relative to that block.
     * 				2. the current block with focus at next
     * 				   applicable socket location
     * 				3. the canvas at the last mouse click point.
     * 			If label is not an empty string, then set the block label
     * 			to that string.
     * 			Then update any focus and block connections.
     */
    protected static void automateBlockInsertion(TextualFactoryBlock block, String label) {
        if (TypeBlockManager.manager == null) {
            System.err.println("AutoMateBlockInsertion invoked but typeBlockManager is disabled.");
            return;
        }
        RenderableBlock createdRB = createRenderableBlock(block);
        // sets the label of the block to whatever the user typed (should only be numbers)
        if (label != EMPTY_LABEL_NAME) {
            createdRB.getBlock().setBlockLabel(label);
        }
        // changes the plus number labels back to +
        if (label.equals(NUMBER_PLUS_OPERATION_LABEL)) {
            createdRB.getBlock().setBlockLabel(PLUS_OPERATION_LABEL);
        }
        // changes the plus text labels back to +
        if (label.equals(TEXT_PLUS_OPERATION_LABEL)) {
            createdRB.getBlock().setBlockLabel(PLUS_OPERATION_LABEL);
        }
        if (createdRB == null) {
            return;
        } else {
            manager.addBlock(createdRB);
        }
    }

    /**
     * @param block - the textual block from which a new RenderableBlock will be constructed
     *
     * @requires
     * @modifies nothing
     * @effects none
     * @return new RenderableBlock instance from the TextualFactoryBlock
     * 		   or null if not possible.
     */
    private static RenderableBlock createRenderableBlock(TextualFactoryBlock block) {
        //if textual wrapper is null, return a null instance of RenderableBlock.
        if (block == null) {
            return null;
        }
        //if FactoryBlock wrapped in textual wrapper is invalid, return null RenderableBlock instance.
        if (block.getfactoryBlock() == null || block.getfactoryBlock().getBlockID().equals(Block.NULL)) {
            return null;
        }
        //create and get the RenderableBloc instance associated with the Textual wrapper's FactoryBlock
        RenderableBlock createdRB = block.getfactoryBlock().createNewInstance();
        //if the above instance of RenderableBlock is invalid (null or points to null)
        //then DO NOT insert a new block, DO NOT change the focus.
        if (createdRB == null || isNullBlockInstance(createdRB.getBlockID())) {
            throw new RuntimeException("Invariant Violated:"
                    + "May not drop null instances of Renderable Blocks");
        }
        //Please keep the above check rep because it does not
        //make any sense to have an exisitn valid
        //FactoryRenderableBlock point to some non-existing
        //block.  In other words, why would you have a factory
        //that churns out invalid products?
        return createdRB;
    }

    /**
     * @param block
     *
     * @requires 	block must be a valid block.  That is, block may not be such that
     * 				block == null || block.getBlockID() == null ||
     *				block.getBlockID() == Block.NULL || block.getBlockID() == -1 ||
     *				Block.getBlock(block.getBlockID()) == null ||
     *				Block.getBlock(block.getBlockID()).getGenusName() == null ||
     *				Block.getBlock(block.getBlockID()).getGenusName().length() == 0 ||
     *				Block.getBlock(block.getBlockID()).getBlockLabel() == null
     * @modfies Objects modified by this method is undefined
     * @effects The effects of this method is unknown
     */
    private void addBlock(RenderableBlock block) {
        //check invariant
        if (block == null || block.getBlockID() == null
                || block.getBlockID().equals(Block.NULL)
                || Block.getBlock(block.getBlockID()) == null
                || Block.getBlock(block.getBlockID()).getGenusName() == null
                || Block.getBlock(block.getBlockID()).getGenusName().length() == 0
                || Block.getBlock(block.getBlockID()).getBlockLabel() == null) {
            throw new RuntimeException("Invariant Violated: may not pass an invalid instance of renderabel block");
        }

        //ignore default arguments
        block.ignoreDefaultArguments();
        this.blockCanvas.getCanvas().add(block, 0);
        block.setLocation(0, 0);
        Long parentBlockID = this.focusManager.getFocusBlockID();
        if (invalidBlockID(parentBlockID)) {
            new BlockDropAnimator(
                    this.focusManager.getCanvasPoint(),
                    block,
                    RenderableBlock.getRenderableBlock(parentBlockID));
        } else {
            RenderableBlock parentBlock = RenderableBlock.getRenderableBlock(parentBlockID);
            new BlockDropAnimator(
                    SwingUtilities.convertPoint(parentBlock,
                    this.focusManager.getBlockPoint(),
                    this.blockCanvas.getCanvas()),
                    block,
                    RenderableBlock.getRenderableBlock(parentBlockID));
        }
        this.focusManager.setFocus(block.getBlockID());
    }
}
