package com.array64.socialCredit.api;

/**
 * An interface for social credit event listeners meant to be implemented.
 * These are meant to be registered with {@link SocialCreditSystem#addListener(SocialCreditListener)}.
 */
public interface SocialCreditListener {
    /**
     * This is called whenever the social credit score of a player is changed.
     * @param e Details about the event
     * @see SocialCreditChangeEvent
     */
    void onSocialCreditChange(SocialCreditChangeEvent e);
    /**
     * This is called whenever the social credit score of a player is loaded (when the plugin is enabled or the player joins).
     * @param e Details about the event
     * @see SocialCreditLoadEvent
     */
    void onSocialCreditLoad(SocialCreditLoadEvent e);
}
