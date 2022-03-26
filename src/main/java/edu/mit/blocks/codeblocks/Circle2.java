package edu.mit.blocks.codeblocks;

public class Circle2 {
    private final BlockConnectorShape blockConnectorShape;

    public Circle2(BlockConnectorShape blockConnectorShape) {
        this.blockConnectorShape = blockConnectorShape;
    }

    void extracted() {
        blockConnectorShape._curveTo(
                BlockConnectorShape.NORMAL_DATA_PLUG_WIDTH, 0,
                BlockConnectorShape.NORMAL_DATA_PLUG_WIDTH, BlockConnectorShape.DATA_PLUG_HEIGHT * 1 / 4,
                BlockConnectorShape.NORMAL_DATA_PLUG_WIDTH * 1 / 2, BlockConnectorShape.DATA_PLUG_HEIGHT * 1 / 2);
        blockConnectorShape._curveTo(BlockConnectorShape.NORMAL_DATA_PLUG_WIDTH, BlockConnectorShape.DATA_PLUG_HEIGHT * 3 / 4,
                BlockConnectorShape.NORMAL_DATA_PLUG_WIDTH, BlockConnectorShape.DATA_PLUG_HEIGHT,
                0, BlockConnectorShape.DATA_PLUG_HEIGHT);
    }
}