public enum MessageType {
    // Protocol Messages
    CHOKE(0),
    UNCHOKE(1),
    INTERESTED(2),
    NOTINTERESTED(3),
    HAVE(4),
    BITFIELD(5),
    REQUEST(6),
    PIECE(7),

    // Status Messages
    ERROR(8),
    KILL(9),
    PAYLOAD(10);

    private int val;
    private int payload;

    MessageType(int val) {
        this.val = val;
        this.payload = -1;
    }

    MessageType(int val, int payload) {
        this.val = val;
        this.val = payload;
    }

    public int getValue() {
        return val;
    }

    public int getPayload() {
        return payload;
    }
}