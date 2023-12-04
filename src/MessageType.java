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

    public void setPayload(int payload) {
        this.payload = payload;
    }

    public static MessageType getMessageType(int val) {
        switch (val) {
            case 0:
                return CHOKE;

            case 1:
                return UNCHOKE;

            case 2:
                return INTERESTED;

            case 3:
                return NOTINTERESTED;

            case 4:
                return HAVE;

            case 5:
                return BITFIELD;

            case 6:
                return REQUEST;

            case 7:
                return PIECE;

            case 8:
                return ERROR;

            case 9:
                return KILL;

            case 10:
                return PAYLOAD;

            default:
                return ERROR;
        }
    }
}