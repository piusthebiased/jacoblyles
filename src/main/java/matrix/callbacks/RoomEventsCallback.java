package matrix.callbacks;

import matrix.bot.events.RoomEvent;

import java.io.IOException;
import java.util.List;

public interface RoomEventsCallback {
    void onEventReceived(List<RoomEvent> roomEvent) throws IOException;
}
