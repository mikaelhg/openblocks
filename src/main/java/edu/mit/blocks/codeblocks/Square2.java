package edu.mit.blocks.codeblocks;

public class Square2 {
    private final BlockConnectorShape blockConnectorShape;

    public Square2(BlockConnectorShape blockConnectorShape) {
        this.blockConnectorShape = blockConnectorShape;
    }

    void extracted() {
        blockConnectorShape._lineTo(0, BlockConnectorShape.DATA_PLUG_HEIGHT * 0.15f);
        blockConnectorShape._lineTo(BlockConnectorShape.NORMAL_DATA_PLUG_WIDTH, BlockConnectorShape.DATA_PLUG_HEIGHT * 0.15f);
        blockConnectorShape._lineTo(BlockConnectorShape.NORMAL_DATA_PLUG_WIDTH, BlockConnectorShape.DATA_PLUG_HEIGHT * 0.45f);
        blockConnectorShape._lineTo(0, BlockConnectorShape.DATA_PLUG_HEIGHT * 0.45f);
        blockConnectorShape._lineTo(0, BlockConnectorShape.DATA_PLUG_HEIGHT * 0.55f);
        blockConnectorShape._lineTo(BlockConnectorShape.NORMAL_DATA_PLUG_WIDTH, BlockConnectorShape.DATA_PLUG_HEIGHT * 0.55f);
        blockConnectorShape._lineTo(BlockConnectorShape.NORMAL_DATA_PLUG_WIDTH, BlockConnectorShape.DATA_PLUG_HEIGHT * 0.85f);
        blockConnectorShape._lineTo(0, BlockConnectorShape.DATA_PLUG_HEIGHT * 0.85f);
        blockConnectorShape._lineTo(0, BlockConnectorShape.DATA_PLUG_HEIGHT);
    }
}