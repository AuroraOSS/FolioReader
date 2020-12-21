package com.folioreader.model.event;

/**
 * @author gautam chibde on 14/6/17.
 */

public class MediaOverlayHighlightStyleEvent {
    private Style style;

    public MediaOverlayHighlightStyleEvent(Style style) {
        this.style = style;
    }

    public Style getStyle() {
        return style;
    }

    public enum Style {
        UNDERLINE, BACKGROUND, DEFAULT,
    }
}