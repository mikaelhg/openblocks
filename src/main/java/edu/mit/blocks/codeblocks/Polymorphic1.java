package edu.mit.blocks.codeblocks;

public class Polymorphic1 {
    private final BlockConnectorShape blockConnectorShape;

    public Polymorphic1(BlockConnectorShape blockConnectorShape) {
        this.blockConnectorShape = blockConnectorShape;
    }

    void extracted() {
        blockConnectorShape._curveTo(0, BlockConnectorShape.DATA_PLUG_HEIGHT / 3,
                BlockConnectorShape.POLYMORPHIC_DATA_PLUG_WIDTH / 3, BlockConnectorShape.DATA_PLUG_HEIGHT / 3,
                BlockConnectorShape.POLYMORPHIC_DATA_PLUG_WIDTH / 2, BlockConnectorShape.DATA_PLUG_HEIGHT / 4);
        blockConnectorShape._curveTo(BlockConnectorShape.POLYMORPHIC_DATA_PLUG_WIDTH * 2 / 3, BlockConnectorShape.DATA_PLUG_HEIGHT / 6,
                BlockConnectorShape.POLYMORPHIC_DATA_PLUG_WIDTH, BlockConnectorShape.DATA_PLUG_HEIGHT / 6,
                BlockConnectorShape.POLYMORPHIC_DATA_PLUG_WIDTH, BlockConnectorShape.DATA_PLUG_HEIGHT / 2);
        blockConnectorShape._curveTo(BlockConnectorShape.POLYMORPHIC_DATA_PLUG_WIDTH, BlockConnectorShape.DATA_PLUG_HEIGHT * 5 / 6,
                BlockConnectorShape.POLYMORPHIC_DATA_PLUG_WIDTH * 2 / 3, BlockConnectorShape.DATA_PLUG_HEIGHT * 5 / 6,
                BlockConnectorShape.POLYMORPHIC_DATA_PLUG_WIDTH / 2, BlockConnectorShape.DATA_PLUG_HEIGHT * 3 / 4);
        blockConnectorShape._curveTo(BlockConnectorShape.POLYMORPHIC_DATA_PLUG_WIDTH / 3, BlockConnectorShape.DATA_PLUG_HEIGHT * 2 / 3,
                0, BlockConnectorShape.DATA_PLUG_HEIGHT * 2 / 3,
                0, BlockConnectorShape.DATA_PLUG_HEIGHT);
    }
}