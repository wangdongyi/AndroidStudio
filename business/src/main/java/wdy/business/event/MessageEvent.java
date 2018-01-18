package wdy.business.event;

/**
 * 作者：王东一
 * 创建时间：2017/8/24.
 */

public class MessageEvent {
    private String message;
    private Object object;
    private int waring;
    private boolean open;
    private String id;

    public MessageEvent(String message) {
        this.message = message;
    }

    public MessageEvent(String message, Object object) {
        this.message = message;
        this.object = object;
    }

    public void setWaring(int waring) {
        this.waring = waring;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public String getMessage() {
        return message;
    }

    public int getWaring() {
        return waring;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
