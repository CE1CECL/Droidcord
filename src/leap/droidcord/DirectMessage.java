package leap.droidcord;

import cc.nnproject.json.*;

public class DirectMessage implements HasIcon {
	boolean isGroup;
	public String id;
	public String name;
	public String username;
	public long lastMessageID;
	public String iconID; // for groups, group ID. for users, recipient ID (not
							// DM channel ID)
	public String iconHash;

	public DirectMessage(State s, JSONObject data) {
		id = data.getString("id");
		isGroup = data.getInt("type") == 3;

		String msgIdStr = data.getString("last_message_id");
		if (msgIdStr != null) {
			lastMessageID = Long.parseLong(msgIdStr);
		} else {
			lastMessageID = Long.parseLong(id);
		}

		if (isGroup) {
			name = data.getString("name");
			iconID = id;
			iconHash = data.getString("icon", null);
		} else {
			try {
				JSONObject recipient = data.getArray("recipients").getObject(0);

				name = recipient.getString("global_name", null);
				if (name == null) {
					name = recipient.getString("username");
				} else {
					username = recipient.getString("username", null);
				}

				iconID = recipient.getString("id");
				iconHash = recipient.getString("avatar");
			} catch (Exception e) {
			}
		}
		if (name == null)
			name = "(unknown)";
	}

	static DirectMessage getById(State s, String id) {
		if (s.directMessages == null)
			return null;

		for (int c = 0; c < s.directMessages.size(); c++) {
			DirectMessage ch = (DirectMessage) s.directMessages.elementAt(c);
			if (id.equals(ch.id))
				return ch;
		}
		return null;
	}

	public String toString(State s) {
		return name;
	}

	public String getIconID() {
		return iconID;
	}

	public String getIconHash() {
		return iconHash;
	}

	public String getIconType() {
		return isGroup ? "/channel-icons/" : "/avatars/";
	}

	public void iconLoaded(State s) {
		// if (s.dmSelector != null) s.dmSelector.update(id);
	}

	public void largeIconLoaded(State s) {
	}
}