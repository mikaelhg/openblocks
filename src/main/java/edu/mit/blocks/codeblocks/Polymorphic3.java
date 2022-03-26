package edu.mit.blocks.codeblocks;

public class Polymorphic3 {
    private final BlockConnectorShape blockConnectorShape;

    public Polymorphic3(BlockConnectorShape blockConnectorShape) {
        this.blockConnectorShape = blockConnectorShape;
    }

    void extracted() {
        blockConnectorShape._lineTo(BlockConnectorShape.POLYMORPHIC_DATA_PLUG_WIDTH / 3, BlockConnectorShape.DATA_PLUG_HEIGHT / 8);
        blockConnectorShape._lineTo(BlockConnectorShape.POLYMORPHIC_DATA_PLUG_WIDTH / 2, BlockConnectorShape.DATA_PLUG_HEIGHT * 0.025f);
        blockConnectorShape._lineTo(BlockConnectorShape.POLYMORPHIC_DATA_PLUG_WIDTH * 3 / 4, BlockConnectorShape.DATA_PLUG_HEIGHT / 8);
        blockConnectorShape._curveTo(BlockConnectorShape.POLYMORPHIC_DATA_PLUG_WIDTH * 10 / 9, BlockConnectorShape.DATA_PLUG_HEIGHT * 0.15f,
                BlockConnectorShape.POLYMORPHIC_DATA_PLUG_WIDTH * 10 / 9, BlockConnectorShape.DATA_PLUG_HEIGHT * 0.35f,
                BlockConnectorShape.POLYMORPHIC_DATA_PLUG_WIDTH * 3 / 4, BlockConnectorShape.DATA_PLUG_HEIGHT * 3 / 8);
        blockConnectorShape._lineTo(BlockConnectorShape.POLYMORPHIC_DATA_PLUG_WIDTH / 2, BlockConnectorShape.DATA_PLUG_HEIGHT * 0.475f);
        blockConnectorShape._lineTo(BlockConnectorShape.POLYMORPHIC_DATA_PLUG_WIDTH / 3, BlockConnectorShape.DATA_PLUG_HEIGHT * 3 / 8);
        blockConnectorShape._lineTo(0, BlockConnectorShape.DATA_PLUG_HEIGHT / 2);
        //inversion
        blockConnectorShape._lineTo(-BlockConnectorShape.POLYMORPHIC_DATA_PLUG_WIDTH / 3, BlockConnectorShape.DATA_PLUG_HEIGHT / 8 + BlockConnectorShape.DATA_PLUG_HEIGHT / 2);
        blockConnectorShape._lineTo(-BlockConnectorShape.POLYMORPHIC_DATA_PLUG_WIDTH / 2, BlockConnectorShape.DATA_PLUG_HEIGHT * 0.025f + BlockConnectorShape.DATA_PLUG_HEIGHT / 2);
        blockConnectorShape._lineTo(-BlockConnectorShape.POLYMORPHIC_DATA_PLUG_WIDTH * 3 / 4, BlockConnectorShape.DATA_PLUG_HEIGHT / 8 + BlockConnectorShape.DATA_PLUG_HEIGHT / 2);
        blockConnectorShape._curveTo(-BlockConnectorShape.POLYMORPHIC_DATA_PLUG_WIDTH * 10 / 9, BlockConnectorShape.DATA_PLUG_HEIGHT * 0.15f + BlockConnectorShape.DATA_PLUG_HEIGHT / 2,
                -BlockConnectorShape.POLYMORPHIC_DATA_PLUG_WIDTH * 10 / 9, BlockConnectorShape.DATA_PLUG_HEIGHT * 0.35f + BlockConnectorShape.DATA_PLUG_HEIGHT / 2,
                -BlockConnectorShape.POLYMORPHIC_DATA_PLUG_WIDTH * 3 / 4, BlockConnectorShape.DATA_PLUG_HEIGHT * 3 / 8 + BlockConnectorShape.DATA_PLUG_HEIGHT / 2);
        blockConnectorShape._lineTo(-BlockConnectorShape.POLYMORPHIC_DATA_PLUG_WIDTH / 2, BlockConnectorShape.DATA_PLUG_HEIGHT * 0.475f + BlockConnectorShape.DATA_PLUG_HEIGHT / 2);
        blockConnectorShape._lineTo(-BlockConnectorShape.POLYMORPHIC_DATA_PLUG_WIDTH / 3, BlockConnectorShape.DATA_PLUG_HEIGHT * 3 / 8 + BlockConnectorShape.DATA_PLUG_HEIGHT / 2);
        blockConnectorShape._lineTo(0, BlockConnectorShape.DATA_PLUG_HEIGHT);
    }
}