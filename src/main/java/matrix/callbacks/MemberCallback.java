package matrix.callbacks;


import matrix.bot.Member;

import java.io.IOException;
import java.util.List;

public interface MemberCallback {
    void onResponse(List<Member> roomMember) throws IOException;
}
