package edu.mit.blocks.codeblocks;

public class CloneFactory {

    public CloneBlockInterface makeACopy(CloneBlockInterface blockInterface){
        return blockInterface.makeCopy();
    }

}