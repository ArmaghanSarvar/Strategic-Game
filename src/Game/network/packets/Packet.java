package Game.network.packets;

/**
 * Created by ASUS UX360CA on 7/9/2017.
 */
public class Packet
{
    public static enum PacketTypes {
        INVALID(-1),LOGIN(00), TILE(01),MOVE(02), PUTCASTLE(03), NEWENTITY(04), REMOVE_ENTITY(05), NEWHUMAN(06),
        TOTAL_ENTITY_REQUEST(07), ENEMY_NUMBER(12), FINISH(23), ENEMY_REQUEST(34),NEW_ENEMY(13), END_ELEMENT(14), PIC_CHANGE(16);

        private int packetID;

        private PacketTypes(int packetID) {
            this.packetID = packetID;
        }

        public int getPacketID() {
            return packetID;
        }
    }
    public static PacketTypes lookupPacket(int id)
    {
        for (PacketTypes p : PacketTypes.values())
        {
            if (p.getPacketID() == id)
            {
                return p;
            }
        }
        return PacketTypes.INVALID;
    }
    public static PacketTypes lookupPacket(String id) {
        try {
            return lookupPacket(Integer.parseInt(id));
        }
        catch (NumberFormatException ne)
        {
            return PacketTypes.INVALID;
        }
    }
    public String readData(byte[]data)
    {
        String message = new String(data).trim();
        return message.substring(2).trim();
    }
}
