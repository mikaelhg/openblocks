package edu.mit.blocks.codeblocks;


public class FactoryLinkRule {
    public LinkRule getInstance(String str){

        if(str.equals("InfixRule")){
            return new InfixRule();
        }

        if(str.equals("SocketRule")){
            return new SocketRule();
        }

        return null;
    }
}
