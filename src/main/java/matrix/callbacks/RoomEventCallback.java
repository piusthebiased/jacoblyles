package matrix.callbacks;

import matrix.bot.events.RoomEvent;

import java.io.IOException;

public interface RoomEventCallback {
    void onEventReceived(RoomEvent roomEvent) throws IOException;
}
