package edu.mit.blocks.codeblocks;

public class Case3 {
    private final BlockConnectorShape blockConnectorShape;

    public Case3(BlockConnectorShape blockConnectorShape) {
        this.blockConnectorShape = blockConnectorShape;
    }

    void extracted() {
        blockConnectorShape._lineTo(0, BlockConnectorShape.DATA_PLUG_HEIGHT * 1 / 4);
        blockConnectorShape._lineTo(BlockConnectorShape.NORMAL_DATA_PLUG_WIDTH, BlockConnectorShape.DATA_PLUG_HEIGHT * 1 / 4);
        blockConnectorShape._lineTo(BlockConnectorShape.NORMAL_DATA_PLUG_WIDTH, BlockConnectorShape.DATA_PLUG_HEIGHT * 3 / 4);
        blockConnectorShape._lineTo(0, BlockConnectorShape.DATA_PLUG_HEIGHT * 3 / 4);
        blockConnectorShape._lineTo(0, BlockConnectorShape.DATA_PLUG_HEIGHT);
    }
}