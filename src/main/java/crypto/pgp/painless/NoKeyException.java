package crypto.pgp.painless;

public class NoKeyException extends Exception {
    public NoKeyException(String msg){
        super(msg);
    }
}
