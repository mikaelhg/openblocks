package edu.mit.blocks.codeblocks;

public class Polymorphic2 {
    private final BlockConnectorShape blockConnectorShape;

    public Polymorphic2(BlockConnectorShape blockConnectorShape) {
        this.blockConnectorShape = blockConnectorShape;
    }

    void extracted() {
        blockConnectorShape._curveTo(0, BlockConnectorShape.DATA_PLUG_HEIGHT / 6,
                BlockConnectorShape.POLYMORPHIC_DATA_PLUG_WIDTH / 3, BlockConnectorShape.DATA_PLUG_HEIGHT / 6,
                BlockConnectorShape.POLYMORPHIC_DATA_PLUG_WIDTH / 2, BlockConnectorShape.DATA_PLUG_HEIGHT / 8);
        blockConnectorShape._curveTo(BlockConnectorShape.POLYMORPHIC_DATA_PLUG_WIDTH * 2 / 3, BlockConnectorShape.DATA_PLUG_HEIGHT / 12,
                BlockConnectorShape.POLYMORPHIC_DATA_PLUG_WIDTH, BlockConnectorShape.DATA_PLUG_HEIGHT / 12,
                BlockConnectorShape.POLYMORPHIC_DATA_PLUG_WIDTH, BlockConnectorShape.DATA_PLUG_HEIGHT / 4);
        blockConnectorShape._curveTo(BlockConnectorShape.POLYMORPHIC_DATA_PLUG_WIDTH, BlockConnectorShape.DATA_PLUG_HEIGHT * 5 / 12,
                BlockConnectorShape.POLYMORPHIC_DATA_PLUG_WIDTH * 2 / 3, BlockConnectorShape.DATA_PLUG_HEIGHT * 5 / 12,
                BlockConnectorShape.POLYMORPHIC_DATA_PLUG_WIDTH / 2, BlockConnectorShape.DATA_PLUG_HEIGHT * 3 / 8);
        blockConnectorShape._curveTo(BlockConnectorShape.POLYMORPHIC_DATA_PLUG_WIDTH / 3, BlockConnectorShape.DATA_PLUG_HEIGHT * 2 / 6,
                0, BlockConnectorShape.DATA_PLUG_HEIGHT * 2 / 6,
                0, BlockConnectorShape.DATA_PLUG_HEIGHT / 2);
        //shifted duplicate
        blockConnectorShape._curveTo(0, BlockConnectorShape.DATA_PLUG_HEIGHT * 4 / 6,
                BlockConnectorShape.POLYMORPHIC_DATA_PLUG_WIDTH / 3, BlockConnectorShape.DATA_PLUG_HEIGHT * 4 / 6,
                BlockConnectorShape.POLYMORPHIC_DATA_PLUG_WIDTH / 2, BlockConnectorShape.DATA_PLUG_HEIGHT * 5 / 8);
        blockConnectorShape._curveTo(BlockConnectorShape.POLYMORPHIC_DATA_PLUG_WIDTH * 2 / 3, BlockConnectorShape.DATA_PLUG_HEIGHT * 7 / 12,
                BlockConnectorShape.POLYMORPHIC_DATA_PLUG_WIDTH, BlockConnectorShape.DATA_PLUG_HEIGHT * 7 / 12,
                BlockConnectorShape.POLYMORPHIC_DATA_PLUG_WIDTH, BlockConnectorShape.DATA_PLUG_HEIGHT * 3 / 4);
        blockConnectorShape._curveTo(BlockConnectorShape.POLYMORPHIC_DATA_PLUG_WIDTH, BlockConnectorShape.DATA_PLUG_HEIGHT * 11 / 12,
                BlockConnectorShape.POLYMORPHIC_DATA_PLUG_WIDTH * 2 / 3, BlockConnectorShape.DATA_PLUG_HEIGHT * 11 / 12,
                BlockConnectorShape.POLYMORPHIC_DATA_PLUG_WIDTH / 2, BlockConnectorShape.DATA_PLUG_HEIGHT * 7 / 8);
        blockConnectorShape._curveTo(BlockConnectorShape.POLYMORPHIC_DATA_PLUG_WIDTH / 3, BlockConnectorShape.DATA_PLUG_HEIGHT * 5 / 6,
                0, BlockConnectorShape.DATA_PLUG_HEIGHT * 5 / 6,
                0, BlockConnectorShape.DATA_PLUG_HEIGHT);
    }
}