package com.sjcqs.rawlauncher.utils.interfaces;

import com.sjcqs.rawlauncher.items.suggestions.SuggestionList;

/**
 * Created by satyan on 8/25/17.
 */

public interface Suggestor {
    SuggestionList suggest(String input);
    void clearSuggestions();
}
