package edu.mit.blocks.codeblocks;

public abstract class Mandate implements LinkRule{

    @Override
    public abstract boolean canLink(Block block1, Block block2, BlockConnector socket1, BlockConnector socket2);

    @Override
    public boolean isMandatory()
    {
        return false;
    }
}
