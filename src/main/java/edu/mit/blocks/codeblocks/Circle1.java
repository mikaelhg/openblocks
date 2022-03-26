package edu.mit.blocks.codeblocks;

public class Circle1 {
    private final BlockConnectorShape blockConnectorShape;

    public Circle1(BlockConnectorShape blockConnectorShape) {
        this.blockConnectorShape = blockConnectorShape;
    }

    void extracted() {
        blockConnectorShape._curveTo(
                (BlockConnectorShape.NORMAL_DATA_PLUG_WIDTH) * 4 / 3, 0,
                (BlockConnectorShape.NORMAL_DATA_PLUG_WIDTH) * 4 / 3, BlockConnectorShape.DATA_PLUG_HEIGHT,
                0, BlockConnectorShape.DATA_PLUG_HEIGHT);
    }
}