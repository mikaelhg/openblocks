package edu.mit.blocks.codeblocks;

public class Triangle3 {
    private final BlockConnectorShape blockConnectorShape;

    public Triangle3(BlockConnectorShape blockConnectorShape) {
        this.blockConnectorShape = blockConnectorShape;
    }

    void extracted() {
        blockConnectorShape._lineTo(BlockConnectorShape.NORMAL_DATA_PLUG_WIDTH, BlockConnectorShape.DATA_PLUG_HEIGHT / 4);
        blockConnectorShape._lineTo(0, BlockConnectorShape.DATA_PLUG_HEIGHT / 2);
        //inversion
        blockConnectorShape._lineTo(-BlockConnectorShape.NORMAL_DATA_PLUG_WIDTH, BlockConnectorShape.DATA_PLUG_HEIGHT * 3 / 4);
        blockConnectorShape._lineTo(0, BlockConnectorShape.DATA_PLUG_HEIGHT);
    }
}