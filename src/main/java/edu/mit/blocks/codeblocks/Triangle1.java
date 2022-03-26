package edu.mit.blocks.codeblocks;

import static edu.mit.blocks.codeblocks.BlockConnectorShape.DATA_PLUG_HEIGHT;
import static edu.mit.blocks.codeblocks.BlockConnectorShape.NORMAL_DATA_PLUG_WIDTH;

public class Triangle1 extends TestAbstract {

    void extracted() {
        _lineTo(NORMAL_DATA_PLUG_WIDTH, DATA_PLUG_HEIGHT / 2);
        _lineTo(0, DATA_PLUG_HEIGHT);
    }

    private void _lineTo(float normalDataPlugWidth, float v) {
    }


}