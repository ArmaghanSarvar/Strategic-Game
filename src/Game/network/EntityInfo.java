package Game.network;

/**
 * Created by ASUS UX360CA on 7/10/2017.
 */
public class EntityInfo
{
    int i, j, EntityID;
    String clientName;

    public EntityInfo(int i, int j, int entityID, String clientName) {
        this.i = i;
        this.j = j;
        EntityID = entityID;
        this.clientName = clientName;
    }
}
