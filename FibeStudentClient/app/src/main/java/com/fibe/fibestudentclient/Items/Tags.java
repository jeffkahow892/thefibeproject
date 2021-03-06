package com.fibe.fibestudentclient.Items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class Tags {

    /**
     * An array of sample (dummy) items.
     */
    public static List<TagItem> ITEMS = new ArrayList<TagItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static Map<String, TagItem> ITEM_MAP = new HashMap<String, TagItem>();

    static {
        // Add 3 sample items.
        addItem(new TagItem("1", "C"));
        addItem(new TagItem("2", "Memory Leak"));
        addItem(new TagItem("3", "Sockets"));
        addItem(new TagItem("4", "Severs"));
        addItem(new TagItem("5", "Clients"));
        addItem(new TagItem("6", "Handshake"));
        addItem(new TagItem("7", "Networking"));
        addItem(new TagItem("8", "Assignment 4"));
        addItem(new TagItem("9", "UDP"));
        addItem(new TagItem("10", "TCP/IP"));
        addItem(new TagItem("11", "Internet"));
    }

    private static void addItem(TagItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    public static String[] getSelectedTagArray() {
        List<String> sel = new ArrayList<String>();
        for (TagItem ti : ITEMS) {
            if (ti.isSelected) sel.add(ti.toString());
        }
        if (sel.size() == 0) return new String[0];
        return sel.toArray(new String[sel.size()]);
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class TagItem {
        public String id;
        public String content;
        public boolean isSelected;

        public TagItem(String id, String content) {
            this.id = id;
            this.content = content;
            isSelected = false;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
