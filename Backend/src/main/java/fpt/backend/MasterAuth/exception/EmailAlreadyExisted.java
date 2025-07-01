package fpt.backend.MasterAuth.exception;

public class EmailAlreadyExisted extends RuntimeException{
    public EmailAlreadyExisted(String msg){
        super(msg);
    }
}
