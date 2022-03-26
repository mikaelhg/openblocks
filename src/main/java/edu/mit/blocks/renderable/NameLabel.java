package edu.mit.blocks.renderable;

import java.awt.Color;

import edu.mit.blocks.codeblocks.BlockConnectorShape;
import edu.mit.blocks.workspace.Workspace;

/**
 * 
 * NameLabel displays the name of a RenderableBlock
 * 
 */
class NameLabel extends BlockLabel {

    private long blockID;

    public NameLabel(Workspace workspace, String initLabelText, BlockLabel.Type labelType, boolean isEditable, long blockID) {
        super(workspace, initLabelText, labelType, isEditable, blockID, true, new Color(255, 255, 225));
        this.blockID = blockID;
    }

    void update() {
        RenderableBlock renderableBlock = workspace.getEnv().getRenderableBlock(blockID);
        if (renderableBlock != null) {
            int x_axis = 0;
            int y_axis = 0;
            x_axis = checkandgetX(renderableBlock, x_axis);

            y_axis = checkandgetY(renderableBlock, y_axis);

            //Comment Label and Collapse Label take up some additional amount of space
            x_axis += renderableBlock.getControlLabelsWidth();

            //if block is collapsed keep the name label from moving
            y_axis += (renderableBlock.isCollapsed() ? BlockConnectorShape.CONTROL_PLUG_HEIGHT / 2 : 0);

            x_axis = rescale(x_axis);
            y_axis = rescale(y_axis);

            setPixelLocation(x_axis, y_axis);
        }
    }

    private int checkandgetX(RenderableBlock block, int x_axis) {
        if (block.getBlock().isCommandBlock()) {
            x_axis += 5;
        }
        if (block.getBlock().isDeclaration()) {
            x_axis += 12;
        }
        if (block.getBlock().hasPlug()) {
            x_axis += 4 + BlockConnectorShape.getConnectorDimensions(block.getBlock().getPlug()).width;
        }
        if (block.getBlock().isInfix()) {
            if (!block.getBlock().getSocketAt(0).hasBlock()) {
                x_axis += 30;
            } else {
                x_axis += block.getSocketSpaceDimension(block.getBlock().getSocketAt(0)).width;
            }

        }
        return x_axis;
    }

    private int checkandgetY(RenderableBlock block, int y_axis) {
        if (block.getBlockWidget() == null) {
            y_axis += block.getAbstractBlockArea().getBounds().height / 2;
        } else {
            y_axis += 12;
        }

        if (block.getBlock().isCommandBlock()) {
            y_axis -= 2;
        }
        if (block.getBlock().hasPageLabel() && block.getBlock().hasAfterConnector()) {
            y_axis -= BlockConnectorShape.CONTROL_PLUG_HEIGHT;
        }
        if (!block.getBlock().hasPageLabel()) {
            y_axis -= getAbstractHeight() / 2;
        }
        return y_axis;
    }

}
